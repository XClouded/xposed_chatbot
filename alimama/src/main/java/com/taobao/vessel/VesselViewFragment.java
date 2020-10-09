package com.taobao.vessel;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import com.taobao.android.vesselview.R;
import com.taobao.tao.log.TLog;
import com.taobao.vessel.Vessel;
import com.taobao.vessel.base.VesselBaseView;
import com.taobao.vessel.callback.OnLoadListener;
import com.taobao.vessel.callback.ScrollViewListener;
import com.taobao.vessel.callback.VesselViewCallback;
import com.taobao.vessel.utils.Utils;
import com.taobao.vessel.utils.VesselType;
import com.taobao.vessel.utils.navigator.NavigatorBarSetter;
import com.taobao.vessel.weex.VesselWeexView;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;
import java.util.Map;

public class VesselViewFragment extends Fragment {
    public static final String TAG = VesselView.class.getSimpleName();
    private View mContentView;
    protected boolean mDowngradeEnable = true;
    protected String mDowngradeUrl = null;
    protected VesselBaseView.EventCallback mEventCallback;
    protected boolean mIsShowloading;
    private IActivityNavBarSetter mNavigatorBarSetter;
    private OnLoadListener mOnLoadListener;
    private ScrollViewListener mScrollViewListener;
    @Deprecated
    protected View mTabbar;
    @Deprecated
    protected View mToolbar;
    protected String mUri = null;
    protected String mVesselData;
    protected Object mVesselParams;
    protected VesselType mVesselType;
    protected VesselView mVesselView = null;
    protected VesselViewCallback mViewCallback;

    public static VesselViewFragment newInstance() {
        return new VesselViewFragment();
    }

    public static VesselViewFragment newInstance(Vessel.Config config) {
        VesselViewFragment vesselViewFragment = new VesselViewFragment();
        Vessel.getInstance().init(config);
        return vesselViewFragment;
    }

    public void loadUrl(String str) {
        loadUrl(str, (Object) null);
    }

    public void loadUrl(String str, Object obj) {
        loadUrl((VesselType) null, str, obj);
    }

    public void loadUrl(VesselType vesselType, String str, Object obj) {
        this.mVesselType = vesselType;
        if (this.mVesselType == null) {
            this.mVesselType = Utils.getUrlType(str);
        }
        this.mUri = str;
        this.mVesselParams = obj;
        if (this.mVesselView != null) {
            this.mVesselView.loadUrl(vesselType, str, obj);
        }
    }

    public void setUrl(String str) {
        setUrl(str, (Object) null);
    }

    public void setUrl(String str, Object obj) {
        setUrl((VesselType) null, str, obj);
    }

    public void setUrl(VesselType vesselType, String str, Object obj) {
        this.mVesselType = vesselType;
        if (this.mVesselType == null) {
            this.mVesselType = Utils.getUrlType(str);
        }
        this.mUri = str;
        this.mVesselParams = obj;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        TLog.logd(TAG, "onCreateView");
        if (this.mToolbar == null && this.mTabbar == null) {
            if (this.mVesselView == null) {
                this.mVesselView = new VesselView(getActivity());
            }
            this.mContentView = this.mVesselView;
        } else {
            addBar(layoutInflater);
        }
        registerListener();
        if (this.mVesselType == null) {
            this.mVesselType = VesselType.Weex;
        }
        if (!TextUtils.isEmpty(this.mUri)) {
            this.mVesselView.loadUrl(this.mVesselType, this.mUri, this.mVesselParams);
        } else if (!TextUtils.isEmpty(this.mVesselData)) {
            this.mVesselView.loadData(this.mVesselType, this.mVesselData);
        }
        return this.mContentView;
    }

    private void registerListener() {
        if (this.mVesselView != null) {
            if (this.mOnLoadListener != null) {
                this.mVesselView.setOnLoadListener(this.mOnLoadListener);
            }
            if (this.mViewCallback != null) {
                this.mVesselView.setVesselViewCallback(this.mViewCallback);
            }
            if (this.mScrollViewListener != null) {
                this.mVesselView.setOnScrollViewListener(this.mScrollViewListener);
            }
            if (this.mEventCallback != null) {
                this.mVesselView.setEventCallback(this.mEventCallback);
            }
            this.mVesselView.setShowLoading(this.mIsShowloading);
            this.mVesselView.setDowngradeEnable(this.mDowngradeEnable);
            this.mVesselView.setDowngradeUrl(this.mDowngradeUrl);
        }
    }

    private void removeAllListeners() {
        this.mOnLoadListener = null;
        this.mViewCallback = null;
        this.mScrollViewListener = null;
        this.mEventCallback = null;
    }

