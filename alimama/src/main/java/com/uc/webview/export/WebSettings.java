package com.uc.webview.export;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.ReflectionUtil;

@Api
/* compiled from: U4Source */
public abstract class WebSettings {
    public static final int COOKIE_TYPE_HYBRID = 4;
    public static final int COOKIE_TYPE_SYSTEM = 1;
    public static final int COOKIE_TYPE_UC = 2;
    public static final int COOKIE_TYPE_UC_ENCRYPT = 3;
    public static final int LOAD_CACHE_ELSE_NETWORK = 1;
    public static final int LOAD_CACHE_ONLY = 3;
    public static final int LOAD_DEFAULT = -1;
    @Deprecated
    public static final int LOAD_NORMAL = 0;
    public static final int LOAD_NO_CACHE = 2;
    public static final int MENU_ITEM_NONE = 0;
    public static final int MENU_ITEM_PROCESS_TEXT = 4;
    public static final int MENU_ITEM_SHARE = 1;
    public static final int MENU_ITEM_WEB_SEARCH = 2;
    public static final int MIXED_CONTENT_ALWAYS_ALLOW = 0;
    public static final int MIXED_CONTENT_COMPATIBILITY_MODE = 2;
    public static final int MIXED_CONTENT_NEVER_ALLOW = 1;
    private String a = "";
    public android.webkit.WebSettings mSettings = null;

    @Api
    /* compiled from: U4Source */
    public enum LayoutAlgorithm {
        NORMAL,
        SINGLE_COLUMN,
        NARROW_COLUMNS,
        TEXT_AUTOSIZING
    }

    @Api
    /* compiled from: U4Source */
    public enum PluginState {
        ON,
        ON_DEMAND,
        OFF
    }

    @Api
    /* compiled from: U4Source */
    public enum RenderPriority {
        NORMAL,
        HIGH,
        LOW
    }

    @Api
    /* compiled from: U4Source */
    public enum TextSize {
        SMALLEST(50),
        SMALLER(75),
        NORMAL(100),
        LARGER(150),
        LARGEST(200);
        
        public int value;

        private TextSize(int i) {
            this.value = i;
        }
    }

    @Api
    /* compiled from: U4Source */
    public enum ZoomDensity {
        FAR(150),
        MEDIUM(100),
        CLOSE(75);
        
        int a;

        private ZoomDensity(int i) {
            this.a = i;
        }

        public final int getValue() {
            return this.a;
        }
    }

    public void setSupportZoom(boolean z) {
        this.mSettings.setSupportZoom(z);
    }

    public boolean supportZoom() {
        return this.mSettings.supportZoom();
    }

    public void setMediaPlaybackRequiresUserGesture(boolean z) {
        ReflectionUtil.invokeNoThrow((Object) this.mSettings, "setMediaPlaybackRequiresUserGesture", new Class[]{Boolean.TYPE}, new Object[]{Boolean.valueOf(z)});
    }

