package com.taobao.vessel.local;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.framework.BundleImpl;
import android.taobao.atlas.framework.BundleInstallerFetcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import com.taobao.vessel.base.VesselBaseView;
import com.taobao.vessel.model.VesselError;
import com.taobao.vessel.utils.Utils;
import com.taobao.vessel.utils.VesselConstants;
import com.taobao.vessel.utils.VesselType;
import java.lang.reflect.Constructor;
import java.util.Map;
import org.osgi.framework.BundleException;

public class VesselNativeView extends VesselBaseView {
    /* access modifiers changed from: private */
    public Runnable loadRunnabe;
    private String mBundleName;
    private String mClassName;
    /* access modifiers changed from: private */
    public Handler mHandler;
    protected VesselNativeFrameLayout mNativeFrameLayout;

    @Deprecated
    public void loadData(String str) {
    }

    public VesselNativeView(Context context) {
        this(context, (AttributeSet) null);
    }

    public VesselNativeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VesselNativeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHandler = new Handler();
        this.loadRunnabe = new Runnable() {
            public void run() {
                if (!Utils.checkActivityDestroy(VesselNativeView.this.getContext())) {
                    VesselNativeView.this.loadView();
                }
            }
        };
        if (getId() == -1) {
            setId(Utils.generateViewId());
        }
    }

    public void fireEvent(String str, Map<String, Object> map) {
        fireEvent(map);
    }

    public void fireEvent(Map<String, Object> map) {
        if (this.mNativeFrameLayout != null) {
            this.mNativeFrameLayout.onFireEvent(map);
        }
    }

    /* access modifiers changed from: private */
    public void getExternalViewFromeBundle(String str) {
        if (str != null) {
            this.mOriginUrl = str;
            this.mBundleName = Utils.getBundleName(str);
            this.mClassName = Utils.getClassName(str);
            if (this.mBundleName != null && this.mClassName != null) {
                try {
                    if (getClass().getClassLoader() != Atlas.getInstance().getBundleClassLoader(this.mBundleName)) {
                        requestRuntimeDependencySync(getClass().getClassLoader(), this.mBundleName, false);
                    }
                    Class<?> loadClass = Atlas.getInstance().getBundleClassLoader(this.mBundleName).loadClass(this.mClassName);
                    Constructor<?> constructor = loadClass.getConstructor(new Class[]{Context.class});
                    if (loadClass != null) {
                        this.mNativeFrameLayout = (VesselNativeFrameLayout) constructor.newInstance(new Object[]{getContext()});
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
    }

    public void requestRuntimeDependencySync(ClassLoader classLoader, String str, boolean z) throws BundleException {
        if (Atlas.getInstance().getBundle(str) == null) {
            BundleInstallerFetcher.obtainInstaller().installTransitivelySync(new String[]{str});
        }
        BundleImpl bundle = Atlas.getInstance().getBundle(str);
        if (bundle != null) {
            Atlas.getInstance().requestRuntimeDependency(classLoader, bundle.getClassLoader(), z);
            return;
        }
        throw new BundleException("failed install deppendencyBundle : " + str);
    }

    public VesselNativeFrameLayout getNativeFragment() {
        return this.mNativeFrameLayout;
    }

    /* access modifiers changed from: private */
    public void loadView() {
        if (this.mNativeFrameLayout == null) {
            onLoadError(new VesselError(VesselConstants.LOAD_ERROR, VesselConstants.LOAD_DATA_NULL, VesselType.Native));
            return;
        }
        this.mNativeFrameLayout.setVesselParams(this.mOriginParams);
        this.mNativeFrameLayout.setVesselViewCallback(this.mVesselViewCallback);
        this.mNativeFrameLayout.setOnLoadListener(this);
        this.mNativeFrameLayout.setScrollViewListener(this);
        this.mNativeFrameLayout.onViewCreated(this.mNativeFrameLayout.onCreateView(LayoutInflater.from(getContext()), this.mNativeFrameLayout, (Bundle) null), (Bundle) null);
        if (this.mNativeFrameLayout.getParent() == null) {
            addView(this.mNativeFrameLayout);
        }
    }

    public void loadUrl(final String str, Object obj) {
        onLoadStart();
        if (obj != null) {
            this.mOriginParams = obj;
        }
        if (Atlas.getInstance().getBundle(Utils.getBundleName(str)) == null) {
            new Thread(new Runnable() {
                public void run() {
                    VesselNativeView.this.getExternalViewFromeBundle(str);
                    VesselNativeView.this.mHandler.post(VesselNativeView.this.loadRunnabe);
                }
            }).start();
            return;
        }
        getExternalViewFromeBundle(str);
        loadView();
    }

    public void releaseMemory() {
        removeAllViews();
        if (this.mNativeFrameLayout != null) {
            this.mNativeFrameLayout = null;
        }
        onDestroy();
    }

    public View getChildView() {
        return this.mNativeFrameLayout;
    }

    public boolean refresh(Object obj) {
        if (obj != null) {
            loadUrl(this.mOriginUrl, obj);
            return false;
        }
        loadUrl(this.mOriginUrl, this.mOriginParams);
        return false;
    }

    public void onStart() {
        if (this.mNativeFrameLayout != null) {
            this.mNativeFrameLayout.onStart();
        }
    }

    public void onResume() {
        if (this.mNativeFrameLayout != null) {
            this.mNativeFrameLayout.onResume();
        }
    }

    public void onPause() {
        if (this.mNativeFrameLayout != null) {
            this.mNativeFrameLayout.onPause();
        }
    }

    public void onStop() {
        if (this.mNativeFrameLayout != null) {
            this.mNativeFrameLayout.onStop();
        }
    }

    public void onDestroy() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        if (this.mNativeFrameLayout != null) {
            this.mNativeFrameLayout.onDestroy();
        }
    }
}
