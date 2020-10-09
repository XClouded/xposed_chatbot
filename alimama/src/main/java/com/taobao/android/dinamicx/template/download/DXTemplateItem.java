package com.taobao.android.dinamicx.template.download;

import android.text.TextUtils;

public class DXTemplateItem {
    public static final int DEFAULT_VERSION = -1;
    private int fileVersion = 0;
    private String identifier;
    public boolean isPreset = false;
    public String name;
    public DXTemplateItem originalItem;
    public DXTemplatePackageInfo packageInfo;
    public String templateUrl;
    public long version = -1;

    public String getIdentifier() {
        if (TextUtils.isEmpty(this.identifier)) {
            this.identifier = this.name + "_" + this.version;
        }
        return this.identifier;
    }

    public String toString() {
        return "name=" + this.name + "version=" + this.version + "templateUrl=" + this.templateUrl;
    }

    public int getFileVersion() {
        return this.fileVersion;
    }

    public void setFileVersion(int i) {
        this.fileVersion = i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DXTemplateItem dXTemplateItem = (DXTemplateItem) obj;
        if (this.name == null ? dXTemplateItem.name != null : !this.name.equals(dXTemplateItem.name)) {
            return false;
        }
        if (this.fileVersion == dXTemplateItem.fileVersion && this.version == dXTemplateItem.version) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return getIdentifier().hashCode();
    }
}
