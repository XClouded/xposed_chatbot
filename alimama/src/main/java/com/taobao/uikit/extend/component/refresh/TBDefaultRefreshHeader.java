package com.taobao.uikit.extend.component.refresh;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import com.taobao.android.animationkit.AnimationView;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.component.refresh.TBRefreshHeader;

public class TBDefaultRefreshHeader extends TBRefreshHeader {
    private static final String TAG = "TBDefaultRefreshHeader";
    private String[] mDefaultRefreshAnimation = {"pullrefresh/normal/uik_pull_to_refresh.json", "pullrefresh/normal/uik_refreshing.json", "pullrefresh/normal/uik_refresh_finished.json"};
    private String[] mDefaultRefreshTips = {getContext().getString(R.string.uik_pull_to_refresh), getContext().getString(R.string.uik_release_to_refresh), getContext().getString(R.string.uik_refreshing), getContext().getString(R.string.uik_refresh_finished)};
    private String[] mRefreshAnimation = {"pullrefresh/normal/uik_pull_to_refresh.json", "pullrefresh/normal/uik_refreshing.json", "pullrefresh/normal/uik_refresh_finished.json"};
    private FrameLayout mRefreshHeaderView;
    private AnimationView mRefreshProgressView1;
    private AnimationView mRefreshProgressView2;
    private AnimationView mRefreshProgressView3;
    private String[] mRefreshTips = {getContext().getString(R.string.uik_pull_to_refresh), getContext().getString(R.string.uik_release_to_refresh), getContext().getString(R.string.uik_refreshing), getContext().getString(R.string.uik_refresh_finished)};
    private FrameLayout mRefreshViewGroup;
    private View mSecondFloorView;

    public void setRefreshTipColor(@ColorInt int i) {
    }

    public void setRefreshTips(String[] strArr) {
    }

