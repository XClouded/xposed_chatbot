package android.taobao.windvane.jsbridge.api;

import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.util.ImageTool;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class WVImage extends WVApiPlugin {
    public boolean execute(String str, String str2, final WVCallBackContext wVCallBackContext) {
        if (!TextUtils.equals(str, "saveImage")) {
            return false;
        }
        try {
            String optString = new JSONObject(str2).optString("url", "");
            if (TextUtils.isEmpty(optString)) {
                return true;
            }
            ImageTool.saveImageToDCIM(this.mContext, optString, (ImageTool.ImageSaveCallback) new ImageTool.ImageSaveCallback() {
                public void success() {
                    wVCallBackContext.success();
                }

                public void error(String str) {
                    WVResult wVResult = new WVResult();
                    wVResult.addData("msg", str);
                    wVCallBackContext.error(wVResult);
                }
            });
            return true;
        } catch (JSONException e) {
            WVResult wVResult = new WVResult();
            wVResult.addData("msg", e.getMessage());
            wVCallBackContext.error(wVResult);
            return true;
        }
    }
}
