package com.facebook.soloader;

import android.content.Context;
import android.os.Parcel;
import android.util.Log;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import javax.annotation.Nullable;

public abstract class UnpackingSoSource extends DirectorySoSource {
    private static final String DEPS_FILE_NAME = "dso_deps";
    private static final String LOCK_FILE_NAME = "dso_lock";
    private static final String MANIFEST_FILE_NAME = "dso_manifest";
    private static final byte MANIFEST_VERSION = 1;
    private static final byte STATE_CLEAN = 1;
    private static final byte STATE_DIRTY = 0;
    private static final String STATE_FILE_NAME = "dso_state";
    private static final String TAG = "fb-UnpackingSoSource";
    @Nullable
    private String[] mAbis;
    protected final Context mContext;

    /* access modifiers changed from: protected */
    public abstract Unpacker makeUnpacker() throws IOException;

    protected UnpackingSoSource(Context context, String str) {
        super(getSoStorePath(context, str), 1);
        this.mContext = context;
    }

    protected UnpackingSoSource(Context context, File file) {
        super(file, 1);
        this.mContext = context;
    }

    public static File getSoStorePath(Context context, String str) {
        return new File(context.getApplicationInfo().dataDir + "/" + str);
    }

    public String[] getSoSourceAbis() {
        if (this.mAbis == null) {
            return super.getSoSourceAbis();
        }
        return this.mAbis;
    }

    public void setSoSourceAbis(String[] strArr) {
        this.mAbis = strArr;
    }

    public static class Dso {
        public final String hash;
        public final String name;

        public Dso(String str, String str2) {
            this.name = str;
            this.hash = str2;
        }
    }

    public static final class DsoManifest {
        public final Dso[] dsos;

        public DsoManifest(Dso[] dsoArr) {
            this.dsos = dsoArr;
        }

        static final DsoManifest read(DataInput dataInput) throws IOException {
            if (dataInput.readByte() == 1) {
                int readInt = dataInput.readInt();
                if (readInt >= 0) {
                    Dso[] dsoArr = new Dso[readInt];
                    for (int i = 0; i < readInt; i++) {
                        dsoArr[i] = new Dso(dataInput.readUTF(), dataInput.readUTF());
                    }
                    return new DsoManifest(dsoArr);
                }
                throw new RuntimeException("illegal number of shared libraries");
            }
            throw new RuntimeException("wrong dso manifest version");
        }

        public final void write(DataOutput dataOutput) throws IOException {
            dataOutput.writeByte(1);
            dataOutput.writeInt(this.dsos.length);
            for (int i = 0; i < this.dsos.length; i++) {
                dataOutput.writeUTF(this.dsos[i].name);
                dataOutput.writeUTF(this.dsos[i].hash);
            }
        }
    }

    protected static final class InputDso implements Closeable {
        public final InputStream content;
        public final Dso dso;

        public InputDso(Dso dso2, InputStream inputStream) {
            this.dso = dso2;
            this.content = inputStream;
        }

        public void close() throws IOException {
            this.content.close();
        }
    }

    protected static abstract class InputDsoIterator implements Closeable {
        public void close() throws IOException {
        }

        public abstract boolean hasNext();

        public abstract InputDso next() throws IOException;

        protected InputDsoIterator() {
        }
    }

    protected static abstract class Unpacker implements Closeable {
        public void close() throws IOException {
        }

        /* access modifiers changed from: protected */
        public abstract DsoManifest getDsoManifest() throws IOException;

        /* access modifiers changed from: protected */
        public abstract InputDsoIterator openDsoIterator() throws IOException;

