package com.alibaba.android.bindingx.core;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.alibaba.android.bindingx.core.internal.BindingXOrientationHandler;
import com.alibaba.android.bindingx.core.internal.BindingXPinchHandler;
import com.alibaba.android.bindingx.core.internal.BindingXRotationHandler;
import com.alibaba.android.bindingx.core.internal.BindingXSpringHandler;
import com.alibaba.android.bindingx.core.internal.BindingXTimingHandler;
import com.alibaba.android.bindingx.core.internal.BindingXTouchHandler;
import com.alibaba.android.bindingx.core.internal.ExpressionPair;
import com.alibaba.android.bindingx.core.internal.Utils;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;

public class BindingXCore implements IHandlerCleanable {
    private static final Map<String, ObjectCreator<IEventHandler, Context, PlatformManager>> sGlobalEventHandlerCreatorMap = new HashMap(4);
    private Map<String, Map<String, IEventHandler>> mBindingCouples;
    private final Map<String, ObjectCreator<IEventHandler, Context, PlatformManager>> mInternalEventHandlerCreatorMap = new HashMap(8);
    private final PlatformManager mPlatformManager;

    public interface JavaScriptCallback {
        void callback(Object obj);
    }

    public interface ObjectCreator<Type, ParamA, ParamB> {
        Type createWith(@NonNull ParamA parama, @NonNull ParamB paramb, Object... objArr);
    }

    public BindingXCore(@NonNull PlatformManager platformManager) {
        this.mPlatformManager = platformManager;
        registerEventHandler("pan", new ObjectCreator<IEventHandler, Context, PlatformManager>() {
            public IEventHandler createWith(@NonNull Context context, @NonNull PlatformManager platformManager, Object... objArr) {
                return new BindingXTouchHandler(context, platformManager, objArr);
            }
        });
        registerEventHandler(BindingXEventType.TYPE_PINCH, new ObjectCreator<IEventHandler, Context, PlatformManager>() {
            public IEventHandler createWith(@NonNull Context context, @NonNull PlatformManager platformManager, Object... objArr) {
                return new BindingXPinchHandler(context, platformManager, objArr);
            }
        });
        registerEventHandler(BindingXEventType.TYPE_ROTATION, new ObjectCreator<IEventHandler, Context, PlatformManager>() {
            public IEventHandler createWith(@NonNull Context context, @NonNull PlatformManager platformManager, Object... objArr) {
                return new BindingXRotationHandler(context, platformManager, objArr);
            }
        });
        registerEventHandler("orientation", new ObjectCreator<IEventHandler, Context, PlatformManager>() {
            public IEventHandler createWith(@NonNull Context context, @NonNull PlatformManager platformManager, Object... objArr) {
                return new BindingXOrientationHandler(context, platformManager, objArr);
            }
        });
        registerEventHandler("timing", new ObjectCreator<IEventHandler, Context, PlatformManager>() {
            public IEventHandler createWith(@NonNull Context context, @NonNull PlatformManager platformManager, Object... objArr) {
                return new BindingXTimingHandler(context, platformManager, objArr);
            }
        });
        registerEventHandler(BindingXEventType.TYPE_SPRING, new ObjectCreator<IEventHandler, Context, PlatformManager>() {
            public IEventHandler createWith(@NonNull Context context, @NonNull PlatformManager platformManager, Object... objArr) {
                return new BindingXSpringHandler(context, platformManager, objArr);
            }
        });
    }

