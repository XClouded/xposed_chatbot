package com.alimama.union.app.infrastructure.image.capture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.alimama.moon.qrcode.NativeQrCodeGenerator;
import com.alimama.union.app.infrastructure.image.capture.Capture;
import com.alimama.union.app.infrastructure.image.save.IImageSaver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitmapCapture implements Capture {
    /* access modifiers changed from: private */
    public static final Pattern QRCODE_URL_PATTERN = Pattern.compile("^http[s]?://gcodex\\.alicdn\\.com/qrcode\\.do");
    /* access modifiers changed from: private */
    public IImageSaver imageSaver;
    /* access modifiers changed from: private */
    public Logger logger = LoggerFactory.getLogger((Class<?>) BitmapCapture.class);

    @Inject
    public BitmapCapture(IImageSaver iImageSaver) {
        this.imageSaver = iImageSaver;
    }

    public void captureHtml(final Context context, String str, int i, int i2, final Capture.CaptureCallback captureCallback) {
        WebView webView = new WebView(context);
        Toast.makeText(context, "正在截图", 0).show();
        webView.setLayerType(1, (Paint) null);
        webView.setDrawingCacheEnabled(true);
        webView.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(i2, 1073741824));
        webView.layout(0, 0, webView.getMeasuredWidth(), webView.getMeasuredHeight());
        webView.getSettings().setUseWideViewPort(true);
        webView.setInitialScale(-1);
        webView.loadData(str, "text/html; charset=UTF-8", (String) null);
        webView.setWebViewClient(new WebViewClient() {
            public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
                if (!BitmapCapture.QRCODE_URL_PATTERN.matcher(str).find()) {
                    return null;
                }
                BitmapCapture.this.logger.info("native generate qrcode bitmap");
                Uri parse = Uri.parse(str);
                String queryParameter = parse.getQueryParameter("content");
                String queryParameter2 = parse.getQueryParameter("size");
                if (TextUtils.isEmpty(queryParameter2)) {
                    queryParameter2 = "280";
                }
                Integer valueOf = Integer.valueOf(queryParameter2);
                try {
                    Bitmap genQRCodeBitmap = NativeQrCodeGenerator.getInstance().genQRCodeBitmap(queryParameter, valueOf.intValue(), valueOf.intValue());
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    genQRCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    return new WebResourceResponse("image/png", "utf-8", new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                } catch (Exception e) {
                    BitmapCapture.this.logger.error("native generate qrcode error: {}", (Object) e.getMessage());
                    return null;
                }
            }

            public void onPageFinished(final WebView webView, final String str) {
                BitmapCapture.this.logger.info("onPageFinished, view.width: {}, view.height: {}, url: {}", Integer.valueOf(webView.getWidth()), Integer.valueOf(webView.getHeight()), str);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            TimeUnit.MILLISECONDS.sleep(1000);
                        } catch (InterruptedException e) {
                            BitmapCapture.this.logger.error("wait webview reader failure: {}", (Object) e.getMessage());
                        }
                        Bitmap createBitmap = Bitmap.createBitmap(webView.getDrawingCache());
                        IImageSaver access$200 = BitmapCapture.this.imageSaver;
                        Context context = context;
                        File saveBitmapToMedia = access$200.saveBitmapToMedia(context, createBitmap, str + System.currentTimeMillis());
                        if (createBitmap != null) {
                            createBitmap.recycle();
                        }
                        captureCallback.onSuccess(saveBitmapToMedia);
                    }
                }).start();
            }
        });
    }
}
