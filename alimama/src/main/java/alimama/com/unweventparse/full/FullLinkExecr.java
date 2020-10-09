package alimama.com.unweventparse.full;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.callback.UNWEventTaskCompletionBlock;
import alimama.com.unwbase.interfaces.IEtaoLogger;
import alimama.com.unwbase.tools.ConvertUtils;
import alimama.com.unweventparse.constants.EventConstants;
import alimama.com.unweventparse.interfaces.BaseExecr;
import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;

public class FullLinkExecr extends BaseExecr {
    public void exec(Uri uri) {
    }

    public void exec(Uri uri, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
    }

    public void exec(JSONObject jSONObject) {
        exec(jSONObject, (UNWEventTaskCompletionBlock) null);
    }

    public void exec(JSONObject jSONObject, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        super.exec(jSONObject, uNWEventTaskCompletionBlock);
        if (jSONObject != null) {
            String string = jSONObject.getString(EventConstants.FULLLink.LOG_TYPE);
            String string2 = jSONObject.getString(EventConstants.FULLLink.LOGGERNAME);
            String string3 = jSONObject.getString("point");
            String string4 = jSONObject.getString("msg");
            String string5 = jSONObject.getString("fileName");
            String string6 = jSONObject.getString("errorCode");
            JSONObject jSONObject2 = jSONObject.getJSONObject(EventConstants.FULLLink.INFOMAP);
            IEtaoLogger logger = UNWManager.getInstance().getLogger();
            if (logger != null) {
                if (TextUtils.equals(string, "0")) {
                    if (jSONObject2 != null) {
                        logger.info(string2, string3, string4, ConvertUtils.fastjson2map(jSONObject2));
                    } else {
                        logger.info(string2, string3, string4);
                    }
                }
                if (!TextUtils.equals(string, "1")) {
                    return;
                }
                if (jSONObject2 != null) {
                    logger.error(string2, string3, string4, string6, string5, ConvertUtils.fastjson2map(jSONObject2));
                } else {
                    logger.info(string2, string3, string4);
                }
            }
        }
    }
}
