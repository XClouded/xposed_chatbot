package com.ut.mini;

class UTVariables {
    private static UTVariables s_instance = new UTVariables();
    private String mBackupH5Url = null;
    private String mH5RefPage = null;
    private String mH5Url = null;
    private String mRefPage = null;

    UTVariables() {
    }

    public static UTVariables getInstance() {
        return s_instance;
    }

    public String getH5Url() {
        return this.mH5Url;
    }

    public void setH5Url(String str) {
        this.mH5Url = str;
    }

    public void setBackupH5Url(String str) {
        this.mBackupH5Url = str;
    }

    public String getBackupH5Url() {
        return this.mBackupH5Url;
    }

    public String getRefPage() {
        return this.mRefPage;
    }

    public void setRefPage(String str) {
        this.mRefPage = str;
    }

    public String getH5RefPage() {
        return this.mH5RefPage;
    }

    public void setH5RefPage(String str) {
        this.mH5RefPage = str;
    }
}
