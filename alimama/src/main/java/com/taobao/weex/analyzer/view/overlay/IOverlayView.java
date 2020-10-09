package com.taobao.weex.analyzer.view.overlay;

public interface IOverlayView {

    public interface ITask {
        void start();

        void stop();
    }

    public interface OnCloseListener {
        void close(IOverlayView iOverlayView);
    }

    void dismiss();

    boolean isViewAttached();

    void show();
}
