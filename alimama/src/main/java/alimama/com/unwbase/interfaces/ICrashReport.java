package alimama.com.unwbase.interfaces;

import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;

public interface ICrashReport extends IInitAction {
    void setCrashCaughtListener(IUTCrashCaughtListener iUTCrashCaughtListener);
}
