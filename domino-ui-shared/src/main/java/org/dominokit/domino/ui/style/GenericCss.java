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
package org.dominokit.domino.ui.style;

public interface GenericCss {
  CssClass dui = () -> "dui";
  CssClass dui_primary = new ReplaceCssClass(CompositeCssClass.of(
          ()->"dui-info",
          ()->"dui-warning",
          ()->"dui-error",
          ()->"dui-accent",
          ()->"dui-dominant",
          ()->"dui-secondary",
          ()->"dui-success"
  )).replaceWith(()->"dui-primary");
  CssClass dui_secondary = new ReplaceCssClass(CompositeCssClass.of(
          ()->"dui-info",
          ()->"dui-warning",
          ()->"dui-error",
          ()->"dui-accent",
          ()->"dui-dominant",
          ()->"dui-primary",
          ()->"dui-success"
  )).replaceWith(()->"dui-secondary");
  CssClass dui_dominant = new ReplaceCssClass(CompositeCssClass.of(
          ()->"dui-info",
          ()->"dui-warning",
          ()->"dui-error",
          ()->"dui-accent",
          ()->"dui-secondary",
          ()->"dui-primary",
          ()->"dui-success"
  )).replaceWith(()->"dui-dominant");
  CssClass dui_accent = new ReplaceCssClass(CompositeCssClass.of(
          ()->"dui-info",
          ()->"dui-warning",
          ()->"dui-error",
          ()->"dui-dominant",
          ()->"dui-secondary",
          ()->"dui-primary",
          ()->"dui-success"
  )).replaceWith(()->"dui-accent");
  CssClass dui_success = new ReplaceCssClass(CompositeCssClass.of(
          ()->"dui-info",
          ()->"dui-warning",
          ()->"dui-error",
          ()->"dui-accent",
          ()->"dui-dominant",
          ()->"dui-secondary",
          ()->"dui-primary"
  )).replaceWith(()->"dui-success");
  CssClass dui_info = new ReplaceCssClass(CompositeCssClass.of(
          ()->"dui-success",
          ()->"dui-warning",
          ()->"dui-error",
          ()->"dui-accent",
          ()->"dui-dominant",
          ()->"dui-secondary",
          ()->"dui-primary"
  )).replaceWith(()->"dui-info");
  CssClass dui_warning = new ReplaceCssClass(CompositeCssClass.of(
          ()->"dui-success",
          ()->"dui-info",
          ()->"dui-error",
          ()->"dui-accent",
          ()->"dui-dominant",
          ()->"dui-secondary",
          ()->"dui-primary"
  )).replaceWith(()->"dui-warning");
  CssClass dui_error = new ReplaceCssClass(CompositeCssClass.of(
          ()->"dui-success",
          ()->"dui-info",
          ()->"dui-warning",
          ()->"dui-accent",
          ()->"dui-dominant",
          ()->"dui-secondary",
          ()->"dui-primary"
  )).replaceWith(()->"dui-error");
  CssClass dui_clickable = () -> "dui-clickable";
  CssClass dui_active = () -> "dui-active";
  CssClass dui_utility = () -> "dui-utility";
  CssClass dui_prefix = () -> "dui-prefix";
  /** The css to add the stripes effect */
  CssClass dui_striped = ()->"dui-striped";
  CssClass dui_xlarge = new ReplaceCssClass(CompositeCssClass.of(
          ()->"dui-lg",
          ()->"dui-md",
          ()->"dui-sm",
          ()->"dui-xs"
  )).replaceWith(()->"dui-xl");
  CssClass dui_large = new ReplaceCssClass(CompositeCssClass.of(
          ()->"dui-xl",
          ()->"dui-md",
          ()->"dui-sm",
          ()->"dui-xs"
  )).replaceWith(()->"dui-lg");
  CssClass dui_medium = new ReplaceCssClass(CompositeCssClass.of(
          ()->"dui-xl",
          ()->"dui-lg",
          ()->"dui-sm",
          ()->"dui-xs"
  )).replaceWith(()->"dui-md");
  CssClass dui_small = new ReplaceCssClass(CompositeCssClass.of(
          ()->"dui-xl",
          ()->"dui-lg",
          ()->"dui-md",
          ()->"dui-xs"
  )).replaceWith(()->"dui-sm");
  CssClass dui_xsmall = new ReplaceCssClass(CompositeCssClass.of(
          ()->"dui-xl",
          ()->"dui-lg",
          ()->"dui-md",
          ()->"dui-sm"
  )).replaceWith(()->"dui-xs");

