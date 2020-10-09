package com.taobao.tao.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import anet.channel.util.HttpConstant;
import com.ali.user.mobile.login.model.LoginConstant;
import com.alibaba.wireless.security.SecExceptionCode;
import com.facebook.imagepipeline.common.RotationOptions;
import com.rd.animation.AbsAnimation;
import com.taobao.login4android.video.AudioRecordFunc;
import com.taobao.tao.image.ImageInitBusinss;
import com.taobao.tao.image.ImageStrategyConfig;
import com.taobao.tao.util.ImageStrategyExtra;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;

@SuppressLint({"NewApi"})
public class TaobaoImageUrlStrategy {
    private static final int[] CDN = {16, 20, 24, 30, 32, 36, 40, 48, 50, 60, 64, 70, 72, 80, 88, 90, 100, 110, 120, 125, 128, 130, 145, 160, 170, RotationOptions.ROTATE_180, 190, 200, 210, 220, 230, 234, 240, 250, RotationOptions.ROTATE_270, 290, 300, SecExceptionCode.SEC_ERROR_STA_INVALID_ENCRYPTED_DATA, 315, 320, 336, AbsAnimation.DEFAULT_ANIMATION_TIME, 360, 400, 430, 460, 468, 480, 490, 540, 560, 570, 580, 600, AudioRecordFunc.FRAME_SIZE, 670, LoginConstant.RESULT_WINDWANE_CLOSEW, 728, 760, 960, 970};
    private static final int[] CDN10000Height = {170, 220, 340, 500};
    private static final int[] CDN10000Width = {110, 150, 170, 220, 240, 290, 450, 570, 580, 620, 790};
    private static final float DEFAULT_LEVEL_RATIO = 0.1f;
    private static final String DOMAIN_DEST = "gw.alicdn.com";
    private static final String[] FUZZY_EXCLUDE_PATH = {"getAvatar"};
    private static final String HEIF_DOMAIN_DEST = "gw.alicdn.com";
    private static final int[] LEVEL_MODEL_CDN = {90, 110, 200, 320, 460, 600, 760, 960, 1200};
    private static final int[] LEVEL_MODEL_XZCDN = {90, 110, 200, 320, 460, AudioRecordFunc.FRAME_SIZE};
    private static final String[] LOOSE_CDN_DOMAIN_BLACK_LIST = {"a.tbcdn.cn", "b.tbcdn.cn", "gqrcode.alicdn.com", "g.tbcdn.cn", "m.alicdn.com", "assets.alicdn.com", "g.alicdn.com", "ag.alicdn.com", "a.dd.alicdn.com", "uaction.alicdn.com", "wwc.alicdn.com", "osdes.alicdn.com", "download.taobaocdn.com", "gtb-fun.alicdn.com", "qianniu.alicdn.com", "gamc.alicdn.com", "glife-img.alicdn.com", "ossgw.alicdn.com", "gjusp.alicdn.com", "alchemy-img.alicdn.com", "alpha.alicdn.com", "h5.m.taobao.com", "nonpublic.alicdn.com"};
    private static final String[] LOOSE_CDN_DOMAIN_WHITE_LIST = {".alicdn.com", ".tbcdn.cn", ".taobaocdn.com", ".wimg.taobao.com", "img.taobao.com", "i.mmcdn.cn"};
    private static final String[] LOOSE_CONVERGENCE_BLACK_LIST = {"i.mmcdn.cn", "cbu01.alicdn.com", "ilce.alicdn.com"};
    private static final String SHORT_SCALE = "O1CN";
    private static final String SPECIAL_DOMAIN_DEST = "picasso.alicdn.com";
    private static final String[] STRICT_CDN_DOMAIN_BLACK_LIST = {"a.tbcdn.cn", "b.tbcdn.cn", "g.tbcdn.cn", "download.taobaocdn.com"};
    private static final String[] STRICT_CDN_DOMAIN_WHITE_LIST = {".tbcdn.cn", ".taobaocdn.com", ".wimg.taobao.com", "img.taobao.com", "i.mmcdn.cn", "gw.alicdn.com", "img.alicdn.com", "gtms03.alicdn.com", "ilce.alicdn.com"};
    private static final String[] STRICT_CONVERGENCE_BLACK_LIST = {"i.mmcdn.cn", "ilce.alicdn.com"};
    private static final int[] XZCDN = {72, 88, 90, 100, 110, 120, 145, 160, 170, RotationOptions.ROTATE_180, 200, 230, 240, RotationOptions.ROTATE_270, 290, SecExceptionCode.SEC_ERROR_STA_INVALID_ENCRYPTED_DATA, 320, 360, 430, 460, 580, AudioRecordFunc.FRAME_SIZE};
    private int[] mCdn10000Height = CDN10000Height;
    private int[] mCdn10000Width = CDN10000Width;
    private int[] mCdnSizes = CDN;
    private float mDip = 1.0f;
    private String mDoMainDest = "gw.alicdn.com";
    private boolean mDomainSwitch = true;
    private String[] mFuzzyExcludePath = FUZZY_EXCLUDE_PATH;
    private boolean mGlobalSwitch = true;
    private int[] mHeifBizWhiteList;
    private String mHeifImageDomain = "gw.alicdn.com";
    private boolean mIsLowQuality = false;
    private boolean mIsNetworkSlow;
    private long mLastUpdateTime;
    private int[] mLevelModelCdnSizes = LEVEL_MODEL_CDN;
    private int[] mLevelModelXzCdnSizes = LEVEL_MODEL_XZCDN;
    private float mLevelRatio = 0.1f;
    private String[] mLooseCDNDomainBlackList = LOOSE_CDN_DOMAIN_BLACK_LIST;
    private String[] mLooseCDNDomainWhiteList = LOOSE_CDN_DOMAIN_WHITE_LIST;
    private String[] mLooseConvergenceBlackList = LOOSE_CONVERGENCE_BLACK_LIST;
    private HashMap<String, ServiceImageSwitch> mServiceImageSwitchList;
    private String mSpecialDomain = SPECIAL_DOMAIN_DEST;
    private String[] mStrictCDNDomainBlackList = STRICT_CDN_DOMAIN_BLACK_LIST;
    private String[] mStrictCDNDomainWhiteList = STRICT_CDN_DOMAIN_WHITE_LIST;
    private String[] mStrictConvergenceBlackList = STRICT_CONVERGENCE_BLACK_LIST;
    private boolean mWebpOn = true;
    private int[] mXzCdnSizes = XZCDN;

    public float getDip() {
        return this.mDip;
    }

    public synchronized void initImageUrlStrategy(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int[] iArr5, int[] iArr6, HashMap<String, ServiceImageSwitch> hashMap, String str, String str2, String str3, int[] iArr7, String[] strArr, String[] strArr2, String[] strArr3, String[] strArr4, boolean z, boolean z2, String str4, boolean z3) {
        synchronized (this) {
            this.mWebpOn = z3;
            this.mGlobalSwitch = z;
            if (this.mGlobalSwitch) {
                String[] strArr5 = strArr2;
                setAliCdnDomain(strArr2);
                setCdnSize(iArr);
                int[] iArr8 = iArr2;
                setCdn10000WidthSize(iArr2);
                int[] iArr9 = iArr3;
                setCdn10000HeightSize(iArr3);
                int[] iArr10 = iArr4;
                setXzCdnSize(iArr4);
                int[] iArr11 = iArr5;
                setLevelModelCdnSize(iArr5);
                int[] iArr12 = iArr6;
                setLevelModelXzCdnSize(iArr6);
                setLevelRatio(str4);
                HashMap<String, ServiceImageSwitch> hashMap2 = hashMap;
                setServiceIamgeSwitch(hashMap);
                String str5 = str;
                setDoMainDest(str);
                this.mDomainSwitch = z2;
                setExactExcludeDomain(strArr3);
                setFuzzyExcludePath(strArr4);
                String[] strArr6 = strArr;
                setExcludeDomainPath(strArr);
                this.mHeifImageDomain = TextUtils.isEmpty(str2) ? "gw.alicdn.com" : str2;
                this.mHeifBizWhiteList = iArr7;
                this.mSpecialDomain = TextUtils.isEmpty(str3) ? SPECIAL_DOMAIN_DEST : str3;
            }
        }
    }

