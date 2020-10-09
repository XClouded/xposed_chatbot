package com.airbnb.lottie.parser;

import com.airbnb.lottie.parser.moshi.JsonReader;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.weex.ui.component.WXComponent;

class ShapeStrokeParser {
    private static final JsonReader.Options DASH_PATTERN_NAMES = JsonReader.Options.of("n", "v");
    private static JsonReader.Options NAMES = JsonReader.Options.of("nm", "c", WXComponent.PROP_FS_WRAP_CONTENT, "o", DinamicConstant.LOWER_PREFIX, "lj", "ml", "hd", "d");

    private ShapeStrokeParser() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x008c  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x008f  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x0098  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static com.airbnb.lottie.model.content.ShapeStroke parse(com.airbnb.lottie.parser.moshi.JsonReader r17, com.airbnb.lottie.LottieComposition r18) throws java.io.IOException {
        /*
            r0 = r17
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r4 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            r11 = 0
            r12 = 0
        L_0x0011:
            boolean r13 = r17.hasNext()
            if (r13 == 0) goto L_0x010b
            com.airbnb.lottie.parser.moshi.JsonReader$Options r13 = NAMES
            int r13 = r0.selectName(r13)
            r14 = 1
            switch(r13) {
                case 0: goto L_0x0102;
                case 1: goto L_0x00f9;
                case 2: goto L_0x00f0;
                case 3: goto L_0x00e7;
                case 4: goto L_0x00d7;
                case 5: goto L_0x00c7;
                case 6: goto L_0x00bd;
                case 7: goto L_0x00b4;
                case 8: goto L_0x0028;
                default: goto L_0x0021;
            }
        L_0x0021:
            r1 = r18
            r2 = 0
            r17.skipValue()
            goto L_0x0011
        L_0x0028:
            r17.beginArray()
        L_0x002b:
            boolean r13 = r17.hasNext()
            if (r13 == 0) goto L_0x009c
            r17.beginObject()
            r13 = 0
            r15 = 0
        L_0x0036:
            boolean r16 = r17.hasNext()
            if (r16 == 0) goto L_0x0056
            com.airbnb.lottie.parser.moshi.JsonReader$Options r2 = DASH_PATTERN_NAMES
            int r2 = r0.selectName(r2)
            switch(r2) {
                case 0: goto L_0x0051;
                case 1: goto L_0x004c;
                default: goto L_0x0045;
            }
        L_0x0045:
            r17.skipName()
            r17.skipValue()
            goto L_0x0036
        L_0x004c:
            com.airbnb.lottie.model.animatable.AnimatableFloatValue r15 = com.airbnb.lottie.parser.AnimatableValueParser.parseFloat(r17, r18)
            goto L_0x0036
        L_0x0051:
            java.lang.String r13 = r17.nextString()
            goto L_0x0036
        L_0x0056:
            r17.endObject()
            int r2 = r13.hashCode()
            r1 = 100
            if (r2 == r1) goto L_0x007e
            r1 = 103(0x67, float:1.44E-43)
            if (r2 == r1) goto L_0x0074
            r1 = 111(0x6f, float:1.56E-43)
            if (r2 == r1) goto L_0x006a
            goto L_0x0088
        L_0x006a:
            java.lang.String r1 = "o"
            boolean r1 = r13.equals(r1)
            if (r1 == 0) goto L_0x0088
            r1 = 0
            goto L_0x0089
        L_0x0074:
            java.lang.String r1 = "g"
            boolean r1 = r13.equals(r1)
            if (r1 == 0) goto L_0x0088
            r1 = 2
            goto L_0x0089
        L_0x007e:
            java.lang.String r1 = "d"
            boolean r1 = r13.equals(r1)
            if (r1 == 0) goto L_0x0088
            r1 = 1
            goto L_0x0089
        L_0x0088:
            r1 = -1
        L_0x0089:
            switch(r1) {
                case 0: goto L_0x0098;
                case 1: goto L_0x008f;
                case 2: goto L_0x008f;
                default: goto L_0x008c;
            }
        L_0x008c:
            r1 = r18
            goto L_0x002b
        L_0x008f:
            r1 = r18
            r1.setHasDashPattern(r14)
            r3.add(r15)
            goto L_0x002b
        L_0x0098:
            r1 = r18
            r5 = r15
            goto L_0x002b
        L_0x009c:
            r1 = r18
            r17.endArray()
            int r2 = r3.size()
            if (r2 != r14) goto L_0x00b1
            r2 = 0
            java.lang.Object r13 = r3.get(r2)
            r3.add(r13)
            goto L_0x0011
        L_0x00b1:
            r2 = 0
            goto L_0x0011
        L_0x00b4:
            r1 = r18
            r2 = 0
            boolean r12 = r17.nextBoolean()
            goto L_0x0011
        L_0x00bd:
            r1 = r18
            r2 = 0
            double r13 = r17.nextDouble()
            float r11 = (float) r13
            goto L_0x0011
        L_0x00c7:
            r1 = r18
            r2 = 0
            com.airbnb.lottie.model.content.ShapeStroke$LineJoinType[] r10 = com.airbnb.lottie.model.content.ShapeStroke.LineJoinType.values()
            int r13 = r17.nextInt()
            int r13 = r13 - r14
            r10 = r10[r13]
            goto L_0x0011
        L_0x00d7:
            r1 = r18
            r2 = 0
            com.airbnb.lottie.model.content.ShapeStroke$LineCapType[] r9 = com.airbnb.lottie.model.content.ShapeStroke.LineCapType.values()
            int r13 = r17.nextInt()
            int r13 = r13 - r14
            r9 = r9[r13]
            goto L_0x0011
        L_0x00e7:
            r1 = r18
            r2 = 0
            com.airbnb.lottie.model.animatable.AnimatableIntegerValue r7 = com.airbnb.lottie.parser.AnimatableValueParser.parseInteger(r17, r18)
            goto L_0x0011
        L_0x00f0:
            r1 = r18
            r2 = 0
            com.airbnb.lottie.model.animatable.AnimatableFloatValue r8 = com.airbnb.lottie.parser.AnimatableValueParser.parseFloat(r17, r18)
            goto L_0x0011
        L_0x00f9:
            r1 = r18
            r2 = 0
            com.airbnb.lottie.model.animatable.AnimatableColorValue r6 = com.airbnb.lottie.parser.AnimatableValueParser.parseColor(r17, r18)
            goto L_0x0011
        L_0x0102:
            r1 = r18
            r2 = 0
            java.lang.String r4 = r17.nextString()
            goto L_0x0011
        L_0x010b:
            com.airbnb.lottie.model.content.ShapeStroke r13 = new com.airbnb.lottie.model.content.ShapeStroke
            r0 = r13
            r1 = r4
            r2 = r5
            r4 = r6
            r5 = r7
            r6 = r8
            r7 = r9
            r8 = r10
            r9 = r11
            r10 = r12
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.ShapeStrokeParser.parse(com.airbnb.lottie.parser.moshi.JsonReader, com.airbnb.lottie.LottieComposition):com.airbnb.lottie.model.content.ShapeStroke");
    }
}
