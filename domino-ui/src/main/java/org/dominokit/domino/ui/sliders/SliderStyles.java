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
package org.dominokit.domino.ui.sliders;

import org.dominokit.domino.ui.style.CssClass;

public interface SliderStyles {
  CssClass dui_slider = () -> "dui-slider";
  CssClass dui_slider_input = () -> "dui-slider-input";
  CssClass dui_slider_thumb = () -> "dui-slider-thumb";
  CssClass dui_slider_value = () -> "dui-slider-value";
  CssClass dui_slider_thumb_rounded = () -> "dui-slider-thumb-rounded";
  CssClass dui_slider_thumb_flat = () -> "dui-slider-thumb-flat";
}