    public boolean isDomainSwitch() {
        return this.mDomainSwitch;
    }

    public void initDip(Context context) {
        if (context != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            if (defaultDisplay != null) {
                defaultDisplay.getMetrics(displayMetrics);
                this.mDip = displayMetrics.density;
                this.mIsLowQuality = this.mDip > 2.0f;
            }
        }
    }

    public TaobaoImageUrlStrategy setAliCdnDomain(String[] strArr) {
        if (strArr != null && strArr.length > 0) {
            this.mLooseCDNDomainWhiteList = strArr;
        }
        return this;
    }

    public TaobaoImageUrlStrategy setExactExcludeDomain(String[] strArr) {
        if (strArr == null || strArr.length <= 0) {
            this.mLooseCDNDomainBlackList = LOOSE_CDN_DOMAIN_BLACK_LIST;
        } else {
            this.mLooseCDNDomainBlackList = strArr;
        }
        return this;
    }

    public TaobaoImageUrlStrategy setExcludeDomainPath(String[] strArr) {
        if (strArr != null && strArr.length > 0) {
            this.mLooseConvergenceBlackList = strArr;
        }
        return this;
    }

    public void updateStrictCDNDomainWhiteList(String[] strArr) {
        if (strArr != null && strArr.length > 0) {
            this.mStrictCDNDomainWhiteList = strArr;
        }
    }

    public void updateStrictCDNDomainBlackList(String[] strArr) {
        if (strArr != null && strArr.length > 0) {
            this.mStrictCDNDomainBlackList = strArr;
        }
    }

    public void updateStrictConvergenceBlackList(String[] strArr) {
        if (strArr != null && strArr.length > 0) {
            this.mStrictConvergenceBlackList = strArr;
        }
    }

    public TaobaoImageUrlStrategy setFuzzyExcludePath(String[] strArr) {
        if (strArr == null || strArr.length <= 0) {
            this.mFuzzyExcludePath = FUZZY_EXCLUDE_PATH;
        } else {
            this.mFuzzyExcludePath = strArr;
        }
        return this;
    }

