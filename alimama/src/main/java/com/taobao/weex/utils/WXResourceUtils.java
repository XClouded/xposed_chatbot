package com.taobao.weex.utils;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.SingleFunctionParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class WXResourceUtils {
    private static final int COLOR_RANGE = 255;
    /* access modifiers changed from: private */
    public static final SingleFunctionParser.NonUniformMapper<Number> FUNCTIONAL_RGBA_MAPPER = new SingleFunctionParser.NonUniformMapper<Number>() {
        public List<Number> map(List<String> list) {
            ArrayList arrayList = new ArrayList(4);
            int i = 0;
            while (i < 3) {
                int parseUnitOrPercent = WXUtils.parseUnitOrPercent(list.get(i), 255);
                if (parseUnitOrPercent < 0) {
                    parseUnitOrPercent = 0;
                } else if (parseUnitOrPercent > 255) {
                    parseUnitOrPercent = 255;
                }
                arrayList.add(Integer.valueOf(parseUnitOrPercent));
                i++;
            }
            arrayList.add(Float.valueOf(list.get(i)));
            return arrayList;
        }
    };
    /* access modifiers changed from: private */
    public static final SingleFunctionParser.FlatMapper<Integer> FUNCTIONAL_RGB_MAPPER = new SingleFunctionParser.FlatMapper<Integer>() {
        public Integer map(String str) {
            int i = 255;
            int parseUnitOrPercent = WXUtils.parseUnitOrPercent(str, 255);
            if (parseUnitOrPercent < 0) {
                i = 0;
            } else if (parseUnitOrPercent <= 255) {
                i = parseUnitOrPercent;
            }
            return Integer.valueOf(i);
        }
    };
    private static final int HEX = 16;
    private static final String RGB = "rgb";
    private static final String RGBA = "rgba";
    private static final int RGBA_SIZE = 4;
    private static final int RGB_SIZE = 3;
    /* access modifiers changed from: private */
    public static final Map<String, Integer> colorMap = new HashMap();

    enum ColorConvertHandler {
        NAMED_COLOR_HANDLER {
            /* access modifiers changed from: package-private */
            @NonNull
            public Pair<Boolean, Integer> handle(String str) {
                if (WXResourceUtils.colorMap.containsKey(str)) {
                    return new Pair<>(Boolean.TRUE, WXResourceUtils.colorMap.get(str));
                }
                return new Pair<>(Boolean.FALSE, 0);
            }
        },
        RGB_HANDLER {
            /* access modifiers changed from: package-private */
            @NonNull
            public Pair<Boolean, Integer> handle(String str) {
                if (str.length() == 4) {
                    int parseInt = Integer.parseInt(str.substring(1, 2), 16);
                    int parseInt2 = Integer.parseInt(str.substring(2, 3), 16);
                    int parseInt3 = Integer.parseInt(str.substring(3, 4), 16);
                    return new Pair<>(Boolean.TRUE, Integer.valueOf(Color.rgb(parseInt + (parseInt << 4), parseInt2 + (parseInt2 << 4), parseInt3 + (parseInt3 << 4))));
                } else if (str.length() == 7 || str.length() == 9) {
                    return new Pair<>(Boolean.TRUE, Integer.valueOf(Color.parseColor(str)));
                } else {
                    return new Pair<>(Boolean.FALSE, 0);
                }
            }
        },
        FUNCTIONAL_RGB_HANDLER {
            /* access modifiers changed from: package-private */
            @NonNull
            public Pair<Boolean, Integer> handle(String str) {
                List parse = new SingleFunctionParser(str, WXResourceUtils.FUNCTIONAL_RGB_MAPPER).parse(WXResourceUtils.RGB);
                if (parse == null || parse.size() != 3) {
                    return new Pair<>(Boolean.FALSE, 0);
                }
                return new Pair<>(Boolean.TRUE, Integer.valueOf(Color.rgb(((Integer) parse.get(0)).intValue(), ((Integer) parse.get(1)).intValue(), ((Integer) parse.get(2)).intValue())));
            }
        },
        FUNCTIONAL_RGBA_HANDLER {
            /* access modifiers changed from: package-private */
            @NonNull
            public Pair<Boolean, Integer> handle(String str) {
                List parse = new SingleFunctionParser(str, WXResourceUtils.FUNCTIONAL_RGBA_MAPPER).parse(WXResourceUtils.RGBA);
                if (parse.size() == 4) {
                    return new Pair<>(Boolean.TRUE, Integer.valueOf(Color.argb(ColorConvertHandler.parseAlpha(((Number) parse.get(3)).floatValue()), ((Number) parse.get(0)).intValue(), ((Number) parse.get(1)).intValue(), ((Number) parse.get(2)).intValue())));
                }
                return new Pair<>(Boolean.FALSE, 0);
            }
        };

        /* access modifiers changed from: private */
        public static int parseAlpha(float f) {
            return (int) (f * 255.0f);
        }

        /* access modifiers changed from: package-private */
        @NonNull
        public abstract Pair<Boolean, Integer> handle(String str);
    }

    static {
        colorMap.put("aliceblue", -984833);
        colorMap.put("antiquewhite", -332841);
        colorMap.put("aqua", -16711681);
        colorMap.put("aquamarine", -8388652);
        colorMap.put("azure", -983041);
        colorMap.put("beige", -657956);
        colorMap.put("bisque", -6972);
        colorMap.put("black", -16777216);
        colorMap.put("blanchedalmond", -5171);
        colorMap.put("blue", -16776961);
        colorMap.put("blueviolet", -7722014);
        colorMap.put("brown", -5952982);
        colorMap.put("burlywood", -2180985);
        colorMap.put("cadetblue", -10510688);
        colorMap.put("chartreuse", -8388864);
        colorMap.put("chocolate", -2987746);
        colorMap.put("coral", -32944);
        colorMap.put("cornflowerblue", -10185235);
        colorMap.put("cornsilk", -1828);
        colorMap.put("crimson", -2354116);
        colorMap.put("cyan", -16711681);
        colorMap.put("darkblue", -16777077);
        colorMap.put("darkcyan", -16741493);
        colorMap.put("darkgoldenrod", -4684277);
        colorMap.put("darkgray", -5658199);
        colorMap.put("darkgreen", -16751616);
        colorMap.put("darkkhaki", -4343957);
        colorMap.put("darkmagenta", -7667573);
        colorMap.put("darkolivegreen", -11179217);
        colorMap.put("darkorange", -29696);
        colorMap.put("darkorchid", -6737204);
        colorMap.put("darkred", -7667712);
        colorMap.put("darksalmon", -1468806);
        colorMap.put("darkseagreen", -7357297);
        colorMap.put("darkslateblue", -12042869);
        colorMap.put("darkslategray", -13676721);
        colorMap.put("darkslategrey", -13676721);
        colorMap.put("darkturquoise", -16724271);
        colorMap.put("darkviolet", -7077677);
        colorMap.put("deeppink", -60269);
        colorMap.put("deepskyblue", -16728065);
        colorMap.put("dimgray", -9868951);
        colorMap.put("dimgrey", -9868951);
        colorMap.put("dodgerblue", -14774017);
        colorMap.put("firebrick", -5103070);
        colorMap.put("floralwhite", -1296);
        colorMap.put("forestgreen", -14513374);
        colorMap.put("fuchsia", -65281);
        colorMap.put("gainsboro", -2302756);
        colorMap.put("ghostwhite", -460545);
        colorMap.put("gold", -10496);
        colorMap.put("goldenrod", -2448096);
        colorMap.put("gray", -8355712);
        colorMap.put("grey", -8355712);
        colorMap.put("green", -16744448);
        colorMap.put("greenyellow", -5374161);
        colorMap.put("honeydew", -983056);
        colorMap.put("hotpink", -38476);
        colorMap.put("indianred", -3318692);
        colorMap.put("indigo", -11861886);
        colorMap.put("ivory", -16);
        colorMap.put("khaki", -989556);
        colorMap.put("lavender", -1644806);
        colorMap.put("lavenderblush", -3851);
        colorMap.put("lawngreen", -8586240);
        colorMap.put("lemonchiffon", -1331);
        colorMap.put("lightblue", -5383962);
        colorMap.put("lightcoral", -1015680);
        colorMap.put("lightcyan", -2031617);
        colorMap.put("lightgoldenrodyellow", -329006);
        colorMap.put("lightgray", -2894893);
        colorMap.put("lightgrey", -2894893);
        colorMap.put("lightgreen", -7278960);
        colorMap.put("lightpink", -18751);
        colorMap.put("lightsalmon", -24454);
        colorMap.put("lightseagreen", -14634326);
        colorMap.put("lightskyblue", -7876870);
        colorMap.put("lightslategray", -8943463);
        colorMap.put("lightslategrey", -8943463);
        colorMap.put("lightsteelblue", -5192482);
        colorMap.put("lightyellow", -32);
        colorMap.put("lime", -16711936);
        colorMap.put("limegreen", -13447886);
        colorMap.put("linen", -331546);
        colorMap.put("magenta", -65281);
        colorMap.put("maroon", -8388608);
        colorMap.put("mediumaquamarine", -10039894);
        colorMap.put("mediumblue", -16777011);
        colorMap.put("mediumorchid", -4565549);
        colorMap.put("mediumpurple", -7114533);
        colorMap.put("mediumseagreen", -12799119);
        colorMap.put("mediumslateblue", -8689426);
        colorMap.put("mediumspringgreen", -16713062);
        colorMap.put("mediumturquoise", -12004916);
        colorMap.put("mediumvioletred", -3730043);
        colorMap.put("midnightblue", -15132304);
        colorMap.put("mintcream", -655366);
        colorMap.put("mistyrose", -6943);
        colorMap.put("moccasin", -6987);
        colorMap.put("navajowhite", -8531);
        colorMap.put("navy", -16777088);
        colorMap.put("oldlace", -133658);
        colorMap.put("olive", -8355840);
        colorMap.put("olivedrab", -9728477);
        colorMap.put("orange", -23296);
        colorMap.put("orangered", -47872);
        colorMap.put("orchid", -2461482);
        colorMap.put("palegoldenrod", -1120086);
        colorMap.put("palegreen", -6751336);
        colorMap.put("paleturquoise", -5247250);
        colorMap.put("palevioletred", -2396013);
        colorMap.put("papayawhip", -4139);
        colorMap.put("peachpuff", -9543);
        colorMap.put("peru", -3308225);
        colorMap.put("pink", -16181);
        colorMap.put("plum", -2252579);
        colorMap.put("powderblue", -5185306);
        colorMap.put("purple", -8388480);
        colorMap.put("rebeccapurple", -10079335);
        colorMap.put("red", Integer.valueOf(SupportMenu.CATEGORY_MASK));
        colorMap.put("rosybrown", -4419697);
        colorMap.put("royalblue", -12490271);
        colorMap.put("saddlebrown", -7650029);
        colorMap.put("salmon", -360334);
        colorMap.put("sandybrown", -744352);
        colorMap.put("seagreen", -13726889);
        colorMap.put("seashell", -2578);
        colorMap.put("sienna", -6270419);
        colorMap.put("silver", -4144960);
        colorMap.put("skyblue", -7876885);
        colorMap.put("slateblue", -9807155);
        colorMap.put("slategray", -9404272);
        colorMap.put("slategrey", -9404272);
        colorMap.put("snow", -1286);
        colorMap.put("springgreen", -16711809);
        colorMap.put("steelblue", -12156236);
        colorMap.put("tan", -2968436);
        colorMap.put("teal", -16744320);
        colorMap.put("thistle", -2572328);
        colorMap.put("tomato", -40121);
        colorMap.put("turquoise", -12525360);
        colorMap.put("violet", -1146130);
        colorMap.put("wheat", -663885);
        colorMap.put("white", -1);
        colorMap.put("whitesmoke", -657931);
        colorMap.put("yellow", Integer.valueOf(InputDeviceCompat.SOURCE_ANY));
        colorMap.put("yellowgreen", -6632142);
        colorMap.put("transparent", 0);
    }

    public static int getColor(String str) {
        return getColor(str, Integer.MIN_VALUE);
    }

    public static int getColor(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            return i;
        }
        String trim = str.trim();
        Integer num = WXUtils.sCache.get(trim);
        if (num != null) {
            return num.intValue();
        }
        ColorConvertHandler[] values = ColorConvertHandler.values();
        int length = values.length;
        int i2 = 0;
        while (i2 < length) {
            try {
                Pair<Boolean, Integer> handle = values[i2].handle(trim);
                if (((Boolean) handle.first).booleanValue()) {
                    int intValue = ((Integer) handle.second).intValue();
                    try {
                        WXUtils.sCache.put(trim, Integer.valueOf(intValue));
                        return intValue;
                    } catch (RuntimeException e) {
                        int i3 = intValue;
                        e = e;
                        i = i3;
                    }
                } else {
                    continue;
                    i2++;
                }
            } catch (RuntimeException e2) {
                e = e2;
                WXLogUtils.v("Color_Parser", WXLogUtils.getStackTrace(e));
                i2++;
            }
        }
        return i;
    }

    public static Shader getShader(String str, float f, float f2) {
        List<String> parseGradientValues = parseGradientValues(str);
        if (parseGradientValues == null || parseGradientValues.size() != 3) {
            return null;
        }
        float[] parseGradientDirection = parseGradientDirection(parseGradientValues.get(0), f, f2);
        return new LinearGradient(parseGradientDirection[0], parseGradientDirection[1], parseGradientDirection[2], parseGradientDirection[3], getColor(parseGradientValues.get(1), -1), getColor(parseGradientValues.get(2), -1), Shader.TileMode.CLAMP);
    }

    @NonNull
    private static List<String> parseGradientValues(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        str.trim();
        if (!str.startsWith("linear-gradient")) {
            return null;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str.substring(str.indexOf(Operators.BRACKET_START_STR) + 1, str.lastIndexOf(Operators.BRACKET_END_STR)), ",");
        ArrayList arrayList = new ArrayList();
        while (true) {
            String str2 = null;
            while (stringTokenizer.hasMoreTokens()) {
                String nextToken = stringTokenizer.nextToken();
                if (nextToken.contains(Operators.BRACKET_START_STR)) {
                    str2 = nextToken + ",";
                } else if (nextToken.contains(Operators.BRACKET_END_STR)) {
                    arrayList.add(str2 + nextToken);
                } else if (str2 != null) {
                    str2 = str2 + nextToken + ",";
                } else {
                    arrayList.add(nextToken);
                }
            }
            return arrayList;
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x002d, code lost:
        if (r8.equals("tobottomright") != false) goto L_0x0063;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static float[] parseGradientDirection(java.lang.String r8, float r9, float r10) {
        /*
            r0 = 4
            float[] r1 = new float[r0]
            r1 = {0, 0, 0, 0} // fill-array
            boolean r2 = android.text.TextUtils.isEmpty(r8)
            if (r2 != 0) goto L_0x001a
            java.lang.String r2 = "\\s*"
            java.lang.String r3 = ""
            java.lang.String r8 = r8.replaceAll(r2, r3)
            java.util.Locale r2 = java.util.Locale.ROOT
            java.lang.String r8 = r8.toLowerCase(r2)
        L_0x001a:
            r2 = -1
            int r3 = r8.hashCode()
            r4 = 3
            r5 = 2
            r6 = 1
            r7 = 0
            switch(r3) {
                case -1352032154: goto L_0x0058;
                case -1137407871: goto L_0x004e;
                case -868157182: goto L_0x0044;
                case -172068863: goto L_0x003a;
                case 110550266: goto L_0x0030;
                case 1176531318: goto L_0x0027;
                default: goto L_0x0026;
            }
        L_0x0026:
            goto L_0x0062
        L_0x0027:
            java.lang.String r3 = "tobottomright"
            boolean r8 = r8.equals(r3)
            if (r8 == 0) goto L_0x0062
            goto L_0x0063
        L_0x0030:
            java.lang.String r0 = "totop"
            boolean r8 = r8.equals(r0)
            if (r8 == 0) goto L_0x0062
            r0 = 3
            goto L_0x0063
        L_0x003a:
            java.lang.String r0 = "totopleft"
            boolean r8 = r8.equals(r0)
            if (r8 == 0) goto L_0x0062
            r0 = 5
            goto L_0x0063
        L_0x0044:
            java.lang.String r0 = "toleft"
            boolean r8 = r8.equals(r0)
            if (r8 == 0) goto L_0x0062
            r0 = 1
            goto L_0x0063
        L_0x004e:
            java.lang.String r0 = "toright"
            boolean r8 = r8.equals(r0)
            if (r8 == 0) goto L_0x0062
            r0 = 0
            goto L_0x0063
        L_0x0058:
            java.lang.String r0 = "tobottom"
            boolean r8 = r8.equals(r0)
            if (r8 == 0) goto L_0x0062
            r0 = 2
            goto L_0x0063
        L_0x0062:
            r0 = -1
        L_0x0063:
            switch(r0) {
                case 0: goto L_0x007a;
                case 1: goto L_0x0077;
                case 2: goto L_0x0074;
                case 3: goto L_0x0071;
                case 4: goto L_0x006c;
                case 5: goto L_0x0067;
                default: goto L_0x0066;
            }
        L_0x0066:
            goto L_0x007c
        L_0x0067:
            r1[r7] = r9
            r1[r6] = r10
            goto L_0x007c
        L_0x006c:
            r1[r5] = r9
            r1[r4] = r10
            goto L_0x007c
        L_0x0071:
            r1[r6] = r10
            goto L_0x007c
        L_0x0074:
            r1[r4] = r10
            goto L_0x007c
        L_0x0077:
            r1[r7] = r9
            goto L_0x007c
        L_0x007a:
            r1[r5] = r9
        L_0x007c:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXResourceUtils.parseGradientDirection(java.lang.String, float, float):float[]");
    }

    public static boolean isNamedColor(String str) {
        return colorMap.containsKey(str);
    }
}
