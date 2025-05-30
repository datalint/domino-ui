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
package org.dominokit.domino.ui.tabs;

import static java.util.Objects.nonNull;
import static org.dominokit.domino.ui.style.DisplayCss.dui_overflow_hidden;

import elemental2.dom.DOMRect;
import elemental2.dom.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.dominokit.domino.ui.elements.UListElement;
import org.dominokit.domino.ui.icons.MdiIcon;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.menu.direction.DropDirection;
import org.dominokit.domino.ui.popover.Popover;
import org.dominokit.domino.ui.utils.BaseDominoElement;
import org.gwtproject.timer.client.Timer;

public class TabsDropDownHandler implements TabsOverflowHandler {

  private final MdiIcon dropDownIcon;
  private final Popover popover;
  private final List<Tab> invisibleTabs = new ArrayList<>();
  private Timer timer;
  private Consumer<Tab> closeHandler;
  private Tab.ActivationHandler activationHandler;
  private Tab lastVisible;
  private final BaseDominoElement.HandlerRecord resizeRecord =
      new BaseDominoElement.HandlerRecord();

  public TabsDropDownHandler() {
    dropDownIcon = Icons.dots_horizontal().clickable();
    popover = Popover.create(dropDownIcon).setPosition(DropDirection.BEST_MIDDLE_DOWN_UP);
  }

  @Override
  public void apply(TabsPanel tabsPanel) {

    tabsPanel.withTabsNav(
        (panel, nav) -> {
          nav.addCss(dui_overflow_hidden);
          tabsPanel.addActivationHandler(
              activationHandler =
                  (tab, active) -> {
                    if (active && invisibleTabs.contains(tab)) {
                      moveActiveTab(tabsPanel, tab);
                      popover.close();
                    }
                  });
          tabsPanel.addCloseHandler(
              closeHandler =
                  tab -> {
                    invisibleTabs.remove(tab);
                    updateTabs(tabsPanel, nav);
                  });

          timer =
              new Timer() {
                @Override
                public void run() {
                  updateTabs(panel, nav);
                }
              };

          nav.nowOrWhenAttached(
              () -> {
                if (isOverFlowing(nav.element())) {
                  panel.getTailNav().appendChild(dropDownIcon);
                }

                nav.onResize(
                    (element, observer, entries) -> {
                      timer.schedule(250);
                    },
                    resizeRecord);
              });
        });
  }

  @Override
  public void update(TabsPanel tabsPanel) {
    tabsPanel.withTabsNav(
        (panel, nav) -> {
          updateTabs(tabsPanel, nav);
        });
  }

  private void moveActiveTab(TabsPanel tabsPanel, Tab tab) {
    if (nonNull(lastVisible)) {
      popover.insertAfter(lastVisible, tab);
      invisibleTabs.add(lastVisible);
      lastVisible = tab;
      invisibleTabs.remove(tab);
      tabsPanel.withTabsNav(
          (panel, nav) -> {
            nav.appendChild(lastVisible);
          });
    }
  }

  private void updateTabs(TabsPanel panel, UListElement nav) {
    popover.close();
    invisibleTabs.forEach(
        tab -> {
          nav.appendChild(tab.element());
        });
    invisibleTabs.clear();
    panel
        .getTabs()
        .forEach(
            tab -> {
              if (isPartialVisible(nav.element(), tab.getTab().element())) {
                invisibleTabs.add(tab);
                popover.appendChild(tab.element());

              } else {
                lastVisible = tab;
              }
            });

    invisibleTabs.stream()
        .filter(Tab::isActive)
        .findFirst()
        .ifPresent(tab -> moveActiveTab(panel, tab));

    if (invisibleTabs.isEmpty()) {
      dropDownIcon.hide();
    } else {
      dropDownIcon.show();
    }
  }

  @Override
  public void cleanUp(TabsPanel tabsPanel) {
    dropDownIcon.remove();
    tabsPanel.withTabsNav(
        (parent, nav) -> {
          dui_overflow_hidden.remove(nav);
        });
    invisibleTabs.forEach(
        tab -> {
          tabsPanel.withTabsNav(
              (parent, nav) -> {
                nav.appendChild(tab);
              });
        });
    invisibleTabs.clear();
    timer.cancel();
    if (nonNull(activationHandler)) {
      tabsPanel.removeActivationHandler(activationHandler);
    }
    if (nonNull(closeHandler)) {
      tabsPanel.removeCloseHandler(closeHandler);
    }
    resizeRecord.remove();
  }

  private boolean isOverFlowing(Element element) {
    return element.scrollWidth > element.clientWidth || element.scrollHeight > element.clientHeight;
  }

  private boolean isPartialVisible(Element parent, Element child) {
    DOMRect parentRect = parent.getBoundingClientRect();
    DOMRect childRect = child.getBoundingClientRect();

    boolean fullyVisible =
        childRect.left >= parentRect.left
            && childRect.right <= parentRect.right
            && childRect.top >= parentRect.top
            && childRect.bottom <= parentRect.bottom;

    return !fullyVisible;
  }
}
