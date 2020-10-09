package com.alimama.moon.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.alimama.moon.R;

public class CommonLoadingView extends LinearLayout {
    private ImageView mLoadingView;

    public CommonLoadingView(Context context) {
        super(context);
        init(context);
    }

    public CommonLoadingView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public CommonLoadingView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_loading, this);
        this.mLoadingView = (ImageView) findViewById(R.id.is_status_loading_image_view);
        setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
    }

    public void show() {
        setVisibility(0);
        if (this.mLoadingView != null) {
            this.mLoadingView.clearAnimation();
        }
        Animation loadAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        if (loadAnimation != null) {
            this.mLoadingView.startAnimation(loadAnimation);
        }
    }

    public void dismiss() {
        if (this.mLoadingView != null) {
            this.mLoadingView.clearAnimation();
        }
        setVisibility(8);
    }
}
