package com.alimama.moon.eventbus;

public final class LoginEvent {

    public static final class LoginCancelEvent implements IEvent {
    }

    public static final class LoginSuccessEvent implements IEvent {
    }

    public static final class LoginSystemErrorEvent implements IEvent {
    }

    public static final class LogoutEvent implements IEvent {
    }

    public static final class MamaAccountFrozenEvent implements IEvent {
    }

    public static final class NeedAgreementEvent implements IEvent {
    }

    public static final class NotMatchAccountConditionEvent implements IEvent {
    }

    public static final class TaobaoAccountNotSecurityEvent implements IEvent {
    }
}
