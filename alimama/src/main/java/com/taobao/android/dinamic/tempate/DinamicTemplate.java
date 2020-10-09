package com.taobao.android.dinamic.tempate;

import android.text.TextUtils;
import java.io.Serializable;

public class DinamicTemplate implements Serializable {
    private String compilerVersion;
    private String interpreterVersion;
    public String name;
    public String templateUrl;
    public String version;

    public String getInterpreterVersion() {
        return this.interpreterVersion;
    }

    public void setInterpreterVersion(String str) {
        this.interpreterVersion = str;
    }

    public String getCompilerVersion() {
        return this.compilerVersion;
    }

    public void setCompilerVersion(String str) {
        this.compilerVersion = str;
    }

    public boolean checkValid() {
        return !TextUtils.isEmpty(this.name);
    }

    public String toString() {
        return "name=" + this.name + "version=" + this.version + "templateUrl=" + this.templateUrl;
    }

    public boolean isPreset() {
        return TextUtils.isEmpty(this.version);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DinamicTemplate dinamicTemplate = (DinamicTemplate) obj;
        if (this.name == null ? dinamicTemplate.name != null : !this.name.equals(dinamicTemplate.name)) {
            return false;
        }
        if (this.version != null) {
            return this.version.equals(dinamicTemplate.version);
        }
        if (dinamicTemplate.version == null) {
            return true;
        }
        return false;
    }
}
