package com.alimama.union.app.rxnetwork;

import com.alimama.moon.utils.CommonUtils;
import com.alimama.union.app.rxnetwork.EtaoDiskLruCache;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class RxDiskRequest {
    public static RxResponse getDataFromDisk(String str, long j) {
        RxResponse rxResponse = new RxResponse();
        EtaoDiskLruCache.DiskLruResult dataFromDisk = EtaoDiskLruCache.getInstance().getDataFromDisk(str);
        if (dataFromDisk != null) {
            long safeLongValue = CommonUtils.getSafeLongValue(dataFromDisk.extra, 0);
            if (j < 0 || System.currentTimeMillis() - safeLongValue <= j * 1000) {
                rxResponse.isReqSuccess = true;
                rxResponse.data = dataFromDisk.data;
                return rxResponse;
            }
        }
        rxResponse.isReqSuccess = false;
        return rxResponse;
    }

    public static void putDataToDisk(final String str, final byte[] bArr) {
        Observable.create(new Observable.OnSubscribe<Object>() {
            public void call(Subscriber<? super Object> subscriber) {
                EtaoDiskLruCache.getInstance().putDataToDisk(str, String.valueOf(System.currentTimeMillis()), bArr);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public static long getSafeLongValue(String str, long j) {
        try {
            return Long.valueOf(str).longValue();
        } catch (Exception unused) {
            return j;
        }
    }
}
