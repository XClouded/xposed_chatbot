package alimama.com.unwimage.interfaces;

import alimama.com.unwbase.callback.ImageDownloadFail;
import alimama.com.unwbase.callback.ImageDownloadSuccess;
import android.widget.ImageView;

public interface IImageLoader {
    void fetch(String str);

    void fetch(String str, ImageDownloadSuccess imageDownloadSuccess, ImageDownloadFail imageDownloadFail);

    void load(String str, ImageView imageView);
}
