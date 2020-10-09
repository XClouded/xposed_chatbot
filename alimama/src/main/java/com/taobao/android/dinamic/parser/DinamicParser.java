package com.taobao.android.dinamic.parser;

import com.taobao.android.dinamic.DRegisterCenter;
import com.taobao.android.dinamic.Dinamic;
import com.taobao.android.dinamic.dinamic.DinamicPerformMonitor;
import com.taobao.android.dinamic.log.DinamicLog;
import com.taobao.android.dinamic.log.DinamicLogThread;
import com.taobao.android.dinamic.tempate.DinamicTemplate;
import com.taobao.android.dinamic.view.DinamicError;
import com.taobao.android.dinamic.view.ViewResult;
import org.xmlpull.v1.XmlPullParser;

public class DinamicParser {
    private static AssetParser assetParser = new AssetParser();
    private static FileParser fileParser = new FileParser();
    private static ResParser resParser = new ResParser();
    private static SDCardFileParser sdCardFileParser = new SDCardFileParser();

    public static XmlPullParser getParser(String str, DinamicTemplate dinamicTemplate, ViewResult viewResult) {
        XmlPullParser openXmlResourceParser = (!Dinamic.isDebugable() || !sdCardFileParser.isFileExist(dinamicTemplate)) ? null : sdCardFileParser.openXmlResourceParser(str, dinamicTemplate, viewResult);
        long nanoTime = System.nanoTime();
        boolean z = false;
        if (!dinamicTemplate.isPreset()) {
            XmlPullParser openXmlResourceParser2 = fileParser.openXmlResourceParser(str, dinamicTemplate, viewResult);
            if (openXmlResourceParser2 != null) {
                z = true;
            }
            logReadFile(str, dinamicTemplate, z, System.nanoTime() - nanoTime);
            return openXmlResourceParser2;
        }
        if (openXmlResourceParser == null) {
            openXmlResourceParser = resParser.openXmlResourceParser(str, dinamicTemplate, viewResult);
        }
        if (openXmlResourceParser == null) {
            openXmlResourceParser = assetParser.openXmlResourceParser(str, dinamicTemplate, viewResult);
        }
        if (openXmlResourceParser != null) {
            z = true;
        }
        logReadFile(str, dinamicTemplate, z, System.nanoTime() - nanoTime);
        return openXmlResourceParser;
    }

    private static void logReadFile(String str, DinamicTemplate dinamicTemplate, boolean z, long j) {
        if (DRegisterCenter.shareCenter().getPerformMonitor() != null && DinamicLogThread.checkInit()) {
            final String str2 = str;
            final DinamicTemplate dinamicTemplate2 = dinamicTemplate;
            final long j2 = j;
            final boolean z2 = z;
            DinamicLogThread.threadHandler.postTask(new Runnable() {
                public void run() {
                    if (Dinamic.isDebugable()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("module=");
                        sb.append(str2);
                        sb.append("read File=");
                        sb.append(dinamicTemplate2);
                        double d = (double) ((float) j2);
                        Double.isNaN(d);
                        sb.append(d / 1000000.0d);
                        DinamicLog.d(Dinamic.TAG, sb.toString());
                    }
                    DinamicPerformMonitor performMonitor = DRegisterCenter.shareCenter().getPerformMonitor();
                    String str = str2;
                    DinamicTemplate dinamicTemplate = dinamicTemplate2;
                    boolean z = z2;
                    double d2 = (double) j2;
                    Double.isNaN(d2);
                    performMonitor.trackReadTemplate(str, dinamicTemplate, z, (DinamicError) null, d2 / 1000000.0d);
                }
            });
        }
    }
}
