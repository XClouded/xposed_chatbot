package com.taobao.android.dxcontainer.loadmore;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.taobao.android.dxcontainer.R;

public class DXContainerDefaultLoadMoreView extends LinearLayout implements IDXContainerLoadMoreView {
    private ImageView imageView;
    private TextView textView;

    public DXContainerDefaultLoadMoreView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(0);
        setGravity(17);
        this.imageView = new ImageView(context);
        this.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.imageView.setBackgroundResource(R.drawable.icon_dxc_default_loadmore);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(32, 32);
        layoutParams.setMargins(0, 0, 12, 0);
        addView(this.imageView, layoutParams);
        this.textView = new TextView(context);
        this.textView.setGravity(17);
        this.textView.setTextSize(13.0f);
        this.textView.setTextColor(Color.parseColor("#A5A5A5"));
        addView(this.textView, -2, -1);
    }

    public void setViewState(String str, int i) {
        this.textView.setText(str);
        if (i == 2) {
            this.imageView.setVisibility(8);
        } else {
            this.imageView.setVisibility(0);
        }
    }
}
