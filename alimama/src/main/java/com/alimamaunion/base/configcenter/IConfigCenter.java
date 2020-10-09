package com.alimamaunion.base.configcenter;

import android.app.Application;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import rx.Observer;

public interface IConfigCenter {
    IConfigCenter addObserver(String str, Observer<String> observer);

    String getConfigResult(String str);

    Map<String, String> getConfigResult(List<String> list);

    IConfigCenter init(String[] strArr, Application application);

    void onDataReceived(Map<String, String> map, Map<String, ConfigData> map2);

    IConfigCenter setBackgroundJudge(IBackgroundJudge iBackgroundJudge);

    IConfigCenter setConfigCenterCache(IConfigCenterCache iConfigCenterCache);

    IConfigCenter setConfigCenterRequest(IConfigCenterRequest iConfigCenterRequest);

    IConfigCenter setPollPeriod(long j, TimeUnit timeUnit);

    void unsubscribe(String str, Observer<String> observer);
}