    public String doBind(@Nullable Context context, @Nullable String str, @NonNull Map<String, Object> map, @NonNull JavaScriptCallback javaScriptCallback, Object... objArr) {
        Map<String, Object> map2;
        Map<String, Object> map3 = map;
        String stringValue = Utils.getStringValue(map3, BindingXConstants.KEY_EVENT_TYPE);
        String stringValue2 = Utils.getStringValue(map3, BindingXConstants.KEY_INSTANCE_ID);
        LogProxy.enableLogIfNeeded(map);
        Object obj = map3.get("options");
        if (obj != null && (obj instanceof Map)) {
            try {
                map2 = Utils.toMap(new JSONObject((Map) obj));
            } catch (Exception e) {
                LogProxy.e("parse external config failed.\n", e);
            }
            ExpressionPair expressionPair = Utils.getExpressionPair(map3, "exitExpression");
            return doBind(Utils.getStringValue(map3, BindingXConstants.KEY_ANCHOR), stringValue2, stringValue, map2, expressionPair, Utils.getRuntimeProps(map), Utils.getCustomInterceptors(map), javaScriptCallback, context, str, map, objArr);
        }
        map2 = null;
        ExpressionPair expressionPair2 = Utils.getExpressionPair(map3, "exitExpression");
        return doBind(Utils.getStringValue(map3, BindingXConstants.KEY_ANCHOR), stringValue2, stringValue, map2, expressionPair2, Utils.getRuntimeProps(map), Utils.getCustomInterceptors(map), javaScriptCallback, context, str, map, objArr);
    }

    public void doUnbind(@Nullable Map<String, Object> map) {
        if (map != null) {
            doUnbind(Utils.getStringValue(map, "token"), Utils.getStringValue(map, BindingXConstants.KEY_EVENT_TYPE));
        }
    }

