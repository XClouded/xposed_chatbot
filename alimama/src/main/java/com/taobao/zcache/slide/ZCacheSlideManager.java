package com.taobao.zcache.slide;

import com.taobao.zcachecorewrapper.IZCacheCore;

public class ZCacheSlideManager {
    private static ZCacheSlideManager Instance;
    private IZCacheCore izCacheCore;
    private ISlide slideSubscribe;

    public static ZCacheSlideManager getInstance() {
        if (Instance == null) {
            synchronized (ZCacheSlideManager.class) {
                if (Instance == null) {
                    Instance = new ZCacheSlideManager();
                }
            }
        }
        return Instance;
    }

    public void setSlideSubscribe(ISlide iSlide) {
        this.slideSubscribe = iSlide;
    }

    public ISlide getSlideSubscribe() {
        return this.slideSubscribe;
    }
}
