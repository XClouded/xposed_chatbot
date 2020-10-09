package com.taobao.android.dinamicx.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.model.DXLayoutParamAttribute;
import com.taobao.android.dinamicx.view.DXMeasuredTextView;
import com.taobao.android.dinamicx.view.DXNativeTextView;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;
import com.taobao.android.dinamicx.widget.utils.DXUtils;
import java.util.HashMap;

public class DXTextViewWidgetNode extends DXWidgetNode {
    private static int DEFAULT_FLAGS = 0;
    public static int DEFAULT_MAX_LINE = 1;
    public static final int DEFAULT_TEXT_COLOR = -16777216;
    public static int DEFAULT_TEXT_SIZE;
    private static ThreadLocal<DXLayoutParamAttribute> attributeThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<HashMap<Class, DXMeasuredTextView>> measuredTextViewHashMapThreadLocal = new ThreadLocal<>();
    private DXLayoutParamAttribute beforeMeasureAttribute;
    int lineBreakMode;
    int maxLine;
    int maxWidth;
    CharSequence text = "";
    int textColor = -16777216;
    int textFlags;
    int textGravity;
    float textSize;
    int textStyle;
    private DXMeasuredTextView textViewUtilForMeasure;

    /* access modifiers changed from: protected */
    public void onSetDoubleAttribute(long j, double d) {
    }

    public class DXTextStyle {
        public static final int MASK_BOLD_ITALIC = 3;
        public static final int TEXT_STYLE_BOLD = 1;
        public static final int TEXT_STYLE_ITALIC = 2;
        public static final int TEXT_STYLE_NORMAL = 0;

        public DXTextStyle() {
        }
    }

    public class DXTextGravity {
        public static final int CENTER = 1;
        public static final int LEFT = 0;
        public static final int RIGHT = 2;

        public DXTextGravity() {
        }
    }

    public class DXTextLineBreakMode {
        public static final int ELLIPSIZE_END = 3;
        public static final int ELLIPSIZE_MIDDLE = 2;
        public static final int ELLIPSIZE_NONE = 0;
        public static final int ELLIPSIZE_START = 1;

        public DXTextLineBreakMode() {
        }
    }

