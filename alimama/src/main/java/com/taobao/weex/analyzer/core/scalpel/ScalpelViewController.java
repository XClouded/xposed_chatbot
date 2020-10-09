package com.taobao.weex.analyzer.core.scalpel;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.IPermissionHandler;
import com.taobao.weex.analyzer.core.scalpel.ScalpelFrameLayout;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.analyzer.view.overlay.IOverlayView;
import com.taobao.weex.analyzer.view.overlay.SimpleOverlayView;

public class ScalpelViewController implements IPermissionHandler {
    private boolean isDrawId;
    private boolean isDrawViewName;
    private boolean isScalpelEnabled;
    private Config mConfig;
    private Context mContext;
    private ScalpelFrameLayout.OnDrawViewNameListener mOnDrawViewNameListener;
    private OnToggleListener mOnToggleListener;
    private ScalpelFrameLayout mScalpelLayout;
    private SimpleOverlayView mSwitchView;

    public interface OnToggleListener {
        void onToggle(View view, boolean z);
    }

    public ScalpelViewController(Context context, Config config) {
        this(false, false, true, context);
        this.mConfig = config;
    }

    public ScalpelViewController(boolean z, boolean z2, boolean z3, Context context) {
        this.isScalpelEnabled = false;
        this.isDrawId = false;
        this.isDrawViewName = false;
        this.isScalpelEnabled = z;
        this.isDrawId = z2;
        this.isDrawViewName = z3;
        this.mContext = context;
        this.mSwitchView = new SimpleOverlayView.Builder(this.mContext, "close").enableDrag(false).listener(new SimpleOverlayView.OnClickListener() {
            public void onClick(@NonNull IOverlayView iOverlayView) {
                ScalpelViewController.this.setScalpelEnabled(false);
            }
        }).gravity(53).y((int) ViewUtils.dp2px(this.mContext, 60)).build();
    }

    public void setOnToggleListener(OnToggleListener onToggleListener) {
        this.mOnToggleListener = onToggleListener;
    }

    public void setOnDrawViewNameListener(ScalpelFrameLayout.OnDrawViewNameListener onDrawViewNameListener) {
        this.mOnDrawViewNameListener = onDrawViewNameListener;
    }

    public View wrapView(@Nullable View view) {
        if (view == null) {
            return null;
        }
        if (this.mConfig != null && !isPermissionGranted(this.mConfig)) {
            return view;
        }
        this.mScalpelLayout = new ScalpelFrameLayout(view.getContext());
        this.mScalpelLayout.setDrawIds(this.isDrawId);
        this.mScalpelLayout.setDrawViewNames(this.isDrawViewName);
        if (this.mOnDrawViewNameListener != null) {
            this.mScalpelLayout.setOnDrawViewNameListener(this.mOnDrawViewNameListener);
        }
        this.mScalpelLayout.addView(view);
        this.mScalpelLayout.setLayerInteractionEnabled(this.isScalpelEnabled);
        return this.mScalpelLayout;
    }

    public boolean isScalpelEnabled() {
        return this.isScalpelEnabled;
    }

    public void setScalpelEnabled(boolean z) {
        if (this.mConfig == null || isPermissionGranted(this.mConfig)) {
            this.isScalpelEnabled = z;
            if (this.mScalpelLayout != null) {
                this.mScalpelLayout.setLayerInteractionEnabled(this.isScalpelEnabled);
                if (z) {
                    this.mSwitchView.show();
                } else {
                    this.mSwitchView.dismiss();
                }
                if (this.mOnToggleListener != null) {
                    this.mOnToggleListener.onToggle(this.mScalpelLayout, this.isScalpelEnabled);
                }
            }
        }
    }

    public boolean isDrawIdEnabled() {
        return this.isDrawId;
    }

    public void setDrawId(boolean z) {
        this.isDrawId = z;
        if (this.mScalpelLayout != null) {
            this.mScalpelLayout.setDrawIds(this.isDrawId);
        }
    }

    public boolean isDrawViewNameEnabled() {
        return this.isDrawViewName;
    }

    public void setDrawViewName(boolean z) {
        this.isDrawViewName = z;
        if (this.mScalpelLayout != null) {
            this.mScalpelLayout.setDrawViewNames(this.isDrawViewName);
        }
    }

    public void toggleScalpelEnabled() {
        if (this.mConfig == null || isPermissionGranted(this.mConfig)) {
            this.isScalpelEnabled = !this.isScalpelEnabled;
            if (this.mScalpelLayout != null) {
                setScalpelEnabled(this.isScalpelEnabled);
            }
        }
    }

    public void pause() {
        if ((this.mConfig == null || isPermissionGranted(this.mConfig)) && this.mSwitchView != null && this.isScalpelEnabled) {
            this.mSwitchView.dismiss();
        }
    }

    public void resume() {
        if ((this.mConfig == null || isPermissionGranted(this.mConfig)) && this.mSwitchView != null && this.isScalpelEnabled) {
            this.mSwitchView.show();
        }
    }

    public boolean isPermissionGranted(@NonNull Config config) {
        return !config.getIgnoreOptions().contains(Config.TYPE_3D);
    }
}
