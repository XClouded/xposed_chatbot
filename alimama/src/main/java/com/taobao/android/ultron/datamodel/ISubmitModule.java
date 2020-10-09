package com.taobao.android.ultron.datamodel;

import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.common.model.IDMComponent;
import com.taobao.android.ultron.datamodel.imp.DMContext;

public interface ISubmitModule {
    JSONObject asyncRequestData(DMContext dMContext, IDMComponent iDMComponent);

    JSONObject submitRequestData(DMContext dMContext);
}
