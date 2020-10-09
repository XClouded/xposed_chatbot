package com.ali.user.mobile.register.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import com.ali.user.mobile.ui.R;

public class AliUserSmsCodeView extends View {
    private boolean autoHideKeyboard = true;
    private float mDividerWidth = 6.0f;
    private int mHeight;
    private int mNextUnderLineColor = -16777216;
    private OnCompletedListener mOnCompletedListener;
    private OnTextChangedListener mOnTextChangedListener;
    private RectF[] mOuterRects;
    private StringBuilder mTextBuilder;
    private int mTextColor = -16777216;
    private int mTextCount = 4;
    private Paint mTextPaint;
    private PointF[] mTextPositions;
    private RectF[] mTextRects;
    private float mTextSize = 36.0f;
    private int mUnderLineColor = -16777216;
    private Paint mUnderLinePaint;
    private float mUnderLineStrokeWidth = 1.0f;
    private int mWidth;

    public interface OnCompletedListener {
        void onCompleted(String str);
    }

    public interface OnTextChangedListener {
        void onTextChanged(String str);
    }

    public AliUserSmsCodeView(Context context) {
        super(context);
        init(context, (AttributeSet) null);
    }

    public AliUserSmsCodeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public AliUserSmsCodeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.AliUserSmsCodeView);
            this.mTextCount = obtainStyledAttributes.getInt(R.styleable.AliUserSmsCodeView_scTextCount, this.mTextCount);
            this.mTextColor = obtainStyledAttributes.getColor(R.styleable.AliUserSmsCodeView_scTextColor, this.mTextColor);
            this.mTextSize = obtainStyledAttributes.getDimension(R.styleable.AliUserSmsCodeView_scTextSize, TypedValue.applyDimension(2, this.mTextSize, context.getResources().getDisplayMetrics()));
            this.mDividerWidth = obtainStyledAttributes.getDimension(R.styleable.AliUserSmsCodeView_scDividerWidth, TypedValue.applyDimension(1, this.mDividerWidth, context.getResources().getDisplayMetrics()));
            this.mUnderLineStrokeWidth = obtainStyledAttributes.getDimension(R.styleable.AliUserSmsCodeView_scUnderLineStrokeWidth, TypedValue.applyDimension(1, this.mUnderLineStrokeWidth, context.getResources().getDisplayMetrics()));
            this.mUnderLineColor = obtainStyledAttributes.getColor(R.styleable.AliUserSmsCodeView_scUnderLineColor, this.mUnderLineColor);
            this.mNextUnderLineColor = obtainStyledAttributes.getColor(R.styleable.AliUserSmsCodeView_scNextUnderLineColor, this.mNextUnderLineColor);
            obtainStyledAttributes.recycle();
        }
        this.mTextBuilder = new StringBuilder(this.mTextCount);
        this.mTextPositions = new PointF[this.mTextCount];
        this.mOuterRects = new RectF[this.mTextCount];
        this.mTextRects = new RectF[this.mTextCount];
        initPaint();
        setFocusableInTouchMode(true);
    }

    private void initPaint() {
        if (this.mTextPaint == null) {
            this.mTextPaint = new Paint(1);
        }
        this.mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setTextSize(this.mTextSize);
        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
        this.mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        if (this.mUnderLinePaint == null) {
            this.mUnderLinePaint = new Paint(1);
        }
        this.mUnderLinePaint.setStyle(Paint.Style.STROKE);
        this.mUnderLinePaint.setColor(this.mUnderLineColor);
        this.mUnderLinePaint.setStrokeWidth(this.mUnderLineStrokeWidth);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mWidth = View.MeasureSpec.getSize(i);
        this.mHeight = View.MeasureSpec.getSize(i2);
        measureChildPositions();
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    private void measureChildPositions() {
        Paint.FontMetricsInt fontMetricsInt = this.mTextPaint.getFontMetricsInt();
        float measureText = this.mTextPaint.measureText("0");
        float f = (((float) (this.mHeight / 2)) + (((float) (fontMetricsInt.bottom - fontMetricsInt.top)) / 2.0f)) - ((float) fontMetricsInt.bottom);
        float f2 = (((float) this.mWidth) - (((float) (this.mTextCount - 1)) * this.mDividerWidth)) / ((float) this.mTextCount);
        int i = 0;
        while (i < this.mTextCount) {
            float f3 = (float) i;
            float f4 = f3 * f2;
            this.mTextPositions[i] = new PointF((this.mDividerWidth * f3) + f4 + (f2 / 2.0f), f);
            int i2 = i + 1;
            this.mOuterRects[i] = new RectF(f4 + (this.mDividerWidth * f3), 0.0f, (((float) i2) * f2) + (f3 * this.mDividerWidth), (float) this.mHeight);
            float f5 = measureText / 2.0f;
            this.mTextRects[i] = new RectF(this.mTextPositions[i].x - f5, this.mTextPositions[i].y + ((float) fontMetricsInt.top), this.mTextPositions[i].x + f5, this.mTextPositions[i].y + ((float) fontMetricsInt.bottom));
            i = i2;
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        int length = this.mTextBuilder.length();
        for (int i = 0; i < this.mTextCount; i++) {
            if (i < length) {
                canvas.drawText(this.mTextBuilder.toString(), i, i + 1, this.mTextPositions[i].x, this.mTextPositions[i].y, this.mTextPaint);
            }
            int i2 = this.mUnderLineColor;
            if (hasFocus() && i == length) {
                i2 = this.mNextUnderLineColor;
            } else if (i < length) {
                i2 = this.mNextUnderLineColor;
            }
            this.mUnderLinePaint.setColor(i2);
            drawUnderLine(canvas, this.mUnderLinePaint, this.mOuterRects[i], this.mTextRects[i]);
            setBackgroundColor(-1);
        }
    }

    public void drawUnderLine(Canvas canvas, Paint paint, RectF rectF, RectF rectF2) {
        canvas.drawLine(rectF2.left - (rectF2.width() / 2.0f), rectF.bottom, rectF2.right + (rectF2.width() / 2.0f), rectF.bottom, paint);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        if (motionEvent.getAction() != 0) {
            return true;
        }
        focus();
        return true;
    }

    public void focus() {
        requestFocus();
        postDelayed(new Runnable() {
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) AliUserSmsCodeView.this.getContext().getSystemService("input_method");
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(AliUserSmsCodeView.this, 2);
                }
            }
        }, 100);
    }

    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        AnonymousClass2 r0 = new BaseInputConnection(this, false) {
            public boolean deleteSurroundingText(int i, int i2) {
                return sendKeyEvent(new KeyEvent(0, 67)) && sendKeyEvent(new KeyEvent(1, 67));
            }
        };
        editorInfo.actionLabel = null;
        editorInfo.inputType = 3;
        editorInfo.imeOptions = 5;
        return r0;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 67 && this.mTextBuilder.length() > 0) {
            this.mTextBuilder.deleteCharAt(this.mTextBuilder.length() - 1);
            if (this.mOnTextChangedListener != null) {
                this.mOnTextChangedListener.onTextChanged(this.mTextBuilder.toString());
            }
            invalidate();
        } else if (i >= 7 && i <= 16 && this.mTextBuilder.length() < this.mTextCount) {
            this.mTextBuilder.append(keyEvent.getDisplayLabel());
            if (this.mOnTextChangedListener != null) {
                this.mOnTextChangedListener.onTextChanged(this.mTextBuilder.toString());
            }
            invalidate();
        }
        if (this.mTextBuilder.length() >= this.mTextCount && this.autoHideKeyboard) {
            if (this.mOnCompletedListener != null) {
                this.mOnCompletedListener.onCompleted(this.mTextBuilder.toString());
            }
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    public String getText() {
        return this.mTextBuilder != null ? this.mTextBuilder.toString() : "";
    }

    public void setTextCount(int i) {
        if (this.mTextCount != i) {
            this.mTextCount = i;
            invalidate();
        }
    }

    public int getTextCount() {
        return this.mTextCount;
    }

    public void clearText() {
        if (this.mTextBuilder.length() != 0) {
            this.mTextBuilder.delete(0, this.mTextBuilder.length());
            if (this.mOnTextChangedListener != null) {
                this.mOnTextChangedListener.onTextChanged(this.mTextBuilder.toString());
            }
            invalidate();
        }
    }

    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.mOnTextChangedListener = onTextChangedListener;
    }

    public void setOnCompletedListener(OnCompletedListener onCompletedListener) {
        this.mOnCompletedListener = onCompletedListener;
    }
}
