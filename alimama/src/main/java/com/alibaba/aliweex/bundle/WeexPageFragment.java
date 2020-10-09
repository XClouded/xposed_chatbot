package com.alibaba.aliweex.bundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.aliweex.AliWXSDKInstance;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.R;
import com.alibaba.aliweex.adapter.INavigationBarModuleAdapter;
import com.alibaba.aliweex.bundle.DefaultView;
import com.alibaba.aliweex.bundle.WeexPageContract;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.render.WXAbstractRenderContainer;
import com.taobao.weex.ui.component.NestedContainer;
import com.taobao.weex.ui.view.WXScrollView;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXLogUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WeexPageFragment extends Fragment {
    public static String FRAGMENT_ARG_BUNDLE_URL = "arg_bundle_url";
    public static String FRAGMENT_ARG_CUSTOM_OPT = "arg_custom_opt";
    public static String FRAGMENT_ARG_FROM_ACTIVITY = "arg_from_activity";
    public static String FRAGMENT_ARG_INIT_DATA = "arg_init_data";
    public static String FRAGMENT_ARG_RENDER_URL = "arg_render_url";
    public static String FRAGMENT_ARG_TAG = "arg_tag";
    public static String FRAGMENT_ARG_TEMPLATE = "arg_template";
    @Deprecated
    public static String FRAGMENT_ARG_URI = "arg_uri";
    public static String FRAGMENT_TAG = "weex_page";
    private static final String TAG = "WeexPageFragment";
    public static final String WX_RENDER_STRATEGY = "render_strategy";
    private boolean mDyUrlEnable = false;
    private WeexPageContract.IDynamicUrlPresenter mDynamicUrlPresenter;
    private WeexPageContract.IErrorView mErrorView;
    protected String mFtag = FRAGMENT_TAG;
    private Boolean mFullScreen;
    private boolean mIsHeron = false;
    private WXNavBarAdapter mNavBarAdapter;
    private boolean mNoAnimated;
    private INavigationBarModuleAdapter.OnItemClickListener mOnBackPressedListener = null;
    private WeexPageContract.IProgressBar mProgressBarView;
    private BroadcastReceiver mRefreshReceiver;
    private BroadcastReceiver mReloadReceiver;
    private WXAbstractRenderContainer mRenderContainer = null;
    private WXRenderListenerAdapter mRenderListener;
    private WeexPageContract.IRenderPresenter mRenderPresenter;
    private FrameLayout mRootView;
    private boolean mUTEnable = true;
    private WeexPageContract.IUTPresenter mUTPresenter;
    private WeexPageContract.IUrlValidate mUrlValidate;
    WXRenderListener mWXRenderListener = null;

    public static class WXRenderListenerAdapter implements IWXRenderListener {
        public boolean needWrapper() {
            return true;
        }

        public View onCreateView(WXSDKInstance wXSDKInstance, View view) {
            return view;
        }

        public void onException(WXSDKInstance wXSDKInstance, String str, String str2) {
        }

        public void onException(WXSDKInstance wXSDKInstance, boolean z, String str, String str2) {
        }

        public void onRefreshSuccess(WXSDKInstance wXSDKInstance, int i, int i2) {
        }

        public void onRenderSuccess(WXSDKInstance wXSDKInstance, int i, int i2) {
        }

        public void onViewCreated(WXSDKInstance wXSDKInstance, View view) {
        }
    }

    @Deprecated
    public static Fragment newInstance(FragmentActivity fragmentActivity, Class<? extends WeexPageFragment> cls, String str, int i) {
        return installFragment(fragmentActivity, cls, (String) null, str, str, (HashMap<String, Object>) null, (String) null, i, (String) null, (Serializable) null);
    }

    public static Fragment newInstanceWithUrl(FragmentActivity fragmentActivity, Class<? extends WeexPageFragment> cls, String str, String str2, int i) {
        return installFragment(fragmentActivity, cls, (String) null, str, str2, (HashMap<String, Object>) null, (String) null, i, (String) null, (Serializable) null);
    }

    public static Fragment newInstanceWithUrl(FragmentActivity fragmentActivity, Class<? extends WeexPageFragment> cls, String str, String str2, int i, Serializable serializable) {
        return installFragment(fragmentActivity, cls, (String) null, str, str2, (HashMap<String, Object>) null, (String) null, i, (String) null, serializable);
    }

    public static Fragment newInstanceWithUrl(FragmentActivity fragmentActivity, Class<? extends WeexPageFragment> cls, String str, String str2, HashMap<String, Object> hashMap, String str3, int i) {
        return installFragment(fragmentActivity, cls, (String) null, str, str2, hashMap, str3, i, (String) null, (Serializable) null);
    }

    public static Fragment newInstanceWithUrl(FragmentActivity fragmentActivity, Class<? extends WeexPageFragment> cls, String str, String str2, HashMap<String, Object> hashMap, String str3, int i, String str4) {
        return installFragment(fragmentActivity, cls, (String) null, str, str2, hashMap, str3, i, str4, (Serializable) null);
    }

    public static Fragment newInstanceWithUrl(FragmentActivity fragmentActivity, Class<? extends WeexPageFragment> cls, String str, String str2, HashMap<String, Object> hashMap, String str3, int i, String str4, Serializable serializable) {
        return installFragment(fragmentActivity, cls, (String) null, str, str2, hashMap, str3, i, str4, serializable);
    }

    public static Fragment newInstanceWithTemplate(FragmentActivity fragmentActivity, Class<? extends WeexPageFragment> cls, String str, String str2, int i) {
        return installFragment(fragmentActivity, cls, str, str2, (String) null, (HashMap<String, Object>) null, (String) null, i, (String) null, (Serializable) null);
    }

    public static Fragment newInstanceWithTemplate(FragmentActivity fragmentActivity, Class<? extends WeexPageFragment> cls, String str, String str2, HashMap<String, Object> hashMap, String str3, int i) {
        return installFragment(fragmentActivity, cls, str, str2, (String) null, hashMap, str3, i, (String) null, (Serializable) null);
    }

    public static Fragment newInstanceWithTemplate(FragmentActivity fragmentActivity, Class<? extends WeexPageFragment> cls, String str, String str2, HashMap<String, Object> hashMap, String str3, int i, String str4) {
        return installFragment(fragmentActivity, cls, str, str2, (String) null, hashMap, str3, i, str4, (Serializable) null);
    }

    @Deprecated
    public static Fragment newInstanceWithTemplate(FragmentActivity fragmentActivity, Class<? extends WeexPageFragment> cls, String str, int i) {
        return installFragment(fragmentActivity, cls, str, (String) null, (String) null, (HashMap<String, Object>) null, (String) null, i, (String) null, (Serializable) null);
    }

    @Deprecated
    public static Fragment newInstanceWithTemplate(FragmentActivity fragmentActivity, Class<? extends WeexPageFragment> cls, String str, HashMap<String, Object> hashMap, String str2, int i) {
        return installFragment(fragmentActivity, cls, str, (String) null, (String) null, hashMap, str2, i, (String) null, (Serializable) null);
    }

    @Deprecated
    public static Fragment newInstanceWithTemplate(FragmentActivity fragmentActivity, Class<? extends WeexPageFragment> cls, String str, HashMap<String, Object> hashMap, String str2, int i, String str3) {
        return installFragment(fragmentActivity, cls, str, (String) null, (String) null, hashMap, str2, i, str3, (Serializable) null);
    }

    public static Fragment newInstance(Context context, Class<? extends WeexPageFragment> cls, String str, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_ARG_BUNDLE_URL, str);
        bundle.putString(FRAGMENT_ARG_RENDER_URL, str2);
        Fragment instantiate = Fragment.instantiate(context, cls.getName(), bundle);
        instantiate.setArguments(bundle);
        return instantiate;
    }

    private static Fragment installFragment(FragmentActivity fragmentActivity, Class<? extends WeexPageFragment> cls, String str, String str2, String str3, HashMap<String, Object> hashMap, String str4, int i, String str5, Serializable serializable) {
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        String str6 = TextUtils.isEmpty(str5) ? FRAGMENT_TAG : str5;
        Fragment findFragmentByTag = supportFragmentManager.findFragmentByTag(str6);
        if (findFragmentByTag != null) {
            return findFragmentByTag;
        }
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_ARG_TAG, str6);
        if (!TextUtils.isEmpty(str)) {
            bundle.putString(FRAGMENT_ARG_TEMPLATE, str);
        }
        if (!TextUtils.isEmpty(str2)) {
            bundle.putString(FRAGMENT_ARG_BUNDLE_URL, str2);
        }
        if (!TextUtils.isEmpty(str3)) {
            bundle.putString(FRAGMENT_ARG_RENDER_URL, str3);
        }
        if (hashMap != null) {
            bundle.putSerializable(FRAGMENT_ARG_CUSTOM_OPT, hashMap);
        }
        if (!TextUtils.isEmpty(str4)) {
            bundle.putString(FRAGMENT_ARG_INIT_DATA, str4);
        }
        if (serializable != null) {
            bundle.putSerializable(FRAGMENT_ARG_FROM_ACTIVITY, serializable);
        }
        Fragment instantiate = Fragment.instantiate(fragmentActivity, cls.getName(), bundle);
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        if (TextUtils.isEmpty(str5)) {
            str5 = FRAGMENT_TAG;
        }
        beginTransaction.add(i, instantiate, str5);
        beginTransaction.commitAllowingStateLoss();
        return instantiate;
    }

    public void setUserTrackEnable(boolean z) {
        this.mUTEnable = z;
    }

    public void setUserTrackPresenter(WeexPageContract.IUTPresenter iUTPresenter) {
        this.mUTPresenter = iUTPresenter;
    }

    public void setDynamicUrlEnable(boolean z) {
        this.mDyUrlEnable = z;
    }

    public void setProgressBarView(WeexPageContract.IProgressBar iProgressBar) {
        this.mProgressBarView = iProgressBar;
    }

    public void setErrorView(WeexPageContract.IErrorView iErrorView) {
        this.mErrorView = iErrorView;
    }

    public String getUrl() {
        return this.mRenderPresenter != null ? this.mRenderPresenter.getUrl() : "";
    }

    public String getOriginalUrl() {
        return this.mRenderPresenter != null ? this.mRenderPresenter.getOriginalUrl() : "";
    }

    public String getRenderUrl() {
        return this.mRenderPresenter != null ? this.mRenderPresenter.getRenderUrl() : "";
    }

    public String getOriginalRenderUrl() {
        return this.mRenderPresenter != null ? this.mRenderPresenter.getOriginalRenderUrl() : "";
    }

    public void setRenderListener(WXRenderListenerAdapter wXRenderListenerAdapter) {
        this.mRenderListener = wXRenderListenerAdapter;
        if (this.mWXRenderListener != null && this.mWXRenderListener.getRenderListener() == null) {
            AliWeex.getInstance().onStage("ReSetRenderListener", (Map<String, Object>) null);
            this.mWXRenderListener.setRenderListener(wXRenderListenerAdapter);
        }
    }

    public void setNavBarAdapter(WXNavBarAdapter wXNavBarAdapter) {
        this.mNavBarAdapter = wXNavBarAdapter;
    }

    public WXNavBarAdapter getNavBarAdapter() {
        return this.mNavBarAdapter;
    }

    public NestedContainer getNestedContainer(WXSDKInstance wXSDKInstance) {
        if (this.mRenderPresenter != null) {
            return this.mRenderPresenter.getNestedContainer(wXSDKInstance);
        }
        return null;
    }

    @Deprecated
    public void startRender(String str) {
        startRenderByTemplate(str, (Map<String, Object>) null, (String) null);
    }

    @Deprecated
    public void startRender(String str, Map<String, Object> map, String str2) {
        startRenderByTemplate(str, map, str2);
    }

    @Deprecated
    public void startRenderWXByUrl(String str, String str2) {
        startRenderByUrl((Map<String, Object>) null, (String) null, str, str2);
    }

    @Deprecated
    public void startRenderWXByUrl(Map<String, Object> map, String str, String str2, String str3) {
        startRenderByUrl(map, str, str2, str3);
    }

    public void reload() {
        if (this.mRenderPresenter != null) {
            this.mRenderPresenter.reload();
        }
    }

    public void replace(String str, String str2) {
        if (this.mRenderPresenter != null) {
            this.mRenderPresenter.replace(str, str2);
        }
    }

    public void destroyWeex() {
        if (this.mRenderPresenter != null) {
            this.mRenderPresenter.destroySDKInstance();
        }
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public void startRenderByTemplate(String str, Map<String, Object> map, String str2) {
        startRenderByTemplate(str, "", map, str2);
    }

    /* access modifiers changed from: protected */
    public void startRenderByTemplate(String str, String str2, Map<String, Object> map, String str3) {
        if (this.mRenderPresenter != null) {
            this.mRenderPresenter.startRenderByTemplate(str, str2, map, str3);
        }
    }

    /* access modifiers changed from: protected */
    public void startRenderByUrl(Map<String, Object> map, String str, String str2, String str3) {
        if (this.mRenderPresenter != null) {
            this.mRenderPresenter.startRenderByUrl(map, str, str2, str3);
        }
    }

    public void fireEvent(String str, Map<String, Object> map) {
        if (this.mRenderPresenter != null) {
            this.mRenderPresenter.fireEvent(str, map);
        }
    }

    public void fireGlobalEvent(String str, Map<String, Object> map) {
        if (this.mRenderPresenter != null && this.mRenderPresenter.getWXSDKInstance() != null) {
            this.mRenderPresenter.getWXSDKInstance().fireGlobalEventCallback(str, map);
        }
    }

    public WXSDKInstance getWXSDKInstance() {
        return this.mRenderPresenter.getWXSDKInstance();
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        parseArgsFromActivity();
        registerBroadcastReceiver();
        setHasOptionsMenu(true);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (this.mUTEnable) {
            if (this.mUTPresenter == null) {
                this.mUTPresenter = new UTPresenter(getActivity());
            }
            this.mUTPresenter.skipPage();
        }
        if (this.mDyUrlEnable && this.mDynamicUrlPresenter == null) {
            this.mDynamicUrlPresenter = new DynamicUrlPresenter();
        }
        if (this.mProgressBarView == null) {
            this.mProgressBarView = new DefaultView.ProgressBarView();
        }
        if (this.mUrlValidate == null) {
            this.mUrlValidate = new UrlValidatePresenter(getActivity());
        }
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments != null && getContext() != null) {
            String string = arguments.getString(FRAGMENT_ARG_URI);
            String string2 = arguments.getString(FRAGMENT_ARG_BUNDLE_URL);
            String string3 = arguments.getString(FRAGMENT_ARG_RENDER_URL);
            String string4 = arguments.getString(FRAGMENT_ARG_TEMPLATE);
            HashMap hashMap = (HashMap) arguments.getSerializable(FRAGMENT_ARG_CUSTOM_OPT);
            String string5 = arguments.getString(FRAGMENT_ARG_INIT_DATA);
            if (!(this.mRenderPresenter == null || getContext() == null)) {
                this.mRenderPresenter.onActivityCreate(this.mRootView, hashMap, string5, string4, string2, string3, string);
            }
            arguments.remove(FRAGMENT_ARG_TEMPLATE);
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        frameLayout.addView(this.mProgressBarView.createProgressBar(getContext()));
        ViewStub viewStub = new ViewStub(getContext());
        viewStub.setId(R.id.wx_fragment_error);
        viewStub.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        viewStub.setLayoutResource(R.layout.weex_content_error);
        viewStub.setVisibility(8);
        frameLayout.addView(viewStub);
        this.mRootView = frameLayout;
        return frameLayout;
    }

    private boolean isScollViewOnTop(ViewGroup viewGroup) {
        if (viewGroup.getChildCount() > 0) {
            View childAt = viewGroup.getChildAt(0);
            if (!(childAt instanceof WXScrollView) || ((WXScrollView) childAt).getScrollY() != 0) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Deprecated
    public boolean isAtTop() {
        try {
            boolean isScollViewOnTop = (!(this.mRootView.getParent() instanceof FrameLayout) || !(((FrameLayout) this.mRootView.getParent()).getChildAt(1) instanceof ViewGroup)) ? false : isScollViewOnTop((ViewGroup) ((FrameLayout) this.mRootView.getParent()).getChildAt(1));
            if (!isScollViewOnTop) {
                try {
                    if (this.mRootView != null && this.mRootView.getChildCount() > 0) {
                        View childAt = this.mRootView.getChildAt(0);
                        if (childAt instanceof ViewGroup) {
                            return isScollViewOnTop((ViewGroup) childAt);
                        }
                    }
                } catch (Exception unused) {
                }
            }
            return isScollViewOnTop;
        } catch (Exception unused2) {
            return false;
        }
    }

    public void addViewToRoot(View view) {
        if (this.mRootView != null) {
            this.mRootView.addView(view);
        }
    }

    public void setRenderContainer(WXAbstractRenderContainer wXAbstractRenderContainer, boolean z) {
        this.mRenderContainer = wXAbstractRenderContainer;
        this.mIsHeron = false;
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.mRenderPresenter == null) {
            HashMap hashMap = new HashMap(1);
            hashMap.put("ListenerValue", this.mRenderListener == null ? "No" : "Yes");
            AliWeex.getInstance().onStage("createWXRenderListener", hashMap);
            this.mWXRenderListener = new WXRenderListener(this.mRootView, this.mProgressBarView, this.mUTPresenter, this.mRenderListener, new WXRenderListenerAdapter() {
                public void onViewCreated(WXSDKInstance wXSDKInstance, View view) {
                    super.onViewCreated(wXSDKInstance, view);
                    WeexPageFragment.this.onWXViewCreated(wXSDKInstance, view);
                }

                public void onException(WXSDKInstance wXSDKInstance, String str, String str2) {
                    super.onException(wXSDKInstance, str, str2);
                    WeexPageFragment.this.onWXException(wXSDKInstance, str, str2);
                }
            });
            this.mRenderPresenter = createRenderPresenter(this.mWXRenderListener, this.mUTPresenter, this.mDynamicUrlPresenter, this.mProgressBarView, this.mUrlValidate);
            if (this.mErrorView == null) {
                this.mErrorView = new DefaultView.ErrorView(this.mRenderPresenter);
            }
            this.mWXRenderListener.setErrorView(this.mErrorView);
            transformUrl();
            if (this.mRenderPresenter instanceof RenderPresenter) {
                ((RenderPresenter) this.mRenderPresenter).setRenderContainer(this.mRenderContainer, this.mIsHeron);
            } else {
                WXLogUtils.e(TAG, "  onViewCreated: heron RenderContainer  fail !!!");
            }
        }
    }

    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            if (this.mNavBarAdapter != null) {
                WXSDKEngine.setActivityNavBarSetter(this.mNavBarAdapter);
            }
            getActivity().invalidateOptionsMenu();
        }
    }

    public void onStart() {
        super.onStart();
        if (this.mRenderPresenter != null) {
            this.mRenderPresenter.onActivityStart();
        }
    }

    public void onStop() {
        super.onStop();
        if (this.mRenderPresenter != null) {
            this.mRenderPresenter.onActivityStop();
        }
    }

    public void onPause() {
        super.onPause();
        if (this.mUTPresenter != null) {
            this.mUTPresenter.pageDisappear();
        }
        if (this.mRenderPresenter != null) {
            this.mRenderPresenter.onActivityPause();
        }
        if (this.mNoAnimated && getActivity() != null) {
            getActivity().overridePendingTransition(0, 0);
        }
        WXSDKEngine.setActivityNavBarSetter((IActivityNavBarSetter) null);
    }

    public void onResume() {
        super.onResume();
        showFullScreenSystemStatusbar();
        if (this.mUTPresenter != null) {
            this.mUTPresenter.pageAppear(getUrl());
        }
        if (this.mRenderPresenter != null) {
            this.mRenderPresenter.onActivityResume();
        }
        if (this.mNavBarAdapter != null) {
            WXSDKEngine.setActivityNavBarSetter(this.mNavBarAdapter);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mRenderPresenter != null) {
            this.mRenderPresenter.onActivityDestroy();
        }
        if (this.mErrorView != null) {
            this.mErrorView.destroy();
        }
        if (WXEnvironment.isApkDebugable() && this.mRefreshReceiver != null) {
            getActivity().unregisterReceiver(this.mRefreshReceiver);
            this.mRefreshReceiver = null;
        }
        if (this.mReloadReceiver != null) {
            getActivity().unregisterReceiver(this.mReloadReceiver);
            this.mReloadReceiver = null;
        }
        if (this.mUTPresenter != null) {
            this.mUTPresenter.destroy();
        }
        if (this.mNavBarAdapter != null) {
            this.mNavBarAdapter.destroy();
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        if (this.mRenderPresenter != null) {
            this.mRenderPresenter.onCreateOptionsMenu(menu);
        }
    }

    public void setBackPressedListener(INavigationBarModuleAdapter.OnItemClickListener onItemClickListener) {
        this.mOnBackPressedListener = onItemClickListener;
    }

    public boolean onBackPressed() {
        if (this.mOnBackPressedListener != null) {
            this.mOnBackPressedListener.onClick(0);
            return true;
        } else if (this.mRenderPresenter != null) {
            return this.mRenderPresenter.onBackPressed();
        } else {
            return false;
        }
    }

    public boolean onSupportNavigateUp() {
        if (this.mRenderPresenter != null) {
            return this.mRenderPresenter.onSupportNavigateUp();
        }
        return false;
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        Intent intent = new Intent(WXModule.ACTION_REQUEST_PERMISSIONS_RESULT);
        intent.putExtra("requestCode", i);
        intent.putExtra(WXModule.PERMISSIONS, strArr);
        intent.putExtra(WXModule.GRANT_RESULTS, iArr);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (this.mRenderPresenter != null) {
            this.mRenderPresenter.onActivityResult(i, i2, intent);
        }
        Intent intent2 = intent == null ? new Intent() : new Intent(intent);
        intent2.setAction(WXModule.ACTION_ACTIVITY_RESULT);
        intent2.putExtra("requestCode", i);
        intent2.putExtra("resultCode", i2);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent2);
    }

    /* access modifiers changed from: protected */
    public void onWXViewCreated(WXSDKInstance wXSDKInstance, View view) {
        if ((wXSDKInstance instanceof AliWXSDKInstance) && getNavBarAdapter() != null) {
            ((AliWXSDKInstance) wXSDKInstance).setWXNavBarAdapter(getNavBarAdapter());
        }
        if (this.mUrlValidate != null) {
            this.mUrlValidate.onWXViewCreated(wXSDKInstance, view);
        }
    }

    /* access modifiers changed from: protected */
    public void onWXException(WXSDKInstance wXSDKInstance, String str, String str2) {
        if (this.mDynamicUrlPresenter != null) {
            this.mDynamicUrlPresenter.onWXException(wXSDKInstance, str, str2);
        }
    }

    /* access modifiers changed from: protected */
    public WeexPageContract.IRenderPresenter createRenderPresenter(IWXRenderListener iWXRenderListener, WeexPageContract.IUTPresenter iUTPresenter, WeexPageContract.IDynamicUrlPresenter iDynamicUrlPresenter, WeexPageContract.IProgressBar iProgressBar, WeexPageContract.IUrlValidate iUrlValidate) {
        return new RenderPresenter(getActivity(), this.mFtag, iWXRenderListener, iUTPresenter, iDynamicUrlPresenter, iProgressBar, getNavBarAdapter(), iUrlValidate);
    }

    private void transformUrl() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString(FRAGMENT_ARG_URI);
            String string2 = arguments.getString(FRAGMENT_ARG_BUNDLE_URL);
            String string3 = arguments.getString(FRAGMENT_ARG_RENDER_URL);
            if (TextUtils.isEmpty(string2) || TextUtils.isEmpty(string3)) {
                if (!TextUtils.isEmpty(string) && this.mRenderPresenter != null) {
                    this.mRenderPresenter.transformUrl(string, string);
                }
            } else if (this.mRenderPresenter != null) {
                this.mRenderPresenter.transformUrl(string2, string3);
            }
        }
    }

    private void registerBroadcastReceiver() {
        if (WXEnvironment.isApkDebugable()) {
            this.mRefreshReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    WeexPageFragment.this.reload();
                }
            };
            getActivity().registerReceiver(this.mRefreshReceiver, new IntentFilter("DEBUG_INSTANCE_REFRESH"));
        }
        try {
            this.mReloadReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (WeexPageFragment.this.getContext() != null) {
                        WeexPageFragment.this.reload();
                    }
                }
            };
            getActivity().registerReceiver(this.mReloadReceiver, new IntentFilter("INSTANCE_RELOAD"));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void showFullScreenSystemStatusbar() {
        if (this.mFullScreen != null && getActivity() != null && this.mFullScreen.booleanValue() && Build.VERSION.SDK_INT >= 16) {
            try {
                getActivity().getWindow().getDecorView().setSystemUiVisibility(4);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private void showFullScreenActionbar() {
        ActionBar supportActionBar;
        if (this.mFullScreen != null && getActivity() != null && this.mFullScreen.booleanValue() && Build.VERSION.SDK_INT >= 16) {
            try {
                if (getActivity().getActionBar() != null) {
                    getActivity().getActionBar().hide();
                    return;
                }
                boolean z = false;
                Class.forName("androidx.appcompat.app.AppCompatActivity");
                z = true;
                if (z && (getActivity() instanceof AppCompatActivity) && (supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar()) != null) {
                    supportActionBar.hide();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    private void parseArgsFromActivity() {
        boolean z;
        Bundle arguments = getArguments();
        if (arguments != null) {
            Serializable serializable = arguments.getSerializable(FRAGMENT_ARG_FROM_ACTIVITY);
            this.mFtag = arguments.getString(FRAGMENT_ARG_TAG);
            if (serializable instanceof Map) {
                Map map = (Map) serializable;
                String str = (String) map.get("orientation");
                if (!TextUtils.isEmpty(str) && str.equalsIgnoreCase("landscape") && getActivity() != null) {
                    String str2 = (String) map.get("preferredOrientation");
                    if ("landscapeRight".equalsIgnoreCase(str2)) {
                        getActivity().setRequestedOrientation(0);
                    } else if ("landscapeLeft".equalsIgnoreCase(str2)) {
                        getActivity().setRequestedOrientation(8);
                    } else {
                        getActivity().setRequestedOrientation(6);
                    }
                }
                this.mFullScreen = Boolean.valueOf((String) map.get("fullscreen"));
                showFullScreenSystemStatusbar();
                showFullScreenActionbar();
                Object obj = map.get("animated");
                if (obj != null) {
                    boolean z2 = true;
                    if (obj instanceof String) {
                        z = Boolean.parseBoolean((String) obj);
                    } else {
                        z = obj instanceof Boolean ? ((Boolean) obj).booleanValue() : true;
                    }
                    if (z) {
                        z2 = false;
                    }
                    this.mNoAnimated = z2;
                }
            }
        }
    }

    public static boolean shouldDegrade(WXSDKInstance wXSDKInstance, String str, String str2) {
        if (!TextUtils.isEmpty(str) && str.contains("|")) {
            return TextUtils.equals("1", str.substring(0, str.indexOf("|")));
        }
        if (TextUtils.equals(str, WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorCode()) && !TextUtils.isEmpty(str2) && str2.contains("createInstance fail")) {
            String instanceId = wXSDKInstance.getInstanceId();
            WXErrorCode wXErrorCode = WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED;
            WXExceptionUtils.commitCriticalExceptionRT(instanceId, wXErrorCode, "shouldDegrade", WXErrorCode.WX_DEGRAD_ERR_INSTANCE_CREATE_FAILED.getErrorMsg() + " -- " + str2, (Map<String, String>) null);
            return true;
        } else if (TextUtils.equals(str, WXErrorCode.WX_DEGRAD_EAGLE_RENDER_ERROR.getErrorCode()) && !TextUtils.isEmpty(str2) && str2.contains("eagleRenderErr")) {
            String instanceId2 = wXSDKInstance.getInstanceId();
            WXErrorCode wXErrorCode2 = WXErrorCode.WX_DEGRAD_EAGLE_RENDER_ERROR;
            WXExceptionUtils.commitCriticalExceptionRT(instanceId2, wXErrorCode2, "shouldDegrade", WXErrorCode.WX_DEGRAD_EAGLE_RENDER_ERROR.getErrorMsg() + " -- " + str2, (Map<String, String>) null);
            return true;
        } else if (TextUtils.equals(str, WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode()) && !TextUtils.isEmpty(str2) && str2.contains("degradeToH5")) {
            String instanceId3 = wXSDKInstance.getInstanceId();
            WXErrorCode wXErrorCode3 = WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR;
            WXExceptionUtils.commitCriticalExceptionRT(instanceId3, wXErrorCode3, "shouldDegrade", WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorMsg() + " -- " + str2, (Map<String, String>) null);
            return true;
        } else if (TextUtils.equals(str, WXErrorCode.WX_DEGRAD_ERR_NETWORK_CHECK_CONTENT_LENGTH_FAILED.getErrorCode()) && !TextUtils.isEmpty(str2) && str2.contains("degradeToH5")) {
            String instanceId4 = wXSDKInstance.getInstanceId();
            WXErrorCode wXErrorCode4 = WXErrorCode.WX_DEGRAD_ERR_NETWORK_CHECK_CONTENT_LENGTH_FAILED;
            WXExceptionUtils.commitCriticalExceptionRT(instanceId4, wXErrorCode4, "shouldDegrade", WXErrorCode.WX_DEGRAD_ERR_NETWORK_CHECK_CONTENT_LENGTH_FAILED.getErrorMsg() + "-- " + str2, (Map<String, String>) null);
            return true;
        } else if (!TextUtils.equals(str, WXErrorCode.WX_ERR_JSC_CRASH.getErrorCode()) || TextUtils.isEmpty(str2) || !str2.contains("degradeToH5")) {
            return false;
        } else {
            String instanceId5 = wXSDKInstance.getInstanceId();
            WXErrorCode wXErrorCode5 = WXErrorCode.WX_ERR_JSC_CRASH;
            WXExceptionUtils.commitCriticalExceptionRT(instanceId5, wXErrorCode5, "shouldDegrade", WXErrorCode.WX_ERR_JSC_CRASH.getErrorMsg() + "-- " + str2, (Map<String, String>) null);
            return true;
        }
    }
}
