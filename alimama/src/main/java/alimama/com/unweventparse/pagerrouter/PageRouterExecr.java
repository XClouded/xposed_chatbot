package alimama.com.unweventparse.pagerrouter;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.callback.UNWEventTaskCompletionBlock;
import alimama.com.unwbase.interfaces.IRouter;
import alimama.com.unwbase.tools.UNWLog;
import alimama.com.unweventparse.interfaces.BaseExecr;
import alimama.com.unwrouter.constants.RouterConstant;
import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;

public class PageRouterExecr extends BaseExecr {
    public void exec(JSONObject jSONObject, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        super.exec(jSONObject, uNWEventTaskCompletionBlock);
        if (jSONObject != null) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("params");
            String string = jSONObject.getString("url");
            String string2 = jSONObject.getString("needLogin");
            jSONObject.getString("animated");
            String string3 = jSONObject.getString("spm");
            if (!TextUtils.isEmpty(string)) {
                Uri.Builder buildUpon = Uri.parse(string).buildUpon();
                if (!TextUtils.isEmpty(string2)) {
                    buildUpon.appendQueryParameter(RouterConstant.NEEDLOGIN, string2);
                }
                if (!TextUtils.isEmpty(string3)) {
                    buildUpon.appendQueryParameter("spm", string3);
                }
                if (jSONObject2 != null) {
                    for (String next : jSONObject2.keySet()) {
                        buildUpon.appendQueryParameter(next, jSONObject2.getString(next));
                    }
                }
                if (buildUpon != null) {
                    gotoPage(buildUpon.build());
                    return;
                }
                return;
            }
            UNWLog.error("url is null");
        }
    }

    public void exec(JSONObject jSONObject) {
        exec(jSONObject, (UNWEventTaskCompletionBlock) null);
    }

    public void exec(Uri uri) {
        exec(uri, (UNWEventTaskCompletionBlock) null);
    }

    public void exec(Uri uri, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        if (uri != null) {
            gotoPage(uri);
        }
    }

    public void gotoPage(Uri uri) {
        IRouter iRouter = (IRouter) UNWManager.getInstance().getService(IRouter.class);
        if (iRouter != null) {
            iRouter.gotoPage(uri.toString());
        }
    }
}
