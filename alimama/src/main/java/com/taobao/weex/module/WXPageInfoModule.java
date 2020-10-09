package com.taobao.weex.module;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.taobao.windvane.util.WVConstants;
import androidx.appcompat.app.ActionBar;
import com.alibaba.aliweex.adapter.ITBPageInfoModuleAdapter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.baseactivity.CustomBaseActivity;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

public class WXPageInfoModule implements ITBPageInfoModuleAdapter {
    private String mInstanseId;

    public void setInstanceId(String str) {
        this.mInstanseId = str;
    }

    public void setTitle(final Context context, final String str) {
        if (context instanceof CustomBaseActivity) {
            ((CustomBaseActivity) context).runOnUiThread(new Runnable() {
                public void run() {
                    ActionBar supportActionBar = context.getSupportActionBar();
                    if (supportActionBar != null) {
                        supportActionBar.setTitle((CharSequence) str);
                    }
                }
            });
        }
        JSONArray jSONArray = new JSONArray();
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("title", (Object) str);
        jSONArray.add(jSONObject.toJSONString());
        WXBridgeManager.getInstance().callModuleMethod(this.mInstanseId, "navigator", "setNavBarTitle", jSONArray);
    }

    public void setIcon(Context context, String str) {
        setDefaultIcon(context, str);
        JSONArray jSONArray = new JSONArray();
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("icon", (Object) str);
        jSONObject.put("iconType", (Object) WVConstants.INTENT_EXTRA_URL);
        jSONArray.add(jSONObject.toJSONString());
        WXBridgeManager.getInstance().callModuleMethod(this.mInstanseId, "navigator", "setNavBarTitle", jSONArray);
    }

    private void setDefaultIcon(final Context context, final String str) {
        if (context instanceof CustomBaseActivity) {
            ((CustomBaseActivity) context).runOnUiThread(new Runnable() {
                public void run() {
                    final ActionBar supportActionBar = context.getSupportActionBar();
                    if (supportActionBar != null) {
                        IWXHttpAdapter iWXHttpAdapter = WXSDKManager.getInstance().getIWXHttpAdapter();
                        WXRequest wXRequest = new WXRequest();
                        wXRequest.url = str;
                        iWXHttpAdapter.sendRequest(wXRequest, new IWXHttpAdapter.OnHttpListener() {
                            public void onHeadersReceived(int i, Map<String, List<String>> map) {
                            }

                            public void onHttpResponseProgress(int i) {
                            }

                            public void onHttpStart() {
                            }

                            public void onHttpUploadProgress(int i) {
                            }

                            public void onHttpFinish(final WXResponse wXResponse) {
                                context.runOnUiThread(new Runnable() {
                                    public void run() {
                                        try {
                                            BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(new ByteArrayInputStream(wXResponse.originalData)));
                                            supportActionBar.setTitle((CharSequence) "");
                                            supportActionBar.setIcon((Drawable) bitmapDrawable);
                                            supportActionBar.setDisplayUseLogoEnabled(true);
                                            supportActionBar.setDisplayShowHomeEnabled(true);
                                        } catch (Exception unused) {
                                            supportActionBar.setTitle((CharSequence) "手机淘宝");
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
    }
}
