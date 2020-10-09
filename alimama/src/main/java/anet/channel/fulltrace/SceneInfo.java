package anet.channel.fulltrace;

public class SceneInfo {
    public long appLaunchTime;
    public int deviceLevel;
    public boolean isUrlLaunch;
    public long lastLaunchTime;
    public String speedBucket;
    public int startType;

    public String toString() {
        return "SceneInfo{" + "startType=" + this.startType + ", isUrlLaunch=" + this.isUrlLaunch + ", appLaunchTime=" + this.appLaunchTime + ", lastLaunchTime=" + this.lastLaunchTime + ", deviceLevel=" + this.deviceLevel + ", speedBucket=" + this.speedBucket + "}";
    }
}