  CssClass dui_w_xlarge = () -> "dui-w-xl";
  CssClass dui_w_large = () -> "dui-w-lg";
  CssClass dui_w_medium = () -> "dui-w-md";
  CssClass dui_w_small = () -> "dui-w-sm";
  CssClass dui_w_xsmall = () -> "dui-w-xs";

  CssClass dui_h_xlarge = () -> "dui-h-xl";
  CssClass dui_h_large = () -> "dui-h-lg";
  CssClass dui_h_medium = () -> "dui-h-md";
  CssClass dui_h_small = () -> "dui-h-sm";
  CssClass dui_h_xsmall = () -> "dui-h-xs";

  CssClass dui_overlay = () -> "dui-overlay";
  CssClass dui_clearable = () -> "dui-clearable";
  CssClass dui_image_responsive = () -> "dui-image-responsive";
  CssClass dui_vertical_center = () -> "dui-vertical-center";
  CssClass dui_horizontal_center = () -> "dui-horizontal-center";

  CssClass dui_close = () -> "dui-close";

  CssClass dui_selected = () -> "dui-selected";

  String font_6 = "font-6";
  String font_10 = "font-10";
  String font_12 = "font-12";
  String font_15 = "font-15";
  String font_20 = "font-20";
  String font_24 = "font-24";
  String font_32 = "font-32";
  String font_40 = "font-40";
  String font_42 = "font-42";
  String font_45 = "font-45";
  String font_48 = "font-48";
  String font_50 = "font-50";
  String font_60 = "font-60";
  String font_72 = "font-72";
  String lead = "lead";
  String font_bold = "font-bold";
  String font_italic = "font-italic";
  String font_under_line = "font-underline";
  String font_line_through = "font-line-through";
  String font_over_line = "font-overline";
  String blockquote_reverse = "blockquote-reverse";
  String list_unstyled = "list-unstyled";
  String align_left = "align-left";
  String align_right = "align-right";
  String align_center = "align-center";
  String align_justify = "align-justify";
  String vertical_center = "dui-vertical-center";
  String horizontal_center = "dui-horizontal-center";