    public void doUnbind(@Nullable String str, @Nullable String str2) {
        LogProxy.d("disable binding [" + str + "," + str2 + Operators.ARRAY_END_STR);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            LogProxy.d("disable binding failed(0x1) [" + str + "," + str2 + Operators.ARRAY_END_STR);
        } else if (this.mBindingCouples == null || this.mBindingCouples.isEmpty()) {
            LogProxy.d("disable binding failed(0x2) [" + str + "," + str2 + Operators.ARRAY_END_STR);
        } else {
            Map map = this.mBindingCouples.get(str);
            if (map == null || map.isEmpty()) {
                LogProxy.d("disable binding failed(0x3) [" + str + "," + str2 + Operators.ARRAY_END_STR);
                return;
            }
            IEventHandler iEventHandler = (IEventHandler) map.get(str2);
            if (iEventHandler == null) {
                LogProxy.d("disable binding failed(0x4) [" + str + "," + str2 + Operators.ARRAY_END_STR);
            } else if (iEventHandler.onDisable(str, str2)) {
                this.mBindingCouples.remove(str);
                LogProxy.d("disable binding success[" + str + "," + str2 + Operators.ARRAY_END_STR);
            } else {
                LogProxy.d("disabled failed(0x4) [" + str + "," + str2 + Operators.ARRAY_END_STR);
            }
        }
    }

    public void doRelease() {
        if (this.mBindingCouples != null) {
            try {
                for (Map next : this.mBindingCouples.values()) {
                    if (next != null && !next.isEmpty()) {
                        for (IEventHandler iEventHandler : next.values()) {
                            if (iEventHandler != null) {
                                iEventHandler.onDestroy();
                            }
                        }
                    }
                }
                this.mBindingCouples.clear();
                this.mBindingCouples = null;
            } catch (Exception e) {
                LogProxy.e("release failed", e);
            }
        }
    }

    public String doPrepare(@Nullable Context context, @Nullable String str, @Nullable String str2, @Nullable String str3, @Nullable String str4, @Nullable Map<String, Object> map, @Nullable Map<String, Object> map2, @Nullable Object... objArr) {
        IEventHandler iEventHandler;
        if (TextUtils.isEmpty(str4)) {
            LogProxy.e("[doPrepare] failed. can not found eventType");
            return null;
        } else if (context == null) {
            LogProxy.e("[doPrepare] failed. context or wxInstance is null");
            return null;
        } else {
            if (TextUtils.isEmpty(str2)) {
                str2 = generateToken();
            }
            if (this.mBindingCouples == null) {
                this.mBindingCouples = new HashMap();
            }
            Map map3 = this.mBindingCouples.get(str2);
            if (map3 == null || (iEventHandler = (IEventHandler) map3.get(str4)) == null) {
                if (map3 == null) {
                    map3 = new HashMap(4);
                    this.mBindingCouples.put(str2, map3);
                }
                IEventHandler createEventHandler = createEventHandler(context, str, str4);
                if (createEventHandler != null) {
                    createEventHandler.setAnchorInstanceId(str3);
                    createEventHandler.setToken(str2);
                    createEventHandler.setGlobalConfig(map);
                    createEventHandler.setExtensionParams(objArr);
                    if (createEventHandler.onCreate(str2, str4)) {
                        createEventHandler.onStart(str2, str4);
                        map3.put(str4, createEventHandler);
                        if (LogProxy.sEnableLog) {
                            LogProxy.d("enableBinding success.[token:" + str2 + ",type:" + str4 + Operators.ARRAY_END_STR);
                        }
                    } else {
                        if (LogProxy.sEnableLog) {
                            LogProxy.e("expression enabled failed. [token:" + str2 + ",type:" + str4 + Operators.ARRAY_END_STR);
                        }
                        return null;
                    }
                } else {
                    LogProxy.e("unknown eventType: " + str4);
                    return null;
                }
            } else {
                if (LogProxy.sEnableLog) {
                    LogProxy.d("you have already enabled binding,[token:" + str2 + ",type:" + str4 + Operators.ARRAY_END_STR);
                }
                iEventHandler.setExtensionParams(objArr);
                iEventHandler.onStart(str2, str4);
                if (LogProxy.sEnableLog) {
                    LogProxy.d("enableBinding success.[token:" + str2 + ",type:" + str4 + Operators.ARRAY_END_STR);
                }
            }
            return str2;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v22, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v20, resolved type: com.alibaba.android.bindingx.core.IEventHandler} */
    /* JADX WARNING: Multi-variable type inference failed */
    @androidx.annotation.RestrictTo({androidx.annotation.RestrictTo.Scope.LIBRARY})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String doBind(@androidx.annotation.Nullable java.lang.String r15, @androidx.annotation.Nullable java.lang.String r16, @androidx.annotation.Nullable java.lang.String r17, @androidx.annotation.Nullable java.util.Map<java.lang.String, java.lang.Object> r18, @androidx.annotation.Nullable com.alibaba.android.bindingx.core.internal.ExpressionPair r19, @androidx.annotation.Nullable java.util.List<java.util.Map<java.lang.String, java.lang.Object>> r20, @androidx.annotation.Nullable java.util.Map<java.lang.String, com.alibaba.android.bindingx.core.internal.ExpressionPair> r21, @androidx.annotation.Nullable com.alibaba.android.bindingx.core.BindingXCore.JavaScriptCallback r22, @androidx.annotation.Nullable android.content.Context r23, @androidx.annotation.Nullable java.lang.String r24, @androidx.annotation.Nullable java.util.Map<java.lang.String, java.lang.Object> r25, @androidx.annotation.Nullable java.lang.Object... r26) {
        /*
            r14 = this;
            r9 = r14
            r10 = r15
            r11 = r17
            r12 = r20
            boolean r0 = android.text.TextUtils.isEmpty(r17)
            r1 = 0
            if (r0 != 0) goto L_0x00eb
            if (r12 != 0) goto L_0x0011
            goto L_0x00eb
        L_0x0011:
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, com.alibaba.android.bindingx.core.IEventHandler>> r0 = r9.mBindingCouples
            if (r0 == 0) goto L_0x002c
            boolean r0 = android.text.TextUtils.isEmpty(r15)
            if (r0 != 0) goto L_0x002c
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, com.alibaba.android.bindingx.core.IEventHandler>> r0 = r9.mBindingCouples
            java.lang.Object r0 = r0.get(r15)
            java.util.Map r0 = (java.util.Map) r0
            if (r0 == 0) goto L_0x002c
            java.lang.Object r0 = r0.get(r11)
            r1 = r0
            com.alibaba.android.bindingx.core.IEventHandler r1 = (com.alibaba.android.bindingx.core.IEventHandler) r1
        L_0x002c:
            r13 = r1
            if (r13 != 0) goto L_0x0087
            boolean r0 = com.alibaba.android.bindingx.core.LogProxy.sEnableLog
            if (r0 == 0) goto L_0x0054
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "binding not enabled,try auto enable it.[sourceRef:"
            r0.append(r1)
            r0.append(r15)
            java.lang.String r1 = ",eventType:"
            r0.append(r1)
            r0.append(r11)
            java.lang.String r1 = "]"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            com.alibaba.android.bindingx.core.LogProxy.d(r0)
        L_0x0054:
            r0 = r14
            r1 = r23
            r2 = r24
            r3 = r15
            r4 = r16
            r5 = r17
            r6 = r18
            r7 = r25
            r8 = r26
            java.lang.String r0 = r0.doPrepare(r1, r2, r3, r4, r5, r6, r7, r8)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L_0x0085
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, com.alibaba.android.bindingx.core.IEventHandler>> r1 = r9.mBindingCouples
            if (r1 == 0) goto L_0x0085
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, com.alibaba.android.bindingx.core.IEventHandler>> r1 = r9.mBindingCouples
            java.lang.Object r1 = r1.get(r0)
            java.util.Map r1 = (java.util.Map) r1
            if (r1 == 0) goto L_0x0085
            java.lang.Object r1 = r1.get(r11)
            com.alibaba.android.bindingx.core.IEventHandler r1 = (com.alibaba.android.bindingx.core.IEventHandler) r1
            r6 = r0
            r13 = r1
            goto L_0x0088
        L_0x0085:
            r6 = r0
            goto L_0x0088
        L_0x0087:
            r6 = r10
        L_0x0088:
            if (r13 == 0) goto L_0x00ca
            r0 = r25
            r13.setOriginalParams(r0)
            r0 = r13
            r1 = r17
            r2 = r18
            r3 = r19
            r4 = r20
            r5 = r22
            r0.onBindExpression(r1, r2, r3, r4, r5)
            boolean r0 = com.alibaba.android.bindingx.core.LogProxy.sEnableLog
            if (r0 == 0) goto L_0x00c4
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "createBinding success.[exitExp:"
            r0.append(r1)
            r1 = r19
            r0.append(r1)
            java.lang.String r1 = ",args:"
            r0.append(r1)
            r0.append(r12)
            java.lang.String r1 = "]"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            com.alibaba.android.bindingx.core.LogProxy.d(r0)
        L_0x00c4:
            r0 = r21
            r13.setInterceptors(r0)
            goto L_0x00ea
        L_0x00ca:
            boolean r0 = com.alibaba.android.bindingx.core.LogProxy.sEnableLog
            if (r0 == 0) goto L_0x00ea
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "internal error.binding failed for ref:"
            r0.append(r1)
            r0.append(r15)
            java.lang.String r1 = ",type:"
            r0.append(r1)
            r0.append(r11)
            java.lang.String r0 = r0.toString()
            com.alibaba.android.bindingx.core.LogProxy.e(r0)
        L_0x00ea:
            return r6
        L_0x00eb:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "doBind failed,illegal argument.["
            r0.append(r2)
            r0.append(r11)
            java.lang.String r2 = ","
            r0.append(r2)
            r0.append(r12)
            java.lang.String r2 = "]"
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            com.alibaba.android.bindingx.core.LogProxy.e(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.bindingx.core.BindingXCore.doBind(java.lang.String, java.lang.String, java.lang.String, java.util.Map, com.alibaba.android.bindingx.core.internal.ExpressionPair, java.util.List, java.util.Map, com.alibaba.android.bindingx.core.BindingXCore$JavaScriptCallback, android.content.Context, java.lang.String, java.util.Map, java.lang.Object[]):java.lang.String");
    }

    public void onActivityPause() {
        if (this.mBindingCouples != null) {
            try {
                for (Map<String, IEventHandler> values : this.mBindingCouples.values()) {
                    for (IEventHandler onActivityPause : values.values()) {
                        try {
                            onActivityPause.onActivityPause();
                        } catch (Exception e) {
                            LogProxy.e("execute activity pause failed.", e);
                        }
                    }
                }
            } catch (Exception e2) {
                LogProxy.e("activity pause failed", e2);
            }
        }
    }

    public void onActivityResume() {
        if (this.mBindingCouples != null) {
            try {
                for (Map<String, IEventHandler> values : this.mBindingCouples.values()) {
                    for (IEventHandler onActivityResume : values.values()) {
                        try {
                            onActivityResume.onActivityResume();
                        } catch (Exception e) {
                            LogProxy.e("execute activity pause failed.", e);
                        }
                    }
                }
            } catch (Exception e2) {
                LogProxy.e("activity pause failed", e2);
            }
        }
    }

    public void registerEventHandler(String str, ObjectCreator<IEventHandler, Context, PlatformManager> objectCreator) {
        if (!TextUtils.isEmpty(str) && objectCreator != null) {
            this.mInternalEventHandlerCreatorMap.put(str, objectCreator);
        }
    }

    public static void registerGlobalEventHandler(String str, ObjectCreator<IEventHandler, Context, PlatformManager> objectCreator) {
        if (!TextUtils.isEmpty(str) && objectCreator != null) {
            sGlobalEventHandlerCreatorMap.put(str, objectCreator);
        }
    }

    public static boolean unregisterGlobalEventHandler(String str) {
        return sGlobalEventHandlerCreatorMap.remove(str) != null;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: com.alibaba.android.bindingx.core.IEventHandler} */
    /* JADX WARNING: Multi-variable type inference failed */
    @androidx.annotation.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.alibaba.android.bindingx.core.IEventHandler createEventHandler(@androidx.annotation.NonNull android.content.Context r4, @androidx.annotation.Nullable java.lang.String r5, @androidx.annotation.NonNull java.lang.String r6) {
        /*
            r3 = this;
            java.util.Map<java.lang.String, com.alibaba.android.bindingx.core.BindingXCore$ObjectCreator<com.alibaba.android.bindingx.core.IEventHandler, android.content.Context, com.alibaba.android.bindingx.core.PlatformManager>> r0 = r3.mInternalEventHandlerCreatorMap
            boolean r0 = r0.isEmpty()
            r1 = 0
            if (r0 != 0) goto L_0x0038
            com.alibaba.android.bindingx.core.PlatformManager r0 = r3.mPlatformManager
            if (r0 != 0) goto L_0x000e
            goto L_0x0038
        L_0x000e:
            java.util.Map<java.lang.String, com.alibaba.android.bindingx.core.BindingXCore$ObjectCreator<com.alibaba.android.bindingx.core.IEventHandler, android.content.Context, com.alibaba.android.bindingx.core.PlatformManager>> r0 = r3.mInternalEventHandlerCreatorMap
            java.lang.Object r0 = r0.get(r6)
            com.alibaba.android.bindingx.core.BindingXCore$ObjectCreator r0 = (com.alibaba.android.bindingx.core.BindingXCore.ObjectCreator) r0
            if (r0 != 0) goto L_0x0021
            java.util.Map<java.lang.String, com.alibaba.android.bindingx.core.BindingXCore$ObjectCreator<com.alibaba.android.bindingx.core.IEventHandler, android.content.Context, com.alibaba.android.bindingx.core.PlatformManager>> r0 = sGlobalEventHandlerCreatorMap
            java.lang.Object r6 = r0.get(r6)
            r0 = r6
            com.alibaba.android.bindingx.core.BindingXCore$ObjectCreator r0 = (com.alibaba.android.bindingx.core.BindingXCore.ObjectCreator) r0
        L_0x0021:
            if (r0 == 0) goto L_0x0032
            com.alibaba.android.bindingx.core.PlatformManager r6 = r3.mPlatformManager
            r1 = 1
            java.lang.Object[] r1 = new java.lang.Object[r1]
            r2 = 0
            r1[r2] = r5
            java.lang.Object r4 = r0.createWith(r4, r6, r1)
            r1 = r4
            com.alibaba.android.bindingx.core.IEventHandler r1 = (com.alibaba.android.bindingx.core.IEventHandler) r1
        L_0x0032:
            if (r1 == 0) goto L_0x0037
            r1.setHandlerCleaner(r3)
        L_0x0037:
            return r1
        L_0x0038:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.bindingx.core.BindingXCore.createEventHandler(android.content.Context, java.lang.String, java.lang.String):com.alibaba.android.bindingx.core.IEventHandler");
    }

    public void cleanHandlerByToken(@NonNull String str) {
        if (this.mBindingCouples != null) {
            this.mBindingCouples.remove(str);
        }
    }
}
