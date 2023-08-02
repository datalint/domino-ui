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
package org.dominokit.domino.ui.utils;

import org.dominokit.domino.ui.style.Color;

/**
 * Components that can have a background should implement this interface
 *
 * @param <T> The type of the component that implements this interface
 * @author vegegoku
 * @version $Id: $Id
 */
@FunctionalInterface
public interface HasBackground<T> {
  /**
   * setBackground.
   *
   * @param background {@link org.dominokit.domino.ui.style.Color}
   * @return same implementing component
   */
  T setBackground(Color background);
}
