package android.taobao.windvane.packageapp.zipapp.data;

import android.support.v4.media.session.PlaybackStateCompat;
import android.taobao.windvane.config.EnvEnum;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.packageapp.WVPackageAppService;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import com.taobao.ju.track.constants.Constants;

import java.util.ArrayList;

public class ZipAppInfo {
    private ZipAppTypeEnum appType;
    public String errorCode;
    public long f = 5;
    public ArrayList<String> folders = new ArrayList<>();
    public long installedSeq = 0;
    public String installedVersion = Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
    public boolean isDamage = false;
    public boolean isInUse;
    public boolean isInstantApp = false;
    public boolean isOptional = false;
    public boolean isPreViewApp = false;
    public ArrayList<String> localFolders = new ArrayList<>();
    public String mappingUrl = "";
    public String name = "";
    public long s = 0;
    public int status = -1;
    public long t = 0;
    public int tempPriority;
    private ZipUpdateInfoEnum updateInfo;
    private ZipUpdateTypeEnum updateType;
    public String v = "";
    public String z = "";

    public int getPriority() {
        return (int) (this.f & 15);
    }

    public ZipAppTypeEnum getAppType() {
        for (ZipAppTypeEnum zipAppTypeEnum : ZipAppTypeEnum.values()) {
            if (zipAppTypeEnum.getValue() == (this.f & 240)) {
                this.appType = zipAppTypeEnum;
                if (zipAppTypeEnum == ZipAppTypeEnum.ZIP_APP_TYPE_MINI_APP) {
                    this.isInstantApp = true;
                    this.appType = ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP;
                }
                return this.appType;
            }
        }
        return ZipAppTypeEnum.ZIP_APP_TYPE_UNKNOWN;
    }

    public ZipUpdateTypeEnum getUpdateType() {
        for (ZipUpdateTypeEnum zipUpdateTypeEnum : ZipUpdateTypeEnum.values()) {
            if (zipUpdateTypeEnum.getValue() == (this.f & 3840)) {
                this.updateType = zipUpdateTypeEnum;
                return this.updateType;
            }
        }
        return ZipUpdateTypeEnum.ZIP_UPDATE_TYPE_PASSIVE;
    }

    public ZipUpdateInfoEnum getInfo() {
        for (ZipUpdateInfoEnum zipUpdateInfoEnum : ZipUpdateInfoEnum.values()) {
            if (zipUpdateInfoEnum.getValue() == (this.f & 12288)) {
                this.updateInfo = zipUpdateInfoEnum;
                return this.updateInfo;
            }
        }
        return ZipUpdateInfoEnum.ZIP_APP_TYPE_NORMAL;
    }

    public boolean getIs2GUpdate() {
        return (this.f & PlaybackStateCompat.ACTION_PREPARE) != 0;
    }

    public boolean getIs3GUpdate() {
        return (this.f & PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID) != 0;
    }

    public String genMidPath(boolean z2) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append("/");
        sb.append((!z2 && !Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE.equals(this.installedVersion)) ? this.installedVersion : this.v);
        return sb.toString();
    }

    public String getNameandVersion() {
        return this.name + "_" + this.v;
    }

    public String getNameAndSeq() {
        return this.name + "-" + this.installedSeq;
    }

    public boolean isAppInstalled() {
        return (0 == this.installedSeq || this.status == ZipAppConstants.ZIP_REMOVED) ? false : true;
    }

    public boolean equals(ZipAppInfo zipAppInfo) {
        if (this.v != null && zipAppInfo != null && zipAppInfo.v != null && !this.v.equals(zipAppInfo.v)) {
            return false;
        }
        if (zipAppInfo == null || this.s == zipAppInfo.s) {
            return true;
        }
        return false;
    }

    public String getZipUrl() {
        if (this.z.contains("wapp")) {
            this.z = "";
        }
        WVPackageAppService.IPackageZipPrefixAdapter packageZipPrefixAdapter = WVPackageAppService.getPackageZipPrefixAdapter();
        if (packageZipPrefixAdapter != null) {
            String packageZipPrefix = packageZipPrefixAdapter.getPackageZipPrefix(GlobalConfig.env, this.isPreViewApp);
            if (!TextUtils.isEmpty(packageZipPrefix)) {
                this.z = packageZipPrefix;
                TaoLog.d("ZipURL", "Zip url by app config: [" + this.name + "] " + packageZipPrefix);
            }
        }
        if (TextUtils.isEmpty(this.z)) {
            if (this.isPreViewApp && (isAppInstalled() || this.isInstantApp)) {
                this.isPreViewApp = false;
            }
            if (!this.isPreViewApp) {
                if (TextUtils.isEmpty(WVCommonConfig.commonConfig.packageZipPrefix)) {
                    switch (GlobalConfig.env) {
                        case ONLINE:
                            this.z = "https://ossgw.alicdn.com/awp/h5.m.taobao.com/";
                            break;
                        case PRE:
                            this.z = "http://h5.wapa.taobao.com/";
                            break;
                        case DAILY:
                            this.z = "http://h5.waptest.taobao.com/";
                            break;
                        default:
                            this.z = "https://ossgw.alicdn.com/awp/h5.m.taobao.com/";
                            break;
                    }
                } else {
                    this.z = WVCommonConfig.commonConfig.packageZipPrefix;
                }
            } else if (TextUtils.isEmpty(WVCommonConfig.commonConfig.packageZipPreviewPrefix)) {
                switch (GlobalConfig.env) {
                    case ONLINE:
                        this.z = "http://wapp.m.taobao.com/";
                        break;
                    case PRE:
                        this.z = "http://wapp.wapa.taobao.com/";
                        break;
                    case DAILY:
                        this.z = "http://wapp.waptest.taobao.com/";
                        break;
                    default:
                        this.z = "http://wapp.m.taobao.com/";
                        break;
                }
            } else {
                this.z = WVCommonConfig.commonConfig.packageZipPreviewPrefix;
            }
        }
        StringBuilder sb = new StringBuilder(this.z);
        if ('/' != sb.charAt(sb.length() - 1)) {
            sb.append("/");
        }
        sb.append("app/");
        sb.append(this.name);
        sb.append("/app-");
        sb.append(this.s);
        if (!this.isPreViewApp && !GlobalConfig.env.equals(EnvEnum.PRE) && this.v.equals(this.installedVersion) && this.s != this.installedSeq) {
            sb.append("-incr");
        }
        sb.append(".zip");
        return sb.toString();
    }
}