    public boolean getMediaPlaybackRequiresUserGesture() {
        Boolean bool = (Boolean) ReflectionUtil.invokeNoThrow((Object) this.mSettings, "getMediaPlaybackRequiresUserGesture");
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public void setBuiltInZoomControls(boolean z) {
        this.mSettings.setBuiltInZoomControls(z);
    }

    public boolean getBuiltInZoomControls() {
        return this.mSettings.getBuiltInZoomControls();
    }

    @TargetApi(11)
    public void setDisplayZoomControls(boolean z) {
        if (Build.VERSION.SDK_INT >= 11) {
            this.mSettings.setDisplayZoomControls(z);
        }
    }

    @TargetApi(11)
    public boolean getDisplayZoomControls() {
        if (Build.VERSION.SDK_INT >= 11) {
            return this.mSettings.getDisplayZoomControls();
        }
        return false;
    }

    public void setAllowFileAccess(boolean z) {
        this.mSettings.setAllowFileAccess(z);
    }

    public boolean getAllowFileAccess() {
        return this.mSettings.getAllowFileAccess();
    }

    @TargetApi(11)
    public void setAllowContentAccess(boolean z) {
        if (Build.VERSION.SDK_INT >= 11) {
            this.mSettings.setAllowContentAccess(z);
        }
    }

    @TargetApi(11)
    public boolean getAllowContentAccess() {
        return Build.VERSION.SDK_INT >= 11 && this.mSettings.getAllowContentAccess();
    }

    public void setLoadWithOverviewMode(boolean z) {
        this.mSettings.setLoadWithOverviewMode(z);
    }

    public boolean getLoadWithOverviewMode() {
        return this.mSettings.getLoadWithOverviewMode();
    }

    @Deprecated
    public void setUseWebViewBackgroundForOverscrollBackground(boolean z) {
        ReflectionUtil.invokeNoThrow((Object) this.mSettings, "setUseWebViewBackgroundForOverscrollBackground", new Class[]{Boolean.TYPE}, new Object[]{Boolean.valueOf(z)});
    }

    @Deprecated
    public boolean getUseWebViewBackgroundForOverscrollBackground() {
        Boolean bool = (Boolean) ReflectionUtil.invokeNoThrow((Object) this.mSettings, "getUseWebViewBackgroundForOverscrollBackground");
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public void setSaveFormData(boolean z) {
        this.mSettings.setSaveFormData(z);
    }

    public boolean getSaveFormData() {
        return this.mSettings.getSaveFormData();
    }

    @Deprecated
    public void setSavePassword(boolean z) {
        ReflectionUtil.invokeNoThrow((Object) this.mSettings, "setSavePassword", new Class[]{Boolean.class}, new Object[]{Boolean.valueOf(z)});
    }

    @Deprecated
    public boolean getSavePassword() {
        Boolean bool = (Boolean) ReflectionUtil.invokeNoThrow((Object) this.mSettings, "getSavePassword");
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    @TargetApi(14)
    public synchronized void setTextZoom(int i) {
    }

    @TargetApi(14)
    public synchronized int getTextZoom() {
        if (Build.VERSION.SDK_INT < 14) {
            return 0;
        }
        return this.mSettings.getTextZoom();
    }

    public synchronized void setUseWideViewPort(boolean z) {
        this.mSettings.setUseWideViewPort(z);
    }

    public synchronized boolean getUseWideViewPort() {
        return this.mSettings.getUseWideViewPort();
    }

    public synchronized void setSupportMultipleWindows(boolean z) {
        this.mSettings.setSupportMultipleWindows(z);
    }

    public synchronized boolean supportMultipleWindows() {
        return this.mSettings.supportMultipleWindows();
    }

    public synchronized void setLayoutAlgorithm(LayoutAlgorithm layoutAlgorithm) {
        this.mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.valueOf(layoutAlgorithm.name()));
    }

    public synchronized LayoutAlgorithm getLayoutAlgorithm() {
        return LayoutAlgorithm.valueOf(this.mSettings.getLayoutAlgorithm().name());
    }

    public synchronized void setStandardFontFamily(String str) {
        this.mSettings.setStandardFontFamily(str);
    }

    public synchronized String getStandardFontFamily() {
        return this.mSettings.getStandardFontFamily();
    }

    public synchronized void setFixedFontFamily(String str) {
        this.mSettings.setFixedFontFamily(str);
    }

    public synchronized String getFixedFontFamily() {
        return this.mSettings.getFixedFontFamily();
    }

    public synchronized void setSansSerifFontFamily(String str) {
        this.mSettings.setSansSerifFontFamily(str);
    }

    public synchronized String getSansSerifFontFamily() {
        return this.mSettings.getSansSerifFontFamily();
    }

    public synchronized void setSerifFontFamily(String str) {
        this.mSettings.setSerifFontFamily(str);
    }

    public synchronized String getSerifFontFamily() {
        return this.mSettings.getSerifFontFamily();
    }

    public synchronized void setCursiveFontFamily(String str) {
        this.mSettings.setCursiveFontFamily(str);
    }

    public synchronized String getCursiveFontFamily() {
        return this.mSettings.getCursiveFontFamily();
    }

    public synchronized void setFantasyFontFamily(String str) {
        this.mSettings.setFantasyFontFamily(str);
    }

    public synchronized String getFantasyFontFamily() {
        return this.mSettings.getFantasyFontFamily();
    }

    public synchronized void setMinimumFontSize(int i) {
        this.mSettings.setMinimumFontSize(i);
    }

    public synchronized int getMinimumFontSize() {
        return this.mSettings.getMinimumFontSize();
    }

    public synchronized void setMinimumLogicalFontSize(int i) {
        this.mSettings.setMinimumLogicalFontSize(i);
    }

    public synchronized int getMinimumLogicalFontSize() {
        return this.mSettings.getMinimumLogicalFontSize();
    }

    public synchronized void setDefaultFontSize(int i) {
        this.mSettings.setDefaultFontSize(i);
    }

    public synchronized int getDefaultFontSize() {
        return this.mSettings.getDefaultFontSize();
    }

    public synchronized void setDefaultFixedFontSize(int i) {
        this.mSettings.setDefaultFixedFontSize(i);
    }

    public synchronized int getDefaultFixedFontSize() {
        return this.mSettings.getDefaultFixedFontSize();
    }

    public synchronized void setLoadsImagesAutomatically(boolean z) {
        this.mSettings.setLoadsImagesAutomatically(z);
    }

    public synchronized boolean getLoadsImagesAutomatically() {
        return this.mSettings.getLoadsImagesAutomatically();
    }

    public synchronized void setBlockNetworkImage(boolean z) {
        this.mSettings.setBlockNetworkImage(z);
    }

    public synchronized boolean getBlockNetworkImage() {
        return this.mSettings.getBlockNetworkImage();
    }

    public synchronized void setBlockNetworkLoads(boolean z) {
        this.mSettings.setBlockNetworkLoads(z);
    }

    public synchronized boolean getBlockNetworkLoads() {
        return this.mSettings.getBlockNetworkLoads();
    }

    public synchronized void setJavaScriptEnabled(boolean z) {
        this.mSettings.setJavaScriptEnabled(z);
    }

    public void setAllowUniversalAccessFromFileURLs(boolean z) {
        ReflectionUtil.invokeNoThrow((Object) this.mSettings, "setAllowUniversalAccessFromFileURLs", new Class[]{Boolean.TYPE}, new Object[]{Boolean.valueOf(z)});
    }

    public void setAllowFileAccessFromFileURLs(boolean z) {
        ReflectionUtil.invokeNoThrow((Object) this.mSettings, "setAllowFileAccessFromFileURLs", new Class[]{Boolean.TYPE}, new Object[]{Boolean.valueOf(z)});
    }

    public synchronized void setDatabasePath(String str) {
        ReflectionUtil.invokeNoThrow((Object) this.mSettings, "setDatabasePath", new Class[]{String.class}, new Object[]{str});
    }

    public synchronized void setGeolocationDatabasePath(String str) {
        this.mSettings.setGeolocationDatabasePath(str);
    }

    public synchronized void setAppCacheEnabled(boolean z) {
        this.mSettings.setAppCacheEnabled(z);
    }

    public synchronized void setAppCachePath(String str) {
        this.mSettings.setAppCachePath(str);
    }

    public synchronized void setDatabaseEnabled(boolean z) {
        this.mSettings.setDatabaseEnabled(z);
    }

    public synchronized void setDomStorageEnabled(boolean z) {
        this.mSettings.setDomStorageEnabled(z);
    }

    public synchronized boolean getDomStorageEnabled() {
        return this.mSettings.getDomStorageEnabled();
    }

    public synchronized boolean getDatabaseEnabled() {
        return this.mSettings.getDatabaseEnabled();
    }

    public synchronized void setGeolocationEnabled(boolean z) {
        this.mSettings.setGeolocationEnabled(z);
    }

    public synchronized boolean getJavaScriptEnabled() {
        return this.mSettings.getJavaScriptEnabled();
    }

    public boolean getAllowUniversalAccessFromFileURLs() {
        Boolean bool = (Boolean) ReflectionUtil.invokeNoThrow((Object) this.mSettings, "getAllowUniversalAccessFromFileURLs");
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public boolean getAllowFileAccessFromFileURLs() {
        Boolean bool = (Boolean) ReflectionUtil.invokeNoThrow((Object) this.mSettings, "getAllowFileAccessFromFileURLs");
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public synchronized void setJavaScriptCanOpenWindowsAutomatically(boolean z) {
        this.mSettings.setJavaScriptCanOpenWindowsAutomatically(z);
    }

    public synchronized boolean getJavaScriptCanOpenWindowsAutomatically() {
        return this.mSettings.getJavaScriptCanOpenWindowsAutomatically();
    }

    public synchronized void setDefaultTextEncodingName(String str) {
        this.mSettings.setDefaultTextEncodingName(str);
    }

    public synchronized String getDefaultTextEncodingName() {
        return this.mSettings.getDefaultTextEncodingName();
    }

    public synchronized void setUserAgentString(String str) {
        this.mSettings.setUserAgentString(str);
    }

    public synchronized String getUserAgentString() {
        return this.mSettings.getUserAgentString();
    }

    public static String getDefaultUserAgent(Context context) {
        return SDKFactory.f(context);
    }

    public void setNeedInitialFocus(boolean z) {
        this.mSettings.setNeedInitialFocus(z);
    }

    @Deprecated
    public synchronized void setAppCacheMaxSize(long j) {
        Log.w("WebSettings", "setAppCacheMaxSize Deprecated");
    }

    @Deprecated
    public synchronized void setRenderPriority(RenderPriority renderPriority) {
        Log.w("WebSettings", "setRenderPriority Deprecated");
    }

    public void setCacheMode(int i) {
        this.mSettings.setCacheMode(i);
    }

    public int getCacheMode() {
        return this.mSettings.getCacheMode();
    }

    public synchronized void setPreCacheScope(String str) {
        this.a = str;
    }

    public synchronized String getPreCacheScope() {
        return this.a;
    }

    public void setMixedContentMode(int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            ReflectionUtil.invokeNoThrow((Object) this.mSettings, "setMixedContentMode", new Class[]{Integer.TYPE}, new Object[]{Integer.valueOf(i)});
        }
    }

    public int getMixedContentMode() {
        Integer num;
        if (Build.VERSION.SDK_INT < 21 || (num = (Integer) ReflectionUtil.invokeNoThrow((Object) this.mSettings, "getMixedContentMode")) == null) {
            return 0;
        }
        return num.intValue();
    }

    @TargetApi(23)
    public void setOffscreenPreRaster(boolean z) {
        if (Build.VERSION.SDK_INT >= 23) {
            ReflectionUtil.invokeNoThrow((Object) this.mSettings, "setOffscreenPreRaster", new Class[]{Boolean.TYPE}, new Object[]{Boolean.valueOf(z)});
        }
    }

    @TargetApi(23)
    public boolean getOffscreenPreRaster() {
        Boolean bool;
        if (Build.VERSION.SDK_INT < 23 || (bool = (Boolean) ReflectionUtil.invokeNoThrow((Object) this.mSettings, "getOffscreenPreRaster")) == null) {
            return false;
        }
        return bool.booleanValue();
    }

    @TargetApi(24)
    public void setDisabledActionModeMenuItems(int i) {
        if (Build.VERSION.SDK_INT >= 24) {
            ReflectionUtil.invokeNoThrow((Object) this.mSettings, "setDisabledActionModeMenuItems", new Class[]{Integer.TYPE}, new Object[]{Integer.valueOf(i)});
        }
    }

    @TargetApi(24)
    public int getDisabledActionModeMenuItems() {
        Integer num;
        if (Build.VERSION.SDK_INT < 24 || (num = (Integer) ReflectionUtil.invokeNoThrow((Object) this.mSettings, "getDisabledActionModeMenuItems")) == null) {
            return 0;
        }
        return num.intValue();
    }
}
