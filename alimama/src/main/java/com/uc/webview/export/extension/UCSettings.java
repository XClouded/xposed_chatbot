package com.uc.webview.export.extension;

import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.IGlobalSettings;
import com.uc.webview.export.internal.interfaces.InvokeObject;
import com.uc.webview.export.internal.interfaces.UCMobileWebKit;
import com.uc.webview.export.internal.utility.Log;
import java.util.HashSet;
import java.util.Set;

@Api
/* compiled from: U4Source */
public abstract class UCSettings implements InvokeObject {
    public static final String CD_RESOURCE_DISABLE_SW_SCRIPTCACHE_LIST = "crwp_disable_sw_scriptcache_list";
    public static final String CD_RESOURCE_EMBED_SURFACE_EMBED_VIEW_ENABLE_LIST = "crwp_embed_surface_embed_view_enable_list";
    public static final String CD_RESOURCE_EMBED_VIEW_REATTACH_LIST = "crwp_embed_view_reattach_list";
    public static final String CD_RESOURCE_ENABLE_IMG_ERROR_INFO = "enable_img_error_info";
    public static final String CD_RESOURCE_FOCUS_AUTO_POPUP_INPUT_WHITELIST = "u4_focus_auto_popup_input_list";
    public static final String CD_RESOURCE_HYBRID_RENDER_EMBED_VIEW_ENABLE_LIST = "crwp_hybrid_render_embed_view_enable_list";
    public static final String CD_RESOURCE_STAT_FILTER_LIST = "stat_filter_list";
    public static final String KEY_ADBLOCK_WHITE_LIST = "resadwhitelist";
    public static final String KEY_COOKIES_BLACKLIST_FOR_JS = "CookiesBlacklistForJs";
    public static final String KEY_DISABLE_ACCELERATE_CANVAS = "DisableAccelerateCanvas";
    @Deprecated
    public static final String KEY_DISABLE_FLOAT_VIDEO_VIEW = "video_fixed_sw_hostlist";
    @Deprecated
    public static final String KEY_DISABLE_VIDEO_RESUME = "disable_video_resume";
    public static final String KEY_DISCARDABLE_FREE_IF_HAS_GPU_DECODE = "DiscardableFreeIfHasGpuDecode";
    @Deprecated
    public static final String KEY_DONOT_PAUSE_AFTER_EXIT_VIDEO_FULLSCREEN = "crsp_npef";
    @Deprecated
    public static final String KEY_DONOT_PAUSE_AFTER_SHOW_MODE_CHANGED = "crsp_npsmc";
    public static final String KEY_ENABLE_VIDEO_AUTO_PLAY_LIST = "video_play_gesture_whitelist";
    public static final String KEY_MEM_CACHE_PAGE_COUNT = "CachePageNumber";
    public static final String KEY_MEM_DISCARDABLE_LIMIT_BYTE = "DiscardableLimitBytes";
    public static final String KEY_MEM_DISCARDABLE_RELEASE_FOR_ALLOC_FAILED_SWITCH = "DiscardableReleaseForAllocFailedSwitch";
    public static final String KEY_MEM_DISCARDABLE_RELEASE_FREE_AFTER_SECOND = "DiscardableReleaseFreeAfterSecond";
    public static final String KEY_MEM_DISCARDABLE_RELEASE_FREE_AFTER_TIME_SWITCH = "DiscardableReleaseFreeAfterTimeSwitch";
    public static final String KEY_MEM_DISCARDABLE_RELEASE_FREE_UNTIL_BYTE = "DiscardableReleaseFreeUntilByte";
    public static final String KEY_MEM_GR_DISCARDABLE_LIMIT_BYTE = "GrDiscardableLimitByte";
    public static final String KEY_MEM_GR_RESOURCE_CACHE_LIMIT_BYTE = "GrResourceCacheLimitByte";
    public static final String KEY_NIGHT_MODE_COLOR = "NightModeColor";
    @Deprecated
    public static final String KEY_NO_DISPLAY_WANING_WHEN_PLAY_MEDIA_ON_MOBILE_NETWORK = "crsp_nwomn";
    public static final String KEY_PAGE_TIMER_COUNT_LIMIT = "PageTimerCountLimit";
    public static final String KEY_USE_RAW_VIDEO_CONTROLS = "u4xr_video_st_list";
    @Deprecated
    public static final String KEY_VIDEO_ENTER_VIEW_FULLSCREEN_ONLY = "crsp_fsa_bl";
    @Deprecated
    public static final String KEY_VIDEO_SUPPORT_RAW_CONTROLS_ATTR = "crsp_sp_rc";
    public static final String KEY_WEBAUDIO_DISABLE_DEFAULT_DECODER = "crsp_wddd";
    private static Set<String> a;

