package com.taobao.tao.log.godeye.api.plugin;

import android.app.Application;
import com.taobao.tao.log.godeye.api.control.IGodeye;

public interface IPluginInitializerContextAware {
    void init(Application application, IGodeye iGodeye);
}
