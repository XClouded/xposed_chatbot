package com.taobao.orange.aidl;

import android.content.Context;
import android.os.RemoteException;
import com.taobao.orange.ConfigCenter;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.OCandidate;
import com.taobao.orange.OConfig;
import com.taobao.orange.aidl.IOrangeApiService;
import com.taobao.orange.candidate.MultiAnalyze;
import com.taobao.orange.util.OLog;
import java.util.Map;

public class OrangeApiServiceStub extends IOrangeApiService.Stub {
    private static final String TAG = "ApiService";
    private Context mContext;

    public OrangeApiServiceStub(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void init(OConfig oConfig) {
        ConfigCenter.getInstance().init(this.mContext, oConfig);
    }

    public String getConfig(String str, String str2, String str3) {
        return ConfigCenter.getInstance().getConfig(str, str2, str3);
    }

    public Map<String, String> getConfigs(String str) throws RemoteException {
        return ConfigCenter.getInstance().getConfigs(str);
    }

    public String getCustomConfig(String str, String str2) throws RemoteException {
        return ConfigCenter.getInstance().getCustomConfig(str, str2);
    }

    public void registerListener(String str, ParcelableConfigListener parcelableConfigListener, boolean z) throws RemoteException {
        ConfigCenter.getInstance().registerListener(str, parcelableConfigListener, z);
    }

    public void unregisterListener(String str, ParcelableConfigListener parcelableConfigListener) throws RemoteException {
        ConfigCenter.getInstance().unregisterListener(str, parcelableConfigListener);
    }

    public void unregisterListeners(String str) throws RemoteException {
        ConfigCenter.getInstance().unregisterListeners(str);
    }

    public void forceCheckUpdate() throws RemoteException {
        ConfigCenter.getInstance().forceCheckUpdate();
    }

    public void addFails(String[] strArr) throws RemoteException {
        ConfigCenter.getInstance().addFails(strArr);
    }

    public void setUserId(String str) throws RemoteException {
        OLog.d(TAG, "setUserId", "userId", str);
        GlobalOrange.userId = str;
    }

    public void addCandidate(String str, String str2, ParcelableCandidateCompare parcelableCandidateCompare) throws RemoteException {
        MultiAnalyze.addCandidate(new OCandidate(str, str2, parcelableCandidateCompare));
    }
}