    public TaobaoImageUrlStrategy setDoMainDest(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mDoMainDest = str;
        } else {
            this.mDoMainDest = "gw.alicdn.com";
        }
        return this;
    }

    public TaobaoImageUrlStrategy setCdnSize(int[] iArr) {
        if (iArr == null || iArr.length <= 0) {
            this.mCdnSizes = CDN;
        } else {
            this.mCdnSizes = iArr;
        }
        return this;
    }

    private boolean isValidSizes(int[] iArr) {
        if (iArr == null || iArr.length == 0) {
            return false;
        }
        int length = iArr.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int i3 = iArr[i];
            if (i2 >= i3) {
                return false;
            }
            i++;
            i2 = i3;
        }
        return true;
    }

    public TaobaoImageUrlStrategy setLevelModelCdnSize(int[] iArr) {
        if (isValidSizes(iArr)) {
            this.mLevelModelCdnSizes = iArr;
        }
        return this;
    }

    public TaobaoImageUrlStrategy setLevelModelXzCdnSize(int[] iArr) {
        if (isValidSizes(iArr)) {
            this.mLevelModelXzCdnSizes = iArr;
        }
        return this;
    }

    public TaobaoImageUrlStrategy setLevelRatio(String str) {
        try {
            this.mLevelRatio = Float.parseFloat(str);
        } catch (Exception unused) {
        }
        if (this.mLevelRatio < 0.0f || this.mLevelRatio > 1.0f) {
            this.mLevelRatio = 0.1f;
        }
        return this;
    }

    public TaobaoImageUrlStrategy setCdn10000WidthSize(int[] iArr) {
        if (iArr == null || iArr.length <= 0) {
            this.mCdn10000Width = CDN10000Width;
        } else {
            this.mCdn10000Width = iArr;
        }
        return this;
    }

    public TaobaoImageUrlStrategy setCdn10000HeightSize(int[] iArr) {
        if (iArr == null || iArr.length <= 0) {
            this.mCdn10000Height = CDN10000Height;
        } else {
            this.mCdn10000Height = iArr;
        }
        return this;
    }

    public TaobaoImageUrlStrategy setXzCdnSize(int[] iArr) {
        if (iArr == null || iArr.length <= 0) {
            this.mXzCdnSizes = XZCDN;
        } else {
            this.mXzCdnSizes = iArr;
        }
        return this;
    }

    public TaobaoImageUrlStrategy setServiceIamgeSwitch(HashMap<String, ServiceImageSwitch> hashMap) {
        this.mServiceImageSwitchList = hashMap;
        return this;
    }

    public int matchSize(int i) {
        if (this.mCdnSizes.length <= 0) {
            return i;
        }
        int i2 = 0;
        while (i2 < this.mCdnSizes.length - 1) {
            int i3 = this.mCdnSizes[i2];
            i2++;
            int i4 = this.mCdnSizes[i2];
            int i5 = i - i3;
            int i6 = i4 - i;
            if (i5 >= 0 && i6 >= 0) {
                return i5 < i6 ? i3 : i4;
            }
        }
        return this.mCdnSizes[this.mCdnSizes.length - 1];
    }

    public int taobaoCDNPatten(int i, boolean z) {
        return taobaoCDNPatten(i, z, true);
    }

    public int taoXZCDN(int i, boolean z, boolean z2) {
        if (z2) {
            return findBestLevel(this.mLevelModelXzCdnSizes, this.mLevelModelXzCdnSizes.length / 2, i);
        }
        return this.mXzCdnSizes[binarySearch(this.mXzCdnSizes, i, z)];
    }

    public int taobaoCDNPatten(int i, boolean z, boolean z2) {
        if (z2) {
            return findBestLevel(this.mLevelModelCdnSizes, this.mLevelModelCdnSizes.length / 2, i);
        }
        return this.mCdnSizes[binarySearch(this.mCdnSizes, i, z)];
    }

    private int findBestLevel(int[] iArr, int i, int i2) {
        int length = iArr.length;
        char c = 65535;
        while (i >= 0 && i < length) {
            int i3 = iArr[i];
            if (i2 > i3) {
                if (c >= 0) {
                    if (c == 2) {
                        break;
                    }
                } else {
                    c = 1;
                }
                i++;
            } else if (i2 >= i3) {
                break;
            } else {
                if (c >= 0) {
                    if (c == 1) {
                        break;
                    }
                } else {
                    c = 2;
                }
                i--;
            }
        }
        if (i < 0) {
            i = 0;
        } else if (i >= length) {
            i = length - 1;
        } else if (c == 1 && ((float) i2) <= ((float) iArr[i - 1]) * (this.mLevelRatio + 1.0f)) {
            i--;
        } else if (c == 2 && ((float) i2) > ((float) iArr[i]) * (this.mLevelRatio + 1.0f)) {
            i++;
        }
        return iArr[i];
    }

    public int taobaoCDN10000Width(int i, boolean z) {
        return this.mCdn10000Width[binarySearch(this.mCdn10000Width, i, z)];
    }

    public int taobaoCDN10000Height(int i, boolean z) {
        return this.mCdn10000Height[binarySearch(this.mCdn10000Height, i, z)];
    }

    public boolean isInCDN(int i) {
        for (int i2 : this.mCdnSizes) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    public String decideUrl(String str, int i) {
        return decideUrl(str, i, "", CutType.non);
    }

    public String decideUrl(String str, int i, String str2) {
        return decideUrl(str, i, "", CutType.non);
    }

    @Deprecated
    public String decideUrl(String str, int i, String str2, CutType cutType) {
        return decideUrl(str, i, str2, cutType, -1, -1, true, true, true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:110:0x0231 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x0242  */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x024b  */
    /* JADX WARNING: Removed duplicated region for block: B:176:0x038b  */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x03be  */
    /* JADX WARNING: Removed duplicated region for block: B:194:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x01a3  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x01a6  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x01f6  */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01fa  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String decideUrl(java.lang.String r31, int r32, com.taobao.tao.image.ImageStrategyConfig r33) {
        /*
            r30 = this;
            r13 = r30
            r14 = r31
            r15 = r32
            boolean r0 = r33.isSkipped()
            if (r0 == 0) goto L_0x000d
            return r14
        L_0x000d:
            r16 = 0
            r12 = 0
            if (r14 != 0) goto L_0x001c
            java.lang.String r0 = "STRATEGY.ALL"
            java.lang.String r1 = "origin url is null"
            java.lang.Object[] r2 = new java.lang.Object[r12]
            com.taobao.tao.image.Logger.w(r0, r1, r2)
            return r16
        L_0x001c:
            java.lang.String r0 = r30.changeUrl(r31)
            com.taobao.tao.util.TaobaoImageUrlStrategy$UriCDNInfo r1 = new com.taobao.tao.util.TaobaoImageUrlStrategy$UriCDNInfo
            r1.<init>(r0)
            com.taobao.tao.util.OssImageUrlStrategy r2 = com.taobao.tao.util.OssImageUrlStrategy.getInstance()
            java.lang.String r3 = r1.host
            boolean r2 = r2.isOssDomain(r3)
            if (r2 == 0) goto L_0x003c
            com.taobao.tao.util.OssImageUrlStrategy r1 = com.taobao.tao.util.OssImageUrlStrategy.getInstance()
            r11 = r33
            java.lang.String r0 = r1.decideUrl(r0, r15, r11)
            return r0
        L_0x003c:
            r11 = r33
            boolean r2 = r13.isCdnImage((com.taobao.tao.util.TaobaoImageUrlStrategy.UriCDNInfo) r1)
            r10 = 1
            if (r2 != 0) goto L_0x0051
            java.lang.String r0 = "STRATEGY.ALL"
            java.lang.String r1 = "origin not cdn url:%s"
            java.lang.Object[] r2 = new java.lang.Object[r10]
            r2[r12] = r14
            com.taobao.tao.image.Logger.w(r0, r1, r2)
            return r14
        L_0x0051:
            java.lang.String r2 = r1.host
            java.lang.Boolean r3 = r33.isEnabledMergeDomain()
            if (r3 != 0) goto L_0x005d
            boolean r3 = r13.mDomainSwitch
            if (r3 != 0) goto L_0x006d
        L_0x005d:
            java.lang.Boolean r3 = r33.isEnabledMergeDomain()
            if (r3 == 0) goto L_0x0078
            java.lang.Boolean r3 = r33.isEnabledMergeDomain()
            boolean r3 = r3.booleanValue()
            if (r3 == 0) goto L_0x0078
        L_0x006d:
            java.lang.String[] r0 = r13.convergenceUrl(r1, r12)
            r1 = r0[r12]
            r2 = r0[r10]
            r17 = r1
            goto L_0x007a
        L_0x0078:
            r17 = r0
        L_0x007a:
            r8 = r2
            com.taobao.tao.util.ImageStrategyExtra$ImageUrlInfo r9 = com.taobao.tao.util.ImageStrategyExtra.getBaseUrlInfo(r17)
            java.lang.String r0 = r9.base
            java.lang.String r1 = "_sum.jpg"
            boolean r0 = r0.endsWith(r1)
            if (r0 == 0) goto L_0x009a
            java.lang.String r0 = r9.base
            java.lang.String r1 = r9.base
            int r1 = r1.length()
            int r1 = r1 + -8
            java.lang.String r0 = r0.substring(r12, r1)
            r9.base = r0
            goto L_0x00be
        L_0x009a:
            java.lang.String r0 = r9.base
            java.lang.String r1 = "_m.jpg"
            boolean r0 = r0.endsWith(r1)
            if (r0 != 0) goto L_0x00ae
            java.lang.String r0 = r9.base
            java.lang.String r1 = "_b.jpg"
            boolean r0 = r0.endsWith(r1)
            if (r0 == 0) goto L_0x00be
        L_0x00ae:
            java.lang.String r0 = r9.base
            java.lang.String r1 = r9.base
            int r1 = r1.length()
            int r1 = r1 + -6
            java.lang.String r0 = r0.substring(r12, r1)
            r9.base = r0
        L_0x00be:
            java.lang.String r0 = r9.base
            com.taobao.tao.util.ImageStrategyExtra.parseImageUrl(r0, r9)
            boolean r0 = r9.existCo
            if (r0 != 0) goto L_0x03e1
            boolean r0 = r9.existCi
            if (r0 == 0) goto L_0x00cd
            goto L_0x03e1
        L_0x00cd:
            java.lang.StringBuffer r6 = new java.lang.StringBuffer
            java.lang.String r0 = r9.base
            int r0 = r0.length()
            int r0 = r0 + 27
            r6.<init>(r0)
            java.lang.String r0 = r9.base
            r6.append(r0)
            java.lang.String r7 = r33.getName()
            boolean r0 = r13.mGlobalSwitch
            if (r0 == 0) goto L_0x00fc
            java.util.HashMap<java.lang.String, com.taobao.tao.util.TaobaoImageUrlStrategy$ServiceImageSwitch> r0 = r13.mServiceImageSwitchList
            if (r0 == 0) goto L_0x00fc
            boolean r0 = android.text.TextUtils.isEmpty(r7)
            if (r0 != 0) goto L_0x00fc
            java.util.HashMap<java.lang.String, com.taobao.tao.util.TaobaoImageUrlStrategy$ServiceImageSwitch> r0 = r13.mServiceImageSwitchList
            java.lang.Object r0 = r0.get(r7)
            com.taobao.tao.util.TaobaoImageUrlStrategy$ServiceImageSwitch r0 = (com.taobao.tao.util.TaobaoImageUrlStrategy.ServiceImageSwitch) r0
            r18 = r0
            goto L_0x00fe
        L_0x00fc:
            r18 = r16
        L_0x00fe:
            r0 = 4604480259023595110(0x3fe6666666666666, double:0.7)
            r2 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            java.lang.String r4 = ""
            if (r18 == 0) goto L_0x011f
            double r0 = r18.getLowNetScale()
            double r2 = r18.getHighNetScale()
            java.lang.String r4 = r18.getSuffix()
            boolean r5 = r18.isUseCdnSizes()
            r5 = r5 ^ r10
            r19 = r0
            r21 = r2
            goto L_0x0124
        L_0x011f:
            r19 = r0
            r21 = r2
            r5 = 1
        L_0x0124:
            java.lang.Boolean r0 = r33.isEnabledLevelModel()
            if (r0 == 0) goto L_0x0135
            java.lang.Boolean r0 = r33.isEnabledLevelModel()
            boolean r0 = r0.booleanValue()
            r23 = r0
            goto L_0x0137
        L_0x0135:
            r23 = r5
        L_0x0137:
            r1 = 0
            int r5 = r33.getFinalWidth()
            int r24 = r33.getFinalHeight()
            com.taobao.tao.util.TaobaoImageUrlStrategy$CutType r25 = r33.getCutType()
            r0 = r30
            r2 = r6
            r3 = r9
            r26 = r4
            r4 = r5
            r5 = r24
            r27 = r6
            r24 = r7
            r6 = r19
            r29 = r8
            r28 = r9
            r8 = r21
            r10 = r32
            r11 = r25
            r12 = r23
            boolean r0 = r0.decideUrlWH(r1, r2, r3, r4, r5, r6, r8, r10, r11, r12)
            java.lang.Boolean r1 = r33.isShortEdgeEnable()
            boolean r1 = r1.booleanValue()
            if (r1 == 0) goto L_0x017e
            java.lang.String r1 = "O1CN"
            boolean r1 = r14.contains(r1)
            if (r1 == 0) goto L_0x017e
            java.lang.String r1 = "xl"
            r2 = r27
            r2.append(r1)
            goto L_0x0180
        L_0x017e:
            r2 = r27
        L_0x0180:
            if (r15 >= 0) goto L_0x0190
            r1 = r28
            java.lang.String r3 = r1.quality
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x0192
            java.lang.String r3 = r1.quality
        L_0x018e:
            r4 = r3
            goto L_0x01dc
        L_0x0190:
            r1 = r28
        L_0x0192:
            java.lang.Boolean r3 = r33.isEnabledQuality()
            if (r3 == 0) goto L_0x01a6
            java.lang.Boolean r3 = r33.isEnabledQuality()
            boolean r3 = r3.booleanValue()
            if (r3 == 0) goto L_0x01a3
            goto L_0x01a6
        L_0x01a3:
            r3 = r16
            goto L_0x018e
        L_0x01a6:
            com.taobao.tao.util.TaobaoImageUrlStrategy$ImageQuality r3 = r33.getFinalImageQuality()
            if (r3 == 0) goto L_0x01b5
            com.taobao.tao.util.TaobaoImageUrlStrategy$ImageQuality r3 = r33.getFinalImageQuality()
            java.lang.String r3 = r3.getImageQuality()
            goto L_0x018e
        L_0x01b5:
            if (r18 == 0) goto L_0x01c0
            java.lang.String r3 = r18.getLowNetQ()
            java.lang.String r4 = r18.getHighNetQ()
            goto L_0x01dc
        L_0x01c0:
            boolean r3 = r13.mIsLowQuality
            if (r3 == 0) goto L_0x01cb
            com.taobao.tao.util.TaobaoImageUrlStrategy$ImageQuality r3 = com.taobao.tao.util.TaobaoImageUrlStrategy.ImageQuality.q50
        L_0x01c6:
            java.lang.String r3 = r3.getImageQuality()
            goto L_0x01ce
        L_0x01cb:
            com.taobao.tao.util.TaobaoImageUrlStrategy$ImageQuality r3 = com.taobao.tao.util.TaobaoImageUrlStrategy.ImageQuality.q75
            goto L_0x01c6
        L_0x01ce:
            boolean r4 = r13.mIsLowQuality
            if (r4 == 0) goto L_0x01d9
            com.taobao.tao.util.TaobaoImageUrlStrategy$ImageQuality r4 = com.taobao.tao.util.TaobaoImageUrlStrategy.ImageQuality.q75
        L_0x01d4:
            java.lang.String r4 = r4.getImageQuality()
            goto L_0x01dc
        L_0x01d9:
            com.taobao.tao.util.TaobaoImageUrlStrategy$ImageQuality r4 = com.taobao.tao.util.TaobaoImageUrlStrategy.ImageQuality.q90
            goto L_0x01d4
        L_0x01dc:
            if (r3 == 0) goto L_0x01ec
            if (r4 == 0) goto L_0x01ec
            boolean r3 = r13.decideValueByNetwork(r0, r2, r3, r4)
            if (r3 != 0) goto L_0x01eb
            if (r0 == 0) goto L_0x01e9
            goto L_0x01eb
        L_0x01e9:
            r0 = 0
            goto L_0x01ec
        L_0x01eb:
            r0 = 1
        L_0x01ec:
            if (r15 >= 0) goto L_0x01fa
            java.lang.String r3 = r1.sharpen
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x01fa
            java.lang.String r3 = r1.sharpen
        L_0x01f8:
            r4 = r3
            goto L_0x0227
        L_0x01fa:
            java.lang.Boolean r3 = r33.isEnabledSharpen()
            if (r3 == 0) goto L_0x020e
            java.lang.Boolean r3 = r33.isEnabledSharpen()
            boolean r3 = r3.booleanValue()
            if (r3 == 0) goto L_0x020b
            goto L_0x020e
        L_0x020b:
            r3 = r16
            goto L_0x01f8
        L_0x020e:
            com.taobao.tao.util.TaobaoImageUrlStrategy$ImageSharpen r3 = com.taobao.tao.util.TaobaoImageUrlStrategy.ImageSharpen.non
            java.lang.String r16 = r3.getImageSharpen()
            com.taobao.tao.util.TaobaoImageUrlStrategy$ImageSharpen r3 = com.taobao.tao.util.TaobaoImageUrlStrategy.ImageSharpen.non
            java.lang.String r3 = r3.getImageSharpen()
            if (r18 == 0) goto L_0x0224
            java.lang.String r16 = r18.getLowNetSharpen()
            java.lang.String r3 = r18.getHighNetSharpen()
        L_0x0224:
            r4 = r3
            r3 = r16
        L_0x0227:
            if (r3 == 0) goto L_0x0237
            if (r4 == 0) goto L_0x0237
            boolean r3 = r13.decideValueByNetwork(r0, r2, r3, r4)
            if (r3 != 0) goto L_0x0236
            if (r0 == 0) goto L_0x0234
            goto L_0x0236
        L_0x0234:
            r0 = 0
            goto L_0x0237
        L_0x0236:
            r0 = 1
        L_0x0237:
            r4 = r26
            r13.decideUrlSuffix(r0, r2, r4)
            boolean r0 = r33.isForcedWebPOn()
            if (r0 == 0) goto L_0x024b
            r0 = 1
            r13.decideUrlWebP(r2, r0, r0)
            r4 = r29
        L_0x0248:
            r7 = 0
            goto L_0x032f
        L_0x024b:
            r0 = 1
            int r3 = r33.getBizId()
            boolean r3 = r13.isHeifAllowedWithBiz(r3)
            if (r3 == 0) goto L_0x02f7
            com.taobao.tao.image.ImageInitBusinss r3 = com.taobao.tao.image.ImageInitBusinss.getInstance()
            if (r3 == 0) goto L_0x02f7
            com.taobao.tao.image.ImageInitBusinss r3 = com.taobao.tao.image.ImageInitBusinss.getInstance()
            com.taobao.tao.image.IImageExtendedSupport r3 = r3.getImageExtendedSupport()
            if (r3 == 0) goto L_0x02f7
            boolean r4 = r3.isHEIFSupported()
            if (r4 == 0) goto L_0x02f7
            java.lang.String r4 = r1.suffix
            java.lang.String r5 = "imgheiftag=0"
            boolean r4 = r4.contains(r5)
            if (r4 != 0) goto L_0x02f7
            boolean r4 = android.text.TextUtils.isEmpty(r29)
            if (r4 != 0) goto L_0x02f7
            r4 = r29
            int r5 = r2.indexOf(r4)
            if (r5 < 0) goto L_0x02f9
            boolean r6 = r3.isHEIFPngSupported()
            if (r6 != 0) goto L_0x02e3
            java.lang.String r6 = r2.toString()
            java.lang.String r7 = ".png"
            boolean r6 = r6.contains(r7)
            if (r6 == 0) goto L_0x02e3
            r6 = 73
            boolean r6 = com.taobao.tao.image.Logger.isLoggable(r6)
            if (r6 == 0) goto L_0x0248
            java.lang.String r6 = "STRATEGY.ALL"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "outString ="
            r7.append(r8)
            r7.append(r2)
            java.lang.String r8 = ","
            r7.append(r8)
            r7.append(r5)
            java.lang.String r8 = ","
            r7.append(r8)
            int r8 = r4.length()
            int r5 = r5 + r8
            r7.append(r5)
            java.lang.String r5 = ","
            r7.append(r5)
            java.lang.String r5 = r13.mHeifImageDomain
            r7.append(r5)
            java.lang.String r5 = ",isHEIFPngSupported()="
            r7.append(r5)
            boolean r3 = r3.isHEIFPngSupported()
            r7.append(r3)
            java.lang.String r3 = r7.toString()
            r7 = 0
            java.lang.Object[] r5 = new java.lang.Object[r7]
            com.taobao.tao.image.Logger.i(r6, r3, r5)
            goto L_0x032f
        L_0x02e3:
            r7 = 0
            int r3 = r4.length()
            int r3 = r3 + r5
            java.lang.String r4 = r13.mHeifImageDomain
            r2.replace(r5, r3, r4)
            java.lang.String r8 = r13.mHeifImageDomain
            java.lang.String r3 = "_.heic"
            r2.append(r3)
            r4 = r8
            goto L_0x032f
        L_0x02f7:
            r4 = r29
        L_0x02f9:
            r7 = 0
            java.lang.String r3 = r1.suffix
            java.lang.String r5 = "imgwebptag=0"
            boolean r3 = r3.contains(r5)
            r3 = r3 ^ r0
            if (r18 == 0) goto L_0x030e
            boolean r5 = r18.isUseWebp()
            if (r5 == 0) goto L_0x030c
            goto L_0x030e
        L_0x030c:
            r5 = 0
            goto L_0x030f
        L_0x030e:
            r5 = 1
        L_0x030f:
            if (r3 == 0) goto L_0x032b
            java.lang.Boolean r3 = r33.isEnabledWebP()
            if (r3 != 0) goto L_0x0319
            if (r5 != 0) goto L_0x0329
        L_0x0319:
            java.lang.Boolean r3 = r33.isEnabledWebP()
            if (r3 == 0) goto L_0x032b
            java.lang.Boolean r3 = r33.isEnabledWebP()
            boolean r3 = r3.booleanValue()
            if (r3 == 0) goto L_0x032b
        L_0x0329:
            r3 = 1
            goto L_0x032c
        L_0x032b:
            r3 = 0
        L_0x032c:
            r13.decideUrlWebP(r2, r7, r3)
        L_0x032f:
            boolean r3 = com.taobao.tao.image.ImageStrategyConfig.isUseSpecialDomain
            if (r3 == 0) goto L_0x037a
            boolean r3 = android.text.TextUtils.isEmpty(r4)
            if (r3 != 0) goto L_0x037a
            java.lang.String r3 = r13.mDoMainDest
            boolean r3 = r4.equals(r3)
            if (r3 != 0) goto L_0x0349
            java.lang.String r3 = r13.mHeifImageDomain
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L_0x037a
        L_0x0349:
            java.lang.String r3 = "/"
            int r3 = r14.lastIndexOf(r3)
            int r3 = r3 + r0
            if (r3 <= 0) goto L_0x037a
            int r5 = r31.length()
            if (r3 >= r5) goto L_0x037a
            java.lang.String r5 = r13.mSpecialDomain
            boolean r5 = android.text.TextUtils.isEmpty(r5)
            if (r5 != 0) goto L_0x037a
            java.lang.String r3 = r14.substring(r3)
            java.lang.String r5 = "O1CNA1"
            boolean r3 = r3.startsWith(r5)
            if (r3 == 0) goto L_0x037a
            int r3 = r2.indexOf(r4)
            int r4 = r4.length()
            int r4 = r4 + r3
            java.lang.String r5 = r13.mSpecialDomain
            r2.replace(r3, r4, r5)
        L_0x037a:
            java.lang.String r1 = r1.suffix
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            r2 = 68
            boolean r2 = com.taobao.tao.image.Logger.isLoggable(r2)
            if (r2 == 0) goto L_0x03ac
            java.lang.String r2 = "STRATEGY.ALL"
            java.lang.String r3 = "Dip=%.1f UISize=%d Area=%s\nOriginUrl=%s\nDecideUrl=%s"
            r4 = 5
            java.lang.Object[] r4 = new java.lang.Object[r4]
            float r5 = r13.mDip
            java.lang.Float r5 = java.lang.Float.valueOf(r5)
            r4[r7] = r5
            java.lang.Integer r5 = java.lang.Integer.valueOf(r32)
            r4[r0] = r5
            r0 = 2
            r4[r0] = r24
            r0 = 3
            r4[r0] = r17
            r0 = 4
            r4[r0] = r1
            com.taobao.tao.image.Logger.d(r2, r3, r4)
        L_0x03ac:
            boolean r0 = com.taobao.tao.image.ImageStrategyConfig.isWebpDegrade
            if (r0 == 0) goto L_0x03e0
            int r0 = android.os.Build.VERSION.SDK_INT
            r2 = 28
            if (r0 != r2) goto L_0x03e0
            java.lang.String r0 = ".png"
            boolean r0 = r1.contains(r0)
            if (r0 != 0) goto L_0x03e0
            java.lang.String r0 = "O1CN"
            boolean r0 = r1.contains(r0)
            if (r0 != 0) goto L_0x03ce
            java.lang.String r0 = "jpg_.heic"
            boolean r0 = r1.endsWith(r0)
            if (r0 != 0) goto L_0x03d6
        L_0x03ce:
            java.lang.String r0 = "jpg_.webp"
            boolean r0 = r1.endsWith(r0)
            if (r0 == 0) goto L_0x03e0
        L_0x03d6:
            int r0 = r1.length()
            int r0 = r0 + -6
            java.lang.String r1 = r1.substring(r7, r0)
        L_0x03e0:
            return r1
        L_0x03e1:
            return r17
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.tao.util.TaobaoImageUrlStrategy.decideUrl(java.lang.String, int, com.taobao.tao.image.ImageStrategyConfig):java.lang.String");
    }

    private boolean isHeifAllowedWithBiz(int i) {
        int[] iArr = this.mHeifBizWhiteList;
        if (iArr == null || iArr.length <= 0) {
            return true;
        }
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:66:0x01b4  */
    @java.lang.Deprecated
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String decideUrl(java.lang.String r31, int r32, java.lang.String r33, com.taobao.tao.util.TaobaoImageUrlStrategy.CutType r34, int r35, int r36, boolean r37, boolean r38, boolean r39) {
        /*
            r30 = this;
            r13 = r30
            r14 = r33
            r15 = 0
            if (r31 != 0) goto L_0x0012
            java.lang.String r0 = "STRATEGY.ALL"
            java.lang.String r1 = "origin url is null"
            java.lang.Object[] r2 = new java.lang.Object[r15]
            com.taobao.tao.image.Logger.i(r0, r1, r2)
            r0 = 0
            return r0
        L_0x0012:
            java.lang.String r0 = r30.changeUrl(r31)
            com.taobao.tao.util.TaobaoImageUrlStrategy$UriCDNInfo r1 = new com.taobao.tao.util.TaobaoImageUrlStrategy$UriCDNInfo
            r1.<init>(r0)
            com.taobao.tao.util.OssImageUrlStrategy r2 = com.taobao.tao.util.OssImageUrlStrategy.getInstance()
            java.lang.String r3 = r1.host
            boolean r2 = r2.isOssDomain(r3)
            if (r2 == 0) goto L_0x003c
            com.taobao.tao.util.OssImageUrlStrategy r1 = com.taobao.tao.util.OssImageUrlStrategy.getInstance()
            java.lang.String r2 = "default"
            com.taobao.tao.image.ImageStrategyConfig$Builder r2 = com.taobao.tao.image.ImageStrategyConfig.newBuilderWithName(r2)
            com.taobao.tao.image.ImageStrategyConfig r2 = r2.build()
            r12 = r32
            java.lang.String r0 = r1.decideUrl(r0, r12, r2)
            return r0
        L_0x003c:
            r12 = r32
            boolean r2 = r13.isCdnImage((com.taobao.tao.util.TaobaoImageUrlStrategy.UriCDNInfo) r1)
            r11 = 1
            if (r2 != 0) goto L_0x0051
            java.lang.String r1 = "STRATEGY.ALL"
            java.lang.String r2 = "origin not cdn url:%s"
            java.lang.Object[] r3 = new java.lang.Object[r11]
            r3[r15] = r0
            com.taobao.tao.image.Logger.w(r1, r2, r3)
            return r0
        L_0x0051:
            boolean r2 = r13.mDomainSwitch
            if (r2 == 0) goto L_0x005b
            java.lang.String[] r0 = r13.convergenceUrl(r1, r15)
            r0 = r0[r15]
        L_0x005b:
            r16 = r0
            com.taobao.tao.util.ImageStrategyExtra$ImageUrlInfo r10 = com.taobao.tao.util.ImageStrategyExtra.getBaseUrlInfo(r16)
            java.lang.String r0 = r10.base
            java.lang.String r1 = "_sum.jpg"
            boolean r0 = r0.endsWith(r1)
            if (r0 == 0) goto L_0x007c
            java.lang.String r0 = r10.base
            java.lang.String r1 = r10.base
            int r1 = r1.length()
            int r1 = r1 + -8
            java.lang.String r0 = r0.substring(r15, r1)
            r10.base = r0
            goto L_0x00a0
        L_0x007c:
            java.lang.String r0 = r10.base
            java.lang.String r1 = "_m.jpg"
            boolean r0 = r0.endsWith(r1)
            if (r0 != 0) goto L_0x0090
            java.lang.String r0 = r10.base
            java.lang.String r1 = "_b.jpg"
            boolean r0 = r0.endsWith(r1)
            if (r0 == 0) goto L_0x00a0
        L_0x0090:
            java.lang.String r0 = r10.base
            java.lang.String r1 = r10.base
            int r1 = r1.length()
            int r1 = r1 + -6
            java.lang.String r0 = r0.substring(r15, r1)
            r10.base = r0
        L_0x00a0:
            java.lang.String r0 = r10.base
            com.taobao.tao.util.ImageStrategyExtra.parseImageUrl(r0, r10)
            java.lang.StringBuffer r8 = new java.lang.StringBuffer
            java.lang.String r0 = r10.base
            int r0 = r0.length()
            int r0 = r0 + 27
            r8.<init>(r0)
            java.lang.String r0 = r10.base
            r8.append(r0)
            java.lang.String r0 = ""
            boolean r1 = r13.mIsLowQuality
            if (r1 == 0) goto L_0x00c4
            com.taobao.tao.util.TaobaoImageUrlStrategy$ImageQuality r1 = com.taobao.tao.util.TaobaoImageUrlStrategy.ImageQuality.q50
        L_0x00bf:
            java.lang.String r1 = r1.getImageQuality()
            goto L_0x00c7
        L_0x00c4:
            com.taobao.tao.util.TaobaoImageUrlStrategy$ImageQuality r1 = com.taobao.tao.util.TaobaoImageUrlStrategy.ImageQuality.q75
            goto L_0x00bf
        L_0x00c7:
            boolean r2 = r13.mIsLowQuality
            if (r2 == 0) goto L_0x00d2
            com.taobao.tao.util.TaobaoImageUrlStrategy$ImageQuality r2 = com.taobao.tao.util.TaobaoImageUrlStrategy.ImageQuality.q75
        L_0x00cd:
            java.lang.String r2 = r2.getImageQuality()
            goto L_0x00d5
        L_0x00d2:
            com.taobao.tao.util.TaobaoImageUrlStrategy$ImageQuality r2 = com.taobao.tao.util.TaobaoImageUrlStrategy.ImageQuality.q90
            goto L_0x00cd
        L_0x00d5:
            com.taobao.tao.util.TaobaoImageUrlStrategy$ImageSharpen r3 = com.taobao.tao.util.TaobaoImageUrlStrategy.ImageSharpen.non
            java.lang.String r3 = r3.getImageSharpen()
            com.taobao.tao.util.TaobaoImageUrlStrategy$ImageSharpen r4 = com.taobao.tao.util.TaobaoImageUrlStrategy.ImageSharpen.non
            java.lang.String r4 = r4.getImageSharpen()
            r5 = 4604480259023595110(0x3fe6666666666666, double:0.7)
            r17 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            boolean r7 = r13.mGlobalSwitch
            if (r7 == 0) goto L_0x0131
            java.util.HashMap<java.lang.String, com.taobao.tao.util.TaobaoImageUrlStrategy$ServiceImageSwitch> r7 = r13.mServiceImageSwitchList
            if (r7 == 0) goto L_0x0131
            boolean r7 = android.text.TextUtils.isEmpty(r33)
            if (r7 != 0) goto L_0x0131
            java.util.HashMap<java.lang.String, com.taobao.tao.util.TaobaoImageUrlStrategy$ServiceImageSwitch> r7 = r13.mServiceImageSwitchList
            java.lang.Object r7 = r7.get(r14)
            com.taobao.tao.util.TaobaoImageUrlStrategy$ServiceImageSwitch r7 = (com.taobao.tao.util.TaobaoImageUrlStrategy.ServiceImageSwitch) r7
            if (r7 == 0) goto L_0x0131
            if (r39 == 0) goto L_0x0131
            boolean r0 = r7.isUseWebp()
            java.lang.String r1 = r7.getLowNetQ()
            java.lang.String r2 = r7.getHighNetQ()
            java.lang.String r3 = r7.getLowNetSharpen()
            java.lang.String r4 = r7.getHighNetSharpen()
            double r5 = r7.getLowNetScale()
            double r17 = r7.getHighNetScale()
            java.lang.String r7 = r7.getSuffix()
            r19 = r0
            r9 = r1
            r27 = r5
            r5 = r2
            r2 = r7
            r6 = r27
            r29 = r4
            r4 = r3
            r3 = r29
            goto L_0x013c
        L_0x0131:
            r19 = r37
            r9 = r1
            r6 = r5
            r5 = r2
            r2 = r0
            r27 = r4
            r4 = r3
            r3 = r27
        L_0x013c:
            r1 = 0
            r20 = 1
            r0 = r30
            r15 = r2
            r2 = r8
            r21 = r3
            r3 = r10
            r22 = r4
            r4 = r35
            r23 = r5
            r5 = r36
            r24 = r8
            r25 = r9
            r8 = r17
            r26 = r10
            r10 = r32
            r17 = 1
            r11 = r34
            r12 = r20
            boolean r0 = r0.decideUrlWH(r1, r2, r3, r4, r5, r6, r8, r10, r11, r12)
            if (r38 == 0) goto L_0x0177
            r3 = r23
            r1 = r24
            r2 = r25
            boolean r2 = r13.decideValueByNetwork(r0, r1, r2, r3)
            if (r2 != 0) goto L_0x0175
            if (r0 == 0) goto L_0x0173
            goto L_0x0175
        L_0x0173:
            r0 = 0
            goto L_0x0179
        L_0x0175:
            r0 = 1
            goto L_0x0179
        L_0x0177:
            r1 = r24
        L_0x0179:
            r4 = r21
            r3 = r22
            boolean r2 = r13.decideValueByNetwork(r0, r1, r3, r4)
            if (r2 != 0) goto L_0x0188
            if (r0 == 0) goto L_0x0186
            goto L_0x0188
        L_0x0186:
            r0 = 0
            goto L_0x0189
        L_0x0188:
            r0 = 1
        L_0x0189:
            r13.decideUrlSuffix(r0, r1, r15)
            if (r19 == 0) goto L_0x019c
            r0 = r26
            java.lang.String r2 = r0.suffix
            java.lang.String r3 = "imgwebptag=0"
            boolean r2 = r2.contains(r3)
            if (r2 != 0) goto L_0x019e
            r2 = 1
            goto L_0x019f
        L_0x019c:
            r0 = r26
        L_0x019e:
            r2 = 0
        L_0x019f:
            r3 = 0
            r13.decideUrlWebP(r1, r3, r2)
            java.lang.String r0 = r0.suffix
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            r1 = 68
            boolean r1 = com.taobao.tao.image.Logger.isLoggable(r1)
            if (r1 == 0) goto L_0x01d6
            java.lang.String r1 = "STRATEGY.ALL"
            java.lang.String r2 = "[Non-Config] Dip=%.1f UISize=%d Area=%s\nOriginUrl=%s\nDecideUrl=%s"
            r3 = 5
            java.lang.Object[] r3 = new java.lang.Object[r3]
            float r4 = r13.mDip
            java.lang.Float r4 = java.lang.Float.valueOf(r4)
            r5 = 0
            r3[r5] = r4
            java.lang.Integer r4 = java.lang.Integer.valueOf(r32)
            r3[r17] = r4
            r4 = 2
            r3[r4] = r14
            r4 = 3
            r3[r4] = r16
            r4 = 4
            r3[r4] = r0
            com.taobao.tao.image.Logger.d(r1, r2, r3)
        L_0x01d6:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.tao.util.TaobaoImageUrlStrategy.decideUrl(java.lang.String, int, java.lang.String, com.taobao.tao.util.TaobaoImageUrlStrategy$CutType, int, int, boolean, boolean, boolean):java.lang.String");
    }

    private boolean decideUrlWH(boolean z, StringBuffer stringBuffer, ImageStrategyExtra.ImageUrlInfo imageUrlInfo, int i, int i2, double d, double d2, int i3, CutType cutType, boolean z2) {
        int i4;
        if (i3 > 0) {
            double d3 = (double) i3;
            if (!isNetworkSlow()) {
                d = d2;
            }
            Double.isNaN(d3);
            i3 = (int) (d3 * d);
        }
        ImageSize imageSize = null;
        if (i3 < 0) {
            if (imageUrlInfo != null && imageUrlInfo.width > 0 && imageUrlInfo.height > 0) {
                imageSize = new ImageSize(true, false, imageUrlInfo.width, imageUrlInfo.height);
            }
        } else if (i >= 0 && i2 >= 0) {
            imageSize = matchWH2CDN10000(i, i2, i3, z2);
            imageSize.keep = false;
        } else if (imageUrlInfo == null || imageUrlInfo.width <= 0 || imageUrlInfo.height <= 0) {
            if (cutType == null || cutType == CutType.non) {
                i4 = taobaoCDNPatten((int) (((float) i3) * this.mDip), true, z2);
            } else {
                i4 = taoXZCDN((int) (((float) i3) * this.mDip), true, z2);
            }
            imageSize = new ImageSize(i4, i4);
        } else {
            imageSize = matchWH2CDN10000(imageUrlInfo.width, imageUrlInfo.height, i3, z2);
        }
        if (imageSize == null) {
            return false;
        }
        if (!z) {
            stringBuffer.append('_');
        }
        stringBuffer.append(imageSize.width);
        stringBuffer.append('x');
        stringBuffer.append(imageSize.height);
        if (imageSize.keep && imageUrlInfo != null) {
            stringBuffer.append(imageUrlInfo.cj);
        } else if (!imageSize.limitWH && cutType != null) {
            stringBuffer.append(cutType.getCutType());
        }
        return true;
    }

    private boolean decideValueByNetwork(boolean z, StringBuffer stringBuffer, String str, String str2) {
        if (!isNetworkSlow()) {
            str = str2;
        }
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (!z) {
            stringBuffer.append('_');
        }
        stringBuffer.append(str);
        return true;
    }

    private void decideUrlSuffix(boolean z, StringBuffer stringBuffer, String str) {
        if (!TextUtils.isEmpty(str)) {
            stringBuffer.append(str);
            z = true;
        }
        if (z) {
            stringBuffer.append(".jpg");
        }
    }

    private void decideUrlWebP(StringBuffer stringBuffer, boolean z, boolean z2) {
        if (z || (z2 && this.mWebpOn && isSupportWebP())) {
            stringBuffer.append("_.webp");
        }
    }

    private ImageSize matchWH2CDN10000(int i, int i2, int i3) {
        return matchWH2CDN10000(i, i2, i3, true);
    }

    private ImageSize matchWH2CDN10000(int i, int i2, int i3, boolean z) {
        boolean z2 = false;
        boolean z3 = true;
        if ((i == 10000 && i2 == 10000) || (i == 0 && i2 == 0)) {
            i = taobaoCDNPatten((int) (((float) i3) * this.mDip), true, z);
            i2 = i;
        } else {
            if (i2 == 10000) {
                i = taobaoCDN10000Width((int) (((float) i3) * this.mDip), true);
                i2 = 10000;
            } else if (i == 10000) {
                i2 = taobaoCDN10000Height((int) (((float) i3) * this.mDip), true);
                i = 10000;
            } else {
                z2 = true;
            }
            return new ImageSize(z2, z3, i, i2);
        }
        z3 = false;
        return new ImageSize(z2, z3, i, i2);
    }

    private String changeUrl(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(SecExceptionCode.SEC_ERROR_INIT_LOW_VERSION_DATA);
        if (indexOf > 0) {
            str = str.substring(0, indexOf);
        }
        return str.replace(Operators.SPACE_STR, "");
    }

    private boolean isExcludeUrl(String[] strArr, String str, String str2) {
        Uri parse;
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        if (strArr != null) {
            if (str2 == null && (parse = Uri.parse(str)) != null) {
                str2 = parse.getHost();
            }
            for (String equals : strArr) {
                if (equals.equals(str2)) {
                    return true;
                }
            }
        }
        if (this.mFuzzyExcludePath != null) {
            for (String indexOf : this.mFuzzyExcludePath) {
                if (str.indexOf(indexOf) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private String[] convergenceUrl(String[] strArr, String[] strArr2, String str, String str2, boolean z) {
        Uri parse;
        if (TextUtils.isEmpty(this.mDoMainDest) || TextUtils.isEmpty(str)) {
            return new String[]{str, str2};
        }
        if (str2 == null && (parse = Uri.parse(str)) != null) {
            str2 = parse.getHost();
        }
        if (str2 == null || str2.equals(this.mDoMainDest)) {
            return new String[]{str, str2};
        }
        int length = strArr2 != null ? strArr2.length : 0;
        for (int i = 0; i < length; i++) {
            if (str2.indexOf(strArr2[i]) >= 0) {
                return new String[]{str, str2};
            }
        }
        if (z) {
            int length2 = strArr != null ? strArr.length : 0;
            int i2 = 0;
            while (i2 < length2 && str2.indexOf(strArr[i2]) < 0) {
                i2++;
            }
            if (i2 >= length2) {
                return new String[]{str, str2};
            }
        }
        return new String[]{str.replaceFirst(str2, this.mDoMainDest), this.mDoMainDest};
    }

    public String[] convergenceUrl(UriCDNInfo uriCDNInfo, boolean z) {
        return convergenceUrl(this.mLooseCDNDomainWhiteList, this.mLooseConvergenceBlackList, uriCDNInfo.url, uriCDNInfo.host, z);
    }

    public String convergenceUrl(String str) {
        return convergenceUrl(this.mLooseCDNDomainWhiteList, this.mLooseConvergenceBlackList, str, (String) null, true)[0];
    }

    public String strictConvergenceUrl(UriCDNInfo uriCDNInfo, boolean z) {
        return convergenceUrl(this.mStrictCDNDomainWhiteList, this.mStrictConvergenceBlackList, uriCDNInfo.url, uriCDNInfo.host, z)[0];
    }

    public boolean isCdnImage(UriCDNInfo uriCDNInfo) {
        return isCdnImage(uriCDNInfo.url, uriCDNInfo.host);
    }

    public boolean isCdnImage(String str) {
        return isCdnImage(str, (String) null);
    }

    private boolean isCdnImage(String str, String str2) {
        Uri parse;
        if (isExcludeUrl(this.mLooseCDNDomainBlackList, str, str2)) {
            return false;
        }
        if (str2 == null && (parse = Uri.parse(str)) != null) {
            str2 = parse.getHost();
        }
        if (str2 != null) {
            for (String indexOf : this.mLooseCDNDomainWhiteList) {
                if (str2.indexOf(indexOf) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isStrictCdnImage(UriCDNInfo uriCDNInfo) {
        String str = uriCDNInfo.host;
        if (!isExcludeUrl(this.mStrictCDNDomainBlackList, uriCDNInfo.url, str) && str != null) {
            for (String indexOf : this.mStrictCDNDomainWhiteList) {
                if (str.indexOf(indexOf) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isExcludeUrl(String str) {
        return isExcludeUrl(this.mLooseCDNDomainBlackList, str, (String) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0022, code lost:
        r7 = r2 + 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int binarySearch(int[] r6, int r7, boolean r8) {
        /*
            r5 = this;
            int r0 = r6.length
            int r0 = r0 + -1
            r1 = 0
            r2 = r0
            r0 = 0
        L_0x0006:
            if (r0 > r2) goto L_0x001d
            int r3 = r0 + r2
            int r3 = r3 / 2
            r4 = r6[r3]
            if (r7 != r4) goto L_0x0011
            return r3
        L_0x0011:
            r4 = r6[r3]
            if (r7 >= r4) goto L_0x0019
            int r3 = r3 + -1
            r2 = r3
            goto L_0x0006
        L_0x0019:
            int r3 = r3 + 1
            r0 = r3
            goto L_0x0006
        L_0x001d:
            if (r2 >= 0) goto L_0x0020
            return r1
        L_0x0020:
            if (r8 == 0) goto L_0x002a
            int r7 = r2 + 1
            int r6 = r6.length
            int r6 = r6 + -1
            if (r7 > r6) goto L_0x002a
            goto L_0x002b
        L_0x002a:
            r7 = r2
        L_0x002b:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.tao.util.TaobaoImageUrlStrategy.binarySearch(int[], int, boolean):int");
    }

    /* access modifiers changed from: package-private */
    public boolean isSupportWebP() {
        return ImageInitBusinss.getInstance() != null && ImageInitBusinss.getInstance().getStrategySupport().isSupportWebP();
    }

    /* access modifiers changed from: package-private */
    public boolean isNetworkSlow() {
        long nanoTime = System.nanoTime();
        if (nanoTime - this.mLastUpdateTime > 2000000000) {
            this.mIsNetworkSlow = ImageInitBusinss.getInstance() != null && ImageInitBusinss.getInstance().getStrategySupport().isNetworkSlow();
            this.mLastUpdateTime = nanoTime;
        }
        return this.mIsNetworkSlow;
    }

    private static class TImageUrlStrategyHolder {
        public static final TaobaoImageUrlStrategy instance = new TaobaoImageUrlStrategy();

        private TImageUrlStrategyHolder() {
        }
    }

    public static TaobaoImageUrlStrategy getInstance() {
        return TImageUrlStrategyHolder.instance;
    }

    public enum CutType {
        xz("xz", "1c"),
        non("", "");
        
        String ossCut;
        String tfsCut;

        private CutType(String str, String str2) {
            this.tfsCut = "";
            this.ossCut = "";
            this.tfsCut = str;
            this.ossCut = str2;
        }

        public String getCutType() {
            return this.tfsCut;
        }

        public String getOssCut() {
            return this.ossCut;
        }
    }

    public enum ImageQuality {
        q90("q90", "90q"),
        q75("q75", "75q"),
        q60("q60", "60q"),
        q50("q50", "50q"),
        q30("q30", "30q"),
        non("", "");
        
        String ossQ;
        String tfsQ;

        private ImageQuality(String str, String str2) {
            this.tfsQ = "";
            this.ossQ = "";
            this.tfsQ = str;
            this.ossQ = str2;
        }

        public String getImageQuality() {
            return this.tfsQ;
        }

        public String getOssQuality() {
            return this.ossQ;
        }
    }

    public enum ImageSharpen {
        s100("s100"),
        s110("s110"),
        s120("s120"),
        s130("s130"),
        s140("s140"),
        s150("s150"),
        non("");
        
        String mImageSharpen;

        private ImageSharpen(String str) {
            this.mImageSharpen = "";
            this.mImageSharpen = str;
        }

        public String getImageSharpen() {
            return this.mImageSharpen;
        }
    }

    public static class ServiceImageSwitch {
        private String areaName;
        private String highNetQ = ImageQuality.q90.getImageQuality();
        private double highNetScale = 1.0d;
        private String highNetSharpen = ImageSharpen.non.getImageSharpen();
        private String lowNetQ = ImageQuality.q75.getImageQuality();
        private double lowNetScale = 1.0d;
        private String lowNetSharpen = ImageSharpen.non.getImageSharpen();
        private String suffix;
        private boolean useCdnSizes = false;
        private boolean useWebp = true;

        public String toString() {
            return "areaName =" + this.areaName + " useWebp =" + this.useWebp + " lowNetQ =" + this.lowNetQ + " highNetQ =" + this.highNetQ + " lowNetSharpen =" + this.lowNetSharpen + " highNetSharpen =" + this.highNetSharpen + " lowNetScale =" + this.lowNetScale + " highNetScale =" + this.highNetScale + " useCdnSizes=" + this.useCdnSizes;
        }

        public String getAreaName() {
            return this.areaName;
        }

        public void setAreaName(String str) {
            this.areaName = str;
        }

        public boolean isUseWebp() {
            return this.useWebp;
        }

        public void setUseWebp(boolean z) {
            this.useWebp = z;
        }

        public boolean isUseCdnSizes() {
            return this.useCdnSizes;
        }

        public void useCdnSizes(boolean z) {
            this.useCdnSizes = z;
        }

        public String getSuffix() {
            return this.suffix;
        }

        public void setSuffix(String str) {
            this.suffix = str;
        }

        public String getLowNetQ() {
            return this.lowNetQ;
        }

        public void setLowNetQ(String str) {
            if (TextUtils.isEmpty(str)) {
                str = ImageQuality.q75.getImageQuality();
            }
            this.lowNetQ = str;
        }

        public String getHighNetQ() {
            return this.highNetQ;
        }

        public void setHighNetQ(String str) {
            if (TextUtils.isEmpty(str)) {
                str = ImageQuality.q90.getImageQuality();
            }
            this.highNetQ = str;
        }

        public String getLowNetSharpen() {
            return this.lowNetSharpen;
        }

        public void setLowNetSharpen(String str) {
            if (TextUtils.isEmpty(str)) {
                str = this.lowNetSharpen;
            }
            this.lowNetSharpen = str;
        }

        public String getHighNetSharpen() {
            return this.highNetSharpen;
        }

        public void setHighNetSharpen(String str) {
            if (TextUtils.isEmpty(str)) {
                str = this.highNetSharpen;
            }
            this.highNetSharpen = str;
        }

        public double getLowNetScale() {
            return this.lowNetScale;
        }

        public void setLowNetScale(double d) {
            if (d <= 0.0d) {
                d = this.lowNetScale;
            }
            this.lowNetScale = d;
        }

        public double getHighNetScale() {
            return this.highNetScale;
        }

        public void setHighNetScale(double d) {
            if (d <= 0.0d) {
                d = this.highNetScale;
            }
            this.highNetScale = d;
        }
    }

    public static class ImageSize {
        public int height;
        public boolean keep;
        public boolean limitWH;
        public int width;

        public ImageSize(boolean z, boolean z2, int i, int i2) {
            this.keep = z;
            this.limitWH = z2;
            this.width = i;
            this.height = i2;
        }

        public ImageSize(int i, int i2) {
            this(false, false, i, i2);
        }
    }

    public static class UriCDNInfo {
        public String host;
        public Uri uri;
        public final String url;

        public UriCDNInfo(String str) {
            this.url = str;
            if (ImageStrategyConfig.isUseOptimize) {
                this.host = getHostFromPath(str);
                return;
            }
            this.uri = Uri.parse(str);
            if (this.uri == null || this.uri.getHost() == null) {
                this.host = "";
            } else {
                this.host = this.uri.getHost();
            }
        }

        private static String getHostFromPath(String str) {
            int i;
            if (TextUtils.isEmpty(str)) {
                return "";
            }
            if (str.startsWith("//")) {
                i = 2;
            } else {
                int indexOf = str.indexOf(HttpConstant.SCHEME_SPLIT);
                i = indexOf < 0 ? 0 : indexOf + 3;
            }
            if (i >= str.length()) {
                return "";
            }
            int indexOf2 = str.indexOf(47, i);
            if (indexOf2 < 0) {
                indexOf2 = str.length();
            }
            return str.substring(i, indexOf2);
        }
    }
}
