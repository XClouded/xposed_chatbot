package com.ali.protodb;

import android.app.Application;
import androidx.annotation.Keep;
import com.ali.protodb.lsdb.Key;
import com.ali.protodb.lsdb.LSDBConfig;
import com.taobao.alivfsadapter.utils.AVFSApplicationUtils;
import java.io.File;

public class Series extends NativeBridgedObject {
    @Keep
    private native void nativeAppendRecord(String str, String str2, ValueWrapper[] valueWrapperArr);

    @Keep
    private native long nativeGetRecord(String str);

    @Keep
    private static native long nativeOpen(String str, LSDBConfig lSDBConfig);

    public static Series open(String str, LSDBConfig lSDBConfig) {
        Application application = AVFSApplicationUtils.getApplication();
        if (application == null) {
            throw new RuntimeException("failed to get android context!");
        } else if (!sNativeLibraryLoaded) {
            return new SeriesNopeImp();
        } else {
            long nativeOpen = nativeOpen(application.getFilesDir() + File.separator + "lsdb-series-" + str, lSDBConfig);
            if (nativeOpen > 0) {
                return new Series(nativeOpen);
            }
            return new SeriesNopeImp();
        }
    }

    public static Series open(String str, String str2, LSDBConfig lSDBConfig) {
        if (!sNativeLibraryLoaded) {
            return new SeriesNopeImp();
        }
        long nativeOpen = nativeOpen(str + File.separator + "lsdb-series-" + str2, lSDBConfig);
        if (nativeOpen > 0) {
            return new Series(nativeOpen);
        }
        return new SeriesNopeImp();
    }

    public Series(long j) {
        super(j);
    }

    public void appendRecord(Key key, String str, Object... objArr) {
        ValueWrapper[] valueWrapperArr = new ValueWrapper[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            String str2 = objArr[i];
            if (str2 instanceof String) {
                valueWrapperArr[i] = ValueWrapper.stringValue(str2);
            } else if (str2 instanceof Number) {
                if (str2 instanceof Integer) {
                    valueWrapperArr[i] = ValueWrapper.intValue(((Integer) str2).intValue());
                } else if (str2 instanceof Long) {
                    valueWrapperArr[i] = ValueWrapper.longValue(((Long) str2).longValue());
                } else if (str2 instanceof Float) {
                    valueWrapperArr[i] = ValueWrapper.floatValue(((Float) str2).floatValue());
                } else if (str2 instanceof Double) {
                    valueWrapperArr[i] = ValueWrapper.doubleValue(((Double) str2).doubleValue());
                }
            }
        }
        nativeAppendRecord(key.getStringKey(), str, valueWrapperArr);
    }

    public Record getRecord(Key key) {
        long nativeGetRecord = nativeGetRecord(key.getStringKey());
        if (nativeGetRecord > 0) {
            return new Record(nativeGetRecord);
        }
        return null;
    }
}
