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

import elemental2.dom.Element;
import org.dominokit.domino.ui.IsElement;

/**
 * FooterContent class.
 *
 * @author vegegoku
 * @version $Id: $Id
 */
public class FooterContent<T extends Element> extends BaseDominoElement<T, FooterContent<T>> {

  private DominoElement<T> element;

  /**
   * of.
   *
   * @param element a T object
   * @param <T> a T class
   * @return a {@link org.dominokit.domino.ui.utils.FooterContent} object
   */
  public static <T extends Element> FooterContent<T> of(T element) {
    return new FooterContent<>(element);
  }

  /**
   * of.
   *
   * @param element a {@link org.dominokit.domino.ui.IsElement} object
   * @param <T> a T class
   * @return a {@link org.dominokit.domino.ui.utils.FooterContent} object
   */
  public static <T extends Element> FooterContent<T> of(IsElement<T> element) {
    return new FooterContent<>(element);
  }

  /**
   * Constructor for FooterContent.
   *
   * @param element a T object
   */
  public FooterContent(T element) {
    this.element = elementOf(element);
    init(this);
  }

  /**
   * Constructor for FooterContent.
   *
   * @param element a {@link org.dominokit.domino.ui.IsElement} object
   */
  public FooterContent(IsElement<T> element) {
    this(element.element());
  }

  /** {@inheritDoc} */
  @Override
  public T element() {
    return element.element();
  }
}