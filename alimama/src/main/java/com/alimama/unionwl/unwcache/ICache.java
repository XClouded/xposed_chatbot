package com.alimama.unionwl.unwcache;

public interface ICache {

    public interface IDiskCache {
        DiskLruResult getDataFromDisk(String str, boolean z);

        void putDataToDisk(String str, String str2, byte[] bArr);
    }

    public interface IMemoryCache {
        MemoryResult getDateFromMemory(String str);

        void putDataToMemory(String str, String str2, byte[] bArr);

        void removeDataFromMemory(String str);
    }
}
