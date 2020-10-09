package com.taobao.uikit.feature.features;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.taobao.uikit.base.R;
import com.taobao.uikit.feature.callback.ImageSaveCallback;
import com.taobao.uikit.feature.callback.TouchEventCallback;
import com.taobao.uikit.feature.view.TBackFragment;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

public class ImageSaveFeature extends AbsFeature<ImageView> implements ImageSaveCallback, TouchEventCallback {
    public static final int IMAGE_SAVE_REQUEST_CODE = 1502;
    private boolean mActive = false;
    private HashMap<String, ImageSaveFeatureCallback> mChoices = new HashMap<>();
    /* access modifiers changed from: private */
    public Context mContext;
    private Dialog mDialog;
    private Bitmap mSaveBmp = null;
    private SaveFileTask mSaveFileTask;
    private PointF mStartPoint = new PointF();

    public interface ImageSaveFeatureCallback {
        void doMethod(ImageView imageView);
    }

    public void afterDispatchTouchEvent(MotionEvent motionEvent) {
    }

    public void afterOnTouchEvent(MotionEvent motionEvent) {
    }

    public void afterPerformLongClick() {
    }

    public void beforeDispatchTouchEvent(MotionEvent motionEvent) {
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
    }

    public void setHost(ImageView imageView) {
        super.setHost(imageView);
        this.mContext = imageView.getContext();
        if (this.mContext != null) {
            this.mChoices.put(this.mContext.getResources().getString(R.string.uik_save_image), new ImageSaveFeatureCallback() {
                public void doMethod(ImageView imageView) {
                    ImageSaveFeature.this.saveImageView(imageView);
                }
            });
        }
    }

    public void beforePerformLongClick() {
        if (this.mActive) {
            showDialog();
        }
    }

