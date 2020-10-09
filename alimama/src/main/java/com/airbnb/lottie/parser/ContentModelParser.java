package com.airbnb.lottie.parser;

import com.airbnb.lottie.parser.moshi.JsonReader;
import com.alipay.sdk.sys.a;

class ContentModelParser {
    private static JsonReader.Options NAMES = JsonReader.Options.of(a.g, "d");

    private ContentModelParser() {
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0092, code lost:
        if (r2.equals("gs") != false) goto L_0x00be;
     */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static com.airbnb.lottie.model.content.ContentModel parse(com.airbnb.lottie.parser.moshi.JsonReader r6, com.airbnb.lottie.LottieComposition r7) throws java.io.IOException {
        /*
            r6.beginObject()
            r0 = 2
            r1 = 2
        L_0x0005:
            boolean r2 = r6.hasNext()
            r3 = 0
            if (r2 == 0) goto L_0x0026
            com.airbnb.lottie.parser.moshi.JsonReader$Options r2 = NAMES
            int r2 = r6.selectName(r2)
            switch(r2) {
                case 0: goto L_0x0021;
                case 1: goto L_0x001c;
                default: goto L_0x0015;
            }
        L_0x0015:
            r6.skipName()
            r6.skipValue()
            goto L_0x0005
        L_0x001c:
            int r1 = r6.nextInt()
            goto L_0x0005
        L_0x0021:
            java.lang.String r2 = r6.nextString()
            goto L_0x0027
        L_0x0026:
            r2 = r3
        L_0x0027:
            if (r2 != 0) goto L_0x002a
            return r3
        L_0x002a:
            r4 = -1
            int r5 = r2.hashCode()
            switch(r5) {
                case 3239: goto L_0x00b3;
                case 3270: goto L_0x00a9;
                case 3295: goto L_0x009f;
                case 3307: goto L_0x0095;
                case 3308: goto L_0x008c;
                case 3488: goto L_0x0081;
                case 3633: goto L_0x0076;
                case 3646: goto L_0x006b;
                case 3669: goto L_0x0061;
                case 3679: goto L_0x0056;
                case 3681: goto L_0x004b;
                case 3705: goto L_0x003f;
                case 3710: goto L_0x0034;
                default: goto L_0x0032;
            }
        L_0x0032:
            goto L_0x00bd
        L_0x0034:
            java.lang.String r0 = "tr"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x00bd
            r0 = 5
            goto L_0x00be
        L_0x003f:
            java.lang.String r0 = "tm"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x00bd
            r0 = 9
            goto L_0x00be
        L_0x004b:
            java.lang.String r0 = "st"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x00bd
            r0 = 1
            goto L_0x00be
        L_0x0056:
            java.lang.String r0 = "sr"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x00bd
            r0 = 10
            goto L_0x00be
        L_0x0061:
            java.lang.String r0 = "sh"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x00bd
            r0 = 6
            goto L_0x00be
        L_0x006b:
            java.lang.String r0 = "rp"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x00bd
            r0 = 12
            goto L_0x00be
        L_0x0076:
            java.lang.String r0 = "rc"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x00bd
            r0 = 8
            goto L_0x00be
        L_0x0081:
            java.lang.String r0 = "mm"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x00bd
            r0 = 11
            goto L_0x00be
        L_0x008c:
            java.lang.String r5 = "gs"
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x00bd
            goto L_0x00be
        L_0x0095:
            java.lang.String r0 = "gr"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x00bd
            r0 = 0
            goto L_0x00be
        L_0x009f:
            java.lang.String r0 = "gf"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x00bd
            r0 = 4
            goto L_0x00be
        L_0x00a9:
            java.lang.String r0 = "fl"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x00bd
            r0 = 3
            goto L_0x00be
        L_0x00b3:
            java.lang.String r0 = "el"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x00bd
            r0 = 7
            goto L_0x00be
        L_0x00bd:
            r0 = -1
        L_0x00be:
            switch(r0) {
                case 0: goto L_0x0117;
                case 1: goto L_0x0112;
                case 2: goto L_0x010d;
                case 3: goto L_0x0108;
                case 4: goto L_0x0103;
                case 5: goto L_0x00fe;
                case 6: goto L_0x00f9;
                case 7: goto L_0x00f4;
                case 8: goto L_0x00ef;
                case 9: goto L_0x00ea;
                case 10: goto L_0x00e5;
                case 11: goto L_0x00db;
                case 12: goto L_0x00d6;
                default: goto L_0x00c1;
            }
        L_0x00c1:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r0 = "Unknown shape type "
            r7.append(r0)
            r7.append(r2)
            java.lang.String r7 = r7.toString()
            com.airbnb.lottie.utils.Logger.warning(r7)
            goto L_0x011b
        L_0x00d6:
            com.airbnb.lottie.model.content.Repeater r3 = com.airbnb.lottie.parser.RepeaterParser.parse(r6, r7)
            goto L_0x011b
        L_0x00db:
            com.airbnb.lottie.model.content.MergePaths r3 = com.airbnb.lottie.parser.MergePathsParser.parse(r6)
            java.lang.String r0 = "Animation contains merge paths. Merge paths are only supported on KitKat+ and must be manually enabled by calling enableMergePathsForKitKatAndAbove()."
            r7.addWarning(r0)
            goto L_0x011b
        L_0x00e5:
            com.airbnb.lottie.model.content.PolystarShape r3 = com.airbnb.lottie.parser.PolystarShapeParser.parse(r6, r7)
            goto L_0x011b
        L_0x00ea:
            com.airbnb.lottie.model.content.ShapeTrimPath r3 = com.airbnb.lottie.parser.ShapeTrimPathParser.parse(r6, r7)
            goto L_0x011b
        L_0x00ef:
            com.airbnb.lottie.model.content.RectangleShape r3 = com.airbnb.lottie.parser.RectangleShapeParser.parse(r6, r7)
            goto L_0x011b
        L_0x00f4:
            com.airbnb.lottie.model.content.CircleShape r3 = com.airbnb.lottie.parser.CircleShapeParser.parse(r6, r7, r1)
            goto L_0x011b
        L_0x00f9:
            com.airbnb.lottie.model.content.ShapePath r3 = com.airbnb.lottie.parser.ShapePathParser.parse(r6, r7)
            goto L_0x011b
        L_0x00fe:
            com.airbnb.lottie.model.animatable.AnimatableTransform r3 = com.airbnb.lottie.parser.AnimatableTransformParser.parse(r6, r7)
            goto L_0x011b
        L_0x0103:
            com.airbnb.lottie.model.content.GradientFill r3 = com.airbnb.lottie.parser.GradientFillParser.parse(r6, r7)
            goto L_0x011b
        L_0x0108:
            com.airbnb.lottie.model.content.ShapeFill r3 = com.airbnb.lottie.parser.ShapeFillParser.parse(r6, r7)
            goto L_0x011b
        L_0x010d:
            com.airbnb.lottie.model.content.GradientStroke r3 = com.airbnb.lottie.parser.GradientStrokeParser.parse(r6, r7)
            goto L_0x011b
        L_0x0112:
            com.airbnb.lottie.model.content.ShapeStroke r3 = com.airbnb.lottie.parser.ShapeStrokeParser.parse(r6, r7)
            goto L_0x011b
        L_0x0117:
            com.airbnb.lottie.model.content.ShapeGroup r3 = com.airbnb.lottie.parser.ShapeGroupParser.parse(r6, r7)
        L_0x011b:
            boolean r7 = r6.hasNext()
            if (r7 == 0) goto L_0x0125
            r6.skipValue()
            goto L_0x011b
        L_0x0125:
            r6.endObject()
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.ContentModelParser.parse(com.airbnb.lottie.parser.moshi.JsonReader, com.airbnb.lottie.LottieComposition):com.airbnb.lottie.model.content.ContentModel");
    }
}
