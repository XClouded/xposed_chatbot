package com.taobao.tao.log.godeye.memorydump.dump;

import java.io.File;

public class MemoryFileZip {
    public static void deleteFile(String str) {
        if (str != null) {
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
