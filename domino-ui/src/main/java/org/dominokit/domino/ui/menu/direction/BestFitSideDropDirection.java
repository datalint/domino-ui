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
package org.dominokit.domino.ui.menu.direction;

import static org.dominokit.domino.ui.style.SpacingCss.dui_flex_col_reverse;

import elemental2.dom.Element;

/** BestFitSideDropDirection class. */
public class BestFitSideDropDirection implements DropDirection {

  /** {@inheritDoc} */
  @Override
  public DropDirection position(DropDirectionContext context) {
    Element source = context.getSource();
    cleanup(source);
    dui_flex_col_reverse.remove(source);

    SpaceChecker spaceChecker = context.getSpaceChecker();

    if (spaceChecker.hasSpaceOnRight()) {
      if (spaceChecker.hasSpaceBelow()) {
        return RIGHT_DOWN.position(context);
      } else if (spaceChecker.hasSpaceAbove()) {
        return RIGHT_UP.position(context);
      }
    } else if (spaceChecker.hasSpaceOnLeft()) {
      if (spaceChecker.hasSpaceBelow()) {
        return LEFT_DOWN.position(context);
      } else if (spaceChecker.hasSpaceAbove()) {
        return LEFT_UP.position(context);
      }
    }

    return MIDDLE_SCREEN.position(context);
  }

  /** {@inheritDoc} */
  @Override
  public void cleanup(Element source) {
    RIGHT_DOWN.cleanSelf(source);
    RIGHT_UP.cleanSelf(source);
    LEFT_DOWN.cleanSelf(source);
    LEFT_UP.cleanSelf(source);
    MIDDLE_SCREEN.cleanSelf(source);
  }
}
