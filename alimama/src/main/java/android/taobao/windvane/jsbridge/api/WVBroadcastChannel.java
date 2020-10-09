package android.taobao.windvane.jsbridge.api;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weaver.broadcast.MessageCallback;
import com.taobao.weaver.broadcast.MessageChannel;
import com.taobao.weex.ui.component.WXWeb;

import java.util.HashMap;
import java.util.Map;

public class WVBroadcastChannel extends WVApiPlugin {
    private static final String CHANNEL_INSTANCE_ID = "instanceId";
    private static final String CHANNEL_KEY = "name";
    private static final String CHANNEL_MESSAGE = "message";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_RESULT = "result";
    private Map<String, MessageChannel> messageTokenChannels = new HashMap();

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        try {
            JSONObject parseObject = JSONObject.parseObject(str2);
            if ("createChannel".equals(str)) {
                createChannel(parseObject, wVCallBackContext);
                return true;
            } else if ("closeChannel".equals(str)) {
                closeChannel(parseObject);
                return true;
            } else if (!WXWeb.POST_MESSAGE.equals(str)) {
                return false;
            } else {
                postMessage(parseObject, wVCallBackContext);
                return true;
            }
        } catch (Throwable th) {
            th.printStackTrace();
            return true;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0064, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void createChannel(com.alibaba.fastjson.JSONObject r6, android.taobao.windvane.jsbridge.WVCallBackContext r7) {
        /*
            r5 = this;
            if (r7 == 0) goto L_0x009c
            android.taobao.windvane.webview.IWVWebView r0 = r7.getWebview()
            android.content.Context r0 = r0.getContext()
            if (r0 == 0) goto L_0x009c
            if (r6 == 0) goto L_0x009c
            java.lang.String r0 = "name"
            java.lang.String r0 = r6.getString(r0)
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x009c
            java.lang.String r0 = "instanceId"
            java.lang.String r0 = r6.getString(r0)
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x0027
            goto L_0x009c
        L_0x0027:
            monitor-enter(r5)
            java.util.Map<java.lang.String, com.taobao.weaver.broadcast.MessageChannel> r0 = r5.messageTokenChannels     // Catch:{ all -> 0x0099 }
            if (r0 != 0) goto L_0x0033
            java.util.HashMap r0 = new java.util.HashMap     // Catch:{ all -> 0x0099 }
            r0.<init>()     // Catch:{ all -> 0x0099 }
            r5.messageTokenChannels = r0     // Catch:{ all -> 0x0099 }
        L_0x0033:
            java.lang.String r0 = "name"
            java.lang.String r0 = r6.getString(r0)     // Catch:{ all -> 0x0099 }
            java.lang.String r1 = "instanceId"
            java.lang.String r1 = r6.getString(r1)     // Catch:{ all -> 0x0099 }
            java.util.Map<java.lang.String, com.taobao.weaver.broadcast.MessageChannel> r2 = r5.messageTokenChannels     // Catch:{ all -> 0x0099 }
            java.lang.Object r2 = r2.get(r1)     // Catch:{ all -> 0x0099 }
            if (r2 == 0) goto L_0x0065
            if (r7 == 0) goto L_0x0063
            com.alibaba.fastjson.JSONObject r6 = new com.alibaba.fastjson.JSONObject     // Catch:{ all -> 0x0099 }
            r6.<init>()     // Catch:{ all -> 0x0099 }
            java.lang.String r0 = "result"
            java.lang.String r1 = "-1"
            r6.put((java.lang.String) r0, (java.lang.Object) r1)     // Catch:{ all -> 0x0099 }
            java.lang.String r0 = "message"
            java.lang.String r1 = "channel error token has been used"
            r6.put((java.lang.String) r0, (java.lang.Object) r1)     // Catch:{ all -> 0x0099 }
            java.lang.String r6 = r6.toJSONString()     // Catch:{ all -> 0x0099 }
            r7.error((java.lang.String) r6)     // Catch:{ all -> 0x0099 }
        L_0x0063:
            monitor-exit(r5)     // Catch:{ all -> 0x0099 }
            return
        L_0x0065:
            android.taobao.windvane.webview.IWVWebView r2 = r7.getWebview()     // Catch:{ all -> 0x0099 }
            android.content.Context r2 = r2.getContext()     // Catch:{ all -> 0x0099 }
            com.taobao.weaver.broadcast.MessageChannel r3 = new com.taobao.weaver.broadcast.MessageChannel     // Catch:{ all -> 0x0099 }
            r4 = 0
            r3.<init>(r2, r0, r4)     // Catch:{ all -> 0x0099 }
            java.util.Map<java.lang.String, com.taobao.weaver.broadcast.MessageChannel> r0 = r5.messageTokenChannels     // Catch:{ all -> 0x0099 }
            r0.put(r1, r3)     // Catch:{ all -> 0x0099 }
            if (r7 == 0) goto L_0x0094
            com.alibaba.fastjson.JSONObject r0 = new com.alibaba.fastjson.JSONObject     // Catch:{ all -> 0x0099 }
            r0.<init>()     // Catch:{ all -> 0x0099 }
            java.lang.String r1 = "result"
            java.lang.String r2 = "0"
            r0.put((java.lang.String) r1, (java.lang.Object) r2)     // Catch:{ all -> 0x0099 }
            java.lang.String r1 = "message"
            java.lang.String r2 = "channel create success"
            r0.put((java.lang.String) r1, (java.lang.Object) r2)     // Catch:{ all -> 0x0099 }
            java.lang.String r0 = r0.toJSONString()     // Catch:{ all -> 0x0099 }
            r7.success((java.lang.String) r0)     // Catch:{ all -> 0x0099 }
        L_0x0094:
            r5.onMessage(r6, r7)     // Catch:{ all -> 0x0099 }
            monitor-exit(r5)     // Catch:{ all -> 0x0099 }
            return
        L_0x0099:
            r6 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0099 }
            throw r6
        L_0x009c:
            com.alibaba.fastjson.JSONObject r6 = new com.alibaba.fastjson.JSONObject
            r6.<init>()
            java.lang.String r0 = "result"
            java.lang.String r1 = "-1"
            r6.put((java.lang.String) r0, (java.lang.Object) r1)
            java.lang.String r0 = "message"
            java.lang.String r1 = "channel args error"
            r6.put((java.lang.String) r0, (java.lang.Object) r1)
            android.taobao.windvane.jsbridge.WVResult r0 = new android.taobao.windvane.jsbridge.WVResult
            java.lang.String r6 = r6.toJSONString()
            r0.<init>(r6)
            r7.error((android.taobao.windvane.jsbridge.WVResult) r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.api.WVBroadcastChannel.createChannel(com.alibaba.fastjson.JSONObject, android.taobao.windvane.jsbridge.WVCallBackContext):void");
    }

    private void onMessage(JSONObject jSONObject, final WVCallBackContext wVCallBackContext) {
        if (this.messageTokenChannels != null) {
            final String string = jSONObject.getString("instanceId");
            if (!TextUtils.isEmpty(string)) {
                MessageChannel messageChannel = this.messageTokenChannels.get(string);
                if (messageChannel != null) {
                    messageChannel.setCallback(new MessageCallback() {
                        public void onMessage(Object obj) {
                            if (wVCallBackContext != null) {
                                WVCallBackContext wVCallBackContext = wVCallBackContext;
                                wVCallBackContext.fireEvent("Broadcast.Message." + string, JSON.toJSONString(obj));
                            }
                        }
                    });
                } else if (wVCallBackContext != null) {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("result", (Object) "-1");
                    jSONObject2.put("message", (Object) "channel token not exist");
                    wVCallBackContext.error(jSONObject2.toJSONString());
                }
            } else if (wVCallBackContext != null) {
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("result", (Object) "-1");
                jSONObject3.put("message", (Object) "channel token empty error");
                wVCallBackContext.error(jSONObject3.toJSONString());
            }
        }
    }

    public void postMessage(JSONObject jSONObject, WVCallBackContext wVCallBackContext) {
        if (this.messageTokenChannels != null) {
            if (!TextUtils.isEmpty(jSONObject.getString("instanceId")) && jSONObject.containsKey("message")) {
                String string = jSONObject.getString("instanceId");
                Object obj = jSONObject.get("message");
                MessageChannel messageChannel = this.messageTokenChannels.get(string);
                if (messageChannel == null) {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("result", (Object) "-1");
                    jSONObject2.put("message", (Object) "channel token not exist");
                    wVCallBackContext.error(jSONObject2.toJSONString());
                    return;
                }
                messageChannel.postMessage(obj);
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("result", (Object) "0");
                jSONObject3.put("message", (Object) "post message success");
                wVCallBackContext.success(jSONObject3.toJSONString());
            } else if (wVCallBackContext != null) {
                JSONObject jSONObject4 = new JSONObject();
                jSONObject4.put("result", (Object) "-1");
                jSONObject4.put("message", (Object) "post message args error");
                wVCallBackContext.error(jSONObject4.toJSONString());
            }
        }
    }

    public void closeChannel(JSONObject jSONObject) {
        MessageChannel remove;
        if (jSONObject != null && this.messageTokenChannels != null && (remove = this.messageTokenChannels.remove(jSONObject.getString("instanceId"))) != null) {
            remove.close();
        }
    }

    public void onDestroy() {
        if (this.messageTokenChannels != null) {
            for (Map.Entry next : this.messageTokenChannels.entrySet()) {
                if (next.getValue() != null) {
                    ((MessageChannel) next.getValue()).close();
                }
            }
            this.messageTokenChannels.clear();
        }
        super.onDestroy();
    }
}
