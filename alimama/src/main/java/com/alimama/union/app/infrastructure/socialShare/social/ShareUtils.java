package com.alimama.union.app.infrastructure.socialShare.social;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.alimama.moon.config.MoonConfigCenter;
import com.alimama.moon.ui.CommonDialog;
import com.alimama.moon.ui.WeexActivity;
import com.alimama.union.app.configcenter.ConfigKeyList;
import com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver;
import com.alimama.union.app.infrastructure.weex.WeexPageGenerator;
import com.alimama.union.app.pagerouter.AppPageInfo;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimamaunion.base.configcenter.EtaoConfigCenter;
import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.taobao.highavail.HighAvailPlugin;
import com.ut.mini.UTAnalytics;
import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShareUtils {
    private static final String FIRST_USE_SYSTEM_SHARE = "first_use_system_share";
    public static final String PLATFORM_QQ = "qqfriend";
    public static final String PLATFORM_WECHAT = "wxfriend";
    public static final String PLATFORM_WEIBO = "sinaweibo";
    private static final String TAG = "ShareUtils";
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) ShareUtils.class);

    private ShareUtils() {
    }

    public static void showShare(@NonNull Context context, @Nullable String str) {
        if (TextUtils.isEmpty(str) || !shouldShowNativeShare(str) || MoonConfigCenter.isShareWeexSwitchOn()) {
            Intent intent = new Intent(context, WeexActivity.class);
            Uri.Builder buildUpon = WeexPageGenerator.getShareCreatorUri().buildUpon();
            buildUpon.appendQueryParameter("url", str);
            intent.setData(buildUpon.build());
            context.startActivity(intent);
            return;
        }
        addSpmUrl(str);
        if (MoonConfigCenter.isShareFlutterSwitchOn()) {
            HighAvailPlugin.setPageStartTime(System.currentTimeMillis());
            MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_SHARE_FLUTTER, Uri.parse(str));
            return;
        }
        MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_SHARE, Uri.parse(str));
    }

    public static Uri uriForShare(Context context, File file) {
        if (Build.VERSION.SDK_INT >= 24) {
            return getImgContentUri(context, file);
        }
        return Uri.fromFile(file);
    }

    private static Uri getImgContentUri(Context context, File file) {
        String absolutePath = file.getAbsolutePath();
        Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data=?", new String[]{absolutePath}, (String) null);
        Uri uri = null;
        if (query != null) {
            if (query.moveToFirst()) {
                int i = query.getInt(query.getColumnIndex("_id"));
                Uri parse = Uri.parse("content://media/external/images/media");
                uri = Uri.withAppendedPath(parse, "" + i);
            }
            query.close();
        }
        if (uri != null) {
            return uri;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("_data", absolutePath);
        return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    public static boolean isWechatInstalled(@NonNull Context context) {
        return isInstallAppWithPlatform(context, PLATFORM_WECHAT).isInstalled;
    }

    public static boolean openWechat(@NonNull Context context) {
        return isWechatInstalled(context) && openApp(context, "com.tencent.mm");
    }

    private static boolean isWeiboInstalled(@NonNull Context context) {
        return isInstallAppWithPlatform(context, PLATFORM_WEIBO).isInstalled;
    }

    public static boolean shareWeibo(@NonNull Context context, @Nullable String str, @Nullable ArrayList<Uri> arrayList) {
        if (!isWeiboInstalled(context)) {
            return false;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.setType("image/png");
        intent.putExtra("android.intent.extra.TEXT", str);
        intent.putExtra("android.intent.extra.STREAM", arrayList);
        intent.setComponent(new ComponentName(WeiboShare.PKG, WeiboShare.CLS));
        intent.addFlags(134217728);
        intent.addFlags(268435456);
        context.startActivity(intent);
        return true;
    }

    public static boolean isQqInstalled(@NonNull Context context) {
        return isInstallAppWithPlatform(context, PLATFORM_QQ).isInstalled;
    }

    public static boolean openQq(@NonNull Context context) {
        return isQqInstalled(context) && openApp(context, QQFriendShare.PKG);
    }

    private static boolean openApp(@NonNull Context context, String str) {
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(str);
        if (launchIntentForPackage == null) {
            logger.error("open app failed for packageName {}", (Object) str);
            return false;
        }
        context.startActivity(launchIntentForPackage);
        return true;
    }

    public static boolean isInstallApp(Context context, String str, String str2) {
        ActivityInfo activityInfo;
        try {
            activityInfo = context.getPackageManager().getActivityInfo(new ComponentName(str, str2), 128);
        } catch (Exception e) {
            logger.error(e.getMessage());
            activityInfo = null;
        }
        return activityInfo != null;
    }

    public static class InstallApp {
        private String errorMsg;
        /* access modifiers changed from: private */
        public boolean isInstalled;

        public boolean isInstalled() {
            return this.isInstalled;
        }

        public void setInstalled(boolean z) {
            this.isInstalled = z;
        }

        public String getErrorMsg() {
            return this.errorMsg;
        }

        public void setErrorMsg(String str) {
            this.errorMsg = str;
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.alimama.union.app.infrastructure.socialShare.social.ShareUtils.InstallApp isInstallAppWithPlatform(android.content.Context r4, java.lang.String r5) {
        /*
            java.lang.String r0 = ""
            java.lang.String r1 = ""
            com.alimama.union.app.infrastructure.socialShare.social.ShareUtils$InstallApp r2 = new com.alimama.union.app.infrastructure.socialShare.social.ShareUtils$InstallApp
            r2.<init>()
            int r3 = r5.hashCode()
            switch(r3) {
                case -1838124510: goto L_0x003a;
                case -1656144897: goto L_0x0030;
                case -951770676: goto L_0x0026;
                case -904024897: goto L_0x001b;
                case -393543490: goto L_0x0011;
                default: goto L_0x0010;
            }
        L_0x0010:
            goto L_0x0045
        L_0x0011:
            java.lang.String r3 = "qqfriend"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x0045
            r5 = 3
            goto L_0x0046
        L_0x001b:
            java.lang.String r3 = "wxfriend"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x0045
            r5 = 0
            goto L_0x0046
        L_0x0026:
            java.lang.String r3 = "qqzone"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x0045
            r5 = 4
            goto L_0x0046
        L_0x0030:
            java.lang.String r3 = "sinaweibo"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x0045
            r5 = 2
            goto L_0x0046
        L_0x003a:
            java.lang.String r3 = "wxtimeline"
            boolean r5 = r5.equals(r3)
            if (r5 == 0) goto L_0x0045
            r5 = 1
            goto L_0x0046
        L_0x0045:
            r5 = -1
        L_0x0046:
            switch(r5) {
                case 0: goto L_0x0076;
                case 1: goto L_0x006b;
                case 2: goto L_0x0060;
                case 3: goto L_0x0055;
                case 4: goto L_0x004a;
                default: goto L_0x0049;
            }
        L_0x0049:
            goto L_0x0080
        L_0x004a:
            java.lang.String r0 = "com.qzone"
            java.lang.String r1 = "com.qzonex.module.operation.ui.QZonePublishMoodActivity"
            java.lang.String r5 = "您还未安装QQ空间，请安装完成后再来试试"
            r2.setErrorMsg(r5)
            goto L_0x0080
        L_0x0055:
            java.lang.String r0 = "com.tencent.mobileqq"
            java.lang.String r1 = "com.tencent.mobileqq.activity.JumpActivity"
            java.lang.String r5 = "您还未安装QQ，请安装完成后再来试试"
            r2.setErrorMsg(r5)
            goto L_0x0080
        L_0x0060:
            java.lang.String r0 = "com.sina.weibo"
            java.lang.String r1 = "com.sina.weibo.composerinde.ComposerDispatchActivity"
            java.lang.String r5 = "您还未安装微博，请安装完成后再来试试"
            r2.setErrorMsg(r5)
            goto L_0x0080
        L_0x006b:
            java.lang.String r0 = "com.tencent.mm"
            java.lang.String r1 = "com.tencent.mm.ui.tools.ShareToTimeLineUI"
            java.lang.String r5 = "您还未安装微信，请安装完成后再来试试"
            r2.setErrorMsg(r5)
            goto L_0x0080
        L_0x0076:
            java.lang.String r0 = "com.tencent.mm"
            java.lang.String r1 = "com.tencent.mm.ui.tools.ShareImgUI"
            java.lang.String r5 = "您还未安装微信，请安装完成后再来试试"
            r2.setErrorMsg(r5)
        L_0x0080:
            boolean r4 = isInstallApp(r4, r0, r1)
            r2.setInstalled(r4)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.infrastructure.socialShare.social.ShareUtils.isInstallAppWithPlatform(android.content.Context, java.lang.String):com.alimama.union.app.infrastructure.socialShare.social.ShareUtils$InstallApp");
    }

    public static void showDialog(Context context, String str) {
        FragmentManager supportFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        Fragment findFragmentByTag = supportFragmentManager.findFragmentByTag("notInstallDialog");
        if (findFragmentByTag != null) {
            beginTransaction.remove(findFragmentByTag);
        }
        beginTransaction.addToBackStack((String) null);
        CommonDialog commonDialog = new CommonDialog();
        Bundle bundle = new Bundle();
        bundle.putString(CommonDialog.EXTRA_ERROR, str);
        commonDialog.setArguments(bundle);
        try {
            commonDialog.show(beginTransaction, "notInstallDialog");
        } catch (Exception unused) {
        }
    }

    @WorkerThread
    @Nullable
    public static Uri saveImageSync(@NonNull Context context, String str, @Nullable Bitmap bitmap) {
        if (TextUtils.isEmpty(str) || bitmap == null) {
            return null;
        }
        try {
            File saveBitmap = ExternalPublicStorageSaver.getInstance().saveBitmap(bitmap, str);
            if (saveBitmap != null && saveBitmap.exists()) {
                if (saveBitmap.length() > 0) {
                    Uri uriForShare = uriForShare(context, saveBitmap);
                    context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(saveBitmap)));
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                    return uriForShare;
                }
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return null;
        } catch (Exception e) {
            logger.warn("save bitmap locally failed", (Throwable) e);
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            return null;
        } catch (Throwable th) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x009e, code lost:
        if (r4.isRecycled() == false) goto L_0x00dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00da, code lost:
        if (r4.isRecycled() == false) goto L_0x00dc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00dc, code lost:
        r4.recycle();
     */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00d6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.ArrayList<android.net.Uri> processShareImages(android.content.Context r6, com.alimama.union.app.infrastructure.socialShare.ShareObj r7, java.util.List<android.net.Uri> r8) {
        /*
            r0 = r8
            java.util.ArrayList r0 = (java.util.ArrayList) r0
            r1 = 0
            com.alimama.moon.qrcode.NativeQrCodeGenerator r2 = com.alimama.moon.qrcode.NativeQrCodeGenerator.getInstance()     // Catch:{ Exception -> 0x00b8, all -> 0x00b3 }
            java.lang.String r3 = r7.getUrl()     // Catch:{ Exception -> 0x00b8, all -> 0x00b3 }
            android.graphics.Bitmap r2 = r2.genQRCodeBitmap(r3)     // Catch:{ Exception -> 0x00b8, all -> 0x00b3 }
            android.content.ContentResolver r3 = r6.getContentResolver()     // Catch:{ Exception -> 0x00ae, all -> 0x00ab }
            int r4 = r8.size()     // Catch:{ Exception -> 0x00ae, all -> 0x00ab }
            int r4 = r4 + -1
            java.lang.Object r4 = r8.get(r4)     // Catch:{ Exception -> 0x00ae, all -> 0x00ab }
            android.net.Uri r4 = (android.net.Uri) r4     // Catch:{ Exception -> 0x00ae, all -> 0x00ab }
            android.graphics.Bitmap r3 = android.provider.MediaStore.Images.Media.getBitmap(r3, r4)     // Catch:{ Exception -> 0x00ae, all -> 0x00ab }
            com.alimama.union.app.infrastructure.image.piccollage.ICollage r4 = com.alimama.union.app.infrastructure.image.piccollage.PicCollageFactory.createPicCollage(r6, r7)     // Catch:{ Exception -> 0x00a8, all -> 0x00a5 }
            android.graphics.Bitmap r4 = r4.collage(r7, r3, r2)     // Catch:{ Exception -> 0x00a8, all -> 0x00a5 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            r1.<init>()     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            int r5 = r8.size()     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            int r5 = r5 + -1
            java.lang.Object r8 = r8.get(r5)     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            android.net.Uri r8 = (android.net.Uri) r8     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            r1.append(r8)     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            java.lang.String r8 = "collage"
            r1.append(r8)     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver r8 = com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver.getInstance()     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            java.lang.String r7 = r7.getUrl()     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            java.lang.String r7 = r8.getFilenameForUrl(r7)     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            r1.append(r7)     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            java.lang.String r7 = r1.toString()     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver r8 = com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver.getInstance()     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            java.io.File r7 = r8.saveBitmap(r4, r7)     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            android.net.Uri r8 = uriForShare(r6, r7)     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            int r1 = r0.size()     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            int r1 = r1 + -1
            r0.remove(r1)     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            r0.add(r8)     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            android.content.Intent r8 = new android.content.Intent     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            java.lang.String r1 = "android.intent.action.MEDIA_SCANNER_SCAN_FILE"
            android.net.Uri r7 = android.net.Uri.fromFile(r7)     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            r8.<init>(r1, r7)     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            r6.sendBroadcast(r8)     // Catch:{ Exception -> 0x00a3, all -> 0x00a1 }
            if (r2 == 0) goto L_0x008d
            boolean r6 = r2.isRecycled()
            if (r6 != 0) goto L_0x008d
            r2.recycle()
        L_0x008d:
            if (r3 == 0) goto L_0x0098
            boolean r6 = r3.isRecycled()
            if (r6 != 0) goto L_0x0098
            r3.recycle()
        L_0x0098:
            if (r4 == 0) goto L_0x00df
            boolean r6 = r4.isRecycled()
            if (r6 != 0) goto L_0x00df
            goto L_0x00dc
        L_0x00a1:
            r6 = move-exception
            goto L_0x00e2
        L_0x00a3:
            r6 = move-exception
            goto L_0x00b1
        L_0x00a5:
            r6 = move-exception
            r4 = r1
            goto L_0x00e2
        L_0x00a8:
            r6 = move-exception
            r4 = r1
            goto L_0x00b1
        L_0x00ab:
            r6 = move-exception
            r3 = r1
            goto L_0x00b6
        L_0x00ae:
            r6 = move-exception
            r3 = r1
            r4 = r3
        L_0x00b1:
            r1 = r2
            goto L_0x00bb
        L_0x00b3:
            r6 = move-exception
            r2 = r1
            r3 = r2
        L_0x00b6:
            r4 = r3
            goto L_0x00e2
        L_0x00b8:
            r6 = move-exception
            r3 = r1
            r4 = r3
        L_0x00bb:
            r6.printStackTrace()     // Catch:{ all -> 0x00e0 }
            if (r1 == 0) goto L_0x00c9
            boolean r6 = r1.isRecycled()
            if (r6 != 0) goto L_0x00c9
            r1.recycle()
        L_0x00c9:
            if (r3 == 0) goto L_0x00d4
            boolean r6 = r3.isRecycled()
            if (r6 != 0) goto L_0x00d4
            r3.recycle()
        L_0x00d4:
            if (r4 == 0) goto L_0x00df
            boolean r6 = r4.isRecycled()
            if (r6 != 0) goto L_0x00df
        L_0x00dc:
            r4.recycle()
        L_0x00df:
            return r0
        L_0x00e0:
            r6 = move-exception
            r2 = r1
        L_0x00e2:
            if (r2 == 0) goto L_0x00ed
            boolean r7 = r2.isRecycled()
            if (r7 != 0) goto L_0x00ed
            r2.recycle()
        L_0x00ed:
            if (r3 == 0) goto L_0x00f8
            boolean r7 = r3.isRecycled()
            if (r7 != 0) goto L_0x00f8
            r3.recycle()
        L_0x00f8:
            if (r4 == 0) goto L_0x0103
            boolean r7 = r4.isRecycled()
            if (r7 != 0) goto L_0x0103
            r4.recycle()
        L_0x0103:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.union.app.infrastructure.socialShare.social.ShareUtils.processShareImages(android.content.Context, com.alimama.union.app.infrastructure.socialShare.ShareObj, java.util.List):java.util.ArrayList");
    }

    public static void markUsedSystemShare(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences("qrcode_switch_data", 0).edit();
        edit.putBoolean(FIRST_USE_SYSTEM_SHARE, false);
        edit.apply();
    }

    public static boolean isFirstTimeUsingSysShare(Context context) {
        return context.getSharedPreferences("qrcode_switch_data", 0).getBoolean(FIRST_USE_SYSTEM_SHARE, true);
    }

    private static boolean shouldShowNativeShare(String str) {
        try {
            str = URLDecoder.decode(str, "UTF-8");
        } catch (Exception e) {
            logger.error("url decode error: {}", (Object) e.getMessage());
        }
        String configResult = EtaoConfigCenter.getInstance().getConfigResult(ConfigKeyList.UNION_SHARE_REGEX);
        if (TextUtils.isEmpty(configResult)) {
            return false;
        }
        try {
            SafeJSONArray optJSONArray = new SafeJSONObject(new SafeJSONObject(configResult).optJSONObject("data").optString("shareRegexJson")).optJSONArray("itemUrl");
            for (int i = 0; i < optJSONArray.length(); i++) {
                if (regexUtil(optJSONArray.optString(i), str)) {
                    return true;
                }
            }
        } catch (JSONException e2) {
            logger.error("safejson or regex error: {}", (Object) e2.getMessage());
        }
        return false;
    }

    private static boolean regexUtil(String str, String str2) {
        return Pattern.compile(str).matcher(str2).find();
    }

    private static void addSpmUrl(String str) {
        String queryParameter = Uri.parse(str).getQueryParameter("spm");
        HashMap hashMap = new HashMap();
        hashMap.put("spm-url", queryParameter);
        UTAnalytics.getInstance().getDefaultTracker().updateNextPageProperties(hashMap);
    }
}
