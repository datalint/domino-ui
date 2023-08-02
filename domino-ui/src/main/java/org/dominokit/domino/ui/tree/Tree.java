/*
 * Copyright © 2019 Dominokit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dominokit.domino.ui.tree;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import java.util.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.collapsible.CollapseStrategy;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.elements.SpanElement;
import org.dominokit.domino.ui.elements.UListElement;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.ToggleMdiIcon;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.search.Search;
import org.dominokit.domino.ui.utils.*;

/**
 * A component provides a tree representation of elements
 *
 * <p>Customize the component can be done by overwriting classes provided by {@link
 * org.dominokit.domino.ui.tree.TreeStyles}
 *
 * <p>For example:
 *
 * <pre>{@code
 * Tree<String> hardwareTree =
 *     Tree.create("HARDWARE")
 *         .setToggleTarget(ToggleTarget.ICON)
 *         .addChangeHandler((treeItem) -> DomGlobal.console.info(treeItem.getValue()))
 *         .addChangeHandler((treeItem) -> Notification.create(treeItem.getTitle()).show())
 *         .appendChild(TreeItem.create("Computer", Icons.laptop()))
 *         .appendChild(TreeItem.create("Headset", Icons.headset()))
 *         .appendChild(TreeItem.create("Keyboard", Icons.keyboard()))
 *         .appendChild(TreeItem.create("Mouse", Icons.mouse()))
 *         .addSeparator()
 *         .appendChild(TreeItem.create("Laptop", Icons.laptop()))
 *         .appendChild(TreeItem.create("Smart phone", Icons.cellphone()))
 *         .appendChild(TreeItem.create("Tablet", Icons.tablet()))
 *         .appendChild(TreeItem.create("Speaker", Icons.speaker()));
 * }</pre>
 *
 * @param <T> the type of the object
 * @see BaseDominoElement
 * @author vegegoku
 * @version $Id: $Id
 */
