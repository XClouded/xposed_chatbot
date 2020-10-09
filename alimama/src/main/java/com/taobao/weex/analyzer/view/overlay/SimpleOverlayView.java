package com.taobao.weex.analyzer.view.overlay;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.analyzer.utils.ViewUtils;

public class SimpleOverlayView extends DragSupportOverlayView {
    /* access modifiers changed from: private */
    public int mBackgroundColor;
    /* access modifiers changed from: private */
    public OnClickListener mOnClickListener;
    /* access modifiers changed from: private */
    public int mTextColor;
    private String mTitle;

    public interface OnClickListener {
        void onClick(@NonNull IOverlayView iOverlayView);
    }

    /* access modifiers changed from: protected */
    public void onDismiss() {
    }

    /* access modifiers changed from: protected */
    public void onShown() {
    }

    private SimpleOverlayView(Context context, @NonNull String str) {
        super(context);
        this.mBackgroundColor = Color.parseColor("#ba000000");
        this.mTextColor = -1;
        this.mTitle = str;
        this.mWidth = (int) ViewUtils.dp2px(this.mContext, 40);
        this.mHeight = (int) ViewUtils.dp2px(this.mContext, 25);
    }

    /* access modifiers changed from: package-private */
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public View onCreateView() {
        TextView textView = new TextView(this.mContext);
        textView.setTextColor(this.mTextColor);
        textView.setBackgroundColor(this.mBackgroundColor);
        textView.setGravity(17);
        textView.setText(this.mTitle);
        if (this.mOnClickListener != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    SimpleOverlayView.this.mOnClickListener.onClick(SimpleOverlayView.this);
                }
            });
        }
        return textView;
    }

    public static class Builder {
        int backgroundColor = Color.parseColor("#ba000000");
        Context context;
        boolean enableDrag = true;
        int gravity;
        int height;
        OnClickListener listener;
        int textColor = -1;
        String title;
        int width;
        int x;
        int y;

        public Builder(@NonNull Context context2, @NonNull String str) {
            this.context = context2;
            this.title = str;
        }

        public Builder backgroundColor(int i) {
            this.backgroundColor = i;
            return this;
        }

        public Builder textColor(int i) {
            this.textColor = i;
            return this;
        }

        public Builder x(int i) {
            this.x = i;
            return this;
        }

        public Builder y(int i) {
            this.y = i;
            return this;
        }

        public Builder width(int i) {
            this.width = i;
            return this;
        }

        public Builder height(int i) {
            this.height = i;
            return this;
        }

        public Builder gravity(int i) {
            this.gravity = i;
            return this;
        }

        public Builder listener(OnClickListener onClickListener) {
            this.listener = onClickListener;
            return this;
        }

        public Builder enableDrag(boolean z) {
            this.enableDrag = z;
            return this;
        }

        public SimpleOverlayView build() {
            SimpleOverlayView simpleOverlayView = new SimpleOverlayView(this.context, this.title);
            if (this.listener != null) {
                simpleOverlayView.setOnClickListener(this.listener);
            }
            if (this.gravity != 0) {
                simpleOverlayView.mGravity = this.gravity;
            }
            if (this.x > 0) {
                simpleOverlayView.mX = this.x;
            }
            if (this.y > 0) {
                simpleOverlayView.mY = this.y;
            }
            if (this.width > 0) {
                simpleOverlayView.mWidth = this.width;
            }
            if (this.height > 0) {
                simpleOverlayView.mHeight = this.height;
            }
            int unused = simpleOverlayView.mBackgroundColor = this.backgroundColor;
            int unused2 = simpleOverlayView.mTextColor = this.textColor;
            simpleOverlayView.setDragEnabled(this.enableDrag);
            return simpleOverlayView;
        }
    }
}
