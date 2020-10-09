package com.taobao.tao.image;

import com.taobao.tao.util.TaobaoImageUrlStrategy;

public class ImageStrategyConfig {
    public static final String BALA = "bala";
    public static final String DEFAULT = "default";
    public static final String DETAIL = "detail";
    public static final String HOME = "home";
    public static final String SEARCH = "search";
    public static final String SHOP = "shop";
    public static final String TBCHANNEL = "tbchannel";
    public static final String WEAPP = "weapp";
    public static final String WEAPPSHARPEN = "weappsharpen";
    public static final String WEITAO = "weitao";
    public static boolean isUseOptimize;
    public static boolean isUseSpecialDomain;
    public static boolean isWebpDegrade;
    int bizId;
    String bizIdStr;
    String bizName;
    TaobaoImageUrlStrategy.CutType cutType;
    Boolean enabledLevelModel;
    Boolean enabledMergeDomain;
    Boolean enabledQuality;
    Boolean enabledSharpen;
    Boolean enabledWebP;
    int finalHeight;
    TaobaoImageUrlStrategy.ImageQuality finalImageQuality;
    int finalWidth;
    boolean forcedWebPOn;
    Boolean shortScale;
    SizeLimitType sizeLimitType;
    boolean skipped;

    public enum SizeLimitType {
        WIDTH_LIMIT,
        HEIGHT_LIMIT,
        ALL_LIMIT
    }

    public static Builder newBuilderWithName(String str, int i) {
        return new Builder(str, i);
    }

    public static Builder newBuilderWithName(String str) {
        return new Builder(str, 0);
    }

    public static Builder newBuilderWithName(String str, String str2) {
        return new Builder(str, str2);
    }

    private ImageStrategyConfig(Builder builder) {
        this.bizName = builder.bizName;
        this.bizIdStr = builder.bizIdStr;
        this.bizId = builder.bizId;
        this.skipped = builder.skipped;
        this.finalWidth = builder.finalWidth;
        this.finalHeight = builder.finalHeight;
        this.cutType = builder.cutType;
        this.enabledWebP = builder.enabledWebP;
        this.enabledQuality = builder.enabledQuality;
        this.enabledSharpen = builder.enabledSharpen;
        this.enabledMergeDomain = builder.enabledMergeDomain;
        this.enabledLevelModel = builder.enabledLevelModel;
        this.finalImageQuality = builder.finalImageQuality;
        this.shortScale = Boolean.valueOf(builder.shortScale);
        if (builder.forcedWebPOn != null) {
            this.forcedWebPOn = builder.forcedWebPOn.booleanValue();
        }
        this.sizeLimitType = builder.sizeLimitType;
        if (this.sizeLimitType == null) {
            this.sizeLimitType = SizeLimitType.ALL_LIMIT;
        } else if (this.sizeLimitType == SizeLimitType.WIDTH_LIMIT) {
            this.finalHeight = 10000;
            this.finalWidth = 0;
        } else if (this.sizeLimitType == SizeLimitType.HEIGHT_LIMIT) {
            this.finalHeight = 0;
            this.finalWidth = 10000;
        }
    }

    public final String toString() {
        return String.valueOf(this.bizId);
    }

    public String report() {
        StringBuilder sb = new StringBuilder(300);
        sb.append("ImageStrategyConfig@");
        sb.append(hashCode());
        sb.append("\n");
        sb.append("bizName:");
        sb.append(this.bizName);
        sb.append("\n");
        sb.append("bizId:");
        sb.append(this.bizId);
        sb.append("\n");
        sb.append("skipped:");
        sb.append(this.skipped);
        sb.append("\n");
        sb.append("finalWidth:");
        sb.append(this.finalWidth);
        sb.append("\n");
        sb.append("finalHeight:");
        sb.append(this.finalHeight);
        sb.append("\n");
        sb.append("cutType:");
        sb.append(this.cutType);
        sb.append("\n");
        sb.append("enabledWebP:");
        sb.append(this.enabledWebP);
        sb.append("\n");
        sb.append("enabledQuality:");
        sb.append(this.enabledQuality);
        sb.append("\n");
        sb.append("enabledSharpen:");
        sb.append(this.enabledSharpen);
        sb.append("\n");
        sb.append("enabledMergeDomain:");
        sb.append(this.enabledMergeDomain);
        sb.append("\n");
        sb.append("enabledLevelModel:");
        sb.append(this.enabledLevelModel);
        sb.append("\n");
        sb.append("finalImageQuality:");
        sb.append(this.finalImageQuality);
        sb.append("\n");
        sb.append("forcedWebPOn:");
        sb.append(this.forcedWebPOn);
        sb.append("\n");
        sb.append("sizeLimitType:");
        sb.append(this.sizeLimitType);
        return sb.toString();
    }

