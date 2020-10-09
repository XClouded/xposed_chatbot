package com.alimama.union.app.configcenter;

import android.app.Application;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.union.app.rxnetwork.RxMtopRequest;
import com.alimama.union.app.rxnetwork.RxMtopResponse;
import com.alimamaunion.base.configcenter.EtaoConfigCenter;
import com.alimamaunion.base.configcenter.IBackgroundJudge;
import com.alimamaunion.base.configcenter.IConfigCenterRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ConfigCenterHelper implements IConfigCenterRequest, IBackgroundJudge {
    private static ConfigCenterHelper sInstance;
    private Application mApplication;
    private ConfigCenterRequest mCenterRequest;

    public static ConfigCenterHelper getInstance() {
        if (sInstance == null) {
            synchronized (ConfigCenterHelper.class) {
                if (sInstance == null) {
                    sInstance = new ConfigCenterHelper();
                }
            }
        }
        return sInstance;
    }

    private ConfigCenterHelper() {
    }

    public void initConfigCenter(Application application, String[] strArr) {
        this.mApplication = application;
        EtaoConfigCenter.getInstance().setConfigCenterRequest((IConfigCenterRequest) this).setBackgroundJudge((IBackgroundJudge) this).setPollPeriod(60, TimeUnit.SECONDS).init(strArr, this.mApplication);
    }

    public boolean isBackground() {
        return MoonComponentManager.getInstance().getPageRouter().isBackground();
    }

    public void doRequest(final Map<String, String> map, String str, final EtaoConfigCenter etaoConfigCenter) {
        if (this.mCenterRequest == null) {
            this.mCenterRequest = new ConfigCenterRequest();
        }
        this.mCenterRequest.appendParam("configKeys", str);
        this.mCenterRequest.sendRequest(new RxMtopRequest.RxMtopResult<ConfigCenterResponse>() {
            public void result(RxMtopResponse<ConfigCenterResponse> rxMtopResponse) {
                etaoConfigCenter.onDataReceived((Map<String, String>) map, ((ConfigCenterResponse) rxMtopResponse.result).configItems);
            }
        });
    }
}
