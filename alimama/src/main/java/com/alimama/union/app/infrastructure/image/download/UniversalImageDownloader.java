package com.alimama.union.app.infrastructure.image.download;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.alimama.moon.App;
import com.alimama.union.app.infrastructure.executor.ITaskExecutor;
import com.alimama.union.app.infrastructure.executor.TaskCallback;
import com.alimama.union.app.infrastructure.image.download.IImageDownloader;
import com.alimama.union.app.infrastructure.image.download.UniversalImageDownloader;
import com.alimama.union.app.infrastructure.image.save.IImageSaver;
import com.alimama.union.app.infrastructure.socialShare.social.ShareUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class UniversalImageDownloader implements IImageDownloader {
    private final ImageLoader imageLoader;
    private final IImageSaver imageSaver;
    private final ITaskExecutor taskExecutor;
    /* access modifiers changed from: private */
    public final Handler uiHandler = new Handler(Looper.getMainLooper());

    public UniversalImageDownloader(ImageLoader imageLoader2, IImageSaver iImageSaver, ITaskExecutor iTaskExecutor) {
        this.imageLoader = imageLoader2;
        this.imageSaver = iImageSaver;
        this.taskExecutor = iTaskExecutor;
    }

    public void batchDownload(final List<String> list, final IImageDownloader.BatchImageDownloadCallback batchImageDownloadCallback) {
        this.taskExecutor.submit(new BatchImageLoadTask(this.imageLoader, this.imageSaver, list, batchImageDownloadCallback), new TaskCallback<List<Uri>>() {
            public void onSuccess(List<Uri> list) {
                UniversalImageDownloader.this.uiHandler.post(new Runnable(list, list, batchImageDownloadCallback) {
                    private final /* synthetic */ List f$0;
                    private final /* synthetic */ List f$1;
                    private final /* synthetic */ IImageDownloader.BatchImageDownloadCallback f$2;

                    {
                        this.f$0 = r1;
                        this.f$1 = r2;
                        this.f$2 = r3;
                    }

                    public final void run() {
                        UniversalImageDownloader.AnonymousClass1.lambda$onSuccess$1(this.f$0, this.f$1, this.f$2);
                    }
                });
            }

            static /* synthetic */ void lambda$onSuccess$1(List list, List list2, IImageDownloader.BatchImageDownloadCallback batchImageDownloadCallback) {
                if (list == null || list.size() != list2.size()) {
                    batchImageDownloadCallback.onFailure("error retry");
                } else {
                    batchImageDownloadCallback.onSuccess(list);
                }
            }

            public void onFailure(Exception exc) {
                UniversalImageDownloader.this.uiHandler.post(new Runnable(exc) {
                    private final /* synthetic */ Exception f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void run() {
                        IImageDownloader.BatchImageDownloadCallback.this.onFailure(this.f$1.getMessage());
                    }
                });
            }
        });
    }

    public void download(String str, final IImageDownloader.ImageDownloadCallback imageDownloadCallback) {
        this.taskExecutor.submit(new ImageLoadTask(this.imageLoader, this.imageSaver, str), new TaskCallback<Uri>() {
            public void onSuccess(Uri uri) {
                if (uri == null) {
                    UniversalImageDownloader.this.uiHandler.post(new Runnable() {
                        public final void run() {
                            IImageDownloader.ImageDownloadCallback.this.onFailure("error_retry");
                        }
                    });
                } else {
                    UniversalImageDownloader.this.uiHandler.post(new Runnable(uri) {
                        private final /* synthetic */ Uri f$1;

                        {
                            this.f$1 = r2;
                        }

                        public final void run() {
                            IImageDownloader.ImageDownloadCallback.this.onSuccess(this.f$1);
                        }
                    });
                }
            }

            public void onFailure(Exception exc) {
                UniversalImageDownloader.this.uiHandler.post(new Runnable(exc) {
                    private final /* synthetic */ Exception f$1;

                    {
                        this.f$1 = r2;
                    }

                    public final void run() {
                        IImageDownloader.ImageDownloadCallback.this.onFailure(this.f$1.getMessage());
                    }
                });
            }
        });
    }

    private static class BatchImageLoadTask implements Callable<List<Uri>> {
        private final IImageDownloader.BatchImageDownloadCallback callback;
        private final List<String> failedUrls = new ArrayList();
        private final ImageLoader imageLoader;
        private final IImageSaver imageSaver;
        private final List<String> imageUrlList;

        public BatchImageLoadTask(ImageLoader imageLoader2, IImageSaver iImageSaver, List<String> list, IImageDownloader.BatchImageDownloadCallback batchImageDownloadCallback) {
            this.imageLoader = imageLoader2;
            this.imageSaver = iImageSaver;
            ArrayList arrayList = new ArrayList();
            for (String parse : list) {
                Uri parse2 = Uri.parse(parse);
                if (TextUtils.isEmpty(parse2.getScheme())) {
                    parse2 = parse2.buildUpon().scheme("https").build();
                }
                arrayList.add(parse2.toString());
            }
            this.imageUrlList = arrayList;
            this.callback = batchImageDownloadCallback;
        }

        public List<Uri> call() {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < this.imageUrlList.size(); i++) {
                String str = this.imageUrlList.get(i);
                Bitmap loadImageSync = this.imageLoader.loadImageSync(str);
                if (loadImageSync == null) {
                    this.failedUrls.add(str);
                } else {
                    arrayList.add(ShareUtils.uriForShare(App.sApplication, this.imageSaver.saveBitmapToMedia(App.sApplication, loadImageSync, str)));
                }
                this.callback.onProgressUpdate(arrayList, this.failedUrls);
            }
            return arrayList;
        }
    }

    private static class ImageLoadTask implements Callable<Uri> {
        private final ImageLoader imageLoader;
        private final IImageSaver imageSaver;
        private final String imageUrl;

        public ImageLoadTask(ImageLoader imageLoader2, IImageSaver iImageSaver, String str) {
            this.imageLoader = imageLoader2;
            this.imageSaver = iImageSaver;
            this.imageUrl = str;
        }

        public Uri call() {
            Bitmap loadImageSync = this.imageLoader.loadImageSync(this.imageUrl);
            if (loadImageSync == null) {
                return null;
            }
            return ShareUtils.uriForShare(App.sApplication, this.imageSaver.saveBitmapToMedia(App.sApplication, loadImageSync, this.imageUrl));
        }
    }
}
