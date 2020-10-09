package com.alimama.unwdinamicxcontainer.model.dxengine;

public class DXEngineDataModel {
    private String fieldsJsonData;
    private String templateJsonData;

    public DXEngineDataModel(String str, String str2) {
        this.templateJsonData = str;
        this.fieldsJsonData = str2;
    }

    public String getTemplateJsonData() {
        return this.templateJsonData;
    }

    public void setTemplateJsonData(String str) {
        this.templateJsonData = str;
    }

    public String getFieldsJsonData() {
        return this.fieldsJsonData;
    }

    public void setFieldsJsonData(String str) {
        this.fieldsJsonData = str;
    }
}
