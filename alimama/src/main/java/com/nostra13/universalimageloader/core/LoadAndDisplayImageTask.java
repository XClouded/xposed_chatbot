package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.os.Handler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.nostra13.universalimageloader.utils.L;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

final class LoadAndDisplayImageTask implements Runnable, IoUtils.CopyListener {
    private static final String ERROR_POST_PROCESSOR_NULL = "Post-processor returned null [%s]";
    private static final String ERROR_PRE_PROCESSOR_NULL = "Pre-processor returned null [%s]";
    private static final String ERROR_PROCESSOR_FOR_DISK_CACHE_NULL = "Bitmap processor for disk cache returned null [%s]";
    private static final String LOG_CACHE_IMAGE_IN_MEMORY = "Cache image in memory [%s]";
    private static final String LOG_CACHE_IMAGE_ON_DISK = "Cache image on disk [%s]";
    private static final String LOG_DELAY_BEFORE_LOADING = "Delay %d ms before loading...  [%s]";
    private static final String LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING = "...Get cached bitmap from memory after waiting. [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_DISK_CACHE = "Load image from disk cache [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_NETWORK = "Load image from network [%s]";
    private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";
    private static final String LOG_PREPROCESS_IMAGE = "PreProcess image before caching in memory [%s]";
    private static final String LOG_PROCESS_IMAGE_BEFORE_CACHE_ON_DISK = "Process image before cache on disk [%s]";
    private static final String LOG_RESIZE_CACHED_IMAGE_FILE = "Resize image in disk cache [%s]";
    private static final String LOG_RESUME_AFTER_PAUSE = ".. Resume loading [%s]";
    private static final String LOG_START_DISPLAY_IMAGE_TASK = "Start display image task [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "ImageAware was collected by GC. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_REUSED = "ImageAware is reused for another image. Task is cancelled. [%s]";
    private static final String LOG_TASK_INTERRUPTED = "Task was interrupted [%s]";
    private static final String LOG_WAITING_FOR_IMAGE_LOADED = "Image already is loading. Waiting... [%s]";
    private static final String LOG_WAITING_FOR_RESUME = "ImageLoader is paused. Waiting...  [%s]";
    /* access modifiers changed from: private */
    public final ImageLoaderConfiguration configuration;
    private final ImageDecoder decoder;
    private final ImageDownloader downloader;
    private final ImageLoaderEngine engine;
    private final Handler handler;
    final ImageAware imageAware;
    private final ImageLoadingInfo imageLoadingInfo;
    final ImageLoadingListener listener;
    private LoadedFrom loadedFrom = LoadedFrom.NETWORK;
    private final String memoryCacheKey;
    private final ImageDownloader networkDeniedDownloader;
    final DisplayImageOptions options;
    final ImageLoadingProgressListener progressListener;
    private final ImageDownloader slowNetworkDownloader;
    private final boolean syncLoading;
    private final ImageSize targetSize;
    final String uri;

    public LoadAndDisplayImageTask(ImageLoaderEngine imageLoaderEngine, ImageLoadingInfo imageLoadingInfo2, Handler handler2) {
        this.engine = imageLoaderEngine;
        this.imageLoadingInfo = imageLoadingInfo2;
        this.handler = handler2;
        this.configuration = imageLoaderEngine.configuration;
        this.downloader = this.configuration.downloader;
        this.networkDeniedDownloader = this.configuration.networkDeniedDownloader;
        this.slowNetworkDownloader = this.configuration.slowNetworkDownloader;
        this.decoder = this.configuration.decoder;
        this.uri = imageLoadingInfo2.uri;
        this.memoryCacheKey = imageLoadingInfo2.memoryCacheKey;
        this.imageAware = imageLoadingInfo2.imageAware;
        this.targetSize = imageLoadingInfo2.targetSize;
        this.options = imageLoadingInfo2.options;
        this.listener = imageLoadingInfo2.listener;
        this.progressListener = imageLoadingInfo2.progressListener;
        this.syncLoading = this.options.isSyncLoading();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00fd, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        fireCancelEvent();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0105, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0106, code lost:
        r0.unlock();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0109, code lost:
        throw r1;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x00ff */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00d4 A[Catch:{ TaskCancelledException -> 0x00ff }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r7 = this;
            boolean r0 = r7.waitIfPaused()
            if (r0 == 0) goto L_0x0007
            return
        L_0x0007:
            boolean r0 = r7.delayIfNeed()
            if (r0 == 0) goto L_0x000e
            return
        L_0x000e:
            com.nostra13.universalimageloader.core.ImageLoadingInfo r0 = r7.imageLoadingInfo
            java.util.concurrent.locks.ReentrantLock r0 = r0.loadFromUriLock
            java.lang.String r1 = "Start display image task [%s]"
            r2 = 1
            java.lang.Object[] r3 = new java.lang.Object[r2]
            java.lang.String r4 = r7.memoryCacheKey
            r5 = 0
            r3[r5] = r4
            com.nostra13.universalimageloader.utils.L.d(r1, r3)
            boolean r1 = r0.isLocked()
            if (r1 == 0) goto L_0x0030
            java.lang.String r1 = "Image already is loading. Waiting... [%s]"
            java.lang.Object[] r3 = new java.lang.Object[r2]
            java.lang.String r4 = r7.memoryCacheKey
            r3[r5] = r4
            com.nostra13.universalimageloader.utils.L.d(r1, r3)
        L_0x0030:
            r0.lock()
            r7.checkTaskNotActual()     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.core.ImageLoaderConfiguration r1 = r7.configuration     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.cache.memory.MemoryCache r1 = r1.memoryCache     // Catch:{ TaskCancelledException -> 0x00ff }
            java.lang.String r3 = r7.memoryCacheKey     // Catch:{ TaskCancelledException -> 0x00ff }
            java.lang.Object r1 = r1.get(r3)     // Catch:{ TaskCancelledException -> 0x00ff }
            android.graphics.Bitmap r1 = (android.graphics.Bitmap) r1     // Catch:{ TaskCancelledException -> 0x00ff }
            if (r1 == 0) goto L_0x005b
            boolean r3 = r1.isRecycled()     // Catch:{ TaskCancelledException -> 0x00ff }
            if (r3 == 0) goto L_0x004b
            goto L_0x005b
        L_0x004b:
            com.nostra13.universalimageloader.core.assist.LoadedFrom r3 = com.nostra13.universalimageloader.core.assist.LoadedFrom.MEMORY_CACHE     // Catch:{ TaskCancelledException -> 0x00ff }
            r7.loadedFrom = r3     // Catch:{ TaskCancelledException -> 0x00ff }
            java.lang.String r3 = "...Get cached bitmap from memory after waiting. [%s]"
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ TaskCancelledException -> 0x00ff }
            java.lang.String r6 = r7.memoryCacheKey     // Catch:{ TaskCancelledException -> 0x00ff }
            r4[r5] = r6     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.utils.L.d(r3, r4)     // Catch:{ TaskCancelledException -> 0x00ff }
            goto L_0x00b3
        L_0x005b:
            android.graphics.Bitmap r1 = r7.tryLoadBitmap()     // Catch:{ TaskCancelledException -> 0x00ff }
            if (r1 != 0) goto L_0x0065
            r0.unlock()
            return
        L_0x0065:
            r7.checkTaskNotActual()     // Catch:{ TaskCancelledException -> 0x00ff }
            r7.checkTaskInterrupted()     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.core.DisplayImageOptions r3 = r7.options     // Catch:{ TaskCancelledException -> 0x00ff }
            boolean r3 = r3.shouldPreProcess()     // Catch:{ TaskCancelledException -> 0x00ff }
            if (r3 == 0) goto L_0x0095
            java.lang.String r3 = "PreProcess image before caching in memory [%s]"
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ TaskCancelledException -> 0x00ff }
            java.lang.String r6 = r7.memoryCacheKey     // Catch:{ TaskCancelledException -> 0x00ff }
            r4[r5] = r6     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.utils.L.d(r3, r4)     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.core.DisplayImageOptions r3 = r7.options     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.core.process.BitmapProcessor r3 = r3.getPreProcessor()     // Catch:{ TaskCancelledException -> 0x00ff }
            android.graphics.Bitmap r1 = r3.process(r1)     // Catch:{ TaskCancelledException -> 0x00ff }
            if (r1 != 0) goto L_0x0095
            java.lang.String r3 = "Pre-processor returned null [%s]"
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ TaskCancelledException -> 0x00ff }
            java.lang.String r6 = r7.memoryCacheKey     // Catch:{ TaskCancelledException -> 0x00ff }
            r4[r5] = r6     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.utils.L.e(r3, r4)     // Catch:{ TaskCancelledException -> 0x00ff }
        L_0x0095:
            if (r1 == 0) goto L_0x00b3
            com.nostra13.universalimageloader.core.DisplayImageOptions r3 = r7.options     // Catch:{ TaskCancelledException -> 0x00ff }
            boolean r3 = r3.isCacheInMemory()     // Catch:{ TaskCancelledException -> 0x00ff }
            if (r3 == 0) goto L_0x00b3
            java.lang.String r3 = "Cache image in memory [%s]"
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ TaskCancelledException -> 0x00ff }
            java.lang.String r6 = r7.memoryCacheKey     // Catch:{ TaskCancelledException -> 0x00ff }
            r4[r5] = r6     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.utils.L.d(r3, r4)     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.core.ImageLoaderConfiguration r3 = r7.configuration     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.cache.memory.MemoryCache r3 = r3.memoryCache     // Catch:{ TaskCancelledException -> 0x00ff }
            java.lang.String r4 = r7.memoryCacheKey     // Catch:{ TaskCancelledException -> 0x00ff }
            r3.put(r4, r1)     // Catch:{ TaskCancelledException -> 0x00ff }
        L_0x00b3:
            if (r1 == 0) goto L_0x00df
            com.nostra13.universalimageloader.core.DisplayImageOptions r3 = r7.options     // Catch:{ TaskCancelledException -> 0x00ff }
            boolean r3 = r3.shouldPostProcess()     // Catch:{ TaskCancelledException -> 0x00ff }
            if (r3 == 0) goto L_0x00df
            java.lang.String r3 = "PostProcess image before displaying [%s]"
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ TaskCancelledException -> 0x00ff }
            java.lang.String r6 = r7.memoryCacheKey     // Catch:{ TaskCancelledException -> 0x00ff }
            r4[r5] = r6     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.utils.L.d(r3, r4)     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.core.DisplayImageOptions r3 = r7.options     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.core.process.BitmapProcessor r3 = r3.getPostProcessor()     // Catch:{ TaskCancelledException -> 0x00ff }
            android.graphics.Bitmap r1 = r3.process(r1)     // Catch:{ TaskCancelledException -> 0x00ff }
            if (r1 != 0) goto L_0x00df
            java.lang.String r3 = "Post-processor returned null [%s]"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ TaskCancelledException -> 0x00ff }
            java.lang.String r4 = r7.memoryCacheKey     // Catch:{ TaskCancelledException -> 0x00ff }
            r2[r5] = r4     // Catch:{ TaskCancelledException -> 0x00ff }
            com.nostra13.universalimageloader.utils.L.e(r3, r2)     // Catch:{ TaskCancelledException -> 0x00ff }
        L_0x00df:
            r7.checkTaskNotActual()     // Catch:{ TaskCancelledException -> 0x00ff }
            r7.checkTaskInterrupted()     // Catch:{ TaskCancelledException -> 0x00ff }
            r0.unlock()
            com.nostra13.universalimageloader.core.DisplayBitmapTask r0 = new com.nostra13.universalimageloader.core.DisplayBitmapTask
            com.nostra13.universalimageloader.core.ImageLoadingInfo r2 = r7.imageLoadingInfo
            com.nostra13.universalimageloader.core.ImageLoaderEngine r3 = r7.engine
            com.nostra13.universalimageloader.core.assist.LoadedFrom r4 = r7.loadedFrom
            r0.<init>(r1, r2, r3, r4)
            boolean r1 = r7.syncLoading
            android.os.Handler r2 = r7.handler
            com.nostra13.universalimageloader.core.ImageLoaderEngine r3 = r7.engine
            runTask(r0, r1, r2, r3)
            return
        L_0x00fd:
            r1 = move-exception
            goto L_0x0106
        L_0x00ff:
            r7.fireCancelEvent()     // Catch:{ all -> 0x00fd }
            r0.unlock()
            return
        L_0x0106:
            r0.unlock()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.run():void");
    }

    private boolean waitIfPaused() {
        AtomicBoolean pause = this.engine.getPause();
        if (pause.get()) {
            synchronized (this.engine.getPauseLock()) {
                if (pause.get()) {
                    L.d(LOG_WAITING_FOR_RESUME, this.memoryCacheKey);
                    try {
                        this.engine.getPauseLock().wait();
                        L.d(LOG_RESUME_AFTER_PAUSE, this.memoryCacheKey);
                    } catch (InterruptedException unused) {
                        L.e(LOG_TASK_INTERRUPTED, this.memoryCacheKey);
                        return true;
                    }
                }
            }
        }
        return isTaskNotActual();
    }

    private boolean delayIfNeed() {
        if (!this.options.shouldDelayBeforeLoading()) {
            return false;
        }
        L.d(LOG_DELAY_BEFORE_LOADING, Integer.valueOf(this.options.getDelayBeforeLoading()), this.memoryCacheKey);
        try {
            Thread.sleep((long) this.options.getDelayBeforeLoading());
            return isTaskNotActual();
        } catch (InterruptedException unused) {
            L.e(LOG_TASK_INTERRUPTED, this.memoryCacheKey);
            return true;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0046, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0049, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004c, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x009a, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x009b, code lost:
        r0 = r1;
        r1 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x009e, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x009f, code lost:
        r0 = r1;
        r1 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00a2, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00a3, code lost:
        r0 = r1;
        r1 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00a6, code lost:
        r1 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00d6, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00d7, code lost:
        throw r0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00d6 A[ExcHandler: TaskCancelledException (r0v1 'e' com.nostra13.universalimageloader.core.LoadAndDisplayImageTask$TaskCancelledException A[CUSTOM_DECLARE]), Splitter:B:1:0x0001] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.graphics.Bitmap tryLoadBitmap() throws com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.TaskCancelledException {
        /*
            r8 = this;
            r0 = 0
            com.nostra13.universalimageloader.core.ImageLoaderConfiguration r1 = r8.configuration     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            com.nostra13.universalimageloader.cache.disc.DiskCache r1 = r1.diskCache     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            java.lang.String r2 = r8.uri     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            java.io.File r1 = r1.get(r2)     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            r2 = 0
            r3 = 1
            if (r1 == 0) goto L_0x0036
            boolean r4 = r1.exists()     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            if (r4 == 0) goto L_0x0036
            java.lang.String r4 = "Load image from disk cache [%s]"
            java.lang.Object[] r5 = new java.lang.Object[r3]     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            java.lang.String r6 = r8.memoryCacheKey     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            r5[r2] = r6     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            com.nostra13.universalimageloader.utils.L.d(r4, r5)     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            com.nostra13.universalimageloader.core.assist.LoadedFrom r4 = com.nostra13.universalimageloader.core.assist.LoadedFrom.DISC_CACHE     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            r8.loadedFrom = r4     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            r8.checkTaskNotActual()     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            com.nostra13.universalimageloader.core.download.ImageDownloader$Scheme r4 = com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme.FILE     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            java.lang.String r1 = r1.getAbsolutePath()     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            java.lang.String r1 = r4.wrap(r1)     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            android.graphics.Bitmap r1 = r8.decodeImage(r1)     // Catch:{ IllegalStateException -> 0x00d8, TaskCancelledException -> 0x00d6, IOException -> 0x00c9, OutOfMemoryError -> 0x00bc, Throwable -> 0x00af }
            goto L_0x0037
        L_0x0036:
            r1 = r0
        L_0x0037:
            if (r1 == 0) goto L_0x004f
            int r4 = r1.getWidth()     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            if (r4 <= 0) goto L_0x004f
            int r4 = r1.getHeight()     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            if (r4 > 0) goto L_0x00de
            goto L_0x004f
        L_0x0046:
            r0 = move-exception
            goto L_0x00b3
        L_0x0049:
            r0 = move-exception
            goto L_0x00c0
        L_0x004c:
            r0 = move-exception
            goto L_0x00cd
        L_0x004f:
            java.lang.String r4 = "Load image from network [%s]"
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            java.lang.String r5 = r8.memoryCacheKey     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            r3[r2] = r5     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            com.nostra13.universalimageloader.utils.L.d(r4, r3)     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            com.nostra13.universalimageloader.core.assist.LoadedFrom r2 = com.nostra13.universalimageloader.core.assist.LoadedFrom.NETWORK     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            r8.loadedFrom = r2     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            java.lang.String r2 = r8.uri     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            com.nostra13.universalimageloader.core.DisplayImageOptions r3 = r8.options     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            boolean r3 = r3.isCacheOnDisk()     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            if (r3 == 0) goto L_0x0084
            boolean r3 = r8.tryCacheImageOnDisk()     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            if (r3 == 0) goto L_0x0084
            com.nostra13.universalimageloader.core.ImageLoaderConfiguration r3 = r8.configuration     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            com.nostra13.universalimageloader.cache.disc.DiskCache r3 = r3.diskCache     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            java.lang.String r4 = r8.uri     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            java.io.File r3 = r3.get(r4)     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            if (r3 == 0) goto L_0x0084
            com.nostra13.universalimageloader.core.download.ImageDownloader$Scheme r2 = com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme.FILE     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            java.lang.String r3 = r3.getAbsolutePath()     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            java.lang.String r2 = r2.wrap(r3)     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
        L_0x0084:
            r8.checkTaskNotActual()     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            android.graphics.Bitmap r2 = r8.decodeImage(r2)     // Catch:{ IllegalStateException -> 0x00d9, TaskCancelledException -> 0x00d6, IOException -> 0x004c, OutOfMemoryError -> 0x0049, Throwable -> 0x0046 }
            if (r2 == 0) goto L_0x00a8
            int r1 = r2.getWidth()     // Catch:{ IllegalStateException -> 0x00a6, TaskCancelledException -> 0x00d6, IOException -> 0x00a2, OutOfMemoryError -> 0x009e, Throwable -> 0x009a }
            if (r1 <= 0) goto L_0x00a8
            int r1 = r2.getHeight()     // Catch:{ IllegalStateException -> 0x00a6, TaskCancelledException -> 0x00d6, IOException -> 0x00a2, OutOfMemoryError -> 0x009e, Throwable -> 0x009a }
            if (r1 > 0) goto L_0x00ad
            goto L_0x00a8
        L_0x009a:
            r1 = move-exception
            r0 = r1
            r1 = r2
            goto L_0x00b3
        L_0x009e:
            r1 = move-exception
            r0 = r1
            r1 = r2
            goto L_0x00c0
        L_0x00a2:
            r1 = move-exception
            r0 = r1
            r1 = r2
            goto L_0x00cd
        L_0x00a6:
            r1 = r2
            goto L_0x00d9
        L_0x00a8:
            com.nostra13.universalimageloader.core.assist.FailReason$FailType r1 = com.nostra13.universalimageloader.core.assist.FailReason.FailType.DECODING_ERROR     // Catch:{ IllegalStateException -> 0x00a6, TaskCancelledException -> 0x00d6, IOException -> 0x00a2, OutOfMemoryError -> 0x009e, Throwable -> 0x009a }
            r8.fireFailEvent(r1, r0)     // Catch:{ IllegalStateException -> 0x00a6, TaskCancelledException -> 0x00d6, IOException -> 0x00a2, OutOfMemoryError -> 0x009e, Throwable -> 0x009a }
        L_0x00ad:
            r1 = r2
            goto L_0x00de
        L_0x00af:
            r1 = move-exception
            r7 = r1
            r1 = r0
            r0 = r7
        L_0x00b3:
            com.nostra13.universalimageloader.utils.L.e(r0)
            com.nostra13.universalimageloader.core.assist.FailReason$FailType r2 = com.nostra13.universalimageloader.core.assist.FailReason.FailType.UNKNOWN
            r8.fireFailEvent(r2, r0)
            goto L_0x00de
        L_0x00bc:
            r1 = move-exception
            r7 = r1
            r1 = r0
            r0 = r7
        L_0x00c0:
            com.nostra13.universalimageloader.utils.L.e(r0)
            com.nostra13.universalimageloader.core.assist.FailReason$FailType r2 = com.nostra13.universalimageloader.core.assist.FailReason.FailType.OUT_OF_MEMORY
            r8.fireFailEvent(r2, r0)
            goto L_0x00de
        L_0x00c9:
            r1 = move-exception
            r7 = r1
            r1 = r0
            r0 = r7
        L_0x00cd:
            com.nostra13.universalimageloader.utils.L.e(r0)
            com.nostra13.universalimageloader.core.assist.FailReason$FailType r2 = com.nostra13.universalimageloader.core.assist.FailReason.FailType.IO_ERROR
            r8.fireFailEvent(r2, r0)
            goto L_0x00de
        L_0x00d6:
            r0 = move-exception
            throw r0
        L_0x00d8:
            r1 = r0
        L_0x00d9:
            com.nostra13.universalimageloader.core.assist.FailReason$FailType r2 = com.nostra13.universalimageloader.core.assist.FailReason.FailType.NETWORK_DENIED
            r8.fireFailEvent(r2, r0)
        L_0x00de:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.tryLoadBitmap():android.graphics.Bitmap");
    }

    private Bitmap decodeImage(String str) throws IOException {
        String str2 = str;
        return this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, str2, this.uri, this.targetSize, this.imageAware.getScaleType(), getDownloader(), this.options));
    }

    private boolean tryCacheImageOnDisk() throws TaskCancelledException {
        L.d(LOG_CACHE_IMAGE_ON_DISK, this.memoryCacheKey);
        try {
            boolean downloadImage = downloadImage();
            if (!downloadImage) {
                return downloadImage;
            }
            int i = this.configuration.maxImageWidthForDiskCache;
            int i2 = this.configuration.maxImageHeightForDiskCache;
            if (i <= 0 && i2 <= 0) {
                return downloadImage;
            }
            L.d(LOG_RESIZE_CACHED_IMAGE_FILE, this.memoryCacheKey);
            resizeAndSaveImage(i, i2);
            return downloadImage;
        } catch (IOException e) {
            L.e(e);
            return false;
        }
    }

    private boolean downloadImage() throws IOException {
        return this.configuration.diskCache.save(this.uri, getDownloader().getStream(this.uri, this.options.getExtraForDownloader()), this);
    }

    private boolean resizeAndSaveImage(int i, int i2) throws IOException {
        File file = this.configuration.diskCache.get(this.uri);
        if (file == null || !file.exists()) {
            return false;
        }
        Bitmap decode = this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, ImageDownloader.Scheme.FILE.wrap(file.getAbsolutePath()), this.uri, new ImageSize(i, i2), ViewScaleType.FIT_INSIDE, getDownloader(), new DisplayImageOptions.Builder().cloneFrom(this.options).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build()));
        if (!(decode == null || this.configuration.processorForDiskCache == null)) {
            L.d(LOG_PROCESS_IMAGE_BEFORE_CACHE_ON_DISK, this.memoryCacheKey);
            decode = this.configuration.processorForDiskCache.process(decode);
            if (decode == null) {
                L.e(ERROR_PROCESSOR_FOR_DISK_CACHE_NULL, this.memoryCacheKey);
            }
        }
        if (decode == null) {
            return false;
        }
        boolean save = this.configuration.diskCache.save(this.uri, decode);
        decode.recycle();
        return save;
    }

    public boolean onBytesCopied(int i, int i2) {
        return this.syncLoading || fireProgressEvent(i, i2);
    }

    private boolean fireProgressEvent(final int i, final int i2) {
        if (isTaskInterrupted() || isTaskNotActual()) {
            return false;
        }
        if (this.progressListener == null) {
            return true;
        }
        runTask(new Runnable() {
            public void run() {
                LoadAndDisplayImageTask.this.progressListener.onProgressUpdate(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView(), i, i2);
            }
        }, false, this.handler, this.engine);
        return true;
    }

    private void fireFailEvent(final FailReason.FailType failType, final Throwable th) {
        if (!this.syncLoading && !isTaskInterrupted() && !isTaskNotActual()) {
            runTask(new Runnable() {
                public void run() {
                    if (LoadAndDisplayImageTask.this.options.shouldShowImageOnFail()) {
                        LoadAndDisplayImageTask.this.imageAware.setImageDrawable(LoadAndDisplayImageTask.this.options.getImageOnFail(LoadAndDisplayImageTask.this.configuration.resources));
                    }
                    LoadAndDisplayImageTask.this.listener.onLoadingFailed(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView(), new FailReason(failType, th));
                }
            }, false, this.handler, this.engine);
        }
    }

    private void fireCancelEvent() {
        if (!this.syncLoading && !isTaskInterrupted()) {
            runTask(new Runnable() {
                public void run() {
                    LoadAndDisplayImageTask.this.listener.onLoadingCancelled(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView());
                }
            }, false, this.handler, this.engine);
        }
    }

    private ImageDownloader getDownloader() {
        if (this.engine.isNetworkDenied()) {
            return this.networkDeniedDownloader;
        }
        if (this.engine.isSlowNetwork()) {
            return this.slowNetworkDownloader;
        }
        return this.downloader;
    }

    private void checkTaskNotActual() throws TaskCancelledException {
        checkViewCollected();
        checkViewReused();
    }

    private boolean isTaskNotActual() {
        return isViewCollected() || isViewReused();
    }

    private void checkViewCollected() throws TaskCancelledException {
        if (isViewCollected()) {
            throw new TaskCancelledException();
        }
    }

    private boolean isViewCollected() {
        if (!this.imageAware.isCollected()) {
            return false;
        }
        L.d(LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED, this.memoryCacheKey);
        return true;
    }

    private void checkViewReused() throws TaskCancelledException {
        if (isViewReused()) {
            throw new TaskCancelledException();
        }
    }

    private boolean isViewReused() {
        if (!(!this.memoryCacheKey.equals(this.engine.getLoadingUriForView(this.imageAware)))) {
            return false;
        }
        L.d(LOG_TASK_CANCELLED_IMAGEAWARE_REUSED, this.memoryCacheKey);
        return true;
    }

    private void checkTaskInterrupted() throws TaskCancelledException {
        if (isTaskInterrupted()) {
            throw new TaskCancelledException();
        }
    }

    private boolean isTaskInterrupted() {
        if (!Thread.interrupted()) {
            return false;
        }
        L.d(LOG_TASK_INTERRUPTED, this.memoryCacheKey);
        return true;
    }

    /* access modifiers changed from: package-private */
    public String getLoadingUri() {
        return this.uri;
    }

    static void runTask(Runnable runnable, boolean z, Handler handler2, ImageLoaderEngine imageLoaderEngine) {
        if (z) {
            runnable.run();
        } else if (handler2 == null) {
            imageLoaderEngine.fireCallback(runnable);
        } else {
            handler2.post(runnable);
        }
    }

    class TaskCancelledException extends Exception {
        TaskCancelledException() {
        }
    }
}
