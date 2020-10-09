package alimama.com.unweventparse.interfaces;

import alimama.com.unwbase.callback.UNWEventTaskCompletionBlock;
import android.net.Uri;
import com.alibaba.fastjson.JSONObject;

public interface IExecr {
    void exec(Uri uri);

    void exec(Uri uri, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock);

    void exec(JSONObject jSONObject);

    void exec(JSONObject jSONObject, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock);
}
