package android.taobao.windvane.cache;

import android.taobao.windvane.file.FileAccesser;
import android.taobao.windvane.util.TaoLog;

import com.taobao.weex.el.parse.Operators;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class WVFileCache {
    public static final int CREATE = 4;
    public static final int DELETE = 3;
    private static final String FILE_INFO = "wv_web_info.dat";
    public static final int READ = 1;
    /* access modifiers changed from: private */
    public static String TAG = "WVFileCache";
    public static final int WRITE = 2;
    /* access modifiers changed from: private */
    public String baseDirPath;
    /* access modifiers changed from: private */
    public FileChannel fInfoChannel;
    private RandomAccessFile fInfoOs;
    private String infoDirPath;
    private boolean isInit;
    private boolean isNoSpaceClear = true;
    /* access modifiers changed from: private */
    public int maxCapacity = 100;
    private boolean sdcard;
    private Map<String, WVFileInfo> storedFile = Collections.synchronizedMap(new FixedSizeLinkedHashMap());

    public WVFileCache(String str, String str2, int i, boolean z) {
        this.baseDirPath = str;
        this.infoDirPath = str2;
        this.maxCapacity = i;
        this.sdcard = z;
        this.isInit = false;
    }

    public String getDirPath() {
        return this.baseDirPath;
    }

    public boolean isSdcard() {
        return this.sdcard;
    }

    private void setCapacity(int i) {
        if (this.storedFile.size() > i) {
            onFileOverflow();
        }
    }

    public int size() {
        if (this.isInit) {
            return this.storedFile.size();
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        if (this.fInfoOs != null) {
            try {
                this.fInfoOs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.fInfoChannel != null) {
            try {
                this.fInfoChannel.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        super.finalize();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00e3, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean init() {
        /*
            r8 = this;
            monitor-enter(r8)
            boolean r0 = r8.isInit     // Catch:{ all -> 0x00e4 }
            r1 = 1
            if (r0 != 0) goto L_0x00e2
            java.io.File r0 = new java.io.File     // Catch:{ all -> 0x00e4 }
            java.lang.String r2 = r8.infoDirPath     // Catch:{ all -> 0x00e4 }
            java.lang.String r3 = "wv_web_info.dat"
            r0.<init>(r2, r3)     // Catch:{ all -> 0x00e4 }
            boolean r2 = r0.exists()     // Catch:{ all -> 0x00e4 }
            r3 = 0
            if (r2 != 0) goto L_0x0041
            java.io.File r2 = new java.io.File     // Catch:{ all -> 0x00e4 }
            java.lang.String r4 = r8.infoDirPath     // Catch:{ all -> 0x00e4 }
            r2.<init>(r4)     // Catch:{ all -> 0x00e4 }
            r2.mkdirs()     // Catch:{ all -> 0x00e4 }
            r0.createNewFile()     // Catch:{ IOException -> 0x0024 }
            goto L_0x0041
        L_0x0024:
            r0 = move-exception
            java.lang.String r1 = TAG     // Catch:{ all -> 0x00e4 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00e4 }
            r2.<init>()     // Catch:{ all -> 0x00e4 }
            java.lang.String r4 = "init createNewFile:"
            r2.append(r4)     // Catch:{ all -> 0x00e4 }
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x00e4 }
            r2.append(r0)     // Catch:{ all -> 0x00e4 }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x00e4 }
            android.taobao.windvane.util.TaoLog.e(r1, r0)     // Catch:{ all -> 0x00e4 }
            monitor-exit(r8)
            return r3
        L_0x0041:
            java.io.File r2 = new java.io.File     // Catch:{ all -> 0x00e4 }
            java.lang.String r4 = r8.baseDirPath     // Catch:{ all -> 0x00e4 }
            r2.<init>(r4)     // Catch:{ all -> 0x00e4 }
            r2.mkdirs()     // Catch:{ all -> 0x00e4 }
            java.io.RandomAccessFile r2 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x00c5 }
            java.lang.String r0 = r0.getAbsolutePath()     // Catch:{ Exception -> 0x00c5 }
            java.lang.String r4 = "rw"
            r2.<init>(r0, r4)     // Catch:{ Exception -> 0x00c5 }
            r8.fInfoOs = r2     // Catch:{ Exception -> 0x00c5 }
            java.nio.channels.FileChannel r0 = r8.fInfoChannel     // Catch:{ Exception -> 0x00c5 }
            if (r0 != 0) goto L_0x0064
            java.io.RandomAccessFile r0 = r8.fInfoOs     // Catch:{ Exception -> 0x00c5 }
            java.nio.channels.FileChannel r0 = r0.getChannel()     // Catch:{ Exception -> 0x00c5 }
            r8.fInfoChannel = r0     // Catch:{ Exception -> 0x00c5 }
        L_0x0064:
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ Exception -> 0x00c5 }
            if (r0 == 0) goto L_0x0084
            java.lang.String r0 = TAG     // Catch:{ Exception -> 0x00c5 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00c5 }
            r2.<init>()     // Catch:{ Exception -> 0x00c5 }
            java.lang.String r4 = "lock success process is "
            r2.append(r4)     // Catch:{ Exception -> 0x00c5 }
            int r4 = android.os.Process.myPid()     // Catch:{ Exception -> 0x00c5 }
            r2.append(r4)     // Catch:{ Exception -> 0x00c5 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x00c5 }
            android.taobao.windvane.util.TaoLog.d(r0, r2)     // Catch:{ Exception -> 0x00c5 }
        L_0x0084:
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00e4 }
            boolean r0 = r8.collectFiles()     // Catch:{ all -> 0x00e4 }
            if (r0 != 0) goto L_0x0090
            monitor-exit(r8)
            return r3
        L_0x0090:
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ all -> 0x00e4 }
            if (r0 == 0) goto L_0x00b2
            java.lang.String r0 = TAG     // Catch:{ all -> 0x00e4 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00e4 }
            r2.<init>()     // Catch:{ all -> 0x00e4 }
            java.lang.String r3 = "init time cost:"
            r2.append(r3)     // Catch:{ all -> 0x00e4 }
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00e4 }
            r3 = 0
            long r6 = r6 - r4
            r2.append(r6)     // Catch:{ all -> 0x00e4 }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x00e4 }
            android.taobao.windvane.util.TaoLog.d(r0, r2)     // Catch:{ all -> 0x00e4 }
        L_0x00b2:
            r8.isInit = r1     // Catch:{ all -> 0x00e4 }
            int r0 = r8.maxCapacity     // Catch:{ all -> 0x00e4 }
            r8.setCapacity(r0)     // Catch:{ all -> 0x00e4 }
            java.util.Map<java.lang.String, android.taobao.windvane.cache.WVFileInfo> r0 = r8.storedFile     // Catch:{ all -> 0x00e4 }
            int r0 = r0.size()     // Catch:{ all -> 0x00e4 }
            if (r0 != 0) goto L_0x00e2
            r8.clear()     // Catch:{ all -> 0x00e4 }
            goto L_0x00e2
        L_0x00c5:
            r0 = move-exception
            java.lang.String r1 = TAG     // Catch:{ all -> 0x00e4 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x00e4 }
            r2.<init>()     // Catch:{ all -> 0x00e4 }
            java.lang.String r4 = "init fInfoOs RandomAccessFile:"
            r2.append(r4)     // Catch:{ all -> 0x00e4 }
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x00e4 }
            r2.append(r0)     // Catch:{ all -> 0x00e4 }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x00e4 }
            android.taobao.windvane.util.TaoLog.e(r1, r0)     // Catch:{ all -> 0x00e4 }
            monitor-exit(r8)
            return r3
        L_0x00e2:
            monitor-exit(r8)
            return r1
        L_0x00e4:
            r0 = move-exception
            monitor-exit(r8)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.cache.WVFileCache.init():boolean");
    }

    public WVFileInfo getFileInfo(String str) {
        WVFileInfo wVFileInfo;
        if (!this.isInit || (wVFileInfo = this.storedFile.get(str)) == null) {
            return null;
        }
        if (new File(this.baseDirPath, str).exists()) {
            return wVFileInfo;
        }
        WVFileInfoParser.updateFileInfo(3, wVFileInfo, this.fInfoChannel);
        return null;
    }

    public void updateFileInfo(WVFileInfo wVFileInfo) {
        String str;
        WVFileInfo wVFileInfo2;
        if (this.isInit && wVFileInfo != null && (str = wVFileInfo.fileName) != null && (wVFileInfo2 = this.storedFile.get(str)) != null) {
            TaoLog.d(TAG, "update info success");
            wVFileInfo.pos = wVFileInfo2.pos;
            this.storedFile.put(str, WVFileInfoParser.updateFileInfo(2, wVFileInfo, this.fInfoChannel));
        }
    }

    public byte[] read(String str) {
        if (TaoLog.getLogStatus()) {
            String str2 = TAG;
            TaoLog.d(str2, "read:" + str);
        }
        if (!this.isInit) {
            return null;
        }
        long currentTimeMillis = System.currentTimeMillis();
        WVFileInfo wVFileInfo = this.storedFile.get(str);
        if (wVFileInfo == null) {
            return null;
        }
        this.storedFile.remove(str);
        this.storedFile.put(str, WVFileInfoParser.updateFileInfo(1, wVFileInfo, this.fInfoChannel));
        byte[] read = FileAccesser.read(new File(this.baseDirPath, str));
        if (TaoLog.getLogStatus()) {
            String str3 = TAG;
            TaoLog.d(str3, "read time cost:" + (System.currentTimeMillis() - currentTimeMillis));
        }
        return read;
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x006c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean write(android.taobao.windvane.cache.WVFileInfo r8, java.nio.ByteBuffer r9) {
        /*
            r7 = this;
            r0 = 0
            if (r8 == 0) goto L_0x00ac
            java.lang.String r1 = r8.fileName
            if (r1 != 0) goto L_0x0009
            goto L_0x00ac
        L_0x0009:
            boolean r2 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r2 == 0) goto L_0x0025
            java.lang.String r2 = TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "write:"
            r3.append(r4)
            r3.append(r1)
            java.lang.String r3 = r3.toString()
            android.taobao.windvane.util.TaoLog.d(r2, r3)
        L_0x0025:
            boolean r2 = r7.isInit
            if (r2 == 0) goto L_0x00ab
            java.io.File r2 = new java.io.File
            java.lang.String r3 = r7.baseDirPath
            r2.<init>(r3, r1)
            boolean r3 = android.taobao.windvane.file.FileAccesser.write((java.io.File) r2, (java.nio.ByteBuffer) r9)     // Catch:{ NotEnoughSpace -> 0x0036 }
            r9 = r3
            goto L_0x006a
        L_0x0036:
            r3 = move-exception
            java.lang.String r4 = TAG
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "write error. fileName="
            r5.append(r6)
            r5.append(r1)
            java.lang.String r6 = ". NotEnoughSpace: "
            r5.append(r6)
            java.lang.String r3 = r3.getMessage()
            r5.append(r3)
            java.lang.String r3 = r5.toString()
            android.taobao.windvane.util.TaoLog.e(r4, r3)
            boolean r3 = r7.isNoSpaceClear
            if (r3 == 0) goto L_0x0069
            r7.clear()
            boolean r9 = android.taobao.windvane.file.FileAccesser.write((java.io.File) r2, (java.nio.ByteBuffer) r9)     // Catch:{ NotEnoughSpace -> 0x0065 }
            goto L_0x006a
        L_0x0065:
            r9 = move-exception
            r9.printStackTrace()
        L_0x0069:
            r9 = 0
        L_0x006a:
            if (r9 == 0) goto L_0x00ab
            java.util.Map<java.lang.String, android.taobao.windvane.cache.WVFileInfo> r9 = r7.storedFile
            java.lang.Object r9 = r9.get(r1)
            android.taobao.windvane.cache.WVFileInfo r9 = (android.taobao.windvane.cache.WVFileInfo) r9
            if (r9 == 0) goto L_0x0092
            java.lang.String r0 = TAG
            java.lang.String r2 = "writed success, file exist"
            android.taobao.windvane.util.TaoLog.d(r0, r2)
            long r2 = r9.pos
            r8.pos = r2
            r9 = 2
            java.nio.channels.FileChannel r0 = r7.fInfoChannel
            android.taobao.windvane.cache.WVFileInfo r8 = android.taobao.windvane.cache.WVFileInfoParser.updateFileInfo(r9, r8, r0)
            java.util.Map<java.lang.String, android.taobao.windvane.cache.WVFileInfo> r9 = r7.storedFile
            android.taobao.windvane.cache.WVFileInfo r8 = r8.convertToFileInfo()
            r9.put(r1, r8)
            goto L_0x00a9
        L_0x0092:
            java.lang.String r9 = TAG
            java.lang.String r0 = "writed success, file do not exist"
            android.taobao.windvane.util.TaoLog.d(r9, r0)
            r9 = 4
            java.nio.channels.FileChannel r0 = r7.fInfoChannel
            android.taobao.windvane.cache.WVFileInfo r8 = android.taobao.windvane.cache.WVFileInfoParser.updateFileInfo(r9, r8, r0)
            java.util.Map<java.lang.String, android.taobao.windvane.cache.WVFileInfo> r9 = r7.storedFile
            android.taobao.windvane.cache.WVFileInfo r8 = r8.convertToFileInfo()
            r9.put(r1, r8)
        L_0x00a9:
            r8 = 1
            return r8
        L_0x00ab:
            return r0
        L_0x00ac:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.cache.WVFileCache.write(android.taobao.windvane.cache.WVFileInfo, java.nio.ByteBuffer):boolean");
    }

    public boolean delete(String str) {
        WVFileInfo wVFileInfo;
        boolean z = false;
        if (!this.isInit || str == null) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        File file = new File(this.baseDirPath, str);
        if (file.isFile()) {
            z = file.delete();
        }
        if ((!z && file.exists()) || (wVFileInfo = this.storedFile.get(str)) == null) {
            return z;
        }
        TaoLog.d(TAG, "delete success");
        WVFileInfoParser.updateFileInfo(3, wVFileInfo, this.fInfoChannel);
        this.storedFile.remove(str);
        if (!TaoLog.getLogStatus()) {
            return true;
        }
        String str2 = TAG;
        TaoLog.d(str2, "delete time cost:" + (System.currentTimeMillis() - currentTimeMillis));
        return true;
    }

    public boolean clear() {
        String[] list;
        if (!this.isInit || (list = new File(this.baseDirPath).list()) == null) {
            return false;
        }
        boolean z = true;
        for (String delete : list) {
            z &= delete(delete);
        }
        return z;
    }

    private void onFileOverflow() {
        TaoLog.d(TAG, "onFileOverflow");
        ArrayList arrayList = new ArrayList();
        Set<Map.Entry<String, WVFileInfo>> entrySet = this.storedFile.entrySet();
        int size = this.storedFile.size();
        for (Map.Entry next : entrySet) {
            if (size < this.maxCapacity) {
                break;
            }
            WVFileInfo wVFileInfo = (WVFileInfo) next.getValue();
            if (wVFileInfo != null) {
                arrayList.add(wVFileInfo);
            }
            size--;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            delete(((WVFileInfo) it.next()).fileName);
        }
    }

    private boolean collectFiles() {
        byte[] bArr;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            ByteBuffer allocate = ByteBuffer.allocate((int) this.fInfoChannel.size());
            this.fInfoChannel.read(allocate);
            bArr = allocate.array();
        } catch (IOException e) {
            String str = TAG;
            TaoLog.e(str, "collectFiles fInfoChannel.read error:" + e.getMessage());
            bArr = null;
        }
        if (TaoLog.getLogStatus()) {
            String str2 = TAG;
            TaoLog.d(str2, "collectFiles read fileinfo:" + (System.currentTimeMillis() - currentTimeMillis));
        }
        long currentTimeMillis2 = System.currentTimeMillis();
        if (bArr == null) {
            return false;
        }
        TaoLog.d("collectFiles", "read fileinfo success");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i = 60;
        int i2 = 0;
        boolean z = false;
        while (i < bArr.length) {
            if (bArr[i] == 10) {
                int i3 = i - i2;
                WVFileInfo fileInfo = WVFileInfoParser.getFileInfo(bArr, i2, i3);
                if (fileInfo != null) {
                    String str3 = fileInfo.fileName;
                    if (!this.storedFile.containsKey(str3)) {
                        fileInfo.pos = (long) byteArrayOutputStream.size();
                        this.storedFile.put(str3, fileInfo);
                        byteArrayOutputStream.write(bArr, i2, i3 + 1);
                        i2 = i + 1;
                        i += 60;
                    }
                }
                z = true;
                i2 = i + 1;
                i += 60;
            }
            i++;
        }
        if (TaoLog.getLogStatus()) {
            String str4 = TAG;
            TaoLog.d(str4, "parse fileinfo:" + (System.currentTimeMillis() - currentTimeMillis2));
        }
        long currentTimeMillis3 = System.currentTimeMillis();
        if (z) {
            try {
                this.fInfoChannel.truncate(0);
                this.fInfoChannel.position(0);
                ByteBuffer wrap = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
                wrap.position(0);
                this.fInfoChannel.write(wrap);
            } catch (IOException e2) {
                String str5 = TAG;
                TaoLog.e(str5, "collectFiles fInfoChannel.write error:" + e2.getMessage());
            }
        }
        try {
            byteArrayOutputStream.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        if (TaoLog.getLogStatus()) {
            String str6 = TAG;
            TaoLog.d(str6, "write fileinfo:" + (System.currentTimeMillis() - currentTimeMillis3));
        }
        return true;
    }

    protected class FixedSizeLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
        private static final long serialVersionUID = 1;

        protected FixedSizeLinkedHashMap() {
        }

        /* access modifiers changed from: protected */
        public boolean removeEldestEntry(Map.Entry<K, V> entry) {
            if (size() <= WVFileCache.this.maxCapacity) {
                return false;
            }
            if (TaoLog.getLogStatus()) {
                String access$100 = WVFileCache.TAG;
                TaoLog.d(access$100, "removeEldestEntry, size:" + size() + Operators.SPACE_STR + entry.getKey());
            }
            V value = entry.getValue();
            if (!(value instanceof WVFileInfo)) {
                return true;
            }
            WVFileInfo wVFileInfo = (WVFileInfo) value;
            if (!FileAccesser.deleteFile(new File(WVFileCache.this.baseDirPath, wVFileInfo.fileName))) {
                return true;
            }
            WVFileInfoParser.updateFileInfo(3, wVFileInfo, WVFileCache.this.fInfoChannel);
            return true;
        }
    }
}
