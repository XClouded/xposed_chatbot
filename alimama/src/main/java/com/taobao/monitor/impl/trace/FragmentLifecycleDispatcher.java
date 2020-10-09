package com.taobao.monitor.impl.trace;

import androidx.fragment.app.Fragment;
import com.taobao.monitor.impl.trace.AbsDispatcher;

public class FragmentLifecycleDispatcher extends AbsDispatcher<IFragmentLifeCycle> {

    public interface IFragmentLifeCycle {
        void onFragmentActivityCreated(Fragment fragment, long j);

        void onFragmentAttached(Fragment fragment, long j);

        void onFragmentCreated(Fragment fragment, long j);

        void onFragmentDestroyed(Fragment fragment, long j);

        void onFragmentDetached(Fragment fragment, long j);

        void onFragmentPaused(Fragment fragment, long j);

        void onFragmentPreAttached(Fragment fragment, long j);

        void onFragmentPreCreated(Fragment fragment, long j);

        void onFragmentResumed(Fragment fragment, long j);

        void onFragmentSaveInstanceState(Fragment fragment, long j);

        void onFragmentStarted(Fragment fragment, long j);

        void onFragmentStopped(Fragment fragment, long j);

        void onFragmentViewCreated(Fragment fragment, long j);

        void onFragmentViewDestroyed(Fragment fragment, long j);
    }

    public void dispatchFragmentPreAttached(final Fragment fragment, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IFragmentLifeCycle>() {
            public void callListener(IFragmentLifeCycle iFragmentLifeCycle) {
                iFragmentLifeCycle.onFragmentPreAttached(fragment, j);
            }
        });
    }

    public void dispatchFragmentAttached(final Fragment fragment, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IFragmentLifeCycle>() {
            public void callListener(IFragmentLifeCycle iFragmentLifeCycle) {
                iFragmentLifeCycle.onFragmentAttached(fragment, j);
            }
        });
    }

    public void dispatchFragmentPreCreated(final Fragment fragment, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IFragmentLifeCycle>() {
            public void callListener(IFragmentLifeCycle iFragmentLifeCycle) {
                iFragmentLifeCycle.onFragmentPreCreated(fragment, j);
            }
        });
    }

    public void dispatchFragmentCreated(final Fragment fragment, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IFragmentLifeCycle>() {
            public void callListener(IFragmentLifeCycle iFragmentLifeCycle) {
                iFragmentLifeCycle.onFragmentCreated(fragment, j);
            }
        });
    }

    public void dispatchFragmentActivityCreated(final Fragment fragment, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IFragmentLifeCycle>() {
            public void callListener(IFragmentLifeCycle iFragmentLifeCycle) {
                iFragmentLifeCycle.onFragmentActivityCreated(fragment, j);
            }
        });
    }

    public void dispatchFragmentViewCreated(final Fragment fragment, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IFragmentLifeCycle>() {
            public void callListener(IFragmentLifeCycle iFragmentLifeCycle) {
                iFragmentLifeCycle.onFragmentViewCreated(fragment, j);
            }
        });
    }

    public void dispatchFragmentStarted(final Fragment fragment, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IFragmentLifeCycle>() {
            public void callListener(IFragmentLifeCycle iFragmentLifeCycle) {
                iFragmentLifeCycle.onFragmentStarted(fragment, j);
            }
        });
    }

    public void dispatchFragmentResumed(final Fragment fragment, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IFragmentLifeCycle>() {
            public void callListener(IFragmentLifeCycle iFragmentLifeCycle) {
                iFragmentLifeCycle.onFragmentResumed(fragment, j);
            }
        });
    }

    public void dispatchFragmentPaused(final Fragment fragment, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IFragmentLifeCycle>() {
            public void callListener(IFragmentLifeCycle iFragmentLifeCycle) {
                iFragmentLifeCycle.onFragmentPaused(fragment, j);
            }
        });
    }

    public void dispatchFragmentStopped(final Fragment fragment, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IFragmentLifeCycle>() {
            public void callListener(IFragmentLifeCycle iFragmentLifeCycle) {
                iFragmentLifeCycle.onFragmentStopped(fragment, j);
            }
        });
    }

    public void dispatchFragmentSaveInstanceState(final Fragment fragment, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IFragmentLifeCycle>() {
            public void callListener(IFragmentLifeCycle iFragmentLifeCycle) {
                iFragmentLifeCycle.onFragmentSaveInstanceState(fragment, j);
            }
        });
    }

    public void dispatchFragmentViewDestroyed(final Fragment fragment, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IFragmentLifeCycle>() {
            public void callListener(IFragmentLifeCycle iFragmentLifeCycle) {
                iFragmentLifeCycle.onFragmentViewDestroyed(fragment, j);
            }
        });
    }

    public void dispatchFragmentDestroyed(final Fragment fragment, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IFragmentLifeCycle>() {
            public void callListener(IFragmentLifeCycle iFragmentLifeCycle) {
                iFragmentLifeCycle.onFragmentDestroyed(fragment, j);
            }
        });
    }

    public void dispatchFragmentDetached(final Fragment fragment, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IFragmentLifeCycle>() {
            public void callListener(IFragmentLifeCycle iFragmentLifeCycle) {
                iFragmentLifeCycle.onFragmentDetached(fragment, j);
            }
        });
    }
}
