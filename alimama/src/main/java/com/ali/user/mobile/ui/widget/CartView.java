package com.ali.user.mobile.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import com.ali.user.mobile.ui.R;

public class CartView extends View {
    public static final int STATUS_END = 2;
    public static final int STATUS_FINISH = 1;
    public static final int STATUS_START = 0;
    private Bitmap bitmap1 = null;
    private Bitmap bitmap2 = null;
    private Bitmap bitmap3 = null;
    private float centerX = 0.0f;
    private float centerX1 = 0.0f;
    private float centerY = 0.0f;
    private float centerY1 = 0.0f;
    private float currentX = 40.0f;
    private float currentY = 50.0f;
    private int mScreenHeight = 0;
    private int mScreenWidth = 0;
    private Paint paint = null;
    private float radius = 120.0f;
    private int status = 0;

    public CartView(Context context) {
        super(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        this.mScreenWidth = displayMetrics.widthPixels;
        this.mScreenHeight = displayMetrics.heightPixels;
        this.bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.aliuser_verification_frame);
        this.bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.aliuser_verification_frame2);
        this.bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.aliuser_verification_frame2);
        this.radius = (float) (this.bitmap1.getWidth() / 2);
        this.paint = new Paint();
        initPostion(((float) this.mScreenWidth) - this.radius, this.radius);
    }

    public void initPostion(float f, float f2) {
        setStatus(0);
        this.currentX = f;
        this.currentY = f2;
        this.centerX = ((float) (this.bitmap2.getWidth() / 2)) + this.currentX;
        this.centerY = ((float) (this.bitmap2.getHeight() / 2)) + this.currentY;
        this.centerX1 = ((float) (this.bitmap3.getWidth() / 2)) + this.currentX;
        this.centerY1 = ((float) (this.bitmap3.getHeight() / 2)) + this.currentY;
    }

    public float getCenterX() {
        return this.centerX;
    }

    public float getCenterY() {
        return this.centerY;
    }

    public float getCenterX1() {
        return this.centerX1;
    }

    public float getCenterY1() {
        return this.centerY1;
    }

    public float getInitLeft() {
        return this.currentX;
    }

    public float getInitRight() {
        return this.currentX + (this.radius * 2.0f);
    }

    public float getInitTop() {
        return this.currentY;
    }

    public float getInitBottom() {
        return this.currentY + (this.radius * 2.0f);
    }

    public int getCartWidth() {
        return this.bitmap1.getWidth();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (this.status) {
            case 0:
                canvas.drawBitmap(this.bitmap1, this.currentX, this.currentY, this.paint);
                return;
            case 1:
                canvas.drawBitmap(this.bitmap2, this.currentX, this.currentY, this.paint);
                return;
            case 2:
                canvas.drawBitmap(this.bitmap3, this.currentX, this.currentY, this.paint);
                return;
            default:
                return;
        }
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }
}
