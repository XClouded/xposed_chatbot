package alimama.com.unwimage;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwimage.interfaces.DownloadLisenter;
import alimama.com.unwimage.interfaces.IImageViewAction;
import alimama.com.unwimage.interfaces.IImageViewCreater;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.Nullable;

public class UNWImageView extends FrameLayout implements IImageViewAction {
    private IImageViewAction action;

    public void make(Context context, AttributeSet attributeSet, int i) {
    }

    public UNWImageView(Context context) {
        this(context, (AttributeSet) null);
    }

    public UNWImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public UNWImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        IImageViewCreater iImageViewCreater = (IImageViewCreater) UNWManager.getInstance().getService(IImageViewCreater.class);
        if (iImageViewCreater != null) {
            this.action = iImageViewCreater.make(context, attributeSet, i);
            int[] iArr = {16842960, 16842964, 16842996, 16842997};
            addView((View) this.action, new FrameLayout.LayoutParams(-1, -1));
        }
    }

    public void setWrapContent(boolean z) {
        removeAllViews();
        if (z) {
            addView((View) this.action, new FrameLayout.LayoutParams(-2, -2));
        } else {
            addView((View) this.action, new FrameLayout.LayoutParams(-1, -1));
        }
    }

    public void setAnyImageURI(Uri uri) {
        if (this.action != null) {
            this.action.setAnyImageURI(uri);
        }
    }

    public void setAnyImageUrl(String str) {
        if (this.action != null) {
            this.action.setAnyImageUrl(str);
        }
    }

    public void setAnyImageAnimUrl(String str) {
        if (this.action != null) {
            this.action.setAnyImageAnimUrl(str);
        }
    }

    public void setAnyImageURI(Uri uri, @Nullable Object obj) {
        if (this.action != null) {
            this.action.setAnyImageURI(uri, obj);
        }
    }

    public void setRoundedCorners(float f) {
        if (this.action != null) {
            this.action.setRoundedCorners(f);
        }
    }

    public void setAnyImageRes(int i) {
        if (this.action != null) {
            this.action.setAnyImageRes(i);
        }
    }

    public void setImageScaleType(ImageView.ScaleType scaleType) {
        if (this.action != null) {
            this.action.setImageScaleType(scaleType);
        }
    }

    public void setBlur(Context context, int i) {
        if (this.action != null) {
            this.action.setBlur(context, i);
        }
    }

    public void enableImageLoadOnFling(boolean z) {
        if (this.action != null) {
            this.action.enableImageLoadOnFling(z);
        }
    }

    public void setRoundedCorners(float f, float f2, float f3, float f4) {
        if (this.action != null) {
            this.action.setRoundedCorners(f, f2, f3, f4);
        }
    }

    public void setSkipAuto(boolean z) {
        this.action.setSkipAuto(z);
    }

    public void setDownLoadListener(DownloadLisenter downloadLisenter) {
        this.action.setDownLoadListener(downloadLisenter);
    }

    public void setPlaceHolder(int i) {
        this.action.setPlaceHolder(i);
    }

    public void setForegroundPlaceHolder(Drawable drawable) {
        this.action.setForegroundPlaceHolder(drawable);
    }

    public void setErrorPlaceHolder(int i) {
        this.action.setErrorPlaceHolder(i);
    }

    public void setRes(int i) {
        this.action.setRes(i);
    }

    public void setBitmap(Bitmap bitmap) {
        this.action.setBitmap(bitmap);
    }

    public void setImgAlpha(int i) {
        this.action.setImgAlpha(i);
    }

    public void setDrawable(Drawable drawable) {
        this.action.setDrawable(drawable);
    }

    public Object getAction() {
        return this.action;
    }
}
