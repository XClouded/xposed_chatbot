package com.taobao.monitor.impl.data.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.taobao.monitor.impl.data.activity.ActivityDataCollector;
import com.taobao.monitor.impl.logger.DataLoggerUtils;
import com.taobao.monitor.impl.trace.FragmentFunctionDispatcher;
import com.taobao.monitor.impl.util.TimeUtils;
import java.util.HashMap;
import java.util.Map;

public class FragmentLifecycle extends FragmentManager.FragmentLifecycleCallbacks {
    static final String TAG = "FragmentLifecycle";
    private static FragmentFunctionDispatcher dispatcher = FragmentFunctionDispatcher.INSTANCE;
    private final Activity activity;
    private ActivityDataCollector activityDataCollector;
    protected Map<Fragment, IFragmentLoadLifeCycle> map = new HashMap();

    interface IFragmentLoadLifeCycle {
        void onFragmentActivityCreated(Fragment fragment);

        void onFragmentAttached(Fragment fragment);

        void onFragmentCreated(Fragment fragment);

        void onFragmentDestroyed(Fragment fragment);

        void onFragmentDetached(Fragment fragment);

        void onFragmentPaused(Fragment fragment);

        void onFragmentPreAttached(Fragment fragment);

        void onFragmentPreCreated(Fragment fragment);

        void onFragmentResumed(Fragment fragment);

        void onFragmentSaveInstanceState(Fragment fragment);

        void onFragmentStarted(Fragment fragment);

        void onFragmentStopped(Fragment fragment);

        void onFragmentViewCreated(Fragment fragment);

        void onFragmentViewDestroyed(Fragment fragment);
    }

    public FragmentLifecycle(Activity activity2, ActivityDataCollector activityDataCollector2) {
        this.activity = activity2;
        this.activityDataCollector = activityDataCollector2;
    }