    public DXTextViewWidgetNode() {
        if (DEFAULT_TEXT_SIZE == 0 && DinamicXEngine.getApplicationContext() != null) {
            DEFAULT_TEXT_SIZE = DXScreenTool.dip2px(DinamicXEngine.getApplicationContext(), 12.0f);
        }
        HashMap hashMap = measuredTextViewHashMapThreadLocal.get();
        if (hashMap == null) {
            hashMap = new HashMap();
            measuredTextViewHashMapThreadLocal.set(hashMap);
        }
        this.textViewUtilForMeasure = (DXMeasuredTextView) hashMap.get(getClass());
        if (this.textViewUtilForMeasure == null) {
            this.textViewUtilForMeasure = new DXMeasuredTextView(DinamicXEngine.getApplicationContext());
            DEFAULT_FLAGS = this.textViewUtilForMeasure.getPaintFlags();
            hashMap.put(getClass(), this.textViewUtilForMeasure);
        }
        this.beforeMeasureAttribute = attributeThreadLocal.get();
        if (this.beforeMeasureAttribute == null) {
            this.beforeMeasureAttribute = new DXLayoutParamAttribute();
            attributeThreadLocal.set(this.beforeMeasureAttribute);
        }
        this.textSize = (float) DEFAULT_TEXT_SIZE;
        this.textStyle = 0;
        this.lineBreakMode = -1;
        this.textGravity = 0;
        this.maxLine = 1;
        this.maxWidth = -1;
    }

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(@Nullable Object obj) {
            return new DXTextViewWidgetNode();
        }
    }

    public DXWidgetNode build(@Nullable Object obj) {
        return new DXTextViewWidgetNode();
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"WrongCall"})
    public void onMeasure(int i, int i2) {
        if (View.MeasureSpec.getMode(i) == 1073741824 && View.MeasureSpec.getMode(i2) == 1073741824) {
            setMeasuredDimension(i, i2);
            return;
        }
        onBeforeMeasure(this.textViewUtilForMeasure);
        this.textViewUtilForMeasure.onMeasure(i, i2);
        if (!TextUtils.isEmpty(this.text) || this.layoutHeight != -2) {
            setMeasuredDimension(this.textViewUtilForMeasure.getMeasuredWidthAndState(), this.textViewUtilForMeasure.getMeasuredHeightAndState());
        } else {
            setMeasuredDimension(this.textViewUtilForMeasure.getMeasuredWidthAndState(), 0);
        }
    }

    /* access modifiers changed from: protected */
    public void onBeforeMeasure(TextView textView) {
        ViewGroup.LayoutParams layoutParams;
        resetMeasuredView(textView);
        this.beforeMeasureAttribute.widthAttr = this.layoutWidth;
        this.beforeMeasureAttribute.heightAttr = this.layoutHeight;
        this.beforeMeasureAttribute.weightAttr = this.weight;
        if (this.layoutGravity != this.beforeMeasureAttribute.oldGravity) {
            this.beforeMeasureAttribute.layoutGravityAttr = DXUtils.transformToNativeGravity(getAbsoluteGravity(this.layoutGravity, getDirection()));
            this.beforeMeasureAttribute.oldGravity = this.layoutGravity;
        }
        DXLayout dXLayout = (DXLayout) this.parentWidget;
        ViewGroup.LayoutParams layoutParams2 = textView.getLayoutParams();
        if (layoutParams2 == null) {
            layoutParams = dXLayout.generateLayoutParams(this.beforeMeasureAttribute);
        } else {
            layoutParams = dXLayout.generateLayoutParams(this.beforeMeasureAttribute, layoutParams2);
        }
        textView.setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public void resetMeasuredView(TextView textView) {
        setNativeText(textView, this.text);
        if (textView.getTextSize() != this.textSize) {
            textView.setTextSize(0, this.textSize);
        }
        setNativeTextStyle(textView, this.textStyle);
        setNativeMaxLines(textView, this.maxLine);
        setNativeEllipsize(textView, this.lineBreakMode);
        setNativeMaxWidth(textView, this.maxWidth);
        setNativeTextFlags(textView, this.textFlags);
    }

    /* access modifiers changed from: protected */
    public void setNativeTextStyle(TextView textView, int i) {
        switch (i) {
            case 0:
                textView.setTypeface(Typeface.defaultFromStyle(0));
                return;
            case 1:
                textView.setTypeface(Typeface.defaultFromStyle(1));
                return;
            case 2:
                textView.setTypeface(Typeface.defaultFromStyle(2));
                return;
            case 3:
                textView.setTypeface(Typeface.defaultFromStyle(3));
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void setNativeTextFlags(TextView textView, int i) {
        if (i != textView.getPaintFlags()) {
            if (i == 0) {
                i = DEFAULT_FLAGS;
            }
            textView.getPaint().setFlags(i);
        }
    }

    /* access modifiers changed from: protected */
    public void setNativeText(TextView textView, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            textView.setText("");
        } else {
            textView.setText(charSequence);
        }
    }

    /* access modifiers changed from: protected */
    public void setNativeMaxLines(TextView textView, int i) {
        if (i > 0) {
            textView.setMaxLines(i);
        } else {
            textView.setMaxLines(Integer.MAX_VALUE);
        }
    }

    /* access modifiers changed from: protected */
    public void setNativeMaxWidth(TextView textView, int i) {
        if (i > 0) {
            textView.setMaxWidth(i);
        } else {
            textView.setMaxWidth(Integer.MAX_VALUE);
        }
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        super.onClone(dXWidgetNode, z);
        if (dXWidgetNode instanceof DXTextViewWidgetNode) {
            DXTextViewWidgetNode dXTextViewWidgetNode = (DXTextViewWidgetNode) dXWidgetNode;
            this.textStyle = dXTextViewWidgetNode.textStyle;
            this.textGravity = dXTextViewWidgetNode.textGravity;
            this.maxLine = dXTextViewWidgetNode.maxLine;
            this.lineBreakMode = dXTextViewWidgetNode.lineBreakMode;
            this.maxWidth = dXTextViewWidgetNode.maxWidth;
            this.text = dXTextViewWidgetNode.text;
            this.textColor = dXTextViewWidgetNode.textColor;
            this.textSize = dXTextViewWidgetNode.textSize;
            this.textFlags = dXTextViewWidgetNode.textFlags;
        }
    }

    public int getDefaultValueForIntAttr(long j) {
        if (j == 5737767606580872653L) {
            return -16777216;
        }
        if (j == 6751005219504497256L) {
            return DEFAULT_TEXT_SIZE;
        }
        if (j == 4685059187929305417L) {
            return DEFAULT_MAX_LINE;
        }
        return super.getDefaultValueForIntAttr(j);
    }

    public String getDefaultValueForStringAttr(long j) {
        return j == 38178040921L ? "" : super.getDefaultValueForStringAttr(j);
    }

    public void onSetIntAttribute(long j, int i) {
        if (5737767606580872653L == j) {
            this.textColor = i;
        } else if (-1564827143683948874L == j) {
            this.textGravity = i;
        } else if (4685059187929305417L == j) {
            if (i > 0) {
                this.maxLine = i;
            } else {
                this.maxLine = Integer.MAX_VALUE;
            }
        } else if (4685059378591825230L == j) {
            if (i > 0) {
                this.maxWidth = i;
            } else {
                this.maxWidth = Integer.MAX_VALUE;
            }
        } else if (1650157837879951391L == j) {
            this.lineBreakMode = i;
        } else if (6751005219504497256L == j) {
            if (i > 0) {
                this.textSize = (float) i;
            } else {
                this.textSize = (float) DEFAULT_TEXT_SIZE;
            }
        } else if (9423384817756195L == j) {
            this.textStyle = i > 0 ? this.textStyle | 1 : this.textStyle & -2;
        } else if (3527554185889034042L == j) {
            this.textStyle = i > 0 ? this.textStyle | 2 : this.textStyle & -3;
        } else if (-1740854880214056386L == j) {
            this.textFlags = i > 0 ? this.textFlags | 17 : this.textFlags & -18;
        } else if (-8089424158689439347L == j) {
            this.textFlags = i > 0 ? this.textFlags | 9 : this.textFlags & -10;
        } else {
            super.onSetIntAttribute(j, i);
        }
    }

    /* access modifiers changed from: protected */
    public void onSetStringAttribute(long j, String str) {
        if (38178040921L == j) {
            this.text = str;
        } else {
            super.onSetStringAttribute(j, str);
        }
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return new DXNativeTextView(context);
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        if (view != null && (view instanceof TextView)) {
            TextView textView = (TextView) view;
            setNativeText(textView, this.text);
            textView.setTextColor(tryFetchDarkModeColor("textColor", 0, this.textColor));
            textView.setTextSize(0, this.textSize);
            if (this.textStyle != -1) {
                setNativeTextStyle(textView, this.textStyle);
            }
            setNativeMaxLines(textView, this.maxLine);
            setNativeTextGravity(textView, this.textGravity);
            if (this.lineBreakMode != -1) {
                setNativeEllipsize(textView, this.lineBreakMode);
            }
            if (this.maxWidth != -1) {
                setNativeMaxWidth(textView, this.maxWidth);
            }
            setNativeTextFlags(textView, this.textFlags);
        }
    }

    /* access modifiers changed from: protected */
    public void setNativeTextGravity(TextView textView, int i) {
        if (getDirection() == 1) {
            if (i == 0) {
                textView.setGravity(21);
            } else if (i == 1) {
                textView.setGravity(17);
            } else if (i == 2) {
                textView.setGravity(19);
            } else {
                textView.setGravity(16);
            }
        } else if (i == 0) {
            textView.setGravity(19);
        } else if (i == 1) {
            textView.setGravity(17);
        } else if (i == 2) {
            textView.setGravity(21);
        } else {
            textView.setGravity(16);
        }
    }

    /* access modifiers changed from: package-private */
    public void setAccessibility(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            if (this.accessibilityText != null) {
                view.setContentDescription(this.accessibilityText);
            }
            if (this.accessibility != 3) {
                if (this.accessibility == 1 || this.accessibility == -1) {
                    view.setImportantForAccessibility(1);
                    view.setFocusable(true);
                } else if (this.accessibility == 2) {
                    view.setImportantForAccessibility(4);
                } else {
                    view.setImportantForAccessibility(2);
                }
            }
        } else {
            view.setContentDescription("");
        }
    }

    /* access modifiers changed from: protected */
    public void setNativeEllipsize(TextView textView, int i) {
        switch (i) {
            case 0:
                textView.setEllipsize((TextUtils.TruncateAt) null);
                return;
            case 1:
                textView.setEllipsize(TextUtils.TruncateAt.START);
                return;
            case 2:
                textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                return;
            case 3:
                textView.setEllipsize(TextUtils.TruncateAt.END);
                return;
            default:
                return;
        }
    }

    public CharSequence getText() {
        return this.text;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public int getTextStyle() {
        return this.textStyle;
    }

    public int getTextGravity() {
        return this.textGravity;
    }

    public int getMaxLines() {
        return this.maxLine;
    }

    public int getLineBreakMode() {
        return this.lineBreakMode;
    }

    public int getMaxWidth() {
        return this.maxWidth;
    }

    public void setText(CharSequence charSequence) {
        this.text = charSequence;
    }

    public void setTextColor(int i) {
        this.textColor = i;
    }

    public void setTextSize(float f) {
        if (f > 0.0f) {
            this.textSize = f;
        } else {
            this.textSize = (float) DEFAULT_TEXT_SIZE;
        }
    }

    public void setTextStyle(int i) {
        this.textStyle = i;
    }

    public void setTextGravity(int i) {
        this.textGravity = i;
    }

    public void setMaxLines(int i) {
        if (i > 0) {
            this.maxLine = i;
        } else {
            this.maxLine = Integer.MAX_VALUE;
        }
    }

    public void setLineBreakMode(int i) {
        this.lineBreakMode = i;
    }

    public void setMaxWidth(int i) {
        if (i > 0) {
            this.maxWidth = i;
        } else {
            this.maxWidth = Integer.MAX_VALUE;
        }
    }

    public static void clearStaticField() {
        measuredTextViewHashMapThreadLocal = new ThreadLocal<>();
        DEFAULT_TEXT_SIZE = 0;
    }
}
