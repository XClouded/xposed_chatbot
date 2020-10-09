package alimama.com.unweventparse.model;

import alimama.com.unweventparse.popup.PopUpExecer;
import android.net.Uri;
import com.alibaba.fastjson.JSONObject;
import com.facebook.common.util.UriUtil;

public class LottieImageData extends BaseResourceData<LottieImageData> {
    public int assetHeight;
    public String assetImg;
    public String assetLottie;
    public String assetType;
    public int assetWidth;
    public String extend;

    public LottieImageData make(JSONObject jSONObject) {
        this.resType = jSONObject.getString("resType");
        this.extend = jSONObject.getString("extend");
        JSONObject jSONObject2 = jSONObject.getJSONObject(UriUtil.LOCAL_ASSET_SCHEME);
        this.spm = jSONObject2.getString("spm");
        this.url = jSONObject2.getString("url");
        this.assetImg = jSONObject2.getString("img");
        this.assetLottie = jSONObject2.getString(PopUpExecer.LOTTIETYPE);
        this.assetType = jSONObject2.getString("assetType");
        this.assetWidth = jSONObject2.getIntValue("width");
        this.assetHeight = jSONObject2.getIntValue("height");
        processTrace(jSONObject2);
        return this;
    }

    public LottieImageData make(Uri uri) {
        log("LottieImageData can`t support url schema");
        return null;
    }
}
