package alimama.com.unwimage.interfaces;

import android.content.Context;
import android.util.AttributeSet;

public interface IImageViewCreater {
    IImageViewAction make(Context context, AttributeSet attributeSet, int i);
}