public class Tree<T> extends BaseDominoElement<HTMLDivElement, Tree<T>>
    implements TreeNode,
        HasChangeListeners<Tree<T>, TreeItem<T>>,
        IsElement<HTMLDivElement>,
        TreeStyles,
        HasSelectionListeners<Tree<T>, TreeItem<T>, TreeItem<T>> {
  public static final Predicate<TreeNode> treeItemOnly = node -> node instanceof TreeItem;

  private ToggleTarget toggleTarget = ToggleTarget.ANY;

  // Used by filter method based on a searcht token primary provided by a user input, can be a
  // relaxed search like case-insensitive
  private Function<String, Predicate<TreeNode>> filter;

  // Used primary by a programma, based on <T> value test, hence corresponding with the data model
  private Function<T, Predicate<TreeNode>> finder;

  /* The real active tree item, not necessary being a direct child but well a descendant of the tree */
  private TreeItem<T> activeItem;

  private boolean autoCollapse = true;
  private final List<TreeItem<T>> childItems = new ArrayList<>();
  private boolean autoExpandFound;
  private LazyChild<Search> search;
  private LazyChild<PostfixAddOn<?>> collapseExpandAllIcon;

  private T value;

  private final Set<ChangeListener<? super TreeItem<T>>> changeHandlers = new HashSet<>();
  private CollapseStrategy collapseStrategy;

  private DivElement element;
  private DivElement bodyElement;
  private UListElement subTree;
  private LazyChild<TreeHeader> headerElement;
  private LazyChild<PostfixAddOn<?>> searchIcon;
  private TreeItemIconSupplier<T> iconSupplier;
  private boolean selectionListenersPaused;
  private final Set<SelectionListener<? super TreeItem<T>, ? super TreeItem<T>>>
      selectionListeners = new HashSet<>();
  private final Set<SelectionListener<? super TreeItem<T>, ? super TreeItem<T>>>
      deselectionListeners = new HashSet<>();

  /** Constructor for Tree. */
  public Tree() {
    element =
        div()
            .addCss(dui_tree)
            .appendChild(
                bodyElement =
                    div().addCss(dui_tree_body).appendChild(subTree = ul().addCss(dui_tree_nav)));
    headerElement = LazyChild.of(TreeHeader.create(), element);
    init(this);
  }

  /**
   * Constructor for Tree.
   *
   * @param treeTitle a {@link java.lang.String} object
   */
  public Tree(String treeTitle) {
    this();
    headerElement.get().setTitle(treeTitle);
  }

  /**
   * Constructor for Tree.
   *
   * @param treeTitle a {@link java.lang.String} object
   * @param value a T object
   */
  public Tree(String treeTitle, T value) {
    this(treeTitle);
    this.value = value;
  }

  /**
   * create.
   *
   * @param title the title of the tree
   * @param value the default selected value
   * @param <T> the type of the object
   * @return new instance
   */
  public static <T> Tree<T> create(String title, T value) {
    return new Tree<>(title, value);
  }

  /**
   * create.
   *
   * @param title the default selected value
   * @param <T> the type of the object
   * @return new instance
   */
  public static <T> Tree<T> create(String title) {
    return new Tree<>(title);
  }

  /**
   * create.
   *
   * @param <T> the type of the object
   * @return new instance
   */
  public static <T> Tree<T> create() {
    return new Tree<>();
  }

  /** {@inheritDoc} */
  @Override
  public HTMLElement getAppendTarget() {
    return subTree.element();
  }

  /**
   * Adds a new tree item
   *
   * @param treeItem a new {@link org.dominokit.domino.ui.tree.TreeItem}
   * @return same instance
   */
  public Tree<T> appendChild(TreeItem<T> treeItem) {
    appendChild((TreeNode) treeItem);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public TreeNode appendChild(TreeNode node) {
    TreeItem<T> treeItem = (TreeItem<T>) node;
    super.appendChild(treeItem.element());
    treeItem.setParent(this);
    treeItem.setToggleTarget(this.toggleTarget);
    if (nonNull(collapseStrategy)) {
      treeItem.setCollapseStrategy(collapseStrategy);
    }
    this.childItems.add(treeItem);
    if (nonNull(iconSupplier)) {
      treeItem.onSuppliedIconChanged(iconSupplier);
    }
    return node;
  }

  /**
   * setTreeItemIconSupplier.
   *
   * @param iconSupplier a {@link org.dominokit.domino.ui.tree.Tree.TreeItemIconSupplier} object
   * @return a {@link org.dominokit.domino.ui.tree.Tree} object
   */
  public Tree<T> setTreeItemIconSupplier(TreeItemIconSupplier<T> iconSupplier) {
    this.iconSupplier = iconSupplier;
    if (nonNull(this.iconSupplier)) {
      childItems.forEach(
          item -> {
            item.onSuppliedIconChanged(iconSupplier);
          });
    }
    return this;
  }

  TreeItemIconSupplier<T> getIconSupplier() {
    return iconSupplier;
  }

  /**
   * Adds a new separator
   *
   * @return same instance
   */
  public Tree<T> addSeparator() {
    appendChild(Separator.create());
    return this;
  }

  /**
   * Sets what is the target for toggling an item
   *
   * @param toggleTarget the {@link org.dominokit.domino.ui.tree.ToggleTarget}
   * @return same instance
   */
  public Tree<T> setToggleTarget(ToggleTarget toggleTarget) {
    if (nonNull(toggleTarget)) {
      childItems.forEach(item -> item.setToggleTarget(toggleTarget));
      this.toggleTarget = toggleTarget;
    }
    return this;
  }

  /** @return the current active tree item */
  public TreeItem<T> getActiveItem() {
    return activeItem;
  }

  /**
   * Activates the given tree item and notify change handlers
   *
   * @param activeItem the tree item to activate
   */
  public void setActiveItem(TreeItem<T> activeItem) {
    setActiveItem(activeItem, false);
  }

  /**
   * Activates the given tree item and do not notify change handlers
   *
   * @param activeItem the tree item to activate
   */
  public void setActiveItemSilent(TreeItem<T> activeItem) {
    setActiveItem(activeItem, true);
  }

  /**
   * Activates the given tree item
   *
   * @param activeItem the tree item to activate
   * @param silent true to not notify change handlers
   */
  public void setActiveItem(TreeItem<T> activeItem, boolean silent) {
    // The contains operation is not free, check it only by debug mode when assert is enabled.
    assert activeItem == null || contains((TreeNode) activeItem);

    if (Objects.equals(this.activeItem, activeItem)) return;

    List<TreeItem<T>> oldItems = getBubblingPath(this.activeItem);
    List<TreeItem<T>> newItems = getBubblingPath(activeItem);
    this.activeItem = activeItem;
    int indexOld = -1;
    int indexNew;
    for (indexNew = 0; indexNew < newItems.size(); indexNew++) {
      indexOld = oldItems.indexOf(newItems.get(indexNew));

      // There is a match between old items and new items, which means that you only need to
      // deactivate old items before the found index.
      if (indexOld >= 0) break;
    }

    if (oldItems.size() > 0 && indexOld != 0) {
      oldItems.get((indexOld > 0 ? indexOld : oldItems.size()) - 1).deactivate(autoCollapse);
    }

    for (int i = 0; i < indexNew; i++) {
      newItems.get(i).activate();
    }

    if (!silent) {
      // changeHandlers.forEach(changeHandler -> changeHandler.onValueChanged(this.activeItem));
    }
  }

  /**
   * Reset the current active item to null
   *
   * @param silent true to not notify change handlers
   */
  public void resetActiveItem(boolean silent) {
    if (activeItem == null) return;

    setActiveItem(null, silent);
  }

  /** Reset the current active item to null without notify change handlers */
  public void resetActiveItemSilent() {
    resetActiveItem(true);
  }

  /**
   * Reset the current active to null if the given item is on the active path of the tree
   *
   * @param item item to test with
   * @param silent true to not notify change handlers
   */
  public void resetItemIfActive(TreeItem<T> item, boolean silent) {
    if (getActiveBubblingPath().contains(this)) setActiveItem(null, silent);
  }

  /** @return the header element */
  /**
   * getHeader.
   *
   * @return a {@link org.dominokit.domino.ui.tree.TreeHeader} object
   */
  public TreeHeader getHeader() {
    return headerElement.get();
  }

  /** @return the root element */
  /**
   * Getter for the field <code>subTree</code>.
   *
   * @return a {@link org.dominokit.domino.ui.elements.UListElement} object
   */
  public UListElement getSubTree() {
    return subTree;
  }

  /** @return the title element */
  /**
   * getTitle.
   *
   * @return a {@link org.dominokit.domino.ui.elements.SpanElement} object
   */
  public SpanElement getTitle() {
    return headerElement.get().getTitle();
  }

  /**
   * Enables the search
   *
   * @return same instance
   * @param searchable a boolean
   */
  public Tree<T> setSearchable(boolean searchable) {
    if (searchable) {

      if (isNull(search)) {
        search =
            LazyChild.of(
                Search.create(true).onSearch(Tree.this::filter).onClose(this::clearFilter),
                headerElement);

        search.whenInitialized(
            () -> {
              search
                  .element()
                  .getInputElement()
                  .onKeyDown(
                      keyEvents -> {
                        keyEvents.onArrowDown(
                            evt -> {
                              childItems.stream()
                                  .filter(item -> !dui_hidden.isAppliedTo(item))
                                  .findFirst()
                                  .ifPresent(item -> item.getClickableElement().focus());
                            });
                      });
            });
      }

      if (isNull(searchIcon)) {
        searchIcon =
            LazyChild.of(
                PostfixAddOn.of(
                        Icons.magnify()
                            .clickable()
                            .addClickListener(
                                evt -> {
                                  evt.stopPropagation();
                                  search.get().open();
                                }))
                    .addCss(dui_tree_header_item),
                headerElement.get().getContent());
      }
      searchIcon.get();
    } else {
      if (nonNull(searchIcon)) {
        searchIcon.remove();
      }

      if (nonNull(search)) {
        search.remove();
      }
    }
    return this;
  }

  /**
   * Adds the ability to expand/collapse all items
   *
   * @return same instance
   * @param foldingEnabled a boolean
   */
  public Tree<T> setFoldable(boolean foldingEnabled) {
    if (foldingEnabled) {
      if (isNull(collapseExpandAllIcon)) {
        collapseExpandAllIcon =
            LazyChild.of(
                PostfixAddOn.of(
                        ToggleMdiIcon.create(Icons.fullscreen(), Icons.fullscreen_exit())
                            .clickable()
                            .apply(
                                self ->
                                    self.addClickListener(
                                        evt -> {
                                          evt.stopPropagation();
                                          if (self.isToggled()) {
                                            collapseAll();
                                          } else {
                                            expandAll();
                                          }
                                          self.toggle();
                                        })))
                    .addCss(dui_tree_header_item),
                headerElement.get().getContent());
      }
      collapseExpandAllIcon.get();
    } else {
      if (nonNull(collapseExpandAllIcon)) {
        collapseExpandAllIcon.remove();
      }
    }
    return this;
  }

  /** Expand all items */
  public void expandAll() {
    getChildNodes().forEach(TreeItem::expandAll);
  }

  /** Collapse all items */
  public void collapseAll() {
    getChildNodes().forEach(TreeItem::collapseAll);
  }

  /** Deactivate all items, GUI effect only */
  public void deactivateAll() {
    getChildNodes().forEach(subItem -> subItem.deactivate(autoCollapse));
  }

  /**
   * Expand the items found by the search automatically
   *
   * @return same instance
   */
  public Tree<T> autoExpandFound() {
    this.autoExpandFound = true;
    return this;
  }

  /** @return true if automatic expanding is enabled when finding items in search */
  /**
   * isAutoExpandFound.
   *
   * @return a boolean
   */
  public boolean isAutoExpandFound() {
    return autoExpandFound;
  }

  /**
   * Sets if the items found by the search should be expanded automatically
   *
   * @param autoExpandFound true to expand automatically, false otherwise
   * @return a {@link org.dominokit.domino.ui.tree.Tree} object
   */
  public Tree<T> setAutoExpandFound(boolean autoExpandFound) {
    this.autoExpandFound = autoExpandFound;
    return this;
  }

  /** Clears all the filters */
  public void clearFilter() {
    childItems.forEach(TreeItem::clearFilter);
  }

  /** @return The {@link Tree} */
  public Tree<T> getTreeRoot() {
    return this;
  }

  /**
   * Sets if item should be collapsed automatically when it is deactivated
   *
   * @param autoCollapse true to collapse automatically, false otherwise
   * @return same instance
   */
  public Tree<T> setAutoCollapse(boolean autoCollapse) {
    this.autoCollapse = autoCollapse;
    return this;
  }

  /**
   * Sets the title of the tree
   *
   * @param title the title text
   * @return same instance
   */
  public Tree<T> setTitle(String title) {
    headerElement.get().setTitle(title);
    return this;
  }

  /**
   * setIcon.
   *
   * @param icon a {@link org.dominokit.domino.ui.icons.Icon} object
   * @return a {@link org.dominokit.domino.ui.tree.Tree} object
   */
  public Tree<T> setIcon(Icon<?> icon) {
    headerElement.get().setIcon(icon);
    return this;
  }

  /** @return true if deactivated items should be collapsed automatically */
  /**
   * isAutoCollapse.
   *
   * @return a boolean
   */
  public boolean isAutoCollapse() {
    return autoCollapse;
  }

  /** @return the children of this item */
  public List<TreeItem<T>> getSubItems() {
    return getChildNodes();
  }

  /** @return the search element */
  /**
   * Getter for the field <code>search</code>.
   *
   * @return a {@link java.util.Optional} object
   */
  public Optional<Search> getSearch() {
    if (nonNull(search) && search.isInitialized()) {
      return Optional.ofNullable(search.get());
    }
    return Optional.empty();
  }

  /** @return the search icon */
  /**
   * Getter for the field <code>searchIcon</code>.
   *
   * @return a {@link java.util.Optional} object
   */
  public Optional<PostfixAddOn<?>> getSearchIcon() {
    if (nonNull(searchIcon) && search.isInitialized()) {
      return Optional.of(searchIcon.get());
    }
    return Optional.empty();
  }

  /** @return the collapse all icon */
  /**
   * Getter for the field <code>collapseExpandAllIcon</code>.
   *
   * @return a {@link java.util.Optional} object
   */
  public Optional<PostfixAddOn<?>> getCollapseExpandAllIcon() {
    if (nonNull(collapseExpandAllIcon) && collapseExpandAllIcon.isInitialized()) {
      return Optional.of(collapseExpandAllIcon.get());
    }
    return Optional.empty();
  }

  /** @return the current value */
  /**
   * Getter for the field <code>value</code>.
   *
   * @return a T object
   */
  public T getValue() {
    return value;
  }

  /**
   * Sets the value
   *
   * @param value the new value
   */
  public void setValue(T value) {
    this.value = value;
  }

  /** @return the list of the items in the current active path */
  /**
   * getActivePath.
   *
   * @return a {@link java.util.List} object
   */
  public List<TreeItem<T>> getActivePath() {
    List<TreeItem<T>> activeItems = getActiveBubblingPath();

    Collections.reverse(activeItems);

    return activeItems;
  }

  /**
   * @return the list of the items in the current active path, from inner tree item to outermost
   *     tree item
   */
  public List<TreeItem<T>> getActiveBubblingPath() {
    return getBubblingPath(activeItem);
  }

  /** @return the list of values in the current active path */
  /**
   * getActivePathValues.
   *
   * @return a {@link java.util.List} object
   */
  public List<T> getActivePathValues() {
    List<T> activeValues = getBubblingPathValues(activeItem);

    Collections.reverse(activeValues);

    return activeValues;
  }

  /** Clear all direct children of the tree, effectively reset the tree */
  public void clear() {
    setActiveItem(null);

    childItems.forEach(TreeItem::remove);
    childItems.clear();
  }

  /**
   * Removes item
   *
   * @param item the item value
   */
  public void removeItem(TreeItem<T> item) {
    removeChild((TreeNode) item);
  }

  /** {@inheritDoc} */
  @Override
  public TreeNode removeChild(TreeNode node) {
    // Remember the current active path of the tree
    List<TreeItem<T>> activePath = getActiveBubblingPath();

    if (getChildNodes().remove(node)) {
      // Update HTML DOM
      ((TreeItem<?>) node).remove();

      // Either the node being removed or one of its descendants is the active tree item of the
      // tree. Since the node is removed and its parent node is self this tree, we should announce
      // that there is no more active tree item at this moment.
      if (activePath.contains(node)) setActiveItem(null);
    }

    return node;
  }

  /**
   * Sets the filter that will be used when searching items, the default filter searches using the
   * title of the items
   *
   * @param filter a {@link org.dominokit.domino.ui.tree.TreeItemFilter}
   * @return same instance
   * @deprecated use {@link #setFilter(Function)}} instead
   */
  @Deprecated
  public Tree<T> setFilter(TreeItemFilter<TreeItem<T>> filter) {
    // Do nothing
    return this;
  }

  /**
   * Filter based on the search query
   *
   * @param searchToken the query
   */
  public void filter(String searchToken) {
    Predicate<TreeNode> predicate = createFilterPredicate(searchToken);
    childItems.forEach(treeItem -> treeItem.filter(predicate));
  }

  public Predicate<TreeNode> createFilterPredicate(String searchToken) {
    return filter == null ? createDefaultFilterPredicate(searchToken) : filter.apply(searchToken);
  }

  /**
   * Set a specific finder of the tree
   *
   * @param filter create predicates used by filter method of the tree
   * @return same instance
   */
  public Tree<T> setFilter(Function<String, Predicate<TreeNode>> filter) {
    this.filter = filter;
    return this;
  }

  /**
   * Find any descendant tree item matching the given item value
   *
   * @param value a value being searched
   * @return an {@code Optional} tree item matching the given item value
   */
  public Optional<TreeItem<T>> findAny(T value) {
    return findAny(createFinderPredicate(value));
  }

  /**
   * @param values argument values to test with
   * @return an {@code Optional} tree item with a path matching exactly the given item values
   */
  public Optional<TreeItem<T>> findExact(T... values) {
    return findExact(createPredicates(this::createFinderPredicate, values));
  }

  /** {@inheritDoc} */
  @Override
  public boolean skipped() {
    return true;
  }

  /**
   * @param value argument value to test with
   * @return a predicate, indicating if a give tree node matching the given value
   */
  public Predicate<TreeNode> createFinderPredicate(T value) {
    return finder == null ? createDefaultFinderPredicate(value) : finder.apply(value);
  }

  /**
   * Set a specific finder of the tree
   *
   * @param finder create predicates used by find methods of the tree
   * @return same instance
   */
  public Tree<T> setFinder(Function<T, Predicate<TreeNode>> finder) {
    this.finder = finder;
    return this;
  }

  /** {@inheritDoc} */
  public Tree<T> setCollapseStrategy(CollapseStrategy collapseStrategy) {
    getChildNodes().forEach(tTreeItem -> setCollapseStrategy(collapseStrategy));
    this.collapseStrategy = collapseStrategy;
    return this;
  }

  /**
   * Getter for the field <code>collapseStrategy</code>.
   *
   * @return a {@link org.dominokit.domino.ui.collapsible.CollapseStrategy} object
   */
  public CollapseStrategy getCollapseStrategy() {
    return collapseStrategy;
  }

  /**
   * withHeader.
   *
   * @param handler a {@link org.dominokit.domino.ui.utils.ChildHandler} object
   * @return a {@link org.dominokit.domino.ui.tree.Tree} object
   */
  public Tree<T> withHeader(ChildHandler<Tree<T>, TreeHeader> handler) {
    handler.apply(this, headerElement.get());
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Tree<T> pauseSelectionListeners() {
    this.selectionListenersPaused = true;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Tree<T> resumeSelectionListeners() {
    this.selectionListenersPaused = false;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Tree<T> togglePauseSelectionListeners(boolean toggle) {
    this.selectionListenersPaused = toggle;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Set<SelectionListener<? super TreeItem<T>, ? super TreeItem<T>>> getSelectionListeners() {
    return this.selectionListeners;
  }

  /** {@inheritDoc} */
  @Override
  public Set<SelectionListener<? super TreeItem<T>, ? super TreeItem<T>>>
      getDeselectionListeners() {
    return this.deselectionListeners;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSelectionListenersPaused() {
    return this.selectionListenersPaused;
  }

  /** {@inheritDoc} */
  @Override
  public Tree<T> triggerSelectionListeners(TreeItem<T> source, TreeItem<T> selection) {
    if (!this.selectionListenersPaused) {
      this.selectionListeners.forEach(
          listener -> listener.onSelectionChanged(Optional.ofNullable(source), selection));
    }
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Tree<T> triggerDeselectionListeners(TreeItem<T> source, TreeItem<T> selection) {
    if (!this.selectionListenersPaused) {
      this.deselectionListeners.forEach(
          listener -> listener.onSelectionChanged(Optional.ofNullable(source), selection));
    }
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public TreeItem<T> getSelection() {
    return this.activeItem;
  }

  /** {@inheritDoc} */
  @Override
  public HTMLDivElement element() {
    return element.element();
  }

  /** {@inheritDoc} */
  @Override
  public TreeNode getParentNode() {
    // A tree instance does not have a parent node
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public List<TreeItem<T>> getChildNodes() {
    return childItems;
  }

  @Override
  public Tree<T> pauseChangeListeners() {
    return this;
  }

  @Override
  public Tree<T> resumeChangeListeners() {
    return this;
  }

  @Override
  public Tree<T> togglePauseChangeListeners(boolean toggle) {
    return this;
  }

  @Override
  public Set<ChangeListener<? super TreeItem<T>>> getChangeListeners() {
    return changeHandlers;
  }

  @Override
  public boolean isChangeListenersPaused() {
    return false;
  }

  @Override
  public Tree<T> triggerChangeListeners(TreeItem<T> oldValue, TreeItem<T> newValue) {
    return this;
  }

  public interface TreeItemIconSupplier<T> {
    Icon<?> createIcon(TreeItem<T> item);
  }

  /**
   * @param treeNode the start item, inclusive.
   * @return the list of the items in the current active path, from inner tree item to outermost
   *     tree item
   */
  public static <T> List<TreeItem<T>> getBubblingPath(TreeNode treeNode) {
    List<TreeItem<T>> activeItems = new ArrayList<>();

    TreeNode.iterate(treeNode, treeItemOnly, node -> activeItems.add((TreeItem<T>) node));

    return activeItems;
  }

  /**
   * @param treeNode the start item, inclusive.
   * @return the list of values in the current active path, from inner tree item value to outermost
   *     tree item value
   */
  public static <T> List<T> getBubblingPathValues(TreeNode treeNode) {
    List<T> activeValues = new ArrayList<>();

    TreeNode.iterate(
        treeNode, treeItemOnly, node -> activeValues.add(((TreeItem<T>) node).getValue()));

    return activeValues;
  }

  /**
   * Create default predicates used by filter method of the tree
   *
   * @param searchToken the query to test with
   * @return a default predicate used to test given query
   */
  public static Predicate<TreeNode> createDefaultFilterPredicate(String searchToken) {
    String lowerCase = searchToken.toLowerCase();

    return treeItemOnly.and(
        node -> ((TreeItem<?>) node).getTitle().toLowerCase().contains(lowerCase));
  }

  /**
   * Create default predicates used by find methods of the tree
   *
   * @param value the value to test with
   * @return a default predicate used to test given value
   */
  public static Predicate<TreeNode> createDefaultFinderPredicate(Object value) {
    return treeItemOnly.and(node -> Objects.equals(((TreeItem<?>) node).getValue(), value));
  }
}
