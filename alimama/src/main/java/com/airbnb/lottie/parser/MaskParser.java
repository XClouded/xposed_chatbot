package com.airbnb.lottie.parser;

class MaskParser {
    private MaskParser() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:44:0x009f, code lost:
        if (r1.equals(com.alibaba.analytics.core.sync.UploadQueueMgr.MSGTYPE_INTERVAL) != false) goto L_0x00ad;
     */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0059  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0062  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00b0  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00cd  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00d6  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00da  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00de  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static com.airbnb.lottie.model.content.Mask parse(com.airbnb.lottie.parser.moshi.JsonReader r12, com.airbnb.lottie.LottieComposition r13) throws java.io.IOException {
        /*
            r12.beginObject()
            r0 = 0
            r1 = 0
            r2 = r1
            r3 = r2
            r4 = 0
        L_0x0008:
            boolean r5 = r12.hasNext()
            if (r5 == 0) goto L_0x00e2
            java.lang.String r5 = r12.nextName()
            int r6 = r5.hashCode()
            r7 = 111(0x6f, float:1.56E-43)
            r8 = 3
            r9 = 1
            r10 = 2
            r11 = -1
            if (r6 == r7) goto L_0x004b
            r7 = 3588(0xe04, float:5.028E-42)
            if (r6 == r7) goto L_0x0041
            r7 = 104433(0x197f1, float:1.46342E-40)
            if (r6 == r7) goto L_0x0037
            r7 = 3357091(0x3339a3, float:4.704286E-39)
            if (r6 == r7) goto L_0x002d
            goto L_0x0055
        L_0x002d:
            java.lang.String r6 = "mode"
            boolean r6 = r5.equals(r6)
            if (r6 == 0) goto L_0x0055
            r6 = 0
            goto L_0x0056
        L_0x0037:
            java.lang.String r6 = "inv"
            boolean r6 = r5.equals(r6)
            if (r6 == 0) goto L_0x0055
            r6 = 3
            goto L_0x0056
        L_0x0041:
            java.lang.String r6 = "pt"
            boolean r6 = r5.equals(r6)
            if (r6 == 0) goto L_0x0055
            r6 = 1
            goto L_0x0056
        L_0x004b:
            java.lang.String r6 = "o"
            boolean r6 = r5.equals(r6)
            if (r6 == 0) goto L_0x0055
            r6 = 2
            goto L_0x0056
        L_0x0055:
            r6 = -1
        L_0x0056:
            switch(r6) {
                case 0: goto L_0x006c;
                case 1: goto L_0x0067;
                case 2: goto L_0x0062;
                case 3: goto L_0x005d;
                default: goto L_0x0059;
            }
        L_0x0059:
            r12.skipValue()
            goto L_0x0008
        L_0x005d:
            boolean r4 = r12.nextBoolean()
            goto L_0x0008
        L_0x0062:
            com.airbnb.lottie.model.animatable.AnimatableIntegerValue r3 = com.airbnb.lottie.parser.AnimatableValueParser.parseInteger(r12, r13)
            goto L_0x0008
        L_0x0067:
            com.airbnb.lottie.model.animatable.AnimatableShapeValue r2 = com.airbnb.lottie.parser.AnimatableValueParser.parseShapeData(r12, r13)
            goto L_0x0008
        L_0x006c:
            java.lang.String r1 = r12.nextString()
            int r6 = r1.hashCode()
            r7 = 97
            if (r6 == r7) goto L_0x00a2
            r7 = 105(0x69, float:1.47E-43)
            if (r6 == r7) goto L_0x0099
            r7 = 110(0x6e, float:1.54E-43)
            if (r6 == r7) goto L_0x008f
            r7 = 115(0x73, float:1.61E-43)
            if (r6 == r7) goto L_0x0085
            goto L_0x00ac
        L_0x0085:
            java.lang.String r6 = "s"
            boolean r1 = r1.equals(r6)
            if (r1 == 0) goto L_0x00ac
            r8 = 1
            goto L_0x00ad
        L_0x008f:
            java.lang.String r6 = "n"
            boolean r1 = r1.equals(r6)
            if (r1 == 0) goto L_0x00ac
            r8 = 2
            goto L_0x00ad
        L_0x0099:
            java.lang.String r6 = "i"
            boolean r1 = r1.equals(r6)
            if (r1 == 0) goto L_0x00ac
            goto L_0x00ad
        L_0x00a2:
            java.lang.String r6 = "a"
            boolean r1 = r1.equals(r6)
            if (r1 == 0) goto L_0x00ac
            r8 = 0
            goto L_0x00ad
        L_0x00ac:
            r8 = -1
        L_0x00ad:
            switch(r8) {
                case 0: goto L_0x00de;
                case 1: goto L_0x00da;
                case 2: goto L_0x00d6;
                case 3: goto L_0x00cd;
                default: goto L_0x00b0;
            }
        L_0x00b0:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r6 = "Unknown mask mode "
            r1.append(r6)
            r1.append(r5)
            java.lang.String r5 = ". Defaulting to Add."
            r1.append(r5)
            java.lang.String r1 = r1.toString()
            com.airbnb.lottie.utils.Logger.warning(r1)
            com.airbnb.lottie.model.content.Mask$MaskMode r1 = com.airbnb.lottie.model.content.Mask.MaskMode.MASK_MODE_ADD
            goto L_0x0008
        L_0x00cd:
            java.lang.String r1 = "Animation contains intersect masks. They are not supported but will be treated like add masks."
            r13.addWarning(r1)
            com.airbnb.lottie.model.content.Mask$MaskMode r1 = com.airbnb.lottie.model.content.Mask.MaskMode.MASK_MODE_INTERSECT
            goto L_0x0008
        L_0x00d6:
            com.airbnb.lottie.model.content.Mask$MaskMode r1 = com.airbnb.lottie.model.content.Mask.MaskMode.MASK_MODE_NONE
            goto L_0x0008
        L_0x00da:
            com.airbnb.lottie.model.content.Mask$MaskMode r1 = com.airbnb.lottie.model.content.Mask.MaskMode.MASK_MODE_SUBTRACT
            goto L_0x0008
        L_0x00de:
            com.airbnb.lottie.model.content.Mask$MaskMode r1 = com.airbnb.lottie.model.content.Mask.MaskMode.MASK_MODE_ADD
            goto L_0x0008
        L_0x00e2:
            r12.endObject()
            com.airbnb.lottie.model.content.Mask r12 = new com.airbnb.lottie.model.content.Mask
            r12.<init>(r1, r2, r3, r4)
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.MaskParser.parse(com.airbnb.lottie.parser.moshi.JsonReader, com.airbnb.lottie.LottieComposition):com.airbnb.lottie.model.content.Mask");
    }
}
