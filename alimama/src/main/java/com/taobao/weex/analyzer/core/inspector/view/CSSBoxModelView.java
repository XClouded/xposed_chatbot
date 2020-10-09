package com.taobao.weex.analyzer.core.inspector.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.analyzer.utils.ViewUtils;

public class CSSBoxModelView extends View {
    private static final int COLOR_BORDER = -1157833573;
    private static final int COLOR_CONTENT = -1173178687;
    private static final int COLOR_MARGIN = -1157968738;
    private static final int COLOR_PADDING = -1161572726;
    private static final int COLOR_TEXT = -1;
    private static final String TEXT_BORDER = "border";
    private static final String TEXT_MARGIN = "margin";
    private static final String TEXT_NULL = "-";
    private static final String TEXT_PADDING = "padding";
    private static final String TEXT_UNKNOWN = "?";
    private static final String TEXT_ZERO = "0";
    private final float DEFAULT_BOX_GAP = ViewUtils.dp2px(getContext(), 45);
    private final float DEFAULT_VIEW_HEIGHT = ViewUtils.dp2px(getContext(), 160);
    private final float DEFAULT_VIEW_WIDTH = ViewUtils.dp2px(getContext(), 200);
    private boolean isNative = false;
    private String mBorderBottomText;
    private RectF mBorderBounds;
    private String mBorderLeftText;
    private String mBorderRightText;
    private String mBorderTopText;
    private Rect mCachedTextBounds;
    private RectF mContentBounds;
    private String mHeightText;
    private String mMarginBottomText;
    private String mMarginLeftText;
    private String mMarginRightText;
    private String mMarginTopText;
    private RectF mOuterBounds;
    private String mPaddingBottomText;
    private RectF mPaddingBounds;
    private String mPaddingLeftText;
    private String mPaddingRightText;
    private String mPaddingTopText;
    private PathEffect mPathEffect;
    private Paint mShapePaint;
    private float mTextOffsetX;
    private float mTextOffsetY;
    private Paint mTextPaint;
    private float mViewMinHeight = this.DEFAULT_VIEW_HEIGHT;
    private float mViewMinWidth = this.DEFAULT_VIEW_WIDTH;
    private String mWidthText;

    public CSSBoxModelView(Context context) {
        super(context);
        init();
    }

