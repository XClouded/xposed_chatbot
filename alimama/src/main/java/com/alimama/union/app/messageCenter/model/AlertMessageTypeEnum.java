package com.alimama.union.app.messageCenter.model;

public enum AlertMessageTypeEnum {
    ENTER_REVIEW_PERIOD(1, "进入审核期消息"),
    QUIT_REVIEW_PERIOD(2, "退出审核期消息"),
    PRE_DOWNGRADE(3, "预降级消息"),
    DOWNGRADE(4, "降级消息"),
    UPGRADE(5, "升级消息"),
    NEW_USER_GRADE(6, "新用户注册等级提醒消息"),
    CHECK_NOTICE(7, "考核预告消息");
    
    private String desc;
    private Integer msgType;

    private AlertMessageTypeEnum(Integer num, String str) {
        this.msgType = num;
        this.desc = str;
    }

    public Integer getMsgType() {
        return this.msgType;
    }

    public String getDesc() {
        return this.desc;
    }

    public static AlertMessageTypeEnum valueOf(Integer num) {
        for (AlertMessageTypeEnum alertMessageTypeEnum : values()) {
            if (alertMessageTypeEnum.getMsgType() == num) {
                return alertMessageTypeEnum;
            }
        }
        return null;
    }
}
