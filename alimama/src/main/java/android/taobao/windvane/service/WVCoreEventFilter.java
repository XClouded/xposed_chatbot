package android.taobao.windvane.service;

public abstract class WVCoreEventFilter implements WVEventListener {
    /* access modifiers changed from: protected */
    public void onCoreSwitch() {
    }

    /* access modifiers changed from: protected */
    public void onUCCorePrepared() {
    }

    public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
        WVEventResult wVEventResult = new WVEventResult(false);
        switch (i) {
            case 3016:
                onUCCorePrepared();
                break;
            case 3017:
                onCoreSwitch();
                break;
        }
        return wVEventResult;
    }
}
