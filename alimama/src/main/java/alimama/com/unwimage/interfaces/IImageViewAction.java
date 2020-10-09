package alimama.com.unwimage.interfaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.annotation.Nullable;

public interface IImageViewAction {
    void enableImageLoadOnFling(boolean z);

    Object getAction();

    void make(Context context, AttributeSet attributeSet, int i);

    void setAnyImageAnimUrl(String str);

    void setAnyImageRes(int i);

    void setAnyImageURI(Uri uri);

    void setAnyImageURI(Uri uri, @Nullable Object obj);

    void setAnyImageUrl(String str);

    void setBitmap(Bitmap bitmap);

    void setBlur(Context context, int i);

    void setDownLoadListener(DownloadLisenter downloadLisenter);

    void setDrawable(Drawable drawable);

    void setErrorPlaceHolder(int i);

    void setForegroundPlaceHolder(Drawable drawable);

    void setImageScaleType(ImageView.ScaleType scaleType);

    void setImgAlpha(int i);

    void setPlaceHolder(int i);

    void setRes(int i);

    void setRoundedCorners(float f);

    void setRoundedCorners(float f, float f2, float f3, float f4);

    void setSkipAuto(boolean z);
}
