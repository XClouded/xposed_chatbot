package androidx.recyclerview.selection;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.List;

public final class OperationMonitor {
    private static final String TAG = "OperationMonitor";
    private List<OnChangeListener> mListeners = new ArrayList();
    private int mNumOps = 0;

    public interface OnChangeListener {
        void onChanged();
    }

    /* access modifiers changed from: package-private */
    @MainThread
    public synchronized void start() {
        this.mNumOps++;
        if (this.mNumOps == 1) {
            for (OnChangeListener onChanged : this.mListeners) {
                onChanged.onChanged();
            }
        }
    }

    /* access modifiers changed from: package-private */
    @MainThread
    public synchronized void stop() {
        Preconditions.checkState(this.mNumOps > 0);
        this.mNumOps--;
        if (this.mNumOps == 0) {
            for (OnChangeListener onChanged : this.mListeners) {
                onChanged.onChanged();
            }
        }
    }

    public synchronized boolean isStarted() {
        return this.mNumOps > 0;
    }

    public void addListener(@NonNull OnChangeListener onChangeListener) {
        Preconditions.checkArgument(onChangeListener != null);
        this.mListeners.add(onChangeListener);
    }

    public void removeListener(@NonNull OnChangeListener onChangeListener) {
        Preconditions.checkArgument(onChangeListener != null);
        this.mListeners.remove(onChangeListener);
    }

    /* access modifiers changed from: package-private */
    public void checkStarted() {
        Preconditions.checkState(this.mNumOps > 0);
    }

    /* access modifiers changed from: package-private */
    public void checkStopped() {
        Preconditions.checkState(this.mNumOps == 0);
    }
}
