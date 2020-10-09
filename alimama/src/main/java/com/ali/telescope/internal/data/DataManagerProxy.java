package com.ali.telescope.internal.data;

public class DataManagerProxy implements IDataManager {
    private IDataManager mRealDataManager;

    private DataManagerProxy() {
        this.mRealDataManager = null;
    }

    public static DataManagerProxy instance() {
        return Holder.instance;
    }

    public IDataManager getRealDataManager() {
        return this.mRealDataManager;
    }

    public DataManagerProxy setRealDataManager(IDataManager iDataManager) {
        this.mRealDataManager = iDataManager;
        return this;
    }

    public void putData(String str, String str2, String str3, Object obj) {
        if (this.mRealDataManager != null) {
            this.mRealDataManager.putData(str, str2, str3, obj);
        }
    }

    public void putData(String str, String str2, Object obj) {
        if (this.mRealDataManager != null) {
            this.mRealDataManager.putData(str, str2, obj);
        }
    }

    public void putData(String str, Object obj) {
        if (this.mRealDataManager != null) {
            this.mRealDataManager.putData(str, obj);
        }
    }

    public Data getRootData() {
        if (this.mRealDataManager != null) {
            return this.mRealDataManager.getRootData();
        }
        return null;
    }

    public boolean isChanged() {
        if (this.mRealDataManager != null) {
            return this.mRealDataManager.isChanged();
        }
        return false;
    }

    private static class Holder {
        static DataManagerProxy instance = new DataManagerProxy();

        private Holder() {
        }
    }
}
