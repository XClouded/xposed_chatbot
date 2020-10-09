package com.alimama.unwdinamicxcontainer.model.dxcengine;

import com.alibaba.fastjson.JSONObject;

public class GlobalModel {
    private ExtendParams extendParams = new ExtendParams();
    private String extendParamsJsonData;
    private JSONObject unwEvents;

    public ExtendParams getExtendParams() {
        return this.extendParams;
    }

    public void setExtendParams(ExtendParams extendParams2) {
        this.extendParams = extendParams2;
    }

    public String getExtendParamsJsonData() {
        return this.extendParamsJsonData;
    }

    public void setExtendParamsJsonData(String str) {
        this.extendParamsJsonData = str;
    }

    public JSONObject getUnwEvents() {
        return this.unwEvents;
    }

    public void setUnwEvents(JSONObject jSONObject) {
        this.unwEvents = jSONObject;
    }

    public class ExtendParams {
        private String emptyList;
        private String forceUpdate;
        private String loadMore;
        private String unwSuccess;

        public ExtendParams() {
        }

        public String getUnwSuccess() {
            return this.unwSuccess;
        }

        public void setUnwSuccess(String str) {
            this.unwSuccess = str;
        }

        public String getLoadMore() {
            return this.loadMore;
        }

        public void setLoadMore(String str) {
            this.loadMore = str;
        }

        public String getEmptyList() {
            return this.emptyList;
        }

        public void setEmptyList(String str) {
            this.emptyList = str;
        }

        public String getForceUpdate() {
            return this.forceUpdate;
        }

        public void setForceUpdate(String str) {
            this.forceUpdate = str;
        }
    }
}
