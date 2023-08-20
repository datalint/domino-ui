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
 * Component that can have a helper text should implement this interface
 *
 * @param <T> the type of the class implementing this interface
 */
public interface HasHelperText<T> {
  /**
   * setHelperText.
   *
   * @param helperText String
   * @return same implementing class instance
   */
  T setHelperText(String helperText);

  /** @return String helper text of the component */
  /**
   * getHelperText.
   *
   * @return a {@link java.lang.String} object
   */
  String getHelperText();
}
