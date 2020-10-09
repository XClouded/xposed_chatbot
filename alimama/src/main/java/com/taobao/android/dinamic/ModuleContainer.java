package com.taobao.android.dinamic;

import com.taobao.android.dinamic.tempate.DTemplateManager;

public class ModuleContainer {
    public DTemplateManager dTemplateManager;
    public DViewGenerator dViewGenerator;

    public static ModuleContainer build(String str) {
        ModuleContainer moduleContainer = new ModuleContainer();
        moduleContainer.dViewGenerator = new DViewGenerator(str);
        moduleContainer.dTemplateManager = new DTemplateManager(str);
        return moduleContainer;
    }
}
