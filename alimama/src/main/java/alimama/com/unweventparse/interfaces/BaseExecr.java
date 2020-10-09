package alimama.com.unweventparse.interfaces;

import alimama.com.unwbase.callback.UNWEventTaskCompletionBlock;
import alimama.com.unweventparse.constants.EventConstants;
import androidx.annotation.CallSuper;
import com.alibaba.fastjson.JSONObject;

public abstract class BaseExecr implements IExecr {
    public UNWEventTaskCompletionBlock callback;
    public JSONObject eventObj;

    public void handleCallBack(Object obj, String str) {
        String str2 = "";
        if (this.eventObj != null) {
            str2 = this.eventObj.getString(EventConstants.EVENTID);
        }
        if (this.callback != null) {
            this.callback.onEventTaskCompletion(str2, obj, str);
        }
    }

    @CallSuper
    public void exec(JSONObject jSONObject, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        this.eventObj = jSONObject;
        this.callback = uNWEventTaskCompletionBlock;
    }
}
