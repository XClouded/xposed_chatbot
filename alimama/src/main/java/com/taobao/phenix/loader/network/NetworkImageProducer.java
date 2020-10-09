package com.taobao.phenix.loader.network;

import android.text.TextUtils;
import anetwork.channel.util.RequestConstant;
import com.taobao.phenix.common.Constant;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.entity.EncodedData;
import com.taobao.phenix.entity.EncodedImage;
import com.taobao.phenix.entity.ResponseData;
import com.taobao.phenix.loader.StreamResultHandler;
import com.taobao.phenix.loader.network.HttpLoader;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.phenix.request.ImageUriInfo;
import com.taobao.rxm.consume.Consumer;
import com.taobao.rxm.produce.BaseChainProducer;
import com.taobao.rxm.request.RequestCancelListener;
import com.taobao.rxm.schedule.PairingThrottlingScheduler;
import com.taobao.rxm.schedule.ScheduledAction;
import com.taobao.rxm.schedule.Scheduler;
import com.taobao.tcommon.core.Preconditions;
import java.util.Map;
import java.util.concurrent.Future;

public class NetworkImageProducer extends BaseChainProducer<EncodedImage, ResponseData, ImageRequest> implements RequestCancelListener<ImageRequest> {
    private HttpLoader mHttpLoader;

    public NetworkImageProducer(HttpLoader httpLoader) {
        super(2, 0);
        Preconditions.checkNotNull(httpLoader);
        this.mHttpLoader = httpLoader;
    }

    public void consumeNewResult(Consumer<EncodedImage, ImageRequest> consumer, boolean z, ResponseData responseData) {
        onConsumeStart(consumer, z);
        ImageRequest context = consumer.getContext();
        UnitedLog.e("Phenix", "Network Read Started.", context);
        context.getStatistics().mRspProcessStart = System.currentTimeMillis();
        if (context.isCancelled()) {
            UnitedLog.i("Network", context, "request is cancelled before reading response stream", new Object[0]);
            consumer.onCancellation();
            responseData.release();
            return;
        }
        StreamResultHandler streamResultHandler = new StreamResultHandler(consumer, responseData.length, context.getProgressUpdateStep());
        try {
            EncodedData transformFrom = EncodedData.transformFrom(responseData, streamResultHandler);
            if (!streamResultHandler.isCancellationCalled()) {
                context.getStatistics().setSize(transformFrom.length);
                if (!transformFrom.completed) {
                    UnitedLog.e("Network", context, "miss bytes while reading response[type:%d], read=%d, content=%d", Integer.valueOf(responseData.type), Integer.valueOf(streamResultHandler.getReadLength()), Integer.valueOf(streamResultHandler.contentLength));
                    consumer.onFailure(new IncompleteResponseException());
                    return;
                }
                context.unregisterCancelListener(this);
                ImageUriInfo imageUriInfo = context.getImageUriInfo();
                onConsumeFinish(consumer, true, z);
                UnitedLog.e("Phenix", "Network Read Finished.", context);
                consumer.onNewResult(new EncodedImage(transformFrom, imageUriInfo.getPath(), 1, false, imageUriInfo.getImageExtension()), z);
            }
        } catch (Exception e) {
            UnitedLog.e("Network", context, "transform data from response[type:%d] error, read=%d, content=%d, throwable=%s", Integer.valueOf(responseData.type), Integer.valueOf(streamResultHandler.getReadLength()), Integer.valueOf(streamResultHandler.contentLength), e);
            consumer.onFailure(e);
        }
    }

    /* access modifiers changed from: protected */
    public boolean conductResult(Consumer<EncodedImage, ImageRequest> consumer, ScheduledAction scheduledAction) {
        Map<String, String> loaderExtras;
        String str;
        ImageRequest context = consumer.getContext();
        final long id = Thread.currentThread().getId();
        onConductStart(consumer);
        UnitedLog.e("Phenix", "Network Connect Started.", context);
        context.addLoaderExtra(Constant.INNER_EXTRA_NETWORK_START_TIME, String.valueOf(System.currentTimeMillis()));
        context.registerCancelListener(this);
        if (!TextUtils.isEmpty(context.getStatistics().mFullTraceId)) {
            context.addLoaderExtra(RequestConstant.KEY_TRACE_ID, context.getStatistics().mFullTraceId);
        }
        final Consumer<EncodedImage, ImageRequest> consumer2 = consumer;
        final ImageRequest imageRequest = context;
        context.setBlockingFuture(this.mHttpLoader.load(context.getPath(), context.getLoaderExtras(), new HttpLoader.FinishCallback() {
            public void onFinished(ResponseData responseData) {
                boolean z = id != Thread.currentThread().getId();
                ImageRequest imageRequest = (ImageRequest) consumer2.getContext();
                imageRequest.addLoaderExtra(Constant.INNER_EXTRA_IS_ASYNC_HTTP, Boolean.toString(z));
                if (imageRequest.isCancelled()) {
                    UnitedLog.e("Phenix", "request is cancelled before consuming response data", imageRequest);
                    consumer2.onCancellation();
                    responseData.release();
                    NetworkImageProducer.this.notifyPairingScheduler(imageRequest.getId());
                    return;
                }
                UnitedLog.e("Phenix", "Network Connect Finished.", imageRequest);
                NetworkImageProducer.this.onConductFinish(consumer2, true);
                if (z) {
                    NetworkImageProducer.this.scheduleNewResult(consumer2, true, responseData, false);
                } else {
                    NetworkImageProducer.this.consumeNewResult((Consumer<EncodedImage, ImageRequest>) consumer2, true, responseData);
                }
            }

            public void onError(Exception exc) {
                NetworkImageProducer.this.notifyPairingScheduler(((ImageRequest) consumer2.getContext()).getId());
                consumer2.onFailure(exc);
            }
        }));
        if (scheduledAction != null && ((loaderExtras = context.getLoaderExtras()) == null || (str = loaderExtras.get(Constant.INNER_EXTRA_IS_ASYNC_HTTP)) == null || Boolean.valueOf(str).booleanValue())) {
            scheduledAction.notConsumeAction(true);
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void notifyPairingScheduler(int i) {
        Scheduler consumeScheduler = getConsumeScheduler();
        if (consumeScheduler instanceof PairingThrottlingScheduler) {
            ((PairingThrottlingScheduler) consumeScheduler).completePairActions(i);
        }
    }

    public void onCancel(ImageRequest imageRequest) {
        notifyPairingScheduler(imageRequest.getId());
        UnitedLog.e("Phenix", "received cancellation.", imageRequest);
        Future<?> blockingFuture = imageRequest.getBlockingFuture();
        if (blockingFuture != null) {
            imageRequest.setBlockingFuture((Future<?>) null);
            try {
                blockingFuture.cancel(true);
                UnitedLog.d("Network", imageRequest, "cancelled blocking future(%s), result=%b", blockingFuture, Boolean.valueOf(blockingFuture.isCancelled()));
            } catch (Exception e) {
                UnitedLog.e("Network", imageRequest, "cancel blocking future error=%s", e);
            }
        }
    }
}
