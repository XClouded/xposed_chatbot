package com.taobao.phenix.strategy;

public class ModuleStrategy {
    public final int diskCachePriority;
    public final int memoryCachePriority;
    public final String name;
    public final boolean preloadWithSmall;
    public final boolean scaleFromLarge;
    public final int schedulePriority;

    public ModuleStrategy(String str, int i, int i2, int i3, boolean z, boolean z2) {
        this.name = str;
        this.schedulePriority = i;
        this.memoryCachePriority = i2;
        this.diskCachePriority = i3;
        this.preloadWithSmall = z;
        this.scaleFromLarge = z2;
    }
}
