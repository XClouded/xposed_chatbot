package androidx.appcompat.smartbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.TypedValue;

@Deprecated
public class TextDrawable extends Drawable {
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int[] appearanceAttributes = {16842901, 16842902, 16842903, 16842904};
    private static final int[] themeAttributes = {16842804};
    private Resources mResources;
    private CharSequence mText = "";
    private Layout.Alignment mTextAlignment = Layout.Alignment.ALIGN_NORMAL;
    private Rect mTextBounds;
    private ColorStateList mTextColors;
    private StaticLayout mTextLayout;
    private TextPaint mTextPaint;
    private Path mTextPath;

    public TextDrawable(Context context) {
        int i;
        int i2;
        ColorStateList colorStateList;
        this.mResources = context.getResources();
        this.mTextBounds = new Rect();
        this.mTextPaint = new TextPaint(1);
        this.mTextPaint.density = this.mResources.getDisplayMetrics().density;
        this.mTextPaint.setDither(true);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(themeAttributes);
        int resourceId = obtainStyledAttributes.getResourceId(0, -1);
        obtainStyledAttributes.recycle();
        Typeface typeface = null;
        TypedArray obtainStyledAttributes2 = resourceId != -1 ? context.obtainStyledAttributes(resourceId, appearanceAttributes) : null;
        int i3 = 15;
        if (obtainStyledAttributes2 != null) {
            colorStateList = null;
            i2 = -1;
            i = -1;
            for (int i4 = 0; i4 < obtainStyledAttributes2.getIndexCount(); i4++) {
                int index = obtainStyledAttributes2.getIndex(i4);
                switch (index) {
                    case 0:
                        try {
                            i3 = obtainStyledAttributes2.getDimensionPixelSize(index, i3);
                            break;
                        } catch (Throwable th) {
                            th.printStackTrace();
                            break;
                        }
                    case 1:
                        i2 = obtainStyledAttributes2.getInt(index, i2);
                        break;
                    case 2:
                        i = obtainStyledAttributes2.getInt(index, i);
                        break;
                    case 3:
                        colorStateList = obtainStyledAttributes2.getColorStateList(index);
                        break;
                }
            }
            obtainStyledAttributes2.recycle();
        } else {
            colorStateList = null;
            i2 = -1;
            i = -1;
        }
        setTextColor(colorStateList == null ? ColorStateList.valueOf(-16777216) : colorStateList);
        setRawTextSize((float) i3);
        switch (i2) {
            case 1:
                typeface = Typeface.SANS_SERIF;
                break;
            case 2:
                typeface = Typeface.SERIF;
                break;
            case 3:
                typeface = Typeface.MONOSPACE;
                break;
        }
        setTypeface(typeface, i);
    }

    public void setText(CharSequence charSequence) {
        if (charSequence == null) {
            charSequence = "";
        }
        this.mText = charSequence;
        measureContent();
    }

    public CharSequence getText() {
        return this.mText;
    }

    public float getTextSize() {
        return this.mTextPaint.getTextSize();
    }

    public void setTextSize(float f) {
        setTextSize(2, f);
    }

    public void setTextSize(int i, float f) {
        setRawTextSize(TypedValue.applyDimension(i, f, this.mResources.getDisplayMetrics()));
    }

    private void setRawTextSize(float f) {
        if (f != this.mTextPaint.getTextSize()) {
            this.mTextPaint.setTextSize(f);
            measureContent();
        }
    }

    public float getTextScaleX() {
        return this.mTextPaint.getTextScaleX();
    }

    public void setTextScaleX(float f) {
        if (f != this.mTextPaint.getTextScaleX()) {
            this.mTextPaint.setTextScaleX(f);
            measureContent();
        }
    }

    public Layout.Alignment getTextAlign() {
        return this.mTextAlignment;
    }

    public void setTextAlign(Layout.Alignment alignment) {
        if (this.mTextAlignment != alignment) {
            this.mTextAlignment = alignment;
            measureContent();
        }
    }

    public void setTypeface(Typeface typeface) {
        if (this.mTextPaint.getTypeface() != typeface) {
            this.mTextPaint.setTypeface(typeface);
            measureContent();
        }
    }

    public void setTypeface(Typeface typeface, int i) {
        Typeface typeface2;
        float f = 0.0f;
        boolean z = false;
        if (i > 0) {
            if (typeface == null) {
                typeface2 = Typeface.defaultFromStyle(i);
            } else {
                typeface2 = Typeface.create(typeface, i);
            }
            setTypeface(typeface2);
            int style = ((typeface2 != null ? typeface2.getStyle() : 0) ^ -1) & i;
            TextPaint textPaint = this.mTextPaint;
            if ((style & 1) != 0) {
                z = true;
            }
            textPaint.setFakeBoldText(z);
            TextPaint textPaint2 = this.mTextPaint;
            if ((style & 2) != 0) {
                f = -0.25f;
            }
            textPaint2.setTextSkewX(f);
            return;
        }
        this.mTextPaint.setFakeBoldText(false);
        this.mTextPaint.setTextSkewX(0.0f);
        setTypeface(typeface);
    }

    public Typeface getTypeface() {
        return this.mTextPaint.getTypeface();
    }

    public void setTextColor(int i) {
        setTextColor(ColorStateList.valueOf(i));
    }

    public void setTextColor(ColorStateList colorStateList) {
        this.mTextColors = colorStateList;
        updateTextColors(getState());
    }

    public void setTextPath(Path path) {
        if (this.mTextPath != path) {
            this.mTextPath = path;
            measureContent();
        }
    }

    private void measureContent() {
        if (this.mTextPath != null) {
            this.mTextLayout = null;
            this.mTextBounds.setEmpty();
        } else {
            this.mTextLayout = new StaticLayout(this.mText, this.mTextPaint, (int) Layout.getDesiredWidth(this.mText, this.mTextPaint), this.mTextAlignment, 1.0f, 0.0f, false);
            this.mTextBounds.set(0, 0, this.mTextLayout.getWidth(), this.mTextLayout.getHeight());
        }
        invalidateSelf();
    }

    private boolean updateTextColors(int[] iArr) {
        int colorForState = this.mTextColors.getColorForState(iArr, -1);
        if (this.mTextPaint.getColor() == colorForState) {
            return false;
        }
        this.mTextPaint.setColor(colorForState);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        this.mTextBounds.set(rect);
    }

    public boolean isStateful() {
        return this.mTextColors.isStateful();
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] iArr) {
        return updateTextColors(iArr);
    }

    public int getIntrinsicHeight() {
        if (this.mTextBounds.isEmpty()) {
            return -1;
        }
        return this.mTextBounds.bottom - this.mTextBounds.top;
    }

    public int getIntrinsicWidth() {
        if (this.mTextBounds.isEmpty()) {
            return -1;
        }
        return this.mTextBounds.right - this.mTextBounds.left;
    }

    public void draw(Canvas canvas) {
        if (this.mTextPath == null) {
            this.mTextLayout.draw(canvas);
            return;
        }
        canvas.drawTextOnPath(this.mText.toString(), this.mTextPath, 0.0f, 0.0f, this.mTextPaint);
    }

    public void setAlpha(int i) {
        if (this.mTextPaint.getAlpha() != i) {
            this.mTextPaint.setAlpha(i);
        }
    }

    public int getOpacity() {
        return this.mTextPaint.getAlpha();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mTextPaint.getColorFilter() != colorFilter) {
            this.mTextPaint.setColorFilter(colorFilter);
        }
    }
}
