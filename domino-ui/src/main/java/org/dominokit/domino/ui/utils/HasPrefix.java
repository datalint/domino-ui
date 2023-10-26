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

/**
 * The {@code HasPrefix} interface defines methods for getting and setting a prefix text for a
 * component.
 *
 * @param <T> The type of the component that can have a prefix.
 */
public interface HasPrefix<T> {

  /**
   * Sets a prefix text for the component.
   *
   * @param prefix The prefix text to set for the component.
   * @return The component with the updated prefix text.
   */
  T setPrefix(String prefix);

  /**
   * Gets the prefix text of the component.
   *
   * @return The prefix text of the component.
   */
  String getPrefix();
}
