package com.alibaba.aliweex.adapter.module.blur;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.collection.LongSparseArray;
import com.alibaba.aliweex.utils.WXModuleUtils;
import com.taobao.android.task.Coordinator;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXResourceUtils;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class WXBlurEXModule extends WXSDKEngine.DestroyableModule {
    public static final String BLUR_MODULE_NAME = "blurEx";
    private static final String DATA = "data";
    private static final String DEFAULT_OVERLAY_COLOR = "0x00FFFFFF";
    private static final int DEFAULT_SIZE = 16;
    private static final String RESULT = "result";
    private static final String RESULT_FAILED = "failure";
    private static final String RESULT_SUCCESS = "success";
    static final String TAG = "WXBlurEXModule";
    /* access modifiers changed from: private */
    @Nullable
    public FixedLongSparseArray<WeakReference<Bitmap>> mBitmapHolders = null;
    /* access modifiers changed from: private */
    public AtomicLong mIdGenerator = new AtomicLong(1);
    /* access modifiers changed from: private */
    @Nullable
    public Handler mUIHandler = new Handler(Looper.getMainLooper());

    @JSMethod
    public void createBlur(@Nullable String str, int i, @Nullable JSCallback jSCallback) {
        createBlurWithOverlay(str, i, DEFAULT_OVERLAY_COLOR, jSCallback);
    }

    @JSMethod
    public void createBlurWithOverlay(@Nullable String str, int i, @Nullable String str2, @Nullable JSCallback jSCallback) {
        if (str == null || "".equals(str.trim()) || i < 0 || jSCallback == null || this.mWXSDKInstance == null) {
            WXLogUtils.e(TAG, "illegal argument. [sourceRef:" + str + ",radius:" + i + ",callback:" + jSCallback + Operators.ARRAY_END_STR);
        } else if (str2 == null || "".equals(str2.trim())) {
            WXLogUtils.e(TAG, "illegal argument. [overlayColor:" + str2 + Operators.ARRAY_END_STR);
        } else {
            final int color = WXResourceUtils.getColor(str2, 0);
            final View findViewByRef = WXModuleUtils.findViewByRef(this.mWXSDKInstance.getInstanceId(), str);
            if (findViewByRef == null) {
                WXLogUtils.e(TAG, "view not found");
                fireCallbackEvent(RESULT_FAILED, -1, jSCallback);
                return;
            }
            final ViewBasedBlurController create = ViewBasedBlurController.create(new StackBlur(true));
            final int i2 = i;
            final JSCallback jSCallback2 = jSCallback;
            Coordinator.postTask(new Coordinator.TaggedRunnable(TAG) {
                public void run() {
                    try {
                        final Bitmap createBlurOnTargetView = create.createBlurOnTargetView(findViewByRef, color, i2);
                        if (WXBlurEXModule.this.mUIHandler != null) {
                            WXBlurEXModule.this.mUIHandler.post(new Runnable() {
                                public void run() {
                                    if (createBlurOnTargetView == null) {
                                        WXLogUtils.e(WXBlurEXModule.TAG, "blur failed");
                                        WXBlurEXModule.this.fireCallbackEvent(WXBlurEXModule.RESULT_FAILED, -1, jSCallback2);
                                        return;
                                    }
                                    if (WXBlurEXModule.this.mBitmapHolders == null) {
                                        FixedLongSparseArray unused = WXBlurEXModule.this.mBitmapHolders = new FixedLongSparseArray(16);
                                    }
                                    long andIncrement = WXBlurEXModule.this.mIdGenerator.getAndIncrement();
                                    WXBlurEXModule.this.mBitmapHolders.put(andIncrement, new WeakReference(createBlurOnTargetView));
                                    WXBlurEXModule.this.fireCallbackEvent("success", andIncrement, jSCallback2);
                                }
                            });
                        }
                    } catch (Exception unused) {
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void fireCallbackEvent(@NonNull String str, long j, @Nullable JSCallback jSCallback) {
        if (jSCallback != null) {
            HashMap hashMap = new HashMap(4);
            hashMap.put("result", str);
            if (j != -1) {
                hashMap.put("data", Long.valueOf(j));
            }
            jSCallback.invoke(hashMap);
        }
    }

    @JSMethod
    public void applyBlur(@Nullable String str, long j, @Nullable JSCallback jSCallback) {
        if (str == null || "".equals(str.trim()) || this.mWXSDKInstance == null) {
            WXLogUtils.e(TAG, "illegal argument. [sourceRef:" + str + Operators.ARRAY_END_STR);
        } else if (this.mBitmapHolders == null) {
            WXLogUtils.e(TAG, "bitmapHolders not initialized");
            fireCallbackEvent(RESULT_FAILED, -1, jSCallback);
        } else {
            WeakReference weakReference = this.mBitmapHolders.get(j);
            if (weakReference == null || weakReference.get() == null) {
                WXLogUtils.e(TAG, "bitmap not found.[id:" + j + Operators.ARRAY_END_STR);
                fireCallbackEvent(RESULT_FAILED, -1, jSCallback);
                return;
            }
            Bitmap bitmap = (Bitmap) weakReference.get();
            View findViewByRef = WXModuleUtils.findViewByRef(this.mWXSDKInstance.getInstanceId(), str);
            if (findViewByRef == null) {
                WXLogUtils.e(TAG, "view not found");
                fireCallbackEvent(RESULT_FAILED, -1, jSCallback);
            } else if (!(findViewByRef instanceof ImageView)) {
                WXLogUtils.e(TAG, "target is not an imageView");
                fireCallbackEvent(RESULT_FAILED, -1, jSCallback);
            } else {
                try {
                    ((ImageView) findViewByRef).setImageBitmap(bitmap);
                    this.mBitmapHolders.remove(j);
                    fireCallbackEvent("success", -1, jSCallback);
                } catch (Exception e) {
                    WXLogUtils.e(TAG, e.getMessage());
                    this.mBitmapHolders.remove(j);
                    fireCallbackEvent(RESULT_FAILED, -1, jSCallback);
                }
            }
        }
    }

    public void destroy() {
        if (this.mBitmapHolders != null) {
            this.mBitmapHolders.clear();
        }
        this.mBitmapHolders = null;
        if (this.mUIHandler != null) {
            this.mUIHandler.removeCallbacksAndMessages((Object) null);
        }
        this.mUIHandler = null;
    }

    @VisibleForTesting
    static class FixedLongSparseArray<E> extends LongSparseArray<E> {
        private final int mFixedSize;

        FixedLongSparseArray(int i) {
            super(i);
            this.mFixedSize = i;
        }

        public void put(long j, E e) {
            super.put(j, e);
            trimToSizeIfNeeded();
        }

        public void append(long j, E e) {
            super.append(j, e);
            trimToSizeIfNeeded();
        }

        private void trimToSizeIfNeeded() {
            int size = size();
            int i = size - this.mFixedSize;
            if (i > 0) {
                long[] jArr = new long[i];
                int i2 = 0;
                for (int i3 = 0; i3 < size && i2 < i; i3++) {
                    jArr[i2] = keyAt(i3);
                    i2++;
                }
                for (int i4 = 0; i4 < i; i4++) {
                    remove(jArr[i4]);
                }
            }
        }
    }
}
