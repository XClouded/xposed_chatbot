package com.taobao.xcode.szxing.qrcode;

import android.app.Activity;
import android.graphics.BitmapFactory;
import androidx.core.internal.view.SupportMenu;
import java.io.InputStream;
import java.io.PrintStream;

public class Demo extends Activity {
    public static void main(String[] strArr) throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        testCreateNormalQrcode();
        PrintStream printStream = System.out;
        printStream.println("nomarl:" + (System.currentTimeMillis() - currentTimeMillis));
        PrintStream printStream2 = System.out;
        printStream2.println("color:" + (System.currentTimeMillis() - currentTimeMillis));
    }

    public static void testCreateNormalQrcode() throws Exception {
        QRCodeWriter.encode2Bitmap("HTTP://M.TAOBAO.COM", 400);
    }

    public static void testCreateQrcodeWithColor() throws Exception {
        Option option = new Option();
        option.setForegroundColor(Integer.valueOf(SupportMenu.CATEGORY_MASK));
        QRCodeWriter.encode2Bitmap("http://ma.taobao.com", 380, 380, option);
    }

    public void testCreateQrcodeWithLogo() throws Exception {
        Option option = new Option();
        option.setForegroundColor(Integer.valueOf(SupportMenu.CATEGORY_MASK));
        InputStream open = getResources().getAssets().open("ic_launcher-web.png");
        byte[] bArr = new byte[open.available()];
        open.read(bArr);
        open.close();
        option.setLogo(BitmapFactory.decodeByteArray(bArr, 0, bArr.length));
        QRCodeWriter.encode2Bitmap("http://ma.taobao.com", 380, 380, option);
    }
}
