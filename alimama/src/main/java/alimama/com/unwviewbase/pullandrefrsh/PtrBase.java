package alimama.com.unwviewbase.pullandrefrsh;

import alimama.com.unwviewbase.R;
import alimama.com.unwviewbase.pullandrefrsh.PullBase;
import android.content.Context;
import android.util.AttributeSet;
import java.util.ArrayList;
import java.util.List;

public class PtrBase extends PullBase {
    public static final int INVALID_VALUE = -1;
    private List<PtrProxy> endPtrProxies;
    private PtrLayout mEndLayout;
    private volatile boolean mFreezeEnd;
    private volatile boolean mFreezeStart;
    private OnRefreshListener mOnRefreshListener;
    private PtrLayout mStartLayout;
    private State mState = State.RESET;
    private List<PtrProxy> startPtrProxies;

    public interface OnRefreshListener {
        void onPullEndToRefresh(PtrBase ptrBase);

        void onPullStartToRefresh(PtrBase ptrBase);
    }

    public PtrBase(Context context) {
        super(context);
    }

    public PtrBase(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PtrBase(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public PtrLayout createStartPullLayout(Context context, PullBase.Mode mode, AttributeSet attributeSet) {
        this.mStartLayout = new PtrLayout(context, mode, getPullDirectionInternal(), attributeSet);
        addStartPtrProxy(this.mStartLayout);
        return this.mStartLayout;
    }

    /* access modifiers changed from: protected */
    public PtrLayout createEndPullLayout(Context context, PullBase.Mode mode, AttributeSet attributeSet) {
        this.mEndLayout = new PtrLayout(context, mode, getPullDirectionInternal(), attributeSet);
        addEndPtrProxy(this.mEndLayout);
        return this.mEndLayout;
    }

    public final void addStartPtrProxy(PtrProxy ptrProxy) {
        if (this.startPtrProxies == null) {
            this.startPtrProxies = new ArrayList();
        }
        if (ptrProxy != null) {
            this.startPtrProxies.add(ptrProxy);
        }
    }

    public final void removeStartPtrProxy(PtrProxy ptrProxy) {
        if (this.endPtrProxies != null) {
            this.endPtrProxies.remove(ptrProxy);
        }
    }

    public final void addEndPtrProxy(PtrProxy ptrProxy) {
        if (this.endPtrProxies == null) {
            this.endPtrProxies = new ArrayList();
        }
        if (ptrProxy != null) {
            this.endPtrProxies.add(ptrProxy);
        }
    }

    public final void removeEndPtrProxy(PtrProxy ptrProxy) {
        if (this.endPtrProxies != null) {
            this.endPtrProxies.remove(ptrProxy);
        }
    }

    /* access modifiers changed from: protected */
    public final void onDirectionUpdated(PullBase.Mode mode, int i) {
        super.onDirectionUpdated(mode, i);
        setState(State.RESET, mode, new CharSequence[0]);
        switch (mode) {
            case DISABLED:
                return;
            case PULL_FROM_START:
                updateStartDirection(i);
                return;
            case PULL_FROM_END:
                updateEndDirection(i);
                return;
            default:
                updateStartDirection(i);
                updateEndDirection(i);
                return;
        }
    }

    private void updateStartDirection(int i) {
        for (PtrProxy onUpdateDirection : this.startPtrProxies) {
            onUpdateDirection.onUpdateDirection(i);
        }
    }

    private void updateEndDirection(int i) {
        for (PtrProxy onUpdateDirection : this.endPtrProxies) {
            onUpdateDirection.onUpdateDirection(i);
        }
    }

    /* access modifiers changed from: protected */
    public final void onModeUpdated(PullBase.Mode mode) {
        super.onModeUpdated(mode);
        switch (mode) {
            case DISABLED:
                for (PtrProxy onDisable : this.startPtrProxies) {
                    onDisable.onDisable();
                }
                for (PtrProxy onDisable2 : this.endPtrProxies) {
                    onDisable2.onDisable();
                }
                return;
            case PULL_FROM_START:
                for (PtrProxy onEnable : this.startPtrProxies) {
                    onEnable.onEnable();
                }
                for (PtrProxy onDisable3 : this.endPtrProxies) {
                    onDisable3.onDisable();
                }
                return;
            case PULL_FROM_END:
                for (PtrProxy onDisable4 : this.startPtrProxies) {
                    onDisable4.onDisable();
                }
                for (PtrProxy onEnable2 : this.endPtrProxies) {
                    onEnable2.onEnable();
                }
                return;
            default:
                for (PtrProxy onEnable3 : this.startPtrProxies) {
                    onEnable3.onEnable();
                }
                for (PtrProxy onEnable4 : this.endPtrProxies) {
                    onEnable4.onEnable();
                }
                return;
        }
    }

    public final void setRefreshing(PullBase.Mode mode, boolean z) {
        if (getMode().isUnderPermit(mode) && !checkIfFreeze(mode) && isIdle()) {
            if (z) {
                int refreshingValue = getRefreshingValue(mode);
                if (refreshingValue != -1) {
                    simulateDrag((int) (((float) refreshingValue) * 1.2f));
                    return;
                }
                return;
            }
            updateCurrentMode(mode);
            setState(State.REFRESHING, mode, new CharSequence[0]);
        }
    }

    private boolean checkIfFreeze(PullBase.Mode mode) {
        switch (mode) {
            case PULL_FROM_START:
                if (this.mFreezeStart) {
                    return true;
                }
                break;
            case PULL_FROM_END:
                if (this.mFreezeEnd) {
                    return true;
                }
                break;
        }
        return false;
    }

    public final void freezeEnd(boolean z, CharSequence charSequence) {
        if (getMode().permitsPullEnd()) {
            this.mFreezeEnd = z;
            for (PtrProxy onFreeze : this.endPtrProxies) {
                onFreeze.onFreeze(z, charSequence);
            }
        }
        if (z && getCurrentMode() == PullBase.Mode.PULL_FROM_END) {
            smoothScrollTo(0, new PullBase.OnSmoothScrollFinishedListener() {
                public void onSmoothScrollFinished() {
                    PtrBase.this.setState(State.RESET, PullBase.Mode.PULL_FROM_END, new CharSequence[0]);
                }
            });
        }
    }

    public final void freezeStart(boolean z, CharSequence charSequence) {
        if (getMode().permitsPullStart()) {
            this.mFreezeStart = z;
            for (PtrProxy onFreeze : this.startPtrProxies) {
                onFreeze.onFreeze(z, charSequence);
            }
        }
        if (z && getCurrentMode() == PullBase.Mode.PULL_FROM_START) {
            smoothScrollTo(0, new PullBase.OnSmoothScrollFinishedListener() {
                public void onSmoothScrollFinished() {
                    PtrBase.this.setState(State.RESET, PullBase.Mode.PULL_FROM_START, new CharSequence[0]);
                }
            });
        }
    }

    public final State getState() {
        return this.mState;
    }

    private boolean isIdle() {
        return getState() == State.RESET && !isRunnableScrolling();
    }

    public PtrLayout getStartLayout() {
        return this.mStartLayout;
    }

    public PtrLayout getEndLayout() {
        return this.mEndLayout;
    }

    public final void setStartLoadingDelegate(PtrLoadingDelegate ptrLoadingDelegate) {
        this.mStartLayout.setLoadingDelegate(ptrLoadingDelegate);
    }

    public final void setEndLoadingDelegate(PtrLoadingDelegate ptrLoadingDelegate) {
        this.mEndLayout.setLoadingDelegate(ptrLoadingDelegate);
    }

    /* access modifiers changed from: protected */
    public final boolean allowCatchPullStartTouch() {
        if (getState() == State.REFRESHING) {
            return false;
        }
        return super.allowCatchPullStartTouch();
    }

    /* access modifiers changed from: protected */
    public final boolean allowCatchPullEndTouch() {
        if (getState() == State.REFRESHING) {
            return false;
        }
        return super.allowCatchPullEndTouch();
    }

    /* access modifiers changed from: protected */
    public boolean allowCheckPullWhenRollBack() {
        if (getState() == State.READY || getState() == State.COMPLETE) {
            return false;
        }
        return super.allowCheckPullWhenRollBack();
    }

    /* access modifiers changed from: protected */
    public final void onPull(PullBase.Mode mode, float f, int i) {
        super.onPull(mode, f, i);
        if (!checkIfFreeze(mode)) {
            pullEvent(mode, (float) i);
        }
    }

    /* access modifiers changed from: protected */
    public final int onRelease(PullBase.Mode mode, float f, int i) {
        if (checkIfFreeze(mode)) {
            return super.onRelease(mode, f, i);
        }
        return releaseEvent(mode, (float) i);
    }

    /* access modifiers changed from: protected */
    public final void onReset(PullBase.Mode mode, float f, int i) {
        if (!checkIfFreeze(mode)) {
            resetEvent(mode, (float) i);
        }
    }

    private int getPullLayoutContentSize(PullBase.Mode mode) {
        int pullDirectionInternal = getPullDirectionInternal();
        switch (mode) {
            case PULL_FROM_START:
                if (!this.mStartLayout.isDisableIntrinsicPullFeature()) {
                    return -this.mStartLayout.getContentSize(pullDirectionInternal);
                }
                break;
            case PULL_FROM_END:
                if (!this.mEndLayout.isDisableIntrinsicPullFeature()) {
                    return this.mEndLayout.getContentSize(pullDirectionInternal);
                }
                break;
        }
        return -1;
    }

    private int getRefreshingValue(PullBase.Mode mode) {
        int readyToRefreshingValue = getPullAdapter() instanceof PtrInterface ? ((PtrInterface) getPullAdapter()).getReadyToRefreshingValue(this, mode, getPullDirectionInternal()) : -1;
        return readyToRefreshingValue == -1 ? getPullLayoutContentSize(mode) : readyToRefreshingValue;
    }

    private int getReleaseValue(PullBase.Mode mode) {
        int releaseTargetValue = getPullAdapter() instanceof PtrInterface ? ((PtrInterface) getPullAdapter()).getReleaseTargetValue(this, mode, getPullDirectionInternal()) : -1;
        return releaseTargetValue == -1 ? getPullLayoutContentSize(mode) : releaseTargetValue;
    }

    private void resetEvent(PullBase.Mode mode, float f) {
        if (getState() == State.READY) {
            setState(State.REFRESHING, mode, new CharSequence[0]);
        } else {
            setState(State.RESET, mode, new CharSequence[0]);
        }
    }

    /* access modifiers changed from: private */
    public synchronized void setState(State state, final PullBase.Mode mode, CharSequence... charSequenceArr) {
        this.mState = state;
        switch (state) {
            case RESET:
                if (!checkIfFreeze(mode)) {
                    onReset(mode);
                    updateCurrentMode(PullBase.Mode.DISABLED);
                    break;
                }
                break;
            case PULL:
            case READY:
                break;
            case REFRESHING:
                if (!checkIfFreeze(mode)) {
                    onRefreshing(mode);
                    callRefreshListener();
                    break;
                }
                break;
            case COMPLETE:
                if (!checkIfFreeze(mode)) {
                    String string = getContext().getString(R.string.ptr_complete_label);
                    if (charSequenceArr != null && charSequenceArr.length > 0) {
                        string = charSequenceArr[0];
                    }
                    onComplete(mode, string);
                    smoothScrollTo(0, allowCheckPullWhenRollBack(), (PullBase.OnSmoothScrollFinishedListener) new PullBase.OnSmoothScrollFinishedListener() {
                        public void onSmoothScrollFinished() {
                            PtrBase.this.setState(State.RESET, mode, new CharSequence[0]);
                        }
                    });
                    break;
                }
                break;
        }
    }

    private void pullEvent(PullBase.Mode mode, float f) {
        onPull(mode, f);
        int refreshingValue = getRefreshingValue(mode);
        if (refreshingValue != -1) {
            switch (mode) {
                case PULL_FROM_START:
                    if (f <= ((float) refreshingValue)) {
                        setState(State.READY, mode, new CharSequence[0]);
                        return;
                    } else {
                        setState(State.PULL, mode, new CharSequence[0]);
                        return;
                    }
                case PULL_FROM_END:
                    if (f >= ((float) refreshingValue)) {
                        setState(State.READY, mode, new CharSequence[0]);
                        return;
                    } else {
                        setState(State.PULL, mode, new CharSequence[0]);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    private void onPull(PullBase.Mode mode, float f) {
        switch (mode) {
            case PULL_FROM_START:
                for (PtrProxy onPull : this.startPtrProxies) {
                    onPull.onPull(f);
                }
                return;
            case PULL_FROM_END:
                for (PtrProxy onPull2 : this.endPtrProxies) {
                    onPull2.onPull(f);
                }
                return;
            default:
                return;
        }
    }

    private int releaseEvent(PullBase.Mode mode, float f) {
        onRelease(mode, f);
        int releaseValue = getState() == State.READY ? getReleaseValue(mode) : 0;
        if (releaseValue == -1) {
            return 0;
        }
        return releaseValue;
    }

    private void onRelease(PullBase.Mode mode, float f) {
        switch (mode) {
            case PULL_FROM_START:
                for (PtrProxy onRelease : this.startPtrProxies) {
                    onRelease.onRelease(f);
                }
                return;
            case PULL_FROM_END:
                for (PtrProxy onRelease2 : this.endPtrProxies) {
                    onRelease2.onRelease(f);
                }
                return;
            default:
                return;
        }
    }

    private void onRefreshing(PullBase.Mode mode) {
        switch (mode) {
            case PULL_FROM_START:
                for (PtrProxy onRefreshing : this.startPtrProxies) {
                    onRefreshing.onRefreshing();
                }
                return;
            case PULL_FROM_END:
                for (PtrProxy onRefreshing2 : this.endPtrProxies) {
                    onRefreshing2.onRefreshing();
                }
                return;
            default:
                return;
        }
    }

    public final void refreshComplete(CharSequence charSequence) {
        if (!checkIfFreeze(getCurrentMode())) {
            setState(State.COMPLETE, getCurrentMode(), charSequence);
        }
    }

    private void onComplete(PullBase.Mode mode, CharSequence charSequence) {
        switch (mode) {
            case PULL_FROM_START:
                for (PtrProxy onCompleteUpdate : this.startPtrProxies) {
                    onCompleteUpdate.onCompleteUpdate(charSequence);
                }
                return;
            case PULL_FROM_END:
                for (PtrProxy onCompleteUpdate2 : this.endPtrProxies) {
                    onCompleteUpdate2.onCompleteUpdate(charSequence);
                }
                return;
            default:
                return;
        }
    }

    private void onReset(PullBase.Mode mode) {
        switch (mode) {
            case PULL_FROM_START:
                for (PtrProxy onReset : this.startPtrProxies) {
                    onReset.onReset();
                }
                return;
            case PULL_FROM_END:
                for (PtrProxy onReset2 : this.endPtrProxies) {
                    onReset2.onReset();
                }
                return;
            default:
                return;
        }
    }

    private void callRefreshListener() {
        if (this.mOnRefreshListener == null) {
            return;
        }
        if (getCurrentMode() == PullBase.Mode.PULL_FROM_START) {
            this.mOnRefreshListener.onPullStartToRefresh(this);
        } else if (getCurrentMode() == PullBase.Mode.PULL_FROM_END) {
            this.mOnRefreshListener.onPullEndToRefresh(this);
        }
    }

    public final void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;
    }

    public enum State {
        RESET(0),
        PULL(1),
        READY(2),
        REFRESHING(3),
        COMPLETE(4);
        
        private int mIntValue;

        static State mapIntToValue(int i) {
            for (State state : values()) {
                if (i == state.getIntValue()) {
                    return state;
                }
            }
            return RESET;
        }

        private State(int i) {
            this.mIntValue = i;
        }

        /* access modifiers changed from: package-private */
        public int getIntValue() {
            return this.mIntValue;
        }
    }
}
