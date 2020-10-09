package com.alimama.union.app.pagerouter;

import alimama.com.unwrouter.PageInfo;
import alimama.com.unwrouter.interfaces.JumpInterceptor;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.alimama.moon.utils.CommonUtils;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.moon.utils.UnionLensUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class MoonJumpIterceptor implements JumpInterceptor {
    public static String CLIP_BOARD_CONTENT_BEFORE_GO_TO_PAGE = "";
    public static final String SPM = "spm";

    public void onCreateIntercept(ArrayList<WeakReference<Activity>> arrayList, Activity activity) {
    }

    public boolean onIntercept(ArrayList<WeakReference<Activity>> arrayList, PageInfo pageInfo, Uri uri, Bundle bundle, int i) {
        String str = "";
        HashMap hashMap = new HashMap();
        if (uri != null && uri.isHierarchical()) {
            str = uri.getQueryParameter("spm");
            String queryParameter = uri.getQueryParameter(UnionLensUtil.UNION_LENS_LOG);
            if (!TextUtils.isEmpty(queryParameter)) {
                hashMap.put(UnionLensUtil.UNION_LENS_LOG, queryParameter);
            }
            if (Build.VERSION.SDK_INT > 28 && !TextUtils.isEmpty(CommonUtils.getClipboardContent())) {
                CLIP_BOARD_CONTENT_BEFORE_GO_TO_PAGE = CommonUtils.getClipboardContent();
            }
        }
        SpmProcessor.updateNextPageProperties(str, hashMap);
        return false;
    }

    public void onResumeIntercept(Activity activity) {
        if (activity instanceof IUTPage) {
            String currentPageName = ((IUTPage) activity).getCurrentPageName();
            if (!TextUtils.isEmpty(currentPageName)) {
                SpmProcessor.pageAppear(activity, currentPageName);
            }
        }
    }

    public void onPauseIntercept(Activity activity) {
        if (activity instanceof IUTPage) {
            String currentSpmCnt = ((IUTPage) activity).getCurrentSpmCnt();
            if (!TextUtils.isEmpty(currentSpmCnt)) {
                SpmProcessor.pageDisappear(activity, currentSpmCnt);
            }
        }
    }
}
