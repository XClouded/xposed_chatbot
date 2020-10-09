package libcore.io;

import android.system.ErrnoException;
import android.system.StructStat;
import android.system.StructStatVfs;
import android.util.MutableLong;
import java.io.FileDescriptor;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;

public class BlockGuardOs implements Os {
    public void chmod(String str, int i) throws ErrnoException {
    }

    public void chown(String str, int i, int i2) throws ErrnoException {
    }

    public void close(FileDescriptor fileDescriptor) throws ErrnoException {
    }

    public void fchmod(FileDescriptor fileDescriptor, int i) throws ErrnoException {
    }

    public void fchown(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException {
    }

    public void fdatasync(FileDescriptor fileDescriptor) throws ErrnoException {
    }

    public StructStat fstat(FileDescriptor fileDescriptor) throws ErrnoException {
        return null;
    }

    public StructStatVfs fstatvfs(FileDescriptor fileDescriptor) throws ErrnoException {
        return null;
    }

    public void lchown(String str, int i, int i2) throws ErrnoException {
    }

    public void link(String str, String str2) throws ErrnoException {
    }

    public long lseek(FileDescriptor fileDescriptor, long j, int i) throws ErrnoException {
        return 0;
    }

    public StructStat lstat(String str) throws ErrnoException {
        return null;
    }

    public void mkdir(String str, int i) throws ErrnoException {
    }

    public void mkfifo(String str, int i) throws ErrnoException {
    }

    public FileDescriptor open(String str, int i, int i2) throws ErrnoException {
        return null;
    }

    public void posix_fallocate(FileDescriptor fileDescriptor, long j, long j2) throws ErrnoException {
    }

    public int pread(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j) throws ErrnoException, InterruptedIOException {
        return 0;
    }

    public int pread(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, long j) throws ErrnoException, InterruptedIOException {
        return 0;
    }

    public int pwrite(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j) throws ErrnoException, InterruptedIOException {
        return 0;
    }

    public int pwrite(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, long j) throws ErrnoException, InterruptedIOException {
        return 0;
    }

    public int read(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws ErrnoException, InterruptedIOException {
        return 0;
    }

    public int read(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException {
        return 0;
    }

    public String readlink(String str) throws ErrnoException {
        return null;
    }

    public int readv(FileDescriptor fileDescriptor, Object[] objArr, int[] iArr, int[] iArr2) throws ErrnoException, InterruptedIOException {
        return 0;
    }

    public String realpath(String str) throws ErrnoException {
        return null;
    }

    public void remove(String str) throws ErrnoException {
    }

    public void rename(String str, String str2) {
    }

    public long sendfile(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, MutableLong mutableLong, long j) throws ErrnoException {
        return 0;
    }

    public StructStat stat(String str) throws ErrnoException {
        return null;
    }

    public StructStatVfs statvfs(String str) throws ErrnoException {
        return null;
    }

    public void symlink(String str, String str2) throws ErrnoException {
    }

    public int write(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws ErrnoException, InterruptedIOException {
        return 0;
    }

    public int write(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException {
        return 0;
    }

    public int writev(FileDescriptor fileDescriptor, Object[] objArr, int[] iArr, int[] iArr2) throws ErrnoException, InterruptedIOException {
        return 0;
    }

    public BlockGuardOs(Os os) {
    }
}