        protected Unpacker() {
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002c, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0022, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0026, code lost:
        if (r3 != null) goto L_0x0028;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeState(java.io.File r3, byte r4) throws java.io.IOException {
        /*
            java.io.RandomAccessFile r0 = new java.io.RandomAccessFile
            java.lang.String r1 = "rw"
            r0.<init>(r3, r1)
            r1 = 0
            r3 = 0
            r0.seek(r1)     // Catch:{ Throwable -> 0x0024 }
            r0.write(r4)     // Catch:{ Throwable -> 0x0024 }
            long r1 = r0.getFilePointer()     // Catch:{ Throwable -> 0x0024 }
            r0.setLength(r1)     // Catch:{ Throwable -> 0x0024 }
            java.io.FileDescriptor r4 = r0.getFD()     // Catch:{ Throwable -> 0x0024 }
            r4.sync()     // Catch:{ Throwable -> 0x0024 }
            r0.close()
            return
        L_0x0022:
            r4 = move-exception
            goto L_0x0026
        L_0x0024:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x0022 }
        L_0x0026:
            if (r3 == 0) goto L_0x002c
            r0.close()     // Catch:{ Throwable -> 0x002f }
            goto L_0x002f
        L_0x002c:
            r0.close()
        L_0x002f:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.UnpackingSoSource.writeState(java.io.File, byte):void");
    }

    private void deleteUnmentionedFiles(Dso[] dsoArr) throws IOException {
        String[] list = this.soDirectory.list();
        if (list != null) {
            for (String str : list) {
                if (!str.equals(STATE_FILE_NAME) && !str.equals(LOCK_FILE_NAME) && !str.equals(DEPS_FILE_NAME) && !str.equals(MANIFEST_FILE_NAME)) {
                    boolean z = false;
                    int i = 0;
                    while (!z && i < dsoArr.length) {
                        if (dsoArr[i].name.equals(str)) {
                            z = true;
                        }
                        i++;
                    }
                    if (!z) {
                        File file = new File(this.soDirectory, str);
                        Log.v(TAG, "deleting unaccounted-for file " + file);
                        SysUtil.dumbDeleteRecursive(file);
                    }
                }
            }
            return;
        }
        throw new IOException("unable to list directory " + this.soDirectory);
    }

    private void extractDso(InputDso inputDso, byte[] bArr) throws IOException {
        RandomAccessFile randomAccessFile;
        Log.i(TAG, "extracting DSO " + inputDso.dso.name);
        if (this.soDirectory.setWritable(true, true)) {
            File file = new File(this.soDirectory, inputDso.dso.name);
            try {
                randomAccessFile = new RandomAccessFile(file, "rw");
            } catch (IOException e) {
                Log.w(TAG, "error overwriting " + file + " trying to delete and start over", e);
                SysUtil.dumbDeleteRecursive(file);
                randomAccessFile = new RandomAccessFile(file, "rw");
            }
            try {
                int available = inputDso.content.available();
                if (available > 1) {
                    SysUtil.fallocateIfSupported(randomAccessFile.getFD(), (long) available);
                }
                SysUtil.copyBytes(randomAccessFile, inputDso.content, Integer.MAX_VALUE, bArr);
                randomAccessFile.setLength(randomAccessFile.getFilePointer());
                if (!file.setExecutable(true, false)) {
                    throw new IOException("cannot make file executable: " + file);
                }
            } finally {
                randomAccessFile.close();
            }
        } else {
            throw new IOException("cannot make directory writable for us: " + this.soDirectory);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x004a A[Catch:{ Throwable -> 0x003a, all -> 0x0037, Throwable -> 0x00e0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0061 A[Catch:{ Throwable -> 0x003a, all -> 0x0037, Throwable -> 0x00e0 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void regenerate(byte r11, com.facebook.soloader.UnpackingSoSource.DsoManifest r12, com.facebook.soloader.UnpackingSoSource.InputDsoIterator r13) throws java.io.IOException {
        /*
            r10 = this;
            java.lang.String r0 = "fb-UnpackingSoSource"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "regenerating DSO store "
            r1.append(r2)
            java.lang.Class r2 = r10.getClass()
            java.lang.String r2 = r2.getName()
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.v(r0, r1)
            java.io.File r0 = new java.io.File
            java.io.File r1 = r10.soDirectory
            java.lang.String r2 = "dso_manifest"
            r0.<init>(r1, r2)
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile
            java.lang.String r2 = "rw"
            r1.<init>(r0, r2)
            r0 = 1
            r2 = 0
            if (r11 != r0) goto L_0x0046
            com.facebook.soloader.UnpackingSoSource$DsoManifest r11 = com.facebook.soloader.UnpackingSoSource.DsoManifest.read(r1)     // Catch:{ Exception -> 0x003e }
            goto L_0x0047
        L_0x0037:
            r11 = move-exception
            goto L_0x00d7
        L_0x003a:
            r11 = move-exception
            r2 = r11
            goto L_0x00d6
        L_0x003e:
            r11 = move-exception
            java.lang.String r3 = "fb-UnpackingSoSource"
            java.lang.String r4 = "error reading existing DSO manifest"
            android.util.Log.i(r3, r4, r11)     // Catch:{ Throwable -> 0x003a }
        L_0x0046:
            r11 = r2
        L_0x0047:
            r3 = 0
            if (r11 != 0) goto L_0x0051
            com.facebook.soloader.UnpackingSoSource$DsoManifest r11 = new com.facebook.soloader.UnpackingSoSource$DsoManifest     // Catch:{ Throwable -> 0x003a }
            com.facebook.soloader.UnpackingSoSource$Dso[] r4 = new com.facebook.soloader.UnpackingSoSource.Dso[r3]     // Catch:{ Throwable -> 0x003a }
            r11.<init>(r4)     // Catch:{ Throwable -> 0x003a }
        L_0x0051:
            com.facebook.soloader.UnpackingSoSource$Dso[] r12 = r12.dsos     // Catch:{ Throwable -> 0x003a }
            r10.deleteUnmentionedFiles(r12)     // Catch:{ Throwable -> 0x003a }
            r12 = 32768(0x8000, float:4.5918E-41)
            byte[] r12 = new byte[r12]     // Catch:{ Throwable -> 0x003a }
        L_0x005b:
            boolean r4 = r13.hasNext()     // Catch:{ Throwable -> 0x003a }
            if (r4 == 0) goto L_0x00b4
            com.facebook.soloader.UnpackingSoSource$InputDso r4 = r13.next()     // Catch:{ Throwable -> 0x003a }
            r5 = 1
            r6 = 0
        L_0x0067:
            if (r5 == 0) goto L_0x0097
            com.facebook.soloader.UnpackingSoSource$Dso[] r7 = r11.dsos     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            int r7 = r7.length     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            if (r6 >= r7) goto L_0x0097
            com.facebook.soloader.UnpackingSoSource$Dso[] r7 = r11.dsos     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            r7 = r7[r6]     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            java.lang.String r7 = r7.name     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            com.facebook.soloader.UnpackingSoSource$Dso r8 = r4.dso     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            java.lang.String r8 = r8.name     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            boolean r7 = r7.equals(r8)     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            if (r7 == 0) goto L_0x008f
            com.facebook.soloader.UnpackingSoSource$Dso[] r7 = r11.dsos     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            r7 = r7[r6]     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            java.lang.String r7 = r7.hash     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            com.facebook.soloader.UnpackingSoSource$Dso r8 = r4.dso     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            java.lang.String r8 = r8.hash     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            boolean r7 = r7.equals(r8)     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            if (r7 == 0) goto L_0x008f
            r5 = 0
        L_0x008f:
            int r6 = r6 + 1
            goto L_0x0067
        L_0x0092:
            r11 = move-exception
            r12 = r2
            goto L_0x00a2
        L_0x0095:
            r11 = move-exception
            goto L_0x009d
        L_0x0097:
            if (r5 == 0) goto L_0x00ae
            r10.extractDso(r4, r12)     // Catch:{ Throwable -> 0x0095, all -> 0x0092 }
            goto L_0x00ae
        L_0x009d:
            throw r11     // Catch:{ all -> 0x009e }
        L_0x009e:
            r12 = move-exception
            r9 = r12
            r12 = r11
            r11 = r9
        L_0x00a2:
            if (r4 == 0) goto L_0x00ad
            if (r12 == 0) goto L_0x00aa
            r4.close()     // Catch:{ Throwable -> 0x00ad }
            goto L_0x00ad
        L_0x00aa:
            r4.close()     // Catch:{ Throwable -> 0x003a }
        L_0x00ad:
            throw r11     // Catch:{ Throwable -> 0x003a }
        L_0x00ae:
            if (r4 == 0) goto L_0x005b
            r4.close()     // Catch:{ Throwable -> 0x003a }
            goto L_0x005b
        L_0x00b4:
            r1.close()
            java.lang.String r11 = "fb-UnpackingSoSource"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r13 = "Finished regenerating DSO store "
            r12.append(r13)
            java.lang.Class r13 = r10.getClass()
            java.lang.String r13 = r13.getName()
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            android.util.Log.v(r11, r12)
            return
        L_0x00d6:
            throw r2     // Catch:{ all -> 0x0037 }
        L_0x00d7:
            if (r2 == 0) goto L_0x00dd
            r1.close()     // Catch:{ Throwable -> 0x00e0 }
            goto L_0x00e0
        L_0x00dd:
            r1.close()
        L_0x00e0:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.UnpackingSoSource.regenerate(byte, com.facebook.soloader.UnpackingSoSource$DsoManifest, com.facebook.soloader.UnpackingSoSource$InputDsoIterator):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00a4, code lost:
        r12 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00a5, code lost:
        r13 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00a9, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00aa, code lost:
        r10 = r13;
        r13 = r12;
        r12 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00b9, code lost:
        r12 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00ba, code lost:
        r13 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00be, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00bf, code lost:
        r10 = r13;
        r13 = r12;
        r12 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00c4, code lost:
        if (r13 != null) goto L_0x00c6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00b9 A[ExcHandler: all (th java.lang.Throwable)] */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x00c4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean refreshLocked(com.facebook.soloader.FileLocker r12, int r13, byte[] r14) throws java.io.IOException {
        /*
            r11 = this;
            java.io.File r5 = new java.io.File
            java.io.File r0 = r11.soDirectory
            java.lang.String r1 = "dso_state"
            r5.<init>(r0, r1)
            java.io.RandomAccessFile r0 = new java.io.RandomAccessFile
            java.lang.String r1 = "rw"
            r0.<init>(r5, r1)
            r7 = 1
            r1 = 0
            r2 = 0
            byte r3 = r0.readByte()     // Catch:{ EOFException -> 0x0036, Throwable -> 0x003a }
            if (r3 == r7) goto L_0x0047
            java.lang.String r3 = "fb-UnpackingSoSource"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ EOFException -> 0x0036, Throwable -> 0x003a }
            r4.<init>()     // Catch:{ EOFException -> 0x0036, Throwable -> 0x003a }
            java.lang.String r6 = "dso store "
            r4.append(r6)     // Catch:{ EOFException -> 0x0036, Throwable -> 0x003a }
            java.io.File r6 = r11.soDirectory     // Catch:{ EOFException -> 0x0036, Throwable -> 0x003a }
            r4.append(r6)     // Catch:{ EOFException -> 0x0036, Throwable -> 0x003a }
            java.lang.String r6 = " regeneration interrupted: wiping clean"
            r4.append(r6)     // Catch:{ EOFException -> 0x0036, Throwable -> 0x003a }
            java.lang.String r4 = r4.toString()     // Catch:{ EOFException -> 0x0036, Throwable -> 0x003a }
            android.util.Log.v(r3, r4)     // Catch:{ EOFException -> 0x0036, Throwable -> 0x003a }
        L_0x0036:
            r3 = 0
            goto L_0x0047
        L_0x0038:
            r12 = move-exception
            goto L_0x003d
        L_0x003a:
            r12 = move-exception
            r1 = r12
            throw r1     // Catch:{ all -> 0x0038 }
        L_0x003d:
            if (r1 == 0) goto L_0x0043
            r0.close()     // Catch:{ Throwable -> 0x0046 }
            goto L_0x0046
        L_0x0043:
            r0.close()
        L_0x0046:
            throw r12
        L_0x0047:
            r0.close()
            java.io.File r4 = new java.io.File
            java.io.File r0 = r11.soDirectory
            java.lang.String r6 = "dso_deps"
            r4.<init>(r0, r6)
            java.io.RandomAccessFile r0 = new java.io.RandomAccessFile
            java.lang.String r6 = "rw"
            r0.<init>(r4, r6)
            long r8 = r0.length()     // Catch:{ Throwable -> 0x010a }
            int r6 = (int) r8     // Catch:{ Throwable -> 0x010a }
            byte[] r6 = new byte[r6]     // Catch:{ Throwable -> 0x010a }
            int r8 = r0.read(r6)     // Catch:{ Throwable -> 0x010a }
            int r9 = r6.length     // Catch:{ Throwable -> 0x010a }
            if (r8 == r9) goto L_0x0070
            java.lang.String r3 = "fb-UnpackingSoSource"
            java.lang.String r8 = "short read of so store deps file: marking unclean"
            android.util.Log.v(r3, r8)     // Catch:{ Throwable -> 0x010a }
            r3 = 0
        L_0x0070:
            boolean r6 = java.util.Arrays.equals(r6, r14)     // Catch:{ Throwable -> 0x010a }
            if (r6 != 0) goto L_0x007e
            java.lang.String r3 = "fb-UnpackingSoSource"
            java.lang.String r6 = "deps mismatch on deps store: regenerating"
            android.util.Log.v(r3, r6)     // Catch:{ Throwable -> 0x010a }
            r3 = 0
        L_0x007e:
            if (r3 != 0) goto L_0x00ce
            java.lang.String r6 = "fb-UnpackingSoSource"
            java.lang.String r8 = "so store dirty: regenerating"
            android.util.Log.v(r6, r8)     // Catch:{ Throwable -> 0x010a }
            writeState(r5, r2)     // Catch:{ Throwable -> 0x010a }
            com.facebook.soloader.UnpackingSoSource$Unpacker r6 = r11.makeUnpacker()     // Catch:{ Throwable -> 0x010a }
            com.facebook.soloader.UnpackingSoSource$DsoManifest r8 = r6.getDsoManifest()     // Catch:{ Throwable -> 0x00bc, all -> 0x00b9 }
            com.facebook.soloader.UnpackingSoSource$InputDsoIterator r9 = r6.openDsoIterator()     // Catch:{ Throwable -> 0x00bc, all -> 0x00b9 }
            r11.regenerate(r3, r8, r9)     // Catch:{ Throwable -> 0x00a7, all -> 0x00a4 }
            if (r9 == 0) goto L_0x009e
            r9.close()     // Catch:{ Throwable -> 0x00bc, all -> 0x00b9 }
        L_0x009e:
            if (r6 == 0) goto L_0x00cf
            r6.close()     // Catch:{ Throwable -> 0x010a }
            goto L_0x00cf
        L_0x00a4:
            r12 = move-exception
            r13 = r1
            goto L_0x00ad
        L_0x00a7:
            r12 = move-exception
            throw r12     // Catch:{ all -> 0x00a9 }
        L_0x00a9:
            r13 = move-exception
            r10 = r13
            r13 = r12
            r12 = r10
        L_0x00ad:
            if (r9 == 0) goto L_0x00b8
            if (r13 == 0) goto L_0x00b5
            r9.close()     // Catch:{ Throwable -> 0x00b8, all -> 0x00b9 }
            goto L_0x00b8
        L_0x00b5:
            r9.close()     // Catch:{ Throwable -> 0x00bc, all -> 0x00b9 }
        L_0x00b8:
            throw r12     // Catch:{ Throwable -> 0x00bc, all -> 0x00b9 }
        L_0x00b9:
            r12 = move-exception
            r13 = r1
            goto L_0x00c2
        L_0x00bc:
            r12 = move-exception
            throw r12     // Catch:{ all -> 0x00be }
        L_0x00be:
            r13 = move-exception
            r10 = r13
            r13 = r12
            r12 = r10
        L_0x00c2:
            if (r6 == 0) goto L_0x00cd
            if (r13 == 0) goto L_0x00ca
            r6.close()     // Catch:{ Throwable -> 0x00cd }
            goto L_0x00cd
        L_0x00ca:
            r6.close()     // Catch:{ Throwable -> 0x010a }
        L_0x00cd:
            throw r12     // Catch:{ Throwable -> 0x010a }
        L_0x00ce:
            r8 = r1
        L_0x00cf:
            r0.close()
            if (r8 != 0) goto L_0x00d5
            return r2
        L_0x00d5:
            com.facebook.soloader.UnpackingSoSource$1 r9 = new com.facebook.soloader.UnpackingSoSource$1
            r0 = r9
            r1 = r11
            r2 = r4
            r3 = r14
            r4 = r8
            r6 = r12
            r0.<init>(r2, r3, r4, r5, r6)
            r12 = r13 & 1
            if (r12 == 0) goto L_0x0104
            java.lang.Thread r12 = new java.lang.Thread
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = "SoSync:"
            r13.append(r14)
            java.io.File r14 = r11.soDirectory
            java.lang.String r14 = r14.getName()
            r13.append(r14)
            java.lang.String r13 = r13.toString()
            r12.<init>(r9, r13)
            r12.start()
            goto L_0x0107
        L_0x0104:
            r9.run()
        L_0x0107:
            return r7
        L_0x0108:
            r12 = move-exception
            goto L_0x010d
        L_0x010a:
            r12 = move-exception
            r1 = r12
            throw r1     // Catch:{ all -> 0x0108 }
        L_0x010d:
            if (r1 == 0) goto L_0x0113
            r0.close()     // Catch:{ Throwable -> 0x0116 }
            goto L_0x0116
        L_0x0113:
            r0.close()
        L_0x0116:
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.soloader.UnpackingSoSource.refreshLocked(com.facebook.soloader.FileLocker, int, byte[]):boolean");
    }

    /* access modifiers changed from: protected */
    public byte[] getDepsBlock() throws IOException {
        Throwable th;
        Parcel obtain = Parcel.obtain();
        Unpacker makeUnpacker = makeUnpacker();
        try {
            Dso[] dsoArr = makeUnpacker.getDsoManifest().dsos;
            obtain.writeByte((byte) 1);
            obtain.writeInt(dsoArr.length);
            for (int i = 0; i < dsoArr.length; i++) {
                obtain.writeString(dsoArr[i].name);
                obtain.writeString(dsoArr[i].hash);
            }
            if (makeUnpacker != null) {
                makeUnpacker.close();
            }
            byte[] marshall = obtain.marshall();
            obtain.recycle();
            return marshall;
        } catch (Throwable unused) {
        }
        throw th;
    }

    /* access modifiers changed from: protected */
    public void prepare(int i) throws IOException {
        SysUtil.mkdirOrThrow(this.soDirectory);
        FileLocker lock = FileLocker.lock(new File(this.soDirectory, LOCK_FILE_NAME));
        try {
            Log.v(TAG, "locked dso store " + this.soDirectory);
            if (refreshLocked(lock, i, getDepsBlock())) {
                lock = null;
            } else {
                Log.i(TAG, "dso store is up-to-date: " + this.soDirectory);
            }
            if (lock != null) {
                Log.v(TAG, "releasing dso store lock for " + this.soDirectory);
                lock.close();
                return;
            }
            Log.v(TAG, "not releasing dso store lock for " + this.soDirectory + " (syncer thread started)");
        } catch (Throwable th) {
            if (lock != null) {
                Log.v(TAG, "releasing dso store lock for " + this.soDirectory);
                lock.close();
            } else {
                Log.v(TAG, "not releasing dso store lock for " + this.soDirectory + " (syncer thread started)");
            }
            throw th;
        }
    }
}
