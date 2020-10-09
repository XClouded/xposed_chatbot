package alimama.com.unwweex.etaovessel;

import android.content.Context;
import android.graphics.Bitmap;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import com.taobao.vessel.model.VesselError;
import com.taobao.vessel.utils.Utils;
import com.taobao.vessel.utils.VesselConstants;
import com.uc.webview.export.WebView;

public class UNWVesselWebClient extends WVUCWebViewClient {
    private VesselViewCallBack mOnLoadListener;

    public UNWVesselWebClient(Context context, VesselViewCallBack vesselViewCallBack) {
        super(context);
        this.mOnLoadListener = vesselViewCallBack;
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        super.onPageStarted(webView, str, bitmap);
        if (this.mOnLoadListener != null) {
            this.mOnLoadListener.onLoadStart(webView, str);
        }
    }

    public void onPageFinished(WebView webView, String str) {
        super.onPageFinished(webView, str);
        if (this.mOnLoadListener != null) {
            this.mOnLoadListener.onLoadFinish(webView, str);
        }
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
        super.onReceivedError(webView, i, str, str2);
        if (this.mOnLoadListener != null) {
            VesselViewCallBack vesselViewCallBack = this.mOnLoadListener;
            vesselViewCallBack.onLoadError(new VesselError(i + "", str, VesselConstants.WEB_TYPE));
            Utils.commitFail(VesselConstants.LOAD_ERROR, i + str);
        }
    }

    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (this.mOnLoadListener == null || !this.mOnLoadListener.onOverrideUrl(webView, str)) {
            return super.shouldOverrideUrlLoading(webView, str);
        }
        return true;
    }
}
