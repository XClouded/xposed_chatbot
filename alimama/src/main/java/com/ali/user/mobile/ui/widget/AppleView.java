package com.ali.user.mobile.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import anet.channel.util.ErrorConstant;
import com.ali.user.mobile.ui.R;

public class AppleView extends View {
    public static final int STATUS_END = 2;
    public static final int STATUS_FINISH = 1;
    public static final int STATUS_INIT = -1;
    public static final int STATUS_START = 0;
    private Bitmap bitmap1 = null;
    private Bitmap bitmap2 = null;
    private Bitmap bitmap3 = null;
    private Bitmap bitmap4 = null;
    private float currentX = 40.0f;
    private float currentY = 50.0f;
    private float initX = 40.0f;
    private float initY = 50.0f;
    private int mScreenHeight = 0;
    private int mScreenWidth = 0;
    Paint paint = null;
    private float radius = 100.0f;
    private float radiusTouch = 120.0f;
    private int status = -1;

    public AppleView(Context context) {
        super(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        this.mScreenWidth = displayMetrics.widthPixels;
        this.mScreenHeight = displayMetrics.heightPixels;
        this.paint = new Paint();
        this.bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.aliuser_ball1);
        this.bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.aliuser_ball3);
        this.bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.aliuser_ball3);
        this.bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.aliuser_ball1);
        this.radius = (float) (this.bitmap1.getWidth() / 2);
        this.radiusTouch = (float) (this.bitmap2.getWidth() / 2);
        initPostion(this.radius, (float) (this.mScreenHeight + ErrorConstant.ERROR_TNET_EXCEPTION));
    }

    public float getInitLeft() {
        return this.initX;
    }

    public float getInitRight() {
        return this.initX + (this.radius * 2.0f);
    }

    public float getInitTop() {
        return this.initY;
    }

    public float getInitBottom() {
        return this.initY + (this.radius * 2.0f);
    }

    public void initPostion(float f, float f2) {
        setStatus(-1);
        this.initX = f;
        this.currentX = f;
        this.initY = f2;
        this.currentY = f2;
    }

    public void resetPostion() {
        setStatus(-1);
        this.currentX = this.initX;
        this.currentY = this.initY;
    }

    public void setPosition(float f, float f2) {
        setStatus(0);
        this.currentX = f;
        this.currentY = f2;
    }

    public float getRadius() {
        return this.radius;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (this.status) {
            case -1:
                canvas.drawBitmap(this.bitmap1, this.initX, this.initY, this.paint);
                return;
            case 0:
                canvas.drawBitmap(this.bitmap2, this.currentX - ((float) (this.bitmap2.getWidth() / 2)), this.currentY - ((float) (this.bitmap2.getHeight() / 2)), this.paint);
                return;
            case 1:
                canvas.drawBitmap(this.bitmap3, this.currentX - ((float) (this.bitmap3.getWidth() / 2)), this.currentY - ((float) (this.bitmap3.getHeight() / 2)), this.paint);
                return;
            case 2:
                canvas.drawBitmap(this.bitmap4, this.currentX - ((float) (this.bitmap4.getWidth() / 2)), this.currentY - ((float) (this.bitmap4.getHeight() / 2)), this.paint);
                return;
            default:
                return;
        }
    }

    public void setPositionFinish(float f, float f2) {
        setStatus(1);
        this.currentX = f;
        this.currentY = f2;
    }

    public void setPositionEnd(float f, float f2) {
        setStatus(2);
        this.currentX = f;
        this.currentY = f2;
    }

    public float getRadiusTouch() {
        return this.radiusTouch;
    }
}
