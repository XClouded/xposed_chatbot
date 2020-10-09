package com.taobao.uikit.extend.feature.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.material.TBCircularProgressDrawable;

public class TZoomPagerItem extends FrameLayout {
    private static final String TAG = "TZoomPagerItem";
    /* access modifiers changed from: private */
    public LinearLayout mErrorView;
    private TZoomImageView mImageView;
    /* access modifiers changed from: private */
    public ImageView mProgressBar;
    /* access modifiers changed from: private */
    public TBCircularProgressDrawable mProgressDrawable;

    public TZoomPagerItem(Context context) {
        super(context);
        init(context);
    }

    public TZoomPagerItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public TZoomPagerItem(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.uik_zoom_page_item, this);
        this.mImageView = (TZoomImageView) inflate.findViewById(R.id.img);
        this.mProgressBar = (ImageView) inflate.findViewById(R.id.progressbar);
        this.mProgressDrawable = new TBCircularProgressDrawable(-1, 4.0f);
        this.mProgressDrawable.start();
        this.mProgressBar.setImageDrawable(this.mProgressDrawable);
        this.mErrorView = (LinearLayout) inflate.findViewById(R.id.error_view);
        this.mImageView.succListener(new IPhenixListener<SuccPhenixEvent>() {
            public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
                TZoomPagerItem.this.mProgressBar.setVisibility(8);
                if (TZoomPagerItem.this.mProgressDrawable != null) {
                    TZoomPagerItem.this.mProgressDrawable.stop();
                }
                TZoomPagerItem.this.mErrorView.setVisibility(8);
                return false;
            }
        });
        this.mImageView.failListener(new IPhenixListener<FailPhenixEvent>() {
            public boolean onHappen(FailPhenixEvent failPhenixEvent) {
                TZoomPagerItem.this.mProgressBar.setVisibility(8);
                if (TZoomPagerItem.this.mProgressDrawable != null) {
                    TZoomPagerItem.this.mProgressDrawable.stop();
                }
                TZoomPagerItem.this.mErrorView.setVisibility(0);
                return false;
            }
        });
    }

    public TZoomImageView getImageView() {
        return this.mImageView;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.stop();
        }
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (this.mProgressDrawable == null) {
            return;
        }
        if (!isShown() || this.mProgressBar.getVisibility() != 0) {
            this.mProgressDrawable.stop();
        } else {
            this.mProgressDrawable.start();
        }
    }
}
