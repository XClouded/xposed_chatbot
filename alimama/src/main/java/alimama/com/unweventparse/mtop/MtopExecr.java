package alimama.com.unweventparse.mtop;

import alimama.com.unwbase.callback.UNWEventTaskCompletionBlock;
import alimama.com.unweventparse.interfaces.BaseExecr;
import android.net.Uri;
import com.alibaba.fastjson.JSONObject;

public class MtopExecr extends BaseExecr {
    private static final String TAG = "MtopExecer";
    private MtopRealExecer execer;

    public void exec(JSONObject jSONObject) {
        exec(jSONObject, (UNWEventTaskCompletionBlock) null);
    }

    public void exec(Uri uri) {
        exec(uri, (UNWEventTaskCompletionBlock) null);
    }

    public void exec(JSONObject jSONObject, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        super.exec(jSONObject, uNWEventTaskCompletionBlock);
        this.execer = new MtopRealExecer(jSONObject, uNWEventTaskCompletionBlock);
        this.execer.constructInfo(jSONObject);
        this.execer.readyToSend();
    }

    public void exec(Uri uri, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        if (uri != null) {
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            for (String next : uri.getQueryParameterNames()) {
                jSONObject2.put(next, (Object) uri.getQueryParameter(next));
            }
            jSONObject.put("params", (Object) jSONObject2);
            super.exec(jSONObject, uNWEventTaskCompletionBlock);
            this.execer = new MtopRealExecer(jSONObject, uNWEventTaskCompletionBlock);
            this.execer.constructInfo(jSONObject);
            this.execer.readyToSend();
        }
    }
}
