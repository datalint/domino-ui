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
package org.dominokit.domino.ui.richtext.commands;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import jsinterop.base.Js;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.elements.DivElement;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.richtext.RichTextCommand;
import org.dominokit.domino.ui.utils.DominoNavigator;

public class PasteCommand extends RichTextCommand<PasteCommand> {

  private String content;
  private Button button;

  public static PasteCommand create(DivElement editableElement) {
    return new PasteCommand(editableElement);
  }

  public PasteCommand(DivElement editableElement) {
    super(editableElement);
    this.button =
        Button.create(Icons.content_paste())
            .setTooltip(getLabels().paste())
            .addClickListener(
                evt -> {
                  editableElement.element().focus();
                  Js.<DominoNavigator>uncheckedCast(DomGlobal.window.navigator)
                      .clipboard
                      .readText()
                      .then(
                          content -> {
                            LOGGER.info(content);
                            this.content = content;
                            execute();
                            return null;
                          });
                  execute();
                });
    init(this);
  }

  @Override
  public HTMLElement element() {
    return button.element();
  }

  @Override
  protected void execute() {
    getSelectedRange()
        .ifPresent(
            range -> {
              range.deleteContents();
              range.insertNode(text(content));
            });
  }
}
