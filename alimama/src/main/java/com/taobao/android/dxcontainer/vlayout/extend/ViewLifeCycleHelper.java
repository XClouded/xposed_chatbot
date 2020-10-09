package com.taobao.android.dxcontainer.vlayout.extend;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;

public class ViewLifeCycleHelper {
    private ViewLifeCycleListener mViewLifeCycleListener;
    private ArrayMap<View, STATUS> mViewStatusMap = new ArrayMap<>();
    private VirtualLayoutManager mVirtualLayoutManager;
    private int scrHeight;

    public enum STATUS {
        APPEARING,
        APPEARED,
        DISAPPEARING,
        DISAPPEARED
    }

    public ViewLifeCycleHelper(VirtualLayoutManager virtualLayoutManager, @NonNull ViewLifeCycleListener viewLifeCycleListener) {
        this.mViewLifeCycleListener = viewLifeCycleListener;
        this.mVirtualLayoutManager = virtualLayoutManager;
    }

    public void checkViewStatusInScreen() {
        for (int i = 0; i < this.mVirtualLayoutManager.getChildCount(); i++) {
            View childAt = this.mVirtualLayoutManager.getChildAt(i);
            if (this.scrHeight == 0) {
                this.scrHeight = childAt.getContext().getResources().getDisplayMetrics().heightPixels;
            }
            if (this.mVirtualLayoutManager.getVirtualLayoutDirection() == 1) {
                if (childAt.getTop() <= 0 && childAt.getBottom() >= 0 && isViewReadyDisAppearing(childAt)) {
                    setViewDisappearing(childAt);
                } else if (childAt.getTop() <= this.scrHeight && childAt.getBottom() >= this.scrHeight && isViewReadyAppearing(childAt)) {
                    setViewAppearing(childAt);
                }
            } else if (childAt.getTop() <= 0 && childAt.getBottom() >= 0 && isViewReadyAppearing(childAt)) {
                setViewAppearing(childAt);
            } else if (childAt.getTop() <= this.scrHeight && childAt.getBottom() >= this.scrHeight && isViewReadyDisAppearing(childAt)) {
                setViewDisappearing(childAt);
            }
            if (childAt.getTop() < 0 || childAt.getBottom() > this.scrHeight) {
                if (childAt.getBottom() <= 0 || childAt.getTop() >= this.scrHeight) {
                    if (isViewReadyDisAppearing(childAt)) {
                        setViewDisappearing(childAt);
                    } else if (isViewReadyDisAppeared(childAt)) {
                        setViewDisappeared(childAt);
                    }
                }
            } else if (isViewReadyAppearing(childAt)) {
                setViewAppearing(childAt);
            } else if (isViewReadyAppeared(childAt)) {
                setViewAppeared(childAt);
            }
        }
    }

    private STATUS getViewStatus(View view) {
        if (this.mViewStatusMap.containsKey(view)) {
            return this.mViewStatusMap.get(view);
        }
        this.mViewStatusMap.put(view, STATUS.DISAPPEARED);
        return STATUS.DISAPPEARED;
    }

    private void setViewstatus(View view, STATUS status) {
        this.mViewStatusMap.put(view, status);
    }

    private boolean isViewReadyAppearing(View view) {
        return getViewStatus(view) == STATUS.DISAPPEARED;
    }

    private void setViewAppearing(View view) {
        if (getViewStatus(view) != STATUS.APPEARING) {
            setViewstatus(view, STATUS.APPEARING);
            if (this.mViewLifeCycleListener != null) {
                this.mViewLifeCycleListener.onAppearing(view);
            }
        }
    }

    private boolean isViewReadyAppeared(View view) {
        return getViewStatus(view) == STATUS.APPEARING;
    }

    private void setViewAppeared(View view) {
        if (getViewStatus(view) != STATUS.APPEARED) {
            setViewstatus(view, STATUS.APPEARED);
            if (this.mViewLifeCycleListener != null) {
                this.mViewLifeCycleListener.onAppeared(view);
            }
        }
    }

    private boolean isViewReadyDisAppearing(View view) {
        return getViewStatus(view) == STATUS.APPEARED;
    }

    private void setViewDisappearing(View view) {
        if (getViewStatus(view) != STATUS.DISAPPEARING) {
            setViewstatus(view, STATUS.DISAPPEARING);
            if (this.mViewLifeCycleListener != null) {
                this.mViewLifeCycleListener.onDisappearing(view);
            }
        }
    }

    private boolean isViewReadyDisAppeared(View view) {
        return getViewStatus(view) == STATUS.DISAPPEARING;
    }

    private void setViewDisappeared(View view) {
        if (getViewStatus(view) != STATUS.DISAPPEARED) {
            setViewstatus(view, STATUS.DISAPPEARED);
            if (this.mViewLifeCycleListener != null) {
                this.mViewLifeCycleListener.onDisappeared(view);
            }
        }
    }
}
