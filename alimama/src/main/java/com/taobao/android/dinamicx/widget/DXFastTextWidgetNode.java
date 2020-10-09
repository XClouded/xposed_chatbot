package com.taobao.android.dinamicx.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.template.loader.binary.DXHashConstant;
import com.taobao.android.dinamicx.view.DXNativeFastText;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;

public class DXFastTextWidgetNode extends DXWidgetNode {
    public static int DEFAULT_MAX_LINE = 1;
    public static final int DEFAULT_TEXT_COLOR = -16777216;
    public static int DEFAULT_TEXT_SIZE;
    public static final CharSequence ELLIPSIS_TEXT = "…";
    Layout.Alignment alignment;
    TextUtils.TruncateAt ellipsize;
    int lineBreakMode;
    int lineHeight = -1;
    int lineSpacing = -1;
    int maxLine;
    int maxWidth;
    int realPaddingBottom;
    int realPaddingTop;
    CharSequence showText = "";
    StaticLayout staticLayout;
    CharSequence text = "";
    int textColor = -16777216;
    int textFlags;
    int textGravity;
    TextPaint textPaint;
    float textSize;
    int textStyle;
    float translateY;

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

    public DXFastTextWidgetNode() {
        if (DEFAULT_TEXT_SIZE == 0 && DinamicXEngine.getApplicationContext() != null) {
            DEFAULT_TEXT_SIZE = DXScreenTool.dip2px(DinamicXEngine.getApplicationContext(), 12.0f);
        }
        this.textSize = (float) DEFAULT_TEXT_SIZE;
        this.textStyle = 0;
        this.lineBreakMode = -1;
        this.textGravity = 0;
        this.maxLine = DEFAULT_MAX_LINE;
        this.maxWidth = Integer.MAX_VALUE;
    }

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(@Nullable Object obj) {
            return new DXFastTextWidgetNode();
        }
    }

    public DXWidgetNode build(@Nullable Object obj) {
        return new DXFastTextWidgetNode();
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"WrongCall"})
    public void onMeasure(int i, int i2) {
        int i3;
        this.showText = this.text;
        initText();
        if (View.MeasureSpec.getMode(i) == 1073741824) {
            i3 = View.MeasureSpec.getSize(i);
            this.staticLayout = makeNewLayout((i3 - getPaddingLeft()) - getPaddingRight(), this.showText);
        } else {
            i3 = Math.min(Math.min(((int) this.textPaint.measureText(this.text.toString())) + getPaddingLeft() + getPaddingRight(), View.MeasureSpec.getSize(i)), this.maxWidth);
            this.staticLayout = makeNewLayout((i3 - getPaddingLeft()) - getPaddingRight(), this.text);
        }
        if (this.maxLine <= 0 || this.maxLine >= this.staticLayout.getLineCount()) {
            this.showText = this.text;
        } else {
            calculateEllipsizeText();
            this.staticLayout = makeNewLayout((i3 - getPaddingLeft()) - getPaddingRight(), this.showText);
        }
        int mode = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i2);
        if (mode != 1073741824) {
            if (!TextUtils.isEmpty(this.text) || this.layoutHeight != -2) {
                int height = this.staticLayout.getHeight() + this.realPaddingBottom + this.realPaddingTop;
                if (this.maxLine > 0 && this.maxLine < this.staticLayout.getLineCount()) {
                    height = this.staticLayout.getLineTop(this.maxLine);
                }
                size = Math.min(height, size);
            } else {
                size = 0;
            }
        }
        setMeasuredDimension(i3, size);
        this.translateY = calculateTranslateY(mode);
    }

    private void calculateEllipsizeText() {
        int i;
        int lineEnd = this.staticLayout.getLineEnd(this.maxLine - 1);
        if (lineEnd <= 0) {
            try {
                this.showText = "";
            } catch (Exception e) {
                this.showText = this.text.subSequence(0, lineEnd);
                if (getDXRuntimeContext() == null || TextUtils.isEmpty(getDXRuntimeContext().getBizType())) {
                    DXError dXError = new DXError("dinamicx");
                    dXError.dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE_DETAIL, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PERFORM_MEASURE, DXError.DXERROR_DETAIL_FAST_TEXT_CALCULATE_CRASH));
                    DXAppMonitor.trackerError(dXError);
                } else {
                    DXError dxError = getDXRuntimeContext().getDxError();
                    dxError.dxTemplateItem = getDXRuntimeContext().getDxTemplateItem();
                    dxError.dxErrorInfoList.add(new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE_DETAIL, DXMonitorConstant.DX_MONITOR_SERVICE_ID_PERFORM_MEASURE, DXError.DXERROR_DETAIL_FAST_TEXT_CALCULATE_CRASH));
                }
                if (DinamicXEngine.isDebug()) {
                    e.printStackTrace();
                }
            }
        } else {
            if (this.ellipsize != null) {
                if (this.text.length() != 1) {
                    float width = ((float) this.staticLayout.getWidth()) - this.textPaint.measureText(ELLIPSIS_TEXT, 0, ELLIPSIS_TEXT.length());
                    int lineStart = this.staticLayout.getLineStart(this.maxLine - 1);
                    if (this.ellipsize == TextUtils.TruncateAt.END) {
                        int i2 = lineEnd - 1;
                        while (true) {
                            if (i2 < lineStart) {
                                i2 = 0;
                                break;
                            }
                            CharSequence subSequence = this.text.subSequence(lineStart, i2);
                            if (this.textPaint.measureText(subSequence, 0, subSequence.length()) < width) {
                                break;
                            }
                            i2--;
                        }
                        this.showText = this.text.subSequence(0, i2).toString() + ELLIPSIS_TEXT;
                        return;
                    } else if (this.ellipsize == TextUtils.TruncateAt.START && this.maxLine == 1) {
                        int length = this.text.length();
                        int i3 = length - 1;
                        while (true) {
                            if (i3 < 0) {
                                i = 0;
                                break;
                            } else if (this.textPaint.measureText(this.text, i3, length) > width) {
                                i = 1 + i3;
                                break;
                            } else {
                                i3--;
                            }
                        }
                        this.showText = ELLIPSIS_TEXT + this.text.subSequence(i, length).toString();
                        return;
                    } else if (this.ellipsize == TextUtils.TruncateAt.MIDDLE && this.maxLine == 1) {
                        int length2 = this.text.length();
                        int i4 = length2;
                        int i5 = 0;
                        boolean z = true;
                        int i6 = 0;
                        float f = 0.0f;
                        float f2 = 0.0f;
                        while (true) {
                            if (i5 >= length2) {
                                break;
                            }
                            if (z) {
                                i6++;
                                float measureText = this.textPaint.measureText(this.text, 0, i6);
                                if (measureText + f > width) {
                                    i6--;
                                    break;
                                } else {
                                    f2 = measureText;
                                    z = false;
                                }
                            } else {
                                i4--;
                                float measureText2 = this.textPaint.measureText(this.text, i4, length2);
                                if (f2 + measureText2 > width) {
                                    i4++;
                                    break;
                                } else {
                                    f = measureText2;
                                    z = true;
                                }
                            }
                            i5++;
                        }
                        this.showText = this.text.subSequence(0, i6).toString() + ELLIPSIS_TEXT + this.text.subSequence(i4, length2);
                        return;
                    } else {
                        return;
                    }
                }
            }
            this.showText = this.text.subSequence(0, lineEnd);
        }
    }

    /* access modifiers changed from: protected */
    public void initText() {
        if (this.textPaint == null) {
            this.textPaint = new TextPaint();
        }
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTextSize(this.textSize);
        this.textPaint.setColor(tryFetchDarkModeColor("textColor", 0, this.textColor));
        this.textPaint.setTypeface(getTypeface(this.textStyle));
        if (this.textFlags > 0) {
            this.textPaint.setFlags(this.textFlags);
        }
        this.ellipsize = getEllipsize(this.lineBreakMode);
        this.alignment = getAlignment(this.textGravity);
        if (this.accessibilityText == null) {
            setAccessibilityText(this.text.toString());
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
    public Typeface getTypeface(int i) {
        switch (i) {
            case 0:
                return Typeface.defaultFromStyle(0);
            case 1:
                return Typeface.defaultFromStyle(1);
            case 2:
                return Typeface.defaultFromStyle(2);
            case 3:
                return Typeface.defaultFromStyle(3);
            default:
                return Typeface.defaultFromStyle(0);
        }
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        super.onClone(dXWidgetNode, z);
        if (dXWidgetNode instanceof DXFastTextWidgetNode) {
            DXFastTextWidgetNode dXFastTextWidgetNode = (DXFastTextWidgetNode) dXWidgetNode;
            this.textStyle = dXFastTextWidgetNode.textStyle;
            this.textGravity = dXFastTextWidgetNode.textGravity;
            this.maxLine = dXFastTextWidgetNode.maxLine;
            this.lineBreakMode = dXFastTextWidgetNode.lineBreakMode;
            this.maxWidth = dXFastTextWidgetNode.maxWidth;
            this.text = dXFastTextWidgetNode.text;
            this.textColor = dXFastTextWidgetNode.textColor;
            this.textSize = dXFastTextWidgetNode.textSize;
            this.textFlags = dXFastTextWidgetNode.textFlags;
            this.showText = dXFastTextWidgetNode.showText;
            this.textPaint = dXFastTextWidgetNode.textPaint;
            this.staticLayout = dXFastTextWidgetNode.staticLayout;
            this.ellipsize = dXFastTextWidgetNode.ellipsize;
            this.translateY = dXFastTextWidgetNode.translateY;
            this.alignment = dXFastTextWidgetNode.alignment;
            this.lineHeight = dXFastTextWidgetNode.lineHeight;
            this.lineSpacing = dXFastTextWidgetNode.lineSpacing;
            this.realPaddingTop = dXFastTextWidgetNode.realPaddingTop;
            this.realPaddingBottom = dXFastTextWidgetNode.realPaddingBottom;
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
        if (j == DXHashConstant.DX_FASTTEXT_LINESPACING || j == DXHashConstant.DX_FASTTEXT_LINEHEIGHT) {
            return -1;
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
        } else if (DXHashConstant.DX_FASTTEXT_LINEHEIGHT == j) {
            this.lineHeight = i;
        } else if (DXHashConstant.DX_FASTTEXT_LINESPACING == j) {
            this.lineSpacing = i;
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
        return new DXNativeFastText(context);
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        if (view != null && (view instanceof DXNativeFastText)) {
            DXNativeFastText dXNativeFastText = (DXNativeFastText) view;
            if (this.staticLayout != null) {
                dXNativeFastText.setStaticLayout(this.staticLayout);
            }
            dXNativeFastText.setTranslateY(this.translateY + ((float) this.realPaddingTop));
            dXNativeFastText.setTranslateX((float) getPaddingLeft());
        }
    }

    /* access modifiers changed from: protected */
    public float calculateTranslateY(int i) {
        int height = this.staticLayout.getHeight();
        int measuredHeight = (getMeasuredHeight() - this.realPaddingTop) - this.realPaddingBottom;
        if (height >= measuredHeight || i != 1073741824) {
            return 0.0f;
        }
        return (float) ((measuredHeight - height) >> 1);
    }

    /* access modifiers changed from: protected */
    public TextUtils.TruncateAt getEllipsize(int i) {
        switch (i) {
            case 0:
                return null;
            case 1:
                return TextUtils.TruncateAt.START;
            case 2:
                return TextUtils.TruncateAt.MIDDLE;
            case 3:
                return TextUtils.TruncateAt.END;
            default:
                return null;
        }
    }

    /* access modifiers changed from: protected */
    public Layout.Alignment getAlignment(int i) {
        if (getDirection() == 1) {
            switch (i) {
                case 0:
                    return Layout.Alignment.ALIGN_OPPOSITE;
                case 1:
                    return Layout.Alignment.ALIGN_CENTER;
                case 2:
                    return Layout.Alignment.ALIGN_NORMAL;
                default:
                    return Layout.Alignment.ALIGN_OPPOSITE;
            }
        } else {
            switch (i) {
                case 0:
                    return Layout.Alignment.ALIGN_NORMAL;
                case 1:
                    return Layout.Alignment.ALIGN_CENTER;
                case 2:
                    return Layout.Alignment.ALIGN_OPPOSITE;
                default:
                    return Layout.Alignment.ALIGN_NORMAL;
            }
        }
    }

    private StaticLayout makeNewLayout(int i, CharSequence charSequence) {
        boolean z;
        float f;
        boolean z2 = true;
        boolean z3 = this.lineSpacing >= 0;
        float textSize2 = getTextSize();
        float descent = this.textPaint.descent() - this.textPaint.ascent();
        boolean z4 = ((float) this.lineHeight) >= descent;
        this.realPaddingTop = getPaddingTop();
        this.realPaddingBottom = getPaddingBottom();
        float f2 = 0.0f;
        if (z3 && !z4) {
            f2 = Math.max(((float) this.lineSpacing) - (descent - textSize2), 0.0f);
            z2 = false;
        }
        if (z4) {
            float f3 = descent - textSize2;
            int max = Math.max((int) (((((float) this.lineHeight) - descent) - f3) / 2.0f), 0);
            int max2 = Math.max((int) (((((float) this.lineHeight) - descent) + f3) / 2.0f), 0);
            this.realPaddingTop = getPaddingTop() + max;
            this.realPaddingBottom = getPaddingBottom() + max2;
            f = z3 ? (float) (max + max2 + this.lineSpacing) : (float) (max + max2);
            z = false;
        } else {
            z = z2;
            f = f2;
        }
        return new StaticLayout(charSequence, this.textPaint, i, this.alignment, 1.0f, f, z);
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

    public void setText(String str) {
        this.text = str;
    }

    public CharSequence getText() {
        return this.text;
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
}
