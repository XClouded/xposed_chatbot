package com.alimama.moon.qrcode;

import android.graphics.Bitmap;

public interface IQRCodeGenerator {
    Bitmap genQRCodeBitmap(String str) throws Exception;

    Bitmap genQRCodeBitmap(String str, int i, int i2) throws Exception;

    String genQRCodeImageUrl(String str) throws Exception;
}
