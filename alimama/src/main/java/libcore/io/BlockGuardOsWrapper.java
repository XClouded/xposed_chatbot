package libcore.io;

import android.system.ErrnoException;
import android.system.StructStat;
import android.system.StructStatVfs;
import android.util.MutableLong;
import com.ali.telescope.util.Reflector;
import java.io.FileDescriptor;
import java.io.InterruptedIOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class BlockGuardOsWrapper extends BlockGuardOs {
    private static BlockGuardOsWrapper sInstance;
    private FDNewListener mFDNewListener;
    private IOEventListener mIOEventListener;

    public interface FDNewListener {
        void onNewFd(FileDescriptor fileDescriptor);
    }

    public interface IOEventListener {
        void onReadFromDisk(int i);

        void onWriteToDisk(int i);
    }

    private BlockGuardOsWrapper(Os os) {
        super(os);
    }

    public void setIOEventListener(IOEventListener iOEventListener) {
        this.mIOEventListener = iOEventListener;
    }

    public void setFDNewListener(FDNewListener fDNewListener) {
        this.mFDNewListener = fDNewListener;
    }

    public static synchronized BlockGuardOsWrapper instance() {
        BlockGuardOsWrapper blockGuardOsWrapper;
        BlockGuardOsWrapper blockGuardOsWrapper2;
        synchronized (BlockGuardOsWrapper.class) {
            if (sInstance == null) {
                try {
                    Field field = Reflector.field(BlockGuardOsWrapper.class.getClassLoader(), "libcore.io.Libcore", "os");
                    field.setAccessible(true);
                    Object obj = field.get((Object) null);
                    Class superclass = obj.getClass().getSuperclass();
                    Field field2 = null;
                    while (true) {
                        if (superclass == null) {
                            break;
                        }
                        try {
                            Field declaredField = superclass.getDeclaredField("os");
                            if (declaredField != null) {
                                field2 = declaredField;
                                break;
                            }
                            field2 = declaredField;
                            superclass = superclass.getSuperclass();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (field2 != null) {
                        field2.setAccessible(true);
                        sInstance = new BlockGuardOsWrapper((Os) field2.get(obj));
                        field.set((Object) null, sInstance);
                    }
                    if (sInstance == null) {
                        blockGuardOsWrapper2 = new BlockGuardOsWrapper((Os) null);
                        sInstance = blockGuardOsWrapper2;
                    }
                } catch (Exception e2) {
                    try {
                        e2.printStackTrace();
                        if (sInstance == null) {
                            blockGuardOsWrapper2 = new BlockGuardOsWrapper((Os) null);
                        }
                    } catch (Throwable th) {
                        if (sInstance == null) {
                            sInstance = new BlockGuardOsWrapper((Os) null);
                        }
                        throw th;
                    }
                }
            }
            blockGuardOsWrapper = sInstance;
        }
        return blockGuardOsWrapper;
    }

    public void chmod(String str, int i) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        super.chmod(str, i);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
    }

    public void chown(String str, int i, int i2) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        super.chown(str, i, i2);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
    }

    public void fchmod(FileDescriptor fileDescriptor, int i) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        super.fchmod(fileDescriptor, i);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
    }

    public void fchown(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        super.fchown(fileDescriptor, i, i2);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
    }

    public void fdatasync(FileDescriptor fileDescriptor) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        super.fdatasync(fileDescriptor);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
    }

    public StructStat fstat(FileDescriptor fileDescriptor) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        StructStat fstat = super.fstat(fileDescriptor);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return fstat;
    }

    public StructStatVfs fstatvfs(FileDescriptor fileDescriptor) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        StructStatVfs fstatvfs = super.fstatvfs(fileDescriptor);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return fstatvfs;
    }

    public void lchown(String str, int i, int i2) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        super.lchown(str, i, i2);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
    }

    public void link(String str, String str2) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        super.link(str, str2);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
    }

    public long lseek(FileDescriptor fileDescriptor, long j, int i) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        long lseek = super.lseek(fileDescriptor, j, i);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return lseek;
    }

    public StructStat lstat(String str) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        StructStat lstat = super.lstat(str);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return lstat;
    }

    public void mkdir(String str, int i) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        super.mkdir(str, i);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
    }

    public void mkfifo(String str, int i) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        super.mkfifo(str, i);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
    }

    public FileDescriptor open(String str, int i, int i2) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        FileDescriptor open = super.open(str, i, i2);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return open;
    }

    public void posix_fallocate(FileDescriptor fileDescriptor, long j, long j2) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        super.posix_fallocate(fileDescriptor, j, j2);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
    }

    public int pread(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j) throws ErrnoException, InterruptedIOException {
        long currentTimeMillis = System.currentTimeMillis();
        int pread = super.pread(fileDescriptor, byteBuffer, j);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return pread;
    }

    public int pread(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, long j) throws ErrnoException, InterruptedIOException {
        long currentTimeMillis = System.currentTimeMillis();
        int pread = super.pread(fileDescriptor, bArr, i, i2, j);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return pread;
    }

    public int pwrite(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j) throws ErrnoException, InterruptedIOException {
        long currentTimeMillis = System.currentTimeMillis();
        int pwrite = super.pwrite(fileDescriptor, byteBuffer, j);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return pwrite;
    }

    public int pwrite(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, long j) throws ErrnoException, InterruptedIOException {
        long currentTimeMillis = System.currentTimeMillis();
        int pwrite = super.pwrite(fileDescriptor, bArr, i, i2, j);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return pwrite;
    }

    public int read(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws ErrnoException, InterruptedIOException {
        long currentTimeMillis = System.currentTimeMillis();
        int read = super.read(fileDescriptor, byteBuffer);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return read;
    }

    public int read(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException {
        long currentTimeMillis = System.currentTimeMillis();
        int read = super.read(fileDescriptor, bArr, i, i2);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return read;
    }

    public String readlink(String str) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        String readlink = super.readlink(str);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return readlink;
    }

    public String realpath(String str) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        String realpath = super.realpath(str);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return realpath;
    }

    public int readv(FileDescriptor fileDescriptor, Object[] objArr, int[] iArr, int[] iArr2) throws ErrnoException, InterruptedIOException {
        long currentTimeMillis = System.currentTimeMillis();
        int readv = super.readv(fileDescriptor, objArr, iArr, iArr2);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return readv;
    }

    public void remove(String str) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        super.remove(str);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
    }

    public void rename(String str, String str2) {
        long currentTimeMillis = System.currentTimeMillis();
        super.rename(str, str2);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
    }

    public long sendfile(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, MutableLong mutableLong, long j) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        long sendfile = super.sendfile(fileDescriptor, fileDescriptor2, mutableLong, j);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return sendfile;
    }

    public StructStat stat(String str) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        StructStat stat = super.stat(str);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return stat;
    }

    public StructStatVfs statvfs(String str) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        StructStatVfs statvfs = super.statvfs(str);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return statvfs;
    }

    public void symlink(String str, String str2) throws ErrnoException {
        long currentTimeMillis = System.currentTimeMillis();
        super.symlink(str, str2);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onReadFromDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
    }

    public int write(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws ErrnoException, InterruptedIOException {
        long currentTimeMillis = System.currentTimeMillis();
        int write = super.write(fileDescriptor, byteBuffer);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return write;
    }

    public int write(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException {
        long currentTimeMillis = System.currentTimeMillis();
        int write = super.write(fileDescriptor, bArr, i, i2);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return write;
    }

    public int writev(FileDescriptor fileDescriptor, Object[] objArr, int[] iArr, int[] iArr2) throws ErrnoException, InterruptedIOException {
        long currentTimeMillis = System.currentTimeMillis();
        int writev = super.writev(fileDescriptor, objArr, iArr, iArr2);
        long currentTimeMillis2 = System.currentTimeMillis();
        if (this.mIOEventListener != null) {
            this.mIOEventListener.onWriteToDisk((int) (currentTimeMillis2 - currentTimeMillis));
        }
        return writev;
    }

    public void close(FileDescriptor fileDescriptor) throws ErrnoException {
        if (this.mFDNewListener != null) {
            this.mFDNewListener.onNewFd(fileDescriptor);
        }
        super.close(fileDescriptor);
    }
}
