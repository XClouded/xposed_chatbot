package alimama.com.unweventparse.popup;

import alimama.com.unweventparse.model.BaseResourceData;
import android.net.Uri;
import android.text.TextUtils;
import com.ali.user.mobile.login.model.LoginConstant;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.common.util.UriUtil;
import org.android.agoo.common.AgooConstants;

public class DialogData extends BaseResourceData<DialogData> {
    public String assetType;
    public String bgUrl;
    public String endTime;
    public String h5WeexUrl;
    public String height;
    public String img;
    public String lottie;
    public String priority;
    public String resKey;
    public String startTime;
    public String url;
    public String width;

    public DialogData make(JSONObject jSONObject) {
        JSONObject jSONObject2 = jSONObject.getJSONObject(UriUtil.LOCAL_ASSET_SCHEME);
        if (jSONObject2 == null) {
            log("asset = null");
            return null;
        }
        this.startTime = jSONObject2.getString(LoginConstant.START_TIME);
        this.endTime = jSONObject2.getString("endTime");
        this.width = jSONObject2.getString("width");
        this.height = jSONObject2.getString("height");
        this.url = jSONObject2.getString("url");
        this.bgUrl = jSONObject2.getString("bgUrl");
        this.img = jSONObject2.getString("img");
        this.lottie = jSONObject2.getString(PopUpExecer.LOTTIETYPE);
        this.h5WeexUrl = jSONObject2.getString("h5WeexUrl");
        this.assetType = jSONObject2.getString("assetType");
        this.priority = jSONObject2.getString("priority");
        this.url = jSONObject2.getString("url");
        this.spm = jSONObject2.getString("spm");
        this.resKey = jSONObject.getString("resKey");
        processTrace(jSONObject2);
        return this;
    }

    public DialogData make(Uri uri) {
        if (uri == null) {
            return null;
        }
        this.startTime = uri.getQueryParameter(LoginConstant.START_TIME);
        this.endTime = uri.getQueryParameter("endTime");
        this.width = uri.getQueryParameter("width");
        this.height = uri.getQueryParameter("height");
        this.url = uri.getQueryParameter("url");
        this.bgUrl = uri.getQueryParameter("bgUrl");
        this.img = uri.getQueryParameter("img");
        this.lottie = uri.getQueryParameter(PopUpExecer.LOTTIETYPE);
        this.h5WeexUrl = uri.getQueryParameter("h5WeexUrl");
        this.assetType = uri.getQueryParameter("assetType");
        this.priority = uri.getQueryParameter("priority");
        this.url = uri.getQueryParameter("url");
        this.spm = uri.getQueryParameter("spm");
        if (TextUtils.isEmpty(uri.getQueryParameter("url"))) {
            return null;
        }
        String queryParameter = uri.getQueryParameter(AgooConstants.MESSAGE_TRACE);
        if (!TextUtils.isEmpty(queryParameter)) {
            processTrace(JSONArray.parseArray(queryParameter));
        }
        return this;
    }
}
