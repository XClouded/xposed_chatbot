package com.alibaba.android.enhance.svg;

import com.alibaba.android.bindingx.core.BindingXJSFunctionRegister;
import com.alibaba.android.bindingx.core.internal.JSFunctionInterface;
import com.alibaba.android.enhance.svg.morph.MorphAlgorithm;
import java.util.ArrayList;
import org.json.JSONException;

public class SVGJSFunction {
    private static final JSFunctionInterface SVG_DRAW_CMD = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            return arrayList;
        }
    };
    private static final JSFunctionInterface SVG_MORPH = new JSFunctionInterface() {
        public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
            Object obj = arrayList.get(0);
            Object obj2 = arrayList.get(1);
            Object obj3 = arrayList.get(2);
            if (!(obj instanceof String) || !(obj2 instanceof String) || !(obj3 instanceof Double)) {
                return null;
            }
            return MorphAlgorithm.morph((String) obj, (String) obj2, (float) ((Double) obj3).doubleValue());
        }
    };

    public static void registerAll() {
        BindingXJSFunctionRegister instance = BindingXJSFunctionRegister.getInstance();
        instance.registerJSFunction("svgDrawCmd", SVG_DRAW_CMD);
        instance.registerJSFunction("svgDrawCmds", SVG_DRAW_CMD);
        instance.registerJSFunction("svgMorph", SVG_MORPH);
        instance.registerJSFunction("svgMorph2", SVG_MORPH);
    }
}
