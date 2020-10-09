package com.taobao.taolive.weexext.drawboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXUtils;
import java.io.FileInputStream;

public class TBLiveImageWeexComponent extends WXComponent {
    private static final String IMAGE_FILL = "scaleType";
    private static final String IMAGE_SRC = "src";
    public static final String NAME = "liveimage";
    private ImageView mImageView;
    private FrameLayout mRootView;

    public TBLiveImageWeexComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    public TBLiveImageWeexComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, int i, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, i, basicComponentData);
    }

    /* access modifiers changed from: protected */
    public FrameLayout initComponentHostView(@NonNull Context context) {
        this.mRootView = new FrameLayout(context);
        this.mImageView = new ImageView(context);
        this.mImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.mRootView.addView(this.mImageView);
        return this.mRootView;
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        if ("src".equals(str)) {
            setImageSrc(WXUtils.getString(obj, ""));
        }
        if (IMAGE_FILL.equals(str) && this.mImageView != null) {
            this.mImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        return super.setProperty(str, obj);
    }

    public void setImageSrc(String str) {
        if (this.mImageView != null) {
            this.mImageView.setImageBitmap(getLocalBitmap(str));
        }
    }

    public void destroy() {
        super.destroy();
        if (this.mRootView != null) {
            this.mRootView.removeAllViews();
        }
        if (this.mImageView != null) {
            this.mImageView.setImageBitmap((Bitmap) null);
        }
        this.mImageView = null;
        this.mRootView = null;
    }

    private static Bitmap getLocalBitmap(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            Bitmap decodeStream = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
            return decodeStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
