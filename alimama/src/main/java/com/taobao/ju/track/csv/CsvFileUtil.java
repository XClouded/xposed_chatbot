package com.taobao.ju.track.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CsvFileUtil {
    private static final char CHAR = ',';
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private static final String FILE_EXTENSIONS = ".*.csv";

    public static boolean isCSV(File file) {
        return file != null && isCSV(file.getName());
    }

    public static boolean isCSV(String str) {
        return str != null && str.matches(FILE_EXTENSIONS);
    }

    public static String[] readHeaders(String str) {
        if (str == null) {
            return null;
        }
        return readHeaders(new File(str));
    }

    public static String[] readHeaders(File file) {
        return readHeaders(file, CHARSET);
    }

    public static String[] readHeaders(String str, Charset charset) {
        if (str == null) {
            return null;
        }
        return readHeaders(new File(str), charset);
    }

    public static String[] readHeaders(File file, Charset charset) {
        if (file == null || !file.exists()) {
            return null;
        }
        try {
            return readHeaders((InputStream) new FileInputStream(file), charset);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] readHeaders(InputStream inputStream) {
        return readHeaders(inputStream, CHARSET);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0021  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0028  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String[] readHeaders(java.io.InputStream r3, java.nio.charset.Charset r4) {
        /*
            r0 = 0
            if (r3 == 0) goto L_0x002c
            com.taobao.ju.track.csv.CsvReader r1 = new com.taobao.ju.track.csv.CsvReader     // Catch:{ Exception -> 0x001a, all -> 0x0017 }
            r2 = 44
            r1.<init>((java.io.InputStream) r3, (char) r2, (java.nio.charset.Charset) r4)     // Catch:{ Exception -> 0x001a, all -> 0x0017 }
            r1.readHeaders()     // Catch:{ Exception -> 0x0015 }
            java.lang.String[] r3 = r1.getHeaders()     // Catch:{ Exception -> 0x0015 }
            r1.close()
            return r3
        L_0x0015:
            r3 = move-exception
            goto L_0x001c
        L_0x0017:
            r3 = move-exception
            r1 = r0
            goto L_0x0026
        L_0x001a:
            r3 = move-exception
            r1 = r0
        L_0x001c:
            r3.printStackTrace()     // Catch:{ all -> 0x0025 }
            if (r1 == 0) goto L_0x002c
            r1.close()
            goto L_0x002c
        L_0x0025:
            r3 = move-exception
        L_0x0026:
            if (r1 == 0) goto L_0x002b
            r1.close()
        L_0x002b:
            throw r3
        L_0x002c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.ju.track.csv.CsvFileUtil.readHeaders(java.io.InputStream, java.nio.charset.Charset):java.lang.String[]");
    }

    public static List<String[]> read(String str) {
        if (str == null) {
            return null;
        }
        return read(new File(str));
    }

    public static List<String[]> read(File file) {
        return read(file, CHARSET);
    }

    public static List<String[]> read(String str, Charset charset) {
        if (str == null) {
            return null;
        }
        return read(new File(str), charset);
    }

    public static List<String[]> read(File file, Charset charset) {
        if (file == null || !file.exists()) {
            return null;
        }
        try {
            return read((InputStream) new FileInputStream(file), charset);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String[]> read(InputStream inputStream) {
        return read(inputStream, CHARSET);
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0030  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0037  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<java.lang.String[]> read(java.io.InputStream r3, java.nio.charset.Charset r4) {
        /*
            r0 = 0
            if (r3 == 0) goto L_0x003b
            com.taobao.ju.track.csv.CsvReader r1 = new com.taobao.ju.track.csv.CsvReader     // Catch:{ Exception -> 0x0029, all -> 0x0026 }
            r2 = 44
            r1.<init>((java.io.InputStream) r3, (char) r2, (java.nio.charset.Charset) r4)     // Catch:{ Exception -> 0x0029, all -> 0x0026 }
            r1.readHeaders()     // Catch:{ Exception -> 0x0024 }
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ Exception -> 0x0024 }
            r3.<init>()     // Catch:{ Exception -> 0x0024 }
        L_0x0012:
            boolean r4 = r1.readRecord()     // Catch:{ Exception -> 0x0024 }
            if (r4 == 0) goto L_0x0020
            java.lang.String[] r4 = r1.getValues()     // Catch:{ Exception -> 0x0024 }
            r3.add(r4)     // Catch:{ Exception -> 0x0024 }
            goto L_0x0012
        L_0x0020:
            r1.close()
            return r3
        L_0x0024:
            r3 = move-exception
            goto L_0x002b
        L_0x0026:
            r3 = move-exception
            r1 = r0
            goto L_0x0035
        L_0x0029:
            r3 = move-exception
            r1 = r0
        L_0x002b:
            r3.printStackTrace()     // Catch:{ all -> 0x0034 }
            if (r1 == 0) goto L_0x003b
            r1.close()
            goto L_0x003b
        L_0x0034:
            r3 = move-exception
        L_0x0035:
            if (r1 == 0) goto L_0x003a
            r1.close()
        L_0x003a:
            throw r3
        L_0x003b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.ju.track.csv.CsvFileUtil.read(java.io.InputStream, java.nio.charset.Charset):java.util.List");
    }

    public static String[][] readAsArray(String str) {
        if (str == null) {
            return null;
        }
        return readAsArray(new File(str));
    }

    public static String[][] readAsArray(File file) {
        return readAsArray(file, CHARSET);
    }

    public static String[][] readAsArray(String str, Charset charset) {
        if (str == null) {
            return null;
        }
        return readAsArray(new File(str), charset);
    }

    public static String[][] readAsArray(File file, Charset charset) {
        List<String[]> read = read(file, charset);
        if (read == null || read.size() <= 0 || read.get(0).length <= 0) {
            return null;
        }
        String[][] strArr = (String[][]) Array.newInstance(String.class, new int[]{read.size(), read.get(0).length});
        read.toArray(strArr);
        return strArr;
    }

    public static File write(File file, List<Object> list) {
        return write(file, CHARSET, list);
    }

    public static File write(File file, Charset charset, List<Object> list) {
        if (file != null) {
            write(file.getPath(), charset, (List<?>) list);
        }
        return file;
    }

    public static File write(String str, List<Object> list) {
        return write(str, CHARSET, (List<?>) list);
    }

    public static File write(String str, Charset charset, List<?> list) {
        File file;
        try {
            file = createFile(str);
        } catch (IOException e) {
            e.printStackTrace();
            file = null;
        }
        if (!(file == null || str == null || list == null)) {
            CsvWriter csvWriter = new CsvWriter(str, ',', charset);
            try {
                if (list.size() <= 0 || !(list.get(0) instanceof Map)) {
                    for (Object next : list) {
                        if (next instanceof String[]) {
                            csvWriter.writeRecord((String[]) next);
                        } else {
                            csvWriter.write(String.valueOf(next));
                        }
                    }
                    csvWriter.close();
                } else {
                    Set keySet = ((Map) list.get(0)).keySet();
                    int size = keySet.size();
                    String[] strArr = new String[size];
                    keySet.toArray(strArr);
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(strArr);
                    Iterator<?> it = list.iterator();
                    while (it.hasNext()) {
                        String[] strArr2 = new String[size];
                        Map map = (Map) it.next();
                        for (int i = 0; i < size; i++) {
                            strArr2[i] = (String) map.get(strArr[i]);
                        }
                        arrayList.add(strArr2);
                    }
                    write(str, charset, (List<?>) arrayList);
                    csvWriter.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (Throwable th) {
                csvWriter.close();
                throw th;
            }
        }
        return file;
    }

    private static File createFile(String str) throws IOException {
        File file = new File(str);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            file.createNewFile();
        }
        return file;
    }
}
