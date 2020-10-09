package com.taobao.tao.util;

import android.os.Build;
import android.text.TextUtils;
import com.taobao.tao.image.ImageStrategyConfig;
import com.taobao.tao.image.Logger;
import com.taobao.tao.util.ImageStrategyExtra;
import com.taobao.tao.util.TaobaoImageUrlStrategy;
import com.taobao.weex.ui.component.WXComponent;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OssImageUrlStrategy {
    private static final String[] DEFAULT_FUZZY_EXCLUDES = {"getAvatar", "@watermark"};
    private static final String[] DEFAULT_OSS_DOMAINS = {"ossgw.alicdn.com"};
    public static final char FIRST_LEVEL_CONCAT = '@';
    private static final String GIF_EXTEND = ".gif";
    private static final String JPEG_EXTEND = ".jpg";
    private static final String SECOND_LEVEL_CONCAT = "_";
    private static final String SHARPEN_IMAGE = "1sh";
    private static final String SMALL_THAN_ORIGIN = "1l";
    private static final String WEBP_EXTEND = ".webp";
    private static final String WHITE_FILL = "1wh";
    private static OssImageUrlStrategy sInstance;
    private Pattern mCdnRuleRegex;
    private String[] mFuzzyExcludePath = DEFAULT_FUZZY_EXCLUDES;
    private String[] mOssDomains = DEFAULT_OSS_DOMAINS;
    private final ReentrantReadWriteLock mRWLock = new ReentrantReadWriteLock();

    public static synchronized OssImageUrlStrategy getInstance() {
        OssImageUrlStrategy ossImageUrlStrategy;
        synchronized (OssImageUrlStrategy.class) {
            if (sInstance == null) {
                sInstance = new OssImageUrlStrategy();
            }
            ossImageUrlStrategy = sInstance;
        }
        return ossImageUrlStrategy;
    }

    public void setupConfigs(String[] strArr, String[] strArr2) {
        this.mRWLock.writeLock().lock();
        if (strArr != null) {
            try {
                if (strArr.length > 0) {
                    this.mOssDomains = strArr;
                }
            } catch (Throwable th) {
                this.mRWLock.writeLock().unlock();
                throw th;
            }
        }
        if (strArr2 != null && strArr2.length > 0) {
            this.mFuzzyExcludePath = strArr2;
        }
        this.mRWLock.writeLock().unlock();
    }

    /* JADX INFO: finally extract failed */
    public boolean isOssDomain(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        this.mRWLock.readLock().lock();
        try {
            if (this.mOssDomains != null) {
                for (String indexOf : this.mOssDomains) {
                    if (str.indexOf(indexOf) >= 0) {
                        this.mRWLock.readLock().unlock();
                        return true;
                    }
                }
            }
            this.mRWLock.readLock().unlock();
            return false;
        } catch (Throwable th) {
            this.mRWLock.readLock().unlock();
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    public boolean isFuzzyExclude(String str) {
        this.mRWLock.readLock().lock();
        try {
            if (this.mFuzzyExcludePath != null) {
                for (String indexOf : this.mFuzzyExcludePath) {
                    if (str.indexOf(indexOf) >= 0) {
                        this.mRWLock.readLock().unlock();
                        return true;
                    }
                }
            }
            this.mRWLock.readLock().unlock();
            return false;
        } catch (Throwable th) {
            this.mRWLock.readLock().unlock();
            throw th;
        }
    }

    public String decideUrl(String str, int i, ImageStrategyConfig imageStrategyConfig) {
        if (isFuzzyExclude(str)) {
            Logger.d(Logger.COMMON_TAG, "[OSS] fuzzy exclude, url=%s", str);
            return str;
        }
        String str2 = "";
        ImageStrategyExtra.ImageUrlInfo parseOssRule = parseOssRule(str);
        StringBuilder sb = new StringBuilder(parseOssRule.base.length() + 26);
        sb.append(parseOssRule.base);
        sb.append('@');
        decideWH(parseOssRule, imageStrategyConfig, i);
        if (parseOssRule.width > 0) {
            sb.append(str2);
            sb.append(parseOssRule.width);
            sb.append(WXComponent.PROP_FS_WRAP_CONTENT);
            str2 = "_";
        }
        if (parseOssRule.height > 0) {
            sb.append(str2);
            sb.append(parseOssRule.height);
            sb.append("h");
            str2 = "_";
        }
        decideQuality(parseOssRule, imageStrategyConfig);
        if (!TextUtils.isEmpty(parseOssRule.quality)) {
            sb.append(str2);
            sb.append(parseOssRule.quality);
            str2 = "_";
        }
        if (decideSharpen(parseOssRule, imageStrategyConfig)) {
            sb.append(str2);
            sb.append(parseOssRule.sharpen);
            str2 = "_";
        }
        if (decideCut(parseOssRule, imageStrategyConfig)) {
            sb.append(str2);
            sb.append(parseOssRule.cj);
            str2 = "_";
        }
        sb.append(str2);
        sb.append(SMALL_THAN_ORIGIN);
        decideExtend(parseOssRule, imageStrategyConfig);
        if (TextUtils.isEmpty(parseOssRule.ext)) {
            sb.append("_");
            sb.append(WHITE_FILL);
            sb.append(JPEG_EXTEND);
        } else {
            sb.append(parseOssRule.ext);
        }
        sb.append(parseOssRule.suffix);
        String substring = sb.substring(0);
        Logger.d(Logger.COMMON_TAG, "[OSS] origin url=%s\ndecide url=%s", str, substring);
        return substring;
    }

    private void decideWH(ImageStrategyExtra.ImageUrlInfo imageUrlInfo, ImageStrategyConfig imageStrategyConfig, int i) {
        int i2;
        TaobaoImageUrlStrategy instance = TaobaoImageUrlStrategy.getInstance();
        if (instance.isNetworkSlow()) {
            double dip = (double) (((float) i) * instance.getDip());
            Double.isNaN(dip);
            i2 = (int) (dip * 0.7d);
        } else {
            i2 = (int) (((float) i) * instance.getDip());
        }
        if (imageStrategyConfig.getFinalWidth() > 0 && imageStrategyConfig.getFinalHeight() > 0) {
            imageUrlInfo.width = imageStrategyConfig.getFinalWidth();
            imageUrlInfo.height = imageStrategyConfig.getFinalHeight();
        } else if ((imageStrategyConfig.getSizeLimitType() != ImageStrategyConfig.SizeLimitType.ALL_LIMIT || imageUrlInfo.width <= 0 || imageUrlInfo.height <= 0) && i2 >= 0) {
            int taobaoCDNPatten = instance.taobaoCDNPatten(i2, true, !isConfigDisabled(imageStrategyConfig.isEnabledLevelModel()));
            switch (imageStrategyConfig.getSizeLimitType()) {
                case WIDTH_LIMIT:
                    imageUrlInfo.width = taobaoCDNPatten;
                    imageUrlInfo.height = 0;
                    return;
                case HEIGHT_LIMIT:
                    imageUrlInfo.width = 0;
                    imageUrlInfo.height = taobaoCDNPatten;
                    return;
                case ALL_LIMIT:
                    imageUrlInfo.height = taobaoCDNPatten;
                    imageUrlInfo.width = taobaoCDNPatten;
                    return;
                default:
                    return;
            }
        }
    }

    private boolean decideQuality(ImageStrategyExtra.ImageUrlInfo imageUrlInfo, ImageStrategyConfig imageStrategyConfig) {
        if (isConfigDisabled(imageStrategyConfig.isEnabledQuality()) || imageStrategyConfig.getFinalImageQuality() == TaobaoImageUrlStrategy.ImageQuality.non) {
            return false;
        }
        if (imageStrategyConfig.getFinalImageQuality() != null) {
            imageUrlInfo.quality = imageStrategyConfig.getFinalImageQuality().getOssQuality();
            return true;
        } else if (TaobaoImageUrlStrategy.getInstance().isNetworkSlow()) {
            imageUrlInfo.quality = TaobaoImageUrlStrategy.ImageQuality.q75.getOssQuality();
            return true;
        } else {
            imageUrlInfo.quality = TaobaoImageUrlStrategy.ImageQuality.q90.getOssQuality();
            return true;
        }
    }

    private boolean decideSharpen(ImageStrategyExtra.ImageUrlInfo imageUrlInfo, ImageStrategyConfig imageStrategyConfig) {
        if (isConfigDisabled(imageStrategyConfig.isEnabledSharpen()) || !TaobaoImageUrlStrategy.getInstance().isNetworkSlow()) {
            return false;
        }
        imageUrlInfo.sharpen = SHARPEN_IMAGE;
        return true;
    }

    private static boolean decideCut(ImageStrategyExtra.ImageUrlInfo imageUrlInfo, ImageStrategyConfig imageStrategyConfig) {
        if (imageStrategyConfig.getCutType() == null || imageStrategyConfig.getCutType() == TaobaoImageUrlStrategy.CutType.non) {
            return false;
        }
        imageUrlInfo.cj = imageStrategyConfig.getCutType().getOssCut();
        return true;
    }

    private void decideExtend(ImageStrategyExtra.ImageUrlInfo imageUrlInfo, ImageStrategyConfig imageStrategyConfig) {
        if (!GIF_EXTEND.equals(imageUrlInfo.ext)) {
            boolean z = ImageStrategyConfig.isWebpDegrade && Build.VERSION.SDK_INT == 28;
            if (imageStrategyConfig.isForcedWebPOn() || (!isConfigDisabled(imageStrategyConfig.isEnabledWebP()) && TaobaoImageUrlStrategy.getInstance().isSupportWebP() && !imageUrlInfo.suffix.contains("imgwebptag=0"))) {
                if (!z) {
                    imageUrlInfo.ext = WEBP_EXTEND;
                }
            } else if (WEBP_EXTEND.equals(imageUrlInfo.ext)) {
                imageUrlInfo.ext = null;
            }
        }
    }

    private boolean isConfigDisabled(Boolean bool) {
        return bool != null && !bool.booleanValue();
    }

    private ImageStrategyExtra.ImageUrlInfo parseOssRule(String str) {
        ImageStrategyExtra.ImageUrlInfo baseUrlInfo = ImageStrategyExtra.getBaseUrlInfo(str);
        String str2 = baseUrlInfo.base;
        int lastIndexOf = str2.lastIndexOf(64);
        if (lastIndexOf < 0) {
            return baseUrlInfo;
        }
        if (this.mCdnRuleRegex == null) {
            this.mCdnRuleRegex = Pattern.compile(String.format("%s(?:(?:%s?(\\d+)w[%s\\.])|(?:%s?(\\d+)w$)|(?:%s?(\\d+)h)|(?:%s?(\\d+[Qq]))|(?:%s?[^%s.]+))+", new Object[]{'@', "_", "_", "_", "_", "_", "_", "_"}));
        }
        Matcher matcher = this.mCdnRuleRegex.matcher(str2);
        baseUrlInfo.base = str2.substring(0, lastIndexOf);
        if (!matcher.find(lastIndexOf) || matcher.groupCount() < 4) {
            return baseUrlInfo;
        }
        try {
            String group = matcher.group(1);
            if (group == null) {
                group = matcher.group(2);
            }
            String group2 = matcher.group(3);
            String group3 = matcher.group(4);
            if (!TextUtils.isEmpty(group)) {
                baseUrlInfo.width = Integer.parseInt(group);
            }
            if (!TextUtils.isEmpty(group2)) {
                baseUrlInfo.height = Integer.parseInt(group2);
            }
            if (!TextUtils.isEmpty(group3)) {
                baseUrlInfo.quality = group3;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Logger.e(Logger.COMMON_TAG, "ImageStrategyExtra parseImageUrl convert number error:%s", e.getMessage());
        }
        return baseUrlInfo;
    }
}
