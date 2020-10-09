package com.alibaba.android.umbrella.link.export;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface UMLLCons {
    public static final int ACTION_TYPE_EXPOSURE = 1;
    public static final int ACTION_TYPE_TOUCH = 0;
    public static final String FEATURE_TYPE_DEBUG = "DEBUG";
    public static final String FEATURE_TYPE_DX = "DinamicX";
    public static final String FEATURE_TYPE_MSOA = "MSOA";
    public static final String FEATURE_TYPE_MTOP = "Mtop";
    public static final String FEATURE_TYPE_PAGE = "Page";
    public static final String FEATURE_TYPE_UI_ACTION = "UIEvent";
    public static final String FEATURE_TYPE_WEEX = "Weex";
    public static final int PAGE_EVENT_APPEAR = 3;
    public static final int PAGE_EVENT_APPEND = 6;
    public static final int PAGE_EVENT_DISAPPEAR = 4;
    public static final int PAGE_EVENT_ENTER = 0;
    public static final int PAGE_EVENT_LEAVE = 1;
    public static final int PAGE_EVENT_LOAD = 2;
    public static final int PAGE_EVENT_RELOAD = 5;
    public static final int PAGE_TYPE_ALERT = 2;
    public static final int PAGE_TYPE_DEFAULT = 0;
    public static final int PAGE_TYPE_POPLAYER = 1;
    public static final int VIEW_STATE_EMPTY = 2;
    public static final int VIEW_STATE_NORMAL = 0;
    public static final int VIEW_STATE_NULL = 1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PageEvent {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PageType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewState {
    }
}
