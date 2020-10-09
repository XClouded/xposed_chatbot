package com.alimama.moon.web;

import android.graphics.Bitmap;
import android.text.TextUtils;
import com.alimama.moon.web.IWebContract;
import com.uc.webview.export.WebView;

public class WebPresenter implements IWebContract.IWebPresenter {
    private final IWebContract.IWebView view;

    public void onProgressChanged(WebView webView, int i) {
    }

    public void onReceivedError(WebView webView, int i, String str, String str2) {
    }

    public void start() {
    }

    public WebPresenter(IWebContract.IWebView iWebView) {
        this.view = iWebView;
        this.view.setPresenter(this);
    }

    public void clickBackBtn() {
        this.view.showPreviousWebPage();
    }

    public void clickCloseBtn() {
        this.view.closeWebPage();
    }

    public void onPageFinished(Boolean bool, String str) {
        if (bool.booleanValue()) {
            this.view.showCloseBtn();
        } else {
            this.view.hideCloseBtn();
        }
        this.view.onPageLoadFinished();
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        this.view.clearToolbarMenu();
    }

    public void openLoginUI() {
        this.view.showLoginUI();
    }

    public void openFeedBack() {
        this.view.showFeedBackUI();
    }

    public void reloadWebPage() {
        this.view.showOriginalWebPage();
    }

    public void openShareUI(String str) {
        this.view.showShareUI(str);
    }

    public void openNewWebView(String str) {
        this.view.showNewWebView(str);
    }

    public void openTaokeDetailUI(String str) {
        if (!TextUtils.isEmpty(str)) {
            openNewWebView(WebPageIntentGenerator.getItemLandingPage(str));
        }
    }

    public void receivePageTitle(String str) {
        this.view.changeTitle(str, WebActivity.TYPE_WEB);
    }

    public void refreshCurrentWebPage() {
        this.view.refreshCurrentWebPage();
    }
}
