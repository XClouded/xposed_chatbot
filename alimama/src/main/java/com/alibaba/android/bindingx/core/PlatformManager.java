package com.alibaba.android.bindingx.core;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Map;

public class PlatformManager {
    /* access modifiers changed from: private */
    public IDeviceResolutionTranslator mResolutionTranslator;
    /* access modifiers changed from: private */
    public IScrollFactory mScrollFactory;
    /* access modifiers changed from: private */
    public IViewFinder mViewFinder;
    /* access modifiers changed from: private */
    public IViewUpdater mViewUpdater;

    public interface IDeviceResolutionTranslator {
        double nativeToWeb(double d, Object... objArr);

        double webToNative(double d, Object... objArr);
    }

    public interface IScrollFactory {
        void addScrollListenerWith(@NonNull String str, @NonNull ScrollListener scrollListener);

        void removeScrollListenerWith(@NonNull String str, @NonNull ScrollListener scrollListener);
    }

    public interface IViewFinder {
        @Nullable
        View findViewBy(String str, Object... objArr);
    }

    public interface IViewUpdater {
        void synchronouslyUpdateViewOnUIThread(@NonNull View view, @NonNull String str, @NonNull Object obj, @NonNull IDeviceResolutionTranslator iDeviceResolutionTranslator, @NonNull Map<String, Object> map, Object... objArr);
    }

    public interface ScrollListener {
        void onScrollEnd(float f, float f2);

        void onScrollStart();

        void onScrolled(float f, float f2);
    }

    private PlatformManager() {
    }

    @NonNull
    public IDeviceResolutionTranslator getResolutionTranslator() {
        return this.mResolutionTranslator;
    }

    @NonNull
    public IViewFinder getViewFinder() {
        return this.mViewFinder;
    }

    @NonNull
    public IViewUpdater getViewUpdater() {
        return this.mViewUpdater;
    }

    @Nullable
    public IScrollFactory getScrollFactory() {
        return this.mScrollFactory;
    }

    public static class Builder {
        private IDeviceResolutionTranslator deviceResolutionTranslator;
        private IScrollFactory scrollFactory;
        private IViewFinder viewFinder;
        private IViewUpdater viewUpdater;

        public PlatformManager build() {
            PlatformManager platformManager = new PlatformManager();
            IViewFinder unused = platformManager.mViewFinder = this.viewFinder;
            IDeviceResolutionTranslator unused2 = platformManager.mResolutionTranslator = this.deviceResolutionTranslator;
            IViewUpdater unused3 = platformManager.mViewUpdater = this.viewUpdater;
            IScrollFactory unused4 = platformManager.mScrollFactory = this.scrollFactory;
            return platformManager;
        }

        public Builder withDeviceResolutionTranslator(@NonNull IDeviceResolutionTranslator iDeviceResolutionTranslator) {
            this.deviceResolutionTranslator = iDeviceResolutionTranslator;
            return this;
        }

        public Builder withViewFinder(@NonNull IViewFinder iViewFinder) {
            this.viewFinder = iViewFinder;
            return this;
        }

        public Builder withViewUpdater(@NonNull IViewUpdater iViewUpdater) {
            this.viewUpdater = iViewUpdater;
            return this;
        }

        public Builder withScrollFactory(@Nullable IScrollFactory iScrollFactory) {
            this.scrollFactory = iScrollFactory;
            return this;
        }
    }
}
