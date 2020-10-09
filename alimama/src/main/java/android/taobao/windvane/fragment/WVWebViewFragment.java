package android.taobao.windvane.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.WVWebChromeClient;
import android.taobao.windvane.webview.WVWebView;
import android.taobao.windvane.webview.WVWebViewClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

public class WVWebViewFragment extends Fragment {
    private static String TAG = "WVWebViewFragment";
    public static String URL = "url";
    private Activity activity;
    private WVWebChromeClient mChromeClient = null;
    private WVWebView mWebView = null;
    private WVWebViewClient mWebclient = null;
    private String url = null;

    public void onAttach(Activity activity2) {
        super.onAttach(activity2);
        this.activity = activity2;
    }

    @Deprecated
    public WVWebViewFragment() {
    }

    public WVWebViewFragment(Activity activity2) {
        this.activity = activity2;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.url = arguments.getString(URL);
        }
    }

    public void onDestroy() {
        if (this.mWebView != null) {
            this.mWebView.setVisibility(8);
            this.mWebView.removeAllViews();
            if (this.mWebView.getParent() != null) {
                ((ViewGroup) this.mWebView.getParent()).removeView(this.mWebView);
            }
            this.mWebView.loadUrl("about:blank");
            this.mWebView.destroy();
            this.mWebView = null;
        }
        this.activity = null;
        try {
            super.onDestroy();
        } catch (Exception unused) {
        }
    }

    public void onDetach() {
        super.onDetach();
    }

    public void onLowMemory() {
        super.onLowMemory();
    }

    public void onPause() {
        if (this.mWebView != null) {
            this.mWebView.onPause();
        }
        super.onPause();
    }

    public void onResume() {
        if (this.mWebView != null) {
            this.mWebView.onResume();
        }
        super.onResume();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getWebView();
        if (this.url == null || this.mWebView == null) {
            TaoLog.d(TAG, "image urls is null");
        } else {
            this.mWebView.loadUrl(this.url);
        }
        return this.mWebView;
    }

    public void setWebViewClient(WVWebViewClient wVWebViewClient) {
        if (wVWebViewClient != null) {
            this.mWebclient = wVWebViewClient;
            if (this.mWebView != null) {
                this.mWebView.setWebViewClient(this.mWebclient);
            }
        }
    }

    public void setWebchormeClient(WVWebChromeClient wVWebChromeClient) {
        if (wVWebChromeClient != null) {
            this.mChromeClient = wVWebChromeClient;
            if (this.mWebView != null) {
                this.mWebView.setWebChromeClient(this.mChromeClient);
            }
        }
    }

    public WebView getWebView() {
        if (this.mWebView == null) {
            Context activity2 = this.activity == null ? getActivity() : this.activity;
            if (activity2 == null) {
                return null;
            }
            this.mWebView = new WVWebView(activity2);
            setWebViewClient(this.mWebclient);
            setWebchormeClient(this.mChromeClient);
            this.mWebView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        }
        return this.mWebView;
    }

    public boolean onBackPressed() {
        if (getWebView() == null || !getWebView().canGoBack()) {
            return false;
        }
        getWebView().goBack();
        return true;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (this.mWebView != null) {
            this.mWebView.onActivityResult(i, i2, intent);
        }
    }
}
