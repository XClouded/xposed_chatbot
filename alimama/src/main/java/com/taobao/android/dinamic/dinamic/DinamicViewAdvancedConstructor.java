package com.taobao.android.dinamic.dinamic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.android.dinamic.DinamicViewUtils;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DAttrConstant;
import com.taobao.android.dinamic.property.DAttrUtils;
import com.taobao.android.dinamic.property.DinamicEventHandlerWorker;
import com.taobao.android.dinamic.property.DinamicProperty;
import com.taobao.android.dinamic.property.ScreenTool;
import com.taobao.android.dinamic.view.DinamicError;
import com.taobao.android.dinamicx.DXDarkModeCenter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DinamicViewAdvancedConstructor {
    private static final String TAG = "DinamicViewAdvancedConstructor";
    private boolean isInitialize = false;
    private boolean isNeedReflect;
    private boolean isRunSuperMethod;
    private List<MethodInfo> methodInfos;

    @Deprecated
    public void applyDefaultProperty(View view) {
    }

    public DinamicViewAdvancedConstructor() {
        DinamicSingleThreadExecutor.executor(new Runnable() {
            public void run() {
                DinamicViewAdvancedConstructor.this.initialize();
            }
        });
    }

    public View initializeView(String str, Context context, AttributeSet attributeSet) {
        return new View(context, attributeSet);
    }

    public View initializeViewWithModule(String str, Context context, AttributeSet attributeSet, DinamicParams dinamicParams) {
        return initializeView(str, context, attributeSet);
    }

    /* access modifiers changed from: private */
    public void initialize() {
        this.isNeedReflect = findSetAttributesMethod() == null;
        scanAllDinamicAttrMethods();
        this.isInitialize = true;
    }

    private Method findSetAttributesMethod() {
        try {
            return getClassLoader().loadClass(getClass().getName()).getDeclaredMethod("setAttributes", new Class[]{View.class, Map.class, ArrayList.class, DinamicParams.class});
        } catch (ClassNotFoundException | NoSuchMethodException unused) {
            return null;
        }
    }

    public void bindDataImpl(View view, Map<String, Object> map, ArrayList<String> arrayList, DinamicParams dinamicParams) {
        if (needBindData(view, map, dinamicParams)) {
            setAttributes(view, map, arrayList, dinamicParams);
        }
    }

    public void setAttributes(View view, Map<String, Object> map, ArrayList<String> arrayList, DinamicParams dinamicParams) {
        if (arrayList.contains("dBackgroundColor") || arrayList.contains(DAttrConstant.VIEW_CORNER_RADIUS) || arrayList.contains(DAttrConstant.VIEW_BORDER_COLOR) || arrayList.contains(DAttrConstant.VIEW_BORDER_WIDTH)) {
            setBackground(view, (String) map.get(DAttrConstant.VIEW_CORNER_RADIUS), (String) map.get(DAttrConstant.VIEW_BORDER_COLOR), (String) map.get(DAttrConstant.VIEW_BORDER_WIDTH), (String) map.get("dBackgroundColor"));
        }
        if (arrayList.contains(DAttrConstant.VIEW_ALPHA)) {
            setAlpha(view, (String) map.get(DAttrConstant.VIEW_ALPHA));
        }
        if (arrayList.contains(DAttrConstant.VIEW_ACCESSIBILITYTEXT)) {
            setAccessibilityText(view, (String) map.get(DAttrConstant.VIEW_ACCESSIBILITYTEXT));
        }
        if (arrayList.contains(DAttrConstant.VIEW_ACCESSIBILITYTEXT_HIDDEN)) {
            String str = (String) map.get(DAttrConstant.VIEW_ACCESSIBILITYTEXT_HIDDEN);
            if (!TextUtils.isEmpty(str)) {
                setAccessibilityHidden(view, Boolean.valueOf(str).booleanValue());
            } else {
                setAccessibilityHidden(view, true);
            }
        }
        if (arrayList.contains(DAttrConstant.VIEW_DISABLE_DARK_MODE)) {
            setForceDark(view, Boolean.valueOf((String) map.get(DAttrConstant.VIEW_DISABLE_DARK_MODE)).booleanValue());
        }
        if (!this.isInitialize) {
            initialize();
        }
        if (this.isNeedReflect) {
            HashMap hashMap = new HashMap();
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                String next = it.next();
                hashMap.put(next, map.get(next));
            }
            setSpecificAttributes(view, hashMap, dinamicParams);
        }
    }

    /* access modifiers changed from: protected */
    public void setForceDark(View view, boolean z) {
        if (z) {
            DXDarkModeCenter.disableForceDark(view);
        }
    }

    public void setAccessibilityHidden(View view, boolean z) {
        if (Build.VERSION.SDK_INT < 16) {
            view.setContentDescription("");
        } else if (z) {
            view.setImportantForAccessibility(2);
        } else {
            view.setImportantForAccessibility(1);
        }
    }

    public final DinamicProperty handleAttributeSet(AttributeSet attributeSet) {
        DinamicProperty dinamicProperty = new DinamicProperty();
        int attributeCount = attributeSet.getAttributeCount();
        if (attributeCount <= 0) {
            return dinamicProperty;
        }
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        HashMap hashMap3 = new HashMap();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = attributeSet.getAttributeName(i);
            String attributeValue = attributeSet.getAttributeValue(DinamicConstant.RES_AUTO_NAMESPACE, attributeName);
            if (attributeValue != null) {
                if (attributeName.startsWith(DAttrConstant.VIEW_EVENT_FLAG)) {
                    hashMap3.put(attributeName, attributeValue);
                } else if (attributeValue.startsWith("$") || attributeValue.startsWith(DinamicConstant.DINAMIC_PREFIX_AT)) {
                    hashMap2.put(attributeName, attributeValue);
                } else {
                    hashMap.put(attributeName, attributeValue);
                }
            }
        }
        dinamicProperty.fixedProperty = hashMap;
        dinamicProperty.dinamicProperty = Collections.unmodifiableMap(hashMap2);
        dinamicProperty.eventProperty = Collections.unmodifiableMap(hashMap3);
        return dinamicProperty;
    }

    public void applyDefaultProperty(View view, Map<String, Object> map, DinamicParams dinamicParams) {
        if (!map.containsKey("dBackgroundColor")) {
            view.setBackgroundColor(0);
        }
        if (!map.containsKey(DAttrConstant.VIEW_ALPHA)) {
            view.setAlpha(1.0f);
        }
        applyDefaultProperty(view);
    }

    public void setAlpha(View view, String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                view.setAlpha(Float.valueOf(str).floatValue());
            } catch (NumberFormatException unused) {
                view.setAlpha(1.0f);
            }
        } else {
            view.setAlpha(1.0f);
        }
    }

    public void setVisibility(View view, String str) {
        if (TextUtils.equals("visible", str)) {
            view.setVisibility(0);
        } else if (TextUtils.equals(DAttrConstant.VISIBILITY_INVISIBLE, str)) {
            view.setVisibility(4);
        } else if (TextUtils.equals("gone", str)) {
            view.setVisibility(8);
        } else {
            view.setVisibility(0);
        }
    }

    public void setAccessibilityText(View view, String str) {
        view.setContentDescription(str);
    }

    public void setBackground(View view, String str, String str2, String str3, String str4) {
        Drawable background = view.getBackground();
        if (background != null && (background instanceof GradientDrawable)) {
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            int parseColor = DAttrUtils.parseColor(str4, 0);
            if (!TextUtils.isEmpty(str4)) {
                gradientDrawable.setColor(parseColor);
            }
            if (!TextUtils.isEmpty(str)) {
                gradientDrawable.setCornerRadius((float) ScreenTool.getPx(view.getContext(), str, 0));
            }
            if (!TextUtils.isEmpty(str2) || !TextUtils.isEmpty(str3)) {
                gradientDrawable.setStroke(ScreenTool.getPx(view.getContext(), str3, 0), DAttrUtils.parseColor(str2, parseColor));
            }
        } else if (!TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2) || !TextUtils.isEmpty(str3)) {
            int parseColor2 = DAttrUtils.parseColor(str4, 0);
            GradientDrawable gradientDrawable2 = new GradientDrawable();
            int px = ScreenTool.getPx(view.getContext(), str, 0);
            int parseColor3 = DAttrUtils.parseColor(str2, parseColor2);
            int px2 = ScreenTool.getPx(view.getContext(), str3, 0);
            gradientDrawable2.setCornerRadius((float) px);
            gradientDrawable2.setShape(0);
            gradientDrawable2.setColor(parseColor2);
            if (px2 > 0) {
                gradientDrawable2.setStroke(px2, parseColor3);
            }
            if (Build.VERSION.SDK_INT >= 16) {
                view.setBackground(gradientDrawable2);
            } else {
                view.setBackgroundDrawable(gradientDrawable2);
            }
        } else {
            view.setBackgroundColor(DAttrUtils.parseColor(str4, 0));
        }
    }

    /* access modifiers changed from: protected */
    public ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }

    public Class getDebugClass() {
        try {
            return getClassLoader().loadClass(getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class MethodInfo {
        String[] attrSet;
        Method method;
        Class[] params;

        MethodInfo(Method method2, String[] strArr, Class[] clsArr) {
            this.method = method2;
            this.attrSet = strArr;
            this.params = clsArr;
        }
    }

    private void scanAllDinamicAttrMethods() {
        try {
            this.methodInfos = new ArrayList();
            for (Method method : getClassLoader().loadClass(getClass().getName()).getMethods()) {
                if (method.isAnnotationPresent(DinamicAttr.class)) {
                    String[] attrSet = ((DinamicAttr) method.getAnnotation(DinamicAttr.class)).attrSet();
                    Class[] parameterTypes = method.getParameterTypes();
                    if (attrSet.length > 0 && parameterTypes.length > 0 && parameterTypes.length - attrSet.length == 1) {
                        this.methodInfos.add(new MethodInfo(method, attrSet, parameterTypes));
                    } else if (Dinamic.isDebugable()) {
                        DinamicLog.d(Dinamic.TAG, "Senioronstructor scanAllDinamicAttrMethods function info error");
                    }
                }
            }
        } catch (Throwable th) {
            if (Dinamic.isDebugable()) {
                DinamicLog.w(Dinamic.TAG, th, "Senioronstructor scanAllDinamicAttrMethods exception");
            }
        }
    }

    private MethodInfo findMethodForAttr(String str) {
        for (MethodInfo next : this.methodInfos) {
            String[] strArr = next.attrSet;
            int length = strArr.length;
            int i = 0;
            while (true) {
                if (i < length) {
                    if (TextUtils.equals(str, strArr[i])) {
                        return next;
                    }
                    i++;
                }
            }
        }
        return null;
    }

    public boolean needBindData(View view, Map<String, Object> map, DinamicParams dinamicParams) {
        if (map.containsKey(DAttrConstant.VIEW_VISIBILITY)) {
            Object obj = map.get(DAttrConstant.VIEW_VISIBILITY);
            if (obj instanceof String) {
                setVisibility(view, (String) obj);
            }
        }
        return view.getVisibility() != 8;
    }

    private void setSpecificAttributes(View view, Map<String, Object> map, DinamicParams dinamicParams) {
        boolean z;
        MethodInfo findMethodForAttr;
        Object obj;
        Class[] clsArr;
        View view2 = view;
        Map<String, Object> map2 = map;
        if (this.methodInfos != null && this.methodInfos.size() != 0) {
            DinamicProperty viewProperty = DinamicViewUtils.getViewProperty(view);
            Map<String, Object> map3 = viewProperty.fixedProperty;
            ArrayList arrayList = new ArrayList();
            for (Map.Entry next : map.entrySet()) {
                Iterator it = arrayList.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (TextUtils.equals((String) it.next(), (CharSequence) next.getKey())) {
                            z = true;
                            break;
                        }
                    } else {
                        z = false;
                        break;
                    }
                }
                if (!z && (findMethodForAttr = findMethodForAttr((String) next.getKey())) != null) {
                    Method method = findMethodForAttr.method;
                    String[] strArr = findMethodForAttr.attrSet;
                    Class[] clsArr2 = findMethodForAttr.params;
                    if (strArr.length > 1) {
                        arrayList.addAll(Arrays.asList(strArr));
                    }
                    Object[] objArr = new Object[clsArr2.length];
                    if (!clsArr2[0].isInstance(view2)) {
                        dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_VIEW_EXCEPTION, viewProperty.viewIdentify);
                        if (Dinamic.isDebugable()) {
                            DinamicLog.i(Dinamic.TAG, "Senioronstructor first param class not match");
                        }
                    } else {
                        objArr[0] = view2;
                        int i = 0;
                        while (i < strArr.length) {
                            if ("module".equals(strArr[i])) {
                                obj = dinamicParams.getModule();
                            } else if (DAttrConstant.DINAMIC_CONTEXT.equals(strArr[i])) {
                                obj = dinamicParams.getDinamicContext();
                            } else if (DAttrConstant.DINAMIC_PARAMS.equals(strArr[i])) {
                                obj = dinamicParams;
                            } else if (map2.containsKey(strArr[i])) {
                                obj = map2.get(strArr[i]);
                            } else {
                                obj = map3.get(strArr[i]);
                            }
                            int i2 = i + 1;
                            if (clsArr2[i2].isInstance(obj)) {
                                objArr[i2] = obj;
                                clsArr = clsArr2;
                            } else {
                                if (obj != null) {
                                    dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_VIEW_EXCEPTION, viewProperty.viewIdentify);
                                }
                                if (Dinamic.isDebugable()) {
                                    clsArr = clsArr2;
                                    DinamicLog.i(Dinamic.TAG, String.format("AdvancedConstructor %s value is null or not exist", new Object[]{strArr[i]}));
                                } else {
                                    clsArr = clsArr2;
                                }
                                objArr[i2] = null;
                            }
                            i = i2;
                            clsArr2 = clsArr;
                            View view3 = view;
                        }
                        try {
                            method.invoke(this, objArr);
                        } catch (Exception e) {
                            Exception exc = e;
                            dinamicParams.getViewResult().getDinamicError().addErrorCodeWithInfo(DinamicError.ERROR_CODE_VIEW_EXCEPTION, viewProperty.viewIdentify);
                            if (Dinamic.isDebugable()) {
                                DinamicLog.w(Dinamic.TAG, exc, "AdvancedConstructor method invoke exception");
                            }
                        }
                        view2 = view;
                    }
                }
            }
        }
    }

    public void setEvents(View view, DinamicParams dinamicParams) {
        new DinamicEventHandlerWorker().bindEventHandler(view, dinamicParams);
    }
}
