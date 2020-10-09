package com.alimama.union.app.logger;

import alimama.com.unwetaologger.base.ErrorContent;
import alimama.com.unwetaologger.base.LogContent;
import alimama.com.unwetaologger.base.UNWLoggerManager;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;

public class BusinessMonitorLogger {

    public static class WithdrawCash {
        private static final String MODULE = "withdraw_cash";

        public static void doWithdraw(String str, String str2) {
            LogContent logContent = new LogContent();
            logContent.setName(str);
            logContent.setPoint("doWithdraw");
            logContent.setMsg(str2);
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).info(logContent);
        }

        public static void getAuthenticationCode(String str) {
            LogContent logContent = new LogContent();
            logContent.setName(str);
            logContent.setPoint("getAuthenticationCode");
            logContent.setMsg("click the get authentication code btn");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).info(logContent);
        }

        public static void receiveAuthenticationCodeFailed(String str) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setName(str);
            errorContent.setPoint("receiveAuthenticationCodeFailed");
            errorContent.setErrorCode(String.valueOf(MonitorErrorCode.WITH_DRAW_RECEIVE_AUTHENTICATION_CODE_FAILED));
            errorContent.setMsg("-8002, click the btn of could not receive authentication code");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void submit(String str, String str2) {
            LogContent logContent = new LogContent();
            logContent.setName(str);
            logContent.setPoint(ProtocolConst.KEY_SUBMIT);
            logContent.setMsg(str2);
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).info(logContent);
        }

        public static void submitFailed(String str, String str2, String str3) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setName(str);
            errorContent.setPoint(str2);
            errorContent.setMsg("-8001, submit fail, " + str3);
            errorContent.setErrorCode(String.valueOf(MonitorErrorCode.WITH_DRAW_FAIL_SERVER_EXCEPTION));
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void withdrawSuccess(String str, double d, double d2, String str2) {
            LogContent logContent = new LogContent();
            logContent.setName(str);
            logContent.setPoint("withdrawSuccess");
            logContent.addInfoItem("amount", String.valueOf(d));
            logContent.addInfoItem("balance", String.valueOf(d2));
            logContent.addInfoItem("alipayAccount", str2);
            logContent.setMsg("withdraw successed");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).info(logContent);
        }
    }

    public static class TaoCodeTransfer {
        private static final String MODULE = "tao_code_transfer_module";

        public static void taoCodeTransferSuccess(String str, String str2) {
            LogContent logContent = new LogContent();
            logContent.setName(str);
            logContent.setPoint("taoCodeTransferSuccess");
            logContent.setMsg(str2);
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).info(logContent);
        }

        public static void taoCodeTransferFailed(String str, String str2, String str3) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setName(str);
            errorContent.setPoint("taoCodeTransferFailed");
            errorContent.setErrorCode(str2);
            errorContent.setMsg(str3);
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }
    }

    public static class Share {
        private static final String MODULE = "tao_code_share";

        public static void qrCodeLoadSuccess(String str) {
            LogContent logContent = new LogContent();
            logContent.setName(str);
            logContent.setPoint("qrCodeLoadSuccess");
            logContent.setMsg("制作拼图海报页面，二维码加载成功");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).info(logContent);
        }

        public static void qrCodeLoadFail(String str, String str2) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setName(str);
            errorContent.setPoint("qrCodeLoadFail");
            errorContent.setMsg("制作拼图海报页面，二维码加载失败, " + str2);
            errorContent.setErrorCode(String.valueOf(MonitorErrorCode.GENERATE_QR_CODE_FAILED_POSTER_VIEW));
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void switchQrCode(String str, boolean z) {
            LogContent logContent = new LogContent();
            logContent.setName(str);
            logContent.setPoint("switchQrCode");
            if (z) {
                logContent.setMsg("打开二维码开关");
            } else {
                logContent.setMsg("关闭二维码开关");
            }
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).info(logContent);
        }

        public static void savePosterSuccess(String str) {
            LogContent logContent = new LogContent();
            logContent.setName(str);
            logContent.setPoint("savePosterSuccess");
            logContent.setMsg("保存海报图片到本地成功");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).info(logContent);
        }

        public static void savePosterFailed(String str, String str2, String str3) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setName(str);
            errorContent.setPoint(" savePosterFailed");
            errorContent.setErrorCode(str3);
            errorContent.setMsg(str2 + " 保存图片到本地失败");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void createPosterSharingSuccess(String str, String str2) {
            LogContent logContent = new LogContent();
            logContent.setName(str);
            logContent.setPoint("createPosterSharingSuccess");
            logContent.setMsg(str2);
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).info(logContent);
        }

        public static void createPosterSharingFailed(String str) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setName(str);
            errorContent.setPoint("createPosterSharingFailed");
            errorContent.setErrorCode(String.valueOf(MonitorErrorCode.CREATE_POSTER_SHAREINT_FAILED));
            errorContent.setMsg("制作拼图海报页面，点击立即分享，创建分享失败");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void flutterViewCopyShareTextSuccess(String str, String str2) {
            LogContent logContent = new LogContent();
            logContent.setName(str);
            logContent.setPoint("flutterViewCopyShareTextSuccess");
            logContent.setMsg("flutter 分享页面，复制文案成功, " + str2);
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).info(logContent);
        }

        public static void flutterViewCopyShareTextFailed(String str, String str2) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setName(str);
            errorContent.setPoint("flutterViewCopyShareTextFailed");
            errorContent.setErrorCode(String.valueOf(MonitorErrorCode.FLUTTER_VIEW_COPY_SHARE_TEXT_FAILED));
            errorContent.setMsg("Flutter分享页面，复制文案失败" + str2);
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }

        public static void flutterViewShareToChannal(String str, String str2, String str3) {
            LogContent logContent = new LogContent();
            logContent.setName(str);
            logContent.setPoint("flutterViewShareToChannal");
            logContent.addInfoItem("channal", str2);
            logContent.setMsg(str3);
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).info(logContent);
        }
    }

    public static class Marketing {
        private static final String MODULE = "marketing";

        public static void show(String str) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setName(str);
            errorContent.setPoint("show");
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }
    }

    public static class Alipay {
        private static final String MODULE = "alipay";

        public static void payResult(String str, String str2) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setName(str);
            errorContent.setPoint("result");
            errorContent.setErrorCode(str2);
            UNWLoggerManager.getInstance().getLoggerByModule(MODULE).error(errorContent);
        }
    }

    public static class PriceParamDetect {
        private static final String MODULE = "fields";

        public static void show(String str, String str2, String str3) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setName(str);
            errorContent.setPoint("show");
            errorContent.addInfoItem("area", str2);
            errorContent.addInfoItem("fields", str3);
            UNWLoggerManager.getInstance().getLoggerByModule("fields").error(errorContent);
        }
    }

    public static class UnionRebate {
        private static final String MODULE = "union_rebate";

        public static void show(String str) {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setName(str);
            errorContent.setPoint("show");
            UNWLoggerManager.getInstance().getLoggerByModule("union_rebate").error(errorContent);
        }
    }
}
