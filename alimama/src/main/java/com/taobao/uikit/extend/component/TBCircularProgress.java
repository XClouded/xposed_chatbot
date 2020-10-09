package com.taobao.uikit.extend.component;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.material.TBCircularProgressDrawable;

public class TBCircularProgress extends LinearLayout {
    private float mAlpha;
    private Drawable mProgressBackground;
    private TBCircularProgressDrawable mProgressDrawable;
    private String mProgressText;
    private ImageView mProgressView;
    private int mRingColor;
    private int mRingSize;
    private int mRingWidth;
    private int mTextColor;
    private int mTextSize;
    private TextView mTextView;

    public TBCircularProgress(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mAlpha = 1.0f;
        this.mProgressDrawable = new TBCircularProgressDrawable(-1, 16.0f);
        this.mProgressDrawable.setCallback(this);
        View.inflate(context, R.layout.uik_circular_progress, this);
        this.mProgressView = (ImageView) findViewById(R.id.uik_circularProgress);
        this.mTextView = (TextView) findViewById(R.id.uik_progressText);
        setOrientation(1);
        initAttr(context, attributeSet, i);
        updateView();
    }

    public TBCircularProgress(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TBCircularProgress(Context context) {
        this(context, (AttributeSet) null);
    }

    private void initAttr(Context context, AttributeSet attributeSet, int i) {
        if (attributeSet != null) {
            Resources resources = context.getResources();
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.TBCircularProgress, i, 0);
            if (obtainStyledAttributes != null) {
                this.mRingColor = obtainStyledAttributes.getColor(R.styleable.TBCircularProgress_uik_ringColor, resources.getColor(R.color.uik_ringColor));
                this.mRingWidth = (int) obtainStyledAttributes.getDimension(R.styleable.TBCircularProgress_uik_ringWidth, resources.getDimension(R.dimen.uik_ringWidth));
                this.mRingSize = (int) obtainStyledAttributes.getDimension(R.styleable.TBCircularProgress_uik_ringSize, resources.getDimension(R.dimen.uik_ringSize));
                this.mProgressText = obtainStyledAttributes.getString(R.styleable.TBCircularProgress_uik_progressText);
                this.mTextSize = (int) obtainStyledAttributes.getDimension(R.styleable.TBCircularProgress_uik_progressTextSize, resources.getDimension(R.dimen.uik_progressTextSize));
                this.mTextColor = obtainStyledAttributes.getColor(R.styleable.TBCircularProgress_uik_progressTextColor, resources.getColor(R.color.uik_progressTextColor));
                this.mAlpha = obtainStyledAttributes.getFloat(R.styleable.TBCircularProgress_uik_progressAlpha, 1.0f);
                this.mProgressBackground = obtainStyledAttributes.getDrawable(R.styleable.TBCircularProgress_uik_progressBackground);
                if (this.mProgressBackground == null) {
                    this.mProgressBackground = resources.getDrawable(R.drawable.uik_shape_waitview);
                }
                obtainStyledAttributes.recycle();
                return;
            }
            return;
        }
        this.mRingColor = ContextCompat.getColor(getContext(), R.color.uik_ringColor);
        this.mRingWidth = (int) getContext().getResources().getDimension(R.dimen.uik_ringWidth);
        this.mRingSize = (int) getContext().getResources().getDimension(R.dimen.uik_ringSize);
        this.mTextSize = (int) getContext().getResources().getDimension(R.dimen.uik_progressTextSize);
        this.mTextColor = ContextCompat.getColor(getContext(), R.color.uik_progressTextColor);
        this.mAlpha = 1.0f;
        if (this.mProgressBackground == null) {
            this.mProgressBackground = ContextCompat.getDrawable(getContext(), R.drawable.uik_shape_waitview);
        }
    }

    private void updateView() {
        this.mProgressDrawable.setRingColor(this.mRingColor);
        this.mProgressDrawable.setRingWidth((float) this.mRingWidth);
        this.mProgressView.getLayoutParams().width = this.mRingSize;
        this.mProgressView.getLayoutParams().height = this.mRingSize;
        this.mProgressView.setImageDrawable(this.mProgressDrawable);
        if (this.mProgressText != null) {
            this.mTextView.setText(this.mProgressText);
        }
        this.mTextView.setTextSize(0, (float) this.mTextSize);
        this.mTextView.setTextColor(this.mTextColor);
        setBackgroundDrawable(this.mProgressBackground);
        setAlpha(this.mAlpha);
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mProgressDrawable.setBounds(0, 0, i, i2);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.start();
        }
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
        if (i == 8 || i == 4) {
            this.mProgressDrawable.stop();
        } else {
            this.mProgressDrawable.start();
        }
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable drawable) {
        return drawable == this.mProgressDrawable || super.verifyDrawable(drawable);
    }

    public void setRingColor(int i) {
        this.mRingColor = i;
        updateView();
    }

    public void setRingWidth(int i) {
        this.mRingWidth = i;
        updateView();
    }

    public void setRingSize(int i) {
        this.mRingSize = i;
        updateView();
    }

    public void setProgressText(String str) {
        this.mProgressText = str;
        updateView();
    }

    public void setTextSize(int i) {
        this.mTextSize = i;
        updateView();
    }

    public void setTextColor(int i) {
        this.mTextColor = i;
        updateView();
    }
}
