package com.alimama.union.app.infrastructure.image.download;

import android.net.Uri;
import androidx.annotation.MainThread;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import java.util.List;

public interface IImageDownloader {

    public interface BatchImageDownloadCallback {
        @MainThread
        void onFailure(String str);

        @WorkerThread
        void onProgressUpdate(List<Uri> list, List<String> list2);

        @MainThread
        void onSuccess(List<Uri> list);
    }

    public interface ImageDownloadCallback {
        void onFailure(String str);

        void onSuccess(@Nullable Uri uri);
    }

    void batchDownload(List<String> list, BatchImageDownloadCallback batchImageDownloadCallback);

    void download(String str, ImageDownloadCallback imageDownloadCallback);
}
