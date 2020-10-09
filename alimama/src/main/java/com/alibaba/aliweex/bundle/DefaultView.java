package com.alibaba.aliweex.bundle;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.alibaba.aliweex.bundle.WeexPageContract;

public class DefaultView {

    public static class ProgressBarView implements WeexPageContract.IProgressBar {
        private ProgressBar mProgressBar;

        public void destroy() {
        }

        public View createProgressBar(Context context) {
            ProgressBar progressBar = new ProgressBar(context);
            this.mProgressBar = progressBar;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            layoutParams.gravity = 17;
            progressBar.setLayoutParams(layoutParams);
            return progressBar;
        }

        public void showProgressBar(boolean z) {
            if (this.mProgressBar != null) {
                this.mProgressBar.setVisibility(z ? 0 : 8);
            }
        }
    }

    public static class ErrorView implements WeexPageContract.IErrorView {
        /* access modifiers changed from: private */
        public WeexPageContract.IRenderPresenter mRenderPresenter;
        private WXErrorController mWXErrorController;

        public ErrorView(WeexPageContract.IRenderPresenter iRenderPresenter) {
            this.mRenderPresenter = iRenderPresenter;
        }

        public void createErrorView(Context context, View view) {
            if (this.mWXErrorController == null && view != null) {
                this.mWXErrorController = new WXErrorController(context, view);
                this.mWXErrorController.hide();
                this.mWXErrorController.setRetryListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (ErrorView.this.mRenderPresenter != null) {
                            ErrorView.this.mRenderPresenter.reload();
                        }
                        ErrorView.this.showErrorView(false, (String) null);
                    }
                });
            }
        }

        public void showErrorView(boolean z, String str) {
            if (this.mWXErrorController == null) {
                return;
            }
            if (z) {
                this.mWXErrorController.show(str);
            } else {
                this.mWXErrorController.hide();
            }
        }

        public void destroy() {
            if (this.mWXErrorController != null) {
                this.mWXErrorController.destroy();
            }
        }
    }
}
