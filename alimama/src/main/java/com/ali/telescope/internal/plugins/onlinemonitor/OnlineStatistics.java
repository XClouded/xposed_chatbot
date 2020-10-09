package com.ali.telescope.internal.plugins.onlinemonitor;

import com.ali.telescope.internal.report.ReportManager;
import java.util.Map;

public class OnlineStatistics {
    private OnlineStatistics() {
    }

    private static class InstanceCreater {
        /* access modifiers changed from: private */
        public static OnlineStatistics instance = new OnlineStatistics();

        private InstanceCreater() {
        }
    }

    public static OnlineStatistics getInstance() {
        return InstanceCreater.instance;
    }

    public void OnBootFinish(Map<String, String> map, Map<String, Double> map2) {
        OnlineBootFinish onlineBootFinish = new OnlineBootFinish();
        onlineBootFinish.dimensionValues = map;
        onlineBootFinish.measureValues = map2;
        onlineBootFinish.time = System.currentTimeMillis();
        ReportManager.getInstance().append(onlineBootFinish);
    }

    public void OnMemoryLeak(Map<String, String> map, Map<String, Double> map2) {
        OnlineMemoryLeak onlineMemoryLeak = new OnlineMemoryLeak();
        onlineMemoryLeak.dimensionValues = map;
        onlineMemoryLeak.measureValues = map2;
        onlineMemoryLeak.time = System.currentTimeMillis();
        ReportManager.getInstance().append(onlineMemoryLeak);
    }

    public void OnBlockOrCloseGuard(Map<String, String> map, Map<String, Double> map2) {
        OnlineBlockOrCloseGuard onlineBlockOrCloseGuard = new OnlineBlockOrCloseGuard();
        onlineBlockOrCloseGuard.dimensionValues = map;
        onlineBlockOrCloseGuard.measureValues = map2;
        onlineBlockOrCloseGuard.time = System.currentTimeMillis();
        ReportManager.getInstance().append(onlineBlockOrCloseGuard);
    }

    public void OnActivityLoad(Map<String, String> map, Map<String, Double> map2) {
        OnlineActivityLoad onlineActivityLoad = new OnlineActivityLoad();
        onlineActivityLoad.dimensionValues = map;
        onlineActivityLoad.measureValues = map2;
        onlineActivityLoad.time = System.currentTimeMillis();
        ReportManager.getInstance().append(onlineActivityLoad);
    }

    public void OnBitmapStatic(Map<String, String> map, Map<String, Double> map2) {
        OnlineBitmapStatic onlineBitmapStatic = new OnlineBitmapStatic();
        onlineBitmapStatic.dimensionValues = map;
        onlineBitmapStatic.measureValues = map2;
        onlineBitmapStatic.time = System.currentTimeMillis();
        ReportManager.getInstance().append(onlineBitmapStatic);
    }

    public void OnCleanerStatic(Map<String, String> map, Map<String, Double> map2) {
        OnlineCleanerStatic onlineCleanerStatic = new OnlineCleanerStatic();
        onlineCleanerStatic.dimensionValues = map;
        onlineCleanerStatic.measureValues = map2;
        onlineCleanerStatic.time = System.currentTimeMillis();
        ReportManager.getInstance().append(onlineCleanerStatic);
    }

    public void OnGotoSleep(Map<String, String> map, Map<String, Double> map2) {
        OnlineGotoSleep onlineGotoSleep = new OnlineGotoSleep();
        onlineGotoSleep.dimensionValues = map;
        onlineGotoSleep.measureValues = map2;
        onlineGotoSleep.time = System.currentTimeMillis();
        ReportManager.getInstance().append(onlineGotoSleep);
    }

    public void OnThreadIoTimes(Map<String, String> map, Map<String, Double> map2) {
        OnlineThreadIoTimes onlineThreadIoTimes = new OnlineThreadIoTimes();
        onlineThreadIoTimes.dimensionValues = map;
        onlineThreadIoTimes.measureValues = map2;
        onlineThreadIoTimes.time = System.currentTimeMillis();
        ReportManager.getInstance().append(onlineThreadIoTimes);
    }

    public void OnMemoryProblem(Map<String, String> map, Map<String, Double> map2) {
        OnlineMemoryProblem onlineMemoryProblem = new OnlineMemoryProblem();
        onlineMemoryProblem.dimensionValues = map;
        onlineMemoryProblem.measureValues = map2;
        onlineMemoryProblem.time = System.currentTimeMillis();
        ReportManager.getInstance().append(onlineMemoryProblem);
    }

    public void OnAnr(Map<String, String> map, Map<String, Double> map2) {
        OnlineAnr onlineAnr = new OnlineAnr();
        onlineAnr.dimensionValues = map;
        onlineAnr.measureValues = map2;
        onlineAnr.time = System.currentTimeMillis();
        ReportManager.getInstance().append(onlineAnr);
    }

    public void OnThreadPoolProblem(Map<String, String> map, Map<String, Double> map2) {
        OnlineThreadPoolProblem onlineThreadPoolProblem = new OnlineThreadPoolProblem();
        onlineThreadPoolProblem.dimensionValues = map;
        onlineThreadPoolProblem.measureValues = map2;
        onlineThreadPoolProblem.time = System.currentTimeMillis();
        ReportManager.getInstance().append(onlineThreadPoolProblem);
    }

    public void WhiteScreen(Map<String, String> map, Map<String, Double> map2) {
        OnlineWhiteScreen onlineWhiteScreen = new OnlineWhiteScreen();
        onlineWhiteScreen.dimensionValues = map;
        onlineWhiteScreen.measureValues = map2;
        onlineWhiteScreen.time = System.currentTimeMillis();
        ReportManager.getInstance().append(onlineWhiteScreen);
    }

    public void OnBootPerfmance(Map<String, String> map, Map<String, Double> map2) {
        OnlineBootPerformance onlineBootPerformance = new OnlineBootPerformance();
        onlineBootPerformance.dimensionValues = map;
        onlineBootPerformance.measureValues = map2;
        onlineBootPerformance.time = System.currentTimeMillis();
        ReportManager.getInstance().append(onlineBootPerformance);
    }
}
