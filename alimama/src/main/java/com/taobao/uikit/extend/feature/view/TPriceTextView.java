package com.taobao.uikit.extend.feature.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.feature.view.TTextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TPriceTextView extends TTextView {
    private static final float DECIMAL_TEXT_FONT_RATIO = 0.75f;
    private static final int TEXT_COLOR = -45056;
    private float mDecimalNumRatio = 0.75f;
    private float mDollarRatio = 0.75f;
    private float mPrice = 0.0f;

    public TPriceTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet, i);
    }

    public TPriceTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet, 0);
    }

    public TPriceTextView(Context context) {
        super(context);
        init((AttributeSet) null, 0);
    }

    private void init(AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes;
        if (attributeSet != null && (obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.TPriceTextView)) != null) {
            this.mPrice = obtainStyledAttributes.getFloat(R.styleable.TPriceTextView_uik_price, this.mPrice);
            this.mDollarRatio = obtainStyledAttributes.getFloat(R.styleable.TPriceTextView_uik_dollar_ratio, this.mDollarRatio);
            this.mDecimalNumRatio = obtainStyledAttributes.getFloat(R.styleable.TPriceTextView_uik_decimal_ratio, this.mDecimalNumRatio);
            setTextColor(obtainStyledAttributes.getColor(R.styleable.TPriceTextView_android_textColor, TEXT_COLOR));
            setDollarRatio(this.mDollarRatio);
            setDecimalNumRatio(this.mDecimalNumRatio);
            setPrice(this.mPrice);
            obtainStyledAttributes.recycle();
        }
    }

    public void setPrice(String str) {
        Pattern compile = Pattern.compile("^[0-9]+[.]*[0-9]*");
        Pattern compile2 = Pattern.compile("(^([0-9]|[/?])+[.]*([0-9]|[/?])*)");
        Matcher matcher = compile.matcher(str);
        Matcher matcher2 = compile2.matcher(str);
        if (matcher.matches()) {
            setPrice(Float.valueOf(str).floatValue());
        } else if (matcher2.matches()) {
            setFestivalPrice(str);
        } else {
            setText(str);
        }
    }

    private void setFestivalPrice(String str) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        int indexOf = str.indexOf(".");
        if (indexOf >= 9) {
            int i = indexOf - 8;
            sb2.insert(0, str.substring(0, i));
            sb2.append(".");
            sb2.append(str.substring(i, indexOf - 7));
            sb.insert(0, "亿");
        } else if (indexOf >= 5) {
            int i2 = indexOf - 4;
            sb2.insert(0, str.substring(0, i2));
            sb2.append(".");
            sb2.append(str.substring(i2, indexOf - 3));
            sb.insert(0, "万");
        } else if (indexOf == -1) {
            sb2.insert(0, str);
        } else if (str.length() - indexOf >= 3) {
            sb2.insert(0, str.substring(0, indexOf + 3));
        } else {
            sb2.insert(0, str.length());
        }
        String sb3 = sb2.toString();
        if (sb3.endsWith(".00")) {
            sb3 = sb3.substring(0, sb2.length() - 3);
        } else if (sb3.endsWith(".0")) {
            sb3 = sb3.substring(0, sb3.length() - 2);
        }
        sb.insert(0, sb3);
        sb.insert(0, "¥ ");
        setText(sb.toString());
        if (1.0f != this.mDollarRatio) {
            SpannableString spannableString = new SpannableString(getText());
            if (spannableString.length() > 2) {
                spannableString.setSpan(new RelativeSizeSpan(this.mDollarRatio), 0, 2, 33);
            }
            setText(spannableString);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0087  */
    /* JADX WARNING: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setPrice(float r8) {
        /*
            r7 = this;
            r7.mPrice = r8
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r1 = 1287568416(0x4cbebc20, float:1.0E8)
            r2 = 0
            int r3 = (r8 > r1 ? 1 : (r8 == r1 ? 0 : -1))
            if (r3 < 0) goto L_0x0018
            float r8 = r8 / r1
            java.lang.String r1 = "#0.0"
            java.lang.String r3 = "亿"
            r0.insert(r2, r3)
            goto L_0x002a
        L_0x0018:
            r1 = 1176256512(0x461c4000, float:10000.0)
            int r3 = (r8 > r1 ? 1 : (r8 == r1 ? 0 : -1))
            if (r3 < 0) goto L_0x0028
            float r8 = r8 / r1
            java.lang.String r1 = "#0.0"
            java.lang.String r3 = "万"
            r0.insert(r2, r3)
            goto L_0x002a
        L_0x0028:
            java.lang.String r1 = "#0.00"
        L_0x002a:
            java.text.DecimalFormatSymbols r3 = new java.text.DecimalFormatSymbols
            r3.<init>()
            r4 = 46
            r3.setDecimalSeparator(r4)
            java.text.DecimalFormat r4 = new java.text.DecimalFormat
            r4.<init>(r1, r3)
            double r5 = (double) r8
            java.lang.String r8 = r4.format(r5)
            java.lang.String r1 = ".00"
            boolean r1 = r8.endsWith(r1)
            r3 = 2
            if (r1 == 0) goto L_0x0053
            int r1 = r8.length()
            int r1 = r1 + -3
            java.lang.String r8 = r8.substring(r2, r1)
        L_0x0051:
            r1 = 0
            goto L_0x0066
        L_0x0053:
            java.lang.String r1 = ".0"
            boolean r1 = r8.endsWith(r1)
            if (r1 == 0) goto L_0x0065
            int r1 = r8.length()
            int r1 = r1 - r3
            java.lang.String r8 = r8.substring(r2, r1)
            goto L_0x0051
        L_0x0065:
            r1 = 1
        L_0x0066:
            r0.insert(r2, r8)
            java.lang.String r8 = "¥ "
            r0.insert(r2, r8)
            if (r1 == 0) goto L_0x0078
            java.lang.String r8 = r0.toString()
            r7.fixSpan(r8)
            goto L_0x007f
        L_0x0078:
            java.lang.String r8 = r0.toString()
            r7.setText(r8)
        L_0x007f:
            r8 = 1065353216(0x3f800000, float:1.0)
            float r0 = r7.mDollarRatio
            int r8 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r8 == 0) goto L_0x00a5
            android.text.SpannableString r8 = new android.text.SpannableString
            java.lang.CharSequence r0 = r7.getText()
            r8.<init>(r0)
            int r0 = r8.length()
            if (r0 <= r3) goto L_0x00a2
            android.text.style.RelativeSizeSpan r0 = new android.text.style.RelativeSizeSpan
            float r1 = r7.mDollarRatio
            r0.<init>(r1)
            r1 = 33
            r8.setSpan(r0, r2, r3, r1)
        L_0x00a2:
            r7.setText(r8)
        L_0x00a5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.uikit.extend.feature.view.TPriceTextView.setPrice(float):void");
    }

    private void fixSpan(String str) {
        int indexOf;
        if (str == null && getText() != null) {
            str = getText().toString();
        }
        if (str != null && -1 != (indexOf = str.indexOf("."))) {
            int indexOf2 = str.indexOf("万");
            if (-1 == indexOf2) {
                indexOf2 = str.indexOf("亿");
            }
            if (-1 == indexOf2) {
                indexOf2 = str.length();
            }
            SpannableString spannableString = new SpannableString(str);
            spannableString.setSpan(new RelativeSizeSpan(this.mDecimalNumRatio), indexOf, indexOf2, 33);
            setText(spannableString);
        }
    }

    public void setTextSize(int i, float f) {
        super.setTextSize(i, f);
        fixSpan((String) null);
    }

    public void setDollarRatio(float f) {
        this.mDollarRatio = f;
        if (this.mPrice >= 0.0f) {
            setPrice(this.mPrice);
        }
    }

    public void setDecimalNumRatio(float f) {
        this.mDecimalNumRatio = f;
        if (this.mPrice >= 0.0f) {
            setPrice(this.mPrice);
        }
    }
}
