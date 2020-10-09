package com.taobao.android.ultron.datamodel;

import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.common.ValidateResult;
import com.taobao.android.ultron.common.model.DynamicTemplate;
import com.taobao.android.ultron.common.model.IDMComponent;
import java.util.List;

public interface IDMContext {
    IDMComponent getComponentByName(String str);

    List<IDMComponent> getComponents();

    List<IDMComponent> getComponentsByRoot(String str);

    List<DynamicTemplate> getDynamicTemplateList();

    JSONObject getGlobal();

    String getProtocolVersion();

    IDMComponent getRootComponent();

    boolean isCacheData();

    void setBizName(String str);

    void setComponents(List<IDMComponent> list);

    void setSubmitModule(ISubmitModule iSubmitModule);

    ValidateResult validate();
}
