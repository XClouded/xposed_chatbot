package com.alibaba.aliweex.adapter.module.broadcast;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weaver.broadcast.MessageCallback;
import com.taobao.weaver.broadcast.MessageChannel;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import java.util.HashMap;
import java.util.Map;

public class WXBroadcastModule extends WXModule {
    private static final String CHANNEL_INSTANCE_ID = "instanceId";
    private static final String CHANNEL_KEY = "name";
    private static final String CHANNEL_MESSAGE = "message";
    private static final String MESSAGE = "message";
    private static final String RESULT = "result";
    private Map<String, MessageChannel> messageTokenChannels = new HashMap();

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0060, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x008b, code lost:
        return;
     */
    @com.taobao.weex.annotation.JSMethod(uiThread = false)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void createChannel(com.alibaba.fastjson.JSONObject r4, com.taobao.weex.bridge.JSCallback r5, com.taobao.weex.bridge.JSCallback r6) {
        /*
            r3 = this;
            com.taobao.weex.WXSDKInstance r0 = r3.mWXSDKInstance
            if (r0 == 0) goto L_0x008f
            com.taobao.weex.WXSDKInstance r0 = r3.mWXSDKInstance
            android.content.Context r0 = r0.getContext()
            if (r0 == 0) goto L_0x008f
            if (r4 == 0) goto L_0x008f
            java.lang.String r0 = "name"
            java.lang.String r0 = r4.getString(r0)
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x008f
            java.lang.String r0 = "instanceId"
            java.lang.String r0 = r4.getString(r0)
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x0027
            goto L_0x008f
        L_0x0027:
            monitor-enter(r3)
            java.util.Map<java.lang.String, com.taobao.weaver.broadcast.MessageChannel> r0 = r3.messageTokenChannels     // Catch:{ all -> 0x008c }
            if (r0 != 0) goto L_0x0033
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ all -> 0x008c }
            r0.<init>()     // Catch:{ all -> 0x008c }
            r3.messageTokenChannels = r0     // Catch:{ all -> 0x008c }
        L_0x0033:
            java.lang.String r0 = "name"
            java.lang.String r0 = r4.getString(r0)     // Catch:{ all -> 0x008c }
            java.lang.String r1 = "instanceId"
            java.lang.String r4 = r4.getString(r1)     // Catch:{ all -> 0x008c }
            java.util.Map<java.lang.String, com.taobao.weaver.broadcast.MessageChannel> r1 = r3.messageTokenChannels     // Catch:{ all -> 0x008c }
            java.lang.Object r1 = r1.get(r4)     // Catch:{ all -> 0x008c }
            if (r1 == 0) goto L_0x0061
            if (r6 == 0) goto L_0x005f
            com.alibaba.fastjson.JSONObject r4 = new com.alibaba.fastjson.JSONObject     // Catch:{ all -> 0x008c }
            r4.<init>()     // Catch:{ all -> 0x008c }
            java.lang.String r5 = "result"
            java.lang.String r0 = "-1"
            r4.put((java.lang.String) r5, (java.lang.Object) r0)     // Catch:{ all -> 0x008c }
            java.lang.String r5 = "message"
            java.lang.String r0 = "channel error token has been used"
            r4.put((java.lang.String) r5, (java.lang.Object) r0)     // Catch:{ all -> 0x008c }
            r6.invoke(r4)     // Catch:{ all -> 0x008c }
        L_0x005f:
            monitor-exit(r3)     // Catch:{ all -> 0x008c }
            return
        L_0x0061:
            com.taobao.weex.WXSDKInstance r6 = r3.mWXSDKInstance     // Catch:{ all -> 0x008c }
            android.content.Context r6 = r6.getContext()     // Catch:{ all -> 0x008c }
            com.taobao.weaver.broadcast.MessageChannel r1 = new com.taobao.weaver.broadcast.MessageChannel     // Catch:{ all -> 0x008c }
            r2 = 0
            r1.<init>(r6, r0, r2)     // Catch:{ all -> 0x008c }
            java.util.Map<java.lang.String, com.taobao.weaver.broadcast.MessageChannel> r6 = r3.messageTokenChannels     // Catch:{ all -> 0x008c }
            r6.put(r4, r1)     // Catch:{ all -> 0x008c }
            if (r5 == 0) goto L_0x008a
            com.alibaba.fastjson.JSONObject r4 = new com.alibaba.fastjson.JSONObject     // Catch:{ all -> 0x008c }
            r4.<init>()     // Catch:{ all -> 0x008c }
            java.lang.String r6 = "result"
            java.lang.String r0 = "0"
            r4.put((java.lang.String) r6, (java.lang.Object) r0)     // Catch:{ all -> 0x008c }
            java.lang.String r6 = "message"
            java.lang.String r0 = "channel create success"
            r4.put((java.lang.String) r6, (java.lang.Object) r0)     // Catch:{ all -> 0x008c }
            r5.invoke(r4)     // Catch:{ all -> 0x008c }
        L_0x008a:
            monitor-exit(r3)     // Catch:{ all -> 0x008c }
            return
        L_0x008c:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x008c }
            throw r4
        L_0x008f:
            if (r6 == 0) goto L_0x00a7
            com.alibaba.fastjson.JSONObject r4 = new com.alibaba.fastjson.JSONObject
            r4.<init>()
            java.lang.String r5 = "result"
            java.lang.String r0 = "-1"
            r4.put((java.lang.String) r5, (java.lang.Object) r0)
            java.lang.String r5 = "message"
            java.lang.String r0 = "channel args error"
            r4.put((java.lang.String) r5, (java.lang.Object) r0)
            r6.invoke(r4)
        L_0x00a7:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.adapter.module.broadcast.WXBroadcastModule.createChannel(com.alibaba.fastjson.JSONObject, com.taobao.weex.bridge.JSCallback, com.taobao.weex.bridge.JSCallback):void");
    }

    @JSMethod(uiThread = false)
    public void onMessage(JSONObject jSONObject, final JSCallback jSCallback, JSCallback jSCallback2) {
        if (this.messageTokenChannels != null) {
            String string = jSONObject.getString("instanceId");
            if (!TextUtils.isEmpty(string)) {
                MessageChannel messageChannel = this.messageTokenChannels.get(string);
                if (messageChannel != null) {
                    messageChannel.setCallback(new MessageCallback() {
                        public void onMessage(Object obj) {
                            if (jSCallback != null) {
                                JSONObject jSONObject = new JSONObject();
                                jSONObject.put("result", (Object) "0");
                                jSONObject.put("message", obj);
                                jSCallback.invokeAndKeepAlive(obj);
                            }
                        }
                    });
                } else if (jSCallback2 != null) {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("result", (Object) "-1");
                    jSONObject2.put("message", (Object) "channel token not exist");
                    jSCallback2.invoke(jSONObject2);
                }
            } else if (jSCallback2 != null) {
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("result", (Object) "-1");
                jSONObject3.put("message", (Object) "channel token empty error");
                jSCallback2.invoke(jSONObject3);
            }
        }
    }

    @JSMethod(uiThread = false)
    public void postMessage(JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
        if (this.messageTokenChannels != null) {
            if (!TextUtils.isEmpty(jSONObject.getString("instanceId")) && jSONObject.containsKey("message")) {
                String string = jSONObject.getString("instanceId");
                Object obj = jSONObject.get("message");
                MessageChannel messageChannel = this.messageTokenChannels.get(string);
                if (messageChannel != null) {
                    messageChannel.postMessage(obj);
                    if (jSCallback != null) {
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put("result", (Object) "0");
                        jSONObject2.put("message", (Object) "post message success");
                        jSCallback.invoke(jSONObject2);
                    }
                } else if (jSCallback2 != null) {
                    JSONObject jSONObject3 = new JSONObject();
                    jSONObject3.put("result", (Object) "-1");
                    jSONObject3.put("message", (Object) "channel token not exist");
                    jSCallback2.invoke(jSONObject3);
                }
            } else if (jSCallback2 != null) {
                JSONObject jSONObject4 = new JSONObject();
                jSONObject4.put("result", (Object) "-1");
                jSONObject4.put("message", (Object) "post message args error");
                jSCallback2.invoke(jSONObject4);
            }
        }
    }

    @JSMethod(uiThread = false)
    public void closeChannel(JSONObject jSONObject) {
        MessageChannel remove;
        if (jSONObject != null && this.messageTokenChannels != null && jSONObject.containsKey("instanceId") && (remove = this.messageTokenChannels.remove(jSONObject.getString("instanceId"))) != null) {
            remove.close();
        }
    }

    public void onActivityDestroy() {
        if (this.messageTokenChannels != null) {
            for (Map.Entry next : this.messageTokenChannels.entrySet()) {
                if (next.getValue() != null) {
                    ((MessageChannel) next.getValue()).close();
                }
            }
            this.messageTokenChannels.clear();
        }
        super.onActivityDestroy();
    }
}
