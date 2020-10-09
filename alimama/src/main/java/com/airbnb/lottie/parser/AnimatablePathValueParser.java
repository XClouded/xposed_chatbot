package com.airbnb.lottie.parser;

import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatablePathValue;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.Keyframe;
import com.taobao.weex.common.Constants;
import java.io.IOException;
import java.util.ArrayList;

public class AnimatablePathValueParser {
    private static JsonReader.Options NAMES = JsonReader.Options.of("k", Constants.Name.X, Constants.Name.Y);

    private AnimatablePathValueParser() {
    }

    public static AnimatablePathValue parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        ArrayList arrayList = new ArrayList();
        if (jsonReader.peek() == JsonReader.Token.BEGIN_ARRAY) {
            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                arrayList.add(PathKeyframeParser.parse(jsonReader, lottieComposition));
            }
            jsonReader.endArray();
            KeyframesParser.setEndFrames(arrayList);
        } else {
            arrayList.add(new Keyframe(JsonUtils.jsonToPoint(jsonReader, Utils.dpScale())));
        }
        return new AnimatablePathValue(arrayList);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003c, code lost:
        r2 = true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static com.airbnb.lottie.model.animatable.AnimatableValue<android.graphics.PointF, android.graphics.PointF> parseSplitPath(com.airbnb.lottie.parser.moshi.JsonReader r7, com.airbnb.lottie.LottieComposition r8) throws java.io.IOException {
        /*
            r7.beginObject()
            r0 = 1
            r1 = 0
            r2 = 0
            r3 = r1
            r4 = r3
        L_0x0008:
            com.airbnb.lottie.parser.moshi.JsonReader$Token r5 = r7.peek()
            com.airbnb.lottie.parser.moshi.JsonReader$Token r6 = com.airbnb.lottie.parser.moshi.JsonReader.Token.END_OBJECT
            if (r5 == r6) goto L_0x0048
            com.airbnb.lottie.parser.moshi.JsonReader$Options r5 = NAMES
            int r5 = r7.selectName(r5)
            switch(r5) {
                case 0: goto L_0x0043;
                case 1: goto L_0x0031;
                case 2: goto L_0x0020;
                default: goto L_0x0019;
            }
        L_0x0019:
            r7.skipName()
            r7.skipValue()
            goto L_0x0008
        L_0x0020:
            com.airbnb.lottie.parser.moshi.JsonReader$Token r5 = r7.peek()
            com.airbnb.lottie.parser.moshi.JsonReader$Token r6 = com.airbnb.lottie.parser.moshi.JsonReader.Token.STRING
            if (r5 != r6) goto L_0x002c
            r7.skipValue()
            goto L_0x003c
        L_0x002c:
            com.airbnb.lottie.model.animatable.AnimatableFloatValue r4 = com.airbnb.lottie.parser.AnimatableValueParser.parseFloat(r7, r8)
            goto L_0x0008
        L_0x0031:
            com.airbnb.lottie.parser.moshi.JsonReader$Token r5 = r7.peek()
            com.airbnb.lottie.parser.moshi.JsonReader$Token r6 = com.airbnb.lottie.parser.moshi.JsonReader.Token.STRING
            if (r5 != r6) goto L_0x003e
            r7.skipValue()
        L_0x003c:
            r2 = 1
            goto L_0x0008
        L_0x003e:
            com.airbnb.lottie.model.animatable.AnimatableFloatValue r3 = com.airbnb.lottie.parser.AnimatableValueParser.parseFloat(r7, r8)
            goto L_0x0008
        L_0x0043:
            com.airbnb.lottie.model.animatable.AnimatablePathValue r1 = parse(r7, r8)
            goto L_0x0008
        L_0x0048:
            r7.endObject()
            if (r2 == 0) goto L_0x0052
            java.lang.String r7 = "Lottie doesn't support expressions."
            r8.addWarning(r7)
        L_0x0052:
            if (r1 == 0) goto L_0x0055
            return r1
        L_0x0055:
            com.airbnb.lottie.model.animatable.AnimatableSplitDimensionPathValue r7 = new com.airbnb.lottie.model.animatable.AnimatableSplitDimensionPathValue
            r7.<init>(r3, r4)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.AnimatablePathValueParser.parseSplitPath(com.airbnb.lottie.parser.moshi.JsonReader, com.airbnb.lottie.LottieComposition):com.airbnb.lottie.model.animatable.AnimatableValue");
    }
}
