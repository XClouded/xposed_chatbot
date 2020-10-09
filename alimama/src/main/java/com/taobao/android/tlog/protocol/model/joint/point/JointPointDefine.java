package com.taobao.android.tlog.protocol.model.joint.point;

public enum JointPointDefine {
    LIFECYCLE("lifecycle", LifecycleJointPoint.class),
    NOTIFICATION("notification", NotificationJointPoint.class),
    STARTUP(StartupJointPoint.TYPE, StartupJointPoint.class),
    TIMER(TimerJointPoint.TYPE, TimerJointPoint.class),
    CUSTOM_JOINT_POINT("event", EventJointPoint.class),
    BACKGROUND(BackgroundJointPoint.TYPE, BackgroundJointPoint.class),
    FOREGROUND(ForegroundJointPoint.TYPE, ForegroundJointPoint.class);
    
    private Class<? extends JointPoint> jointPointClass;
    private String jointPointType;

    private JointPointDefine(String str, Class<? extends JointPoint> cls) {
        this.jointPointType = str;
        this.jointPointClass = cls;
    }

    public String toString() {
        return this.jointPointType;
    }

    public Class<? extends JointPoint> getJointPointClass() {
        return this.jointPointClass;
    }

    public static JointPointDefine fromName(String str) {
        for (JointPointDefine jointPointDefine : values()) {
            if (jointPointDefine.jointPointType.equalsIgnoreCase(str)) {
                return jointPointDefine;
            }
        }
        return null;
    }
}