    public boolean isSkipped() {
        return this.skipped;
    }

    public String getName() {
        return this.bizName;
    }

    public int getBizId() {
        return this.bizId;
    }

    public String getBizIdStr() {
        return this.bizIdStr;
    }

    public int getFinalWidth() {
        return this.finalWidth;
    }

    public int getFinalHeight() {
        return this.finalHeight;
    }

    public TaobaoImageUrlStrategy.CutType getCutType() {
        return this.cutType;
    }

    public Boolean isEnabledWebP() {
        return this.enabledWebP;
    }

    public boolean isForcedWebPOn() {
        return this.forcedWebPOn;
    }

    public Boolean isEnabledQuality() {
        return this.enabledQuality;
    }

    public Boolean isEnabledSharpen() {
        return this.enabledSharpen;
    }

    public Boolean isEnabledMergeDomain() {
        return this.enabledMergeDomain;
    }

    public Boolean isEnabledLevelModel() {
        return this.enabledLevelModel;
    }

    public Boolean isShortEdgeEnable() {
        return this.shortScale;
    }

    public TaobaoImageUrlStrategy.ImageQuality getFinalImageQuality() {
        return this.finalImageQuality;
    }

    public SizeLimitType getSizeLimitType() {
        return this.sizeLimitType;
    }

    public static class Builder {
        int bizId;
        String bizIdStr;
        String bizName;
        TaobaoImageUrlStrategy.CutType cutType;
        Boolean enabledLevelModel;
        Boolean enabledMergeDomain;
        Boolean enabledQuality;
        Boolean enabledSharpen;
        Boolean enabledWebP;
        int finalHeight = -1;
        TaobaoImageUrlStrategy.ImageQuality finalImageQuality;
        int finalWidth = -1;
        Boolean forcedWebPOn;
        boolean shortScale;
        SizeLimitType sizeLimitType;
        boolean skipped;

        public Builder(String str, int i) {
            this.bizName = str;
            this.bizIdStr = "";
            this.bizId = i;
        }

        public Builder(String str, String str2) {
            this.bizName = str;
            this.bizIdStr = str2;
            this.bizId = 0;
        }

        public Builder skip(boolean z) {
            this.skipped = z;
            return this;
        }

        public Builder enableShortEdgeScale(boolean z) {
            this.shortScale = z;
            return this;
        }

        public Builder setFinalWidth(int i) {
            this.finalWidth = i;
            return this;
        }

        public Builder setFinalHeight(int i) {
            this.finalHeight = i;
            return this;
        }

        public Builder setCutType(TaobaoImageUrlStrategy.CutType cutType2) {
            this.cutType = cutType2;
            return this;
        }

        public Builder enableWebP(boolean z) {
            this.enabledWebP = Boolean.valueOf(z);
            return this;
        }

        public Builder setSizeLimitType(SizeLimitType sizeLimitType2) {
            this.sizeLimitType = sizeLimitType2;
            return this;
        }

        public Builder forceWebPOn(boolean z) {
            this.forcedWebPOn = Boolean.valueOf(z);
            return this;
        }

        public Builder enableQuality(boolean z) {
            this.enabledQuality = Boolean.valueOf(z);
            return this;
        }

        public Builder enableSharpen(boolean z) {
            this.enabledSharpen = Boolean.valueOf(z);
            return this;
        }

        public Builder enableMergeDomain(boolean z) {
            this.enabledMergeDomain = Boolean.valueOf(z);
            return this;
        }

        public Builder enableLevelModel(boolean z) {
            this.enabledLevelModel = Boolean.valueOf(z);
            return this;
        }

        public Builder setFinalImageQuality(TaobaoImageUrlStrategy.ImageQuality imageQuality) {
            this.finalImageQuality = imageQuality;
            return this;
        }

        public ImageStrategyConfig build() {
            return new ImageStrategyConfig(this);
        }
    }
}