    public TBDefaultRefreshHeader(Context context) {
        super(context);
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.uik_swipe_refresh_header, this, true);
        setBackgroundResource(R.color.uik_refresh_head_bg);
        this.mRefreshHeaderView = (FrameLayout) findViewById(R.id.uik_refresh_header);
        this.mRefreshViewGroup = (FrameLayout) findViewById(R.id.uik_refresh_header_fl);
        this.mRefreshProgressView1 = (AnimationView) findViewById(R.id.uik_refresh_header_progress1);
        this.mRefreshProgressView2 = (AnimationView) findViewById(R.id.uik_refresh_header_progress2);
        this.mRefreshProgressView3 = (AnimationView) findViewById(R.id.uik_refresh_header_progress3);
        this.mSecondFloorView = findViewById(R.id.uik_refresh_header_second_floor);
        this.mRefreshProgressView1.setImageAssetsFolder("pullrefresh/normal/images");
        this.mRefreshProgressView2.setImageAssetsFolder("pullrefresh/normal/images");
        this.mRefreshProgressView3.setImageAssetsFolder("pullrefresh/normal/images");
        changeToState(TBRefreshHeader.RefreshState.NONE);
    }

    public void setProgress(float f) {
        if (this.mRefreshProgressView1 != null) {
            this.mRefreshProgressView1.setProgress(f);
        }
        if (this.mRefreshProgressView2 != null) {
            this.mRefreshProgressView2.setProgress(f);
        }
        if (this.mRefreshProgressView3 != null) {
            this.mRefreshProgressView3.setProgress(f);
        }
    }

    public void changeToState(TBRefreshHeader.RefreshState refreshState) {
        if (this.mState != refreshState) {
            Log.d(TAG, "changeToState called: oldState is " + this.mState.toString() + " newState is " + refreshState.toString());
            if (this.mPullRefreshListener != null) {
                this.mPullRefreshListener.onRefreshStateChanged(this.mState, refreshState);
            }
            this.mState = refreshState;
            this.mRefreshProgressView1.setAnimation(this.mRefreshAnimation == null ? this.mDefaultRefreshAnimation[0] : this.mRefreshAnimation[0]);
            this.mRefreshProgressView2.setAnimation(this.mRefreshAnimation == null ? this.mDefaultRefreshAnimation[1] : this.mRefreshAnimation[1]);
            this.mRefreshProgressView3.setAnimation(this.mRefreshAnimation == null ? this.mDefaultRefreshAnimation[2] : this.mRefreshAnimation[2]);
            switch (this.mState) {
                case NONE:
                    this.mRefreshProgressView3.setVisibility(0);
                    this.mRefreshProgressView1.setVisibility(8);
                    this.mRefreshProgressView2.setVisibility(8);
                    this.mRefreshProgressView1.cancelAnimation();
                    this.mRefreshProgressView2.cancelAnimation();
                    this.mRefreshProgressView3.cancelAnimation();
                    break;
                case PULL_TO_REFRESH:
                    this.mRefreshProgressView1.setVisibility(0);
                    this.mRefreshProgressView2.setVisibility(8);
                    this.mRefreshProgressView3.setVisibility(8);
                    this.mRefreshViewGroup.setVisibility(0);
                    break;
                case RELEASE_TO_REFRESH:
                    this.mRefreshViewGroup.setVisibility(0);
                    break;
                case REFRESHING:
                    this.mRefreshProgressView2.setVisibility(0);
                    this.mRefreshProgressView1.setVisibility(8);
                    this.mRefreshProgressView3.setVisibility(8);
                    this.mRefreshViewGroup.setVisibility(0);
                    break;
                case PREPARE_TO_SECOND_FLOOR:
                    this.mRefreshViewGroup.setVisibility(8);
                    break;
            }
            if (this.mState == TBRefreshHeader.RefreshState.REFRESHING) {
                this.mRefreshProgressView2.playAnimation();
            }
        }
    }

    public void setRefreshAnimation(String[] strArr, @Nullable String str) {
        if (strArr == null || strArr.length != 3) {
            this.mRefreshAnimation = null;
        }
        this.mRefreshAnimation = strArr;
        this.mRefreshProgressView1.setImageAssetsFolder(str);
        this.mRefreshProgressView2.setImageAssetsFolder(str);
        this.mRefreshProgressView3.setImageAssetsFolder(str);
    }

    public View getRefreshView() {
        return this.mRefreshViewGroup;
    }

    public View getSecondFloorView() {
        return this.mSecondFloorView;
    }

    public void setSecondFloorView(View view) {
        if (this.mSecondFloorView != null) {
            if (this.mRefreshHeaderView != null) {
                this.mRefreshHeaderView.removeView(this.mSecondFloorView);
                this.mRefreshHeaderView.addView(view, 0, (FrameLayout.LayoutParams) this.mSecondFloorView.getLayoutParams());
                this.mSecondFloorView = view;
                this.mSecondFloorView.setId(R.id.uik_refresh_header_second_floor);
            }
        } else if (this.mRefreshHeaderView != null) {
            this.mRefreshHeaderView.addView(view, 0, new FrameLayout.LayoutParams(-2, -2));
            this.mSecondFloorView = view;
            this.mSecondFloorView.setId(R.id.uik_refresh_header_second_floor);
        }
    }

    public void switchStyle(TBRefreshHeader.RefreshHeaderStyle refreshHeaderStyle) {
        switch (refreshHeaderStyle) {
            case NORMAL:
                this.mDefaultRefreshAnimation = new String[]{"pullrefresh/normal/uik_pull_to_refresh.json", "pullrefresh/normal/uik_refreshing.json", "pullrefresh/normal/uik_refresh_finished.json"};
                this.mRefreshAnimation = new String[]{"pullrefresh/normal/uik_pull_to_refresh.json", "pullrefresh/normal/uik_refreshing.json", "pullrefresh/normal/uik_refresh_finished.json"};
                this.mRefreshProgressView1.setImageAssetsFolder("pullrefresh/normal/images");
                this.mRefreshProgressView2.setImageAssetsFolder("pullrefresh/normal/images");
                this.mRefreshProgressView3.setImageAssetsFolder("pullrefresh/normal/images");
                return;
            case DARK:
                this.mDefaultRefreshAnimation = new String[]{"pullrefresh/dark/uik_pull_to_refresh.json", "pullrefresh/dark/uik_refreshing.json", "pullrefresh/dark/uik_refresh_finished.json"};
                this.mRefreshAnimation = new String[]{"pullrefresh/dark/uik_pull_to_refresh.json", "pullrefresh/dark/uik_refreshing.json", "pullrefresh/dark/uik_refresh_finished.json"};
                this.mRefreshProgressView1.setImageAssetsFolder("pullrefresh/dark/images");
                this.mRefreshProgressView2.setImageAssetsFolder("pullrefresh/dark/images");
                this.mRefreshProgressView3.setImageAssetsFolder("pullrefresh/dark/images");
                this.mRefreshProgressView1.setAnimation(this.mRefreshAnimation == null ? this.mDefaultRefreshAnimation[0] : this.mRefreshAnimation[0]);
                this.mRefreshProgressView2.setAnimation(this.mRefreshAnimation == null ? this.mDefaultRefreshAnimation[1] : this.mRefreshAnimation[1]);
                this.mRefreshProgressView3.setAnimation(this.mRefreshAnimation == null ? this.mDefaultRefreshAnimation[2] : this.mRefreshAnimation[2]);
                return;
            default:
                return;
        }
    }

    public void setHardwareEnable(boolean z) {
        this.mRefreshProgressView1.setHardwareEnable(z);
        this.mRefreshProgressView2.setHardwareEnable(z);
        this.mRefreshProgressView3.setHardwareEnable(z);
    }
}
