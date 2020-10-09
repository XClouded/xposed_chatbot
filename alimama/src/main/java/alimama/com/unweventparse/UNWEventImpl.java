package alimama.com.unweventparse;

import alimama.com.unwbase.callback.UNWEventTaskCompletionBlock;
import alimama.com.unwbase.interfaces.IEvent;
import alimama.com.unwbase.tools.UNWLog;
import alimama.com.unweventparse.constants.EventConstants;
import alimama.com.unweventparse.full.FullLinkExecr;
import alimama.com.unweventparse.interfaces.IExecr;
import alimama.com.unweventparse.mtop.MtopExecr;
import alimama.com.unweventparse.pagerrouter.PageRouterExecr;
import alimama.com.unweventparse.popup.PopUpExecer;
import alimama.com.unweventparse.ut.UTExecr;
import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UNWEventImpl implements IEvent<UNWEventTaskCompletionBlock> {
    private static final String TAG = "_UNWEventImpl";
    private HashMap<String, IExecr> execers = new HashMap<>();
    public List<String> legalHeader = new ArrayList();

    public boolean parseUrl(@NonNull Uri uri, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        IExecr iExecr;
        String host = uri.getHost();
        if (TextUtils.isEmpty(host) || (iExecr = this.execers.get(host)) == null) {
            return false;
        }
        if (uNWEventTaskCompletionBlock == null) {
            iExecr.exec(uri);
            return true;
        }
        iExecr.exec(uri, uNWEventTaskCompletionBlock);
        return true;
    }

    public void parseJson(@NonNull JSONObject jSONObject, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        UNWLog.error(TAG, "parseJson=" + jSONObject);
        String string = jSONObject.getString("event_type");
        UNWLog.error(TAG, "type=" + string);
        if (string == null) {
            if (TextUtils.equals(ResourceFactory.TYPE_POPUP, jSONObject.getString("resType"))) {
                string = "popup";
            } else {
                return;
            }
        }
        IExecr iExecr = this.execers.get(string);
        if (iExecr != null) {
            if (uNWEventTaskCompletionBlock == null) {
                iExecr.exec(jSONObject);
            } else {
                iExecr.exec(jSONObject, uNWEventTaskCompletionBlock);
            }
        }
    }

    public boolean parseUrl(@NonNull Uri uri) {
        return parseUrl(uri, (UNWEventTaskCompletionBlock) null);
    }

    public void parseJson(@NonNull JSONObject jSONObject) {
        parseJson(jSONObject, (UNWEventTaskCompletionBlock) null);
    }

    public int isLegalEvent(@NonNull JSONObject jSONObject) {
        if (jSONObject.getString(EventConstants.EVENT_LIST) != null) {
            return 1;
        }
        return jSONObject.getString(EventConstants.EVENT_ID) != null ? 2 : -1;
    }

    public void execute(String str, JSONObject jSONObject, UNWEventTaskCompletionBlock uNWEventTaskCompletionBlock) {
        if (jSONObject == null) {
            UNWLog.error(TAG, "execute json = null");
            return;
        }
        UNWLog.error(TAG, "eventInfo:" + jSONObject);
        IExecr iExecr = this.execers.get(str);
        if (iExecr != null) {
            iExecr.exec(jSONObject, uNWEventTaskCompletionBlock);
            return;
        }
        UNWLog.error(TAG, str + "  has no execr");
    }

    public void init() {
        this.legalHeader.add("event");
        this.execers.put("popup", new PopUpExecer());
        this.execers.put("mtop", new MtopExecr());
        this.execers.put(EventConstants.FULLLink.NAME, new FullLinkExecr());
        this.execers.put(EventConstants.UT.NAME, new UTExecr());
        this.execers.put(EventConstants.pageRouter.NAME, new PageRouterExecr());
    }
}
