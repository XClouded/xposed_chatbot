package com.taobao.android.dinamicx;

import com.taobao.android.dinamicx.widget.utils.DXScreenTool;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DXRenderOptions {
    public static final DXRenderOptions DEFAULT_PRERENDER_OPTIONS = new Builder().withPreType(2).withToStage(8).build();
    public static final DXRenderOptions DEFAULT_RENDER_OPTIONS = new Builder().build();
    public static final int NORMAL = 0;
    public static final int PRE_FETCH = 1;
    public static final int PRE_RENDER = 2;
    public static final int SIMPLE = 3;
    private int fromStage;
    private int heightSpec;
    private boolean isCanceled;
    private boolean isControlEvent;
    @Deprecated
    private Object objectUserContext;
    private int renderType;
    private int toStage;
    private DXUserContext userContext;
    private int widthSpec;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DXRenderType {
    }

    public int getWidthSpec() {
        if (this.widthSpec == 0) {
            return DXScreenTool.getDefaultWidthSpec();
        }
        return this.widthSpec;
    }

    public int getHeightSpec() {
        if (this.heightSpec == 0) {
            return DXScreenTool.getDefaultHeightSpec();
        }
        return this.heightSpec;
    }

    public DXUserContext getUserContext() {
        return this.userContext;
    }

    public Object getObjectUserContext() {
        return this.objectUserContext;
    }

    public boolean isControlEvent() {
        return this.isControlEvent;
    }

    public boolean isCanceled() {
        return this.isCanceled;
    }

    public void setCanceled(boolean z) {
        this.isCanceled = z;
    }

    public int getRenderType() {
        return this.renderType;
    }

    public int getFromStage() {
        return this.fromStage;
    }

    public int getToStage() {
        return this.toStage;
    }

    private DXRenderOptions(Builder builder) {
        this.widthSpec = builder.widthSpec;
        this.heightSpec = builder.heightSpec;
        this.userContext = builder.userContext;
        this.objectUserContext = builder.objectUserContext;
        this.isControlEvent = builder.isControlEvent;
        this.isCanceled = builder.isCanceled;
        this.fromStage = builder.fromStage;
        this.toStage = builder.toStage;
        this.renderType = builder.renderType;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public int fromStage = 0;
        /* access modifiers changed from: private */
        public int heightSpec = DXScreenTool.getDefaultHeightSpec();
        /* access modifiers changed from: private */
        public boolean isCanceled;
        /* access modifiers changed from: private */
        public boolean isControlEvent;
        /* access modifiers changed from: private */
        public Object objectUserContext;
        /* access modifiers changed from: private */
        public int renderType;
        /* access modifiers changed from: private */
        public int toStage = 8;
        /* access modifiers changed from: private */
        public DXUserContext userContext;
        /* access modifiers changed from: private */
        public int widthSpec = DXScreenTool.getDefaultWidthSpec();

        public Builder withWidthSpec(int i) {
            this.widthSpec = i;
            return this;
        }

        public Builder withHeightSpec(int i) {
            this.heightSpec = i;
            return this;
        }

        public Builder withUserContext(DXUserContext dXUserContext) {
            this.userContext = dXUserContext;
            return this;
        }

        public Builder withObjectUserContext(Object obj) {
            this.objectUserContext = obj;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder withIsControlEvent(boolean z) {
            this.isControlEvent = z;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder withIsCanceled(boolean z) {
            this.isCanceled = z;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder withPreType(int i) {
            this.renderType = i;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder withFromStage(int i) {
            this.fromStage = i;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder withToStage(int i) {
            this.toStage = i;
            return this;
        }

        public DXRenderOptions build() {
            return new DXRenderOptions(this);
        }
    }
}
