package com.alimama.moon.web;

import android.graphics.Bitmap;
import com.alimama.moon.IPresenter;
import com.alimama.moon.IView;
import com.uc.webview.export.WebView;

public interface IWebContract {

    public interface IWebPresenter extends IPresenter {
        void clickBackBtn();

        void clickCloseBtn();

        void onPageFinished(Boolean bool, String str);

        void onPageStarted(WebView webView, String str, Bitmap bitmap);

        void onProgressChanged(WebView webView, int i);

        void onReceivedError(WebView webView, int i, String str, String str2);

        void openFeedBack();

        void openLoginUI();

        void openNewWebView(String str);

        void openShareUI(String str);

        void openTaokeDetailUI(String str);

        void receivePageTitle(String str);

        void refreshCurrentWebPage();

        void reloadWebPage();
    }

    public interface IWebView extends IView<IWebPresenter> {
        void changeTitle(String str, String str2);

        void clearToolbarMenu();

        void closeLoadingView();

        void closeWebPage();

        void hideCloseBtn();

        void onPageLoadFinished();

        void refreshCurrentWebPage();

        void showCloseBtn();

        void showFeedBackUI();

        void showLoginUI();

        void showNewWebView(String str);

        void showOriginalWebPage();

        void showPreviousWebPage();

        void showShareUI(String str);

        void showloadingView();
    }
}
