package alimama.com.unwviewbase.pullandrefrsh;

import alimama.com.unwviewbase.pullandrefrsh.PullBase;

public interface PtrInterface {
    int getReadyToRefreshingValue(PtrBase ptrBase, PullBase.Mode mode, int i);

    int getReleaseTargetValue(PtrBase ptrBase, PullBase.Mode mode, int i);
}
