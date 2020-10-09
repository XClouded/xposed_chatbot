package com.alimama.moon.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.alibaba.aliweex.bundle.WeexPageFragment;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.alimama.moon.R;
import com.alimama.moon.ui.IBottomNavFragment;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.union.app.infrastructure.weex.WeexPageGenerator;
import com.taobao.android.tlog.protocol.Constants;
import com.uc.webview.export.media.MessageID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscoveryFragment extends WeexPageFragment implements IBottomNavFragment {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) DiscoveryFragment.class);
    private boolean hasDisplayed = false;

    public static DiscoveryFragment newInstance() {
        DiscoveryFragment discoveryFragment = new DiscoveryFragment();
        discoveryFragment.setArguments(new Bundle());
        return discoveryFragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        logger.info("onAttach");
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        logger.info(UmbrellaConstants.LIFECYCLE_CREATE);
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        logger.info(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_CREATED);
        renderUrl();
    }

    public void onStart() {
        super.onStart();
        logger.info(UmbrellaConstants.LIFECYCLE_START);
    }

    public void onResume() {
        super.onResume();
        logger.info(UmbrellaConstants.LIFECYCLE_RESUME);
    }

    public void onPause() {
        super.onPause();
        logger.info(MessageID.onPause);
    }

    public void onStop() {
        super.onStop();
        logger.info(MessageID.onStop);
    }

    public void onDestroyView() {
        super.onDestroyView();
        logger.info("onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        logger.info("onDestroy");
    }

    public void onDetach() {
        super.onDetach();
        logger.info("onDetach");
    }

    public void willBeDisplayed() {
        logger.info("willBeDisplayed");
        SpmProcessor.pageAppear(this, UTHelper.HomePage.PAGE_NAME);
        renderUrl();
    }

    private void renderUrl() {
        if (!this.hasDisplayed) {
            String uri = WeexPageGenerator.getIndexUri().toString();
            logger.info("weex page url: {}", (Object) uri);
            startRenderWXByUrl(uri, uri);
            this.hasDisplayed = true;
        }
    }

    public void willBeHidden() {
        logger.info("willBeHidden");
        SpmProcessor.pageDisappear(this, UTHelper.HomePage.SPM_CNT);
    }

    public void refresh() {
        String uri = WeexPageGenerator.getIndexUri().toString();
        logger.info("weex page url: {}", (Object) uri);
        replace(uri, uri);
    }

    public String currFragmentTitle() {
        return getResources().getString(R.string.tab_title_default);
    }
}
