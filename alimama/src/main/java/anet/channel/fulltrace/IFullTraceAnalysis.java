package anet.channel.fulltrace;

import anet.channel.statist.RequestStatistic;

public interface IFullTraceAnalysis {
    void commitRequest(String str, RequestStatistic requestStatistic);

    String createRequest();

    SceneInfo getSceneInfo();
}
