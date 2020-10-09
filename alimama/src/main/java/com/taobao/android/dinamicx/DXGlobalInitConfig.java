package com.taobao.android.dinamicx;

import com.taobao.android.dinamicx.expression.parser.IDXDataParser;
import com.taobao.android.dinamicx.log.IDXRemoteDebugLog;
import com.taobao.android.dinamicx.model.DXLongSparseArray;
import com.taobao.android.dinamicx.monitor.DXAbsUmbrella;
import com.taobao.android.dinamicx.monitor.IDXAppMonitor;
import com.taobao.android.dinamicx.template.download.IDXDownloader;
import com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode;
import com.taobao.android.dinamicx.widget.IDXWebImageInterface;

public final class DXGlobalInitConfig {
    public static final int ORIENTATION_AUTO = 3;
    public static final int ORIENTATION_LANDSCAPE = 2;
    public static final int ORIENTATION_PORTRAIT = 1;
    protected IDXAppMonitor appMonitor;
    protected IDXDarkModeInterface dxDarkModeInterface;
    protected DXLongSparseArray<IDXDataParser> dxDataParserMap;
    protected IDXDownloader dxDownloader;
    protected DXLongSparseArray<IDXEventHandler> dxEventHandlerMap;
    protected IDXWebImageInterface dxWebImageInterface;
    protected DXLongSparseArray<IDXBuilderWidgetNode> dxWidgetMap;
    protected boolean enableDarkModeSupport;
    protected boolean isDebug;
    protected IDXRemoteDebugLog remoteDebugLog;
    protected int screenOrientation;
    protected DXAbsUmbrella umbrellaImpl;

    private DXGlobalInitConfig(Builder builder) {
        this.dxEventHandlerMap = builder.dxEventHandlerMap;
        this.dxDataParserMap = builder.dxDataParserMap;
        this.dxWidgetMap = builder.dxWidgetMap;
        this.dxDownloader = builder.dxDownloader;
        this.appMonitor = builder.appMonitor;
        this.remoteDebugLog = builder.remoteDebugLog;
        this.dxWebImageInterface = builder.dxWebImageInterface;
        this.dxDarkModeInterface = builder.dxDarkModeInterface;
        this.isDebug = builder.isDebug;
        this.enableDarkModeSupport = builder.enableDarkModeSupport;
        this.screenOrientation = builder.screenOrientation;
        this.umbrellaImpl = builder.umbrellaImpl;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public IDXAppMonitor appMonitor;
        /* access modifiers changed from: private */
        public IDXDarkModeInterface dxDarkModeInterface;
        /* access modifiers changed from: private */
        public DXLongSparseArray<IDXDataParser> dxDataParserMap;
        /* access modifiers changed from: private */
        public IDXDownloader dxDownloader;
        /* access modifiers changed from: private */
        public DXLongSparseArray<IDXEventHandler> dxEventHandlerMap;
        /* access modifiers changed from: private */
        public IDXWebImageInterface dxWebImageInterface;
        /* access modifiers changed from: private */
        public DXLongSparseArray<IDXBuilderWidgetNode> dxWidgetMap;
        protected boolean enableDarkModeSupport = false;
        /* access modifiers changed from: private */
        public boolean isDebug;
        /* access modifiers changed from: private */
        public IDXRemoteDebugLog remoteDebugLog;
        /* access modifiers changed from: private */
        public int screenOrientation;
        /* access modifiers changed from: private */
        public DXAbsUmbrella umbrellaImpl;

        public Builder withDxEventHandlerMap(DXLongSparseArray<IDXEventHandler> dXLongSparseArray) {
            this.dxEventHandlerMap = dXLongSparseArray;
            return this;
        }

        public Builder withDxDataParserMap(DXLongSparseArray<IDXDataParser> dXLongSparseArray) {
            this.dxDataParserMap = dXLongSparseArray;
            return this;
        }

        public Builder withDxWidgetMap(DXLongSparseArray<IDXBuilderWidgetNode> dXLongSparseArray) {
            this.dxWidgetMap = dXLongSparseArray;
            return this;
        }

        public Builder withDxDownloader(IDXDownloader iDXDownloader) {
            this.dxDownloader = iDXDownloader;
            return this;
        }

        public Builder withAppMonitor(IDXAppMonitor iDXAppMonitor) {
            this.appMonitor = iDXAppMonitor;
            return this;
        }

        public Builder withRemoteDebugLog(IDXRemoteDebugLog iDXRemoteDebugLog) {
            this.remoteDebugLog = iDXRemoteDebugLog;
            return this;
        }

        public Builder withWebImageInterface(IDXWebImageInterface iDXWebImageInterface) {
            this.dxWebImageInterface = iDXWebImageInterface;
            return this;
        }

        public Builder withDebug(boolean z) {
            this.isDebug = z;
            return this;
        }

        public Builder withScreenOrientation(int i) {
            this.screenOrientation = i;
            return this;
        }

        public Builder withUmbrellaImpl(DXAbsUmbrella dXAbsUmbrella) {
            this.umbrellaImpl = dXAbsUmbrella;
            return this;
        }

        public Builder withDarkModeInterface(IDXDarkModeInterface iDXDarkModeInterface) {
            this.dxDarkModeInterface = iDXDarkModeInterface;
            return this;
        }

        public Builder withEnableDarkModeSupport(boolean z) {
            this.enableDarkModeSupport = z;
            return this;
        }

        public DXGlobalInitConfig build() {
            return new DXGlobalInitConfig(this);
        }
    }
}
