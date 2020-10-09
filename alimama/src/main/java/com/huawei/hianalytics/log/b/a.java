package com.huawei.hianalytics.log.b;

import com.taobao.tao.log.TLogInitializer;
import java.io.File;

public class a extends com.huawei.hianalytics.global.a {

    /* renamed from: com.huawei.hianalytics.log.b.a$a  reason: collision with other inner class name */
    public static class C0006a {
        public static final String a = (File.separator + "hianalytics" + File.separator + "applog");
        public static final String b;
        public static final String c = (a + File.separator + "logzips");
        public static final String d = (a + File.separator + "bigzip");

        static {
            StringBuilder sb = new StringBuilder();
            sb.append(a);
            sb.append(File.separator);
            sb.append(TLogInitializer.DEFAULT_DIR);
            b = sb.toString();
        }
    }
}
