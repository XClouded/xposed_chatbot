package com.taobao.phenix.request;

import androidx.annotation.NonNull;
import com.alibaba.wireless.security.SecExceptionCode;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.loader.LocalSchemeHandler;
import java.util.List;

public class SchemeInfo {
    private static final String BASE64_SCHEME_PREFIX = "data:image/";
    private static final int BASE64_SCHEME_PREFIX_LENGTH = 11;
    private static final String BASE64_SCHEME_SUFFIX = ";base64,";
    private static final int BASE64_SCHEME_SUFFIX_LENGTH = 8;
    private static final String LOCAL_ASSET_SCHEME = "asset://";
    private static final int LOCAL_ASSET_SCHEME_LENGTH = 8;
    private static final String LOCAL_FILE_SCHEME = "file://";
    private static final int LOCAL_FILE_SCHEME_LENGTH = 7;
    private static final String LOCAL_RES_SCHEME = "res://";
    private static final int LOCAL_RES_SCHEME_LENGTH = 6;
    public static final int LOCAL_URI = 32;
    private static final String LOCAL_URI_SCHEME = "content://";
    public static final int NETWORK_URI = 1;
    private static final String TARGET_CDN_DOMAIN_PREFIX = "//gw.alicdn.com";
    private static final int TARGET_CDN_MAX_SIDE = 10000;
    private static final String TARGET_CDN_WEBP_SUFFIX = "_.webp";
    private static final char X_CONCAT_CHAR = 'x';
    public String base64;
    public String baseName;
    public String extension = "";
    public int handleIndex;
    public int height;
    public boolean isCdnSize;
    public String path;
    public int quality;
    public int resId;
    public int thumbnailType;
    public final int type;
    public boolean useOriginIfThumbNotExist;
    public int width;

    public SchemeInfo(int i) {
        this.type = i;
    }

    public String toString() {
        return "type=" + this.type + ", baseName=" + this.baseName + ", extension=" + this.extension + ", width=" + this.width + ", height=" + this.height + ", cdnSize=" + this.isCdnSize + ", path=" + this.path + ", resId=" + this.resId + ", base64=" + this.base64;
    }

    public static String wrapRes(int i) {
        return LOCAL_RES_SCHEME + i;
    }

    public static String wrapFile(String str) {
        return LOCAL_FILE_SCHEME + str;
    }

