package alimama.com.unwviewbase.pullandrefrsh;

public interface PullAdapter {
    int getPullDirection();

    boolean isReadyForPullEnd();

    boolean isReadyForPullStart();

    void onPullAdapterAdded(PullBase pullBase);

    void onPullAdapterRemoved(PullBase pullBase);
}
