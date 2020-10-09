package alimama.com.unweventparse.mtop;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSONObject;

public class MtopResponse {
    public boolean isInteralParse;
    public Object keyResult;

    public MtopResponse(@NonNull JSONObject jSONObject, String str, boolean z) {
        this.isInteralParse = z;
        if (!z) {
            this.keyResult = jSONObject;
        } else if (!TextUtils.isEmpty(str)) {
            this.keyResult = jSONObject.get(str);
        }
    }
}
