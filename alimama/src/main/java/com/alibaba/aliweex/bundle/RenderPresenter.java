package com.alibaba.aliweex.bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.alibaba.aliweex.AliWXSDKEngine;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.bundle.WXNestedInstanceInterceptor;
import com.alibaba.aliweex.bundle.WeexPageContract;
import com.alibaba.aliweex.utils.MemoryMonitor;
import com.alibaba.aliweex.utils.WXPrefetchUtil;
import com.alibaba.aliweex.utils.WXUriUtil;
import com.alibaba.aliweex.utils.WXUtil;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.RenderContainer;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.render.WXAbstractRenderContainer;
import com.taobao.weex.ui.component.NestedContainer;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RenderPresenter implements WeexPageContract.IRenderPresenter {
    private static final String QUERY_ACTIVITY = "activity";
    private static final String WX_GET_DEEP_VIEW_LAYER = "get_deep_view_layer";
    private static final String WX_NAMESPACE_EXT_CONFIG = "wx_namespace_ext_config";
    private static String WX_SANBOX_SWITCH = "weex_sandbox";
    private Activity mActivity;
    private String mBundleUrl;
    private Map<String, Object> mCustomOpt;
    private WeexPageContract.IDynamicUrlPresenter mDynamicUrlPresenter;
    protected String mFtag;
    protected boolean mHeronPage;
    private String mInitData;
    protected WXNavBarAdapter mNavBarAdapter;
    protected WXNestedInstanceInterceptor mNestedInstanceInterceptor;
    private WeexPageContract.IProgressBar mProgressBarView;
    private IWXRenderListener mRenderListener;
    private String mRenderUrl;
    private String mTemplate;
    private WeexPageContract.IUTPresenter mUTPresenter;
    private WeexPageContract.IUrlValidate mUrlValidate;
    /* access modifiers changed from: private */
    public WXSDKInstance mWXSDKInstance;
    private String motuUploadUrl;
    private WXAbstractRenderContainer renderContainer = null;

    public RenderPresenter(Activity activity, String str, IWXRenderListener iWXRenderListener, WeexPageContract.IUTPresenter iUTPresenter, WeexPageContract.IDynamicUrlPresenter iDynamicUrlPresenter, WeexPageContract.IProgressBar iProgressBar, WXNavBarAdapter wXNavBarAdapter, WeexPageContract.IUrlValidate iUrlValidate) {
        this.mActivity = activity;
        this.mFtag = str;
        this.mRenderListener = iWXRenderListener;
        this.mUTPresenter = iUTPresenter;
        this.mDynamicUrlPresenter = iDynamicUrlPresenter;
        this.mProgressBarView = iProgressBar;
        this.mNavBarAdapter = wXNavBarAdapter;
        this.mUrlValidate = iUrlValidate;
        this.mNestedInstanceInterceptor = new WXNestedInstanceInterceptor(activity, iUrlValidate.getHandler());
    }

    public void startRenderByTemplate(String str, String str2, Map<String, Object> map, String str3) {
        if (!TextUtils.isEmpty(str)) {
            this.mProgressBarView.showProgressBar(true);
            initSDKInstance(this.mActivity);
            this.mCustomOpt = map;
            this.mInitData = str3;
            this.mTemplate = str;
            this.mBundleUrl = str2;
            WXSDKInstance wXSDKInstance = this.mWXSDKInstance;
            if (TextUtils.isEmpty(str2)) {
                str2 = "AliWeex";
            }
            wXSDKInstance.render(str2, str, map, str3, getWxRenderStrategy(this.mCustomOpt));
        }
    }

    public void startRenderByUrl(Map<String, Object> map, String str, String str2, String str3) {
        Log.w("test ->", "startRenderByUrl in renderListener");
        if (!TextUtils.isEmpty(str3)) {
            if (this.mUrlValidate != null) {
                this.mUrlValidate.checkUrlValidate(str3);
            }
            this.mProgressBarView.showProgressBar(!Uri.parse(str3).getBooleanQueryParameter("wx_mute_loading_indicator", false));
            initSDKInstance(this.mActivity);
            transformUrl(str2, str3);
            setCurCrashUrl(getUrl());
            if (!this.mWXSDKInstance.isPreInitMode() && !this.mWXSDKInstance.isPreDownLoad()) {
                str2 = WXPrefetchUtil.handleUrl(this.mWXSDKInstance, getOriginalUrl());
            }
            this.mCustomOpt = map;
            this.mInitData = str;
            HashMap hashMap = new HashMap();
            if (TextUtils.isEmpty(str2)) {
                str2 = str3;
            }
            hashMap.put("bundleUrl", str2);
            if (map != null) {
                for (String next : map.keySet()) {
                    hashMap.put(next, map.get(next));
                }
            }
            if (this.mUTPresenter != null) {
                this.mUTPresenter.updatePageName(getUrl());
            }
            render(hashMap, str, getWxRenderStrategy(this.mCustomOpt));
        }
    }

    /* access modifiers changed from: private */
    public void fireEventOnUiThread(final WXSDKInstance wXSDKInstance, final String str, final Map<String, Object> map) {
        if (wXSDKInstance != null) {
            wXSDKInstance.runOnUiThread(new Runnable() {
                public void run() {
                    RenderPresenter.this.fireEvent(wXSDKInstance, str, map);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void fireEvent(WXSDKInstance wXSDKInstance, String str, Map<String, Object> map) {
        if (wXSDKInstance != null && wXSDKInstance.getRootComponent() != null) {
            wXSDKInstance.fireEvent(wXSDKInstance.getRootComponent().getRef(), str, map);
        }
    }

    public void fireEvent(String str, Map<String, Object> map) {
        fireEvent(this.mWXSDKInstance, str, map);
    }

    public void reload() {
        if (this.mUTPresenter != null) {
            this.mUTPresenter.refreshUT(getUrl());
        }
        if (!TextUtils.isEmpty(getOriginalUrl()) && !TextUtils.isEmpty(getOriginalRenderUrl())) {
            destroySDKInstance();
            startRenderByUrl(this.mCustomOpt, this.mInitData, getOriginalUrl(), getOriginalRenderUrl());
        } else if (!TextUtils.isEmpty(this.mTemplate)) {
            destroySDKInstance();
            startRenderByTemplate(this.mTemplate, this.mBundleUrl, this.mCustomOpt, this.mInitData);
        }
    }

    public void replace(String str, String str2) {
        destroySDKInstance();
        transformUrl(str, str2);
        if (this.mUTPresenter != null) {
            this.mUTPresenter.refreshUT(getUrl());
        }
        startRenderByUrl(this.mCustomOpt, this.mInitData, str, str2);
    }

    public void transformUrl(String str, String str2) {
        if (this.mDynamicUrlPresenter != null) {
            this.mDynamicUrlPresenter.transformUrl(str, str2);
            return;
        }
        this.mBundleUrl = str;
        this.mRenderUrl = str2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x0028  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.taobao.weex.WXSDKInstance createWXSDKInstance(android.content.Context r4) {
        /*
            r3 = this;
            java.lang.String r0 = r3.getRenderUrl()
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L_0x0025
            com.alibaba.aliweex.preLoad.WXPreLoadManager r1 = com.alibaba.aliweex.preLoad.WXPreLoadManager.getInstance()
            com.taobao.weex.WXSDKInstance r0 = r1.offerPreInitInstance(r0, r4)
            boolean r1 = r0 instanceof com.alibaba.aliweex.AliWXSDKInstance
            if (r1 == 0) goto L_0x0025
            com.alibaba.aliweex.AliWXSDKInstance r0 = (com.alibaba.aliweex.AliWXSDKInstance) r0
            java.lang.String r1 = r3.mFtag
            r0.setFragmentTag(r1)
            java.lang.String r1 = "RenderPresenter"
            java.lang.String r2 = "preinit -> use preinitInstance "
            android.util.Log.e(r1, r2)
            goto L_0x0026
        L_0x0025:
            r0 = 0
        L_0x0026:
            if (r0 != 0) goto L_0x0036
            java.lang.String r0 = "RenderPresenter"
            java.lang.String r1 = "preinit -> failed ,and  new AliWXSDKInstance "
            android.util.Log.e(r0, r1)
            com.alibaba.aliweex.AliWXSDKInstance r0 = new com.alibaba.aliweex.AliWXSDKInstance
            java.lang.String r1 = r3.mFtag
            r0.<init>(r4, r1)
        L_0x0036:
            com.alibaba.aliweex.bundle.WXNavBarAdapter r4 = r3.mNavBarAdapter
            r0.setWXNavBarAdapter(r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.bundle.RenderPresenter.createWXSDKInstance(android.content.Context):com.taobao.weex.WXSDKInstance");
    }

    public void destroySDKInstance() {
        if (this.mWXSDKInstance != null) {
            MemoryMonitor.removeListeners(this.mWXSDKInstance.getInstanceId());
            this.mWXSDKInstance.destroy();
            this.mWXSDKInstance = null;
        }
    }

    public NestedContainer getNestedContainer(WXSDKInstance wXSDKInstance) {
        if (this.mNestedInstanceInterceptor == null || wXSDKInstance == null) {
            return null;
        }
        return this.mNestedInstanceInterceptor.getNestedContainer(wXSDKInstance);
    }

    public WXSDKInstance getWXSDKInstance() {
        if (this.mWXSDKInstance == null) {
            initSDKInstance(this.mActivity);
        }
        return this.mWXSDKInstance;
    }

    /* access modifiers changed from: protected */
    public void setRenderContainer(WXAbstractRenderContainer wXAbstractRenderContainer, boolean z) {
        this.renderContainer = wXAbstractRenderContainer;
        this.mHeronPage = false;
    }

    public void onActivityCreate(ViewGroup viewGroup, Map<String, Object> map, String str, String str2, String str3, String str4, String str5) {
        if (this.renderContainer == null) {
            this.renderContainer = new RenderContainer(this.mActivity);
        }
        viewGroup.addView(this.renderContainer);
        initSDKInstance(this.mActivity);
        this.renderContainer.createInstanceRenderView(this.mWXSDKInstance.getInstanceId());
        this.mWXSDKInstance.setWXAbstractRenderContainer(this.renderContainer);
        if (!TextUtils.isEmpty(str2)) {
            startRenderByTemplate(str2, str3, map, str);
        } else if (!TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str4)) {
            startRenderByUrl(map, str, str3, str4);
        } else if (!TextUtils.isEmpty(str5)) {
            startRenderByUrl(map, str, str5, str5);
        }
        this.mWXSDKInstance.onActivityCreate();
        MemoryMonitor.addMemoryListener(this.mWXSDKInstance.getInstanceId(), new MemoryMonitor.MemoryListener() {
            public void onChange(String str) {
                ArrayList<WXNestedInstanceInterceptor.NestedInfo> nestedInfos;
                WXSDKInstance instance;
                HashMap hashMap = new HashMap(1);
                hashMap.put("evaluatedStatus", str);
                RenderPresenter.this.fireEventOnUiThread(RenderPresenter.this.mWXSDKInstance, "memoryevaluated", hashMap);
                if (RenderPresenter.this.mNestedInstanceInterceptor != null && (nestedInfos = RenderPresenter.this.mNestedInstanceInterceptor.getNestedInfos()) != null && nestedInfos.size() != 0) {
                    Iterator<WXNestedInstanceInterceptor.NestedInfo> it = nestedInfos.iterator();
                    while (it.hasNext()) {
                        WXNestedInstanceInterceptor.EmbedEventListener embedEventListener = it.next().mEventListener;
                        if (!(embedEventListener == null || (instance = embedEventListener.getInstance()) == null)) {
                            RenderPresenter.this.fireEventOnUiThread(instance, "memoryevaluated", hashMap);
                        }
                    }
                }
            }
        });
    }

    public void onActivityStart() {
        if (this.mWXSDKInstance != null) {
            this.mWXSDKInstance.onActivityStart();
        }
    }

    public void onActivityStop() {
        if (this.mWXSDKInstance != null) {
            this.mWXSDKInstance.onActivityStop();
        }
    }

    public void onActivityPause() {
        if (this.mWXSDKInstance != null) {
            if (this.mWXSDKInstance.getContainerView() != null && isGetDeepViewLayer()) {
                this.mWXSDKInstance.setMaxDeepLayer(getMaxDeepViewLayer((ViewGroup) this.mWXSDKInstance.getContainerView()));
            }
            this.mWXSDKInstance.onActivityPause();
        }
        AliWXSDKEngine.setCurCrashUrl("");
    }

    public void onActivityResume() {
        if (this.mWXSDKInstance != null) {
            this.mWXSDKInstance.onActivityResume();
        }
        setCurCrashUrl(getUrl());
    }

    public void onActivityDestroy() {
        if (this.mWXSDKInstance != null) {
            WXSDKInstance wXSDKInstance = getWXSDKInstance();
            if (wXSDKInstance != null) {
                MemoryMonitor.removeListeners(wXSDKInstance.getInstanceId());
            }
            this.mWXSDKInstance.onActivityDestroy();
        }
        if (this.mNestedInstanceInterceptor != null) {
            this.mNestedInstanceInterceptor.destroy();
        }
    }

    public void onCreateOptionsMenu(Menu menu) {
        if (this.mWXSDKInstance != null) {
            this.mWXSDKInstance.onCreateOptionsMenu(menu);
        }
        ArrayList<WXNestedInstanceInterceptor.NestedInfo> nestedInfos = this.mNestedInstanceInterceptor.getNestedInfos();
        if (nestedInfos != null) {
            Iterator<WXNestedInstanceInterceptor.NestedInfo> it = nestedInfos.iterator();
            while (it.hasNext()) {
                WXNestedInstanceInterceptor.NestedInfo next = it.next();
                if (next.mEventListener.getInstance() != null) {
                    next.mEventListener.getInstance().onCreateOptionsMenu(menu);
                }
            }
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (this.mWXSDKInstance != null) {
            this.mWXSDKInstance.onActivityResult(i, i2, intent);
        }
    }

    public boolean onBackPressed() {
        if (this.mWXSDKInstance != null) {
            return this.mWXSDKInstance.onBackPressed();
        }
        return false;
    }

    public boolean onSupportNavigateUp() {
        if (this.mWXSDKInstance != null) {
            return this.mWXSDKInstance.onSupportNavigateUp();
        }
        return false;
    }

    public String getUrl() {
        return this.mDynamicUrlPresenter != null ? this.mDynamicUrlPresenter.getUrl() : this.mBundleUrl;
    }

    public String getOriginalUrl() {
        return this.mDynamicUrlPresenter != null ? this.mDynamicUrlPresenter.getOriginalUrl() : this.mBundleUrl;
    }

    public String getRenderUrl() {
        return this.mDynamicUrlPresenter != null ? this.mDynamicUrlPresenter.getRenderUrl() : this.mRenderUrl;
    }

    public String getOriginalRenderUrl() {
        return this.mDynamicUrlPresenter != null ? this.mDynamicUrlPresenter.getOriginalRenderUrl() : this.mRenderUrl;
    }

    private void initSDKInstance(Context context) {
        if (this.mWXSDKInstance == null) {
            AliWXSDKEngine.updateGlobalConfig();
            this.mWXSDKInstance = createWXSDKInstance(context);
            AliWXSDKEngine.setCurInstanceId(this.mWXSDKInstance.getInstanceId());
            if (AliWeex.getInstance().getConfigAdapter() != null) {
                if ("false".equals(AliWeex.getInstance().getConfigAdapter().getConfig(WX_SANBOX_SWITCH, "enableSanbox", "true"))) {
                    this.mWXSDKInstance.setUseSandBox(false);
                } else {
                    this.mWXSDKInstance.setUseSandBox(true);
                }
            }
            if (this.mUTPresenter != null) {
                this.mUTPresenter.viewAutoExposure(this.mWXSDKInstance);
            }
            this.mWXSDKInstance.registerRenderListener(this.mRenderListener);
            if (this.mNestedInstanceInterceptor != null) {
                this.mWXSDKInstance.setNestedInstanceInterceptor(this.mNestedInstanceInterceptor);
            }
            this.mWXSDKInstance.onInstanceReady();
        }
    }

    private void render(Map<String, Object> map, String str, WXRenderStrategy wXRenderStrategy) {
        String renderUrl = getRenderUrl();
        String str2 = (UrlValidate.isValid(renderUrl) || WXEnvironment.isApkDebugable()) ? renderUrl : WXUtil.ERROR_RENDER_URL;
        if (!this.mWXSDKInstance.isPreDownLoad()) {
            this.mWXSDKInstance.renderByUrl(str2, str2, map, str, wXRenderStrategy);
            try {
                WXUriUtil.reportWPLHost(this.mWXSDKInstance, renderUrl);
            } catch (Throwable unused) {
            }
        }
    }

    private void setCurCrashUrl(String str) {
        if (!TextUtils.isEmpty(str)) {
            boolean z = true;
            if (!TextUtils.equals(this.motuUploadUrl, str)) {
                if (TextUtils.isEmpty(this.motuUploadUrl)) {
                    this.motuUploadUrl = str;
                } else {
                    z = false;
                }
            }
            if (this.mActivity != null && z) {
                this.motuUploadUrl = Uri.parse(str).buildUpon().appendQueryParameter(QUERY_ACTIVITY, this.mActivity.getClass().getName()).build().toString();
            }
            AliWXSDKEngine.setCurCrashUrl(this.motuUploadUrl);
        }
    }

    private synchronized boolean isGetDeepViewLayer() {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter == null) {
            return false;
        }
        return Boolean.parseBoolean(configAdapter.getConfig("wx_namespace_ext_config", "get_deep_view_layer", Boolean.toString(true)));
    }

    private int getMaxDeepViewLayer(ViewGroup viewGroup) {
        int maxDeepViewLayer;
        if (viewGroup == null) {
            return -1;
        }
        if (viewGroup.getChildCount() == 0) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < viewGroup.getChildCount(); i2++) {
            View childAt = viewGroup.getChildAt(i2);
            if (childAt != null && (childAt instanceof ViewGroup) && (maxDeepViewLayer = getMaxDeepViewLayer((ViewGroup) childAt)) > i) {
                i = maxDeepViewLayer;
            }
        }
        return i + 1;
    }

    @NonNull
    private WXRenderStrategy getWxRenderStrategy(Map<String, Object> map) {
        WXRenderStrategy wXRenderStrategy = WXRenderStrategy.APPEND_ASYNC;
        if (map == null) {
            return wXRenderStrategy;
        }
        try {
            if (map.containsKey(WeexPageFragment.WX_RENDER_STRATEGY)) {
                return WXRenderStrategy.valueOf(map.get(WeexPageFragment.WX_RENDER_STRATEGY).toString());
            }
            return wXRenderStrategy;
        } catch (Exception e) {
            WXLogUtils.e("RenderPresenter", WXLogUtils.getStackTrace(e));
            return wXRenderStrategy;
        }
    }
}
