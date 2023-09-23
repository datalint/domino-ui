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

import elemental2.dom.*;
import elemental2.dom.EventListener;
import java.util.*;
import java.util.function.Predicate;
import org.dominokit.domino.ui.collapsible.Collapsible;
import org.dominokit.domino.ui.config.HasComponentConfig;
import org.dominokit.domino.ui.config.TreeConfig;
import org.dominokit.domino.ui.elements.*;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.StateChangeIcon;
import org.dominokit.domino.ui.icons.ToggleIcon;
import org.dominokit.domino.ui.style.Style;
import org.dominokit.domino.ui.style.WaveStyle;
import org.dominokit.domino.ui.utils.*;

/**
 * A component representing the tree item
 *
 * @param <T> the type of the value object inside the item
 * @author vegegoku
 * @version $Id: $Id
 * @see CanActivate
 * @see CanDeactivate
 * @see HasClickableElement
 */
public class TreeItem<T> extends BaseDominoElement<HTMLLIElement, TreeItem<T>>
    implements TreeNode,
        CanActivate,
        CanDeactivate,
        HasClickableElement,
        TreeStyles,
        HasComponentConfig<TreeConfig>,
        HasSelectionListeners<TreeItem<T>, TreeItem<T>, TreeItem<T>> {

  private String title;
  private LIElement element;
  private final AnchorElement anchorElement;
  private final DivElement contentElement;
  private LazyChild<Icon<?>> itemIcon;
  private final LazyChild<SpanElement> textElement;
  private final List<TreeItem<T>> subItems = new LinkedList<>();
  private TreeItem<T> activeTreeItem;
  private TreeParent<T> parent;
  private final UListElement subTree;
  private final List<TreeItem<T>> childItems = new ArrayList<>();
  private TreeNode parentNode;

  private T value;

  private ToggleTarget toggleTarget = ToggleTarget.ANY;

  private OriginalState originalState;
  private boolean selectionListenersPaused;
  private Set<SelectionListener<? super TreeItem<T>, ? super TreeItem<T>>> selectionListeners =
      new HashSet<>();
  private Set<SelectionListener<? super TreeItem<T>, ? super TreeItem<T>>> deselectionListeners =
      new HashSet<>();

  private final EventListener anchorListener =
      evt -> {
        if (ToggleTarget.ANY.equals(this.toggleTarget)) {
          evt.stopPropagation();
          if (isParent()) {
            toggle();
          }
          setActiveItem();
        }
      };

  private final EventListener iconListener =
      evt -> {
        if (ToggleTarget.ICON.equals(this.toggleTarget)) {
          evt.stopPropagation();
          if (isParent()) {
            toggle();
          }
          setActiveItem();
        }
      };

  private TreeItem() {
    this.element =
        li().addCss(dui_tree_item)
            .appendChild(
                anchorElement =
                    a().removeHref()
                        .addCss(dui_tree_anchor)
                        .appendChild(contentElement = div().addCss(dui_tree_item_content)))
            .appendChild(subTree = ul().addCss(dui_tree_nav).hide());
    this.textElement = LazyChild.of(span().addCss(dui_tree_item_text), contentElement);
    init(this);

    setCollapseStrategy(getConfig().getTreeDefaultCollapseStrategy(this).get());
    setAttribute(Collapsible.DUI_COLLAPSED, "true");
  }

  /**
   * Constructor for TreeItem.
   *
   * @param icon a {@link org.dominokit.domino.ui.icons.Icon} object
   * @param title a {@link java.lang.String} object
   */
  public TreeItem(Icon<?> icon, String title) {
    this();
    setIcon(icon);
    setTitle(title);
    init();
  }

  /**
   * Constructor for TreeItem.
   *
   * @param title a {@link java.lang.String} object
   */
  public TreeItem(String title) {
    this();
    setTitle(title);
    init();
  }

  /**
   * Constructor for TreeItem.
   *
   * @param icon a {@link org.dominokit.domino.ui.icons.Icon} object
   */
  public TreeItem(Icon<?> icon) {
    this();
    setIcon(icon);
    init();
  }

  /**
   * Constructor for TreeItem.
   *
   * @param title a {@link java.lang.String} object
   * @param value a T object
   */
  public TreeItem(String title, T value) {
    this(title);
    this.value = value;
  }

  /**
   * Constructor for TreeItem.
   *
   * @param icon a {@link org.dominokit.domino.ui.icons.Icon} object
   * @param title a {@link java.lang.String} object
   * @param value a T object
   */
  public TreeItem(Icon<?> icon, String title, T value) {
    this(icon, title);
    this.value = value;
  }

  /**
   * Constructor for TreeItem.
   *
   * @param icon a {@link org.dominokit.domino.ui.icons.Icon} object
   * @param value a T object
   */
  public TreeItem(Icon<?> icon, T value) {
    this(icon);
    this.value = value;
  }

  /**
   * Creates new tree item with a title
   *
   * @param title the title of the item
   * @return new instance
   */
  public static TreeItem<String> create(String title) {
    TreeItem<String> treeItem = new TreeItem<>(title);
    treeItem.value = title;
    return treeItem;
  }

  /**
   * Creates new tree item with a title and an icon
   *
   * @param icon the item's {@link org.dominokit.domino.ui.icons.Icon}
   * @param title the title of the item
   * @return new instance
   */
  public static TreeItem<String> create(Icon<?> icon, String title) {
    TreeItem<String> treeItem = new TreeItem<>(icon, title);
    treeItem.value = title;
    return treeItem;
  }

  /**
   * Creates new tree item with an icon
   *
   * @param icon the item's {@link org.dominokit.domino.ui.icons.Icon}
   * @return new instance
   */
  public static TreeItem<String> create(Icon<?> icon) {
    TreeItem<String> treeItem = new TreeItem<>(icon);
    treeItem.value = "";
    return treeItem;
  }

  /**
   * Creates new tree item with a title and a value
   *
   * @param title the title of the item
   * @param value the value of the item
   * @param <T> the type of the value
   * @return new instance
   */
  public static <T> TreeItem<T> create(String title, T value) {
    return new TreeItem<>(title, value);
  }

  /**
   * Creates new tree item with a title, an icon and a value
   *
   * @param title the title of the item
   * @param icon the item's {@link org.dominokit.domino.ui.icons.Icon}
   * @param value the value of the item
   * @param <T> the type of the value
   * @return new instance
   */
  public static <T> TreeItem<T> create(Icon<?> icon, String title, T value) {
    return new TreeItem<>(icon, title, value);
  }

  /**
   * Creates new tree item with an icon and a value
   *
   * @param icon the item's {@link org.dominokit.domino.ui.icons.Icon}
   * @param value the value of the item
   * @param <T> the type of the value
   * @return new instance
   */
  public static <T> TreeItem<T> create(Icon<?> icon, T value) {
    return new TreeItem<>(icon, value);
  }

  private void init() {
    addBeforeCollapseListener(() -> updateIcon(true));
    addBeforeExpandListener(() -> updateIcon(false));
    anchorElement.addClickListener(anchorListener);
    applyWaves();
  }

  private void applyWaves() {
    withWaves((item, waves) -> waves.setWaveStyle(WaveStyle.BLOCK));
  }

  /**
   * This method is internally used by a tree item self to notify its root node {@link Tree} that
   * this item should be the active one in the tree.
   */
  private void setActiveItem() {
    getRootNode().setActiveItem(this);
  }

  /**
   * Adds a child item to this one
   *
   * @param treeItem the child {@link org.dominokit.domino.ui.tree.TreeItem}
   * @return same instance
   */
  public TreeItem<T> appendChild(TreeItem<T> treeItem) {
    appendChild((TreeNode) treeItem);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public TreeNode appendChild(TreeNode node) {
    TreeItem<T> treeItem = (TreeItem<T>) node;
    this.childItems.add(treeItem);
    subTree.appendChild(treeItem);
    treeItem.parentNode = this;
    treeItem.setToggleTarget(this.toggleTarget);
    updateIcon(isCollapsed());
    getParent()
        .ifPresent(
            p -> {
              if (nonNull(p.getTreeRoot())) {
                Tree.TreeItemIconSupplier<T> iconSupplier = p.getTreeRoot().getIconSupplier();
                if (nonNull(iconSupplier)) {
                  subItems.forEach(item -> item.onSuppliedIconChanged(iconSupplier));
                }
              }
            });
    return node;
  }

  /**
   * appendChild.
   *
   * @param postfixAddOn a {@link org.dominokit.domino.ui.utils.PostfixAddOn} object
   * @return a {@link org.dominokit.domino.ui.tree.TreeItem} object
   */
  public TreeItem<T> appendChild(PostfixAddOn<?> postfixAddOn) {
    contentElement.appendChild(postfixAddOn);
    return this;
  }

  /**
   * appendChild.
   *
   * @param prefixAddOn a {@link org.dominokit.domino.ui.utils.PrefixAddOn} object
   * @return a {@link org.dominokit.domino.ui.tree.TreeItem} object
   */
  public TreeItem<T> appendChild(PrefixAddOn<?> prefixAddOn) {
    contentElement.appendChild(prefixAddOn);
    return this;
  }

  private void updateIcon(boolean collapsed) {
    if (nonNull(itemIcon) && itemIcon.isInitialized()) {
      if (itemIcon.element() instanceof ToggleIcon) {
        ((ToggleIcon<?, ?>) itemIcon.element()).toggle();
      } else if (itemIcon.element() instanceof StateChangeIcon) {
        StateChangeIcon<?, ?> icon = (StateChangeIcon<?, ?>) itemIcon.element();
        if (isParent()) {
          icon.setState(collapsed ? TreeItemIcon.STATE_COLLAPSED : TreeItemIcon.STATE_EXPANDED);
        } else {
          if (dui_active.isAppliedTo(this)) {
            icon.setState(TreeItemIcon.STATE_ACTIVE);
          } else {
            icon.setState(TreeItemIcon.STATE_LEAF);
          }
        }
      }
    }
  }

  /**
   * Adds new separator
   *
   * @return same instance
   */
  public TreeItem<T> addSeparator() {
    subTree.appendChild(li().addCss(dui_separator));
    return this;
  }

  /**
   * Sets what is the target for toggling an item
   *
   * @param toggleTarget the {@link org.dominokit.domino.ui.tree.ToggleTarget}
   * @return same instance
   */
  public TreeItem<T> setToggleTarget(ToggleTarget toggleTarget) {
    if (nonNull(toggleTarget)) {

      this.toggleTarget = toggleTarget;
      if (ToggleTarget.ICON.equals(toggleTarget)) {
        removeWaves();
        if (nonNull(itemIcon) && itemIcon.isInitialized()) {
          itemIcon.get().setClickable(true).addEventListener("click", iconListener, true);
        }
      } else {
        applyWaves();
        if (nonNull(itemIcon) && itemIcon.isInitialized()) {
          itemIcon.get().setClickable(false).removeEventListener("click", iconListener);
          ;
        }
      }

      childItems.forEach(item -> item.setToggleTarget(toggleTarget));
    }
    return this;
  }

  private void toggle() {
    if (isParent()) {
      toggleCollapse();
    }
  }

  /**
   * Expands the tree item
   *
   * @return same instance
   */
  public TreeItem<T> expandNode() {
    return show(false);
  }

  /**
   * Shows the item
   *
   * @param expandParent true to expand the parent of the item
   * @return same instance
   */
  public TreeItem<T> show(boolean expandParent) {
    if (isParent()) {
      super.expand();
    }
    if (expandParent) {
      getParent().ifPresent(itemParent -> itemParent.expandNode(true));
    }
    return this;
  }

  /**
   * Expands the tree item
   *
   * @param expandParent true to expand the parent of the item
   * @return same instance
   */
  public TreeItem<T> expandNode(boolean expandParent) {
    return show(expandParent);
  }

  /** {@inheritDoc} */
  @Override
  public TreeItem<T> toggleCollapse() {
    if (isParent()) {
      super.toggleCollapse();
    }
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public HTMLLIElement element() {
    return element.element();
  }

  /**
   * @return the current active value
   * @deprecated use {@link Tree#getActiveItem()} instead
   */
  @Deprecated
  public TreeItem<T> getActiveItem() {
    return getRootNode().getActiveItem();
  }

  /** @return The {@link Tree} */
  public Tree<T> getTreeRoot() {
    return getRootNode();
  }

  /** @return the parent item */
  public Optional<TreeItem<T>> getParent() {
    if (parentNode instanceof TreeItem) {
      return Optional.of((TreeItem<T>) parentNode);
    } else {
      return Optional.empty();
    }
  }

  /**
   * Activates the item representing the value
   *
   * @param activeItem the value of the item to activate
   * @deprecated use {@link Tree#setActiveItem(TreeItem)} instead
   */
  @Deprecated
  public void setActiveItem(TreeItem<T> activeItem) {
    setActiveItem(activeItem, false);
  }

  /**
   * Activates the item representing the value
   *
   * @param activeItem the value of the item to activate
   * @param silent true to not notify listeners
   * @deprecated use {@link Tree#setActiveItem(TreeItem, boolean)} instead
   */
  @Deprecated
  public void setActiveItem(TreeItem<T> activeItem, boolean silent) {
    getRootNode().setActiveItem(activeItem, silent);
  }

  /** @return A list of tree items representing the path for this item */
  /**
   * getPath.
   *
   * @return a {@link java.util.List} object
   */
  public List<TreeItem<T>> getPath() {
    List<TreeItem<T>> items = Tree.getBubblingPath(this);

    Collections.reverse(items);

    return items;
  }

  /** @return A list of values representing the path for this item */
  /**
   * getPathValues.
   *
   * @return a {@link java.util.List} object
   */
  public List<T> getPathValues() {
    List<T> values = Tree.getBubblingPathValues(this);

    Collections.reverse(values);

    return values;
  }

  /** Activates the component, GUI effect only */
  @Override
  public void activate() {
    addCss(dui_active);
    updateIcon(isCollapsed());
  }

  /**
   * Activates the item
   *
   * @param activateParent true to activate parent
   * @deprecated use either {@link #activate()} or {@link Tree#setActiveItem(TreeItem)} instead
   */
  @Deprecated
  public void activate(boolean activateParent) {
    if (activateParent) setActiveItem();
    else activate();
  }

  /** Deactivate the component, GUI effect only */
  @Override
  public void deactivate() {
    deactivate(getRootNode().isAutoCollapse());
  }

  /**
   * Deactivate the component, GUI effect only
   *
   * @param autoCollapse indicator if item should be collapsed automatically
   */
  public void deactivate(boolean autoCollapse) {
    dui_active.remove(this);
    if (isParent()) {
      childItems.forEach(TreeItem::deactivate);
      if (getTreeRoot().isAutoCollapse()) {
        collapse();
      }
    }
    updateIcon(isCollapsed());
  }

  /** {@inheritDoc} */
  @Override
  public HTMLAnchorElement getClickableElement() {
    return anchorElement.element();
  }

  /**
   * Sets the icon of the item
   *
   * @param icon the new {@link org.dominokit.domino.ui.icons.Icon}
   * @return same instance
   */
  public TreeItem<T> setIcon(Icon<?> icon) {
    if (nonNull(itemIcon)) {
      if (itemIcon.isInitialized()) {
        itemIcon.remove();
      }
    }
    itemIcon = LazyChild.of(icon.addCss(dui_tree_item_icon), contentElement);
    itemIcon.whenInitialized(
        () -> {
          itemIcon.element().forEachChild(i -> i.addCss(dui_tree_item_icon));
        });
    itemIcon.whenInitialized(
        () -> {
          if (ToggleTarget.ICON.equals(this.toggleTarget)) {
            itemIcon.element().clickable();
          }
          itemIcon
              .element()
              .addClickListener(
                  evt -> {
                    if (ToggleTarget.ICON.equals(this.toggleTarget)) {
                      evt.stopPropagation();
                      toggle();
                    }
                    setActiveItem();
                  });
        });
    itemIcon.get();
    updateIcon(isCollapsed());

    return this;
  }

  boolean isParent() {
    return !childItems.isEmpty();
  }

  void setParent(TreeNode parentTreeNode) {
    this.parentNode = parentTreeNode;
  }

  /** @return the title of the item */
  /**
   * Getter for the field <code>title</code>.
   *
   * @return a {@link java.lang.String} object
   */
  public String getTitle() {
    return title;
  }

  /**
   * Find any descendant tree item matching the given item value
   *
   * @param value a value being searched
   * @return an {@code Optional} tree item matching the given item value
   */
  public Optional<TreeItem<T>> findAny(T value) {
    return findAny(getRootNode().createFinderPredicate(value));
  }

  /**
   * Filter this item based on the search token
   *
   * @param searchToken the search token
   * @return true if this item should be shown, false otherwise
   */
  public boolean filter(String searchToken) {
    return filter(createFilterPredicate(searchToken));
  }

  /**
   * Filter this item based on the given predicate
   *
   * @param predicate a predicate to test with
   * @return true if this item should be shown, false otherwise
   */
  public boolean filter(Predicate<TreeNode> predicate) {
    return search(
        (node, found) -> {
          if (isNull(this.originalState)) {
            this.originalState = new OriginalState(isExpanded());
          }

          if (found || predicate.test(node)) {
            Style.of(element).removeCssProperty("display");
            if (isParent() && isCollapsed()) {
              this.expandNode();
            }
            return true;
          } else {
            Style.of(element).setDisplay("none");
            return false;
          }
        });
  }

  /** @return true if automatic expanding is enabled when finding items in search */
  public boolean isAutoExpandFound() {
    return getTreeRoot().isAutoExpandFound();
  }

  /** Clears the filter applied */
  public void clearFilter() {
    if (nonNull(originalState)) {
      if (isExpanded() != originalState.expanded) {
        if (this.equals(this.getTreeRoot().getActiveItem())) {
          this.expandNode();
        } else {
          toggleCollapse(originalState.expanded);
        }
      }
      this.originalState = null;
    }
    dui_hidden.remove(this);
    subItems.forEach(TreeItem::clearFilter);
  }

  /**
   * Filters the children and make sure the filter is applied to all children
   *
   * @param searchToken the search token
   * @return true of one of the children matches the search token, false otherwise
   */
  public boolean filterChildren(String searchToken) {
    // We use the noneMatch here instead of anyMatch to make sure we are looping all children
    // instead of early exit on first matching one
    return childItems.stream().filter(treeItem -> treeItem.filter(searchToken)).count() > 0;
  }

  /** Collapse all children */
  public void collapseAll() {
    if (isParent() && !isCollapsed()) {
      addCss(dui_transition_none);
      subItems.forEach(TreeItem::collapseAll);
      collapse();
      dui_transition_none.remove(this);
    }
  }

  /** Expand all children */
  public void expandAll() {
    if (isParent() && isCollapsed()) {
      addCss(dui_transition_none);
      this.expandNode();
      subItems.forEach(TreeItem::expandAll);
      dui_transition_none.remove(this);
    }
  }

  /** {@inheritDoc} */
  @Override
  public Element getWavesElement() {
    return anchorElement.element();
  }

  /** @return true if this item does not have children, false otherwise */
  /**
   * isLeaf.
   *
   * @return a boolean
   */
  public boolean isLeaf() {
    return childItems.isEmpty();
  }

  /** @return the children of this item */
  public List<TreeItem<T>> getSubItems() {
    return getChildNodes();
  }

  /**
   * Selects this item, the item will be shown and activated, effectively set this tree item as the
   * active one of the tree
   */
  public void select() {
    setActiveItem();
  }

  /** @return the value of the item */
  /**
   * Getter for the field <code>value</code>.
   *
   * @return a T object
   */
  public T getValue() {
    return value;
  }

  /**
   * Sets the value of the item
   *
   * @param value the value
   */
  public void setValue(T value) {
    this.value = value;
  }

  /**
   * Removes item
   *
   * @param item the item value
   */
  public void removeItem(TreeItem<T> item) {
    removeChild((TreeNode) item);
  }

  private void removeParentStyle() {
    style().removeCss("tree-item-parent");
  }

  /**
   * Remove all the child tree items.
   *
   * @return same TreeItem instance
   */
  public TreeItem<T> clear() {
    Tree tree = getRootNode();

    // Remember the current active path of the root tree
    List<TreeItem<T>> activePath = tree.getActiveBubblingPath();

    // Update HTML DOM
    childItems.stream().forEach(TreeItem::remove);

    childItems.clear();

    removeParentStyle();

    // Either this tree item or one of its descendants is the active tree item of the root tree.
    // Since all the descendants of the tree item are cleared now, this one should be automatically
    // the active one.
    if (activePath.contains(this)) tree.setActiveItem(this);

    return this;
  }

  /** {@inheritDoc} */
  @Override
  public TreeNode removeChild(TreeNode node) {
    Tree<T> tree = getRootNode();

    // Remember the current active path of the root tree
    List<TreeItem<T>> activePath = tree.getActiveBubblingPath();

    if (childItems.remove(node)) {
      // Update HTML DOM
      ((TreeItem<T>) node).remove();

      if (childItems.isEmpty()) removeParentStyle();

      // Either the node being removed or one of its descendants is the active tree item of the root
      // tree. Since the node is removed, its parent node, namely this one, should be automatically
      // the active one.
      if (activePath.contains(node)) tree.setActiveItem(this);
    }

    return node;
  }

  /**
   * Replace original override remove() method, a convenient way to remove this tree item from the
   * tree
   *
   * @return same instance
   */
  public TreeItem<T> removeFromTree() {
    return (TreeItem<T>) getParentNode().removeChild(this);
  }

  /** {@inheritDoc} */
  @Override
  public TreeItem<T> remove() {
    // This method overriding can be removed in the future.
    // Before it invokes its parent tree item to remove itself. This is confusing, since you mix
    // logical list remove operation with DOM remove operation in one remove method, both parent and
    // child are the same type instance (TreeItem). Keep it a pure original DOM remove operation is
    // probably better. For a logical and DOM remove, you can use new added removeFromTree method.
    return super.remove();
  }

  public Predicate<TreeNode> createFilterPredicate(String searchToken) {
    return getRootNode().createFilterPredicate(searchToken);
  }

  /** @return The {@link HTMLElement} that contains the title of this TreeItem */
  /**
   * Getter for the field <code>textElement</code>.
   *
   * @return a {@link org.dominokit.domino.ui.elements.SpanElement} object
   */
  public SpanElement getTextElement() {
    return textElement.get();
  }

  /**
   * Change the title of a TreeItem, If the TreeItem was created without a value and the title is
   * used as a value then it will not change when the title is changed to change the value a call to
   * {@link #setValue(T)} should be called
   *
   * @param title String title to set
   * @return same TreeItem instance
   */
  public TreeItem<T> setTitle(String title) {
    this.title = title;
    textElement.get().setTextContent(title);
    return this;
  }

  /** @return the {@link HTMLUListElement} that contains the tree items */
  /**
   * Getter for the field <code>subTree</code>.
   *
   * @return a {@link org.dominokit.domino.ui.elements.UListElement} object
   */
  public UListElement getSubTree() {
    return subTree;
  }

  void onSuppliedIconChanged(Tree.TreeItemIconSupplier<T> iconSupplier) {
    Icon<?> icon = iconSupplier.createIcon(this);
    if (nonNull(icon) && isNull(itemIcon)) {
      setIcon(icon);
      subItems.forEach(item -> item.onSuppliedIconChanged(iconSupplier));
    }
  }

  /** {@inheritDoc} */
  @Override
  public TreeItem<T> pauseSelectionListeners() {
    this.selectionListenersPaused = true;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public TreeItem<T> resumeSelectionListeners() {
    this.selectionListenersPaused = false;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public TreeItem<T> togglePauseSelectionListeners(boolean toggle) {
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
  public TreeItem<T> triggerSelectionListeners(TreeItem<T> source, TreeItem<T> selection) {
    if (!isSelectionListenersPaused()) {
      this.selectionListeners.forEach(
          selectionListener ->
              selectionListener.onSelectionChanged(Optional.ofNullable(source), selection));
    }
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public TreeItem<T> triggerDeselectionListeners(TreeItem<T> source, TreeItem<T> selection) {
    if (!isSelectionListenersPaused()) {
      this.deselectionListeners.forEach(
          selectionListener ->
              selectionListener.onSelectionChanged(Optional.ofNullable(source), selection));
    }
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public TreeItem<T> getSelection() {
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public TreeNode getParentNode() {
    return parentNode;
  }

  /** {@inheritDoc} */
  @Override
  public List<TreeItem<T>> getChildNodes() {
    return childItems;
  }

  /** {@inheritDoc} */
  @Override
  public Tree<T> getRootNode() {
    return (Tree<T>) TreeNode.super.getRootNode();
  }

  private static class OriginalState {
    private boolean expanded;

    public OriginalState(boolean expanded) {
      this.expanded = expanded;
    }
  }
}
