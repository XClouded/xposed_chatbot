package com.taobao.phenix.cache.memory;

public interface ReleasableReferenceListener {
    void onReferenceDowngrade2Passable(ReleasableBitmapDrawable releasableBitmapDrawable);

    void onReferenceReleased(ReleasableBitmapDrawable releasableBitmapDrawable);
}
