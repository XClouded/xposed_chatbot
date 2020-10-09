package com.taobao.monitor.impl.processor.fragmentload;

import androidx.fragment.app.Fragment;
import com.taobao.monitor.impl.data.GlobalStats;
import com.taobao.monitor.impl.processor.IProcessorFactory;
import com.taobao.monitor.impl.trace.FragmentLifecycleDispatcher;
import java.util.HashMap;
import java.util.Map;

public class FragmentModelLifecycle implements FragmentLifecycleDispatcher.IFragmentLifeCycle {
    private int fragmentCount = 0;
    private final IProcessorFactory<FragmentPopProcessor> fragmentPopProcessorFactory = new FragmentPopProcessorFactory();
    private final IProcessorFactory<FragmentProcessor> fragmentProcessorFactory = new FragmentProcessorFactory();
    private Map<Fragment, IFragmentLoadLifeCycle> map = new HashMap();
    private Map<Fragment, IFragmentPopLifeCycle> popMap = new HashMap();
    private Fragment topFragment;

    public interface IFragmentLoadLifeCycle {
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

    public interface IFragmentPopLifeCycle {
        void onFragmentStarted(Fragment fragment);

        void onFragmentStopped(Fragment fragment);
    }

    public void onFragmentPreAttached(Fragment fragment, long j) {
        GlobalStats.activityStatusManager.setFirst(fragment.getClass().getName());
        IFragmentLoadLifeCycle createProcessor = this.fragmentProcessorFactory.createProcessor();
        if (createProcessor != null) {
            this.map.put(fragment, createProcessor);
            createProcessor.onFragmentPreAttached(fragment, j);
            this.topFragment = fragment;
        }
    }

    public void onFragmentAttached(Fragment fragment, long j) {
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentAttached(fragment, j);
        }
    }

    public void onFragmentPreCreated(Fragment fragment, long j) {
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentPreCreated(fragment, j);
        }
    }

    public void onFragmentCreated(Fragment fragment, long j) {
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentCreated(fragment, j);
        }
    }

    public void onFragmentActivityCreated(Fragment fragment, long j) {
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentActivityCreated(fragment, j);
        }
    }

    public void onFragmentViewCreated(Fragment fragment, long j) {
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentViewCreated(fragment, j);
        }
    }

    public void onFragmentStarted(Fragment fragment, long j) {
        IFragmentPopLifeCycle createProcessor;
        this.fragmentCount++;
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentStarted(fragment, j);
        }
        if (!(this.topFragment == fragment || !FragmentInterceptorProxy.INSTANCE.needPopFragment(fragment) || (createProcessor = this.fragmentPopProcessorFactory.createProcessor()) == null)) {
            createProcessor.onFragmentStarted(fragment);
            this.popMap.put(fragment, createProcessor);
        }
        this.topFragment = fragment;
    }

    public void onFragmentResumed(Fragment fragment, long j) {
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentResumed(fragment, j);
        }
    }

    public void onFragmentPaused(Fragment fragment, long j) {
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentPaused(fragment, j);
        }
    }

    public void onFragmentStopped(Fragment fragment, long j) {
        this.fragmentCount--;
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentStopped(fragment, j);
        }
        IFragmentPopLifeCycle iFragmentPopLifeCycle = this.popMap.get(fragment);
        if (iFragmentPopLifeCycle != null) {
            iFragmentPopLifeCycle.onFragmentStopped(fragment);
            this.popMap.remove(fragment);
        }
        if (this.fragmentCount == 0) {
            this.topFragment = null;
        }
    }

    public void onFragmentSaveInstanceState(Fragment fragment, long j) {
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentSaveInstanceState(fragment, j);
        }
    }

    public void onFragmentViewDestroyed(Fragment fragment, long j) {
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentViewDestroyed(fragment, j);
        }
    }

    public void onFragmentDestroyed(Fragment fragment, long j) {
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentDestroyed(fragment, j);
        }
    }

    public void onFragmentDetached(Fragment fragment, long j) {
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentDetached(fragment, j);
        }
        this.map.remove(fragment);
    }
}
