package com.taobao.android.dinamicx.bindingx;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.alibaba.android.bindingx.core.PlatformManager;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.alibaba.android.bindingx.plugin.android.NativeBindingX;
import com.alibaba.android.bindingx.plugin.android.NativeCallback;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.android.dinamicx.DXBaseClass;
import com.taobao.android.dinamicx.DXEngineContext;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXMsgConstant;
import com.taobao.android.dinamicx.DXPublicConstant;
import com.taobao.android.dinamicx.DXRootView;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.expression.event.bindingx.DXBindingXStateChangeEvent;
import com.taobao.android.dinamicx.log.DXLog;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.widget.DXTextViewWidgetNode;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DXBindingXManager extends DXBaseClass {
    NativeBindingX bindingX;
    DXBindingXScrollHandler scrollHandler = new DXBindingXScrollHandler();

    public DXBindingXManager(@NonNull DXEngineContext dXEngineContext) {
        super(dXEngineContext);
        DXBindingXNativeViewFinder dXBindingXNativeViewFinder = new DXBindingXNativeViewFinder();
        DXBindingXViewUpdateManager dXBindingXViewUpdateManager = new DXBindingXViewUpdateManager();
        this.bindingX = NativeBindingX.create(dXBindingXNativeViewFinder, (PlatformManager.IDeviceResolutionTranslator) null, dXBindingXViewUpdateManager, this.scrollHandler);
    }

    public DXBindingXSpec parseBindingXSpec(String str, String str2, Map<String, Object> map) {
        JSONObject jSONObject;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || (jSONObject = JSONObject.parseObject(str2).getJSONObject(str)) == null) {
            return null;
        }
        DXBindingXSpec dXBindingXSpec = new DXBindingXSpec();
        dXBindingXSpec.name = str;
        dXBindingXSpec.eventType = jSONObject.getString(BindingXConstants.KEY_EVENT_TYPE);
        dXBindingXSpec.specOriginMap = jSONObject;
        if (map == null || !map.containsKey(DXBindingXConstant.RESET_ON_STOP)) {
            str3 = jSONObject.getString(DXBindingXConstant.RESET_ON_STOP);
        } else {
            str3 = (String) map.get(DXBindingXConstant.RESET_ON_STOP);
        }
        if (!TextUtils.isEmpty(str3)) {
            dXBindingXSpec.resetOnStop = !str3.equalsIgnoreCase("false");
        }
        if (map == null || !map.containsKey(DXBindingXConstant.RESET_ON_FINISH)) {
            str4 = jSONObject.getString(DXBindingXConstant.RESET_ON_FINISH);
        } else {
            str4 = (String) map.get(DXBindingXConstant.RESET_ON_FINISH);
        }
        if (!TextUtils.isEmpty(str4)) {
            dXBindingXSpec.resetOnFinish = !str4.equalsIgnoreCase("false");
        }
        if (!dXBindingXSpec.resetOnStop) {
            if (map == null || !map.containsKey(DXBindingXConstant.UPDATE_FLATTEN_ONLY_ON_STOP)) {
                str7 = jSONObject.getString(DXBindingXConstant.UPDATE_FLATTEN_ONLY_ON_STOP);
            } else {
                str7 = (String) map.get(DXBindingXConstant.UPDATE_FLATTEN_ONLY_ON_STOP);
            }
            if (!TextUtils.isEmpty(str7)) {
                dXBindingXSpec.updateFlattenOnlyOnStop = "true".equalsIgnoreCase(str7);
            }
        }
        if (!dXBindingXSpec.resetOnFinish) {
            if (map == null || !map.containsKey(DXBindingXConstant.UPDATE_FLATTEN_ONLY_ON_FINISH)) {
                str6 = jSONObject.getString(DXBindingXConstant.UPDATE_FLATTEN_ONLY_ON_FINISH);
            } else {
                str6 = (String) map.get(DXBindingXConstant.UPDATE_FLATTEN_ONLY_ON_FINISH);
            }
            if (!TextUtils.isEmpty(str6)) {
                dXBindingXSpec.updateFlattenOnlyOnFinish = "true".equalsIgnoreCase(str6);
            }
        }
        if (map == null || !map.containsKey(DXBindingXConstant.REPEAT)) {
            str5 = jSONObject.getString(DXBindingXConstant.REPEAT);
        } else {
            str5 = (String) map.get(DXBindingXConstant.REPEAT);
        }
        if (!TextUtils.isEmpty(str5)) {
            dXBindingXSpec.repeat = str5.equals("true");
        }
        dXBindingXSpec.propsJsonArray = jSONObject.getJSONArray("props");
        dXBindingXSpec.exitExpression = jSONObject.getJSONObject("exitExpression");
        processTransformed(dXBindingXSpec, map);
        return dXBindingXSpec;
    }

    private void processTransformed(DXBindingXSpec dXBindingXSpec, Map<String, Object> map) {
        JSONArray jSONArray = dXBindingXSpec.propsJsonArray;
        if (jSONArray != null) {
            int size = jSONArray.size();
            for (int i = 0; i < size; i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                transformPropAST(jSONObject.getJSONObject("expression").getJSONObject("transformed"), map, needProcessDefaultAP(jSONObject));
            }
        }
        JSONObject jSONObject2 = dXBindingXSpec.exitExpression;
        if (jSONObject2 != null) {
            transformPropAST(jSONObject2.getJSONObject("transformed"), map, false);
        }
    }

    private boolean needProcessDefaultAP(JSONObject jSONObject) {
        String string = jSONObject.getString("property");
        return "transform.translate".equals(string) || "transform.translateX".equals(string) || "transform.translateY".equals(string);
    }

    private void processPropValue(DXWidgetNode dXWidgetNode, JSONObject jSONObject, int i, boolean z) {
        DXWidgetNode dXWidgetNode2;
        Drawable background;
        DXWidgetNode dXWidgetNode3;
        String string = jSONObject.getString("property");
        if (jSONObject != null && !TextUtils.isEmpty(string)) {
            WeakReference weakReference = (WeakReference) jSONObject.get(DXBindingXConstant.ELEMENT_WIDGETNODE);
            if (weakReference == null || weakReference.get() == null) {
                String splitElement = splitElement(jSONObject.getString("element"));
                if ("this".equalsIgnoreCase(splitElement)) {
                    dXWidgetNode3 = dXWidgetNode;
                } else {
                    dXWidgetNode3 = dXWidgetNode.queryWTByUserId(splitElement);
                }
                dXWidgetNode2 = dXWidgetNode3 == null ? dXWidgetNode.queryWidgetNodeByUserId(splitElement) : dXWidgetNode3;
                if (dXWidgetNode2 != null) {
                    jSONObject.put(DXBindingXConstant.ELEMENT_WIDGETNODE, (Object) new WeakReference(dXWidgetNode2));
                } else {
                    return;
                }
            } else {
                dXWidgetNode2 = (DXWidgetNode) weakReference.get();
            }
            if ("opacity".equals(string)) {
                switch (i) {
                    case 1:
                        View nativeView = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView != null) {
                            if (!z) {
                                dXWidgetNode2.setAlpha(nativeView.getAlpha());
                            }
                            DXWidgetNode flatten = getFlatten(dXWidgetNode2);
                            if (flatten != null) {
                                flatten.setAlpha(nativeView.getAlpha());
                                return;
                            }
                            return;
                        }
                        return;
                    case 2:
                        View nativeView2 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView2 != null) {
                            nativeView2.setAlpha(dXWidgetNode2.getAlpha());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else if ("transform.translate".equals(string)) {
                switch (i) {
                    case 1:
                        View nativeView3 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView3 != null) {
                            float translationX = nativeView3.getTranslationX();
                            float translationY = nativeView3.getTranslationY();
                            if (!z) {
                                dXWidgetNode2.setTranslateX(translationX);
                                dXWidgetNode2.setTranslateY(translationY);
                            }
                            DXWidgetNode flatten2 = getFlatten(dXWidgetNode2);
                            if (flatten2 != null) {
                                flatten2.setTranslateX(translationX);
                                flatten2.setTranslateY(translationY);
                                return;
                            }
                            return;
                        }
                        return;
                    case 2:
                        View nativeView4 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView4 != null) {
                            nativeView4.setTranslationX(dXWidgetNode2.getTranslateX());
                            nativeView4.setTranslationY(dXWidgetNode2.getTranslateY());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else if ("transform.translateX".equals(string)) {
                switch (i) {
                    case 1:
                        View nativeView5 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView5 != null) {
                            float translationX2 = nativeView5.getTranslationX();
                            if (!z) {
                                dXWidgetNode2.setTranslateX(translationX2);
                            }
                            DXWidgetNode flatten3 = getFlatten(dXWidgetNode2);
                            if (flatten3 != null) {
                                flatten3.setTranslateX(translationX2);
                                return;
                            }
                            return;
                        }
                        return;
                    case 2:
                        View nativeView6 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView6 != null) {
                            nativeView6.setTranslationX(dXWidgetNode2.getTranslateX());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else if ("transform.translateY".equals(string)) {
                switch (i) {
                    case 1:
                        View nativeView7 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView7 != null) {
                            float translationY2 = nativeView7.getTranslationY();
                            if (!z) {
                                dXWidgetNode2.setTranslateY(translationY2);
                            }
                            DXWidgetNode flatten4 = getFlatten(dXWidgetNode2);
                            if (flatten4 != null) {
                                flatten4.setTranslateY(translationY2);
                                return;
                            }
                            return;
                        }
                        return;
                    case 2:
                        View nativeView8 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView8 != null) {
                            nativeView8.setTranslationY(dXWidgetNode2.getTranslateY());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else if ("transform.scale".equals(string)) {
                switch (i) {
                    case 1:
                        View nativeView9 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView9 != null) {
                            float scaleX = nativeView9.getScaleX();
                            float scaleY = nativeView9.getScaleY();
                            if (!z) {
                                dXWidgetNode2.setScaleX(scaleX);
                                dXWidgetNode2.setScaleY(scaleY);
                            }
                            DXWidgetNode flatten5 = getFlatten(dXWidgetNode2);
                            if (flatten5 != null) {
                                flatten5.setScaleX(scaleX);
                                flatten5.setScaleY(scaleY);
                                return;
                            }
                            return;
                        }
                        return;
                    case 2:
                        View nativeView10 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView10 != null) {
                            nativeView10.setScaleX(dXWidgetNode2.getScaleX());
                            nativeView10.setScaleY(dXWidgetNode2.getScaleY());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else if ("transform.scaleX".equals(string)) {
                switch (i) {
                    case 1:
                        View nativeView11 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView11 != null) {
                            float scaleX2 = nativeView11.getScaleX();
                            if (!z) {
                                dXWidgetNode2.setScaleX(scaleX2);
                            }
                            DXWidgetNode flatten6 = getFlatten(dXWidgetNode2);
                            if (flatten6 != null) {
                                flatten6.setScaleX(scaleX2);
                                return;
                            }
                            return;
                        }
                        return;
                    case 2:
                        View nativeView12 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView12 != null) {
                            nativeView12.setScaleX(dXWidgetNode2.getScaleX());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else if ("transform.scaleY".equals(string)) {
                switch (i) {
                    case 1:
                        View nativeView13 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView13 != null) {
                            float scaleY2 = nativeView13.getScaleY();
                            if (!z) {
                                dXWidgetNode2.setScaleY(scaleY2);
                            }
                            DXWidgetNode flatten7 = getFlatten(dXWidgetNode2);
                            if (flatten7 != null) {
                                flatten7.setScaleY(scaleY2);
                                return;
                            }
                            return;
                        }
                        return;
                    case 2:
                        View nativeView14 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView14 != null) {
                            nativeView14.setScaleY(dXWidgetNode2.getScaleY());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else if ("transform.rotateX".equals(string)) {
                switch (i) {
                    case 1:
                        View nativeView15 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView15 != null) {
                            float rotationX = nativeView15.getRotationX();
                            if (!z) {
                                dXWidgetNode2.setRotationX(rotationX);
                            }
                            DXWidgetNode flatten8 = getFlatten(dXWidgetNode2);
                            if (flatten8 != null) {
                                flatten8.setRotationX(rotationX);
                                return;
                            }
                            return;
                        }
                        return;
                    case 2:
                        View nativeView16 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView16 != null) {
                            nativeView16.setRotationX(dXWidgetNode2.getRotationX());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else if ("transform.rotateY".equals(string)) {
                switch (i) {
                    case 1:
                        View nativeView17 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView17 != null) {
                            float rotationY = nativeView17.getRotationY();
                            if (!z) {
                                dXWidgetNode2.setRotationY(rotationY);
                            }
                            DXWidgetNode flatten9 = getFlatten(dXWidgetNode2);
                            if (flatten9 != null) {
                                flatten9.setRotationY(rotationY);
                                return;
                            }
                            return;
                        }
                        return;
                    case 2:
                        View nativeView18 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView18 != null) {
                            nativeView18.setRotationY(dXWidgetNode2.getRotationY());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else if ("transform.rotateZ".equals(string)) {
                switch (i) {
                    case 1:
                        View nativeView19 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView19 != null) {
                            float rotation = nativeView19.getRotation();
                            if (!z) {
                                dXWidgetNode2.setRotationZ(rotation);
                            }
                            DXWidgetNode flatten10 = getFlatten(dXWidgetNode2);
                            if (flatten10 != null) {
                                flatten10.setRotationZ(rotation);
                                return;
                            }
                            return;
                        }
                        return;
                    case 2:
                        View nativeView20 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView20 != null) {
                            nativeView20.setRotation(dXWidgetNode2.getRotationZ());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else if ("background-color".equals(string)) {
                switch (i) {
                    case 1:
                        View nativeView21 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView21 != null && (background = nativeView21.getBackground()) != null && (background instanceof ColorDrawable)) {
                            if (!z) {
                                dXWidgetNode2.setBackGroundColor(((ColorDrawable) background).getColor());
                            }
                            DXWidgetNode flatten11 = getFlatten(dXWidgetNode2);
                            if (flatten11 != null) {
                                flatten11.setBackGroundColor(((ColorDrawable) background).getColor());
                                return;
                            }
                            return;
                        }
                        return;
                    case 2:
                        View nativeView22 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView22 != null) {
                            nativeView22.setBackgroundColor(dXWidgetNode2.getBackGroundColor());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            } else if ("color".equals(string) && (dXWidgetNode2 instanceof DXTextViewWidgetNode)) {
                switch (i) {
                    case 1:
                        View nativeView23 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView23 != null && (nativeView23 instanceof TextView)) {
                            int currentTextColor = ((TextView) nativeView23).getCurrentTextColor();
                            if (!z) {
                                ((DXTextViewWidgetNode) dXWidgetNode2).setTextColor(currentTextColor);
                            }
                            DXWidgetNode flatten12 = getFlatten(dXWidgetNode2);
                            if (flatten12 != null && (flatten12 instanceof DXTextViewWidgetNode)) {
                                ((DXTextViewWidgetNode) flatten12).setTextColor(currentTextColor);
                                return;
                            }
                            return;
                        }
                        return;
                    case 2:
                        View nativeView24 = dXWidgetNode2.getDXRuntimeContext().getNativeView();
                        if (nativeView24 != null && (nativeView24 instanceof TextView)) {
                            ((TextView) nativeView24).setTextColor(((DXTextViewWidgetNode) dXWidgetNode2).getTextColor());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private void transformPropAST(JSONObject jSONObject, Map<String, Object> map, boolean z) {
        int i;
        if (jSONObject != null) {
            String string = jSONObject.getString("type");
            Object obj = jSONObject.get("value");
            if (obj instanceof String) {
                String str = (String) obj;
                if (str.startsWith("$")) {
                    obj = processDollar(map, str, string);
                }
            }
            if (z) {
                try {
                    if (DXBindingXConstant.NUMERIC_LITERRAL.equals(string) && (obj instanceof String)) {
                        String str2 = (String) obj;
                        if (str2.endsWith(DXBindingXConstant.AP)) {
                            obj = Integer.valueOf(DXScreenTool.ap2px(DinamicXEngine.getApplicationContext(), Float.parseFloat(str2.substring(0, str2.length() - 2))));
                        } else if (((String) obj).endsWith(DXBindingXConstant.NP)) {
                            obj = Integer.valueOf(DXScreenTool.dip2px(DinamicXEngine.getApplicationContext(), Float.parseFloat(str2.substring(0, str2.length() - 2))));
                        }
                    }
                } catch (Throwable th) {
                    DXExceptionUtil.printStack(th);
                    i = 0;
                }
            }
            i = obj;
            jSONObject.put("value", i);
            JSONArray jSONArray = jSONObject.getJSONArray("children");
            if (jSONArray != null && jSONArray.size() > 0) {
                for (int i2 = 0; i2 < jSONArray.size(); i2++) {
                    transformPropAST(jSONArray.getJSONObject(i2), map, z);
                }
            }
        }
    }

    private Object processDollar(Map<String, Object> map, String str, String str2) {
        if (str.length() < 2) {
            return str;
        }
        String substring = str.substring(1);
        if (map == null || !map.containsKey(substring)) {
            return "";
        }
        Object obj = map.get(substring);
        if (!DXBindingXConstant.STRING_LITERRAL.equals(str2) || str.startsWith(DXBindingXConstant.SINGLE_QUOTE)) {
            return obj;
        }
        return DXBindingXConstant.SINGLE_QUOTE + obj + DXBindingXConstant.SINGLE_QUOTE;
    }

    public static String splitElement(String str) {
        return (TextUtils.isEmpty(str) || !str.startsWith(DinamicConstant.DINAMIC_PREFIX_AT)) ? str : str.substring(1);
    }

    public void startAnimation(DXRootView dXRootView, DXWidgetNode dXWidgetNode, JSONArray jSONArray, Map<String, Object> map) {
        if (jSONArray != null && !jSONArray.isEmpty()) {
            for (int i = 0; i < jSONArray.size(); i++) {
                startAnimation(dXRootView, dXWidgetNode, jSONArray.getString(i), map);
            }
        }
    }

    private DXWidgetNode getFlatten(DXWidgetNode dXWidgetNode) {
        if (dXWidgetNode == null) {
            return null;
        }
        if (dXWidgetNode.isFlatten()) {
            return dXWidgetNode;
        }
        return dXWidgetNode.getReferenceNode();
    }

    public void startAnimation(DXRootView dXRootView, DXWidgetNode dXWidgetNode, String str, Map<String, Object> map) {
        DXWidgetNode expandWidgetNode = dXRootView.getExpandWidgetNode();
        if (dXRootView != null && dXWidgetNode != null && this.bindingX != null && !TextUtils.isEmpty(expandWidgetNode.getAnimation()) && expandWidgetNode.getDXRuntimeContext() != null && !TextUtils.isEmpty(str) && !dXWidgetNode.containsExecutingAnimationSpec(str)) {
            DXBindingXSpec dXBindingXSpec = null;
            Map<String, DXBindingXSpec> bindingXSpecMap = dXWidgetNode.getBindingXSpecMap();
            if ((map == null || map.isEmpty()) && bindingXSpecMap != null) {
                dXBindingXSpec = bindingXSpecMap.get(str);
            }
            if (dXBindingXSpec == null) {
                dXBindingXSpec = parseBindingXSpec(str, expandWidgetNode.getAnimation(), map);
            }
            if (dXBindingXSpec != null) {
                if (bindingXSpecMap == null) {
                    HashMap hashMap = new HashMap();
                    hashMap.put(str, dXBindingXSpec);
                    dXWidgetNode.setBindingXSpecMap(hashMap);
                } else {
                    bindingXSpecMap.put(str, dXBindingXSpec);
                }
                if (dXBindingXSpec.token == null) {
                    bindAnimation(dXRootView, dXBindingXSpec, dXWidgetNode);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void bindAnimation(final DXRootView dXRootView, final DXBindingXSpec dXBindingXSpec, final DXWidgetNode dXWidgetNode) {
        View nativeView = dXWidgetNode.getDXRuntimeContext().getNativeView();
        if (nativeView != null) {
            nativeView.setTag(DXPublicConstant.TAG_ANIMATION_EXPANDED_WIDGET_ON_VIEW, dXWidgetNode);
        }
        Map<String, Object> bind = this.bindingX.bind(nativeView, (Map<String, Object>) dXBindingXSpec.specOriginMap, (NativeCallback) new NativeCallback() {
            public void callback(Map<String, Object> map) {
                if (map != null) {
                    String str = null;
                    try {
                        if (DinamicXEngine.isDebug()) {
                            DXLog.print(map.toString());
                        }
                        String str2 = (String) map.get("state");
                        String str3 = (String) map.get("token");
                        if ("exit".equals(str2)) {
                            if (dXBindingXSpec.token != null && ((String) dXBindingXSpec.token.get("token")).equalsIgnoreCase(str3)) {
                                DXBindingXManager.this.updateToken((Map<String, Object>) null, dXBindingXSpec);
                                if (dXBindingXSpec.repeat && dXBindingXSpec.eventType.equalsIgnoreCase("timing")) {
                                    DXBindingXManager.this.bindAnimation(dXRootView, dXBindingXSpec, dXWidgetNode);
                                } else if (dXBindingXSpec.resetOnFinish) {
                                    DXBindingXManager.this.syncWidgetViewProperty(dXWidgetNode, dXBindingXSpec, 2, dXBindingXSpec.updateFlattenOnlyOnFinish);
                                } else {
                                    DXBindingXManager.this.syncWidgetViewProperty(dXWidgetNode, dXBindingXSpec, 1, dXBindingXSpec.updateFlattenOnlyOnFinish);
                                }
                                DXBindingXManager.this.removeSpec(dXRootView, dXWidgetNode, dXBindingXSpec);
                                DXBindingXManager.this.postEvent(dXWidgetNode, DXBindingXStateChangeEvent.DXVIEWWIDGETNODE_ONBINDINGXFINISH, dXBindingXSpec.name);
                            }
                        } else if ("start".equalsIgnoreCase(str2)) {
                            if (dXBindingXSpec.eventType.equalsIgnoreCase("timing")) {
                                DXBindingXManager.this.postEvent(dXWidgetNode, DXBindingXStateChangeEvent.DXVIEWWIDGETNODE_ONBINDINGXSTART, dXBindingXSpec.name);
                            }
                        } else if (!"end".equalsIgnoreCase(str2)) {
                            if (DXBindingXConstant.STATE_SCROLL_START.equalsIgnoreCase(str2)) {
                                DXBindingXManager.this.postEvent(dXWidgetNode, DXBindingXStateChangeEvent.DXVIEWWIDGETNODE_ONBINDINGXSTART, dXBindingXSpec.name);
                            } else if (DXBindingXConstant.STATE_SCROLL_END.equalsIgnoreCase(str2)) {
                                if (dXBindingXSpec.exitExpression == null || dXBindingXSpec.exitExpression.isEmpty()) {
                                    if (dXBindingXSpec.resetOnFinish) {
                                        DXBindingXManager.this.syncWidgetViewProperty(dXWidgetNode, dXBindingXSpec, 2, dXBindingXSpec.updateFlattenOnlyOnFinish);
                                    } else {
                                        DXBindingXManager.this.syncWidgetViewProperty(dXWidgetNode, dXBindingXSpec, 1, dXBindingXSpec.updateFlattenOnlyOnFinish);
                                    }
                                }
                                DXBindingXManager.this.postEvent(dXWidgetNode, DXBindingXStateChangeEvent.DXVIEWWIDGETNODE_ONBINDINGXSTOP, dXBindingXSpec.name);
                            }
                        }
                    } catch (Throwable th) {
                        DXExceptionUtil.printStack(th);
                        if (!(dXWidgetNode == null || dXWidgetNode.getDXRuntimeContext() == null)) {
                            str = dXWidgetNode.getDXRuntimeContext().getBizType();
                        }
                        if (TextUtils.isEmpty(str)) {
                            str = "dinamicx";
                        }
                        DXAppMonitor.trackerError(str, (DXTemplateItem) null, DXMonitorConstant.DX_MONITOR_BINDINGX, DXMonitorConstant.DX_BINDINGX_CRASH, DXError.BINDINGX_BINDINGX_CALL_BACK_CRASH, DXExceptionUtil.getStackTrace(th));
                    }
                }
            }
        });
        if (bind != null && !bind.isEmpty()) {
            updateToken(bind, dXBindingXSpec);
            putSpec(dXRootView, dXWidgetNode, dXBindingXSpec);
        }
    }

    private void putSpec(DXRootView dXRootView, DXWidgetNode dXWidgetNode, DXBindingXSpec dXBindingXSpec) {
        if (dXRootView != null && dXWidgetNode != null && dXBindingXSpec != null) {
            dXWidgetNode.putBindingXExecutingSpec(dXBindingXSpec);
            dXRootView._addAnimationWidget(dXWidgetNode);
        }
    }

    /* access modifiers changed from: private */
    public void removeSpec(DXRootView dXRootView, DXWidgetNode dXWidgetNode, DXBindingXSpec dXBindingXSpec) {
        if (dXRootView != null && dXWidgetNode != null && dXBindingXSpec != null) {
            dXWidgetNode.removeBindingXSpec(dXBindingXSpec);
            if (!dXWidgetNode.hasExecutingAnimationSpec()) {
                dXRootView._removeAnimationWidget(dXWidgetNode);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void postEvent(DXWidgetNode dXWidgetNode, long j, String str) {
        if (dXWidgetNode != null) {
            dXWidgetNode.postEvent(new DXBindingXStateChangeEvent(j, str));
        }
    }

    /* access modifiers changed from: private */
    public void updateToken(Map<String, Object> map, DXBindingXSpec dXBindingXSpec) {
        dXBindingXSpec.token = map;
    }

    /* access modifiers changed from: private */
    public void syncWidgetViewProperty(DXWidgetNode dXWidgetNode, DXBindingXSpec dXBindingXSpec, int i, boolean z) {
        JSONArray jSONArray = dXBindingXSpec.propsJsonArray;
        int size = jSONArray.size();
        for (int i2 = 0; i2 < size; i2++) {
            processPropValue(dXWidgetNode, jSONArray.getJSONObject(i2), i, z);
        }
    }

    public void stopAnimation(DXRootView dXRootView, DXWidgetNode dXWidgetNode, JSONArray jSONArray) {
        Map<String, DXBindingXSpec> bindingXExecutingMap;
        if (this.bindingX != null && dXWidgetNode.getDXRuntimeContext() != null && dXRootView != null && dXRootView._containAnimationWidget(dXWidgetNode) && (bindingXExecutingMap = dXWidgetNode.getBindingXExecutingMap()) != null && !bindingXExecutingMap.isEmpty()) {
            if (jSONArray == null || jSONArray.isEmpty()) {
                for (DXBindingXSpec next : bindingXExecutingMap.values()) {
                    if (next != null) {
                        unBindAnimation(dXRootView, dXWidgetNode, next, false, next.resetOnStop, next.updateFlattenOnlyOnStop);
                    }
                }
                bindingXExecutingMap.clear();
                dXRootView._removeAnimationWidget(dXWidgetNode);
                return;
            }
            for (int i = 0; i < jSONArray.size(); i++) {
                DXBindingXSpec dXBindingXSpec = bindingXExecutingMap.get(jSONArray.getString(i));
                if (dXBindingXSpec != null) {
                    unBindAnimation(dXRootView, dXWidgetNode, dXBindingXSpec, true, dXBindingXSpec.resetOnStop, dXBindingXSpec.updateFlattenOnlyOnStop);
                }
            }
        }
    }

    public void resetAnimationOnRootView(DXRootView dXRootView) {
        List<DXWidgetNode> _getAnimationWidgets;
        if (dXRootView != null && (_getAnimationWidgets = dXRootView._getAnimationWidgets()) != null && !_getAnimationWidgets.isEmpty()) {
            for (DXWidgetNode next : _getAnimationWidgets) {
                Map<String, DXBindingXSpec> bindingXExecutingMap = next.getBindingXExecutingMap();
                if (bindingXExecutingMap != null && bindingXExecutingMap.size() > 0) {
                    for (DXBindingXSpec unBindAnimation : bindingXExecutingMap.values()) {
                        unBindAnimation(dXRootView, next, unBindAnimation, false, true, false);
                    }
                    bindingXExecutingMap.clear();
                }
            }
            dXRootView._clearAnimationWidgets();
        }
    }

    public void processDXMsg(DXRootView dXRootView, JSONObject jSONObject) {
        JSONObject jSONObject2;
        if (this.bindingX != null && jSONObject != null && (jSONObject2 = jSONObject.getJSONObject("params")) != null) {
            String string = jSONObject2.getString("action");
            if (DXMsgConstant.DX_MSG_ACTION_SCROLLING_BINDINGX.equalsIgnoreCase(string) || DXMsgConstant.DX_MSG_ACTION_SCROLL_BEGIN_BINDINGX.equalsIgnoreCase(string) || DXMsgConstant.DX_MSG_ACTION_SCROLL_END_BINDINGX.equalsIgnoreCase(string)) {
                processScrollAction(jSONObject2, string);
            } else if (dXRootView != null) {
                JSONArray jSONArray = jSONObject2.getJSONArray(DXMsgConstant.DX_MSG_SPEC);
                Object obj = jSONObject2.get(DXMsgConstant.DX_MSG_WIDGET);
                if (obj instanceof DXWidgetNode) {
                    DXWidgetNode dXWidgetNode = (DXWidgetNode) obj;
                    if (dXWidgetNode.queryRootWidgetNode() == dXRootView.getExpandWidgetNode()) {
                        JSONObject jSONObject3 = jSONObject2.getJSONObject("args");
                        if ("start".equalsIgnoreCase(string)) {
                            startAnimation(dXRootView, dXWidgetNode, jSONArray, (Map<String, Object>) jSONObject3);
                        } else if ("stop".equalsIgnoreCase(string)) {
                            stopAnimation(dXRootView, dXWidgetNode, jSONArray);
                        }
                    }
                }
            }
        }
    }

    private void processScrollAction(JSONObject jSONObject, String str) {
        if (jSONObject != null && this.scrollHandler != null) {
            String string = jSONObject.getString(DXMsgConstant.DX_MSG_SOURCE_ID);
            int i = 0;
            int intValue = jSONObject.containsKey(DXMsgConstant.DX_MSG_OFFSET_X) ? jSONObject.getInteger(DXMsgConstant.DX_MSG_OFFSET_X).intValue() : 0;
            if (jSONObject.containsKey(DXMsgConstant.DX_MSG_OFFSET_Y)) {
                i = jSONObject.getInteger(DXMsgConstant.DX_MSG_OFFSET_Y).intValue();
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject("args");
            if (DXMsgConstant.DX_MSG_ACTION_SCROLL_BEGIN_BINDINGX.equalsIgnoreCase(str)) {
                this.scrollHandler.postScrollStartMessage(string, intValue, i, jSONObject2);
            } else if (DXMsgConstant.DX_MSG_ACTION_SCROLLING_BINDINGX.equalsIgnoreCase(str)) {
                this.scrollHandler.postScrollingMessage(string, intValue, i, jSONObject2);
            } else if (DXMsgConstant.DX_MSG_ACTION_SCROLL_END_BINDINGX.equalsIgnoreCase(str)) {
                this.scrollHandler.postScrollEndMessage(string, intValue, i, jSONObject2);
            }
        }
    }

    public NativeBindingX getBindingX() {
        return this.bindingX;
    }

    public DXBindingXScrollHandler getScrollHandler() {
        return this.scrollHandler;
    }

    private void unBindAnimation(DXRootView dXRootView, DXWidgetNode dXWidgetNode, DXBindingXSpec dXBindingXSpec, boolean z, boolean z2, boolean z3) {
        if (dXBindingXSpec != null && dXBindingXSpec.token != null) {
            this.bindingX.unbind(dXBindingXSpec.token);
            updateToken((Map<String, Object>) null, dXBindingXSpec);
            if (z2) {
                syncWidgetViewProperty(dXWidgetNode, dXBindingXSpec, 2, z3);
            } else {
                syncWidgetViewProperty(dXWidgetNode, dXBindingXSpec, 1, z3);
            }
            if (z) {
                removeSpec(dXRootView, dXWidgetNode, dXBindingXSpec);
            }
            if ("timing".equalsIgnoreCase(dXBindingXSpec.eventType)) {
                postEvent(dXWidgetNode, DXBindingXStateChangeEvent.DXVIEWWIDGETNODE_ONBINDINGXSTOP, dXBindingXSpec.name);
            }
        }
    }
}