    public static String wrapAsset(String str) {
        return LOCAL_ASSET_SCHEME + str;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r0 = parseAssetFile(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000c, code lost:
        r0 = parseResFile(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0012, code lost:
        r0 = parseBase64(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0018, code lost:
        r0 = parseLocalExtended(r1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.taobao.phenix.request.SchemeInfo parse(@androidx.annotation.NonNull java.lang.String r1) {
        /*
            com.taobao.phenix.request.SchemeInfo r0 = parseLocalFile(r1)
            if (r0 != 0) goto L_0x0024
            com.taobao.phenix.request.SchemeInfo r0 = parseAssetFile(r1)
            if (r0 != 0) goto L_0x0024
            com.taobao.phenix.request.SchemeInfo r0 = parseResFile(r1)
            if (r0 != 0) goto L_0x0024
            com.taobao.phenix.request.SchemeInfo r0 = parseBase64(r1)
            if (r0 != 0) goto L_0x0024
            com.taobao.phenix.request.SchemeInfo r0 = parseLocalExtended(r1)
            if (r0 == 0) goto L_0x001f
            goto L_0x0024
        L_0x001f:
            com.taobao.phenix.request.SchemeInfo r1 = parseHttpUrl(r1)
            return r1
        L_0x0024:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.phenix.request.SchemeInfo.parse(java.lang.String):com.taobao.phenix.request.SchemeInfo");
    }

    public boolean isLocalUri() {
        return (this.type & 32) > 0;
    }

    public boolean isNetworkUri() {
        return (this.type & 32) == 0;
    }

    private static SchemeInfo parseLocalExtended(@NonNull String str) {
        List<LocalSchemeHandler> extendedSchemeHandlers = Phenix.instance().getExtendedSchemeHandlers();
        if (extendedSchemeHandlers == null) {
            return null;
        }
        int i = 0;
        for (LocalSchemeHandler isSupported : extendedSchemeHandlers) {
            if (isSupported.isSupported(str)) {
                SchemeInfo schemeInfo = new SchemeInfo(48);
                schemeInfo.handleIndex = i;
                schemeInfo.baseName = str;
                return schemeInfo;
            }
            i++;
        }
        return null;
    }

    private static SchemeInfo parseLocalFile(@NonNull String str) {
        boolean startsWith = str.startsWith("file:///");
        if (startsWith || (str.length() > 1 && str.charAt(0) == '/' && str.charAt(1) != '/')) {
            SchemeInfo schemeInfo = new SchemeInfo(33);
            schemeInfo.extension = parseExtend(str);
            if (startsWith) {
                schemeInfo.path = str.substring(7);
            } else {
                schemeInfo.path = str;
            }
            schemeInfo.baseName = schemeInfo.path;
            return schemeInfo;
        } else if (!str.startsWith(LOCAL_URI_SCHEME)) {
            return null;
        } else {
            SchemeInfo schemeInfo2 = new SchemeInfo(33);
            schemeInfo2.path = str;
            schemeInfo2.baseName = str;
            return schemeInfo2;
        }
    }

    private static SchemeInfo parseAssetFile(@NonNull String str) {
        if (!str.startsWith(LOCAL_ASSET_SCHEME)) {
            return null;
        }
        SchemeInfo schemeInfo = new SchemeInfo(34);
        schemeInfo.baseName = str;
        schemeInfo.path = str.substring(8);
        schemeInfo.extension = parseExtend(str);
        return schemeInfo;
    }

    private static SchemeInfo parseResFile(@NonNull String str) {
        if (!str.startsWith(LOCAL_RES_SCHEME)) {
            return null;
        }
        try {
            int parseInt = Integer.parseInt(str.substring(6));
            SchemeInfo schemeInfo = new SchemeInfo(36);
            schemeInfo.resId = parseInt;
            schemeInfo.baseName = str;
            return schemeInfo;
        } catch (Exception unused) {
            return null;
        }
    }

    private static SchemeInfo parseBase64(@NonNull String str) {
        int indexOf;
        if (!str.startsWith(BASE64_SCHEME_PREFIX) || (indexOf = str.indexOf(BASE64_SCHEME_SUFFIX, 11)) <= 0 || indexOf >= 17) {
            return null;
        }
        SchemeInfo schemeInfo = new SchemeInfo(40);
        int i = indexOf + 8;
        schemeInfo.base64 = str.substring(i);
        schemeInfo.baseName = str.substring(0, i) + "hash=" + Integer.toHexString(schemeInfo.base64.hashCode());
        StringBuilder sb = new StringBuilder();
        sb.append('.');
        sb.append(str.substring(11, indexOf));
        schemeInfo.extension = sb.toString();
        return schemeInfo;
    }

    private static String parseExtend(@NonNull String str) {
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf > 0 ? str.substring(lastIndexOf) : "";
    }

    private static SchemeInfo parseHttpUrl(@NonNull String str) {
        int indexOf;
        SchemeInfo schemeInfo = new SchemeInfo(1);
        schemeInfo.baseName = str;
        int length = str.length();
        int indexOf2 = str.indexOf(63);
        if (indexOf2 < 0) {
            indexOf2 = length;
        }
        int lastIndexOf = str.lastIndexOf(47, indexOf2 - 1);
        if (lastIndexOf < 0 || lastIndexOf >= length - 1) {
            return schemeInfo;
        }
        String substring = str.substring(lastIndexOf + 1, indexOf2);
        schemeInfo.extension = parseExtend(substring);
        if (!parseTfsRule(substring, schemeInfo) && !parseOssRule(str, lastIndexOf, substring, schemeInfo) && (indexOf = str.indexOf(TARGET_CDN_DOMAIN_PREFIX)) >= 0 && indexOf <= 6) {
            if (substring.endsWith(TARGET_CDN_WEBP_SUFFIX)) {
                schemeInfo.baseName = substring.substring(0, substring.length() - TARGET_CDN_WEBP_SUFFIX.length());
            } else {
                schemeInfo.baseName = substring;
            }
            schemeInfo.height = 10000;
            schemeInfo.width = 10000;
            schemeInfo.isCdnSize = true;
        }
        return schemeInfo;
    }

    private static boolean parseTfsRule(String str, SchemeInfo schemeInfo) {
        int indexOf = str.indexOf(95);
        if (indexOf < 0) {
            return false;
        }
        int indexOf2 = str.indexOf(120, indexOf);
        int length = str.length();
        while (indexOf2 > indexOf) {
            schemeInfo.width = traverseValue(str, indexOf2, false, indexOf);
            schemeInfo.height = traverseValue(str, indexOf2, true, length);
            if (schemeInfo.width > 0 && schemeInfo.width == schemeInfo.height) {
                int length2 = String.valueOf(schemeInfo.height).length();
                int i = indexOf2 + length2;
                int i2 = i + 2;
                if (!(i2 < str.length() && str.charAt(i + 1) == 'x' && str.charAt(i2) == 'z')) {
                    schemeInfo.isCdnSize = true;
                    int i3 = (indexOf2 - length2) - 1;
                    if (i3 > 0) {
                        schemeInfo.baseName = str.substring(0, i3);
                    }
                }
                return true;
            } else if ((schemeInfo.width > 0 && schemeInfo.height == 10000) || (schemeInfo.height > 0 && schemeInfo.width == 10000)) {
                return true;
            } else {
                schemeInfo.height = 0;
                schemeInfo.width = 0;
                indexOf = indexOf2 + 1;
                indexOf2 = str.indexOf(120, indexOf);
            }
        }
        return false;
    }

    private static boolean parseOssRule(String str, int i, String str2, SchemeInfo schemeInfo) {
        int lastIndexOf = str2.lastIndexOf(64);
        int i2 = 0;
        if (lastIndexOf < 0 || str2.indexOf(45, lastIndexOf) > 0 || str2.indexOf(SecExceptionCode.SEC_ERROR_INIT_LOW_VERSION_DATA, lastIndexOf) > 0 || str2.indexOf("_2e", lastIndexOf) > 0) {
            return false;
        }
        int traverseOssSize = traverseOssSize(str2, lastIndexOf, 'w');
        schemeInfo.width = traverseOssSize;
        if (traverseOssSize != 0) {
            int traverseOssSize2 = traverseOssSize(str2, lastIndexOf, 'h');
            schemeInfo.height = traverseOssSize2;
            if (traverseOssSize2 != 0 && schemeInfo.width == schemeInfo.height) {
                schemeInfo.isCdnSize = true;
                int indexOf = str.indexOf("//");
                if (indexOf > 0 && str.charAt(indexOf - 1) == ':') {
                    i2 = indexOf;
                }
                schemeInfo.baseName = str.substring(i2, i + lastIndexOf + 1);
                return true;
            }
        }
        return false;
    }

    private static int traverseOssSize(String str, int i, char c) {
        int traverseValue;
        int indexOf = str.indexOf(c, i);
        while (indexOf > i) {
            if (isMatchOssRule(str, indexOf) && (traverseValue = traverseValue(str, indexOf, false, i)) != 0) {
                return traverseValue;
            }
            i = indexOf + 1;
            indexOf = str.indexOf(c, i);
        }
        return 0;
    }

    private static boolean isMatchOssRule(String str, int i) {
        char charAt;
        int i2 = i + 1;
        if (i2 >= str.length() || (charAt = str.charAt(i2)) == '.' || charAt == '_') {
            return true;
        }
        return false;
    }

    private static int traverseValue(String str, int i, boolean z, int i2) {
        int i3 = 0;
        if (i < 0) {
            return 0;
        }
        if (!z) {
            int i4 = i - 1;
            int i5 = 0;
            while (i4 > i2) {
                int charAt = str.charAt(i4) - '0';
                if (charAt < 0 || charAt > 9) {
                    break;
                }
                i3 += charAt * ((int) Math.pow(10.0d, (double) i5));
                i4--;
                i5++;
            }
        } else {
            for (int i6 = i + 1; i6 < i2; i6++) {
                int charAt2 = str.charAt(i6) - '0';
                if (charAt2 < 0 || charAt2 > 9) {
                    break;
                }
                i3 = (i3 * 10) + charAt2;
            }
        }
        return i3;
    }
}