  String m_t__125 = "m-t--125";
  String m_t__120 = "m-t--120";
  String m_t__115 = "m-t--115";
  String m_t__110 = "m-t--110";
  String m_t__105 = "m-t--105";
  String m_t__100 = "m-t--100";
  String m_t__95 = "m-t--95";
  String m_t__90 = "m-t--90";
  String m_t__85 = "m-t--85";
  String m_t__80 = "m-t--80";
  String m_t__75 = "m-t--75";
  String m_t__70 = "m-t--70";
  String m_t__65 = "m-t--65";
  String m_t__60 = "m-t--60";
  String m_t__55 = "m-t--55";
  String m_t__50 = "m-t--50";
  String m_t__45 = "m-t--45";
  String m_t__40 = "m-t--40";
  String m_t__35 = "m-t--35";
  String m_t__30 = "m-t--30";
  String m_t__25 = "m-t--25";
  String m_t__20 = "m-t--20";
  String m_t__15 = "m-t--15";
  String m_t__10 = "m-t--10";
  String m_t__5 = "m-t--5";
  String m_t_0 = "m-t-0";
  String m_t_5 = "m-t-5";
  String m_t_10 = "m-t-10";
  String m_t_15 = "m-t-15";
  String m_t_20 = "m-t-20";
  String m_t_25 = "m-t-25";
  String m_t_30 = "m-t-30";
  String m_t_35 = "m-t-35";
  String m_t_40 = "m-t-40";
  String m_t_45 = "m-t-45";
  String m_t_50 = "m-t-50";
  String m_t_55 = "m-t-55";
  String m_t_60 = "m-t-60";
  String m_t_65 = "m-t-65";
  String m_t_70 = "m-t-70";
  String m_t_75 = "m-t-75";
  String m_t_80 = "m-t-80";
  String m_t_85 = "m-t-85";
  String m_t_90 = "m-t-90";
  String m_t_95 = "m-t-95";
  String m_t_100 = "m-t-100";
  String m_t_105 = "m-t-105";
  String m_t_110 = "m-t-110";
  String m_t_115 = "m-t-115";
  String m_t_120 = "m-t-120";
  String m_t_125 = "m-t-125";
  String m_l__125 = "m-l--125";
  String m_l__120 = "m-l--120";
  String m_l__115 = "m-l--115";
  String m_l__110 = "m-l--110";
  String m_l__105 = "m-l--105";
  String m_l__100 = "m-l--100";
  String m_l__95 = "m-l--95";
  String m_l__90 = "m-l--90";
  String m_l__85 = "m-l--85";
  String m_l__80 = "m-l--80";
  String m_l__75 = "m-l--75";
  String m_l__70 = "m-l--70";
  String m_l__65 = "m-l--65";
  String m_l__60 = "m-l--60";
  String m_l__55 = "m-l--55";
  String m_l__50 = "m-l--50";
  String m_l__45 = "m-l--45";
  String m_l__40 = "m-l--40";
  String m_l__35 = "m-l--35";
  String m_l__30 = "m-l--30";
  String m_l__25 = "m-l--25";
  String m_l__20 = "m-l--20";
  String m_l__15 = "m-l--15";
  String m_l__10 = "m-l--10";
  String m_l__5 = "m-l--5";
  String m_l_0 = "m-l-0";
  String m_l_5 = "m-l-5";
  String m_l_2 = "m-l-2";
  String m_l_10 = "m-l-10";
  String m_l_15 = "m-l-15";
  String m_l_20 = "m-l-20";
  String m_l_25 = "m-l-25";
  String m_l_30 = "m-l-30";
  String m_l_35 = "m-l-35";
  String m_l_40 = "m-l-40";
  String m_l_45 = "m-l-45";
  String m_l_50 = "m-l-50";
  String m_l_55 = "m-l-55";
  String m_l_60 = "m-l-60";
  String m_l_65 = "m-l-65";
  String m_l_70 = "m-l-70";
  String m_l_75 = "m-l-75";
  String m_l_80 = "m-l-80";
  String m_l_85 = "m-l-85";
  String m_l_90 = "m-l-90";
  String m_l_95 = "m-l-95";
  String m_l_100 = "m-l-100";
  String m_l_105 = "m-l-105";
  String m_l_110 = "m-l-110";
  String m_l_115 = "m-l-115";
  String m_l_120 = "m-l-120";
  String m_l_125 = "m-l-125";
  String m_b__125 = "m-b--125";
  String m_b__120 = "m-b--120";
  String m_b__115 = "m-b--115";
  String m_b__110 = "m-b--110";
  String m_b__105 = "m-b--105";
  String m_b__100 = "m-b--100";
  String m_b__95 = "m-b--95";
  String m_b__90 = "m-b--90";
  String m_b__85 = "m-b--85";
  String m_b__80 = "m-b--80";
  String m_b__75 = "m-b--75";
  String m_b__70 = "m-b--70";
  String m_b__65 = "m-b--65";
  String m_b__60 = "m-b--60";
  String m_b__55 = "m-b--55";
  String m_b__50 = "m-b--50";
  String m_b__45 = "m-b--45";
  String m_b__40 = "m-b--40";
  String m_b__35 = "m-b--35";
  String m_b__30 = "m-b--30";
  String m_b__25 = "m-b--25";
  String m_b__20 = "m-b--20";
  String m_b__15 = "m-b--15";
  String m_b__10 = "m-b--10";
  String m_b__5 = "m-b--5";
  String m_b_0 = "m-b-0";
  String m_b_5 = "m-b-5";
  String m_b_10 = "m-b-10";
  String m_b_15 = "m-b-15";
  String m_b_20 = "m-b-20";
  String m_b_25 = "m-b-25";
  String m_b_30 = "m-b-30";
  String m_b_35 = "m-b-35";
  String m_b_40 = "m-b-40";
  String m_b_45 = "m-b-45";
  String m_b_50 = "m-b-50";
  String m_b_55 = "m-b-55";
  String m_b_60 = "m-b-60";
  String m_b_65 = "m-b-65";
  String m_b_70 = "m-b-70";
  String m_b_75 = "m-b-75";
  String m_b_80 = "m-b-80";
  String m_b_85 = "m-b-85";
  String m_b_90 = "m-b-90";
  String m_b_95 = "m-b-95";
  String m_b_100 = "m-b-100";
  String m_b_105 = "m-b-105";
  String m_b_110 = "m-b-110";
  String m_b_115 = "m-b-115";
  String m_b_120 = "m-b-120";
  String m_b_125 = "m-b-125";
  String m_r__125 = "m-r--125";
  String m_r__120 = "m-r--120";
  String m_r__115 = "m-r--115";
  String m_r__110 = "m-r--110";
  String m_r__105 = "m-r--105";
  String m_r__100 = "m-r--100";
  String m_r__95 = "m-r--95";
  String m_r__90 = "m-r--90";
  String m_r__85 = "m-r--85";
  String m_r__80 = "m-r--80";
  String m_r__75 = "m-r--75";
  String m_r__70 = "m-r--70";
  String m_r__65 = "m-r--65";
  String m_r__60 = "m-r--60";
  String m_r__55 = "m-r--55";
  String m_r__50 = "m-r--50";
  String m_r__45 = "m-r--45";
  String m_r__40 = "m-r--40";
  String m_r__35 = "m-r--35";
  String m_r__30 = "m-r--30";
  String m_r__25 = "m-r--25";
  String m_r__20 = "m-r--20";
  String m_r__15 = "m-r--15";
  String m_r__10 = "m-r--10";
  String m_r__5 = "m-r--5";
  String m_r_0 = "m-r-0";
  String m_r_2 = "m-r-2";
  String m_r_5 = "m-r-5";
  String m_r_10 = "m-r-10";
  String m_r_15 = "m-r-15";
  String m_r_20 = "m-r-20";
  String m_r_25 = "m-r-25";
  String m_r_30 = "m-r-30";
  String m_r_35 = "m-r-35";
  String m_r_40 = "m-r-40";
  String m_r_45 = "m-r-45";
  String m_r_50 = "m-r-50";
  String m_r_55 = "m-r-55";
  String m_r_60 = "m-r-60";
  String m_r_65 = "m-r-65";
  String m_r_70 = "m-r-70";
  String m_r_75 = "m-r-75";
  String m_r_80 = "m-r-80";
  String m_r_85 = "m-r-85";
  String m_r_90 = "m-r-90";
  String m_r_95 = "m-r-95";
  String m_r_100 = "m-r-100";
  String m_r_105 = "m-r-105";
  String m_r_110 = "m-r-110";
  String m_r_115 = "m-r-115";
  String m_r_120 = "m-r-120";
  String m_r_125 = "m-r-125";
  String margin_0 = "margin-0";
  String p_t_0 = "p-t-0";
  String p_t_5 = "p-t-5";
  String p_t_10 = "p-t-10";
  String p_t_15 = "p-t-15";
  String p_t_20 = "p-t-20";
  String p_t_25 = "p-t-25";
  String p_t_30 = "p-t-30";
  String p_t_35 = "p-t-35";
  String p_t_40 = "p-t-40";
  String p_t_45 = "p-t-45";
  String p_t_50 = "p-t-50";
  String p_t_55 = "p-t-55";
  String p_t_60 = "p-t-60";
  String p_t_65 = "p-t-65";
  String p_t_70 = "p-t-70";
  String p_t_75 = "p-t-75";
  String p_t_80 = "p-t-80";
  String p_t_85 = "p-t-85";
  String p_t_90 = "p-t-90";
  String p_t_95 = "p-t-95";
  String p_t_100 = "p-t-100";
  String p_t_105 = "p-t-105";
  String p_t_110 = "p-t-110";
  String p_t_115 = "p-t-115";
  String p_t_120 = "p-t-120";
  String p_t_125 = "p-t-125";
  String p_l_0 = "p-l-0";
  String p_l_5 = "p-l-5";
  String p_l_10 = "p-l-10";
  String p_l_15 = "p-l-15";
  String p_l_20 = "p-l-20";
  String p_l_25 = "p-l-25";
  String p_l_30 = "p-l-30";
  String p_l_35 = "p-l-35";
  String p_l_40 = "p-l-40";
  String p_l_45 = "p-l-45";
  String p_l_50 = "p-l-50";
  String p_l_55 = "p-l-55";
  String p_l_60 = "p-l-60";
  String p_l_65 = "p-l-65";
  String p_l_70 = "p-l-70";
  String p_l_75 = "p-l-75";
  String p_l_80 = "p-l-80";
  String p_l_85 = "p-l-85";
  String p_l_90 = "p-l-90";
  String p_l_95 = "p-l-95";
  String p_l_100 = "p-l-100";
  String p_l_105 = "p-l-105";
  String p_l_110 = "p-l-110";
  String p_l_115 = "p-l-115";
  String p_l_120 = "p-l-120";
  String p_l_125 = "p-l-125";
  String p_b_0 = "p-b-0";
  String p_b_5 = "p-b-5";
  String p_b_10 = "p-b-10";
  String p_b_15 = "p-b-15";
  String p_b_20 = "p-b-20";
  String p_b_25 = "p-b-25";
  String p_b_30 = "p-b-30";
  String p_b_35 = "p-b-35";
  String p_b_40 = "p-b-40";
  String p_b_45 = "p-b-45";
  String p_b_50 = "p-b-50";
  String p_b_55 = "p-b-55";
  String p_b_60 = "p-b-60";
  String p_b_65 = "p-b-65";
  String p_b_70 = "p-b-70";
  String p_b_75 = "p-b-75";
  String p_b_80 = "p-b-80";
  String p_b_85 = "p-b-85";
  String p_b_90 = "p-b-90";
  String p_b_95 = "p-b-95";
  String p_b_100 = "p-b-100";
  String p_b_105 = "p-b-105";
  String p_b_110 = "p-b-110";
  String p_b_115 = "p-b-115";
  String p_b_120 = "p-b-120";
  String p_b_125 = "p-b-125";
  String p_r_0 = "p-r-0";
  String p_r_5 = "p-r-5";
  String p_r_10 = "p-r-10";
  String p_r_15 = "p-r-15";
  String p_r_20 = "p-r-20";
  String p_r_25 = "p-r-25";
  String p_r_30 = "p-r-30";
  String p_r_35 = "p-r-35";
  String p_r_40 = "p-r-40";
  String p_r_45 = "p-r-45";
  String p_r_50 = "p-r-50";
  String p_r_55 = "p-r-55";
  String p_r_60 = "p-r-60";
  String p_r_65 = "p-r-65";
  String p_r_70 = "p-r-70";
  String p_r_75 = "p-r-75";
  String p_r_80 = "p-r-80";
  String p_r_85 = "p-r-85";
  String p_r_90 = "p-r-90";
  String p_r_95 = "p-r-95";
  String p_r_100 = "p-r-100";
  String p_r_105 = "p-r-105";
  String p_r_110 = "p-r-110";
  String p_r_115 = "p-r-115";
  String p_r_120 = "p-r-120";
  String p_r_125 = "p-r-125";
  String padding_0 = "padding-0";
  String padding_5 = "padding-5";
  String padding_10 = "padding-10";
  String padding_15 = "padding-15";
  String padding_20 = "padding-20";
  String padding_25 = "padding-25";
  String padding_30 = "padding-30";
  String padding_35 = "padding-35";
  String padding_40 = "padding-40";
  String padding_45 = "padding-45";
  String padding_50 = "padding-50";
  String padding_55 = "padding-55";
  String padding_60 = "padding-60";
  String padding_65 = "padding-65";
  String padding_70 = "padding-70";
  String padding_75 = "padding-75";
  String padding_80 = "padding-80";
  String padding_85 = "padding-85";
  String padding_90 = "padding-90";
  String padding_95 = "padding-95";
  String padding_100 = "padding-100";
  String padding_105 = "padding-105";
  String padding_110 = "padding-110";
  String padding_115 = "padding-115";
  String padding_120 = "padding-120";
  String padding_125 = "padding-125";
  String img_responsive = "img-responsive";
  String img_circle = "img-circle";
  String clearfix = "clearfix";
  String pull_right = "pull-right";
  String pull_left = "pull-left";
  String no_wrap = "no-wrap";
  String disable_selection = "disable-selection";
  String alert_link = "alert-link";
  String ellipsis_text = "ellipsis-text";
  String opacity_l_0 = "opacity-l-0";
  String opacity_l_1 = "opacity-l-1";
  String opacity_l_2 = "opacity-l-2";
  String opacity_l_3 = "opacity-l-3";
  String opacity_l_4 = "opacity-l-4";
  String opacity_l_5 = "opacity-l-5";
  String opacity_l_6 = "opacity-l-6";
  String opacity_l_7 = "opacity-l-7";
  String opacity_l_8 = "opacity-l-8";
  String opacity_l_9 = "opacity-l-9";
  String opacity_l_full = "opacity-l-full";
  String b_b_0 = "b-b-0";
  String b_t_0 = "b-t-0";
  String b_l_0 = "b-l-0";
  String b_r_0 = "b-r-0";
  String b_0 = "b-0";
}
