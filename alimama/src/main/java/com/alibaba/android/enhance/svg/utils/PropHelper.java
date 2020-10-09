package com.alibaba.android.enhance.svg.utils;

import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import com.alibaba.android.enhance.svg.SVGPlugin;
import com.taobao.uikit.feature.features.FeatureFactory;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropHelper {
    private static final Pattern percentageRegExp = Pattern.compile("^(-?\\d+(?:\\.\\d+)?)%$");

    @Nullable
    public static List<Float> resolveViewBoxFromString(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] split = str.split("(\\s*,\\s*)|(\\s+)");
        if (split.length <= 0 || split.length > 4) {
            return null;
        }
        ArrayList arrayList = new ArrayList(4);
        for (String str2 : split) {
            float floatValue = WXUtils.getFloat(str2, Float.valueOf(-1.0f)).floatValue();
            if (floatValue == -1.0f) {
                return null;
            }
            arrayList.add(Float.valueOf(floatValue));
        }
        if (arrayList.size() <= 0) {
            return null;
        }
        return arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0008, code lost:
        r4 = r4.trim();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String resolveIDFromString(java.lang.String r4) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r4)
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.String r4 = r4.trim()
            java.lang.String r0 = "url(#"
            int r0 = r4.indexOf(r0)
            r2 = -1
            if (r0 != r2) goto L_0x0016
            return r1
        L_0x0016:
            java.lang.String r3 = ")"
            int r3 = r4.indexOf(r3)
            if (r3 != r2) goto L_0x001f
            return r1
        L_0x001f:
            int r0 = r0 + 5
            java.lang.String r4 = r4.substring(r0, r3)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.svg.utils.PropHelper.resolveIDFromString(java.lang.String):java.lang.String");
    }

    public static String resolveHrefFromString(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String trim = str.trim();
        if (!trim.startsWith("#") || trim.length() <= 1) {
            return null;
        }
        return trim.substring(1);
    }

    public static float resolveFloatFromString(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0.0f;
        }
        String trim = str.trim();
        if (trim.indexOf(37) > 0) {
            return WXUtils.getFloat(trim.substring(0, trim.indexOf(37)), Float.valueOf(0.0f)).floatValue() / 100.0f;
        }
        return WXUtils.getFloat(trim, Float.valueOf(0.0f)).floatValue();
    }

    public static boolean isColorValid(@Nullable String str) {
        return !TextUtils.isEmpty(str) && !"none".equals(str) && !BuildConfig.buildJavascriptFrameworkVersion.equals(str);
    }

    @FloatRange(from = 0.0d, to = 1.0d)
    public static float parseOpacityFromString(@Nullable String str) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str.trim()) || "none".equals(str) || BuildConfig.buildJavascriptFrameworkVersion.equals(str)) {
            return 1.0f;
        }
        return WXUtils.getFloat(str);
    }

    public static float calculateScale(@Nullable WXSDKInstance wXSDKInstance) {
        int instanceViewPortWidth = wXSDKInstance != null ? wXSDKInstance.getInstanceViewPortWidth() : FeatureFactory.PRIORITY_ABOVE_NORMAL;
        if (wXSDKInstance != null && wXSDKInstance.getContext() != null) {
            return ((float) WXViewUtils.getScreenWidth(wXSDKInstance.getContext())) / ((float) instanceViewPortWidth);
        }
        WXLogUtils.e(SVGPlugin.TAG, "err! can not calculate scale");
        return 0.0f;
    }

    public static int getViewPort(@Nullable WXSDKInstance wXSDKInstance) {
        return wXSDKInstance != null ? wXSDKInstance.getInstanceViewPortWidth() : FeatureFactory.PRIORITY_ABOVE_NORMAL;
    }

    public static String pointsToDString(String str) {
        return str.replaceAll("(?:\\s*,\\s*|\\s+)", Operators.SPACE_STR);
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x00b1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static double fromRelative(java.lang.String r5, double r6, double r8, double r10, double r12) {
        /*
            boolean r12 = android.text.TextUtils.isEmpty(r5)
            if (r12 == 0) goto L_0x0009
            r5 = 0
            return r5
        L_0x0009:
            java.lang.String r5 = r5.trim()
            int r12 = r5.length()
            int r13 = r12 + -1
            if (r12 == 0) goto L_0x00d0
            java.lang.String r0 = "normal"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x001f
            goto L_0x00d0
        L_0x001f:
            int r0 = r5.codePointAt(r13)
            r1 = 37
            r2 = 0
            if (r0 != r1) goto L_0x003b
            java.lang.String r5 = r5.substring(r2, r13)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            double r10 = r5.doubleValue()
            r12 = 4636737291354636288(0x4059000000000000, double:100.0)
            double r10 = r10 / r12
            double r10 = r10 * r6
            double r10 = r10 + r8
            return r10
        L_0x003b:
            int r6 = r12 + -2
            if (r6 <= 0) goto L_0x00c4
            java.lang.String r7 = r5.substring(r6)
            r0 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r13 = -1
            int r3 = r7.hashCode()
            r4 = 3178(0xc6a, float:4.453E-42)
            if (r3 == r4) goto L_0x00a3
            r4 = 3240(0xca8, float:4.54E-42)
            if (r3 == r4) goto L_0x0099
            r4 = 3365(0xd25, float:4.715E-42)
            if (r3 == r4) goto L_0x008f
            r4 = 3488(0xda0, float:4.888E-42)
            if (r3 == r4) goto L_0x0085
            r4 = 3571(0xdf3, float:5.004E-42)
            if (r3 == r4) goto L_0x007b
            r4 = 3588(0xe04, float:5.028E-42)
            if (r3 == r4) goto L_0x0071
            r4 = 3592(0xe08, float:5.033E-42)
            if (r3 == r4) goto L_0x0067
            goto L_0x00ad
        L_0x0067:
            java.lang.String r3 = "px"
            boolean r7 = r7.equals(r3)
            if (r7 == 0) goto L_0x00ad
            r7 = 0
            goto L_0x00ae
        L_0x0071:
            java.lang.String r3 = "pt"
            boolean r7 = r7.equals(r3)
            if (r7 == 0) goto L_0x00ad
            r7 = 2
            goto L_0x00ae
        L_0x007b:
            java.lang.String r3 = "pc"
            boolean r7 = r7.equals(r3)
            if (r7 == 0) goto L_0x00ad
            r7 = 3
            goto L_0x00ae
        L_0x0085:
            java.lang.String r3 = "mm"
            boolean r7 = r7.equals(r3)
            if (r7 == 0) goto L_0x00ad
            r7 = 4
            goto L_0x00ae
        L_0x008f:
            java.lang.String r3 = "in"
            boolean r7 = r7.equals(r3)
            if (r7 == 0) goto L_0x00ad
            r7 = 6
            goto L_0x00ae
        L_0x0099:
            java.lang.String r3 = "em"
            boolean r7 = r7.equals(r3)
            if (r7 == 0) goto L_0x00ad
            r7 = 1
            goto L_0x00ae
        L_0x00a3:
            java.lang.String r3 = "cm"
            boolean r7 = r7.equals(r3)
            if (r7 == 0) goto L_0x00ad
            r7 = 5
            goto L_0x00ae
        L_0x00ad:
            r7 = -1
        L_0x00ae:
            switch(r7) {
                case 0: goto L_0x00b2;
                case 1: goto L_0x00b2;
                case 2: goto L_0x00b2;
                case 3: goto L_0x00b2;
                case 4: goto L_0x00b2;
                case 5: goto L_0x00b2;
                case 6: goto L_0x00b2;
                default: goto L_0x00b1;
            }
        L_0x00b1:
            r6 = r12
        L_0x00b2:
            java.lang.String r5 = r5.substring(r2, r6)
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            double r5 = r5.doubleValue()
            double r5 = r5 * r0
            double r5 = r5 * r10
            double r5 = r5 + r8
            return r5
        L_0x00c4:
            java.lang.Double r5 = java.lang.Double.valueOf(r5)
            double r5 = r5.doubleValue()
            double r5 = r5 * r10
            double r5 = r5 + r8
            return r5
        L_0x00d0:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.svg.utils.PropHelper.fromRelative(java.lang.String, double, double, double, double):double");
    }

    public static boolean isPercentage(String str) {
        return percentageRegExp.matcher(str).matches();
    }

    public static class PathParser {
        private static final Pattern DECIMAL_REG_EXP = Pattern.compile("(\\.\\d+)(?=-?\\.)");
        private static final Pattern PATH_REG_EXP = Pattern.compile("[a-df-z]|[\\-+]?(?:[\\d.]e[\\-+]?|[^\\s\\-+,a-z])+", 2);
        private List<List> mBezierCurves;
        private RectF mBoundingBox;
        private String mLastCommand;
        private Map<String, Double> mLastStartPoint;
        private String mLastValue;
        private Matcher mMatcher;
        private Path mPath;
        private float mPenDownX;
        private float mPenDownY;
        private float mPenX = 0.0f;
        private float mPenY = 0.0f;
        private boolean mPendDownSet = false;
        private float mPivotX = 0.0f;
        private float mPivotY = 0.0f;
        private float mScale = 1.0f;
        private final String mString;
        private boolean mValid = true;

        public PathParser(String str, float f) {
            this.mScale = f;
            this.mString = str;
        }

        /* JADX WARNING: Can't fix incorrect switch cases order */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void executeCommand(java.lang.String r9) {
            /*
                r8 = this;
                int r0 = r9.hashCode()
                switch(r0) {
                    case 65: goto L_0x00dd;
                    case 67: goto L_0x00d2;
                    case 72: goto L_0x00c8;
                    case 76: goto L_0x00be;
                    case 77: goto L_0x00b4;
                    case 81: goto L_0x00a9;
                    case 83: goto L_0x009e;
                    case 84: goto L_0x0093;
                    case 86: goto L_0x0089;
                    case 90: goto L_0x007e;
                    case 97: goto L_0x0072;
                    case 99: goto L_0x0066;
                    case 104: goto L_0x005b;
                    case 108: goto L_0x0050;
                    case 109: goto L_0x0045;
                    case 113: goto L_0x0039;
                    case 115: goto L_0x002d;
                    case 116: goto L_0x0021;
                    case 118: goto L_0x0016;
                    case 122: goto L_0x0009;
                    default: goto L_0x0007;
                }
            L_0x0007:
                goto L_0x00e8
            L_0x0009:
                java.lang.String r0 = "z"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 19
                goto L_0x00e9
            L_0x0016:
                java.lang.String r0 = "v"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 6
                goto L_0x00e9
            L_0x0021:
                java.lang.String r0 = "t"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 14
                goto L_0x00e9
            L_0x002d:
                java.lang.String r0 = "s"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 10
                goto L_0x00e9
            L_0x0039:
                java.lang.String r0 = "q"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 12
                goto L_0x00e9
            L_0x0045:
                java.lang.String r0 = "m"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 0
                goto L_0x00e9
            L_0x0050:
                java.lang.String r0 = "l"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 2
                goto L_0x00e9
            L_0x005b:
                java.lang.String r0 = "h"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 4
                goto L_0x00e9
            L_0x0066:
                java.lang.String r0 = "c"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 8
                goto L_0x00e9
            L_0x0072:
                java.lang.String r0 = "a"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 16
                goto L_0x00e9
            L_0x007e:
                java.lang.String r0 = "Z"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 18
                goto L_0x00e9
            L_0x0089:
                java.lang.String r0 = "V"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 7
                goto L_0x00e9
            L_0x0093:
                java.lang.String r0 = "T"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 15
                goto L_0x00e9
            L_0x009e:
                java.lang.String r0 = "S"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 11
                goto L_0x00e9
            L_0x00a9:
                java.lang.String r0 = "Q"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 13
                goto L_0x00e9
            L_0x00b4:
                java.lang.String r0 = "M"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 1
                goto L_0x00e9
            L_0x00be:
                java.lang.String r0 = "L"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 3
                goto L_0x00e9
            L_0x00c8:
                java.lang.String r0 = "H"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 5
                goto L_0x00e9
            L_0x00d2:
                java.lang.String r0 = "C"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 9
                goto L_0x00e9
            L_0x00dd:
                java.lang.String r0 = "A"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x00e8
                r0 = 17
                goto L_0x00e9
            L_0x00e8:
                r0 = -1
            L_0x00e9:
                r1 = 0
                switch(r0) {
                    case 0: goto L_0x022f;
                    case 1: goto L_0x0223;
                    case 2: goto L_0x0217;
                    case 3: goto L_0x020b;
                    case 4: goto L_0x0203;
                    case 5: goto L_0x01f9;
                    case 6: goto L_0x01f1;
                    case 7: goto L_0x01e7;
                    case 8: goto L_0x01ca;
                    case 9: goto L_0x01ac;
                    case 10: goto L_0x0197;
                    case 11: goto L_0x0182;
                    case 12: goto L_0x016d;
                    case 13: goto L_0x0158;
                    case 14: goto L_0x014b;
                    case 15: goto L_0x013e;
                    case 16: goto L_0x011c;
                    case 17: goto L_0x00fa;
                    case 18: goto L_0x00f5;
                    case 19: goto L_0x00f5;
                    default: goto L_0x00ed;
                }
            L_0x00ed:
                r8.mLastValue = r9
                java.lang.String r0 = r8.mLastCommand
                r8.executeCommand(r0)
                return
            L_0x00f5:
                r8.close()
                goto L_0x023a
            L_0x00fa:
                float r1 = r8.getNextFloat()
                float r2 = r8.getNextFloat()
                float r3 = r8.getNextFloat()
                boolean r4 = r8.getNextBoolean()
                boolean r5 = r8.getNextBoolean()
                float r6 = r8.getNextFloat()
                float r7 = r8.getNextFloat()
                r0 = r8
                r0.arcTo(r1, r2, r3, r4, r5, r6, r7)
                goto L_0x023a
            L_0x011c:
                float r1 = r8.getNextFloat()
                float r2 = r8.getNextFloat()
                float r3 = r8.getNextFloat()
                boolean r4 = r8.getNextBoolean()
                boolean r5 = r8.getNextBoolean()
                float r6 = r8.getNextFloat()
                float r7 = r8.getNextFloat()
                r0 = r8
                r0.arc(r1, r2, r3, r4, r5, r6, r7)
                goto L_0x023a
            L_0x013e:
                float r0 = r8.getNextFloat()
                float r1 = r8.getNextFloat()
                r8.smoothQuadraticBezierCurveTo(r0, r1)
                goto L_0x023a
            L_0x014b:
                float r0 = r8.getNextFloat()
                float r1 = r8.getNextFloat()
                r8.smoothQuadraticBezierCurve(r0, r1)
                goto L_0x023a
            L_0x0158:
                float r0 = r8.getNextFloat()
                float r1 = r8.getNextFloat()
                float r2 = r8.getNextFloat()
                float r3 = r8.getNextFloat()
                r8.quadraticBezierCurveTo(r0, r1, r2, r3)
                goto L_0x023a
            L_0x016d:
                float r0 = r8.getNextFloat()
                float r1 = r8.getNextFloat()
                float r2 = r8.getNextFloat()
                float r3 = r8.getNextFloat()
                r8.quadraticBezierCurve(r0, r1, r2, r3)
                goto L_0x023a
            L_0x0182:
                float r0 = r8.getNextFloat()
                float r1 = r8.getNextFloat()
                float r2 = r8.getNextFloat()
                float r3 = r8.getNextFloat()
                r8.smoothCurveTo(r0, r1, r2, r3)
                goto L_0x023a
            L_0x0197:
                float r0 = r8.getNextFloat()
                float r1 = r8.getNextFloat()
                float r2 = r8.getNextFloat()
                float r3 = r8.getNextFloat()
                r8.smoothCurve(r0, r1, r2, r3)
                goto L_0x023a
            L_0x01ac:
                float r1 = r8.getNextFloat()
                float r2 = r8.getNextFloat()
                float r3 = r8.getNextFloat()
                float r4 = r8.getNextFloat()
                float r5 = r8.getNextFloat()
                float r6 = r8.getNextFloat()
                r0 = r8
                r0.curveTo(r1, r2, r3, r4, r5, r6)
                goto L_0x023a
            L_0x01ca:
                float r1 = r8.getNextFloat()
                float r2 = r8.getNextFloat()
                float r3 = r8.getNextFloat()
                float r4 = r8.getNextFloat()
                float r5 = r8.getNextFloat()
                float r6 = r8.getNextFloat()
                r0 = r8
                r0.curve(r1, r2, r3, r4, r5, r6)
                goto L_0x023a
            L_0x01e7:
                float r0 = r8.mPenX
                float r1 = r8.getNextFloat()
                r8.lineTo(r0, r1)
                goto L_0x023a
            L_0x01f1:
                float r0 = r8.getNextFloat()
                r8.line(r1, r0)
                goto L_0x023a
            L_0x01f9:
                float r0 = r8.getNextFloat()
                float r1 = r8.mPenY
                r8.lineTo(r0, r1)
                goto L_0x023a
            L_0x0203:
                float r0 = r8.getNextFloat()
                r8.line(r0, r1)
                goto L_0x023a
            L_0x020b:
                float r0 = r8.getNextFloat()
                float r1 = r8.getNextFloat()
                r8.lineTo(r0, r1)
                goto L_0x023a
            L_0x0217:
                float r0 = r8.getNextFloat()
                float r1 = r8.getNextFloat()
                r8.line(r0, r1)
                goto L_0x023a
            L_0x0223:
                float r0 = r8.getNextFloat()
                float r1 = r8.getNextFloat()
                r8.moveTo(r0, r1)
                goto L_0x023a
            L_0x022f:
                float r0 = r8.getNextFloat()
                float r1 = r8.getNextFloat()
                r8.move(r0, r1)
            L_0x023a:
                r8.mLastCommand = r9
                java.lang.String r0 = "m"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x0249
                java.lang.String r0 = "l"
                r8.mLastCommand = r0
                goto L_0x0255
            L_0x0249:
                java.lang.String r0 = "M"
                boolean r0 = r9.equals(r0)
                if (r0 == 0) goto L_0x0255
                java.lang.String r0 = "L"
                r8.mLastCommand = r0
            L_0x0255:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.svg.utils.PropHelper.PathParser.executeCommand(java.lang.String):void");
        }

        public Path getPath() {
            return getPath((RectF) null);
        }

        public Path getPath(RectF rectF) {
            this.mPath = new Path();
            this.mBezierCurves = new ArrayList();
            this.mMatcher = PATH_REG_EXP.matcher(DECIMAL_REG_EXP.matcher(this.mString).replaceAll("$1,"));
            this.mBoundingBox = rectF;
            while (this.mMatcher.find() && this.mValid) {
                executeCommand(this.mMatcher.group());
            }
            this.mBoundingBox = null;
            return this.mPath;
        }

        private Map<String, Double> getPointMap(float f, float f2) {
            HashMap hashMap = new HashMap();
            hashMap.put(Constants.Name.X, Double.valueOf((double) (f * getScaleX())));
            hashMap.put(Constants.Name.Y, Double.valueOf((double) (f2 * getScaleY())));
            return hashMap;
        }

        private float getScaleX() {
            if (this.mBoundingBox != null) {
                return this.mBoundingBox.width();
            }
            return this.mScale;
        }

        private float getScaleY() {
            if (this.mBoundingBox != null) {
                return this.mBoundingBox.height();
            }
            return this.mScale;
        }

        private Map<String, Double> clonePointMap(Map<String, Double> map) {
            HashMap hashMap = new HashMap();
            hashMap.put(Constants.Name.X, map.get(Constants.Name.X));
            hashMap.put(Constants.Name.Y, map.get(Constants.Name.Y));
            return hashMap;
        }

        private boolean getNextBoolean() {
            if (this.mMatcher.find()) {
                return this.mMatcher.group().equals("1");
            }
            this.mValid = false;
            this.mPath = new Path();
            return false;
        }

        private float getNextFloat() {
            if (this.mLastValue != null) {
                String str = this.mLastValue;
                this.mLastValue = null;
                float f = WXUtils.getFloat(str);
                if (Float.isNaN(f)) {
                    return 0.0f;
                }
                return f;
            } else if (this.mMatcher.find()) {
                float f2 = WXUtils.getFloat(this.mMatcher.group());
                if (Float.isNaN(f2)) {
                    return 0.0f;
                }
                return f2;
            } else {
                this.mValid = false;
                this.mPath = new Path();
                return 0.0f;
            }
        }

        private void move(float f, float f2) {
            moveTo(f + this.mPenX, f2 + this.mPenY);
        }

        private void moveTo(float f, float f2) {
            this.mPenX = f;
            this.mPivotX = f;
            this.mPenY = f2;
            this.mPivotY = f2;
            this.mPath.moveTo(getScaleX() * f, getScaleY() * f2);
            this.mLastStartPoint = getPointMap(f, f2);
            ArrayList arrayList = new ArrayList();
            arrayList.add(getPointMap(f, f2));
            this.mBezierCurves.add(arrayList);
        }

        private void line(float f, float f2) {
            lineTo(f + this.mPenX, f2 + this.mPenY);
        }

        private void lineTo(float f, float f2) {
            setPenDown();
            this.mPenX = f;
            this.mPivotX = f;
            this.mPenY = f2;
            this.mPivotY = f2;
            this.mPath.lineTo(getScaleX() * f, getScaleY() * f2);
            ArrayList arrayList = new ArrayList();
            arrayList.add(getPointMap(f, f2));
            arrayList.add(getPointMap(f, f2));
            arrayList.add(getPointMap(f, f2));
            this.mBezierCurves.add(arrayList);
        }

        private void curve(float f, float f2, float f3, float f4, float f5, float f6) {
            curveTo(f + this.mPenX, f2 + this.mPenY, f3 + this.mPenX, f4 + this.mPenY, f5 + this.mPenX, f6 + this.mPenY);
        }

        private void curveTo(float f, float f2, float f3, float f4, float f5, float f6) {
            this.mPivotX = f3;
            this.mPivotY = f4;
            cubicTo(f, f2, f3, f4, f5, f6);
        }

        private void cubicTo(float f, float f2, float f3, float f4, float f5, float f6) {
            setPenDown();
            this.mPenX = f5;
            this.mPenY = f6;
            this.mPath.cubicTo(getScaleX() * f, getScaleY() * f2, getScaleX() * f3, getScaleY() * f4, getScaleX() * f5, getScaleY() * f6);
            ArrayList arrayList = new ArrayList();
            arrayList.add(getPointMap(f, f2));
            arrayList.add(getPointMap(f3, f4));
            arrayList.add(getPointMap(f5, f6));
            this.mBezierCurves.add(arrayList);
        }

        private void smoothCurve(float f, float f2, float f3, float f4) {
            smoothCurveTo(f + this.mPenX, f2 + this.mPenY, f3 + this.mPenX, f4 + this.mPenY);
        }

        private void smoothCurveTo(float f, float f2, float f3, float f4) {
            this.mPivotX = f;
            this.mPivotY = f2;
            cubicTo((this.mPenX * 2.0f) - this.mPivotX, (this.mPenY * 2.0f) - this.mPivotY, f, f2, f3, f4);
        }

        private void quadraticBezierCurve(float f, float f2, float f3, float f4) {
            quadraticBezierCurveTo(f + this.mPenX, f2 + this.mPenY, f3 + this.mPenX, f4 + this.mPenY);
        }

        private void quadraticBezierCurveTo(float f, float f2, float f3, float f4) {
            this.mPivotX = f;
            this.mPivotY = f2;
            float f5 = f * 2.0f;
            float f6 = f2 * 2.0f;
            float f7 = (this.mPenX + f5) / 3.0f;
            float f8 = (this.mPenY + f6) / 3.0f;
            cubicTo(f7, f8, (f3 + f5) / 3.0f, (f4 + f6) / 3.0f, f3, f4);
        }

        private void smoothQuadraticBezierCurve(float f, float f2) {
            smoothQuadraticBezierCurveTo(f + this.mPenX, f2 + this.mPenY);
        }

        private void smoothQuadraticBezierCurveTo(float f, float f2) {
            quadraticBezierCurveTo((this.mPenX * 2.0f) - this.mPivotX, (this.mPenY * 2.0f) - this.mPivotY, f, f2);
        }

        private void arc(float f, float f2, float f3, boolean z, boolean z2, float f4, float f5) {
            arcTo(f, f2, f3, z, z2, f4 + this.mPenX, f5 + this.mPenY);
        }

        private void arcTo(float f, float f2, float f3, boolean z, boolean z2, float f4, float f5) {
            float f6;
            float f7;
            float f8;
            boolean z3 = z;
            boolean z4 = z2;
            float f9 = f4;
            float f10 = f5;
            float f11 = this.mPenX;
            float f12 = this.mPenY;
            float abs = Math.abs(f2 == 0.0f ? f == 0.0f ? f10 - f12 : f : f2);
            float abs2 = Math.abs(f == 0.0f ? f9 - f11 : f);
            if (abs2 == 0.0f || abs == 0.0f || (f9 == f11 && f10 == f12)) {
                lineTo(f9, f10);
                return;
            }
            float radians = (float) Math.toRadians((double) f3);
            double d = (double) radians;
            float cos = (float) Math.cos(d);
            float sin = (float) Math.sin(d);
            float f13 = f9 - f11;
            float f14 = f10 - f12;
            float f15 = ((cos * f13) / 2.0f) + ((sin * f14) / 2.0f);
            float f16 = -sin;
            float f17 = ((f16 * f13) / 2.0f) + ((cos * f14) / 2.0f);
            float f18 = abs2 * abs2;
            float f19 = f18 * abs * abs;
            float f20 = abs * abs * f15 * f15;
            float f21 = f18 * f17 * f17;
            float f22 = (f19 - f21) - f20;
            if (f22 < 0.0f) {
                float sqrt = (float) Math.sqrt((double) (1.0f - (f22 / f19)));
                abs *= sqrt;
                f6 = f13 / 2.0f;
                f7 = abs2 * sqrt;
                f8 = f14 / 2.0f;
                boolean z5 = z2;
            } else {
                float sqrt2 = (float) Math.sqrt((double) (f22 / (f21 + f20)));
                if (z3 == z2) {
                    sqrt2 = -sqrt2;
                }
                float f23 = (((-sqrt2) * f17) * abs2) / abs;
                float f24 = ((sqrt2 * f15) * abs) / abs2;
                f6 = ((cos * f23) - (sin * f24)) + (f13 / 2.0f);
                f7 = abs2;
                f8 = (f14 / 2.0f) + (f23 * sin) + (f24 * cos);
            }
            float f25 = cos / f7;
            float f26 = sin / f7;
            float f27 = f16 / abs;
            float f28 = cos / abs;
            float f29 = -f6;
            float f30 = -f8;
            float f31 = radians;
            float f32 = abs;
            float f33 = f7;
            float atan2 = (float) Math.atan2((double) ((f27 * f29) + (f28 * f30)), (double) ((f29 * f25) + (f30 * f26)));
            float f34 = f13 - f6;
            float f35 = f14 - f8;
            float atan22 = (float) Math.atan2((double) ((f27 * f34) + (f28 * f35)), (double) ((f25 * f34) + (f26 * f35)));
            float f36 = f6 + f11;
            float f37 = f8 + f12;
            float f38 = f13 + f11;
            float f39 = f14 + f12;
            setPenDown();
            this.mPivotX = f38;
            this.mPenX = f38;
            this.mPivotY = f39;
            this.mPenY = f39;
            if (f33 == f32 && f31 == 0.0f) {
                float degrees = (float) Math.toDegrees((double) atan2);
                float abs3 = Math.abs((degrees - ((float) Math.toDegrees((double) atan22))) % 360.0f);
                if (z) {
                    if (abs3 < 180.0f) {
                        abs3 = 360.0f - abs3;
                    }
                } else if (abs3 > 180.0f) {
                    abs3 = 360.0f - abs3;
                }
                if (!z2) {
                    abs3 = -abs3;
                }
                this.mPath.arcTo(new RectF((f36 - f33) * getScaleX(), (f37 - f33) * getScaleY(), (f36 + f33) * getScaleX(), (f37 + f33) * getScaleY()), degrees, abs3);
                return;
            }
            boolean z6 = z2;
            arcToBezier(f36, f37, f33, f32, atan2, atan22, z2, f31);
        }

        private void close() {
            if (this.mPendDownSet) {
                this.mPenX = this.mPenDownX;
                this.mPenY = this.mPenDownY;
                this.mPendDownSet = false;
                this.mPath.close();
                ArrayList arrayList = new ArrayList();
                arrayList.add(clonePointMap(this.mLastStartPoint));
                arrayList.add(clonePointMap(this.mLastStartPoint));
                arrayList.add(clonePointMap(this.mLastStartPoint));
                this.mBezierCurves.add(arrayList);
            }
        }

        private void arcToBezier(float f, float f2, float f3, float f4, float f5, float f6, boolean z, float f7) {
            float f8 = f5;
            double d = (double) f7;
            float cos = (float) Math.cos(d);
            float sin = (float) Math.sin(d);
            float f9 = cos * f3;
            float f10 = (-sin) * f4;
            float f11 = sin * f3;
            float f12 = cos * f4;
            float f13 = f6 - f8;
            if (f13 < 0.0f && z) {
                double d2 = (double) f13;
                Double.isNaN(d2);
                f13 = (float) (d2 + 6.283185307179586d);
            } else if (f13 > 0.0f && !z) {
                double d3 = (double) f13;
                Double.isNaN(d3);
                f13 = (float) (d3 - 6.283185307179586d);
            }
            double d4 = (double) f13;
            Double.isNaN(d4);
            int ceil = (int) Math.ceil(Math.abs(round(d4 / 1.5707963267948966d, 4)));
            float f14 = f13 / ((float) ceil);
            float tan = (float) (Math.tan((double) (f14 / 4.0f)) * 1.3333333333333333d);
            double d5 = (double) f8;
            float cos2 = (float) Math.cos(d5);
            float sin2 = (float) Math.sin(d5);
            int i = 0;
            while (i < ceil) {
                float f15 = cos2 - (tan * sin2);
                float f16 = sin2 + (cos2 * tan);
                float f17 = f8 + f14;
                float f18 = f14;
                int i2 = ceil;
                double d6 = (double) f17;
                int i3 = i;
                cos2 = (float) Math.cos(d6);
                float sin3 = (float) Math.sin(d6);
                float f19 = (tan * sin3) + cos2;
                float f20 = sin3 - (tan * cos2);
                float f21 = f17;
                Path path = this.mPath;
                path.cubicTo(getScaleX() * (f + (f9 * f15) + (f10 * f16)), (f2 + (f15 * f11) + (f16 * f12)) * getScaleY(), (f + (f9 * f19) + (f10 * f20)) * getScaleX(), (f2 + (f19 * f11) + (f20 * f12)) * getScaleY(), (f + (f9 * cos2) + (f10 * sin3)) * getScaleX(), (f2 + (f11 * cos2) + (f12 * sin3)) * getScaleY());
                i = i3 + 1;
                sin2 = sin3;
                ceil = i2;
                f14 = f18;
                f8 = f21;
            }
        }

        private void setPenDown() {
            if (!this.mPendDownSet) {
                this.mPenDownX = this.mPenX;
                this.mPenDownY = this.mPenY;
                this.mPendDownSet = true;
            }
        }

        private double round(double d, int i) {
            double pow = Math.pow(10.0d, (double) i);
            double round = (double) Math.round(d * pow);
            Double.isNaN(round);
            return round / pow;
        }
    }
}