    @Deprecated
    public static boolean isEnableCustomErrorPage() {
        return true;
    }

    static {
        HashSet hashSet = new HashSet();
        a = hashSet;
        hashSet.add("u4xr_video_st_list");
        a.add(KEY_VIDEO_SUPPORT_RAW_CONTROLS_ATTR);
        a.add(KEY_VIDEO_ENTER_VIEW_FULLSCREEN_ONLY);
        a.add(KEY_DISABLE_FLOAT_VIDEO_VIEW);
        a.add("video_play_gesture_whitelist");
        a.add(KEY_ADBLOCK_WHITE_LIST);
        a.add(CD_RESOURCE_STAT_FILTER_LIST);
        a.add(KEY_NIGHT_MODE_COLOR);
        a.add("u4_focus_auto_popup_input_list");
        a.add("crwp_hybrid_render_embed_view_enable_list");
        a.add("crwp_embed_surface_embed_view_enable_list");
        a.add("crwp_embed_view_reattach_list");
        a.add("crwp_disable_sw_scriptcache_list");
        a.add("enable_img_error_info");
        a.add("CachePageNumber");
        a.add("DiscardableFreeIfHasGpuDecode");
        a.add("DiscardableLimitBytes");
        a.add("DiscardableReleaseFreeAfterTimeSwitch");
        a.add("DiscardableReleaseFreeAfterSecond");
        a.add("DiscardableReleaseFreeUntilByte");
        a.add("DiscardableReleaseForAllocFailedSwitch");
        a.add("GrDiscardableLimitByte");
        a.add("GrResourceCacheLimitByte");
        a.add("CookiesBlacklistForJs");
        a.add("PageTimerCountLimit");
    }

    public static void updateBussinessInfo(int i, int i2, String str, Object obj) {
        String str2;
        if (SDKFactory.d != null && a.contains(str)) {
            UCMobileWebKit uCMobileWebKit = SDKFactory.d;
            if (obj instanceof String[]) {
                String[] strArr = (String[]) obj;
                StringBuilder sb = new StringBuilder();
                for (String trim : strArr) {
                    String trim2 = trim.trim();
                    if (trim2.length() != 0) {
                        sb.append(trim2);
                        sb.append("^^");
                    }
                }
                if (sb.length() > 0) {
                    sb.setLength(sb.length() - 2);
                    str2 = sb.toString();
                    uCMobileWebKit.updateBussinessInfo(i, i2, str, str2);
                }
            }
            if (obj instanceof String) {
                str2 = (String) obj;
            } else {
                str2 = "";
            }
            uCMobileWebKit.updateBussinessInfo(i, i2, str, str2);
        }
    }

    @Deprecated
    public void setEnableFastScroller(boolean z) {
        Log.w("UCSettings", "setEnableFastScroller not override");
    }

    @Deprecated
    public void setEnableUCProxy(boolean z) {
        Log.w("UCSettings", "setEnableUCProxy deprecated");
    }

    @Deprecated
    public void setForceUCProxy(boolean z) {
        Log.w("UCSettings", "setForceUCProxy deprecated");
    }

    @Deprecated
    public void setUCCookieType(int i) {
        Log.w("UCSettings", "setUCCookieType deprecated");
    }

    public static void setGlobalIntValue(String str, int i) {
        IGlobalSettings e = SDKFactory.e();
        if (e != null) {
            e.setIntValue(str, i);
        }
    }

    public static int getGlobalIntValue(String str) {
        IGlobalSettings e = SDKFactory.e();
        if (e != null) {
            return e.getIntValue(str);
        }
        return -1;
    }

    public static void setGlobalStringValue(String str, String str2) {
        IGlobalSettings e = SDKFactory.e();
        if (e != null) {
            e.setStringValue(str, str2);
        }
    }

    public static String getGlobalStringValue(String str) {
        IGlobalSettings e = SDKFactory.e();
        return e != null ? e.getStringValue(str) : "";
    }

    public static void setGlobalBoolValue(String str, boolean z) {
        IGlobalSettings e = SDKFactory.e();
        if (e != null) {
            e.setBoolValue(str, z);
        }
    }

    public static boolean getGlobalBoolValue(String str) {
        IGlobalSettings e = SDKFactory.e();
        if (e != null) {
            return e.getBoolValue(str);
        }
        return false;
    }

    public void setStringValue(String str, String str2) {
        invoke(304, new Object[]{str, str2});
    }
}
