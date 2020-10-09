package alimama.com.unweventparse.ut;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.callback.UNWEventTaskCompletionBlock;
import alimama.com.unwbase.interfaces.IUTAction;
import alimama.com.unwbase.tools.ConvertUtils;
import alimama.com.unwbase.tools.UNWLog;
import alimama.com.unweventparse.constants.EventConstants;
import alimama.com.unweventparse.interfaces.BaseExecr;
import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import java.util.Map;

public class UTExecr extends BaseExecr {
    private UNWEventTaskCompletionBlock callBack;

    public void exec(JSONObject jSONObject) {
        exec(jSONObject, (UNWEventTaskCompletionBlock) null);
    }

    public void exec(Uri uri) {
        UNWLog.error("ut not support uri");
    }

    public void exec(JSONObject jSONObject, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        IUTAction iUTAction;
        super.exec(jSONObject, uNWEventTaskCompletionBlock);
        this.callBack = uNWEventTaskCompletionBlock;
        if (jSONObject != null && (iUTAction = (IUTAction) UNWManager.getInstance().getService(IUTAction.class)) != null) {
            String string = jSONObject.getString(EventConstants.UT.UT_EVENTID);
            String string2 = jSONObject.getString(EventConstants.UT.ARG1);
            String string3 = jSONObject.getString(EventConstants.UT.PAGE_NAME);
            String str = "Page_" + string3 + "_Button-" + string2;
            Map<String, String> fastjson2map = ConvertUtils.fastjson2map(jSONObject.getJSONObject("args"));
            if (TextUtils.equals(string, "19999")) {
                iUTAction.customEvent(string3, str, fastjson2map);
            } else if (TextUtils.equals(string, "2101")) {
                iUTAction.ctrlClicked(string3, str, fastjson2map);
            } else if (TextUtils.equals(string, "2201")) {
                iUTAction.expoTrack(string3, str, "", "", fastjson2map);
            }
            handleCallBack(jSONObject, "");
        }
    }

    public void exec(Uri uri, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        UNWLog.error("ut not support uri");
    }
}
