package com.alibaba.aliweex.adapter.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.aliweex.R;

public class ElevatorText extends LinearLayout {
    private ImageView imageView;
    private String normalColor;
    private String selectedColor;
    private TextView textView;

    public ElevatorText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        LayoutInflater.from(context).inflate(R.layout.huichang_tbelevatortext_layout, this, true);
        this.imageView = (ImageView) findViewById(R.id.loc_icon);
        this.textView = (TextView) findViewById(R.id.loc_text);
    }

    public ElevatorText(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ElevatorText(Context context) {
        this(context, (AttributeSet) null);
    }

    public void setText(String str) {
        this.textView.setText(str);
    }

    public void show() {
        this.imageView.setVisibility(0);
        this.textView.setTextColor(Color.parseColor(this.selectedColor));
    }

    public void hide() {
        this.imageView.setVisibility(4);
        this.textView.setTextColor(Color.parseColor(this.normalColor));
    }

    public void setSelectedColor(String str) {
        this.selectedColor = str;
    }

    public void setNormalColor(String str) {
        this.normalColor = str;
    }
}
