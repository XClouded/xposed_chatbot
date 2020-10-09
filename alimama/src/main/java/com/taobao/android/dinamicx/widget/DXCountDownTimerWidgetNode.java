package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import com.taobao.android.dinamicx.template.loader.binary.DXHashConstant;
import com.taobao.android.dinamicx.view.DXNativeCountDownTimerView;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;

public class DXCountDownTimerWidgetNode extends DXWidgetNode {
    public static final int DEFAULT_TEXT_COLOR = -16777216;
    private String colonText = ":";
    private int colonTextColor = -16777216;
    private int colonTextMarginBottom;
    private int colonTextMarginLeft;
    private int colonTextMarginRight;
    private int colonTextMarginTop;
    private double colonTextSize = 10.0d;
    private long currentTime;
    private long futureTime;
    private int milliSecondDigitCount = 1;
    private boolean milliSecondTextSelfAdaption = false;
    private double seeMoreSize = 12.0d;
    private String seeMoreText;
    private int seeMoreTextColor = -16777216;
    private int seeMoreTextMarginBottom;
    private int seeMoreTextMarginLeft;
    private int seeMoreTextMarginRight;
    private int seeMoreTextMarginTop;
    private boolean showMilliSecond = false;
    private boolean showSeeMoreText = true;
    private int timerBackgroundColor = -16777216;
    private int timerCornerRadius;
    private int timerTextColor = -1;
    private int timerTextHeight = 0;
    private int timerTextMarginBottom;
    private int timerTextMarginLeft;
    private int timerTextMarginRight;
    private int timerTextMarginTop;
    private double timerTextSize = 12.0d;
    private int timerTextWidth = 0;
    private boolean useRemoteTime = false;

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(@Nullable Object obj) {
            return new DXCountDownTimerWidgetNode();
        }
    }

    public DXWidgetNode build(@Nullable Object obj) {
        return new DXCountDownTimerWidgetNode();
    }

    public DXCountDownTimerWidgetNode() {
        if (DinamicXEngine.getApplicationContext() != null) {
            this.colonTextSize = (double) DXScreenTool.dip2px(DinamicXEngine.getApplicationContext(), 10.0f);
            this.timerTextSize = (double) DXScreenTool.dip2px(DinamicXEngine.getApplicationContext(), 12.0f);
            this.seeMoreSize = (double) DXScreenTool.dip2px(DinamicXEngine.getApplicationContext(), 12.0f);
        }
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        super.onClone(dXWidgetNode, z);
        if (dXWidgetNode instanceof DXCountDownTimerWidgetNode) {
            DXCountDownTimerWidgetNode dXCountDownTimerWidgetNode = (DXCountDownTimerWidgetNode) dXWidgetNode;
            this.currentTime = dXCountDownTimerWidgetNode.currentTime;
            this.futureTime = dXCountDownTimerWidgetNode.futureTime;
            this.colonTextColor = dXCountDownTimerWidgetNode.colonTextColor;
            this.colonText = dXCountDownTimerWidgetNode.colonText;
            this.colonTextMarginBottom = dXCountDownTimerWidgetNode.colonTextMarginBottom;
            this.colonTextMarginLeft = dXCountDownTimerWidgetNode.colonTextMarginLeft;
            this.colonTextMarginRight = dXCountDownTimerWidgetNode.colonTextMarginRight;
            this.colonTextMarginTop = dXCountDownTimerWidgetNode.colonTextMarginTop;
            this.colonTextSize = dXCountDownTimerWidgetNode.colonTextSize;
            this.seeMoreText = dXCountDownTimerWidgetNode.seeMoreText;
            this.seeMoreSize = dXCountDownTimerWidgetNode.seeMoreSize;
            this.seeMoreTextColor = dXCountDownTimerWidgetNode.seeMoreTextColor;
            this.seeMoreTextMarginLeft = dXCountDownTimerWidgetNode.seeMoreTextMarginLeft;
            this.seeMoreTextMarginTop = dXCountDownTimerWidgetNode.seeMoreTextMarginTop;
            this.seeMoreTextMarginRight = dXCountDownTimerWidgetNode.seeMoreTextMarginRight;
            this.seeMoreTextMarginBottom = dXCountDownTimerWidgetNode.seeMoreTextMarginBottom;
            this.timerBackgroundColor = dXCountDownTimerWidgetNode.timerBackgroundColor;
            this.timerCornerRadius = dXCountDownTimerWidgetNode.timerCornerRadius;
            this.timerTextColor = dXCountDownTimerWidgetNode.timerTextColor;
            this.timerTextHeight = dXCountDownTimerWidgetNode.timerTextHeight;
            this.timerTextWidth = dXCountDownTimerWidgetNode.timerTextWidth;
            this.timerTextMarginBottom = dXCountDownTimerWidgetNode.timerTextMarginBottom;
            this.timerTextMarginLeft = dXCountDownTimerWidgetNode.timerTextMarginLeft;
            this.timerTextMarginTop = dXCountDownTimerWidgetNode.timerTextMarginTop;
            this.timerTextMarginRight = dXCountDownTimerWidgetNode.timerTextMarginRight;
            this.timerTextSize = dXCountDownTimerWidgetNode.timerTextSize;
            this.showSeeMoreText = dXCountDownTimerWidgetNode.showSeeMoreText;
            this.showMilliSecond = dXCountDownTimerWidgetNode.showMilliSecond;
            this.milliSecondDigitCount = dXCountDownTimerWidgetNode.milliSecondDigitCount;
            this.milliSecondTextSelfAdaption = dXCountDownTimerWidgetNode.milliSecondTextSelfAdaption;
            this.useRemoteTime = dXCountDownTimerWidgetNode.useRemoteTime;
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int mode = DXWidgetNode.DXMeasureSpec.getMode(i);
        int mode2 = DXWidgetNode.DXMeasureSpec.getMode(i2);
        boolean z = true;
        int i3 = 0;
        boolean z2 = mode == 1073741824;
        if (mode2 != 1073741824) {
            z = false;
        }
        int size = z2 ? DXWidgetNode.DXMeasureSpec.getSize(i) : 0;
        if (z) {
            i3 = DXWidgetNode.DXMeasureSpec.getSize(i2);
        }
        setMeasuredDimension(size, i3);
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        View view2 = view;
        if (view2 != null && (view2 instanceof DXNativeCountDownTimerView)) {
            DXNativeCountDownTimerView dXNativeCountDownTimerView = (DXNativeCountDownTimerView) view2;
            if (!(getDXRuntimeContext() == null || getDXRuntimeContext().getEngineContext() == null)) {
                long fetchRemoteTimeSync = getDXRuntimeContext().getEngineContext().fetchRemoteTimeSync();
                if (this.useRemoteTime && fetchRemoteTimeSync > 0) {
                    this.currentTime = fetchRemoteTimeSync;
                }
            }
            dXNativeCountDownTimerView.setShowMilliSecond(this.showMilliSecond);
            dXNativeCountDownTimerView.setMilliSecondDigitCount(this.milliSecondDigitCount);
            int tryFetchDarkModeColor = tryFetchDarkModeColor("colonTextColor", 0, this.colonTextColor);
            int tryFetchDarkModeColor2 = tryFetchDarkModeColor("seeMoreTextColor", 0, this.seeMoreTextColor);
            int tryFetchDarkModeColor3 = tryFetchDarkModeColor("timerBackgroundColor", 1, this.timerBackgroundColor);
            DXNativeCountDownTimerView dXNativeCountDownTimerView2 = dXNativeCountDownTimerView;
            setTimerTextViewStyle(dXNativeCountDownTimerView2, this.timerTextMarginLeft, this.timerTextMarginTop, this.timerTextMarginRight, this.timerTextMarginBottom, this.timerTextWidth, this.timerTextHeight, this.timerTextSize, tryFetchDarkModeColor("timerTextColor", 0, this.timerTextColor), tryFetchDarkModeColor3, this.timerCornerRadius);
            setColonTextViewStyle(dXNativeCountDownTimerView2, this.colonTextMarginLeft, this.colonTextMarginTop, this.colonTextMarginRight, this.colonTextMarginBottom, this.colonTextSize, tryFetchDarkModeColor, this.colonText);
            setSeeMoreTextViewStyle(dXNativeCountDownTimerView2, this.seeMoreText, this.seeMoreTextMarginLeft, this.seeMoreTextMarginTop, this.seeMoreTextMarginRight, this.seeMoreTextMarginBottom, this.seeMoreSize, tryFetchDarkModeColor2);
            dXNativeCountDownTimerView.setShowSeeMoreText(this.showSeeMoreText);
            setFutureTime(dXNativeCountDownTimerView2, this.futureTime, this.currentTime);
        }
    }

    private void setFutureTime(DXNativeCountDownTimerView dXNativeCountDownTimerView, long j, long j2) {
        dXNativeCountDownTimerView.setFutureTime(j);
        dXNativeCountDownTimerView.setCurrentTime(j2);
        if (dXNativeCountDownTimerView.getLastTime() > 0) {
            dXNativeCountDownTimerView.showCountDown();
            dXNativeCountDownTimerView.updateCountView();
            dXNativeCountDownTimerView.getTimer().start();
            dXNativeCountDownTimerView.setOnFinishListener(new DXNativeCountDownTimerView.OnFinishListener() {
                public void onFinish() {
                    DXCountDownTimerWidgetNode.this.postEvent(new DXEvent(DXHashConstant.DX_COUNTDOWNVIEW_ONCOUNTDOWNFINISH));
                }
            });
            return;
        }
        dXNativeCountDownTimerView.hideCountDown();
        dXNativeCountDownTimerView.getTimer().stop();
        postEvent(new DXEvent(DXHashConstant.DX_COUNTDOWNVIEW_ONCOUNTDOWNFINISH));
    }

    private void setSeeMoreTextViewStyle(DXNativeCountDownTimerView dXNativeCountDownTimerView, String str, int i, int i2, int i3, int i4, double d, int i5) {
        TextView seeMoreView = dXNativeCountDownTimerView.getSeeMoreView();
        String str2 = str;
        seeMoreView.setText(str);
        setTextViewStyle(seeMoreView, i, i2, i3, i4, 0, 0, d, i5);
    }

    private void setTimerTextViewStyle(DXNativeCountDownTimerView dXNativeCountDownTimerView, int i, int i2, int i3, int i4, int i5, int i6, double d, int i7, int i8, int i9) {
        TextView hour = dXNativeCountDownTimerView.getHour();
        TextView minute = dXNativeCountDownTimerView.getMinute();
        TextView second = dXNativeCountDownTimerView.getSecond();
        TextView textView = hour;
        int i10 = i;
        int i11 = i2;
        int i12 = i3;
        int i13 = i4;
        int i14 = i5;
        int i15 = i6;
        double d2 = d;
        TextView textView2 = hour;
        TextView milli = dXNativeCountDownTimerView.getMilli();
        int i16 = i7;
        setTextViewStyle(textView, i10, i11, i12, i13, i14, i15, d2, i16);
        setTextViewStyle(minute, i10, i11, i12, i13, i14, i15, d2, i16);
        setTextViewStyle(second, i10, i11, i12, i13, i14, i15, d2, i16);
        if (this.showMilliSecond) {
            milli.setVisibility(0);
            setTextViewStyle(milli, i, i2, i3, i4, (this.milliSecondDigitCount != 1 || !this.milliSecondTextSelfAdaption || i5 <= 0) ? i5 : i5 / 2, i6, d, i7);
        } else {
            milli.setVisibility(8);
        }
        setTimerTextBackground(textView2, minute, second, milli, i8, i9);
    }

    private void setColonTextViewStyle(DXNativeCountDownTimerView dXNativeCountDownTimerView, int i, int i2, int i3, int i4, double d, int i5, String str) {
        String str2 = str;
        TextView colonFirst = dXNativeCountDownTimerView.getColonFirst();
        TextView colonSecond = dXNativeCountDownTimerView.getColonSecond();
        TextView colonThird = dXNativeCountDownTimerView.getColonThird();
        int i6 = i;
        int i7 = i2;
        int i8 = i3;
        int i9 = i4;
        double d2 = d;
        int i10 = i5;
        setTextViewStyle(colonFirst, i6, i7, i8, i9, 0, 0, d2, i10);
        setTextViewStyle(colonSecond, i6, i7, i8, i9, 0, 0, d2, i10);
        if (this.showMilliSecond) {
            colonThird.setVisibility(0);
            setTextViewStyle(colonThird, i, i2, i3, i4, 0, 0, d, i5);
        } else {
            colonThird.setVisibility(8);
        }
        colonFirst.setText(str2);
        colonSecond.setText(str2);
        colonThird.setText(str2);
    }

    private void setTextViewStyle(TextView textView, int i, int i2, int i3, int i4, int i5, int i6, double d, int i7) {
        textView.setTextSize(0, (float) d);
        textView.setTextColor(i7);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
        if (i5 != 0) {
            marginLayoutParams.width = i5;
        }
        if (i6 != 0) {
            marginLayoutParams.height = i6;
        }
        marginLayoutParams.setMargins(i, i2, i3, i4);
        textView.setLayoutParams(marginLayoutParams);
    }

    private void setTimerTextBackground(TextView textView, TextView textView2, TextView textView3, TextView textView4, int i, int i2) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        float f = (float) i2;
        gradientDrawable.setCornerRadius(f);
        gradientDrawable.setColor(i);
        textView.setBackgroundDrawable(gradientDrawable);
        textView2.setBackgroundDrawable(gradientDrawable);
        textView3.setBackgroundDrawable(gradientDrawable);
        if (!this.showMilliSecond) {
            return;
        }
        if (this.milliSecondDigitCount != 1 || !this.milliSecondTextSelfAdaption) {
            textView4.setBackgroundDrawable(gradientDrawable);
            return;
        }
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setCornerRadius(f);
        gradientDrawable2.setColor(i);
        textView4.setBackgroundDrawable(gradientDrawable2);
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return new DXNativeCountDownTimerView(context);
    }

    /* access modifiers changed from: protected */
    public void onSetStringAttribute(long j, String str) {
        if (DXHashConstant.DX_COUNTDOWNVIEW_COLONTEXT == j) {
            this.colonText = str;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_SEEMORETEXT == j) {
            this.seeMoreText = str;
        }
    }

    public int getDefaultValueForIntAttr(long j) {
        if (j == DXHashConstant.DX_COUNTDOWNVIEW_SEEMORETEXTCOLOR || j == DXHashConstant.DX_COUNTDOWNVIEW_COLONTEXTCOLOR) {
            return -16777216;
        }
        if (j == DXHashConstant.DX_COUNTDOWNVIEW_TIMERTEXTCOLOR) {
            return -1;
        }
        if (j == DXHashConstant.DX_COUNTDOWNVIEW_SHOWSEEMORETEXT || j == DXHashConstant.DX_COUNTDOWNVIEW_MILLI_SECOND_DIGIT_COUNT) {
            return 1;
        }
        if (j == DXHashConstant.DX_COUNTDOWNVIEW_MILLI_SECOND_TEXT_SELF_ADAPTION) {
            return 0;
        }
        return super.getDefaultValueForIntAttr(j);
    }

    /* access modifiers changed from: protected */
    public void onSetIntAttribute(long j, int i) {
        if (DXHashConstant.DX_COUNTDOWNVIEW_COLONTEXTCOLOR == j) {
            this.colonTextColor = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_COLONTEXTMARGINBOTTOM == j) {
            this.colonTextMarginBottom = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_COLONTEXTMARGINLEFT == j) {
            this.colonTextMarginLeft = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_COLONTEXTMARGINRIGHT == j) {
            this.colonTextMarginRight = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_COLONTEXTMARGINTOP == j) {
            this.colonTextMarginTop = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_SEEMORETEXTCOLOR == j) {
            this.seeMoreTextColor = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_SEEMORETEXTMARGINBOTTOM == j) {
            this.seeMoreTextMarginBottom = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_SEEMORETEXTMARGINLEFT == j) {
            this.seeMoreTextMarginLeft = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_SEEMORETEXTMARGINRIGHT == j) {
            this.seeMoreTextMarginRight = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_SEEMORETEXTMARGINTOP == j) {
            this.seeMoreTextMarginTop = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_TIMERBACKGROUNDCOLOR == j) {
            this.timerBackgroundColor = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_TIMERCORNERRADIUS == j) {
            this.timerCornerRadius = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_TIMERTEXTCOLOR == j) {
            this.timerTextColor = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_TIMERTEXTHEIGHT == j) {
            this.timerTextHeight = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_TIMERTEXTWIDTH == j) {
            this.timerTextWidth = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_TIMERTEXTMARGINBOTTOM == j) {
            this.timerTextMarginBottom = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_TIMERTEXTMARGINLEFT == j) {
            this.timerTextMarginLeft = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_TIMERTEXTMARGINRIGHT == j) {
            this.timerTextMarginRight = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_TIMERTEXTMARGINTOP == j) {
            this.timerTextMarginTop = i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_SEEMORETEXTSIZE == j) {
            this.seeMoreSize = (double) i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_COLONTEXTSIZE == j) {
            this.colonTextSize = (double) i;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_TIMERTEXTSIZE == j) {
            this.timerTextSize = (double) i;
        } else {
            boolean z = false;
            if (DXHashConstant.DX_COUNTDOWNVIEW_SHOWSEEMORETEXT == j) {
                if (i != 0) {
                    z = true;
                }
                this.showSeeMoreText = z;
            } else if (DXHashConstant.DX_COUNTDOWNVIEW_SHOW_MILLI_SECOND == j) {
                if (i != 0) {
                    z = true;
                }
                this.showMilliSecond = z;
            } else if (DXHashConstant.DX_COUNTDOWNVIEW_MILLI_SECOND_DIGIT_COUNT == j) {
                this.milliSecondDigitCount = i;
            } else if (DXHashConstant.DX_COUNTDOWNVIEW_MILLI_SECOND_TEXT_SELF_ADAPTION == j) {
                if (i != 0) {
                    z = true;
                }
                this.milliSecondTextSelfAdaption = z;
            } else if (DXHashConstant.DXCOUNTDOWNVIEW_USEREMOTETIME == j) {
                if (i != 0) {
                    z = true;
                }
                this.useRemoteTime = z;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSetLongAttribute(long j, long j2) {
        if (DXHashConstant.DX_COUNTDOWNVIEW_CURRENTTIME == j) {
            this.currentTime = j2;
        } else if (DXHashConstant.DX_COUNTDOWNVIEW_FUTURETIME == j) {
            this.futureTime = j2;
        }
    }

    public String getColonText() {
        return this.colonText;
    }

    public int getColonTextColor() {
        return this.colonTextColor;
    }

    public int getColonTextMarginBottom() {
        return this.colonTextMarginBottom;
    }

    public int getColonTextMarginLeft() {
        return this.colonTextMarginLeft;
    }

    public int getColonTextMarginRight() {
        return this.colonTextMarginRight;
    }

    public int getColonTextMarginTop() {
        return this.colonTextMarginTop;
    }

    public String getSeeMoreText() {
        return this.seeMoreText;
    }

    public int getSeeMoreTextColor() {
        return this.seeMoreTextColor;
    }

    public int getSeeMoreTextMarginBottom() {
        return this.seeMoreTextMarginBottom;
    }

    public int getSeeMoreTextMarginLeft() {
        return this.seeMoreTextMarginLeft;
    }

    public int getSeeMoreTextMarginRight() {
        return this.seeMoreTextMarginRight;
    }

    public int getSeeMoreTextMarginTop() {
        return this.seeMoreTextMarginTop;
    }

    public double getSeeMoreTextSize() {
        return this.seeMoreSize;
    }

    public int getTimerBackgroundColor() {
        return this.timerBackgroundColor;
    }

    public int getTimerCornerRadius() {
        return this.timerCornerRadius;
    }

    public int getTimerTextColor() {
        return this.timerTextColor;
    }

    public int getTimerTextHeight() {
        return this.timerTextHeight;
    }

    public int getTimerTextWidth() {
        return this.timerTextWidth;
    }

    public int getTimerTextMarginBottom() {
        return this.timerTextMarginBottom;
    }

    public int getTimerTextMarginLeft() {
        return this.timerTextMarginLeft;
    }

    public int getTimerTextMarginRight() {
        return this.timerTextMarginRight;
    }

    public int getTimerTextMarginTop() {
        return this.timerTextMarginTop;
    }

    public long getCurrentTime() {
        return this.currentTime;
    }

    public long getFutureTime() {
        return this.futureTime;
    }

    public double getColonTextSize() {
        return this.colonTextSize;
    }

    public double getTimerTextSize() {
        return this.timerTextSize;
    }

    public boolean isShowSeeMoreText() {
        return this.showSeeMoreText;
    }

    public boolean isShowMilliSecond() {
        return this.showMilliSecond;
    }

    public int getMilliSecondDigitCount() {
        return this.milliSecondDigitCount;
    }

    public boolean isMilliSecondTextSelfAdaption() {
        return this.milliSecondTextSelfAdaption;
    }
}
