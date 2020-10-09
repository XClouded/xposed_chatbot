package alimama.com.unwnetwork;

import java.util.Map;

public abstract class RxPageRequest<T> extends RxMtopRequest<T> {
    protected boolean isFirstPage = false;
    private boolean isHasMore = false;
    protected boolean isLoading = false;
    protected boolean isRefreshing = false;
    private long mLastDataRequestTime;

    /* access modifiers changed from: protected */
    public abstract void prepareFirstParams(Map<String, String> map);

    /* access modifiers changed from: protected */
    public abstract void prepareNextParams(Map<String, String> map);

    public RxPageRequest(ApiInfo apiInfo) {
        setApiInfo(apiInfo);
    }

    public void queryFirstPage() {
        this.isLoading = true;
        this.isFirstPage = true;
        prepareFirstParams(this.mParams);
        sendRequest();
    }

    public void queryNextPage() {
        if (!isLoading() && isHasMore()) {
            this.isLoading = true;
            this.isFirstPage = false;
            prepareNextParams(this.mParams);
            sendRequest();
        }
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public void restoreState() {
        this.isLoading = true;
        this.isFirstPage = true;
    }

    public void clearLoadingState() {
        this.isLoading = false;
    }

    public boolean isFirstPage() {
        return this.isFirstPage;
    }

    public boolean isRefreshing() {
        return this.isRefreshing;
    }

    public void setRefreshing(boolean z) {
        this.isRefreshing = z;
    }

    public boolean isHasMore() {
        return this.isHasMore;
    }

    public void setHasMore(boolean z) {
        this.isHasMore = z;
    }
}
