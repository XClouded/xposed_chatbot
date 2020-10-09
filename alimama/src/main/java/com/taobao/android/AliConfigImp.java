package com.taobao.android;

import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.orange.OConfigListener;
import com.taobao.orange.OrangeConfig;
import com.taobao.weex.el.parse.Operators;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AliConfigImp implements AliConfigInterface {
    private static final String TAG = "AliConfigImp";
    private static final AliConfigImp sInstance = new AliConfigImp(OrangeConfig.getInstance());
    private final HashMap<AliConfigListener, AliConfigListenerAdapter> mAliConfigListenerAdaptersMap = new HashMap<>();
    private final OrangeConfig mOrangeConfig;

    public static AliConfigImp getInstance() {
        return sInstance;
    }

    public AliConfigImp(OrangeConfig orangeConfig) {
        this.mOrangeConfig = orangeConfig;
    }

    public String getConfig(@NonNull String str, @NonNull String str2, @Nullable String str3) {
        String str4;
        if (TextUtils.isEmpty(str2)) {
            str4 = this.mOrangeConfig.getCustomConfig(str, str3);
        } else {
            str4 = this.mOrangeConfig.getConfig(str, str2, str3);
        }
        Log.d(TAG, "getConfig(" + str + AVFSCacheConstants.COMMA_SEP + str2 + AVFSCacheConstants.COMMA_SEP + str3 + ")=" + str4);
        return str4;
    }

    public Map<String, String> getConfigs(@NonNull String str) {
        Map<String, String> configs = this.mOrangeConfig.getConfigs(str);
        Log.d(TAG, "getConfigs(" + str + ")=" + configs);
        return configs;
    }

    public void registerListener(@NonNull String[] strArr, @NonNull AliConfigListener aliConfigListener) {
        synchronized (this.mAliConfigListenerAdaptersMap) {
            AliConfigListenerAdapter aliConfigListenerAdapter = this.mAliConfigListenerAdaptersMap.get(aliConfigListener);
            if (aliConfigListenerAdapter == null) {
                aliConfigListenerAdapter = new AliConfigListenerAdapter(aliConfigListener);
                this.mAliConfigListenerAdaptersMap.put(aliConfigListener, aliConfigListenerAdapter);
            }
            this.mOrangeConfig.registerListener(strArr, aliConfigListenerAdapter, false);
            Log.d(TAG, "registerListener(" + Arrays.toString(strArr) + AVFSCacheConstants.COMMA_SEP + aliConfigListener + Operators.BRACKET_END_STR);
        }
    }

    public void unregisterListener(@NonNull String[] strArr, @NonNull AliConfigListener aliConfigListener) {
        synchronized (this.mAliConfigListenerAdaptersMap) {
            AliConfigListenerAdapter aliConfigListenerAdapter = this.mAliConfigListenerAdaptersMap.get(aliConfigListener);
            if (aliConfigListenerAdapter != null) {
                this.mOrangeConfig.unregisterListener(strArr, (OConfigListener) aliConfigListenerAdapter);
                this.mAliConfigListenerAdaptersMap.remove(aliConfigListener);
                Log.d(TAG, "unregisterListener(" + Arrays.toString(strArr) + AVFSCacheConstants.COMMA_SEP + aliConfigListener + Operators.BRACKET_END_STR);
            }
        }
    }
}