    public void beforeOnTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action == 0) {
            this.mStartPoint.set(motionEvent.getX(), motionEvent.getY());
            this.mActive = true;
        } else if (action == 2) {
            float x = motionEvent.getX() - this.mStartPoint.x;
            float y = motionEvent.getY() - this.mStartPoint.y;
            if (Math.abs(x) > 10.0f || Math.abs(y) > 10.0f) {
                this.mActive = false;
                dismissDialog();
            }
        } else if (action == 5) {
            this.mActive = false;
            dismissDialog();
        }
    }

    public void addDialogChoice(String str, ImageSaveFeatureCallback imageSaveFeatureCallback) {
        this.mChoices.put(str, imageSaveFeatureCallback);
    }

    @SuppressLint({"InflateParams"})
    private void showDialog() {
        if (this.mChoices.size() > 0) {
            if (this.mContext == null || !(this.mContext instanceof Activity) || !((Activity) this.mContext).isFinishing()) {
                this.mDialog = new Dialog(this.mContext, R.style.uik_imagesavedialog);
                LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
                LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.uik_image_save_dialog, (ViewGroup) null);
                Iterator<String> it = this.mChoices.keySet().iterator();
                while (it.hasNext()) {
                    TextView textView = (TextView) layoutInflater.inflate(R.layout.uik_image_save_choice, linearLayout, false);
                    String next = it.next();
                    textView.setText(next);
                    final ImageSaveFeatureCallback imageSaveFeatureCallback = this.mChoices.get(next);
                    textView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            imageSaveFeatureCallback.doMethod((ImageView) ImageSaveFeature.this.getHost());
                            ImageSaveFeature.this.dismissDialog();
                        }
                    });
                    linearLayout.addView(textView);
                    if (it.hasNext()) {
                        linearLayout.addView(layoutInflater.inflate(R.layout.uik_choice_divider, linearLayout, false));
                    }
                }
                this.mDialog.setContentView(linearLayout);
                this.mDialog.show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void dismissDialog() {
        if (this.mDialog != null && this.mDialog.isShowing()) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }

    /* access modifiers changed from: private */
    public void saveImageView(ImageView imageView) {
        this.mSaveBmp = getBitmap(imageView);
        if (this.mSaveBmp == null) {
            Toast.makeText(this.mContext.getApplicationContext(), getStringResource(R.string.uik_save_image_fail_get), 0).show();
        } else if (checkSavePlan()) {
            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (externalStoragePublicDirectory.exists() || externalStoragePublicDirectory.mkdirs()) {
                saveImageFile(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + imageView.toString().hashCode() + ".png")), this.mSaveBmp);
                return;
            }
            Toast.makeText(this.mContext.getApplicationContext(), getStringResource(R.string.uik_save_image_fail), 0).show();
        } else {
            Intent intent = new Intent("android.intent.action.CREATE_DOCUMENT");
            intent.addCategory("android.intent.category.OPENABLE");
            intent.setType("image/*");
            intent.putExtra("android.intent.extra.TITLE", imageView.toString().hashCode() + ".png");
            Context context = this.mContext;
            while ((context instanceof ContextWrapper) && !(context instanceof Activity)) {
                context = ((ContextWrapper) context).getBaseContext();
            }
            if (context == null || !(context instanceof Activity)) {
                Toast.makeText(this.mContext.getApplicationContext(), getStringResource(R.string.uik_save_image_fail), 0).show();
                return;
            }
            FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
            TBackFragment tBackFragment = (TBackFragment) fragmentManager.findFragmentByTag("TBackFragment");
            if (tBackFragment == null) {
                tBackFragment = new TBackFragment();
                fragmentManager.beginTransaction().add(tBackFragment, "TBackFragment").commit();
                fragmentManager.executePendingTransactions();
            }
            tBackFragment.setImageSaveFeature(this);
            tBackFragment.startActivityForResult(intent, IMAGE_SAVE_REQUEST_CODE);
        }
    }

    /* access modifiers changed from: private */
    public boolean checkSavePlan() {
        if (Build.VERSION.SDK_INT < 19) {
            return true;
        }
        if (Build.VERSION.SDK_INT > 28 || this.mContext.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            return false;
        }
        return true;
    }

    private Bitmap getBitmap(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if ((drawable != null || (drawable = imageView.getBackground()) != null) && (drawable instanceof BitmapDrawable)) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        return null;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1502 && i2 == -1) {
            saveImageFile(intent.getData(), this.mSaveBmp);
        }
    }

    public void saveImageFile(Uri uri, Bitmap bitmap) {
        if (this.mSaveFileTask == null || AsyncTask.Status.RUNNING != this.mSaveFileTask.getStatus()) {
            this.mSaveFileTask = new SaveFileTask();
            this.mSaveFileTask.execute(new Object[]{uri, bitmap});
        }
    }

    /* access modifiers changed from: private */
    public void notifyNewMedia(Uri uri) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(uri);
        this.mContext.sendBroadcast(intent);
    }

    /* access modifiers changed from: private */
    public String getStringResource(int i) {
        return this.mContext.getResources().getString(i);
    }

    private class SaveFileTask extends AsyncTask<Object, Void, Integer> {
        private static final int FAIL_FULL = 2;
        private static final int FAIL_GET = 1;
        private static final int SUCCESS_SAVE = 0;
        private Uri mUri;

        private SaveFileTask() {
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Removed duplicated region for block: B:28:0x0055 A[SYNTHETIC, Splitter:B:28:0x0055] */
        /* JADX WARNING: Removed duplicated region for block: B:31:0x005d A[Catch:{ IOException -> 0x003d }] */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x0064 A[SYNTHETIC, Splitter:B:34:0x0064] */
        /* JADX WARNING: Removed duplicated region for block: B:39:0x006f A[Catch:{ IOException -> 0x006b }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Integer doInBackground(java.lang.Object... r6) {
            /*
                r5 = this;
                r0 = 0
                r1 = r6[r0]
                android.net.Uri r1 = (android.net.Uri) r1
                r5.mUri = r1
                r1 = 1
                r6 = r6[r1]
                android.graphics.Bitmap r6 = (android.graphics.Bitmap) r6
                if (r6 == 0) goto L_0x0077
                r1 = 0
                com.taobao.uikit.feature.features.ImageSaveFeature r2 = com.taobao.uikit.feature.features.ImageSaveFeature.this     // Catch:{ Exception -> 0x004d, all -> 0x004a }
                android.content.Context r2 = r2.mContext     // Catch:{ Exception -> 0x004d, all -> 0x004a }
                android.content.ContentResolver r2 = r2.getContentResolver()     // Catch:{ Exception -> 0x004d, all -> 0x004a }
                android.net.Uri r3 = r5.mUri     // Catch:{ Exception -> 0x004d, all -> 0x004a }
                java.lang.String r4 = "w"
                android.os.ParcelFileDescriptor r2 = r2.openFileDescriptor(r3, r4)     // Catch:{ Exception -> 0x004d, all -> 0x004a }
                java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0048 }
                java.io.FileDescriptor r4 = r2.getFileDescriptor()     // Catch:{ Exception -> 0x0048 }
                r3.<init>(r4)     // Catch:{ Exception -> 0x0048 }
                android.graphics.Bitmap$CompressFormat r1 = android.graphics.Bitmap.CompressFormat.PNG     // Catch:{ Exception -> 0x0045, all -> 0x0042 }
                r4 = 100
                r6.compress(r1, r4, r3)     // Catch:{ Exception -> 0x0045, all -> 0x0042 }
                r3.flush()     // Catch:{ IOException -> 0x003d }
                r3.close()     // Catch:{ IOException -> 0x003d }
                if (r2 == 0) goto L_0x0078
                r2.close()     // Catch:{ IOException -> 0x003d }
                goto L_0x0078
            L_0x003d:
                r6 = move-exception
                r6.printStackTrace()
                goto L_0x0078
            L_0x0042:
                r6 = move-exception
                r1 = r3
                goto L_0x0062
            L_0x0045:
                r6 = move-exception
                r1 = r3
                goto L_0x004f
            L_0x0048:
                r6 = move-exception
                goto L_0x004f
            L_0x004a:
                r6 = move-exception
                r2 = r1
                goto L_0x0062
            L_0x004d:
                r6 = move-exception
                r2 = r1
            L_0x004f:
                r6.printStackTrace()     // Catch:{ all -> 0x0061 }
                r0 = 2
                if (r1 == 0) goto L_0x005b
                r1.flush()     // Catch:{ IOException -> 0x003d }
                r1.close()     // Catch:{ IOException -> 0x003d }
            L_0x005b:
                if (r2 == 0) goto L_0x0078
                r2.close()     // Catch:{ IOException -> 0x003d }
                goto L_0x0078
            L_0x0061:
                r6 = move-exception
            L_0x0062:
                if (r1 == 0) goto L_0x006d
                r1.flush()     // Catch:{ IOException -> 0x006b }
                r1.close()     // Catch:{ IOException -> 0x006b }
                goto L_0x006d
            L_0x006b:
                r0 = move-exception
                goto L_0x0073
            L_0x006d:
                if (r2 == 0) goto L_0x0076
                r2.close()     // Catch:{ IOException -> 0x006b }
                goto L_0x0076
            L_0x0073:
                r0.printStackTrace()
            L_0x0076:
                throw r6
            L_0x0077:
                r0 = 1
            L_0x0078:
                java.lang.Integer r6 = java.lang.Integer.valueOf(r0)
                return r6
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.uikit.feature.features.ImageSaveFeature.SaveFileTask.doInBackground(java.lang.Object[]):java.lang.Integer");
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Integer num) {
            switch (num.intValue()) {
                case 0:
                    Toast.makeText(ImageSaveFeature.this.mContext.getApplicationContext(), ImageSaveFeature.this.getStringResource(R.string.uik_save_image_success), 0).show();
                    if (ImageSaveFeature.this.checkSavePlan()) {
                        ImageSaveFeature.this.notifyNewMedia(this.mUri);
                        return;
                    }
                    return;
                case 1:
                    Toast.makeText(ImageSaveFeature.this.mContext.getApplicationContext(), ImageSaveFeature.this.getStringResource(R.string.uik_save_image_fail_get), 0).show();
                    return;
                case 2:
                    Toast.makeText(ImageSaveFeature.this.mContext.getApplicationContext(), ImageSaveFeature.this.getStringResource(R.string.uik_save_image_fail_full), 0).show();
                    return;
                default:
                    return;
            }
        }
    }
}
