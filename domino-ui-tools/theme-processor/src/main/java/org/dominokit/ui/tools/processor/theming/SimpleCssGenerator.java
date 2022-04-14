package org.dominokit.ui.tools.processor.theming;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleCssGenerator {
    public static void main(String[] args) throws IOException, TemplateException {

        List<SimpleCss> displayStyles = Arrays.asList(
                SimpleCss.of("block", "display: block;"),
                SimpleCss.of("inline-block", "display: inline-block;"),
                SimpleCss.of("inline", "display: inline;"),
                SimpleCss.of("flex", "display: flex;"),
                SimpleCss.of("inline-flex", "display: inline-flex;"),
                SimpleCss.of("table", "display: table;"),
                SimpleCss.of("inline-table", "display: inline-table;"),
                SimpleCss.of("table-caption", "display: table-caption;"),
                SimpleCss.of("table-cell", "display: table-cell;"),
                SimpleCss.of("table-column", "display: table-column;"),
                SimpleCss.of("table-column-group", "display: table-column-group;"),
                SimpleCss.of("table-footer-group", "display: table-footer-group;"),
                SimpleCss.of("table-header-group", "display: table-header-group;"),
                SimpleCss.of("table-row-group", "display: table-row-group;"),
                SimpleCss.of("table-row", "display: table-row;"),
                SimpleCss.of("flow-root", "display: flow-root;"),
                SimpleCss.of("grid", "display: grid;"),
                SimpleCss.of("inline-grid", "display: inline-grid;"),
                SimpleCss.of("contents", "display: contents;"),
                SimpleCss.of("list-item", "display: list-item;"),
                SimpleCss.of("hidden", "display: none;"),

                SimpleCss.of("float-right", "float: right;"),
                SimpleCss.of("float-left", "float: left;"),
                SimpleCss.of("float-none", "float: none;"),

                SimpleCss.of("clear-left", "clear: left;"),
                SimpleCss.of("clear-right", "clear: right;"),
                SimpleCss.of("clear-both", "clear: both;"),
                SimpleCss.of("clear-none", "clear: none;"),

                SimpleCss.of("isolate", "isolation: isolate;"),
                SimpleCss.of("isolation-auto", "isolation: auto;"),

                SimpleCss.of("object-contain", "object-fit: contain;"),
                SimpleCss.of("object-cover", "object-fit: cover;"),
                SimpleCss.of("object-fill", "object-fit: fill;"),
                SimpleCss.of("object-none", "object-fit: none;"),
                SimpleCss.of("object-scale-down", "object-fit: scale-down;"),

                SimpleCss.of("object-bottom", "object-position: bottom;"),
                SimpleCss.of("object-center", "object-position: center;"),
                SimpleCss.of("object-left", "object-position: left;"),
                SimpleCss.of("object-bottom", "object-position: left bottom;"),
                SimpleCss.of("object-top", "object-position: left top;"),
                SimpleCss.of("object-right", "object-position: right;"),
                SimpleCss.of("object-bottom", "object-position: right bottom;"),
                SimpleCss.of("object-top", "object-position: right top;"),
                SimpleCss.of("object-top", "object-position: top;"),


                SimpleCss.of("overflow-auto", "overflow: auto;"),
                SimpleCss.of("overflow-hidden", "overflow: hidden;"),
                SimpleCss.of("overflow-clip", "overflow: clip;"),
                SimpleCss.of("overflow-visible", "overflow: visible;"),
                SimpleCss.of("overflow-scroll", "overflow: scroll;"),
                SimpleCss.of("overflow-x-auto", "overflow-x: auto;"),
                SimpleCss.of("overflow-y-auto", "overflow-y: auto;"),
                SimpleCss.of("overflow-x-hidden", "overflow-x: hidden;"),
                SimpleCss.of("overflow-y-hidden", "overflow-y: hidden;"),
                SimpleCss.of("overflow-x-clip", "overflow-x: clip;"),
                SimpleCss.of("overflow-y-clip", "overflow-y: clip;"),
                SimpleCss.of("overflow-x-visible", "overflow-x: visible;"),
                SimpleCss.of("overflow-y-visible", "overflow-y: visible;"),
                SimpleCss.of("overflow-x-scroll", "overflow-x: scroll;"),
                SimpleCss.of("overflow-y-scroll", "overflow-y: scroll;"),


                SimpleCss.of("overscroll-auto", "overscroll-behavior: auto;"),
                SimpleCss.of("overscroll-contain", "overscroll-behavior: contain;"),
                SimpleCss.of("overscroll-none", "overscroll-behavior: none;"),
                SimpleCss.of("overscroll-y-auto", "overscroll-behavior-y: auto;"),
                SimpleCss.of("overscroll-y-contain", "overscroll-behavior-y: contain;"),
                SimpleCss.of("overscroll-y-none", "overscroll-behavior-y: none;"),
                SimpleCss.of("overscroll-x-auto", "overscroll-behavior-x: auto;"),
                SimpleCss.of("overscroll-x-contain", "overscroll-behavior-x: contain;"),
                SimpleCss.of("overscroll-x-none", "overscroll-behavior-x: none;"),


                SimpleCss.of("static", "position: static;"),
                SimpleCss.of("fixed", "position: fixed;"),
                SimpleCss.of("absolute", "position: absolute;"),
                SimpleCss.of("relative", "position: relative;"),
                SimpleCss.of("sticky", "position: sticky;"),


                SimpleCss.of("inset-0", "top: 0px;\n\tright: 0px;\n\tbottom: 0px;\n\tleft: 0px;"),
                SimpleCss.of("inset-x-0", "left: 0px;\n\tright: 0px;"),
                SimpleCss.of("inset-y-0", "top: 0px;\n\tbottom: 0px;"),
                SimpleCss.of("top-0", "top: 0px;"),
                SimpleCss.of("right-0", "right: 0px;"),
                SimpleCss.of("bottom-0", "bottom: 0px;"),
                SimpleCss.of("left-0", "left: 0px;"),
                SimpleCss.of("inset-px", "top: 1px;\n\tright: 1px;\n\tbottom: 1px;\n\tleft: 1px;"),
                SimpleCss.of("inset-x-px", "left: 1px;\n\tright: 1px;"),
                SimpleCss.of("inset-y-px", "top: 1px;\n\tbottom: 1px;"),
                SimpleCss.of("top-px", "top: 1px;"),
                SimpleCss.of("right-px", "right: 1px;"),
                SimpleCss.of("bottom-px", "bottom: 1px;"),
                SimpleCss.of("left-px", "left: 1px;"),
                SimpleCss.of("inset-0\\.5", "top: 0.125rem; /* 2px */\n\tright: 0.125rem; /* 2px */\n\tbottom: 0.125rem; /* 2px */\n\tleft: 0.125rem; /* 2px */"),
                SimpleCss.of("inset-x-0\\.5", "left: 0.125rem; /* 2px */\n\tright: 0.125rem; /* 2px */"),
                SimpleCss.of("inset-y-0\\.5", "top: 0.125rem; /* 2px */\n\tbottom: 0.125rem; /* 2px */"),
                SimpleCss.of("top-0\\.5", "top: 0.125rem; /* 2px */"),
                SimpleCss.of("right-0\\.5", "right: 0.125rem; /* 2px */"),
                SimpleCss.of("bottom-0\\.5", "bottom: 0.125rem; /* 2px */"),
                SimpleCss.of("left-0\\.5", "left: 0.125rem; /* 2px */"),
                SimpleCss.of("inset-1", "top: 0.25rem; /* 4px */\n\tright: 0.25rem; /* 4px */\n\tbottom: 0.25rem; /* 4px */\n\tleft: 0.25rem; /* 4px */"),
                SimpleCss.of("inset-x-1", "left: 0.25rem; /* 4px */\n\tright: 0.25rem; /* 4px */"),
                SimpleCss.of("inset-y-1", "top: 0.25rem; /* 4px */\n\tbottom: 0.25rem; /* 4px */"),
                SimpleCss.of("top-1", "top: 0.25rem; /* 4px */"),
                SimpleCss.of("right-1", "right: 0.25rem; /* 4px */"),
                SimpleCss.of("bottom-1", "bottom: 0.25rem; /* 4px */"),
                SimpleCss.of("left-1", "left: 0.25rem; /* 4px */"),
                SimpleCss.of("inset-1\\.5", "top: 0.375rem; /* 6px */\n\tright: 0.375rem; /* 6px */\n\tbottom: 0.375rem; /* 6px */\n\tleft: 0.375rem; /* 6px */"),
                SimpleCss.of("inset-x-1\\.5", "left: 0.375rem; /* 6px */\n\tright: 0.375rem; /* 6px */"),
                SimpleCss.of("inset-y-1\\.5", "top: 0.375rem; /* 6px */\n\tbottom: 0.375rem; /* 6px */"),
                SimpleCss.of("top-1\\.5", "top: 0.375rem; /* 6px */"),
                SimpleCss.of("right-1\\.5", "right: 0.375rem; /* 6px */"),
                SimpleCss.of("bottom-1\\.5", "bottom: 0.375rem; /* 6px */"),
                SimpleCss.of("left-1\\.5", "left: 0.375rem; /* 6px */"),
                SimpleCss.of("inset-2", "top: 0.5rem; /* 8px */\n\tright: 0.5rem; /* 8px */\n\tbottom: 0.5rem; /* 8px */\n\tleft: 0.5rem; /* 8px */"),
                SimpleCss.of("inset-x-2", "left: 0.5rem; /* 8px */\n\tright: 0.5rem; /* 8px */"),
                SimpleCss.of("inset-y-2", "top: 0.5rem; /* 8px */\n\tbottom: 0.5rem; /* 8px */"),
                SimpleCss.of("top-2", "top: 0.5rem; /* 8px */"),
                SimpleCss.of("right-2", "right: 0.5rem; /* 8px */"),
                SimpleCss.of("bottom-2", "bottom: 0.5rem; /* 8px */"),
                SimpleCss.of("left-2", "left: 0.5rem; /* 8px */"),
                SimpleCss.of("inset-2\\.5", "top: 0.625rem; /* 10px */\n\tright: 0.625rem; /* 10px */\n\tbottom: 0.625rem; /* 10px */\n\tleft: 0.625rem; /* 10px */"),
                SimpleCss.of("inset-x-2\\.5", "left: 0.625rem; /* 10px */\n\tright: 0.625rem; /* 10px */"),
                SimpleCss.of("inset-y-2\\.5", "top: 0.625rem; /* 10px */\n\tbottom: 0.625rem; /* 10px */"),
                SimpleCss.of("top-2\\.5", "top: 0.625rem; /* 10px */"),
                SimpleCss.of("right-2\\.5", "right: 0.625rem; /* 10px */"),
                SimpleCss.of("bottom-2\\.5", "bottom: 0.625rem; /* 10px */"),
                SimpleCss.of("left-2\\.5", "left: 0.625rem; /* 10px */"),
                SimpleCss.of("inset-3", "top: 0.75rem; /* 12px */\n\tright: 0.75rem; /* 12px */\n\tbottom: 0.75rem; /* 12px */\n\tleft: 0.75rem; /* 12px */"),
                SimpleCss.of("inset-x-3", "left: 0.75rem; /* 12px */\n\tright: 0.75rem; /* 12px */"),
                SimpleCss.of("inset-y-3", "top: 0.75rem; /* 12px */\n\tbottom: 0.75rem; /* 12px */"),
                SimpleCss.of("top-3", "top: 0.75rem; /* 12px */"),
                SimpleCss.of("right-3", "right: 0.75rem; /* 12px */"),
                SimpleCss.of("bottom-3", "bottom: 0.75rem; /* 12px */"),
                SimpleCss.of("left-3", "left: 0.75rem; /* 12px */"),
                SimpleCss.of("inset-3\\.5", "top: 0.875rem; /* 14px */\n\tright: 0.875rem; /* 14px */\n\t bottom: 0.875rem; /* 14px */\n\tleft: 0.875rem; /* 14px */"),
                SimpleCss.of("inset-x-3\\.5", "left: 0.875rem; /* 14px */\n\tright: 0.875rem; /* 14px */"),
                SimpleCss.of("inset-y-3\\.5", "top: 0.875rem; /* 14px */bottom: 0.875rem; /* 14px */"),
                SimpleCss.of("top-3\\.5", "top: 0.875rem; /* 14px */"),
                SimpleCss.of("right-3\\.5", "right: 0.875rem; /* 14px */"),
                SimpleCss.of("bottom-3\\.5", "bottom: 0.875rem; /* 14px */"),
                SimpleCss.of("left-3\\.5", "left: 0.875rem; /* 14px */"),
                SimpleCss.of("inset-4", "top: 1rem; /* 16px */\n\tright: 1rem; /* 16px */\n\tbottom: 1rem; /* 16px */\n\tleft: 1rem; /* 16px */"),
                SimpleCss.of("inset-x-4", "left: 1rem; /* 16px */\n\tright: 1rem; /* 16px */"),
                SimpleCss.of("inset-y-4", "top: 1rem; /* 16px */\n\tbottom: 1rem; /* 16px */"),
                SimpleCss.of("top-4", "top: 1rem; /* 16px */"),
                SimpleCss.of("right-4", "right: 1rem; /* 16px */"),
                SimpleCss.of("bottom-4", "bottom: 1rem; /* 16px */"),
                SimpleCss.of("left-4", "left: 1rem; /* 16px */"),
                SimpleCss.of("inset-5", "top: 1.25rem; /* 20px */\n\tright: 1.25rem; /* 20px */\n\tbottom: 1.25rem; /* 20px */\n\tleft: 1.25rem; /* 20px */"),
                SimpleCss.of("inset-x-5", "left: 1.25rem; /* 20px */\n\tright: 1.25rem; /* 20px */"),
                SimpleCss.of("inset-y-5", "top: 1.25rem; /* 20px */\n\tbottom: 1.25rem; /* 20px */"),
                SimpleCss.of("top-5", "top: 1.25rem; /* 20px */"),
                SimpleCss.of("right-5", "right: 1.25rem; /* 20px */"),
                SimpleCss.of("bottom-5", "bottom: 1.25rem; /* 20px */"),
                SimpleCss.of("left-5", "left: 1.25rem; /* 20px */"),
                SimpleCss.of("inset-6", "top: 1.5rem; /* 24px */\n\tright: 1.5rem; /* 24px */\n\tbottom: 1.5rem; /* 24px */\n\tleft: 1.5rem; /* 24px */"),
                SimpleCss.of("inset-x-6", "left: 1.5rem; /* 24px */\n\tright: 1.5rem; /* 24px */"),
                SimpleCss.of("inset-y-6", "top: 1.5rem; /* 24px */\n\tbottom: 1.5rem; /* 24px */"),
                SimpleCss.of("top-6", "top: 1.5rem; /* 24px */"),
                SimpleCss.of("right-6", "right: 1.5rem; /* 24px */"),
                SimpleCss.of("bottom-6", "bottom: 1.5rem; /* 24px */"),
                SimpleCss.of("left-6", "left: 1.5rem; /* 24px */"),
                SimpleCss.of("inset-7", "top: 1.75rem; /* 28px */\n\tright: 1.75rem; /* 28px */\n\tbottom: 1.75rem; /* 28px */\n\tleft: 1.75rem; /* 28px */"),
                SimpleCss.of("inset-x-7", "left: 1.75rem; /* 28px */\n\tright: 1.75rem; /* 28px */"),
                SimpleCss.of("inset-y-7", "top: 1.75rem; /* 28px */\n\tbottom: 1.75rem; /* 28px */"),
                SimpleCss.of("top-7", "top: 1.75rem; /* 28px */"),
                SimpleCss.of("right-7", "right: 1.75rem; /* 28px */"),
                SimpleCss.of("bottom-7", "bottom: 1.75rem; /* 28px */"),
                SimpleCss.of("left-7", "left: 1.75rem; /* 28px */"),
                SimpleCss.of("inset-8", "top: 2rem; /* 32px */\n\tright: 2rem; /* 32px */\n\tbottom: 2rem; /* 32px */\n\tleft: 2rem; /* 32px */"),
                SimpleCss.of("inset-x-8", "left: 2rem; /* 32px */\n\tright: 2rem; /* 32px */"),
                SimpleCss.of("inset-y-8", "top: 2rem; /* 32px */\n\tbottom: 2rem; /* 32px */"),
                SimpleCss.of("top-8", "top: 2rem; /* 32px */"),
                SimpleCss.of("right-8", "right: 2rem; /* 32px */"),
                SimpleCss.of("bottom-8", "bottom: 2rem; /* 32px */"),
                SimpleCss.of("left-8", "left: 2rem; /* 32px */"),
                SimpleCss.of("inset-9", "top: 2.25rem; /* 36px */\n\tright: 2.25rem; /* 36px */\n\tbottom: 2.25rem; /* 36px */\n\tleft: 2.25rem; /* 36px */"),
                SimpleCss.of("inset-x-9", "left: 2.25rem; /* 36px */\n\tright: 2.25rem; /* 36px */"),
                SimpleCss.of("inset-y-9", "top: 2.25rem; /* 36px */\n\tbottom: 2.25rem; /* 36px */"),
                SimpleCss.of("top-9", "top: 2.25rem; /* 36px */"),
                SimpleCss.of("right-9", "right: 2.25rem; /* 36px */"),
                SimpleCss.of("bottom-9", "bottom: 2.25rem; /* 36px */"),
                SimpleCss.of("left-9", "left: 2.25rem; /* 36px */"),
                SimpleCss.of("inset-10", "top: 2.5rem; /* 40px */\n\tright: 2.5rem; /* 40px */\n\tbottom: 2.5rem; /* 40px */\n\tleft: 2.5rem; /* 40px */"),
                SimpleCss.of("inset-x-10", "left: 2.5rem; /* 40px */\n\tright: 2.5rem; /* 40px */"),
                SimpleCss.of("inset-y-10", "top: 2.5rem; /* 40px */\n\tbottom: 2.5rem; /* 40px */"),
                SimpleCss.of("top-10", "top: 2.5rem; /* 40px */"),
                SimpleCss.of("right-10", "right: 2.5rem; /* 40px */"),
                SimpleCss.of("bottom-10", "bottom: 2.5rem; /* 40px */"),
                SimpleCss.of("left-10", "left: 2.5rem; /* 40px */"),
                SimpleCss.of("inset-11", "top: 2.75rem; /* 44px */\n\tright: 2.75rem; /* 44px */\n\tbottom: 2.75rem; /* 44px */\n\tleft: 2.75rem; /* 44px */"),
                SimpleCss.of("inset-x-11", "left: 2.75rem; /* 44px */\n\tright: 2.75rem; /* 44px */"),
                SimpleCss.of("inset-y-11", "top: 2.75rem; /* 44px */\n\tbottom: 2.75rem; /* 44px */"),
                SimpleCss.of("top-11", "top: 2.75rem; /* 44px */"),
                SimpleCss.of("right-11", "right: 2.75rem; /* 44px */"),
                SimpleCss.of("bottom-11", "bottom: 2.75rem; /* 44px */"),
                SimpleCss.of("left-11", "left: 2.75rem; /* 44px */"),
                SimpleCss.of("inset-12", "top: 3rem; /* 48px */\n\tright: 3rem; /* 48px */\n\tbottom: 3rem; /* 48px */\n\tleft: 3rem; /* 48px */"),
                SimpleCss.of("inset-x-12", "left: 3rem; /* 48px */\n\tright: 3rem; /* 48px */"),
                SimpleCss.of("inset-y-12", "top: 3rem; /* 48px */\n\tbottom: 3rem; /* 48px */"),
                SimpleCss.of("top-12", "top: 3rem; /* 48px */"),
                SimpleCss.of("right-12", "right: 3rem; /* 48px */"),
                SimpleCss.of("bottom-12", "bottom: 3rem; /* 48px */"),
                SimpleCss.of("left-12", "left: 3rem; /* 48px */"),
                SimpleCss.of("inset-14", "top: 3.5rem; /* 56px */\n\tright: 3.5rem; /* 56px */\n\tbottom: 3.5rem; /* 56px */\n\tleft: 3.5rem; /* 56px */"),
                SimpleCss.of("inset-x-14", "left: 3.5rem; /* 56px */\n\tright: 3.5rem; /* 56px */"),
                SimpleCss.of("inset-y-14", "top: 3.5rem; /* 56px */\n\tbottom: 3.5rem; /* 56px */"),
                SimpleCss.of("top-14", "top: 3.5rem; /* 56px */"),
                SimpleCss.of("right-14", "right: 3.5rem; /* 56px */"),
                SimpleCss.of("bottom-14", "bottom: 3.5rem; /* 56px */"),
                SimpleCss.of("left-14", "left: 3.5rem; /* 56px */"),
                SimpleCss.of("inset-16", "top: 4rem; /* 64px */\n\tright: 4rem; /* 64px */\n\tbottom: 4rem; /* 64px */\n\tleft: 4rem; /* 64px */"),
                SimpleCss.of("inset-x-16", "left: 4rem; /* 64px */\n\tright: 4rem; /* 64px */"),
                SimpleCss.of("inset-y-16", "top: 4rem; /* 64px */\n\tbottom: 4rem; /* 64px */"),
                SimpleCss.of("top-16", "top: 4rem; /* 64px */"),
                SimpleCss.of("right-16", "right: 4rem; /* 64px */"),
                SimpleCss.of("bottom-16", "bottom: 4rem; /* 64px */"),
                SimpleCss.of("left-16", "left: 4rem; /* 64px */"),
                SimpleCss.of("inset-20", "top: 5rem; /* 80px */\n\tright: 5rem; /* 80px */\n\tbottom: 5rem; /* 80px */\n\tleft: 5rem; /* 80px */"),
                SimpleCss.of("inset-x-20", "left: 5rem; /* 80px */\n\tright: 5rem; /* 80px */"),
                SimpleCss.of("inset-y-20", "top: 5rem; /* 80px */\n\tbottom: 5rem; /* 80px */"),
                SimpleCss.of("top-20", "top: 5rem; /* 80px */"),
                SimpleCss.of("right-20", "right: 5rem; /* 80px */"),
                SimpleCss.of("bottom-20", "bottom: 5rem; /* 80px */"),
                SimpleCss.of("left-20", "left: 5rem; /* 80px */"),
                SimpleCss.of("inset-24", "top: 6rem; /* 96px */\n\tright: 6rem; /* 96px */\n\tbottom: 6rem; /* 96px */\n\tleft: 6rem; /* 96px */"),
                SimpleCss.of("inset-x-24", "left: 6rem; /* 96px */\n\tright: 6rem; /* 96px */"),
                SimpleCss.of("inset-y-24", "top: 6rem; /* 96px */\n\tbottom: 6rem; /* 96px */"),
                SimpleCss.of("top-24", "top: 6rem; /* 96px */"),
                SimpleCss.of("right-24", "right: 6rem; /* 96px */"),
                SimpleCss.of("bottom-24", "bottom: 6rem; /* 96px */"),
                SimpleCss.of("left-24", "left: 6rem; /* 96px */"),
                SimpleCss.of("inset-28", "top: 7rem; /* 112px */\n\tright: 7rem; /* 112px */\n\tbottom: 7rem; /* 112px */\n\tleft: 7rem; /* 112px */"),
                SimpleCss.of("inset-x-28", "left: 7rem; /* 112px */\n\tright: 7rem; /* 112px */"),
                SimpleCss.of("inset-y-28", "top: 7rem; /* 112px */\n\tbottom: 7rem; /* 112px */"),
                SimpleCss.of("top-28", "top: 7rem; /* 112px */"),
                SimpleCss.of("right-28", "right: 7rem; /* 112px */"),
                SimpleCss.of("bottom-28", "bottom: 7rem; /* 112px */"),
                SimpleCss.of("left-28", "left: 7rem; /* 112px */"),
                SimpleCss.of("inset-32", "top: 8rem; /* 128px */\n\tright: 8rem; /* 128px */\n\tbottom: 8rem; /* 128px */\n\tleft: 8rem; /* 128px */"),
                SimpleCss.of("inset-x-32", "left: 8rem; /* 128px */\n\tright: 8rem; /* 128px */"),
                SimpleCss.of("inset-y-32", "top: 8rem; /* 128px */\n\tbottom: 8rem; /* 128px */"),
                SimpleCss.of("top-32", "top: 8rem; /* 128px */"),
                SimpleCss.of("right-32", "right: 8rem; /* 128px */"),
                SimpleCss.of("bottom-32", "bottom: 8rem; /* 128px */"),
                SimpleCss.of("left-32", "left: 8rem; /* 128px */"),
                SimpleCss.of("inset-36", "top: 9rem; /* 144px */\n\tright: 9rem; /* 144px */\n\tbottom: 9rem; /* 144px */\n\tleft: 9rem; /* 144px */"),
                SimpleCss.of("inset-x-36", "left: 9rem; /* 144px */\n\tright: 9rem; /* 144px */"),
                SimpleCss.of("inset-y-36", "top: 9rem; /* 144px */\n\tbottom: 9rem; /* 144px */"),
                SimpleCss.of("top-36", "top: 9rem; /* 144px */"),
                SimpleCss.of("right-36", "right: 9rem; /* 144px */"),
                SimpleCss.of("bottom-36", "bottom: 9rem; /* 144px */"),
                SimpleCss.of("left-36", "left: 9rem; /* 144px */"),
                SimpleCss.of("inset-40", "top: 10rem; /* 160px */\n\tright: 10rem; /* 160px */\n\tbottom: 10rem; /* 160px */\n\tleft: 10rem; /* 160px */"),
                SimpleCss.of("inset-x-40", "left: 10rem; /* 160px */\n\tright: 10rem; /* 160px */"),
                SimpleCss.of("inset-y-40", "top: 10rem; /* 160px */\n\tbottom: 10rem; /* 160px */"),
                SimpleCss.of("top-40", "top: 10rem; /* 160px */"),
                SimpleCss.of("right-40", "right: 10rem; /* 160px */"),
                SimpleCss.of("bottom-40", "bottom: 10rem; /* 160px */"),
                SimpleCss.of("left-40", "left: 10rem; /* 160px */"),
                SimpleCss.of("inset-44", "top: 11rem; /* 176px */\n\tright: 11rem; /* 176px */\n\tbottom: 11rem; /* 176px */\n\tleft: 11rem; /* 176px */"),
                SimpleCss.of("inset-x-44", "left: 11rem; /* 176px */\n\tright: 11rem; /* 176px */"),
                SimpleCss.of("inset-y-44", "top: 11rem; /* 176px */bottom: 11rem; /* 176px */"),
                SimpleCss.of("top-44", "top: 11rem; /* 176px */"),
                SimpleCss.of("right-44", "right: 11rem; /* 176px */"),
                SimpleCss.of("bottom-44", "bottom: 11rem; /* 176px */"),
                SimpleCss.of("left-44", "left: 11rem; /* 176px */"),
                SimpleCss.of("inset-48", "top: 12rem; /* 192px */\n\tright: 12rem; /* 192px */\n\tbottom: 12rem; /* 192px */\n\tleft: 12rem; /* 192px */"),
                SimpleCss.of("inset-x-48", "left: 12rem; /* 192px */\n\tright: 12rem; /* 192px */"),
                SimpleCss.of("inset-y-48", "top: 12rem; /* 192px */\n\tbottom: 12rem; /* 192px */"),
                SimpleCss.of("top-48", "top: 12rem; /* 192px */"),
                SimpleCss.of("right-48", "right: 12rem; /* 192px */"),
                SimpleCss.of("bottom-48", "bottom: 12rem; /* 192px */"),
                SimpleCss.of("left-48", "left: 12rem; /* 192px */"),
                SimpleCss.of("inset-52", "top: 13rem; /* 208px */\n\tright: 13rem; /* 208px */\n\tbottom: 13rem; /* 208px */\n\tleft: 13rem; /* 208px */"),
                SimpleCss.of("inset-x-52", "left: 13rem; /* 208px */\n\tright: 13rem; /* 208px */"),
                SimpleCss.of("inset-y-52", "top: 13rem; /* 208px */\n\tbottom: 13rem; /* 208px */"),
                SimpleCss.of("top-52", "top: 13rem; /* 208px */"),
                SimpleCss.of("right-52", "right: 13rem; /* 208px */"),
                SimpleCss.of("bottom-52", "bottom: 13rem; /* 208px */"),
                SimpleCss.of("left-52", "left: 13rem; /* 208px */"),
                SimpleCss.of("inset-56", "top: 14rem; /* 224px */\n\tright: 14rem; /* 224px */\n\tbottom: 14rem; /* 224px */\n\tleft: 14rem; /* 224px */"),
                SimpleCss.of("inset-x-56", "left: 14rem; /* 224px */\n\tright: 14rem; /* 224px */"),
                SimpleCss.of("inset-y-56", "top: 14rem; /* 224px */\n\tbottom: 14rem; /* 224px */"),
                SimpleCss.of("top-56", "top: 14rem; /* 224px */"),
                SimpleCss.of("right-56", "right: 14rem; /* 224px */"),
                SimpleCss.of("bottom-56", "bottom: 14rem; /* 224px */"),
                SimpleCss.of("left-56", "left: 14rem; /* 224px */"),
                SimpleCss.of("inset-60", "top: 15rem; /* 240px */\n\tright: 15rem; /* 240px */\n\tbottom: 15rem; /* 240px */\n\tleft: 15rem; /* 240px */"),
                SimpleCss.of("inset-x-60", "left: 15rem; /* 240px */\n\tright: 15rem; /* 240px */"),
                SimpleCss.of("inset-y-60", "top: 15rem; /* 240px */\n\tbottom: 15rem; /* 240px */"),
                SimpleCss.of("top-60", "top: 15rem; /* 240px */"),
                SimpleCss.of("right-60", "right: 15rem; /* 240px */"),
                SimpleCss.of("bottom-60", "bottom: 15rem; /* 240px */"),
                SimpleCss.of("left-60", "left: 15rem; /* 240px */"),
                SimpleCss.of("inset-64", "top: 16rem; /* 256px */\n\tright: 16rem; /* 256px */\n\tbottom: 16rem; /* 256px */\n\tleft: 16rem; /* 256px */"),
                SimpleCss.of("inset-x-64", "left: 16rem; /* 256px */\n\tright: 16rem; /* 256px */"),
                SimpleCss.of("inset-y-64", "top: 16rem; /* 256px */\n\tbottom: 16rem; /* 256px */"),
                SimpleCss.of("top-64", "top: 16rem; /* 256px */"),
                SimpleCss.of("right-64", "right: 16rem; /* 256px */"),
                SimpleCss.of("bottom-64", "bottom: 16rem; /* 256px */"),
                SimpleCss.of("left-64", "left: 16rem; /* 256px */"),
                SimpleCss.of("inset-72", "top: 18rem; /* 288px */\n\tright: 18rem; /* 288px */\n\tbottom: 18rem; /* 288px */\n\tleft: 18rem; /* 288px */"),
                SimpleCss.of("inset-x-72", "left: 18rem; /* 288px */\n\tright: 18rem; /* 288px */"),
                SimpleCss.of("inset-y-72", "top: 18rem; /* 288px */\n\tbottom: 18rem; /* 288px */"),
                SimpleCss.of("top-72", "top: 18rem; /* 288px */"),
                SimpleCss.of("right-72", "right: 18rem; /* 288px */"),
                SimpleCss.of("bottom-72", "bottom: 18rem; /* 288px */"),
                SimpleCss.of("left-72", "left: 18rem; /* 288px */"),
                SimpleCss.of("inset-80", "top: 20rem; /* 320px */\n\tright: 20rem; /* 320px */\n\tbottom: 20rem; /* 320px */\n\tleft: 20rem; /* 320px */"),
                SimpleCss.of("inset-x-80", "left: 20rem; /* 320px */\n\tright: 20rem; /* 320px */"),
                SimpleCss.of("inset-y-80", "top: 20rem; /* 320px */\n\tbottom: 20rem; /* 320px */"),
                SimpleCss.of("top-80", "top: 20rem; /* 320px */"),
                SimpleCss.of("right-80", "right: 20rem; /* 320px */"),
                SimpleCss.of("bottom-80", "bottom: 20rem; /* 320px */"),
                SimpleCss.of("left-80", "left: 20rem; /* 320px */"),
                SimpleCss.of("inset-96", "top: 24rem; /* 384px */\n\tright: 24rem; /* 384px */\n\tbottom: 24rem; /* 384px */\n\tleft: 24rem; /* 384px */"),
                SimpleCss.of("inset-x-96", "left: 24rem; /* 384px */\n\tright: 24rem; /* 384px */"),
                SimpleCss.of("inset-y-96", "top: 24rem; /* 384px */\n\tbottom: 24rem; /* 384px */"),
                SimpleCss.of("top-96", "top: 24rem; /* 384px */"),
                SimpleCss.of("right-96", "right: 24rem; /* 384px */"),
                SimpleCss.of("bottom-96", "bottom: 24rem; /* 384px */"),
                SimpleCss.of("left-96", "left: 24rem; /* 384px */"),
                SimpleCss.of("inset-auto", "top: auto;\n\tright: auto;\n\tbottom: auto;\n\tleft: auto;"),
                SimpleCss.of("inset-1\\/2", "top: 50%;\n\tright: 50%;\n\tbottom: 50%;\n\tleft: 50%;"),
                SimpleCss.of("inset-1\\/3", "top: 33.333333%;\n\tright: 33.333333%;\n\tbottom: 33.333333%;\n\tleft: 33.333333%;"),
                SimpleCss.of("inset-2\\/3", "top: 66.666667%;\n\tright: 66.666667%;\n\tbottom: 66.666667%;\n\tleft: 66.666667%;"),
                SimpleCss.of("inset-1\\/4", "top: 25%;\n\tright: 25%;\n\tbottom: 25%;\n\tleft: 25%;"),
                SimpleCss.of("inset-2\\/4", "top: 50%;\n\tright: 50%;\n\tbottom: 50%;\n\tleft: 50%;"),
                SimpleCss.of("inset-3\\/4", "top: 75%;\n\tright: 75%;\n\tbottom: 75%;\n\tleft: 75%;"),
                SimpleCss.of("inset-full", "top: 100%;\n\tright: 100%;\n\tbottom: 100%;\n\tleft: 100%;"),
                SimpleCss.of("inset-x-auto", "left: auto;\n\tright: auto;"),
                SimpleCss.of("inset-x-1\\/2", "left: 50%;\n\tright: 50%;"),
                SimpleCss.of("inset-x-1\\/3", "left: 33.333333%;\n\tright: 33.333333%;"),
                SimpleCss.of("inset-x-2\\/3", "left: 66.666667%;\n\tright: 66.666667%;"),
                SimpleCss.of("inset-x-1\\/4", "left: 25%;\n\tright: 25%;"),
                SimpleCss.of("inset-x-2\\/4", "left: 50%;\n\tright: 50%;"),
                SimpleCss.of("inset-x-3\\/4", "left: 75%;\n\tright: 75%;"),
                SimpleCss.of("inset-x-full", "left: 100%;\n\tright: 100%;"),
                SimpleCss.of("inset-y-auto", "top: auto;\n\tbottom: auto;"),
                SimpleCss.of("inset-y-1\\/2", "top: 50%;\n\tbottom: 50%;"),
                SimpleCss.of("inset-y-1\\/3", "top: 33.333333%;\n\tbottom: 33.333333%;"),
                SimpleCss.of("inset-y-2\\/3", "top: 66.666667%;\n\tbottom: 66.666667%;"),
                SimpleCss.of("inset-y-1\\/4", "top: 25%;\n\tbottom: 25%;"),
                SimpleCss.of("inset-y-2\\/4", "top: 50%;\n\tbottom: 50%;"),
                SimpleCss.of("inset-y-3\\/4", "top: 75%;\n\tbottom: 75%;"),
                SimpleCss.of("inset-y-full", "top: 100%;\n\tbottom: 100%;"),
                SimpleCss.of("top-auto", "top: auto;"),
                SimpleCss.of("top-1\\/2", "top: 50%;"),
                SimpleCss.of("top-1\\/3", "top: 33.333333%;"),
                SimpleCss.of("top-2\\/3", "top: 66.666667%;"),
                SimpleCss.of("top-1\\/4", "top: 25%;"),
                SimpleCss.of("top-2\\/4", "top: 50%;"),
                SimpleCss.of("top-3\\/4", "top: 75%;"),
                SimpleCss.of("top-full", "top: 100%;"),
                SimpleCss.of("right-auto", "right: auto;"),
                SimpleCss.of("right-1\\/2", "right: 50%;"),
                SimpleCss.of("right-1\\/3", "right: 33.333333%;"),
                SimpleCss.of("right-2\\/3", "right: 66.666667%;"),
                SimpleCss.of("right-1\\/4", "right: 25%;"),
                SimpleCss.of("right-2\\/4", "right: 50%;"),
                SimpleCss.of("right-3\\/4", "right: 75%;"),
                SimpleCss.of("right-full", "right: 100%;"),
                SimpleCss.of("bottom-auto", "bottom: auto;"),
                SimpleCss.of("bottom-1\\/2", "bottom: 50%;"),
                SimpleCss.of("bottom-1\\/3", "bottom: 33.333333%;"),
                SimpleCss.of("bottom-2\\/3", "bottom: 66.666667%;"),
                SimpleCss.of("bottom-1\\/4", "bottom: 25%;"),
                SimpleCss.of("bottom-2\\/4", "bottom: 50%;"),
                SimpleCss.of("bottom-3\\/4", "bottom: 75%;"),
                SimpleCss.of("bottom-full", "bottom: 100%;"),
                SimpleCss.of("left-auto", "left: auto;"),
                SimpleCss.of("left-1\\/2", "left: 50%;"),
                SimpleCss.of("left-1\\/3", "left: 33.333333%;"),
                SimpleCss.of("left-2\\/3", "left: 66.666667%;"),
                SimpleCss.of("left-1\\/4", "left: 25%;"),
                SimpleCss.of("left-2\\/4", "left: 50%;"),
                SimpleCss.of("left-3\\/4", "left: 75%;"),
                SimpleCss.of("left-full", "left: 100%;"),

                SimpleCss.of("visible", "visibility: visible;"),
                SimpleCss.of("invisible", "visibility: hidden;"),

                SimpleCss.of("border-solid", "border-style: solid;"),
                SimpleCss.of("border-dashed", "border-style: dashed;"),
                SimpleCss.of("border-dotted", "border-style: dotted;"),
                SimpleCss.of("border-double", "border-style: double;"),
                SimpleCss.of("border-hidden", "border-style: hidden;"),
                SimpleCss.of("border-none", "border-style: none;"),

                SimpleCss.of("divide-solid > * + *", "border-style: solid;"),
                SimpleCss.of("divide-dashed > * + *", "border-style: dashed;"),
                SimpleCss.of("divide-dotted > * + *", "border-style: dotted;"),
                SimpleCss.of("divide-double > * + *", "border-style: double;"),
                SimpleCss.of("divide-none > * + *", "border-style: none;"),

                SimpleCss.of("outline-none", "outline: 2px solid transparent;\n\toutline-offset: 2px;"),
                SimpleCss.of("outline", "outline-style: solid;"),
                SimpleCss.of("outline-dashed", "outline-style: dashed;"),
                SimpleCss.of("outline-dotted", "outline-style: dotted;"),
                SimpleCss.of("outline-double", "outline-style: double;"),
                SimpleCss.of("outline-hidden", "outline-style: hidden;"),

                SimpleCss.of("shadow-sm", "box-shadow: 0 1px 2px 0 var(--dui-shadow-clr);"),
                SimpleCss.of("shadow", "box-shadow: 0 1px 3px 0 var(--dui-shadow-clr), 0 1px 2px -1px var(--dui-shadow-clr);"),
                SimpleCss.of("shadow-md", "box-shadow: 0 4px 6px -1px var(--dui-shadow-clr), 0 2px 4px -2px var(--dui-shadow-clr);"),
                SimpleCss.of("shadow-lg", "box-shadow: 0 10px 15px -3px var(--dui-shadow-clr), 0 4px 6px -4px var(--dui-shadow-clr);"),
                SimpleCss.of("shadow-xl", "box-shadow: 0 20px 25px -5px var(--dui-shadow-clr), 0 8px 10px -6px var(--dui-shadow-clr);"),
                SimpleCss.of("shadow-2xl", "box-shadow: 0 25px 50px -12px var(--dui-shadow-clr);"),
                SimpleCss.of("shadow-inner", "box-shadow: inset 0 2px 4px 0 var(--dui-shadow-clr);"),
                SimpleCss.of("shadow-none", "box-shadow: 0 0 var(--dui-shadow-clr);"),

                SimpleCss.of("cursor-auto", "cursor: auto;"),
                SimpleCss.of("cursor-default", "cursor: default;"),
                SimpleCss.of("cursor-pointer", "cursor: pointer;"),
                SimpleCss.of("cursor-wait", "cursor: wait;"),
                SimpleCss.of("cursor-text", "cursor: text;"),
                SimpleCss.of("cursor-move", "cursor: move;"),
                SimpleCss.of("cursor-help", "cursor: help;"),
                SimpleCss.of("cursor-not-allowed", "cursor: not-allowed;"),
                SimpleCss.of("cursor-none", "cursor: none;"),
                SimpleCss.of("cursor-context-menu", "cursor: context-menu;"),
                SimpleCss.of("cursor-progress", "cursor: progress;"),
                SimpleCss.of("cursor-cell", "cursor: cell;"),
                SimpleCss.of("cursor-crosshair", "cursor: crosshair;"),
                SimpleCss.of("cursor-vertical-text", "cursor: vertical-text;"),
                SimpleCss.of("cursor-alias", "cursor: alias;"),
                SimpleCss.of("cursor-copy", "cursor: copy;"),
                SimpleCss.of("cursor-no-drop", "cursor: no-drop;"),
                SimpleCss.of("cursor-grab", "cursor: grab;"),
                SimpleCss.of("cursor-grabbing", "cursor: grabbing;"),
                SimpleCss.of("cursor-all-scroll", "cursor: all-scroll;"),
                SimpleCss.of("cursor-col-resize", "cursor: col-resize;"),
                SimpleCss.of("cursor-row-resize", "cursor: row-resize;"),
                SimpleCss.of("cursor-n-resize", "cursor: n-resize;"),
                SimpleCss.of("cursor-e-resize", "cursor: e-resize;"),
                SimpleCss.of("cursor-s-resize", "cursor: s-resize;"),
                SimpleCss.of("cursor-w-resize", "cursor: w-resize;"),
                SimpleCss.of("cursor-ne-resize", "cursor: ne-resize;"),
                SimpleCss.of("cursor-nw-resize", "cursor: nw-resize;"),
                SimpleCss.of("cursor-se-resize", "cursor: se-resize;"),
                SimpleCss.of("cursor-sw-resize", "cursor: sw-resize;"),
                SimpleCss.of("cursor-ew-resize", "cursor: ew-resize;"),
                SimpleCss.of("cursor-ns-resize", "cursor: ns-resize;"),
                SimpleCss.of("cursor-nesw-resize", "cursor: nesw-resize;"),
                SimpleCss.of("cursor-nwse-resize", "cursor: nwse-resize;"),
                SimpleCss.of("cursor-zoom-in", "cursor: zoom-in;"),
                SimpleCss.of("cursor-zoom-out", "cursor: zoom-out;"),
                SimpleCss.of("resize-none", "resize: none;"),
                SimpleCss.of("resize-y", "resize: vertical;"),
                SimpleCss.of("resize-x", "resize: horizontal;"),
                SimpleCss.of("resize", "resize: both;"),
                SimpleCss.of("scroll-auto", "scroll-behavior: auto;"),
                SimpleCss.of("scroll-smooth", "scroll-behavior: smooth;")


        );


        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
        configuration.setClassForTemplateLoading(SimpleCssGenerator.class, "/");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        configuration.setFallbackOnNullLoopVariable(false);

        Template themeTemplate = configuration.getTemplate("SimpleCssTemplate.ftl");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("displayStyles", displayStyles);

        FileWriter fileWriter = getFileWriter("display.css");

        themeTemplate.process(parameters, fileWriter);

    }

    private static FileWriter getFileWriter(String name) throws IOException {
        Path source = Paths.get(SimpleCssGenerator.class.getResource("/").getPath());

        File file = Paths.get(source.toAbsolutePath().toString(), name).toFile();
        FileWriter fileWriter = new FileWriter(file);
        return fileWriter;
    }

}
