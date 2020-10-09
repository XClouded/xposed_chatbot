package com.alimama.moon.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import com.alimama.moon.App;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.eventbus.ISubscriber;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.utils.ToastUtil;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseFragment extends Fragment implements ISubscriber {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) BaseFragment.class);
    @Inject
    IEventBus eventBus;
    protected boolean isForeground;
    private String mTitle = null;
    public View storedView;

    public void beforeCreateView() {
    }

    public abstract View returnCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle);

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        App.getAppComponent().inject(this);
        registerEventBus();
    }

    public void onDestroyView() {
        unregisterEventBus();
        super.onDestroyView();
    }

    private void registerEventBus() {
        if (!this.eventBus.isRegistered(this)) {
            try {
                this.eventBus.register(this);
            } catch (EventBusException e) {
                logger.error("EventBus register exception: {}", (Object) e.getMessage());
            }
        }
    }

    private void unregisterEventBus() {
        if (this.eventBus.isRegistered(this)) {
            try {
                this.eventBus.unregister(this);
            } catch (EventBusException e) {
                logger.error("EventBus unregister exception: {}", (Object) e.getMessage());
            }
        }
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
    }

    public void onStop() {
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public ActionBar getSupportActionBar() {
        ((BaseActivity) getActivity()).getSupportActionBar().setElevation(0.0f);
        return ((BaseActivity) getActivity()).getSupportActionBar();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        try {
            beforeCreateView();
            if (this.storedView != null) {
                ViewGroup viewGroup2 = (ViewGroup) this.storedView.getParent();
                if (viewGroup2 != null) {
                    viewGroup2.removeView(this.storedView);
                }
                return this.storedView;
            }
            this.storedView = returnCreateView(layoutInflater, viewGroup, bundle);
            return this.storedView;
        } catch (Exception e) {
            ToastUtil.toast((Context) getActivity(), (CharSequence) e.getMessage());
            return returnCreateView(layoutInflater, viewGroup, bundle);
        }
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String str) {
        this.mTitle = str;
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle((CharSequence) this.mTitle);
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        this.isForeground = z;
    }
}