    public void onFragmentPreAttached(FragmentManager fragmentManager, Fragment fragment, Context context) {
        super.onFragmentPreAttached(fragmentManager, fragment, context);
        DataLoggerUtils.log(TAG, "onFragmentPreAttached", fragment.getClass().getSimpleName());
        dispatcher.onFunction(fragment.getActivity(), fragment, "onFragmentPreAttached", TimeUtils.currentTimeMillis());
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle == null) {
            iFragmentLoadLifeCycle = new FragmentDataCollector(this.activity, fragment, this.activityDataCollector);
            this.map.put(fragment, iFragmentLoadLifeCycle);
        }
        iFragmentLoadLifeCycle.onFragmentPreAttached(fragment);
    }

    public void onFragmentAttached(FragmentManager fragmentManager, Fragment fragment, Context context) {
        super.onFragmentAttached(fragmentManager, fragment, context);
        dispatcher.onFunction(fragment.getActivity(), fragment, "onFragmentAttached", TimeUtils.currentTimeMillis());
        DataLoggerUtils.log(TAG, "onFragmentAttached", fragment.getClass().getSimpleName());
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentAttached(fragment);
        }
    }

    public void onFragmentPreCreated(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        super.onFragmentPreCreated(fragmentManager, fragment, bundle);
        dispatcher.onFunction(fragment.getActivity(), fragment, "onFragmentPreCreated", TimeUtils.currentTimeMillis());
        DataLoggerUtils.log(TAG, "onFragmentPreCreated", fragment.getClass().getSimpleName());
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentPreCreated(fragment);
        }
    }

    public void onFragmentCreated(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        super.onFragmentCreated(fragmentManager, fragment, bundle);
        dispatcher.onFunction(fragment.getActivity(), fragment, "onFragmentCreated", TimeUtils.currentTimeMillis());
        DataLoggerUtils.log(TAG, "onFragmentCreated", fragment.getClass().getSimpleName());
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentCreated(fragment);
        }
    }

    public void onFragmentActivityCreated(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        super.onFragmentActivityCreated(fragmentManager, fragment, bundle);
        dispatcher.onFunction(fragment.getActivity(), fragment, "onFragmentActivityCreated", TimeUtils.currentTimeMillis());
        DataLoggerUtils.log(TAG, "onFragmentActivityCreated", fragment.getClass().getSimpleName());
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentActivityCreated(fragment);
        }
    }

    public void onFragmentViewCreated(FragmentManager fragmentManager, Fragment fragment, View view, Bundle bundle) {
        super.onFragmentViewCreated(fragmentManager, fragment, view, bundle);
        dispatcher.onFunction(fragment.getActivity(), fragment, "onFragmentViewCreated", TimeUtils.currentTimeMillis());
        DataLoggerUtils.log(TAG, "onFragmentViewCreated", fragment.getClass().getSimpleName());
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentViewCreated(fragment);
        }
    }

    public void onFragmentStarted(FragmentManager fragmentManager, Fragment fragment) {
        super.onFragmentStarted(fragmentManager, fragment);
        dispatcher.onFunction(fragment.getActivity(), fragment, "onFragmentStarted", TimeUtils.currentTimeMillis());
        DataLoggerUtils.log(TAG, "onFragmentStarted", fragment.getClass().getSimpleName());
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentStarted(fragment);
        }
    }

    public void onFragmentResumed(FragmentManager fragmentManager, Fragment fragment) {
        super.onFragmentResumed(fragmentManager, fragment);
        dispatcher.onFunction(fragment.getActivity(), fragment, "onFragmentResumed", TimeUtils.currentTimeMillis());
        DataLoggerUtils.log(TAG, "onFragmentResumed", fragment.getClass().getSimpleName());
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentResumed(fragment);
        }
    }

    public void onFragmentPaused(FragmentManager fragmentManager, Fragment fragment) {
        super.onFragmentPaused(fragmentManager, fragment);
        dispatcher.onFunction(fragment.getActivity(), fragment, "onFragmentPaused", TimeUtils.currentTimeMillis());
        DataLoggerUtils.log(TAG, "onFragmentPaused", fragment.getClass().getSimpleName());
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentPaused(fragment);
        }
    }

    public void onFragmentStopped(FragmentManager fragmentManager, Fragment fragment) {
        super.onFragmentStopped(fragmentManager, fragment);
        dispatcher.onFunction(fragment.getActivity(), fragment, "onFragmentStopped", TimeUtils.currentTimeMillis());
        DataLoggerUtils.log(TAG, "onFragmentStopped", fragment.getClass().getSimpleName());
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentStopped(fragment);
        }
    }

    public void onFragmentSaveInstanceState(FragmentManager fragmentManager, Fragment fragment, Bundle bundle) {
        super.onFragmentSaveInstanceState(fragmentManager, fragment, bundle);
        dispatcher.onFunction(fragment.getActivity(), fragment, "onFragmentSaveInstanceState", TimeUtils.currentTimeMillis());
        DataLoggerUtils.log(TAG, "onFragmentSaveInstanceState", fragment.getClass().getSimpleName());
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentSaveInstanceState(fragment);
        }
    }

    public void onFragmentViewDestroyed(FragmentManager fragmentManager, Fragment fragment) {
        super.onFragmentViewDestroyed(fragmentManager, fragment);
        dispatcher.onFunction(fragment.getActivity(), fragment, "onFragmentViewDestroyed", TimeUtils.currentTimeMillis());
        DataLoggerUtils.log(TAG, "onFragmentViewDestroyed", fragment.getClass().getSimpleName());
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentViewDestroyed(fragment);
        }
    }

    public void onFragmentDestroyed(FragmentManager fragmentManager, Fragment fragment) {
        super.onFragmentDestroyed(fragmentManager, fragment);
        dispatcher.onFunction(fragment.getActivity(), fragment, "onFragmentDestroyed", TimeUtils.currentTimeMillis());
        DataLoggerUtils.log(TAG, "onFragmentDestroyed", fragment.getClass().getSimpleName());
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentDestroyed(fragment);
        }
    }

    public void onFragmentDetached(FragmentManager fragmentManager, Fragment fragment) {
        super.onFragmentDetached(fragmentManager, fragment);
        dispatcher.onFunction(fragment.getActivity(), fragment, "onFragmentDetached", TimeUtils.currentTimeMillis());
        DataLoggerUtils.log(TAG, "onFragmentDetached", fragment.getClass().getSimpleName());
        IFragmentLoadLifeCycle iFragmentLoadLifeCycle = this.map.get(fragment);
        if (iFragmentLoadLifeCycle != null) {
            iFragmentLoadLifeCycle.onFragmentDetached(fragment);
        }
        this.map.remove(fragment);
    }
}
