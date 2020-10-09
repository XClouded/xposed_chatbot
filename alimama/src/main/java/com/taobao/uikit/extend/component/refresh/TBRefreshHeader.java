package com.taobao.uikit.extend.component.refresh;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import com.taobao.uikit.extend.component.refresh.TBSwipeRefreshLayout;

public abstract class TBRefreshHeader extends RelativeLayout {
    protected TBSwipeRefreshLayout.OnPullRefreshListener mPullRefreshListener;
    protected RefreshState mState = RefreshState.NONE;

    public enum RefreshHeaderStyle {
        NORMAL,
        DARK
    }

    public abstract void changeToState(RefreshState refreshState);

    public abstract View getRefreshView();

    public abstract View getSecondFloorView();

    public abstract void setProgress(float f);

    public abstract void setRefreshAnimation(String[] strArr, @Nullable String str);

    public abstract void setRefreshTipColor(@ColorInt int i);

    public abstract void setRefreshTips(String[] strArr);

    public abstract void setSecondFloorView(View view);

    public void switchStyle(RefreshHeaderStyle refreshHeaderStyle) {
    }

    public enum RefreshState {
        NONE,
        PULL_TO_REFRESH,
        RELEASE_TO_REFRESH,
        REFRESHING,
        PREPARE_TO_SECOND_FLOOR,
        SECOND_FLOOR_START,
        SECOND_FLOOR_END;

        public String toString() {
            switch (this) {
                case NONE:
                    return "NONE";
                case PULL_TO_REFRESH:
                    return "PULL_TO_REFRESH";
                case RELEASE_TO_REFRESH:
                    return "RELEASE_TO_REFRESH";
                case REFRESHING:
                    return "REFRESHING";
                case PREPARE_TO_SECOND_FLOOR:
                    return "PREPARE_TO_SECOND_FLOOR";
                case SECOND_FLOOR_START:
                    return "SECOND_FLOOR_START";
                case SECOND_FLOOR_END:
                    return "SECOND_FLOOR_END";
                default:
                    return "";
            }
        }
    }

    public TBRefreshHeader(Context context) {
        super(context);
    }

    public RefreshState getCurrentState() {
        return this.mState;
    }

    public void setPullRefreshListener(TBSwipeRefreshLayout.OnPullRefreshListener onPullRefreshListener) {
        this.mPullRefreshListener = onPullRefreshListener;
    }
}
