package anet.channel.strategy.dispatch;

import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.utils.AmdcThreadPoolExecutor;
import anet.channel.util.ALog;
import java.util.Map;
import java.util.Random;
import java.util.Set;

class AmdcTaskExecutor {
    public static final String TAG = "awcn.AmdcThreadPoolExecutor";
    private static Random random = new Random();
    /* access modifiers changed from: private */
    public Map<String, Object> cachedParams;

    AmdcTaskExecutor() {
    }

    public void addTask(Map<String, Object> map) {
        try {
            map.put("Env", GlobalAppRuntimeInfo.getEnv());
            synchronized (this) {
                if (this.cachedParams == null) {
                    this.cachedParams = map;
                    int nextInt = random.nextInt(3000) + 2000;
                    ALog.i(TAG, "merge amdc request", (String) null, "delay", Integer.valueOf(nextInt));
                    AmdcThreadPoolExecutor.scheduleTask(new AmdcTask(), (long) nextInt);
                } else {
                    Set set = (Set) this.cachedParams.get("hosts");
                    Set set2 = (Set) map.get("hosts");
                    if (map.get("Env") != this.cachedParams.get("Env")) {
                        this.cachedParams = map;
                    } else if (set.size() + set2.size() <= 40) {
                        set2.addAll(set);
                        this.cachedParams = map;
                    } else {
                        AmdcThreadPoolExecutor.submitTask(new AmdcTask(map));
                    }
                }
            }
        } catch (Exception unused) {
        }
    }

    private class AmdcTask implements Runnable {
        private Map<String, Object> params;

        AmdcTask(Map<String, Object> map) {
            this.params = map;
        }

        AmdcTask() {
        }

        public void run() {
            Map<String, Object> access$000;
            try {
                Map<String, Object> map = this.params;
                if (map == null) {
                    synchronized (AmdcTaskExecutor.class) {
                        access$000 = AmdcTaskExecutor.this.cachedParams;
                        Map unused = AmdcTaskExecutor.this.cachedParams = null;
                    }
                    map = access$000;
                }
                if (NetworkStatusHelper.isConnected()) {
                    if (GlobalAppRuntimeInfo.getEnv() != map.get("Env")) {
                        ALog.w(AmdcTaskExecutor.TAG, "task's env changed", (String) null, new Object[0]);
                    } else {
                        DispatchCore.sendRequest(DispatchParamBuilder.buildParamMap(map));
                    }
                }
            } catch (Exception e) {
                ALog.e(AmdcTaskExecutor.TAG, "exec amdc task failed.", (String) null, e, new Object[0]);
            }
        }
    }
}
