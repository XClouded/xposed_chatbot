package alimama.com.unweventparse.interfaces;

import alimama.com.unweventparse.model.BaseResourceData;
import android.net.Uri;
import com.alibaba.fastjson.JSONObject;

public interface IResourceParse<T extends BaseResourceData> {
    T make(Uri uri);

    T make(JSONObject jSONObject);
}
