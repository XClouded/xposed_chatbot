package com.ali.user.mobile.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.ui.R;

public class AUBladeView extends View {
    private String[] b = {"★", "A", "B", "C", "D", "E", ApiConstants.UTConstants.UT_SUCCESS_F, "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", ApiConstants.UTConstants.UT_SUCCESS_T, "U", "V", "W", "X", "Y", "Z"};
    private int choose = -1;
    Runnable dismissRunnable = new Runnable() {
        public void run() {
            if (AUBladeView.this.mPopupWindow != null) {
                try {
                    AUBladeView.this.mPopupWindow.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private Handler handler = new Handler();
    private OnItemClickListener mOnItemClickListener;
    private TextView mPopupText;
    /* access modifiers changed from: private */
    public PopupWindow mPopupWindow;
    private Paint paint = new Paint();
    private boolean showBkg = false;

    public interface OnItemClickListener {
        void onItemClick(String str);
    }

    public AUBladeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initPop(context);
    }

    public AUBladeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initPop(context);
    }

    public AUBladeView(Context context) {
        super(context);
        initPop(context);
    }

    private void initPop(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.aliuser_letter_popupwindow, (ViewGroup) null);
        this.mPopupWindow = new PopupWindow(inflate, -2, -2);
        this.mPopupText = (TextView) inflate.findViewById(R.id.letterText);
        this.mPopupWindow.setFocusable(true);
        this.mPopupWindow.setOutsideTouchable(true);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.showBkg) {
            canvas.drawColor(Color.parseColor("#00000000"));
        }
        int height = getHeight();
        int width = getWidth();
        int length = height / this.b.length;
        for (int i = 0; i < this.b.length; i++) {
            this.paint.setColor(Color.parseColor("#999999"));
            this.paint.setAntiAlias(true);
            if (height < 400) {
                this.paint.setTextSize(getResources().getDimension(R.dimen.letters_item_little_fontsize));
            } else {
                this.paint.setTextSize(getResources().getDimension(R.dimen.letters_item_fontsize));
            }
            if (i == this.choose) {
                this.paint.setColor(Color.parseColor("#3399ff"));
            }
            canvas.drawText(this.b[i], ((float) (width / 2)) - (this.paint.measureText(this.b[i]) / 2.0f), (float) ((length * i) + length), this.paint);
            this.paint.reset();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float y = motionEvent.getY();
        int i = this.choose;
        int height = (int) ((y / ((float) ((getHeight() / this.b.length) * this.b.length))) * ((float) this.b.length));
        if (height >= 0 && height < this.b.length) {
            this.mPopupWindow.showAtLocation(this, 17, 0, 0);
            this.mPopupText.setText(this.b[height]);
        }
        switch (action) {
            case 0:
                this.showBkg = true;
                if (i != height && height >= 0 && height < this.b.length) {
                    performItemClicked(height);
                    this.choose = height;
                    invalidate();
                    break;
                }
            case 1:
                this.showBkg = false;
                this.choose = -1;
                dismissPopup();
                invalidate();
                break;
            case 2:
                if (i != height && height >= 0 && height < this.b.length) {
                    performItemClicked(height);
                    this.choose = height;
                    invalidate();
                    break;
                }
        }
        return true;
    }

    private void dismissPopup() {
        this.handler.postDelayed(this.dismissRunnable, 800);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private void performItemClicked(int i) {
        if (this.mOnItemClickListener != null) {
            this.mOnItemClickListener.onItemClick(this.b[i]);
        }
    }
}
