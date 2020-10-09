package com.taobao.monitor.impl.trace;

import com.taobao.monitor.impl.trace.AbsDispatcher;

public class ImageStageDispatcher extends AbsDispatcher<IImageStageListener> {

    public interface IImageStageListener {
        void onImageStage(int i);
    }

    public void dispatchImageStage(final int i) {
        foreach(new AbsDispatcher.ListenerCaller<IImageStageListener>() {
            public void callListener(IImageStageListener iImageStageListener) {
                iImageStageListener.onImageStage(i);
            }
        });
    }
}
