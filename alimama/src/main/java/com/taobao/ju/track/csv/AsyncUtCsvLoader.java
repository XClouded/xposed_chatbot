package com.taobao.ju.track.csv;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import com.taobao.ju.track.util.LogUtil;
import java.util.HashMap;
import java.util.Map;

@TargetApi(3)
public class AsyncUtCsvLoader extends AsyncTask<Object, Object, Map<String, Map<String, String>>> {
    private static final String TAG = "AsyncUtCsvLoader";
    private Map<String, Map<String, String>> mParams;

    public AsyncUtCsvLoader(Map<String, Map<String, String>> map) {
        this.mParams = map;
    }

    /* access modifiers changed from: protected */
    public Map<String, Map<String, String>> doInBackground(Object... objArr) {
        if (objArr == null || objArr.length < 2 || !(objArr[0] instanceof Context) || !(objArr[1] instanceof String)) {
            return new HashMap();
        }
        return new UtCsvLoader().load(objArr[0], objArr[1]);
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Map<String, Map<String, String>> map) {
        if (!(this.mParams == null || map == null || map.size() <= 0)) {
            this.mParams.clear();
            this.mParams.putAll(map);
        }
        String str = TAG;
        Object[] objArr = new Object[3];
        int i = 0;
        objArr[0] = "loadPrams-Success";
        objArr[1] = getClass().getSimpleName();
        if (this.mParams != null) {
            i = this.mParams.size();
        }
        objArr[2] = Integer.valueOf(i);
        LogUtil.d(str, objArr);
    }
}
