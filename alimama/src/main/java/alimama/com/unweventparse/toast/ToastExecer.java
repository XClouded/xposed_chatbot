package alimama.com.unweventparse.toast;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.callback.UNWEventTaskCompletionBlock;
import alimama.com.unwbase.interfaces.IRouter;
import alimama.com.unweventparse.interfaces.BaseExecr;
import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;

public class ToastExecer extends BaseExecr {
    private UNWEventTaskCompletionBlock callBack;

    public void exec(Uri uri, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
    }

    public void exec(Uri uri) {
        exec(uri, (UNWEventTaskCompletionBlock) null);
    }

    public void exec(JSONObject jSONObject, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        IRouter iRouter;
        super.exec(jSONObject, uNWEventTaskCompletionBlock);
        this.callBack = uNWEventTaskCompletionBlock;
        if (jSONObject != null && (iRouter = (IRouter) UNWManager.getInstance().getService(IRouter.class)) != null) {
            String string = jSONObject.getString("text");
            Activity currentActivity = iRouter.getCurrentActivity();
            if (currentActivity != null && !currentActivity.isFinishing()) {
                Toast.makeText(UNWManager.getInstance().application, string, 1).show();
            }
            handleCallBack(jSONObject, "");
        }
    }

    public void exec(JSONObject jSONObject) {
        exec(jSONObject, (UNWEventTaskCompletionBlock) null);
    }
}
