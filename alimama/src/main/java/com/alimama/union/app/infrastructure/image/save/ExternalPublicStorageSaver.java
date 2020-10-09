package com.alimama.union.app.infrastructure.image.save;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalPublicStorageSaver implements IImageSaver {
    private static ExternalPublicStorageSaver instance;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) ExternalPublicStorageSaver.class);
    private File baseDir;

    private ExternalPublicStorageSaver() {
    }

    public static ExternalPublicStorageSaver getInstance() {
        if (instance == null) {
            instance = new ExternalPublicStorageSaver();
        }
        return instance;
    }

    public File getBaseFileDir() {
        if (this.baseDir != null) {
            return this.baseDir;
        }
        this.baseDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "淘宝联盟");
        if (!this.baseDir.exists()) {
            logger.error("make dir ret: {}", (Object) Boolean.valueOf(this.baseDir.mkdirs()));
        }
        return this.baseDir;
    }

    public File saveBitmapToMedia(Context context, Bitmap bitmap, String str) {
        File saveBitmap = saveBitmap(bitmap, str);
        insertImageToMediaStore(context, saveBitmap, bitmap.getWidth(), bitmap.getHeight());
        return saveBitmap;
    }

    private void insertImageToMediaStore(Context context, File file, int i, int i2) {
        long currentTimeMillis = System.currentTimeMillis();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(currentTimeMillis);
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", file.getName());
        contentValues.put("_display_name", file.getName());
        contentValues.put("date_modified", Long.valueOf(seconds));
        contentValues.put("date_added", Long.valueOf(seconds));
        contentValues.put("_data", file.getAbsolutePath());
        contentValues.put("_size", Long.valueOf(file.length()));
        contentValues.put("datetaken", Long.valueOf(currentTimeMillis));
        contentValues.put("orientation", 0);
        contentValues.put("width", Integer.valueOf(i));
        contentValues.put("height", Integer.valueOf(i2));
        contentValues.put("mime_type", "image/png");
        logger.info("insert image to media store success: {}", (Object) context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues));
    }

    public File saveBitmapAs(Bitmap bitmap, String str) {
        File file = new File(getBaseFileDir(), str);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            logger.error("saveBitmap error: {}", (Object) e.getMessage());
        }
        return file;
    }

    public File saveBitmap(Bitmap bitmap, String str) {
        String concat = str.concat(String.valueOf(System.currentTimeMillis()));
        File file = new File(getBaseFileDir(), getFilenameForUrl(concat) + ".png");
        if (isFileValid(file)) {
            return file;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            logger.error("saveBitmap error: {}", (Object) e.getMessage());
        }
        return file;
    }

    private boolean isFileValid(File file) {
        return file.exists() && file.length() > 0;
    }

    public String getFilenameForUrl(String str) {
        int length = str.length() / 2;
        String valueOf = String.valueOf(str.substring(0, length).hashCode());
        return valueOf + String.valueOf(str.substring(length).hashCode());
    }
}
