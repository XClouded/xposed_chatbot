package com.taobao.zcache.intelligent;

public class ZIntelligentManger {
    private static ZIntelligentManger instance;
    private IIntelligent intelligent;

    public static ZIntelligentManger getInstance() {
        if (instance == null) {
            synchronized (ZIntelligentManger.class) {
                if (instance == null) {
                    instance = new ZIntelligentManger();
                }
            }
        }
        return instance;
    }

    public void setIntelligentImpl(IIntelligent iIntelligent) {
        this.intelligent = iIntelligent;
    }

    public IIntelligent getIntelligentImpl() {
        return this.intelligent;
    }
}
