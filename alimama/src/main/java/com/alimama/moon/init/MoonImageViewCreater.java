package com.alimama.moon.init;

import alimama.com.unwimage.UNWPhenixImageImpl;
import alimama.com.unwimage.interfaces.IImageViewAction;
import alimama.com.unwimage.interfaces.IImageViewCreater;
import android.content.Context;
import android.util.AttributeSet;

public class MoonImageViewCreater implements IImageViewCreater {
    public IImageViewAction make(Context context, AttributeSet attributeSet, int i) {
        return new UNWPhenixImageImpl(context, attributeSet, i);
    }
}
