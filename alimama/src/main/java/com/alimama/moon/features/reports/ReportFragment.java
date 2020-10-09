package com.alimama.moon.features.reports;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import com.alibaba.aliweex.bundle.WeexPageFragment;
import com.alimama.moon.R;
import com.alimama.moon.ui.IBottomNavFragment;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.moon.utils.SpmProcessor;
import com.alimama.moon.utils.UnionLensUtil;
import com.alimama.moon.web.WebPageIntentGenerator;
import com.alimama.union.app.infrastructure.weex.WeexPageGenerator;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.ut.mini.UTAnalytics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportFragment extends WeexPageFragment implements IBottomNavFragment {
    private static final String PAGE_NAME = "Page_tblm_lmapp_ReportCenter";
    private static final String SPM_CNT = "a21wq.9116284";
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) ReportFragment.class);
    private boolean hasDisplayed = false;

    public static ReportFragment newInstance() {
        ReportFragment reportFragment = new ReportFragment();
        reportFragment.setArguments(new Bundle());
        return reportFragment;
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        renderUrl();
    }

    public void willBeDisplayed() {
        UTHelper.sendControlHit(PAGE_NAME, "showReport");
        UnionLensUtil.generatePrepvid();
        UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(getActivity(), PAGE_NAME);
        renderUrl();
    }

    private void renderUrl() {
        if (!this.hasDisplayed) {
            String reportUrl = WeexPageGenerator.getReportUrl();
            startRenderWXByUrl(reportUrl, reportUrl);
            this.hasDisplayed = true;
        }
    }

    public void willBeHidden() {
        UTHelper.sendControlHit(PAGE_NAME, "hiddenReport");
        SpmProcessor.pageDisappear(getActivity(), SPM_CNT);
    }

    public void refresh() {
        String reportUrl = WeexPageGenerator.getReportUrl();
        replace(reportUrl, reportUrl);
    }

    public String currFragmentTitle() {
        return getResources().getString(R.string.tab_account_title);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_report_nav, menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.view_explanation) {
            return true;
        }
        MoonComponentManager.getInstance().getPageRouter().gotoPage(WebPageIntentGenerator.getReportRobotEntrance());
        return true;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }
}
