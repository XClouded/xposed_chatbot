package com.alimama.moon.features.search;

public class SearchOptionModel {
    private final String mCondition;
    private final String mControlName;
    private final String mOptionName;

    public SearchOptionModel(String str, String str2, String str3) {
        this.mOptionName = str;
        this.mCondition = str2;
        this.mControlName = str3;
    }

    public String getName() {
        return this.mOptionName;
    }

    public String getCondition() {
        return this.mCondition;
    }

    public String getControlName() {
        return this.mControlName;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SearchOptionModel)) {
            return false;
        }
        SearchOptionModel searchOptionModel = (SearchOptionModel) obj;
        if (this.mOptionName == null ? searchOptionModel.mOptionName != null : !this.mOptionName.equals(searchOptionModel.mOptionName)) {
            return false;
        }
        if (this.mCondition == null ? searchOptionModel.mCondition != null : !this.mCondition.equals(searchOptionModel.mCondition)) {
            return false;
        }
        if (this.mControlName != null) {
            return this.mControlName.equals(searchOptionModel.mControlName);
        }
        if (searchOptionModel.mControlName == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = (((this.mOptionName != null ? this.mOptionName.hashCode() : 0) * 31) + (this.mCondition != null ? this.mCondition.hashCode() : 0)) * 31;
        if (this.mControlName != null) {
            i = this.mControlName.hashCode();
        }
        return hashCode + i;
    }
}
