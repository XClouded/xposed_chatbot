package com.alibaba.android.bindingx.core.internal;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.text.TextUtils;
import androidx.annotation.Keep;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import com.taobao.weex.common.Constants;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONException;

@Keep
public class JSMath {
    /* access modifiers changed from: private */
    public static ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();

    private JSMath() {
    }

    public static Object sin() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.sin(((Double) arrayList.get(0)).doubleValue()));
            }
        };
    }

    public static Object cos() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.cos(((Double) arrayList.get(0)).doubleValue()));
            }
        };
    }

    public static Object tan() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.tan(((Double) arrayList.get(0)).doubleValue()));
            }
        };
    }

    public static Object asin() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.asin(((Double) arrayList.get(0)).doubleValue()));
            }
        };
    }

    public static Object acos() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.acos(((Double) arrayList.get(0)).doubleValue()));
            }
        };
    }

    public static Object atan() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.atan(((Double) arrayList.get(0)).doubleValue()));
            }
        };
    }

    public static Object atan2() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.atan2(((Double) arrayList.get(0)).doubleValue(), ((Double) arrayList.get(1)).doubleValue()));
            }
        };
    }

    public static Object pow() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.pow(((Double) arrayList.get(0)).doubleValue(), ((Double) arrayList.get(1)).doubleValue()));
            }
        };
    }

    public static Object exp() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.exp(((Double) arrayList.get(0)).doubleValue()));
            }
        };
    }

    public static Object sqrt() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.sqrt(((Double) arrayList.get(0)).doubleValue()));
            }
        };
    }

    public static Object cbrt() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.cbrt(((Double) arrayList.get(0)).doubleValue()));
            }
        };
    }

    public static Object log() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.log(((Double) arrayList.get(0)).doubleValue()));
            }
        };
    }

    public static Object abs() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.abs(((Double) arrayList.get(0)).doubleValue()));
            }
        };
    }

    public static Object sign() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                if (doubleValue > 0.0d) {
                    return 1;
                }
                if (doubleValue == 0.0d) {
                    return 0;
                }
                if (doubleValue < 0.0d) {
                    return -1;
                }
                return Double.valueOf(Double.NaN);
            }
        };
    }

    public static Object ceil() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.ceil(((Double) arrayList.get(0)).doubleValue()));
            }
        };
    }

    public static Object floor() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Double.valueOf(Math.floor(((Double) arrayList.get(0)).doubleValue()));
            }
        };
    }

    public static Object round() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                return Long.valueOf(Math.round(((Double) arrayList.get(0)).doubleValue()));
            }
        };
    }

    public static Object max() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                if (arrayList == null) {
                    return null;
                }
                if (arrayList.size() < 1) {
                    return null;
                }
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                int size = arrayList.size();
                for (int i = 1; i < size; i++) {
                    double doubleValue2 = ((Double) arrayList.get(i)).doubleValue();
                    if (doubleValue2 > doubleValue) {
                        doubleValue = doubleValue2;
                    }
                }
                return Double.valueOf(doubleValue);
            }
        };
    }

    public static Object min() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) {
                if (arrayList == null) {
                    return null;
                }
                if (arrayList.size() < 1) {
                    return null;
                }
                double doubleValue = ((Double) arrayList.get(0)).doubleValue();
                int size = arrayList.size();
                for (int i = 1; i < size; i++) {
                    double doubleValue2 = ((Double) arrayList.get(i)).doubleValue();
                    if (doubleValue2 < doubleValue) {
                        doubleValue = doubleValue2;
                    }
                }
                return Double.valueOf(doubleValue);
            }
        };
    }

    public static Object PI() {
        return Double.valueOf(3.141592653589793d);
    }

    public static Object E() {
        return Double.valueOf(2.718281828459045d);
    }

    public static Object rgb() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                if (arrayList == null || arrayList.size() < 3) {
                    return null;
                }
                return Integer.valueOf(Color.rgb((int) ((Double) arrayList.get(0)).doubleValue(), (int) ((Double) arrayList.get(1)).doubleValue(), (int) ((Double) arrayList.get(2)).doubleValue()));
            }
        };
    }

    public static Object rgba() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                if (arrayList == null || arrayList.size() < 4) {
                    return null;
                }
                return Integer.valueOf(Color.argb((int) (((Double) arrayList.get(3)).doubleValue() * 255.0d), (int) ((Double) arrayList.get(0)).doubleValue(), (int) ((Double) arrayList.get(1)).doubleValue(), (int) ((Double) arrayList.get(2)).doubleValue()));
            }
        };
    }

    public static Object evaluateColor() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                int access$000 = JSMath.parseColor((String) arrayList.get(0));
                int access$0002 = JSMath.parseColor((String) arrayList.get(1));
                return JSMath.sArgbEvaluator.evaluate((float) Math.min(1.0d, Math.max(0.0d, ((Double) arrayList.get(2)).doubleValue())), Integer.valueOf(access$000), Integer.valueOf(access$0002));
            }
        };
    }

    /* access modifiers changed from: private */
    public static int parseColor(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (str.startsWith(DXBindingXConstant.SINGLE_QUOTE) || str.startsWith("\"")) {
                str = str.substring(1, str.length() - 1);
            }
            int parseColor = Color.parseColor(str);
            return Color.argb(255, Color.red(parseColor), Color.green(parseColor), Color.blue(parseColor));
        }
        throw new IllegalArgumentException("Unknown color");
    }

    public static Object asArray() {
        return new JSFunctionInterface() {
            public Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException {
                return arrayList;
            }
        };
    }

    static void applyXYToScope(Map<String, Object> map, double d, double d2, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator) {
        map.put(Constants.Name.X, Double.valueOf(iDeviceResolutionTranslator.nativeToWeb(d, new Object[0])));
        map.put(Constants.Name.Y, Double.valueOf(iDeviceResolutionTranslator.nativeToWeb(d2, new Object[0])));
        map.put("internal_x", Double.valueOf(d));
        map.put("internal_y", Double.valueOf(d2));
    }

    static void applySpringValueToScope(Map<String, Object> map, double d, double d2) {
        map.put("p", Double.valueOf(d));
        map.put("v", Double.valueOf(d2));
    }

    static void applyScaleFactorToScope(Map<String, Object> map, double d) {
        map.put("s", Double.valueOf(d));
    }

    static void applyRotationInDegreesToScope(Map<String, Object> map, double d) {
        map.put(UploadQueueMgr.MSGTYPE_REALTIME, Double.valueOf(d));
    }

    static void applyOrientationValuesToScope(Map<String, Object> map, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        Map<String, Object> map2 = map;
        map.put("alpha", Double.valueOf(d));
        map.put("beta", Double.valueOf(d2));
        map.put("gamma", Double.valueOf(d3));
        map.put("dalpha", Double.valueOf(d - d4));
        map.put("dbeta", Double.valueOf(d2 - d5));
        map.put("dgamma", Double.valueOf(d3 - d6));
        map.put(Constants.Name.X, Double.valueOf(d7));
        map.put(Constants.Name.Y, Double.valueOf(d8));
        map.put("z", Double.valueOf(d9));
    }

    static void applyTimingValuesToScope(Map<String, Object> map, double d) {
        map.put("t", Double.valueOf(d));
    }

    static void applyScrollValuesToScope(Map<String, Object> map, double d, double d2, double d3, double d4, double d5, double d6, PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator) {
        Map<String, Object> map2 = map;
        PlatformManager.IDeviceResolutionTranslator iDeviceResolutionTranslator2 = iDeviceResolutionTranslator;
        double d7 = d;
        map.put(Constants.Name.X, Double.valueOf(iDeviceResolutionTranslator2.nativeToWeb(d, new Object[0])));
        double d8 = d2;
        map.put(Constants.Name.Y, Double.valueOf(iDeviceResolutionTranslator2.nativeToWeb(d2, new Object[0])));
        map.put("dx", Double.valueOf(iDeviceResolutionTranslator2.nativeToWeb(d3, new Object[0])));
        map.put("dy", Double.valueOf(iDeviceResolutionTranslator2.nativeToWeb(d4, new Object[0])));
        map.put("tdx", Double.valueOf(iDeviceResolutionTranslator2.nativeToWeb(d5, new Object[0])));
        map.put("tdy", Double.valueOf(iDeviceResolutionTranslator2.nativeToWeb(d6, new Object[0])));
        map.put("internal_x", Double.valueOf(d));
        map.put("internal_y", Double.valueOf(d2));
    }
}
