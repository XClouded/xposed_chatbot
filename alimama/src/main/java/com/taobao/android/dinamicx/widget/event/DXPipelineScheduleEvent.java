package com.taobao.android.dinamicx.widget.event;

public class DXPipelineScheduleEvent extends DXControlEvent {
    public static final String DX_EVENT_PIPELINE_SCHEDULE = "DX_EVENT_PIPELINE_SCHEDULE";
    public int stage;

    public DXPipelineScheduleEvent() {
        this.eventName = DX_EVENT_PIPELINE_SCHEDULE;
    }

    public boolean equals(DXControlEvent dXControlEvent) {
        if (dXControlEvent != null && (dXControlEvent instanceof DXPipelineScheduleEvent) && this.stage == ((DXPipelineScheduleEvent) dXControlEvent).stage) {
            return super.equals(dXControlEvent);
        }
        return false;
    }

    public String toString() {
        return "DXPipelineScheduleEvent{stage=" + this.stage + ", sender=" + this.sender + ", eventName='" + this.eventName + '\'' + ", args=" + this.args + '}';
    }
}
