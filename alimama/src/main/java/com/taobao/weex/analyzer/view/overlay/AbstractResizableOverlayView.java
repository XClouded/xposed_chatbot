package com.taobao.weex.analyzer.view.overlay;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.rd.animation.AbsAnimation;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.analyzer.view.overlay.IResizableView;

public abstract class AbstractResizableOverlayView extends PermissionOverlayView implements IResizableView {
    private IResizableView.OnSizeChangedListener mOnSizeChangedListener;
    /* access modifiers changed from: protected */
    public int mViewSize = 1;

    public AbstractResizableOverlayView(Context context, Config config) {
        super(context, true, config);
    }

    public void setOnSizeChangedListener(@NonNull IResizableView.OnSizeChangedListener onSizeChangedListener) {
        this.mOnSizeChangedListener = onSizeChangedListener;
    }

    public void setViewSize(int i) {
        this.mViewSize = i;
    }

    public void setViewSize(int i, @Nullable View view, boolean z) {
        FrameLayout.LayoutParams layoutParams;
        if (view != null && (layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams()) != null) {
            int dp2px = (int) ViewUtils.dp2px(this.mContext, 200);
            int dp2px2 = (int) ViewUtils.dp2px(this.mContext, AbsAnimation.DEFAULT_ANIMATION_TIME);
            int dp2px3 = (int) ViewUtils.dp2px(this.mContext, 450);
            int i2 = layoutParams.height;
            switch (i) {
                case 0:
                    break;
                case 1:
                    dp2px = dp2px2;
                    break;
                case 2:
                    dp2px = dp2px3;
                    break;
                default:
                    dp2px = i2;
                    break;
            }
            if (dp2px != layoutParams.height) {
                layoutParams.height = dp2px;
                view.setLayoutParams(layoutParams);
                if (this.mOnSizeChangedListener != null && z) {
                    this.mOnSizeChangedListener.onSizeChanged(i);
                }
            }
        }
    }
}
