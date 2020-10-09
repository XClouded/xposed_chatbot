package com.taobao.android.dinamic;

import com.taobao.android.dinamic.constructor.DImageViewConstructor;
import com.taobao.android.dinamic.dinamic.AbsDinamicEventHandler;
import com.taobao.android.dinamic.dinamic.AbsDinamicMonitor;
import com.taobao.android.dinamic.dinamic.DinamicAppMonitor;
import com.taobao.android.dinamic.dinamic.DinamicPerformMonitor;
import com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor;
import com.taobao.android.dinamic.exception.DinamicException;
import com.taobao.android.dinamic.expression.parser.AbsDinamicDataParser;
import com.taobao.android.dinamic.expression.parser.DinamicDataParserFactory;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.log.IDinamicLog;
import com.taobao.android.dinamic.log.IDinamicRemoteDebugLog;
import com.taobao.android.dinamic.tempate.DTemplateManager;
import com.taobao.android.dinamic.tempate.manager.TemplateCache;

public class DRegisterCenter {
    private static final DRegisterCenter instance = new DRegisterCenter();
    private DinamicAppMonitor appMonitor;
    private DinamicPerformMonitor dinamicPerformMonitor;
    private DImageViewConstructor.DXWebImageInterface dxWebImageInterface;
    private TemplateCache.HttpLoader httpLoader;
    private AbsDinamicMonitor monitor;

    @Deprecated
    public void registerLogger(IDinamicLog iDinamicLog) {
    }

    public static DRegisterCenter shareCenter() {
        return instance;
    }

    public void registerViewConstructor(String str, DinamicViewAdvancedConstructor dinamicViewAdvancedConstructor) throws DinamicException {
        DinamicViewHelper.registerViewConstructor(str, dinamicViewAdvancedConstructor);
    }

    public void registerDataParser(String str, AbsDinamicDataParser absDinamicDataParser) throws DinamicException {
        DinamicDataParserFactory.registerParser(str, absDinamicDataParser);
    }

    public void registerEventHandler(String str, AbsDinamicEventHandler absDinamicEventHandler) throws DinamicException {
        DinamicViewHelper.registerEventHandler(str, absDinamicEventHandler);
    }

    public void registerViewConstructor(String str, String str2, DinamicViewAdvancedConstructor dinamicViewAdvancedConstructor) throws DinamicException {
        if (!"detail".equals(str) || (!"XAdaptiveTextView".equals(str2) && !"XCommentTagView".equals(str2) && !"XSimpleRichText".equals(str2) && !"XRichText".equals(str2) && !"XWrapTagView".equals(str2) && !"XRichTextByCoupon".equals(str2) && !"XCategoryCoupon".equals(str2))) {
            DinamicViewHelper.registerViewConstructor(str2, dinamicViewAdvancedConstructor);
        } else {
            DinamicViewHelper.registerReplaceViewConstructor(str2, dinamicViewAdvancedConstructor);
        }
    }

    public void registerDataParser(String str, String str2, AbsDinamicDataParser absDinamicDataParser) throws DinamicException {
        if (!"detail".equals(str) || (!"tenary".equals(str2) && !"strcat".equals(str2) && !"xtrim".equals(str2) && !"equals".equals(str2))) {
            DinamicDataParserFactory.registerParser(str2, absDinamicDataParser);
        } else {
            DinamicDataParserFactory.registerReplaceParser(str2, absDinamicDataParser);
        }
    }

    public void registerEventHandler(String str, String str2, AbsDinamicEventHandler absDinamicEventHandler) throws DinamicException {
        if ("detail".equals(str)) {
            if ("xTap".equals(str2) || "xCopy".equals(str2)) {
                DinamicViewHelper.registerReplaceEventHandler(str2, absDinamicEventHandler);
                return;
            }
        } else if ("mcCart".equals(str) && "mcAddCart".equals(str2)) {
            DinamicViewHelper.registerReplaceEventHandler(str2, absDinamicEventHandler);
            return;
        }
        DinamicViewHelper.registerEventHandler(str2, absDinamicEventHandler);
    }

    public void registerHttpLoader(TemplateCache.HttpLoader httpLoader2) {
        if (this.httpLoader != null) {
            DinamicLog.e("registerHttpLoader failed, loader is exist", new String[0]);
            return;
        }
        this.httpLoader = httpLoader2;
        DTemplateManager.defaultTemplateManager().registerHttpLoader(httpLoader2);
    }

    public void registerRemoteDebugLog(IDinamicRemoteDebugLog iDinamicRemoteDebugLog) {
        if (DinamicLog.iDinamicRemoteDebugLog != null) {
            DinamicLog.e("registerRemoteDebugLog failed, iDinamicRemoteDebugLog is exist", new String[0]);
        } else {
            DinamicLog.setDinamicRemoteDebugLog(iDinamicRemoteDebugLog);
        }
    }

    public void registerMonitor(AbsDinamicMonitor absDinamicMonitor) {
        if (this.monitor == null) {
            this.monitor = absDinamicMonitor;
        } else {
            DinamicLog.e("registerMonitor failed, monitor is exist", new String[0]);
        }
    }

    public void registerImageInterface(DImageViewConstructor.DXWebImageInterface dXWebImageInterface) {
        if (this.dxWebImageInterface != null) {
            DinamicLog.e("registerImageInterface failed, imageInterface is exist", new String[0]);
            return;
        }
        this.dxWebImageInterface = dXWebImageInterface;
        DImageViewConstructor dImageViewConstructor = (DImageViewConstructor) Dinamic.getViewConstructor(DinamicConstant.D_IMAGE_VIEW);
        if (dImageViewConstructor != null) {
            dImageViewConstructor.setDxWebImageInterface(dXWebImageInterface);
        }
    }

    public void registerAppMonitor(DinamicAppMonitor dinamicAppMonitor) {
        if (this.dinamicPerformMonitor == null) {
            this.dinamicPerformMonitor = new DinamicPerformMonitor(dinamicAppMonitor);
        }
        this.appMonitor = dinamicAppMonitor;
    }

    public DinamicPerformMonitor getPerformMonitor() {
        return this.dinamicPerformMonitor;
    }

    public AbsDinamicMonitor getMonitor() {
        return this.monitor;
    }

    public DinamicAppMonitor getAppMonitor() {
        return this.appMonitor;
    }

    public DImageViewConstructor.DXWebImageInterface getDxWebImageInterface() {
        return this.dxWebImageInterface;
    }

    public TemplateCache.HttpLoader getHttpLoader() {
        return this.httpLoader;
    }
}
