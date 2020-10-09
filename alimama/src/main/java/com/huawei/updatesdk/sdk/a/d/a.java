package com.huawei.updatesdk.sdk.a.d;

import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.tao.image.Logger;
import kotlin.UByte;

public class a {
    private static char[] a = {'A', 'B', 'C', Logger.LEVEL_D, Logger.LEVEL_E, 'F', 'G', 'H', Logger.LEVEL_I, 'J', 'K', Logger.LEVEL_L, 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', Logger.LEVEL_V, Logger.LEVEL_W, 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', DXTemplateNamePathUtil.DIR, '='};

    public static String a(byte[] bArr) {
        return a(bArr, bArr.length);
    }

    public static String a(byte[] bArr, int i) {
        boolean z;
        char[] cArr = new char[(((i + 2) / 3) * 4)];
        int i2 = 0;
        int i3 = 0;
        while (i2 < i) {
            int i4 = (bArr[i2] & UByte.MAX_VALUE) << 8;
            int i5 = i2 + 1;
            boolean z2 = true;
            if (i5 < i) {
                i4 |= bArr[i5] & UByte.MAX_VALUE;
                z = true;
            } else {
                z = false;
            }
            int i6 = i4 << 8;
            int i7 = i2 + 2;
            if (i7 < i) {
                i6 |= bArr[i7] & UByte.MAX_VALUE;
            } else {
                z2 = false;
            }
            int i8 = 64;
            cArr[i3 + 3] = a[z2 ? i6 & 63 : 64];
            int i9 = i6 >> 6;
            int i10 = i3 + 2;
            char[] cArr2 = a;
            if (z) {
                i8 = i9 & 63;
            }
            cArr[i10] = cArr2[i8];
            int i11 = i9 >> 6;
            cArr[i3 + 1] = a[i11 & 63];
            cArr[i3 + 0] = a[(i11 >> 6) & 63];
            i2 += 3;
            i3 += 4;
        }
        return new String(cArr);
    }
}
