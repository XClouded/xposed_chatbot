package com.alipay.literpc.android.phone.mrpc.core.gwprotocol;

import android.util.Log;
import com.alipay.literpc.android.phone.mrpc.core.RpcException;
import com.alipay.literpc.jsoncodec.JSONCodec;
import java.util.ArrayList;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class JsonSerializer extends AbstractSerializer {
    private static final String TAG = "JsonSerializer";
    public static final String VERSION = "1.0.0";
    private Object mExtParam;
    private int mId;

    public JsonSerializer(int i, String str, Object obj) {
        super(str, obj);
        this.mId = i;
    }

    public void setExtParam(Object obj) {
        this.mExtParam = obj;
    }

    public byte[] packet() throws RpcException {
        String str;
        String str2;
        try {
            ArrayList arrayList = new ArrayList();
            if (this.mExtParam != null) {
                arrayList.add(new BasicNameValuePair("extParam", JSONCodec.toJSONString(this.mExtParam)));
            }
            arrayList.add(new BasicNameValuePair("operationType", this.mOperationType));
            arrayList.add(new BasicNameValuePair("id", this.mId + ""));
            Log.d(TAG, "mParams is:" + this.mParams);
            if (this.mParams == null) {
                str2 = "[]";
            } else {
                str2 = JSONCodec.toJSONString(this.mParams);
            }
            arrayList.add(new BasicNameValuePair("requestData", str2));
            String format = URLEncodedUtils.format(arrayList, "utf-8");
            Log.i(TAG, "request = " + format);
            return format.getBytes();
        } catch (Exception e) {
            if (("request  =" + this.mParams + ":" + e) == null) {
                str = "";
            } else {
                str = e.getMessage();
            }
            throw new RpcException(9, str, e);
        }
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int i) {
        this.mId = i;
    }
}
