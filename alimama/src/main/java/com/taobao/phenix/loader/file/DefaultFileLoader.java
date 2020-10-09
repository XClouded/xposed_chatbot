package com.taobao.phenix.loader.file;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import com.taobao.phenix.entity.ResponseData;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.request.SchemeInfo;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class DefaultFileLoader implements FileLoader {
    private static boolean mUseNewThumbNail;

    public static void setUseNewThumb(boolean z) {
        mUseNewThumbNail = z;
    }

    public ResponseData load(SchemeInfo schemeInfo, String str, Map<String, String> map) throws IOException, Resources.NotFoundException, UnSupportedSchemeException {
        int i = schemeInfo.type;
        if (i == 36) {
            Context applicationContext = Phenix.instance().applicationContext();
            if (applicationContext != null) {
                TypedValue typedValue = new TypedValue();
                InputStream openRawResource = applicationContext.getResources().openRawResource(schemeInfo.resId, typedValue);
                return new ResponseData(openRawResource, openRawResource.available(), typedValue);
            }
            throw new IOException("Phenix.with(Context) hasn't been called before FileLoader(resource) loading");
        } else if (i == 40) {
            byte[] decode = Base64.decode(schemeInfo.base64, 0);
            return new ResponseData(decode, 0, decode.length);
        } else if (i != 48) {
            switch (i) {
                case 33:
                    if (str.startsWith("content://")) {
                        Context applicationContext2 = Phenix.instance().applicationContext();
                        if (applicationContext2 != null) {
                            InputStream openInputStream = applicationContext2.getContentResolver().openInputStream(Uri.parse(str));
                            if (mUseNewThumbNail && Build.VERSION.SDK_INT >= 29 && (schemeInfo.thumbnailType == 1 || schemeInfo.thumbnailType == 3)) {
                                byte[] thumbnail = new ExifInterface(openInputStream).getThumbnail();
                                openInputStream.close();
                                openInputStream = (thumbnail == null || thumbnail.length <= 0) ? applicationContext2.getContentResolver().openInputStream(Uri.parse(str)) : new ByteArrayInputStream(thumbnail);
                            }
                            return new ResponseData(openInputStream, openInputStream.available());
                        }
                        throw new IOException("Phenix.with(Context) hasn't been called before FileLoader UriContent loading");
                    }
                    String str2 = schemeInfo.path;
                    if (schemeInfo.thumbnailType == 1 || schemeInfo.thumbnailType == 3) {
                        Context applicationContext3 = Phenix.instance().applicationContext();
                        if (applicationContext3 != null) {
                            String thumbnailPath = getThumbnailPath(applicationContext3, str2, schemeInfo.thumbnailType, false);
                            str2 = TextUtils.isEmpty(thumbnailPath) ? getThumbnailPath(applicationContext3, str2, schemeInfo.thumbnailType, true) : thumbnailPath;
                            if (TextUtils.isEmpty(str2) || !new File(str2).exists()) {
                                if (schemeInfo.useOriginIfThumbNotExist) {
                                    str2 = schemeInfo.path;
                                } else {
                                    throw new IOException("Retrieved thumbnail(type:" + schemeInfo.thumbnailType + ") failed from local path");
                                }
                            }
                        } else {
                            throw new IOException("Phenix.with(Context) hasn't been called before FileLoader(thumbnail) loading");
                        }
                    }
                    File file = new File(str2);
                    return new ResponseData(new FileInputStream(file), (int) file.length());
                case 34:
                    Context applicationContext4 = Phenix.instance().applicationContext();
                    if (applicationContext4 != null) {
                        InputStream open = applicationContext4.getResources().getAssets().open(schemeInfo.path);
                        return new ResponseData(open, open.available());
                    }
                    throw new IOException("Phenix.with(Context) hasn't been called before FileLoader(asset) loading");
                default:
                    throw new UnSupportedSchemeException(i);
            }
        } else {
            try {
                return Phenix.instance().getExtendedSchemeHandlers().get(schemeInfo.handleIndex).handleScheme(str);
            } catch (IndexOutOfBoundsException unused) {
                throw new UnSupportedSchemeException(i);
            }
        }
    }

    public String getThumbnailPath(Context context, String str, int i, boolean z) {
        Uri uri;
        String str2;
        Cursor cursor;
        if (z) {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        Uri uri2 = uri;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor query = contentResolver.query(uri2, new String[]{"_id"}, "_data=?", new String[]{str}, (String) null);
        long j = -1;
        if (query != null) {
            if (query.moveToFirst()) {
                j = (long) query.getInt(query.getColumnIndex("_id"));
            }
            query.close();
        }
        String str3 = null;
        if (j > 0) {
            if (z) {
                str2 = "_data";
                ContentResolver contentResolver2 = contentResolver;
                cursor = contentResolver2.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, new String[]{str2}, "video_id=" + j + " AND " + "kind" + SymbolExpUtil.SYMBOL_EQUAL + i, (String[]) null, (String) null);
            } else {
                str2 = "_data";
                cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(contentResolver, j, i, (String[]) null);
            }
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    str3 = cursor.getString(cursor.getColumnIndexOrThrow(str2));
                }
                cursor.close();
            }
        }
        return str3;
    }
}
