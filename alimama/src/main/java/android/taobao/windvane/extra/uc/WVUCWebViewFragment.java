package android.taobao.windvane.extra.uc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.taobao.windvane.util.TaoLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class WVUCWebViewFragment extends Fragment {
    private static String TAG = "WVUCWebViewFragment";
    public static String URL = "url";
    private Activity activity;
    private WVUCWebChromeClient mChromeClient = null;
    protected WVUCWebView mWebView = null;
    private WVUCWebViewClient mWebclient = null;
    private String url = null;

    public void onAttach(Activity activity2) {
        super.onAttach(activity2);
        this.activity = activity2;
    }

    @Deprecated
    public WVUCWebViewFragment() {
    }

    public WVUCWebViewFragment(Activity activity2) {
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
            this.mWebView.destroy();
            this.mWebView = null;
        }
        this.activity = null;
        try {
            super.onDestroy();
        } catch (Exception e) {
            TaoLog.e(TAG, e.getMessage());
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

    public void setWebViewClient(WVUCWebViewClient wVUCWebViewClient) {
        if (wVUCWebViewClient != null) {
            this.mWebclient = wVUCWebViewClient;
            if (this.mWebView != null) {
                this.mWebView.setWebViewClient(this.mWebclient);
            }
        }
    }

    public void setWebchormeClient(WVUCWebChromeClient wVUCWebChromeClient) {
        if (wVUCWebChromeClient != null) {
            this.mChromeClient = wVUCWebChromeClient;
            if (this.mWebView != null) {
                this.mWebView.setWebChromeClient(this.mChromeClient);
            }
        }
    }

    public WVUCWebView getWebView() {
        if (this.mWebView == null) {
            Context activity2 = this.activity == null ? getActivity() : this.activity;
            if (activity2 == null) {
                return null;
            }
            this.mWebView = new WVUCWebView(activity2);
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
