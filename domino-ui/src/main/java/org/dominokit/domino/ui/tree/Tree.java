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
import static org.dominokit.domino.ui.utils.Domino.*;

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
 * The Tree class provides a tree structure for displaying hierarchical data.
 *
 * <p>Example usage:
 *
 * <pre>
 * Tree<String> tree = Tree.create("Root");
 * TreeItem<String> item1 = TreeItem.create("Item 1", "Value 1");
 * TreeItem<String> item2 = TreeItem.create("Item 2", "Value 2");
 * tree.appendChild(item1);
 * tree.appendChild(item2);
 * </pre>
 *
 * @param <T> The type of data associated with each tree item.
 * @see BaseDominoElement
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

  /** Creates a new empty tree. */
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
   * Creates a new tree with the given title.
   *
   * @param treeTitle The title of the tree.
   */
  public Tree(String treeTitle) {
    this();
    headerElement.get().setTitle(treeTitle);
  }

  /**
   * Creates a new tree with the given title and associated data value.
   *
   * @param treeTitle The title of the tree.
   * @param value The data value associated with the tree.
   */
  public Tree(String treeTitle, T value) {
    this(treeTitle);
    this.value = value;
  }

  /**
   * Creates a new instance of Tree with the specified title and associated data value.
   *
   * @param title The title of the tree.
   * @param value The data value associated with the tree.
   * @param <T> The type of data associated with each tree item.
   * @return A new Tree instance.
   */
  public static <T> Tree<T> create(String title, T value) {
    return new Tree<>(title, value);
  }

  /**
   * Creates a new instance of Tree with the specified title.
   *
   * @param title The title of the tree.
   * @param <T> The type of data associated with each tree item.
   * @return A new Tree instance.
   */
  public static <T> Tree<T> create(String title) {
    return new Tree<>(title);
  }

  /**
   * Creates a new empty instance of Tree.
   *
   * @param <T> The type of data associated with each tree item.
   * @return A new Tree instance.
   */
  public static <T> Tree<T> create() {
    return new Tree<>();
  }

  @Override
  public HTMLElement getAppendTarget() {
    return subTree.element();
  }

  /**
   * Appends a child tree item to this tree.
   *
   * @param treeItem The tree item to append.
   * @return This Tree instance for method chaining.
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

  public Tree<T> appendChild(TreeItem<T>... treeItems) {
    Arrays.stream(treeItems).forEach(this::appendChild);
    return this;
  }

  /**
   * Sets a custom icon supplier for tree items in this tree. The icon supplier provides icons for
   * each tree item based on its content.
   *
   * @param iconSupplier The custom icon supplier to set.
   * @return This {@code Tree} instance for method chaining.
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

  /**
   * Gets the custom icon supplier set for this tree. The icon supplier provides icons for each tree
   * item based on its content.
   *
   * @return The custom icon supplier for tree items, or {@code null} if not set.
   */
  TreeItemIconSupplier<T> getIconSupplier() {
    return iconSupplier;
  }

  /**
   * Appends a separator to this tree.
   *
   * @return This Tree instance for method chaining.
   */
  public Tree<T> addSeparator() {
    appendChild(Separator.create());
    return this;
  }

  /**
   * Sets the toggle target for this tree.
   *
   * @param toggleTarget The toggle target to set.
   * @return This `Tree` instance for method chaining.
   */
  public Tree<T> setToggleTarget(ToggleTarget toggleTarget) {
    if (nonNull(toggleTarget)) {
      childItems.forEach(item -> item.setToggleTarget(toggleTarget));
      this.toggleTarget = toggleTarget;
    }
    return this;
  }

  /**
   * Gets the currently active tree item in this tree.
   *
   * @return The currently active tree item, or {@code null} if none is active.
   */
  public TreeItem<T> getActiveItem() {
    return activeItem;
  }

  /**
   * Sets the currently active tree item in this tree. The tree item will be activated, and any
   * previously active item will be deactivated.
   *
   * @param activeItem The tree item to set as active.
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
   * Sets the currently active tree item in this tree with an option to suppress selection events.
   * The tree item will be activated, and any previously active item will be deactivated.
   *
   * @param activeItem The tree item to set as active.
   * @param silent {@code true} to suppress selection events, {@code false} otherwise.
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

  /**
   * Gets the header of this tree.
   *
   * @return The `TreeHeader` of this tree.
   */
  public TreeHeader getHeader() {
    return headerElement.get();
  }

  /**
   * Gets the sub-tree element of this tree.
   *
   * @return The sub-tree element.
   */
  public UListElement getSubTree() {
    return subTree;
  }

  /**
   * Gets the title element of this tree's header.
   *
   * @return The title element.
   */
  public SpanElement getTitle() {
    return headerElement.get().getTitle();
  }

  /**
   * Sets whether this tree is searchable.
   *
   * @param searchable `true` to enable search functionality, `false` otherwise.
   * @return This `Tree` instance for method chaining.
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
   * Sets whether this tree is foldable.
   *
   * @param foldingEnabled `true` to enable folding functionality, `false` otherwise.
   * @return This `Tree` instance for method chaining.
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

  /** Expands all tree items in this tree. */
  public void expandAll() {
    getChildNodes().forEach(TreeItem::expandAll);
  }

  /** Collapses all tree items in this tree. */
  public void collapseAll() {
    getChildNodes().forEach(TreeItem::collapseAll);
  }

  /** Deactivates all tree items in this tree, GUI effect only. */
  public void deactivateAll() {
    getChildNodes().forEach(subItem -> subItem.deactivate(autoCollapse));
  }

  /**
   * Enables automatic expansion of found tree items when using the search feature.
   *
   * @return This tree instance to allow method chaining.
   */
  public Tree<T> autoExpandFound() {
    this.autoExpandFound = true;
    return this;
  }

  /**
   * Checks if automatic expansion of found tree items is enabled when using the search feature.
   *
   * @return {@code true} if automatic expansion is enabled, {@code false} otherwise.
   */
  public boolean isAutoExpandFound() {
    return autoExpandFound;
  }

  /**
   * Sets whether to auto-expand found items in the tree.
   *
   * @param autoExpandFound `true` to auto-expand found items, `false` otherwise.
   * @return This `Tree` instance for method chaining.
   */
  public Tree<T> setAutoExpandFound(boolean autoExpandFound) {
    this.autoExpandFound = autoExpandFound;
    return this;
  }

  /** Clears the search filter applied to tree items in this tree. */
  public void clearFilter() {
    childItems.forEach(TreeItem::clearFilter);
  }

  /**
   * Returns a reference to the root tree within which this tree is contained. Since a tree is
   * self-contained and typically not nested within other trees, this method returns a reference to
   * the current tree instance.
   *
   * @return A reference to the current tree instance.
   */
  public Tree<T> getTreeRoot() {
    return this;
  }

  /**
   * Sets whether this tree should automatically collapse when a new item is selected.
   *
   * @param autoCollapse `true` to automatically collapse the tree, `false` otherwise.
   * @return This `Tree` instance for method chaining.
   */
  public Tree<T> setAutoCollapse(boolean autoCollapse) {
    this.autoCollapse = autoCollapse;
    return this;
  }

  /**
   * Sets the title of this tree.
   *
   * @param title The title to set.
   * @return This `Tree` instance for method chaining.
   */
  public Tree<T> setTitle(String title) {
    headerElement.get().setTitle(title);
    return this;
  }

  /**
   * Sets the icon for this tree.
   *
   * @param icon The icon to set.
   * @return This `Tree` instance for method chaining.
   */
  public Tree<T> setIcon(Icon<?> icon) {
    headerElement.get().setIcon(icon);
    return this;
  }

  /**
   * Checks whether this tree is set to auto-collapse when a new item is selected.
   *
   * @return `true` if auto-collapse is enabled, `false` otherwise.
   */
  public boolean isAutoCollapse() {
    return autoCollapse;
  }

  /** @return the children of this item */
  public List<TreeItem<T>> getSubItems() {
    return getChildNodes();
  }

  /**
   * Gets the search input field associated with this tree if it is searchable.
   *
   * @return An `Optional` containing the search input field, or empty if not searchable.
   */
  public Optional<Search> getSearch() {
    if (nonNull(search) && search.isInitialized()) {
      return Optional.ofNullable(search.get());
    }
    return Optional.empty();
  }

  /**
   * Gets the search icon associated with this tree if it is searchable.
   *
   * @return An `Optional` containing the search icon, or empty if not searchable.
   */
  public Optional<PostfixAddOn<?>> getSearchIcon() {
    if (nonNull(searchIcon) && search.isInitialized()) {
      return Optional.of(searchIcon.get());
    }
    return Optional.empty();
  }

  /**
   * Gets the collapse/expand all icon associated with this tree if it is foldable.
   *
   * @return An `Optional` containing the collapse/expand all icon, or empty if not foldable.
   */
  public Optional<PostfixAddOn<?>> getCollapseExpandAllIcon() {
    if (nonNull(collapseExpandAllIcon) && collapseExpandAllIcon.isInitialized()) {
      return Optional.of(collapseExpandAllIcon.get());
    }
    return Optional.empty();
  }

  /**
   * Gets the value associated with this tree.
   *
   * @return The value associated with this tree.
   */
  public T getValue() {
    return value;
  }

  /**
   * Sets the value associated with this tree.
   *
   * @param value The value to set.
   */
  public void setValue(T value) {
    this.value = value;
  }

  /**
   * Gets a list of active tree items in the path from the root to the currently active item.
   *
   * @return A list of active tree items.
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
   * Gets a list of values associated with active tree items in the path from the root to the
   * currently active item.
   *
   * @return A list of values associated with active tree items.
   */
  public List<T> getActivePathValues() {
    List<T> activeValues = getBubblingPathValues(activeItem);

    Collections.reverse(activeValues);

    return activeValues;
  }

  /**
   * Removes the specified tree item from this tree. This method removes the tree item from the list
   * of sub-items and calls the {@link TreeItem#remove()} method on the item to detach it from the
   * DOM.
   *
   * @param item The tree item to be removed.
   */
  public void removeItem(TreeItem<T> item) {
    removeChild((TreeNode) item);
  }

  /** Clear all direct children of the tree, effectively reset the tree */
  public void clear() {
    setActiveItem(null);

    childItems.forEach(TreeItem::remove);
    childItems.clear();
  }

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
   * Sets a filter for tree items in this tree.
   *
   * @param filter The filter to set.
   * @return This `Tree` instance for method chaining.
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

  /**
   * Gets the current filter used for searching within the tree.
   *
   * @return The current filter applied to the tree items for searching.
   */
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

  /**
   * Sets the collapse strategy for all tree items in this tree.
   *
   * @param collapseStrategy The collapse strategy to set.
   * @return This `Tree` instance for method chaining.
   */
  public Tree<T> setCollapseStrategy(CollapseStrategy collapseStrategy) {
    getChildNodes().forEach(tTreeItem -> setCollapseStrategy(collapseStrategy));
    this.collapseStrategy = collapseStrategy;
    return this;
  }

  /**
   * Gets the collapse strategy set for this tree.
   *
   * @return The collapse strategy.
   */
  public CollapseStrategy getCollapseStrategy() {
    return collapseStrategy;
  }

  /**
   * Configures the header of this tree using a `ChildHandler`.
   *
   * @param handler The `ChildHandler` to configure the header.
   * @return This `Tree` instance for method chaining.
   */
  public Tree<T> withHeader(ChildHandler<Tree<T>, TreeHeader> handler) {
    handler.apply(this, headerElement.get());
    return this;
  }
  /**
   * Pauses the selection listeners of the tree, preventing them from reacting to selection events.
   *
   * @return The current tree instance with selection listeners paused or resumed based on the
   *     toggle value.
   */
  @Override
  public Tree<T> pauseSelectionListeners() {
    this.selectionListenersPaused = true;
    return this;
  }

  /**
   * Resumes the paused selection listeners of the tree, allowing them to react to selection events.
   *
   * @return The current tree instance with selection listeners resumed.
   */
  @Override
  public Tree<T> resumeSelectionListeners() {
    this.selectionListenersPaused = false;
    return this;
  }

  /**
   * Toggles the pause state of selection listeners of the tree.
   *
   * @param toggle {@code true} to pause the listeners, {@code false} to resume them.
   * @return The current tree instance with selection listeners paused or resumed based on the
   *     toggle value.
   */
  @Override
  public Tree<T> togglePauseSelectionListeners(boolean toggle) {
    this.selectionListenersPaused = toggle;
    return this;
  }

  /**
   * Gets the set of selection listeners registered with the tree.
   *
   * @return A set containing selection listeners.
   */
  @Override
  public Set<SelectionListener<? super TreeItem<T>, ? super TreeItem<T>>> getSelectionListeners() {
    return this.selectionListeners;
  }

  /**
   * Gets the set of deselection listeners registered with the tree.
   *
   * @return A set containing deselection listeners.
   */
  @Override
  public Set<SelectionListener<? super TreeItem<T>, ? super TreeItem<T>>>
      getDeselectionListeners() {
    return this.deselectionListeners;
  }

  /**
   * Checks if the selection listeners of the tree are currently paused.
   *
   * @return {@code true} if selection listeners are paused, {@code false} otherwise.
   */
  @Override
  public boolean isSelectionListenersPaused() {
    return this.selectionListenersPaused;
  }

  /**
   * Triggers selection listeners with the provided source and selection tree items.
   *
   * @param source The source tree item that triggered the selection.
   * @param selection The selected tree item.
   * @return The current tree instance with selection listeners triggered.
   */
  @Override
  public Tree<T> triggerSelectionListeners(TreeItem<T> source, TreeItem<T> selection) {
    if (!this.selectionListenersPaused) {
      this.selectionListeners.forEach(
          listener -> listener.onSelectionChanged(Optional.ofNullable(source), selection));
    }
    return this;
  }

  /**
   * Triggers deselection listeners with the provided source and deselected tree items.
   *
   * @param source The source tree item that triggered the deselection.
   * @param selection The deselected tree item.
   * @return The current tree instance with deselection listeners triggered.
   */
  @Override
  public Tree<T> triggerDeselectionListeners(TreeItem<T> source, TreeItem<T> selection) {
    if (!this.selectionListenersPaused) {
      this.deselectionListeners.forEach(
          listener -> listener.onSelectionChanged(Optional.ofNullable(source), selection));
    }
    return this;
  }

  /**
   * Gets the currently selected tree item in the tree.
   *
   * @return The currently selected tree item, or {@code null} if none is selected.
   */
  @Override
  public TreeItem<T> getSelection() {
    return this.activeItem;
  }

  /**
   * Gets the HTMLDivElement representing the tree element.
   *
   * @return The HTMLDivElement representing the tree.
   */
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

  /**
   * An interface to handle item click events in the tree.
   *
   * @param <T> The type of data associated with each tree item.
   */
  public interface ItemClickListener<T> {
    /**
     * Called when a tree item is clicked.
     *
     * @param treeItem The tree item that was clicked.
     */
    void onTreeItemClicked(TreeItem<T> treeItem);
  }

  /**
   * An interface to provide custom icons for tree items.
   *
   * @param <T> The type of data associated with each tree item.
   */
  public interface TreeItemIconSupplier<T> {
    /**
     * Creates an icon for the given tree item.
     *
     * @param item The tree item for which to create the icon.
     * @return The created icon.
     */
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
