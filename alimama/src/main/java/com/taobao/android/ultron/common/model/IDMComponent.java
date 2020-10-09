package com.taobao.android.ultron.common.model;

import androidx.collection.ArrayMap;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.ultron.common.ValidateResult;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IDMComponent extends Serializable {

    public interface CustomValidate {
        ValidateResult onCustomValidate(IDMComponent iDMComponent);
    }

    String getCardGroup();

    List<IDMComponent> getChildren();

    JSONObject getContainerInfo();

    String getContainerType();

    JSONObject getData();

    Map<String, List<IDMEvent>> getEventMap();

    JSONObject getEvents();

    ArrayMap<String, Object> getExtMap();

    JSONObject getFields();

    JSONObject getHidden();

    String getId();

    String getKey();

    JSONObject getLayout();

    JSONObject getLayoutStyle();

    String getLayoutType();

    LinkageType getLinkageType();

    int getModifiedCount();

    IDMComponent getParent();

    String getPosition();

    JSONObject getStashData();

    int getStatus();

    String getTag();

    String getType();

    void record();

    void rollBack();

    void setCustomValidate(CustomValidate customValidate);

    void updateModifiedCount();

    ValidateResult validate();

    void writeFields(String str, Object obj);
}
