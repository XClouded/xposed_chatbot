package androidx.recyclerview.selection;

import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;
import java.util.Arrays;
import java.util.List;

final class ToolHandlerRegistry<T> {
    private static final int NUM_INPUT_TYPES = 5;
    private final T mDefault;
    private final List<T> mHandlers = Arrays.asList(new Object[]{null, null, null, null, null});

    ToolHandlerRegistry(@NonNull T t) {
        boolean z = true;
        Preconditions.checkArgument(t == null ? false : z);
        this.mDefault = t;
        for (int i = 0; i < 5; i++) {
            this.mHandlers.set(i, (Object) null);
        }
    }

    /* access modifiers changed from: package-private */
    public void set(int i, @Nullable T t) {
        boolean z = true;
        Preconditions.checkArgument(i >= 0 && i <= 4);
        if (this.mHandlers.get(i) != null) {
            z = false;
        }
        Preconditions.checkState(z);
        this.mHandlers.set(i, t);
    }

    /* access modifiers changed from: package-private */
    public T get(@NonNull MotionEvent motionEvent) {
        T t = this.mHandlers.get(motionEvent.getToolType(0));
        return t != null ? t : this.mDefault;
    }
}
