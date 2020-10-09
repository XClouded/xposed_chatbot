package com.taobao.android;

import android.util.Log;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.orange.OConfigListener;
import com.taobao.weex.el.parse.Operators;
import java.util.Map;

public class AliConfigListenerAdapter implements OConfigListener {
    private static final String TAG = "AliConfigListenerAdapterImpl";
    private final AliConfigListener mAliConfigListener;

    public AliConfigListenerAdapter(AliConfigListener aliConfigListener) {
        this.mAliConfigListener = aliConfigListener;
    }

    public void onConfigUpdate(String str, Map<String, String> map) {
        Log.d(TAG, "onConfigUpdate(" + str + AVFSCacheConstants.COMMA_SEP + map + Operators.BRACKET_END_STR);
        this.mAliConfigListener.onConfigUpdate(str, map);
    }
}
