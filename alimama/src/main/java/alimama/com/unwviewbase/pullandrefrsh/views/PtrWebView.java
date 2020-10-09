package alimama.com.unwviewbase.pullandrefrsh.views;

import alimama.com.unwviewbase.R;
import alimama.com.unwviewbase.pullandrefrsh.PtrBase;
import alimama.com.unwviewbase.pullandrefrsh.PullAdapter;
import alimama.com.unwviewbase.pullandrefrsh.PullBase;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PtrWebView extends WebView implements PullAdapter {
    private final PtrBase.OnRefreshListener defaultOnRefreshListener = new PtrBase.OnRefreshListener() {
        public void onPullEndToRefresh(PtrBase ptrBase) {
        }

        public void onPullStartToRefresh(PtrBase ptrBase) {
            PtrWebView.this.reload();
        }
    };
    private final WebChromeClient defaultWebChromeClient = new WebChromeClient() {
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            if (i >= 100 && PtrWebView.this.mParent != null) {
                PtrWebView.this.mParent.refreshComplete(PtrWebView.this.getResources().getString(R.string.ptr_complete_label));
            }
        }
    };
    private final WebViewClient defaultWebViewClient = new WebViewClient() {
        public void onScaleChanged(WebView webView, float f, float f2) {
            super.onScaleChanged(webView, f, f2);
        }
    };
    private int lagThreshold;
    /* access modifiers changed from: private */
    public PtrBase mParent;

    public int getPullDirection() {
        return 0;
    }

    public PtrWebView(Context context) {
        super(context);
        init();
    }

    public PtrWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public PtrWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        setWebViewClient(this.defaultWebViewClient);
        setWebChromeClient(this.defaultWebChromeClient);
    }

    private void initPtr() {
        if (this.mParent != null) {
            this.mParent.setOnRefreshListener(this.defaultOnRefreshListener);
        }
    }

    public void setLagThreshold(int i) {
        this.lagThreshold = i;
    }

    public boolean isReadyForPullStart() {
        return getScrollY() <= this.lagThreshold;
    }

    public boolean isReadyForPullEnd() {
        return ((float) (getScrollY() + this.lagThreshold)) >= ((float) Math.floor((double) (((float) getContentHeight()) * getScale()))) - ((float) getHeight());
    }

    public void onPullAdapterAdded(PullBase pullBase) {
        if (pullBase instanceof PtrBase) {
            this.mParent = (PtrBase) pullBase;
            initPtr();
        }
    }

    public void onPullAdapterRemoved(PullBase pullBase) {
        if (pullBase instanceof PtrBase) {
            this.mParent.setOnRefreshListener((PtrBase.OnRefreshListener) null);
            this.mParent = null;
        }
    }
}
