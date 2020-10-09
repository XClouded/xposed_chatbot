package com.taobao.ju.track;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import com.taobao.ju.track.constants.Constants;
import com.taobao.ju.track.impl.CtrlTrackImpl;
import com.taobao.ju.track.impl.ExtTrackImpl;
import com.taobao.ju.track.impl.PageTrackImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class JTrack {
    private static Map<String, CtrlTrackImpl> mCtrlTracks;
    private static Map<String, ExtTrackImpl> mExtTracks;
    private static Map<String, PageTrackImpl> mPageTracks;
    /* access modifiers changed from: private */
    public static Context sSystemContext;

    public static void init(Context context) {
        sSystemContext = context;
    }

    public static void setPosStartFromOne(boolean z) {
        Constants.setPosStartFromOne(z);
    }

    public static PageTrackImpl getPage(String str) {
        synchronized (PageTrackImpl.class) {
            if (mPageTracks == null) {
                mPageTracks = new HashMap();
            }
            if (mPageTracks.containsKey(str)) {
                PageTrackImpl pageTrackImpl = mPageTracks.get(str);
                return pageTrackImpl;
            }
            PageTrackImpl pageTrackImpl2 = new PageTrackImpl(sSystemContext, str);
            mPageTracks.put(str, pageTrackImpl2);
            return pageTrackImpl2;
        }
    }

    public static synchronized CtrlTrackImpl getCtrl(String str) {
        synchronized (JTrack.class) {
            synchronized (CtrlTrackImpl.class) {
                if (mCtrlTracks == null) {
                    mCtrlTracks = new HashMap();
                }
                if (mCtrlTracks.containsKey(str)) {
                    CtrlTrackImpl ctrlTrackImpl = mCtrlTracks.get(str);
                    return ctrlTrackImpl;
                }
                CtrlTrackImpl ctrlTrackImpl2 = new CtrlTrackImpl(sSystemContext, str);
                mCtrlTracks.put(str, ctrlTrackImpl2);
                return ctrlTrackImpl2;
            }
        }
    }

    public static synchronized ExtTrackImpl getExt(String str) {
        synchronized (JTrack.class) {
            synchronized (ExtTrackImpl.class) {
                if (mExtTracks == null) {
                    mExtTracks = new HashMap();
                }
                if (mExtTracks.containsKey(str)) {
                    ExtTrackImpl extTrackImpl = mExtTracks.get(str);
                    return extTrackImpl;
                }
                ExtTrackImpl extTrackImpl2 = new ExtTrackImpl(sSystemContext, str);
                mExtTracks.put(str, extTrackImpl2);
                return extTrackImpl2;
            }
        }
    }

    public static final class Page {
        private static PageTrackImpl sTrack;
        private static String sTrackCsvFileName;

        private Page() {
        }

        public static synchronized PageTrackImpl getTrack() {
            PageTrackImpl pageTrackImpl;
            synchronized (Page.class) {
                if (sTrack == null) {
                    sTrack = new PageTrackImpl(JTrack.sSystemContext, sTrackCsvFileName);
                }
                pageTrackImpl = sTrack;
            }
            return pageTrackImpl;
        }

        public static void setCsvFileName(String str) {
            sTrack = null;
            sTrackCsvFileName = str;
        }

        public static boolean hasPoint(String str) {
            return getTrack().hasPoint(str);
        }

        public static boolean hasParam(String str, String str2) {
            return getTrack().hasParam(str, str2);
        }

        public static String[] getParamKvs(String str) {
            return getTrack().getParamKvs(str);
        }

        public static Properties getParamProp(String str) {
            return getTrack().getParamProp(str);
        }

        public static Map<String, String> getParamMap(String str) {
            return getTrack().getParamMap(str);
        }

        public static String getParamValue(String str, String str2) {
            return getTrack().getParamValue(str, str2);
        }

        public static String getParamValue(String str, String str2, String str3) {
            return getTrack().getParamValue(str, str2, str3);
        }

        public static Map<String, String> getStatic(String str) {
            return getTrack().getStatic(str);
        }

        public static Map<String, String> getRefer(String str) {
            return getTrack().getRefer(str);
        }

        public static Map<String, String> getDynamic(String str) {
            return getTrack().getDynamic(str);
        }

        public static boolean isInternal(String str, String str2) {
            return getTrack().isInternal(str, str2);
        }

        public static boolean isStatic(String str, String str2) {
            return getTrack().isStatic(str, str2);
        }

        public static boolean isRefer(String str, String str2) {
            return getTrack().isRefer(str, str2);
        }

        public static boolean isDynamic(String str, String str2) {
            return getTrack().isDynamic(str, str2);
        }

        public static boolean isValidateToUt(String str) {
            return getTrack().isValidateToUT(str);
        }

        public static String getPageName(String str) {
            return getTrack().getPageName(str);
        }

        public static String getPageName(Activity activity) {
            return getTrack().getPageName(activity);
        }

        public static String getSpm(String str) {
            return getTrack().getSpm(str);
        }

        public static String getSpm(Activity activity) {
            return getTrack().getSpm(activity);
        }

        public static Properties getSpmAsProp(Activity activity) {
            return getTrack().getSpmAsProp(activity);
        }

        public static Map<String, String> getSpmAsMap(Activity activity) {
            return getTrack().getSpmAsMap(activity);
        }

        public static String getSpmAB(String str) {
            return getTrack().getSpmAB(str);
        }

        public static String getSpmAB(Activity activity) {
            return getTrack().getSpmAB(activity);
        }

        public static String getArgs(Activity activity) {
            return getTrack().getArgs(activity);
        }

        public static String getArgs(String str) {
            return getTrack().getArgs(str);
        }

        public static Map<String, String> getArgsMap(Activity activity, Bundle bundle) {
            return getTrack().getArgsMap(activity, bundle);
        }

        public static Map<String, String> getArgsMap(String str, Bundle bundle) {
            return getTrack().getArgsMap(str, bundle);
        }

        public static Map<String, String> getArgsMap(Activity activity, Uri uri) {
            return getTrack().getArgsMap(activity, uri);
        }

        public static Map<String, String> getArgsMap(String str, Uri uri) {
            return getTrack().getArgsMap(str, uri);
        }
    }

    public static class Ctrl {
        private static CtrlTrackImpl sTrack;
        private static String sTrackCsvFileName;

        private Ctrl() {
        }

        public static synchronized CtrlTrackImpl getTrack() {
            CtrlTrackImpl ctrlTrackImpl;
            synchronized (Ctrl.class) {
                if (sTrack == null) {
                    sTrack = new CtrlTrackImpl(JTrack.sSystemContext, sTrackCsvFileName);
                }
                ctrlTrackImpl = sTrack;
            }
            return ctrlTrackImpl;
        }

        public static void setCsvFileName(String str) {
            sTrack = null;
            sTrackCsvFileName = str;
        }

        public static boolean hasPoint(String str) {
            return getTrack().hasPoint(str);
        }

        public static boolean hasParam(String str, String str2) {
            return getTrack().hasParam(str, str2);
        }

        public static String[] getParamKvs(String str) {
            return getTrack().getParamKvs(str);
        }

        public static Properties getParamProp(String str) {
            return getTrack().getParamProp(str);
        }

        public static Map<String, String> getParamMap(String str) {
            return getTrack().getParamMap(str);
        }

        public static String getParamValue(String str, String str2) {
            return getTrack().getParamValue(str, str2);
        }

        public static String getParamValue(String str, String str2, String str3) {
            return getTrack().getParamValue(str, str2, str3);
        }

        public static Map<String, String> getStatic(String str) {
            return getTrack().getStatic(str);
        }

        public static Map<String, String> getRefer(String str) {
            return getTrack().getRefer(str);
        }

        public static Map<String, String> getDynamic(String str) {
            return getTrack().getDynamic(str);
        }

        public static boolean isInternal(String str, String str2) {
            return getTrack().isInternal(str, str2);
        }

        public static boolean isStatic(String str, String str2) {
            return getTrack().isStatic(str, str2);
        }

        public static boolean isRefer(String str, String str2) {
            return getTrack().isRefer(str, str2);
        }

        public static boolean isDynamic(String str, String str2) {
            return getTrack().isDynamic(str, str2);
        }

        public static boolean isValidateToUt(String str) {
            return getTrack().isValidateToUT(str);
        }

        public static String getSpm(String str, String str2) {
            return getTrack().getSpm(str, str2);
        }

        public static String getSpm(Activity activity, String str) {
            return getTrack().getSpm(activity, str);
        }
    }

    public static class Ext {
        private static ExtTrackImpl sTrack;
        private static String sTrackCsvFileName;

        private Ext() {
        }

        public static synchronized ExtTrackImpl getTrack() {
            ExtTrackImpl extTrackImpl;
            synchronized (Ext.class) {
                if (sTrack == null) {
                    sTrack = new ExtTrackImpl(JTrack.sSystemContext, sTrackCsvFileName);
                }
                extTrackImpl = sTrack;
            }
            return extTrackImpl;
        }

        public static void setCsvFileName(String str) {
            sTrack = null;
            sTrackCsvFileName = str;
        }

        public static boolean hasPoint(String str) {
            return getTrack().hasPoint(str);
        }

        public static boolean hasParam(String str, String str2) {
            return getTrack().hasParam(str, str2);
        }

        public static String[] getParamKvs(String str) {
            return getTrack().getParamKvs(str);
        }

        public static Properties getParamProp(String str) {
            return getTrack().getParamProp(str);
        }

        public static Map<String, String> getParamMap(String str) {
            return getTrack().getParamMap(str);
        }

        public static String getParamValue(String str, String str2) {
            return getTrack().getParamValue(str, str2);
        }

        public static String getParamValue(String str, String str2, String str3) {
            return getTrack().getParamValue(str, str2, str3);
        }

        public static Map<String, String> getStatic(String str) {
            return getTrack().getStatic(str);
        }

        public static Map<String, String> getRefer(String str) {
            return getTrack().getRefer(str);
        }

        public static Map<String, String> getDynamic(String str) {
            return getTrack().getDynamic(str);
        }

        public static boolean isInternal(String str, String str2) {
            return getTrack().isInternal(str, str2);
        }

        public static boolean isStatic(String str, String str2) {
            return getTrack().isStatic(str, str2);
        }

        public static boolean isRefer(String str, String str2) {
            return getTrack().isRefer(str, str2);
        }

        public static boolean isDynamic(String str, String str2) {
            return getTrack().isDynamic(str, str2);
        }

        public static boolean isValidateToUt(String str) {
            return getTrack().isValidateToUT(str);
        }
    }
}
