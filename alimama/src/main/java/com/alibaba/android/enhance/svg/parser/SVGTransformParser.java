package com.alibaba.android.enhance.svg.parser;

import android.graphics.Matrix;
import androidx.annotation.NonNull;
import com.alibaba.android.enhance.svg.SVGPlugin;
import com.alibaba.android.enhance.svg.parser.SimpleFunctionParser;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SVGTransformParser {
    private static final String TYPE_MATRIX = "matrix";
    private static final String TYPE_ROTATE = "rotate";
    private static final String TYPE_SCALE = "scale";
    private static final String TYPE_SKEWX = "skewX";
    private static final String TYPE_SKEWY = "skewY";
    private static final String TYPE_TRANSLATE = "translate";
    private Matrix mMatrix = new Matrix();
    private float mScale;

    public Matrix parseTransform(@NonNull String str, float f) {
        this.mScale = f;
        if (this.mMatrix == null) {
            this.mMatrix = new Matrix();
        } else {
            this.mMatrix.reset();
        }
        LinkedList parse = new SimpleFunctionParser(str, new SimpleFunctionParser.NonUniformMapper<Float>() {
            public List<Float> map(List<String> list) {
                LinkedList linkedList = new LinkedList();
                for (String str : list) {
                    Float f = WXUtils.getFloat(str, Float.valueOf(Float.NaN));
                    if (f.floatValue() != Float.NaN) {
                        linkedList.add(f);
                    }
                }
                return linkedList;
            }
        }).parse();
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d(SVGPlugin.TAG, "parsed transform:" + parse.toString());
        }
        if (parse != null && !parse.isEmpty()) {
            Iterator it = parse.iterator();
            while (it.hasNext()) {
                for (Map.Entry entry : ((Map) it.next()).entrySet()) {
                    String str2 = (String) entry.getKey();
                    List list = (List) entry.getValue();
                    char c = 65535;
                    switch (str2.hashCode()) {
                        case -1081239615:
                            if (str2.equals(TYPE_MATRIX)) {
                                c = 0;
                                break;
                            }
                            break;
                        case -925180581:
                            if (str2.equals("rotate")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 109250890:
                            if (str2.equals("scale")) {
                                c = 2;
                                break;
                            }
                            break;
                        case 109493390:
                            if (str2.equals(TYPE_SKEWX)) {
                                c = 4;
                                break;
                            }
                            break;
                        case 109493391:
                            if (str2.equals(TYPE_SKEWY)) {
                                c = 5;
                                break;
                            }
                            break;
                        case 1052832078:
                            if (str2.equals("translate")) {
                                c = 3;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            if (list.size() != 6) {
                                break;
                            } else {
                                Matrix matrix = new Matrix();
                                matrix.setValues(new float[]{((Float) list.get(0)).floatValue(), ((Float) list.get(2)).floatValue(), ((Float) list.get(4)).floatValue() * this.mScale, ((Float) list.get(1)).floatValue(), ((Float) list.get(3)).floatValue(), ((Float) list.get(5)).floatValue() * this.mScale, 0.0f, 0.0f, 1.0f});
                                this.mMatrix.preConcat(matrix);
                                break;
                            }
                        case 1:
                            if (list.size() != 1) {
                                if (list.size() != 3) {
                                    break;
                                } else {
                                    this.mMatrix.preRotate(((Float) list.get(0)).floatValue(), ((Float) list.get(1)).floatValue() * this.mScale, ((Float) list.get(2)).floatValue() * this.mScale);
                                    break;
                                }
                            } else {
                                this.mMatrix.preRotate(((Float) list.get(0)).floatValue());
                                break;
                            }
                        case 2:
                            if (list.size() != 1) {
                                if (list.size() != 2) {
                                    break;
                                } else {
                                    this.mMatrix.preScale(((Float) list.get(0)).floatValue(), ((Float) list.get(1)).floatValue());
                                    break;
                                }
                            } else {
                                this.mMatrix.preScale(((Float) list.get(0)).floatValue(), ((Float) list.get(0)).floatValue());
                                break;
                            }
                        case 3:
                            if (list.size() != 1) {
                                if (list.size() != 2) {
                                    break;
                                } else {
                                    this.mMatrix.preTranslate(((Float) list.get(0)).floatValue() * this.mScale, ((Float) list.get(1)).floatValue() * this.mScale);
                                    break;
                                }
                            } else {
                                this.mMatrix.preTranslate(((Float) list.get(0)).floatValue() * this.mScale, 0.0f);
                                break;
                            }
                        case 4:
                            if (list.size() != 1) {
                                break;
                            } else {
                                this.mMatrix.preSkew((float) Math.tan(Math.toRadians((double) ((Float) list.get(0)).floatValue())), 0.0f);
                                break;
                            }
                        case 5:
                            if (list.size() != 1) {
                                break;
                            } else {
                                this.mMatrix.preSkew(0.0f, (float) Math.tan(Math.toRadians((double) ((Float) list.get(0)).floatValue())));
                                break;
                            }
                        default:
                            WXLogUtils.d("weex-svg", "unknown transform property:" + str2);
                            break;
                    }
                }
            }
        }
        return this.mMatrix;
    }
}
