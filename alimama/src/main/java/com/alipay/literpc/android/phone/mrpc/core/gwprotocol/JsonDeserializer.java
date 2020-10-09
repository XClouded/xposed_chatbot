package com.alipay.literpc.android.phone.mrpc.core.gwprotocol;

import android.util.Log;
import com.alipay.literpc.android.phone.mrpc.core.RpcException;
import com.alipay.literpc.jsoncodec.JSONCodec;
import com.alipay.sdk.util.l;
import java.lang.reflect.Type;
import org.json.JSONObject;

public class JsonDeserializer extends AbstractDeserializer {
    public JsonDeserializer(Type type, byte[] bArr) {
        super(type, bArr);
    }

    public Object parser() throws RpcException {
        String str;
        try {
            String str2 = new String(this.mData);
            Log.v("HttpCaller", "threadid = " + Thread.currentThread().getId() + "; rpc response:  " + str2);
            JSONObject jSONObject = new JSONObject(str2);
            int i = jSONObject.getInt(l.a);
            if (i != 1000) {
                throw new RpcException(Integer.valueOf(i), jSONObject.optString("tips"));
            } else if (this.mType == String.class) {
                return jSONObject.optString("result");
            } else {
                return JSONCodec.parseObject(jSONObject.optString("result"), this.mType);
            }
        } catch (Exception e) {
            if (("response  =" + new String(this.mData) + ":" + e) == null) {
                str = "";
            } else {
                str = e.getMessage();
            }
            throw new RpcException((Integer) 10, str);
        }
    }
}
