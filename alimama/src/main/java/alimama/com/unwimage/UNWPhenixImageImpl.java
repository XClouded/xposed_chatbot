package alimama.com.unwimage;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwimage.interfaces.DownloadLisenter;
import alimama.com.unwimage.interfaces.IImageViewAction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.taobao.phenix.compat.effects.BlurBitmapProcessor;
import com.taobao.phenix.compat.effects.RoundedCornersBitmapProcessor;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import com.taobao.phenix.request.SchemeInfo;
import com.taobao.uikit.extend.feature.features.PhenixOptions;
import com.taobao.uikit.extend.feature.view.TUrlImageView;
import com.taobao.vessel.utils.Utils;

public class UNWPhenixImageImpl extends TUrlImageView implements IImageViewAction {
    /* access modifiers changed from: private */
    public DownloadLisenter lisenter;

    public Object getAction() {
        return null;
    }

    public UNWPhenixImageImpl(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        make(context, attributeSet, i);
    }

    public UNWPhenixImageImpl(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public UNWPhenixImageImpl(Context context) {
        super(context);
    }

    public void make(Context context, AttributeSet attributeSet, int i) {
        setAutoRelease(false);
        succListener(new IPhenixListener<SuccPhenixEvent>() {
            public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
                UNWManager.getInstance().getLogger().success("image", "load");
                if (succPhenixEvent == null || UNWPhenixImageImpl.this.lisenter == null) {
                    return true;
                }
                UNWPhenixImageImpl.this.lisenter.onSuccess(succPhenixEvent.getUrl());
                return true;
            }
        });
        failListener(new IPhenixListener<FailPhenixEvent>() {
            public boolean onHappen(FailPhenixEvent failPhenixEvent) {
                String str = "";
                String str2 = "-9001";
                String str3 = "";
                if (failPhenixEvent != null) {
                    str = failPhenixEvent.getHttpMessage();
                    str2 = failPhenixEvent.getHttpCode() + "";
                    str3 = failPhenixEvent.getUrl();
                    if (UNWPhenixImageImpl.this.lisenter != null) {
                        UNWPhenixImageImpl.this.lisenter.onFail(str3, str, str2);
                    }
                }
                UNWManager.getInstance().getLogger().error("image", "load", str3 + ":" + str + "-" + str2, str2);
                return true;
            }
        });
    }

    public void setAnyImageURI(Uri uri) {
        if (uri != null) {
            setImageUrl(SchemeInfo.wrapFile(uri.getPath()));
        }
    }

    public void setAnyImageUrl(String str) {
        if (!TextUtils.isEmpty(str) && str.startsWith("//")) {
            str = Utils.HTTPS_SCHEMA + str;
        }
        setImageUrl(str);
    }

    public void setAnyImageAnimUrl(String str) {
        setAnyImageUrl(str);
    }

    public void setAnyImageURI(Uri uri, @Nullable Object obj) {
        if (uri != null) {
            setAnyImageUrl(uri.toString());
        }
    }

    public void setRoundedCorners(float f) {
        setPhenixOptions(new PhenixOptions().bitmapProcessors(new RoundedCornersBitmapProcessor(Math.round(f), 0)));
    }

    public void setAnyImageRes(int i) {
        setImageUrl(SchemeInfo.wrapRes(i));
    }

    public void setImageScaleType(ImageView.ScaleType scaleType) {
        setScaleType(scaleType);
    }

    public void setBlur(Context context, int i) {
        new PhenixOptions().bitmapProcessors(new BlurBitmapProcessor(context, 25));
    }

    public void enableImageLoadOnFling(boolean z) {
        enableLoadOnFling(z);
    }

    public void setRoundedCorners(float f, float f2, float f3, float f4) {
        if (f > 0.0f && f2 > 0.0f) {
            setPhenixOptions(new PhenixOptions().bitmapProcessors(new RoundedCornersBitmapProcessor(0, 0, Math.round(f), 0, RoundedCornersBitmapProcessor.CornerType.TOP)));
        } else if (f > 0.0f && f4 > 0.0f) {
            setPhenixOptions(new PhenixOptions().bitmapProcessors(new RoundedCornersBitmapProcessor(0, 0, Math.round(f), 0, RoundedCornersBitmapProcessor.CornerType.LEFT)));
        } else if (f2 > 0.0f && f3 > 0.0f) {
            setPhenixOptions(new PhenixOptions().bitmapProcessors(new RoundedCornersBitmapProcessor(0, 0, Math.round(f), 0, RoundedCornersBitmapProcessor.CornerType.RIGHT)));
        } else if (f4 > 0.0f && f3 > 0.0f) {
            setPhenixOptions(new PhenixOptions().bitmapProcessors(new RoundedCornersBitmapProcessor(0, 0, Math.round(f), 0, RoundedCornersBitmapProcessor.CornerType.BOTTOM)));
        }
    }

    public void setSkipAuto(boolean z) {
        setSkipAutoSize(z);
    }

    public void setDownLoadListener(DownloadLisenter downloadLisenter) {
        this.lisenter = downloadLisenter;
    }

    public void setForegroundPlaceHolder(Drawable drawable) {
        setPlaceHoldForeground(drawable);
    }

    public void setPlaceHolder(int i) {
        setPlaceHoldImageResId(i);
    }

    public void setErrorPlaceHolder(int i) {
        setErrorImageResId(i);
    }

    public void setRes(int i) {
        setAnyImageRes(i);
    }

    public void setBitmap(Bitmap bitmap) {
        setImageBitmap(bitmap);
    }

    public void setImgAlpha(int i) {
        setAlpha(i);
    }

    public void setDrawable(Drawable drawable) {
        setImageDrawable(drawable);
    }
}
