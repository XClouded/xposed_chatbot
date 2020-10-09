package libcore.io;

import android.system.ErrnoException;
import android.system.StructStat;
import android.system.StructStatVfs;
import android.util.MutableLong;
import java.io.FileDescriptor;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;

public interface Os {
    void chmod(String str, int i) throws ErrnoException;

    void chown(String str, int i, int i2) throws ErrnoException;

    void fchmod(FileDescriptor fileDescriptor, int i) throws ErrnoException;

    void fchown(FileDescriptor fileDescriptor, int i, int i2) throws ErrnoException;

    void fdatasync(FileDescriptor fileDescriptor) throws ErrnoException;

    StructStat fstat(FileDescriptor fileDescriptor) throws ErrnoException;

    StructStatVfs fstatvfs(FileDescriptor fileDescriptor) throws ErrnoException;

    void lchown(String str, int i, int i2) throws ErrnoException;

    void link(String str, String str2) throws ErrnoException;

    long lseek(FileDescriptor fileDescriptor, long j, int i) throws ErrnoException;

    StructStat lstat(String str) throws ErrnoException;

    void mkdir(String str, int i) throws ErrnoException;

    void mkfifo(String str, int i) throws ErrnoException;

    FileDescriptor open(String str, int i, int i2) throws ErrnoException;

    void posix_fallocate(FileDescriptor fileDescriptor, long j, long j2) throws ErrnoException;

    int pread(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j) throws ErrnoException, InterruptedIOException;

    int pread(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, long j) throws ErrnoException, InterruptedIOException;

    int pwrite(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, long j) throws ErrnoException, InterruptedIOException;

    int pwrite(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, long j) throws ErrnoException, InterruptedIOException;

    int read(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws ErrnoException, InterruptedIOException;

    int read(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException;

    String readlink(String str) throws ErrnoException;

    int readv(FileDescriptor fileDescriptor, Object[] objArr, int[] iArr, int[] iArr2) throws ErrnoException, InterruptedIOException;

    String realpath(String str) throws ErrnoException;

    void remove(String str) throws ErrnoException;

    void rename(String str, String str2);

    long sendfile(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, MutableLong mutableLong, long j) throws ErrnoException;

    StructStat stat(String str) throws ErrnoException;

    StructStatVfs statvfs(String str) throws ErrnoException;

    void symlink(String str, String str2) throws ErrnoException;

    int write(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws ErrnoException, InterruptedIOException;

    int write(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws ErrnoException, InterruptedIOException;

    int writev(FileDescriptor fileDescriptor, Object[] objArr, int[] iArr, int[] iArr2) throws ErrnoException, InterruptedIOException;
}
