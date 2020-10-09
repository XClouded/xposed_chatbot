package com.alibaba.android.umbrella.export;

import androidx.annotation.NonNull;
import com.alibaba.android.umbrella.export.FetcherCore;
import com.alibaba.android.umbrella.link.UMLinkLogInterface;

public class UmbrellaServiceFetcher {
    private static final FetcherCore<UMLinkLogInterface> umCore = new FetcherCore<>(UMLinkLogInterface.class);

    private UmbrellaServiceFetcher() {
    }

    public static UMLinkLogInterface getUmbrella() {
        return umCore.get(new FetcherCore.FetchCallback<UMLinkLogInterface>() {
            @NonNull
            public UMLinkLogInterface call() {
                return new UMLinkLogFallbackImpl();
            }
        });
    }
}
