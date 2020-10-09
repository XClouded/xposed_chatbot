package com.taobao.monitor.impl.data.newvisible;

import alimama.com.unweventparse.constants.EventConstants;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import com.taobao.monitor.impl.data.IExecutor;
import com.taobao.monitor.impl.data.IInteractiveDetector;
import com.taobao.monitor.impl.data.IVisibleDetector;
import com.taobao.monitor.impl.util.TimeUtils;
import com.taobao.monitor.impl.util.TopicUtils;
import com.taobao.monitor.procedure.IProcedure;
import com.taobao.monitor.procedure.ProcedureConfig;
import com.taobao.monitor.procedure.ProcedureFactoryProxy;

public class NewIVDetector implements IExecutor {
    private static final String TAG = "NewIVDetector";
    private boolean callOnce = false;
    /* access modifiers changed from: private */
    public boolean hasVisibleType = false;
    final InteractiveDetectorFrameImpl interactiveDetector;
    final String pageName;
    /* access modifiers changed from: private */
    public IProcedure procedure;
    private long startTime = SystemClock.uptimeMillis();
    final VisibleDetectorStatusImpl visibleDetector;

    private void init() {
        this.procedure = ProcedureFactoryProxy.PROXY.createProcedure(TopicUtils.getFullTopic("/pageLoad"), new ProcedureConfig.Builder().setIndependent(false).setUpload(true).setParentNeedStats(true).setParent((IProcedure) null).build());
        this.procedure.begin();
    }

    public NewIVDetector(View view, String str, String str2, long j, float f) {
        init();
        this.procedure.addProperty("apm_current_time", Long.valueOf(j));
        this.procedure.stage("loadStartTime", j);
        this.procedure.stage("renderStartTime", TimeUtils.currentTimeMillis());
        this.pageName = str;
        this.interactiveDetector = new InteractiveDetectorFrameImpl(100, this.procedure);
        this.interactiveDetector.setCallback(new IInteractiveDetector.IDetectorCallback() {
            public void completed(long j) {
                NewIVDetector.this.procedure.addProperty("apm_interactive_time", Long.valueOf(j));
                NewIVDetector.this.procedure.stage("interactiveTime", j);
                NewIVDetector.this.procedure.stage("skiInteractiveTime", j);
            }
        });
        this.visibleDetector = new VisibleDetectorStatusImpl(view, str, f);
        this.visibleDetector.setCallback(new IVisibleDetector.IDetectorCallback() {
            public void changed(long j) {
            }

            public void completed(long j) {
                NewIVDetector.this.visibleDetector.visibleEndByType("VISIBLE");
                NewIVDetector.this.procedure.addProperty("apm_visible_time", Long.valueOf(j));
                NewIVDetector.this.procedure.addProperty("apm_cal_visible_time", Long.valueOf(TimeUtils.currentTimeMillis()));
                if (!NewIVDetector.this.hasVisibleType) {
                    NewIVDetector.this.procedure.addProperty("apm_visible_type", "normal");
                    NewIVDetector.this.procedure.stage("displayedTime", j);
                    boolean unused = NewIVDetector.this.hasVisibleType = true;
                }
                NewIVDetector.this.interactiveDetector.setVisibleTime(j);
            }

            public void validElement(int i) {
                NewIVDetector.this.procedure.addProperty("apm_visible_valid_count", Integer.valueOf(i));
            }
        });
        if (!TextUtils.isEmpty(str2)) {
            this.procedure.addProperty("apm_url", str2);
        }
        str.substring(str.lastIndexOf(".") + 1);
    }

    public void execute() {
        this.interactiveDetector.execute();
        this.visibleDetector.execute();
        this.procedure.addProperty("apm_first_paint", Long.valueOf(TimeUtils.currentTimeMillis()));
    }

    public void stop() {
        if (!this.hasVisibleType) {
            this.procedure.addProperty("apm_visible_type", "left");
            this.procedure.stage("displayedTime", this.visibleDetector.getLastChangedTime());
            this.hasVisibleType = true;
        }
        this.visibleDetector.visibleEndByType("LEFT");
        this.visibleDetector.stop();
        this.interactiveDetector.stop();
        IProcedure iProcedure = this.procedure;
        iProcedure.addProperty(EventConstants.UT.PAGE_NAME, "apm." + this.pageName);
        this.procedure.addProperty("apm_page_name", this.pageName);
        this.procedure.addProperty("apm_left_time", Long.valueOf(TimeUtils.currentTimeMillis()));
        this.procedure.addProperty("apm_left_visible_time", Long.valueOf(this.visibleDetector.getLastChangedTime()));
        this.procedure.addProperty("apm_left_usable_time", Long.valueOf(this.interactiveDetector.getUsableTime()));
        this.procedure.end();
    }

    public void visibleProxyAction() {
        if (!this.callOnce) {
            if (!this.hasVisibleType) {
                this.procedure.addProperty("apm_visible_type", "touch");
                this.procedure.stage("displayedTime", this.visibleDetector.getLastChangedTime());
                this.hasVisibleType = true;
            }
            this.procedure.stage("firstInteractiveTime", TimeUtils.currentTimeMillis());
            this.visibleDetector.visibleEndByType("TOUCH");
            this.procedure.addProperty("apm_touch_time", Long.valueOf(TimeUtils.currentTimeMillis()));
            this.procedure.addProperty("apm_touch_visible_time", Long.valueOf(this.visibleDetector.getLastChangedTime()));
            this.procedure.addProperty("apm_touch_usable_time", Long.valueOf(this.interactiveDetector.getUsableTime()));
            this.visibleDetector.stop();
            this.interactiveDetector.setVisibleTime(this.visibleDetector.getLastChangedTime());
            this.callOnce = true;
        }
    }
}