    public CSSBoxModelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public CSSBoxModelView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.mShapePaint = new Paint(1);
        this.mShapePaint.setStrokeWidth(ViewUtils.dp2px(getContext(), 1));
        this.mShapePaint.setDither(true);
        this.mShapePaint.setColor(-1);
        this.mTextPaint = new Paint(1);
        this.mTextPaint.setDither(true);
        this.mTextPaint.setTextSize(ViewUtils.sp2px(getContext(), 12));
        this.mTextPaint.setColor(-1);
        this.mTextPaint.setTypeface(Typeface.DEFAULT);
        Paint.FontMetrics fontMetrics = this.mTextPaint.getFontMetrics();
        this.mTextOffsetY = (-(fontMetrics.descent + fontMetrics.ascent)) / 2.0f;
        this.mTextOffsetX = (fontMetrics.descent - fontMetrics.ascent) / 2.0f;
        this.mCachedTextBounds = new Rect();
        this.mViewMinWidth = this.DEFAULT_VIEW_WIDTH;
        this.mViewMinHeight = this.DEFAULT_VIEW_HEIGHT;
        this.mPathEffect = new DashPathEffect(new float[]{5.0f, 5.0f, 5.0f, 5.0f}, 1.0f);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        float max = Math.max((float) getWidth(), this.mViewMinWidth);
        float max2 = Math.max((float) getHeight(), this.mViewMinHeight);
        this.mOuterBounds = new RectF((float) getPaddingLeft(), (float) getPaddingTop(), ((float) getPaddingLeft()) + max, ((float) getPaddingTop()) + max2);
        this.mBorderBounds = new RectF((float) getPaddingLeft(), (float) getPaddingTop(), (((float) getPaddingLeft()) + max) - this.DEFAULT_BOX_GAP, ((float) getPaddingTop()) + ((max2 * 3.0f) / 4.0f));
        this.mPaddingBounds = new RectF((float) getPaddingLeft(), (float) getPaddingTop(), (((float) getPaddingLeft()) + max) - (this.DEFAULT_BOX_GAP * 2.0f), ((float) getPaddingTop()) + (max2 / 2.0f));
        this.mContentBounds = new RectF((float) getPaddingLeft(), (float) getPaddingTop(), (((float) getPaddingLeft()) + max) - (this.DEFAULT_BOX_GAP * 3.0f), ((float) getPaddingTop()) + (max2 / 4.0f));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(getResolvedSize((int) (this.mViewMinWidth + ((float) getPaddingLeft()) + ((float) getPaddingRight())), i), getResolvedSize((int) (this.mViewMinHeight + ((float) getPaddingTop()) + ((float) getPaddingBottom())), i2));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        this.mShapePaint.setStyle(Paint.Style.FILL);
        canvas.save();
        this.mShapePaint.setColor(COLOR_MARGIN);
        canvas.clipRect((this.mOuterBounds.width() - this.mBorderBounds.width()) / 2.0f, (this.mOuterBounds.height() - this.mBorderBounds.height()) / 2.0f, (this.mOuterBounds.width() + this.mBorderBounds.width()) / 2.0f, (this.mOuterBounds.height() + this.mBorderBounds.height()) / 2.0f, Region.Op.DIFFERENCE);
        canvas.drawRect(this.mOuterBounds, this.mShapePaint);
        canvas.restore();
        canvas.save();
        canvas.translate((this.mOuterBounds.width() - this.mBorderBounds.width()) / 2.0f, (this.mOuterBounds.height() - this.mBorderBounds.height()) / 2.0f);
        this.mShapePaint.setColor(COLOR_BORDER);
        canvas.clipRect((this.mBorderBounds.width() - this.mPaddingBounds.width()) / 2.0f, (this.mBorderBounds.height() - this.mPaddingBounds.height()) / 2.0f, (this.mBorderBounds.width() + this.mPaddingBounds.width()) / 2.0f, (this.mBorderBounds.height() + this.mPaddingBounds.height()) / 2.0f, Region.Op.DIFFERENCE);
        canvas.drawRect(this.mBorderBounds, this.mShapePaint);
        canvas.restore();
        canvas.save();
        canvas.translate((this.mOuterBounds.width() - this.mPaddingBounds.width()) / 2.0f, (this.mOuterBounds.height() - this.mPaddingBounds.height()) / 2.0f);
        this.mShapePaint.setColor(COLOR_PADDING);
        canvas.clipRect((this.mPaddingBounds.width() - this.mContentBounds.width()) / 2.0f, (this.mPaddingBounds.height() - this.mContentBounds.height()) / 2.0f, (this.mPaddingBounds.width() + this.mContentBounds.width()) / 2.0f, (this.mPaddingBounds.height() + this.mContentBounds.height()) / 2.0f, Region.Op.DIFFERENCE);
        canvas.drawRect(this.mPaddingBounds, this.mShapePaint);
        canvas.restore();
        canvas.save();
        canvas.translate((this.mOuterBounds.width() - this.mContentBounds.width()) / 2.0f, (this.mOuterBounds.height() - this.mContentBounds.height()) / 2.0f);
        this.mShapePaint.setColor(COLOR_CONTENT);
        canvas.drawRect(this.mContentBounds, this.mShapePaint);
        canvas.restore();
        this.mShapePaint.setColor(-1);
        this.mShapePaint.setStyle(Paint.Style.STROKE);
        this.mShapePaint.setPathEffect(this.mPathEffect);
        canvas.save();
        canvas.drawRect(this.mOuterBounds, this.mShapePaint);
        canvas.translate((this.mOuterBounds.width() - this.mBorderBounds.width()) / 2.0f, (this.mOuterBounds.height() - this.mBorderBounds.height()) / 2.0f);
        this.mShapePaint.setPathEffect((PathEffect) null);
        canvas.drawRect(this.mBorderBounds, this.mShapePaint);
        canvas.translate((this.mBorderBounds.width() - this.mPaddingBounds.width()) / 2.0f, (this.mBorderBounds.height() - this.mPaddingBounds.height()) / 2.0f);
        this.mShapePaint.setPathEffect(this.mPathEffect);
        canvas.drawRect(this.mPaddingBounds, this.mShapePaint);
        canvas.translate((this.mPaddingBounds.width() - this.mContentBounds.width()) / 2.0f, (this.mPaddingBounds.height() - this.mContentBounds.height()) / 2.0f);
        this.mShapePaint.setPathEffect((PathEffect) null);
        canvas.drawRect(this.mContentBounds, this.mShapePaint);
        canvas.restore();
        canvas.save();
        canvas.drawText("margin", this.mTextOffsetX + this.mOuterBounds.left, ((this.mOuterBounds.height() - this.mBorderBounds.height()) / 4.0f) + this.mTextOffsetY, this.mTextPaint);
        String prepareText = prepareText(this.mMarginLeftText, "0");
        this.mTextPaint.getTextBounds(prepareText, 0, prepareText.length(), this.mCachedTextBounds);
        canvas.drawText(prepareText, (this.mOuterBounds.left + ((this.mOuterBounds.width() - this.mBorderBounds.width()) / 4.0f)) - (((float) this.mCachedTextBounds.width()) / 2.0f), this.mOuterBounds.top + (this.mOuterBounds.height() / 2.0f) + this.mTextOffsetY, this.mTextPaint);
        String prepareText2 = prepareText(this.mMarginTopText, "0");
        this.mTextPaint.getTextBounds(prepareText2, 0, prepareText2.length(), this.mCachedTextBounds);
        canvas.drawText(prepareText2, (this.mOuterBounds.left + (this.mOuterBounds.width() / 2.0f)) - (((float) this.mCachedTextBounds.width()) / 2.0f), this.mOuterBounds.top + ((this.mOuterBounds.height() - this.mBorderBounds.height()) / 4.0f) + this.mTextOffsetY, this.mTextPaint);
        String prepareText3 = prepareText(this.mMarginRightText, "0");
        this.mTextPaint.getTextBounds(prepareText3, 0, prepareText3.length(), this.mCachedTextBounds);
        canvas.drawText(prepareText3, (this.mOuterBounds.width() - ((this.mOuterBounds.width() - this.mBorderBounds.width()) / 4.0f)) - (((float) this.mCachedTextBounds.width()) / 2.0f), this.mOuterBounds.top + (this.mOuterBounds.height() / 2.0f) + this.mTextOffsetY, this.mTextPaint);
        String prepareText4 = prepareText(this.mMarginBottomText, "0");
        this.mTextPaint.getTextBounds(prepareText4, 0, prepareText4.length(), this.mCachedTextBounds);
        canvas.drawText(prepareText4, (this.mOuterBounds.left + (this.mOuterBounds.width() / 2.0f)) - (((float) this.mCachedTextBounds.width()) / 2.0f), (this.mOuterBounds.bottom - ((this.mOuterBounds.height() - this.mBorderBounds.height()) / 4.0f)) + this.mTextOffsetY, this.mTextPaint);
        canvas.translate((this.mOuterBounds.width() - this.mBorderBounds.width()) / 2.0f, (this.mOuterBounds.height() - this.mBorderBounds.height()) / 2.0f);
        canvas.drawText(TEXT_BORDER, this.mTextOffsetX, ((this.mBorderBounds.height() - this.mPaddingBounds.height()) / 4.0f) + this.mTextOffsetY, this.mTextPaint);
        String prepareText5 = prepareText(this.mBorderLeftText, this.isNative ? "-" : "0");
        this.mTextPaint.getTextBounds(prepareText5, 0, prepareText5.length(), this.mCachedTextBounds);
        canvas.drawText(prepareText5, (this.mBorderBounds.left + ((this.mBorderBounds.width() - this.mPaddingBounds.width()) / 4.0f)) - (((float) this.mCachedTextBounds.width()) / 2.0f), this.mBorderBounds.top + (this.mBorderBounds.height() / 2.0f) + this.mTextOffsetY, this.mTextPaint);
        String prepareText6 = prepareText(this.mBorderTopText, this.isNative ? "-" : "0");
        this.mTextPaint.getTextBounds(prepareText6, 0, prepareText6.length(), this.mCachedTextBounds);
        canvas.drawText(prepareText6, (this.mBorderBounds.left + (this.mBorderBounds.width() / 2.0f)) - (((float) this.mCachedTextBounds.width()) / 2.0f), this.mBorderBounds.top + ((this.mBorderBounds.height() - this.mPaddingBounds.height()) / 4.0f) + this.mTextOffsetY, this.mTextPaint);
        String prepareText7 = prepareText(this.mBorderRightText, this.isNative ? "-" : "0");
        this.mTextPaint.getTextBounds(prepareText7, 0, prepareText7.length(), this.mCachedTextBounds);
        canvas.drawText(prepareText7, (this.mBorderBounds.width() - ((this.mBorderBounds.width() - this.mPaddingBounds.width()) / 4.0f)) - (((float) this.mCachedTextBounds.width()) / 2.0f), this.mBorderBounds.top + (this.mBorderBounds.height() / 2.0f) + this.mTextOffsetY, this.mTextPaint);
        String prepareText8 = prepareText(this.mBorderBottomText, this.isNative ? "-" : "0");
        this.mTextPaint.getTextBounds(prepareText8, 0, prepareText8.length(), this.mCachedTextBounds);
        canvas.drawText(prepareText8, (this.mBorderBounds.left + (this.mBorderBounds.width() / 2.0f)) - (((float) this.mCachedTextBounds.width()) / 2.0f), (this.mBorderBounds.bottom - ((this.mBorderBounds.height() - this.mPaddingBounds.height()) / 4.0f)) + this.mTextOffsetY, this.mTextPaint);
        canvas.translate((this.mBorderBounds.width() - this.mPaddingBounds.width()) / 2.0f, (this.mBorderBounds.height() - this.mPaddingBounds.height()) / 2.0f);
        canvas.drawText("padding", this.mTextOffsetX / 2.0f, ((this.mPaddingBounds.height() - this.mContentBounds.height()) / 4.0f) + this.mTextOffsetY, this.mTextPaint);
        String prepareText9 = prepareText(this.mPaddingLeftText, "0");
        this.mTextPaint.getTextBounds(prepareText9, 0, prepareText9.length(), this.mCachedTextBounds);
        canvas.drawText(prepareText9, (this.mPaddingBounds.left + ((this.mPaddingBounds.width() - this.mContentBounds.width()) / 4.0f)) - (((float) this.mCachedTextBounds.width()) / 2.0f), this.mPaddingBounds.top + (this.mPaddingBounds.height() / 2.0f) + this.mTextOffsetY, this.mTextPaint);
        String prepareText10 = prepareText(this.mPaddingTopText, "0");
        this.mTextPaint.getTextBounds(prepareText10, 0, prepareText10.length(), this.mCachedTextBounds);
        canvas.drawText(prepareText10, (this.mPaddingBounds.left + (this.mPaddingBounds.width() / 2.0f)) - (((float) this.mCachedTextBounds.width()) / 2.0f), this.mPaddingBounds.top + ((this.mPaddingBounds.height() - this.mContentBounds.height()) / 4.0f) + this.mTextOffsetY, this.mTextPaint);
        String prepareText11 = prepareText(this.mPaddingRightText, "0");
        this.mTextPaint.getTextBounds(prepareText11, 0, prepareText11.length(), this.mCachedTextBounds);
        canvas.drawText(prepareText11, (this.mPaddingBounds.width() - ((this.mPaddingBounds.width() - this.mContentBounds.width()) / 4.0f)) - (((float) this.mCachedTextBounds.width()) / 2.0f), this.mPaddingBounds.top + (this.mPaddingBounds.height() / 2.0f) + this.mTextOffsetY, this.mTextPaint);
        String prepareText12 = prepareText(this.mPaddingBottomText, "0");
        this.mTextPaint.getTextBounds(prepareText12, 0, prepareText12.length(), this.mCachedTextBounds);
        canvas.drawText(prepareText12, (this.mPaddingBounds.left + (this.mPaddingBounds.width() / 2.0f)) - (((float) this.mCachedTextBounds.width()) / 2.0f), (this.mPaddingBounds.bottom - ((this.mPaddingBounds.height() - this.mContentBounds.height()) / 4.0f)) + this.mTextOffsetY, this.mTextPaint);
        canvas.translate((this.mPaddingBounds.width() - this.mContentBounds.width()) / 2.0f, (this.mPaddingBounds.height() - this.mContentBounds.height()) / 2.0f);
        String str = prepareText(this.mWidthText, "?") + " x " + prepareText(this.mHeightText, "?");
        this.mTextPaint.getTextBounds(str, 0, str.length(), this.mCachedTextBounds);
        canvas.drawText(str, (this.mContentBounds.width() / 2.0f) - (((float) this.mCachedTextBounds.width()) / 2.0f), (this.mContentBounds.height() / 2.0f) + this.mTextOffsetY, this.mTextPaint);
        canvas.restore();
    }

    private String prepareText(@Nullable String str, @NonNull String str2) {
        return (TextUtils.isEmpty(str) || "0".equals(str)) ? str2 : str;
    }

    private int getResolvedSize(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i2);
        if (mode == 1073741824) {
            return size;
        }
        return mode == Integer.MIN_VALUE ? Math.min(i, size) : i;
    }

    public boolean isNative() {
        return this.isNative;
    }

    public void setNative(boolean z) {
        this.isNative = z;
    }

    public String getMarginTopText() {
        return this.mMarginTopText;
    }

    public void setMarginTopText(String str) {
        this.mMarginTopText = str;
    }

    public String getMarginLeftText() {
        return this.mMarginLeftText;
    }

    public void setMarginLeftText(String str) {
        this.mMarginLeftText = str;
    }

    public String getMarginRightText() {
        return this.mMarginRightText;
    }

    public void setMarginRightText(String str) {
        this.mMarginRightText = str;
    }

    public String getMarginBottomText() {
        return this.mMarginBottomText;
    }

    public void setMarginBottomText(String str) {
        this.mMarginBottomText = str;
    }

    public String getPaddingLeftText() {
        return this.mPaddingLeftText;
    }

    public void setPaddingLeftText(String str) {
        this.mPaddingLeftText = str;
    }

    public String getPaddingTopText() {
        return this.mPaddingTopText;
    }

    public void setPaddingTopText(String str) {
        this.mPaddingTopText = str;
    }

    public String getPaddingRightText() {
        return this.mPaddingRightText;
    }

    public void setPaddingRightText(String str) {
        this.mPaddingRightText = str;
    }

    public String getPaddingBottomText() {
        return this.mPaddingBottomText;
    }

    public void setPaddingBottomText(String str) {
        this.mPaddingBottomText = str;
    }

    public String getWidthText() {
        return this.mWidthText;
    }

    public void setWidthText(String str) {
        this.mWidthText = str;
    }

    public String getHeightText() {
        return this.mHeightText;
    }

    public void setHeightText(String str) {
        this.mHeightText = str;
    }

    public String getBorderBottomText() {
        return this.mBorderBottomText;
    }

    public void setBorderBottomText(String str) {
        this.mBorderBottomText = str;
    }

    public String getBorderRightText() {
        return this.mBorderRightText;
    }

    public void setBorderRightText(String str) {
        this.mBorderRightText = str;
    }

    public String getBorderTopText() {
        return this.mBorderTopText;
    }

    public void setBorderTopText(String str) {
        this.mBorderTopText = str;
    }

    public String getBorderLeftText() {
        return this.mBorderLeftText;
    }

    public void setBorderLeftText(String str) {
        this.mBorderLeftText = str;
    }
}
