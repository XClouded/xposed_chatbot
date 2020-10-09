package com.taobao.monitor.impl.processor.fragmentload;

import androidx.fragment.app.Fragment;

public class FragmentInterceptorProxy implements IFragmentInterceptor {
    public static final FragmentInterceptorProxy INSTANCE = new FragmentInterceptorProxy();
    private IFragmentInterceptor interceptor = null;

    public boolean needPopFragment(Fragment fragment) {
        if (this.interceptor != null) {
            return this.interceptor.needPopFragment(fragment);
        }
        return false;
    }

    public IFragmentInterceptor getInterceptor() {
        return this.interceptor;
    }

    public FragmentInterceptorProxy setInterceptor(IFragmentInterceptor iFragmentInterceptor) {
        this.interceptor = iFragmentInterceptor;
        return this;
    }
}
