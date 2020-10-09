package com.ali.telescope.internal.data;

public interface IDataManager {
    Data getRootData();

    boolean isChanged();

    void putData(String str, Object obj);

    void putData(String str, String str2, Object obj);

    void putData(String str, String str2, String str3, Object obj);
}
