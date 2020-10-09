package androidx.core.util;

import android.taobao.windvane.util.WVConstants;
import android.util.AtomicFile;
import androidx.annotation.RequiresApi;
import com.taobao.alivfsadapter.MonitorCacheEvent;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\b\u001a\u0016\u0010\u0003\u001a\u00020\u0004*\u00020\u00022\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007\u001a0\u0010\u0007\u001a\u00020\b*\u00020\u00022!\u0010\t\u001a\u001d\u0012\u0013\u0012\u00110\u000b¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u00020\b0\nH\b\u001a\u0014\u0010\u000f\u001a\u00020\b*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u0001H\u0007\u001a\u001e\u0010\u0011\u001a\u00020\b*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\u0013"}, d2 = {"readBytes", "", "Landroid/util/AtomicFile;", "readText", "", "charset", "Ljava/nio/charset/Charset;", "tryWrite", "", "block", "Lkotlin/Function1;", "Ljava/io/FileOutputStream;", "Lkotlin/ParameterName;", "name", "out", "writeBytes", "array", "writeText", "text", "core-ktx_release"}, k = 2, mv = {1, 1, 10})
/* compiled from: AtomicFile.kt */
public final class AtomicFileKt {
    @RequiresApi(17)
    public static final void tryWrite(@NotNull AtomicFile atomicFile, @NotNull Function1<? super FileOutputStream, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(atomicFile, "$receiver");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        FileOutputStream startWrite = atomicFile.startWrite();
        try {
            Intrinsics.checkExpressionValueIsNotNull(startWrite, MonitorCacheEvent.RESOURCE_STREAM);
            function1.invoke(startWrite);
            InlineMarker.finallyStart(1);
            atomicFile.finishWrite(startWrite);
            InlineMarker.finallyEnd(1);
        } catch (Throwable th) {
            InlineMarker.finallyStart(1);
            atomicFile.failWrite(startWrite);
            InlineMarker.finallyEnd(1);
            throw th;
        }
    }

    @RequiresApi(17)
    public static /* bridge */ /* synthetic */ void writeText$default(AtomicFile atomicFile, String str, Charset charset, int i, Object obj) {
        if ((i & 2) != 0) {
            charset = Charsets.UTF_8;
        }
        writeText(atomicFile, str, charset);
    }

    @RequiresApi(17)
    public static final void writeText(@NotNull AtomicFile atomicFile, @NotNull String str, @NotNull Charset charset) {
        Intrinsics.checkParameterIsNotNull(atomicFile, "$receiver");
        Intrinsics.checkParameterIsNotNull(str, "text");
        Intrinsics.checkParameterIsNotNull(charset, WVConstants.CHARSET);
        byte[] bytes = str.getBytes(charset);
        Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
        writeBytes(atomicFile, bytes);
    }

    @RequiresApi(17)
    @NotNull
    public static final byte[] readBytes(@NotNull AtomicFile atomicFile) {
        Intrinsics.checkParameterIsNotNull(atomicFile, "$receiver");
        byte[] readFully = atomicFile.readFully();
        Intrinsics.checkExpressionValueIsNotNull(readFully, "readFully()");
        return readFully;
    }

    @RequiresApi(17)
    @NotNull
    public static /* bridge */ /* synthetic */ String readText$default(AtomicFile atomicFile, Charset charset, int i, Object obj) {
        if ((i & 1) != 0) {
            charset = Charsets.UTF_8;
        }
        return readText(atomicFile, charset);
    }

    @RequiresApi(17)
    @NotNull
    public static final String readText(@NotNull AtomicFile atomicFile, @NotNull Charset charset) {
        Intrinsics.checkParameterIsNotNull(atomicFile, "$receiver");
        Intrinsics.checkParameterIsNotNull(charset, WVConstants.CHARSET);
        byte[] readFully = atomicFile.readFully();
        Intrinsics.checkExpressionValueIsNotNull(readFully, "readFully()");
        return new String(readFully, charset);
    }

    @RequiresApi(17)
    public static final void writeBytes(@NotNull AtomicFile atomicFile, @NotNull byte[] bArr) {
        Intrinsics.checkParameterIsNotNull(atomicFile, "$receiver");
        Intrinsics.checkParameterIsNotNull(bArr, "array");
        FileOutputStream startWrite = atomicFile.startWrite();
        try {
            Intrinsics.checkExpressionValueIsNotNull(startWrite, MonitorCacheEvent.RESOURCE_STREAM);
            startWrite.write(bArr);
            atomicFile.finishWrite(startWrite);
        } catch (Throwable th) {
            atomicFile.failWrite(startWrite);
            throw th;
        }
    }
}
