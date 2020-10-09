package alimama.com.unwnetwork;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IDiskCache;
import android.text.TextUtils;
import com.ali.user.mobile.rpc.ApiConstants;
import com.alibaba.fastjson.JSONObject;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class RxDiskRequest {
    public static RxResponse getDataFromDisk(String str, long j) {
        RxResponse rxResponse = new RxResponse();
        IDiskCache iDiskCache = (IDiskCache) UNWManager.getInstance().getService(IDiskCache.class);
        if (iDiskCache == null) {
            return null;
        }
        String str2 = (String) iDiskCache.getDataFromDisk(str);
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        JSONObject parseObject = JSONObject.parseObject(str2);
        if (parseObject != null) {
            long safeLongValue = getSafeLongValue(parseObject.getString(ApiConstants.ApiField.EXTRA), 0);
            if (j < 0 || System.currentTimeMillis() - safeLongValue <= j * 1000) {
                rxResponse.isReqSuccess = true;
                String string = parseObject.getString("data");
                if (!TextUtils.isEmpty(string)) {
                    rxResponse.oriData = string.getBytes();
                }
                return rxResponse;
            }
        }
        rxResponse.isReqSuccess = false;
        return rxResponse;
    }

    public static void putDataToDisk(final String str, final byte[] bArr) {
        final IDiskCache iDiskCache = (IDiskCache) UNWManager.getInstance().getService(IDiskCache.class);
        if (iDiskCache != null) {
            Observable.create(new Observable.OnSubscribe<Object>() {
                public void call(Subscriber<? super Object> subscriber) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put(ApiConstants.ApiField.EXTRA, (Object) String.valueOf(System.currentTimeMillis()));
                    jSONObject.put("data", (Object) new String(bArr));
                    iDiskCache.putDataToDisk(str, jSONObject.toJSONString());
                }
            }).subscribeOn(Schedulers.io()).subscribe();
        }
    }

    public static long getSafeLongValue(String str, long j) {
        try {
            return Long.valueOf(str).longValue();
        } catch (Exception unused) {
            return j;
        }
    }
}
