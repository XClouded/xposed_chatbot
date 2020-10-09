package com.alimama.moon.dinamicx.model;

public class BulletinBoardModel {
    public String fieldData;
    public SwitchModel switchData;
    public String template;

    public SwitchModel getSwitchData() {
        return this.switchData;
    }

    public void setSwitchData(SwitchModel switchModel) {
        this.switchData = switchModel;
    }

    public String getTemplate() {
        return this.template;
    }

    public void setTemplate(String str) {
        this.template = str;
    }

    public String getFieldData() {
        return this.fieldData;
    }

    public void setFieldData(String str) {
        this.fieldData = str;
    }

    public class SwitchModel {
        public String isShowDiscovery;
        public String isShowMidH5Tab;
        public String isShowMine;
        public String isShowReport;
        public String isShowTool;

        public SwitchModel() {
        }

        public String getIsShowDiscovery() {
            return this.isShowDiscovery;
        }

        public void setIsShowDiscovery(String str) {
            this.isShowDiscovery = str;
        }

        public String getIsShowTool() {
            return this.isShowTool;
        }

        public void setIsShowTool(String str) {
            this.isShowTool = str;
        }

        public String getIsShowMidH5Tab() {
            return this.isShowMidH5Tab;
        }

        public void setIsShowMidH5Tab(String str) {
            this.isShowMidH5Tab = str;
        }

        public String getIsShowReport() {
            return this.isShowReport;
        }

        public void setIsShowReport(String str) {
            this.isShowReport = str;
        }

        public String getIsShowMine() {
            return this.isShowMine;
        }

        public void setIsShowMine(String str) {
            this.isShowMine = str;
        }
    }
}
