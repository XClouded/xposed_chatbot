package com.alibaba.motu.tbrest.rest;

import android.content.Context;
import com.alibaba.motu.tbrest.SendService;
import com.alibaba.motu.tbrest.data.RestData;
import com.alibaba.motu.tbrest.data.RestDataBlocks;
import com.alibaba.motu.tbrest.data.RestDataQueue;
import com.alibaba.motu.tbrest.data.RestOrangeConfigure;
import com.alibaba.motu.tbrest.logger.LoggerAdapter;
import com.alibaba.motu.tbrest.request.BizRequest;
import com.alibaba.motu.tbrest.rest.RestSender;
import com.taobao.weex.common.Constants;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class RestBlockHandler {
    private static final String TAG = "RestBlockHandler";
    private static final Executor dataThread = Executors.newSingleThreadExecutor();
    /* access modifiers changed from: private */
    public final RestOrangeConfigure configure = RestOrangeConfigure.instance();
    /* access modifiers changed from: private */
    public final RestDataBlocks dataBlocks = new RestDataBlocks();
    /* access modifiers changed from: private */
    public int failedCount = 0;
    private final Random random = new Random();
    /* access modifiers changed from: private */
    public final RestSender restSender = new RestSender();
    /* access modifiers changed from: private */
    public final RestDataQueue<RestData> retryDataQueue = new RestDataQueue<>(100);
    /* access modifiers changed from: private */
    public int succeedCount = 0;

    RestBlockHandler() {
    }

    public boolean sendAsyncInfo(int i, String str, Context context, String str2, String str3) {
        if (!isNeedUpdate(i)) {
            return false;
        }
        final String str4 = str;
        final String str5 = str2;
        final int i2 = i;
        final String str6 = str3;
        final Context context2 = context;
        dataThread.execute(new Runnable() {
            public void run() {
                RestDataBlocks.RestDataBlock createBlockIfNotExist = RestBlockHandler.this.dataBlocks.createBlockIfNotExist(str4, str5);
                createBlockIfNotExist.appendData(String.valueOf(i2), str6);
                if (createBlockIfNotExist.dataSize() >= RestBlockHandler.this.configure.getDataSize() || createBlockIfNotExist.getContextCount() >= RestBlockHandler.this.configure.getMessageCount()) {
                    RestBlockHandler.this.sendDataBlock(createBlockIfNotExist, context2);
                    RestBlockHandler.this.dataBlocks.removeBlockIfExist(str4, str5);
                }
            }
        });
        return true;
    }

    private boolean isNeedUpdate(int i) {
        return this.random.nextFloat() < this.configure.getSampleByEventID(String.valueOf(i));
    }

    /* access modifiers changed from: private */
    public void sendDataBlock(RestDataBlocks.RestDataBlock restDataBlock, Context context) {
        byte[] packageRequest = packageRequest(restDataBlock.getAppKey(), context, restDataBlock.data());
        if (packageRequest != null) {
            this.restSender.sendRestDataAsync(new RestData(restDataBlock.getAppKey(), restDataBlock.getUrl(), restDataBlock.getContextCount(), packageRequest), new RestSender.Callback() {
                public void onSuccess(RestData restData) {
                    RestBlockHandler.this.onSendSucceed(restData);
                }

                public void onFailed(RestData restData) {
                    RestBlockHandler.this.onSendFailed(restData);
                }
            });
        }
    }

    private byte[] packageRequest(String str, Context context, Map<String, String> map) {
        try {
            return BizRequest.getPackRequest(str, context, map);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void onSendFailed(final RestData restData) {
        dataThread.execute(new Runnable() {
            public void run() {
                RestData restData = (RestData) RestBlockHandler.this.retryDataQueue.push(restData);
                if (restData != null) {
                    int count = restData.getCount();
                    int unused = RestBlockHandler.this.failedCount = RestBlockHandler.this.failedCount + count;
                    LoggerAdapter.log(Constants.Event.FAIL, "totalCount", Integer.valueOf(RestBlockHandler.this.failedCount), "currentCount", Integer.valueOf(count));
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void onSendSucceed(final RestData restData) {
        dataThread.execute(new Runnable() {
            public void run() {
                int count = restData.getCount();
                int unused = RestBlockHandler.this.succeedCount = RestBlockHandler.this.succeedCount + count;
                LoggerAdapter.log("success", "totalCount", Integer.valueOf(RestBlockHandler.this.succeedCount), "currentCount", Integer.valueOf(count));
                RestData restData = (RestData) RestBlockHandler.this.retryDataQueue.poll();
                if (restData != null) {
                    RestBlockHandler.this.restSender.sendRestDataAsync(restData, new RestSender.Callback() {
                        public void onSuccess(RestData restData) {
                            RestBlockHandler.this.onSendSucceed(restData);
                        }

                        public void onFailed(RestData restData) {
                            RestBlockHandler.this.onSendFailed(restData);
                        }
                    });
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void sendAllCacheData() {
        dataThread.execute(new Runnable() {
            public void run() {
                for (RestDataBlocks.RestDataBlock access$200 : RestBlockHandler.this.dataBlocks.getAll().values()) {
                    RestBlockHandler.this.sendDataBlock(access$200, SendService.getInstance().context);
                }
                RestBlockHandler.this.dataBlocks.clear();
            }
        });
    }
}
