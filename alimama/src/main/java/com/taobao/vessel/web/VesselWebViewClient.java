package com.taobao.vessel.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import com.taobao.vessel.callback.OnLoadListener;
import com.taobao.vessel.model.VesselError;
import com.taobao.vessel.utils.Utils;
import com.taobao.vessel.utils.VesselConstants;
import com.uc.webview.export.WebView;

public class VesselWebViewClient extends WVUCWebViewClient {
    private OnLoadListener mOnLoadListener;

    public VesselWebViewClient(Context context, OnLoadListener onLoadListener) {
        super(context);
        this.mOnLoadListener = onLoadListener;
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        super.onPageStarted(webView, str, bitmap);
        if (this.mOnLoadListener != null) {
            this.mOnLoadListener.onLoadStart();
        }
    }

    public void onPageFinished(WebView webView, String str) {
        super.onPageFinished(webView, str);
        if (this.mOnLoadListener != null) {
            this.mOnLoadListener.onLoadFinish(webView);
        }
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        super.onReceivedError(webView, i, str, str2);
        if (this.mOnLoadListener != null) {
            OnLoadListener onLoadListener = this.mOnLoadListener;
            onLoadListener.onLoadError(new VesselError(i + "", str, VesselConstants.WEB_TYPE));
            Utils.commitFail(VesselConstants.LOAD_ERROR, i + str);
        }
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        return super.shouldOverrideUrlLoading(webView, str);
    }
}
