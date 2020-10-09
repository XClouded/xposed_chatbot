package com.taobao.weex.layout.measurefunc;

import android.graphics.Canvas;
import android.os.Build;
import android.os.Looper;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.WorkerThread;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.dom.TextDecorationSpan;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.dom.WXCustomStyleSpan;
import com.taobao.weex.dom.WXLineHeightSpan;
import com.taobao.weex.dom.WXStyle;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.layout.MeasureMode;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXTextDecoration;
import com.taobao.weex.utils.WXDomUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import com.taobao.weex.utils.WXUtils;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class TextContentBoxMeasurement extends ContentBoxMeasurement {
    private static final Canvas DUMMY_CANVAS = new Canvas();
    private static final String ELLIPSIS = "…";
    /* access modifiers changed from: private */
    public AtomicReference<Layout> atomicReference = new AtomicReference<>();
    private boolean hasBeenMeasured = false;
    @Nullable
    private Layout layout;
    private Layout.Alignment mAlignment;
    private int mColor;
    private String mFontFamily = null;
    private int mFontSize = -1;
    private int mFontStyle = -1;
    private int mFontWeight = -1;
    private boolean mIsColorSet = false;
    private int mLineHeight = -1;
    private int mNumberOfLines = -1;
    private String mText = null;
    private WXTextDecoration mTextDecoration = WXTextDecoration.NONE;
    private TextPaint mTextPaint;
    private float previousWidth = Float.NaN;
    @Nullable
    private Spanned spanned;
    private TextUtils.TruncateAt textOverflow;

    public TextContentBoxMeasurement(WXComponent wXComponent) {
        super(wXComponent);
    }

    class SetSpanOperation {
        protected final int end;
        protected final int flag;
        protected final int start;
        protected final Object what;

        SetSpanOperation(TextContentBoxMeasurement textContentBoxMeasurement, int i, int i2, Object obj) {
            this(i, i2, obj, 17);
        }

        SetSpanOperation(int i, int i2, Object obj, int i3) {
            this.start = i;
            this.end = i2;
            this.what = obj;
            this.flag = i3;
        }

        public void execute(Spannable spannable) {
            spannable.setSpan(this.what, this.start, this.end, this.flag);
        }
    }

    public void layoutBefore() {
        this.mTextPaint = new TextPaint(1);
        this.hasBeenMeasured = false;
        updateStyleAndText();
        this.spanned = createSpanned(this.mText);
    }

    public void measureInternal(float f, float f2, int i, int i2) {
        boolean z = true;
        this.hasBeenMeasured = true;
        TextPaint textPaint = this.mTextPaint;
        if (i != MeasureMode.EXACTLY) {
            z = false;
        }
        float textWidth = getTextWidth(textPaint, f, z);
        if (textWidth <= 0.0f || this.spanned == null) {
            if (i == MeasureMode.UNSPECIFIED) {
                f = 0.0f;
            }
            if (i2 == MeasureMode.UNSPECIFIED) {
                f2 = 0.0f;
            }
        } else {
            this.layout = createLayout(textWidth, (Layout) null);
            this.previousWidth = (float) this.layout.getWidth();
            if (Float.isNaN(f)) {
                r4 = (float) this.layout.getWidth();
            } else {
                r4 = Math.min((float) this.layout.getWidth(), f);
            }
            if (Float.isNaN(f2)) {
                f2 = (float) this.layout.getHeight();
            }
        }
        this.mMeasureWidth = r4;
        this.mMeasureHeight = f2;
    }

    public void layoutAfter(float f, float f2) {
        if (this.mComponent != null) {
            if (!this.hasBeenMeasured) {
                updateStyleAndText();
                recalculateLayout(f);
            } else if (!(this.layout == null || WXDomUtils.getContentWidth(this.mComponent.getPadding(), this.mComponent.getBorder(), f) == this.previousWidth)) {
                recalculateLayout(f);
            }
            this.hasBeenMeasured = false;
            if (this.layout != null && !this.layout.equals(this.atomicReference.get()) && Build.VERSION.SDK_INT >= 19 && Thread.currentThread() != Looper.getMainLooper().getThread()) {
                warmUpTextLayoutCache(this.layout);
            }
            swap();
            WXSDKManager.getInstance().getWXRenderManager().postOnUiThread((Runnable) new Runnable() {
                public void run() {
                    if (TextContentBoxMeasurement.this.mComponent != null) {
                        TextContentBoxMeasurement.this.mComponent.updateExtra(TextContentBoxMeasurement.this.atomicReference.get());
                    }
                }
            }, this.mComponent.getInstanceId());
        }
    }

    private void updateStyleAndText() {
        updateStyleImp(this.mComponent.getStyles());
        this.mText = WXAttr.getValue(this.mComponent.getAttrs());
    }

    @WorkerThread
    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public void forceRelayout() {
        layoutBefore();
        measure(this.previousWidth, Float.NaN, MeasureMode.EXACTLY, MeasureMode.UNSPECIFIED);
        layoutAfter(this.previousWidth, Float.NaN);
    }

    private void updateStyleImp(Map<String, Object> map) {
        if (map != null) {
            if (map.containsKey("lines")) {
                int lines = WXStyle.getLines(map);
                if (lines <= 0) {
                    lines = -1;
                }
                this.mNumberOfLines = lines;
            }
            if (map.containsKey("fontSize")) {
                this.mFontSize = WXStyle.getFontSize(map, this.mComponent.getViewPortWidth());
            }
            if (map.containsKey("fontWeight")) {
                this.mFontWeight = WXStyle.getFontWeight(map);
            }
            if (map.containsKey("fontStyle")) {
                this.mFontStyle = WXStyle.getFontStyle(map);
            }
            if (map.containsKey("color")) {
                this.mColor = WXResourceUtils.getColor(WXStyle.getTextColor(map));
                this.mIsColorSet = this.mColor != Integer.MIN_VALUE;
            }
            if (map.containsKey("textDecoration")) {
                this.mTextDecoration = WXStyle.getTextDecoration(map);
            }
            if (map.containsKey("fontFamily")) {
                this.mFontFamily = WXStyle.getFontFamily(map);
            }
            this.mAlignment = WXStyle.getTextAlignment(map, this.mComponent.isLayoutRTL());
            this.textOverflow = WXStyle.getTextOverflow(map);
            int lineHeight = WXStyle.getLineHeight(map, this.mComponent.getViewPortWidth());
            if (lineHeight != -1) {
                this.mLineHeight = lineHeight;
            }
        }
    }

    /* access modifiers changed from: protected */
    @NonNull
    public Spanned createSpanned(String str) {
        if (TextUtils.isEmpty(str)) {
            return new SpannableString("");
        }
        SpannableString spannableString = new SpannableString(str);
        updateSpannable(spannableString, 17);
        return spannableString;
    }

    /* access modifiers changed from: protected */
    public void updateSpannable(Spannable spannable, int i) {
        int length = spannable.length();
        if (this.mFontSize == -1) {
            this.mTextPaint.setTextSize(32.0f);
        } else {
            this.mTextPaint.setTextSize((float) this.mFontSize);
        }
        if (this.mLineHeight != -1) {
            setSpan(spannable, new WXLineHeightSpan(this.mLineHeight), 0, length, i);
        }
        setSpan(spannable, new AlignmentSpan.Standard(this.mAlignment), 0, length, i);
        if (!(this.mFontStyle == -1 && this.mFontWeight == -1 && this.mFontFamily == null)) {
            setSpan(spannable, new WXCustomStyleSpan(this.mFontStyle, this.mFontWeight, this.mFontFamily), 0, length, i);
        }
        if (this.mIsColorSet) {
            this.mTextPaint.setColor(this.mColor);
        }
        if (this.mTextDecoration == WXTextDecoration.UNDERLINE || this.mTextDecoration == WXTextDecoration.LINETHROUGH) {
            setSpan(spannable, new TextDecorationSpan(this.mTextDecoration), 0, length, i);
        }
    }

    private void setSpan(Spannable spannable, Object obj, int i, int i2, int i3) {
        spannable.setSpan(obj, i, i2, i3);
    }

    private float getTextWidth(TextPaint textPaint, float f, boolean z) {
        if (this.mText != null) {
            if (!z) {
                float desiredWidth = Layout.getDesiredWidth(this.spanned, textPaint);
                if (WXUtils.isUndefined(f) || desiredWidth < f) {
                    return desiredWidth;
                }
            }
            return f;
        } else if (z) {
            return f;
        } else {
            return 0.0f;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v1, resolved type: android.text.StaticLayout} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r15v5, resolved type: android.text.StaticLayout} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: android.text.StaticLayout} */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002f, code lost:
        r0 = r15.getLineStart(r13.mNumberOfLines - 1);
        r1 = r15.getLineEnd(r13.mNumberOfLines - 1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x003f, code lost:
        if (r0 >= r1) goto L_0x008e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0041, code lost:
        if (r0 <= 0) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0043, code lost:
        r15 = new android.text.SpannableStringBuilder(r13.spanned.subSequence(0, r0));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0050, code lost:
        r15 = new android.text.SpannableStringBuilder();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0055, code lost:
        r3 = (double) r14;
        r15.append(truncate(new android.text.SpannableStringBuilder(r13.spanned.subSequence(r0, r1)), r13.mTextPaint, (int) java.lang.Math.ceil(r3), r13.textOverflow));
        adjustSpansRange(r13.spanned, r15);
        r13.spanned = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x008d, code lost:
        return new android.text.StaticLayout(r13.spanned, r13.mTextPaint, (int) java.lang.Math.ceil(r3), android.text.Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x008e, code lost:
        return r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        if (r15 == null) goto L_0x0008;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0008, code lost:
        r1 = new android.text.StaticLayout(r13.spanned, r13.mTextPaint, (int) java.lang.Math.ceil((double) r14), android.text.Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0021, code lost:
        if (r13.mNumberOfLines == -1) goto L_0x008e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0025, code lost:
        if (r13.mNumberOfLines <= 0) goto L_0x008e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002d, code lost:
        if (r13.mNumberOfLines >= r15.getLineCount()) goto L_0x008e;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    @androidx.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.text.Layout createLayout(float r14, @androidx.annotation.Nullable android.text.Layout r15) {
        /*
            r13 = this;
            float r0 = r13.previousWidth
            int r0 = (r0 > r14 ? 1 : (r0 == r14 ? 0 : -1))
            if (r0 != 0) goto L_0x0008
            if (r15 != 0) goto L_0x001e
        L_0x0008:
            android.text.StaticLayout r15 = new android.text.StaticLayout
            android.text.Spanned r2 = r13.spanned
            android.text.TextPaint r3 = r13.mTextPaint
            double r0 = (double) r14
            double r0 = java.lang.Math.ceil(r0)
            int r4 = (int) r0
            android.text.Layout$Alignment r5 = android.text.Layout.Alignment.ALIGN_NORMAL
            r6 = 1065353216(0x3f800000, float:1.0)
            r7 = 0
            r8 = 0
            r1 = r15
            r1.<init>(r2, r3, r4, r5, r6, r7, r8)
        L_0x001e:
            int r0 = r13.mNumberOfLines
            r1 = -1
            if (r0 == r1) goto L_0x008e
            int r0 = r13.mNumberOfLines
            if (r0 <= 0) goto L_0x008e
            int r0 = r13.mNumberOfLines
            int r1 = r15.getLineCount()
            if (r0 >= r1) goto L_0x008e
            int r0 = r13.mNumberOfLines
            int r0 = r0 + -1
            int r0 = r15.getLineStart(r0)
            int r1 = r13.mNumberOfLines
            int r1 = r1 + -1
            int r1 = r15.getLineEnd(r1)
            if (r0 >= r1) goto L_0x008e
            if (r0 <= 0) goto L_0x0050
            android.text.SpannableStringBuilder r15 = new android.text.SpannableStringBuilder
            android.text.Spanned r2 = r13.spanned
            r3 = 0
            java.lang.CharSequence r2 = r2.subSequence(r3, r0)
            r15.<init>(r2)
            goto L_0x0055
        L_0x0050:
            android.text.SpannableStringBuilder r15 = new android.text.SpannableStringBuilder
            r15.<init>()
        L_0x0055:
            android.text.SpannableStringBuilder r2 = new android.text.SpannableStringBuilder
            android.text.Spanned r3 = r13.spanned
            java.lang.CharSequence r0 = r3.subSequence(r0, r1)
            r2.<init>(r0)
            android.text.TextPaint r0 = r13.mTextPaint
            double r3 = (double) r14
            double r5 = java.lang.Math.ceil(r3)
            int r14 = (int) r5
            android.text.TextUtils$TruncateAt r1 = r13.textOverflow
            android.text.Spanned r14 = r13.truncate(r2, r0, r14, r1)
            r15.append(r14)
            android.text.Spanned r14 = r13.spanned
            r13.adjustSpansRange(r14, r15)
            r13.spanned = r15
            android.text.StaticLayout r14 = new android.text.StaticLayout
            android.text.Spanned r6 = r13.spanned
            android.text.TextPaint r7 = r13.mTextPaint
            double r0 = java.lang.Math.ceil(r3)
            int r8 = (int) r0
            android.text.Layout$Alignment r9 = android.text.Layout.Alignment.ALIGN_NORMAL
            r10 = 1065353216(0x3f800000, float:1.0)
            r11 = 0
            r12 = 0
            r5 = r14
            r5.<init>(r6, r7, r8, r9, r10, r11, r12)
            return r14
        L_0x008e:
            return r15
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.layout.measurefunc.TextContentBoxMeasurement.createLayout(float, android.text.Layout):android.text.Layout");
    }

    @NonNull
    private Spanned truncate(@Nullable Editable editable, @NonNull TextPaint textPaint, int i, @Nullable TextUtils.TruncateAt truncateAt) {
        Editable editable2 = editable;
        SpannedString spannedString = new SpannedString("");
        if (!TextUtils.isEmpty(editable) && editable.length() > 0) {
            if (truncateAt != null) {
                editable.append(ELLIPSIS);
                for (Object obj : editable.getSpans(0, editable.length(), Object.class)) {
                    int spanStart = editable.getSpanStart(obj);
                    int spanEnd = editable.getSpanEnd(obj);
                    if (spanStart == 0 && spanEnd == editable.length() - 1) {
                        editable.removeSpan(obj);
                        editable.setSpan(obj, 0, editable.length(), editable.getSpanFlags(obj));
                    }
                }
            }
            while (editable.length() > 1) {
                int length = editable.length() - 1;
                if (truncateAt != null) {
                    length--;
                }
                editable.delete(length, length + 1);
                if (new StaticLayout(editable, textPaint, i, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false).getLineCount() <= 1) {
                    return editable2;
                }
            }
        }
        return spannedString;
    }

    private void adjustSpansRange(@NonNull Spanned spanned2, @NonNull Spannable spannable) {
        for (Object obj : spanned2.getSpans(0, spanned2.length(), Object.class)) {
            int spanStart = spanned2.getSpanStart(obj);
            int spanEnd = spanned2.getSpanEnd(obj);
            if (spanStart == 0 && spanEnd == spanned2.length()) {
                spannable.removeSpan(obj);
                spannable.setSpan(obj, 0, spannable.length(), spanned2.getSpanFlags(obj));
            }
        }
    }

    private void recalculateLayout(float f) {
        float contentWidth = WXDomUtils.getContentWidth(this.mComponent.getPadding(), this.mComponent.getBorder(), f);
        if (contentWidth > 0.0f) {
            this.spanned = createSpanned(this.mText);
            if (this.spanned != null) {
                this.layout = createLayout(contentWidth, this.layout);
                this.previousWidth = (float) this.layout.getWidth();
                return;
            }
            this.previousWidth = 0.0f;
        }
    }

    private boolean warmUpTextLayoutCache(Layout layout2) {
        try {
            layout2.draw(DUMMY_CANVAS);
            return true;
        } catch (Exception e) {
            WXLogUtils.eTag("TextWarmUp", e);
            return false;
        }
    }

    private void swap() {
        if (this.layout != null) {
            this.atomicReference.set(this.layout);
            this.layout = null;
        }
        this.hasBeenMeasured = false;
    }
}
