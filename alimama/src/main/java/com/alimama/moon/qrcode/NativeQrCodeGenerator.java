package com.alimama.moon.qrcode;

import android.graphics.Bitmap;
import com.taobao.xcode.szxing.qrcode.Option;
import com.taobao.xcode.szxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NativeQrCodeGenerator implements IQRCodeGenerator {
    private static volatile NativeQrCodeGenerator instance;
    private Logger log = LoggerFactory.getLogger((Class<?>) NativeQrCodeGenerator.class);

    private NativeQrCodeGenerator() {
    }

    public static final NativeQrCodeGenerator getInstance() {
        if (instance == null) {
            synchronized (NativeQrCodeGenerator.class) {
                if (instance == null) {
                    instance = new NativeQrCodeGenerator();
                }
            }
        }
        return instance;
    }

    public String genQRCodeImageUrl(String str) throws Exception {
        throw new Exception("not implemented");
    }

    public Bitmap genQRCodeBitmap(String str) throws Exception {
        return genQRCodeBitmap(str, 280, 280);
    }

    public Bitmap genQRCodeBitmap(String str, int i, int i2) throws Exception {
        this.log.info("genQRCodeBitmap, contentUrl: {}, width: {}, height: {}", str, Integer.valueOf(i), Integer.valueOf(i2));
        return QRCodeWriter.encode2Bitmap(str, i, i2, new Option());
    }
}