    @Deprecated
    public void addBar(LayoutInflater layoutInflater) {
        TLog.logd(TAG, "addBar");
        this.mContentView = layoutInflater.inflate(R.layout.vessel_panel, (ViewGroup) null);
        if (this.mToolbar != null) {
            ((ViewGroup) this.mContentView.findViewById(R.id.top_frame)).addView(this.mToolbar, new FrameLayout.LayoutParams(-1, -2));
        }
        if (this.mTabbar != null) {
            ((ViewGroup) this.mContentView.findViewById(R.id.bottom_frame)).addView(this.mTabbar, new FrameLayout.LayoutParams(-1, -2));
        }
        this.mVesselView = (VesselView) this.mContentView.findViewById(R.id.vesselview);
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        VesselWeexView vesselWeexView = (this.mVesselView == null || this.mVesselView.getChildProxyView() == null || !(this.mVesselView.getChildProxyView() instanceof VesselWeexView)) ? null : (VesselWeexView) this.mVesselView.getChildProxyView();
        if (z && vesselWeexView != null) {
            vesselWeexView.onAppear();
        } else if (!z && vesselWeexView != null) {
            vesselWeexView.onDisappear();
        }
    }

    public void setDowngradeUrl(String str) {
        this.mDowngradeUrl = str;
        if (this.mVesselView != null) {
            this.mVesselView.setDowngradeUrl(str);
        }
    }

    public void setData(VesselType vesselType, String str) {
        loadData(vesselType, str, (Map) null);
    }

    public void setData(VesselType vesselType, String str, Map map) {
        this.mVesselType = vesselType;
        this.mVesselData = str;
        this.mVesselParams = map;
    }

    public void loadData(VesselType vesselType, String str) {
        loadData(vesselType, str, (Map) null);
    }

    public void loadData(VesselType vesselType, String str, Map map) {
        this.mVesselType = vesselType;
        this.mVesselData = str;
        if (this.mVesselView != null) {
            this.mVesselView.loadData(vesselType, str, map);
        }
    }

    public void onStart() {
        super.onStart();
        if (this.mVesselView != null) {
            this.mVesselView.onStart();
        }
    }

    public void onResume() {
        super.onResume();
        if (Vessel.getInstance().getActivityBarSetter() != null) {
            this.mNavigatorBarSetter = Vessel.getInstance().getActivityBarSetter();
        } else {
            this.mNavigatorBarSetter = new NavigatorBarSetter(getActivity());
        }
        WXSDKEngine.setActivityNavBarSetter(this.mNavigatorBarSetter);
        if (this.mVesselView != null) {
            this.mVesselView.onResume();
        }
    }

    public void onPause() {
        super.onPause();
        if (this.mVesselView != null) {
            this.mVesselView.onPause();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (this.mVesselView != null) {
            this.mVesselView.setVisibility(8);
            this.mVesselView.removeAllViews();
            this.mVesselView.onDestroy();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        removeAllListeners();
        if (WXSDKEngine.getActivityNavBarSetter() != null && (WXSDKEngine.getActivityNavBarSetter() instanceof NavigatorBarSetter)) {
            WXSDKEngine.setActivityNavBarSetter((IActivityNavBarSetter) null);
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        TLog.logd(TAG, "onSaveInstanceState");
    }

    public VesselView getVesselView() {
        return this.mVesselView;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
        if (this.mVesselView != null) {
            this.mVesselView.setOnLoadListener(onLoadListener);
        }
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.mScrollViewListener = scrollViewListener;
        if (this.mVesselView != null) {
            this.mVesselView.setOnScrollViewListener(this.mScrollViewListener);
        }
    }

    public void setVesselCallback(VesselViewCallback vesselViewCallback) {
        this.mViewCallback = vesselViewCallback;
        if (this.mVesselView != null) {
            this.mVesselView.setVesselViewCallback(this.mViewCallback);
        }
    }

    public void setEventCallback(VesselBaseView.EventCallback eventCallback) {
        this.mEventCallback = eventCallback;
        if (this.mVesselView != null) {
            this.mVesselView.setEventCallback(eventCallback);
        }
    }

    public void setIsLoading(boolean z) {
        this.mIsShowloading = z;
        if (this.mVesselView != null) {
            this.mVesselView.setShowLoading(z);
        }
    }

    public void setDowngradeEable(boolean z) {
        this.mDowngradeEnable = z;
        if (this.mVesselView != null) {
            this.mVesselView.setDowngradeEnable(this.mDowngradeEnable);
        }
    }

    @Deprecated
    public void setToolbar(View view) {
        if (view != null) {
            this.mToolbar = view;
        }
    }

    @Deprecated
    public void setTabbar(View view) {
        if (view != null) {
            this.mTabbar = view;
        }
    }
}
