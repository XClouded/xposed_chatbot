package com.taobao.orange.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import anet.channel.appmonitor.AppMonitor;
import com.alibaba.fastjson.JSON;
import com.taobao.orange.GlobalOrange;
import com.taobao.orange.OConstant;
import com.taobao.orange.OThreadFactory;
import com.taobao.orange.model.ConfigAckDO;
import com.taobao.orange.model.IndexAckDO;
import com.taobao.orange.sync.BaseAuthRequest;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReportAckUtils {
    static final int MSG_REPORT_CONFIGACKS = 1;
    static final int MSG_WAIT_CONFIGACKS = 0;
    static final String TAG = "ReportAck";
    private static Handler handler = new ConfigHandler(Looper.getMainLooper());
    static final Set<ConfigAckDO> mConfigAckDOSet = new HashSet();

    static class ConfigHandler extends Handler {
        ConfigHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    if (OLog.isPrintLog(1)) {
                        OLog.d(ReportAckUtils.TAG, "wait config acks", new Object[0]);
                    }
                    sendEmptyMessageDelayed(1, 30000);
                    return;
                case 1:
                    synchronized (ReportAckUtils.mConfigAckDOSet) {
                        if (OLog.isPrintLog(1)) {
                            OLog.d(ReportAckUtils.TAG, "report config acks", "size", Integer.valueOf(ReportAckUtils.mConfigAckDOSet.size()));
                        }
                        HashSet hashSet = new HashSet();
                        hashSet.addAll(ReportAckUtils.mConfigAckDOSet);
                        ReportAckUtils.reportConfigAcks(hashSet);
                        ReportAckUtils.mConfigAckDOSet.clear();
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public static void reportConfigAck(ConfigAckDO configAckDO) {
        AppMonitor.getInstance().commitStat(configAckDO);
        if (GlobalOrange.reportUpdateAck && configAckDO != null) {
            synchronized (mConfigAckDOSet) {
                if (mConfigAckDOSet.size() == 0) {
                    handler.sendEmptyMessage(0);
                }
                mConfigAckDOSet.add(configAckDO);
            }
        }
    }

    static void reportConfigAcks(final Set<ConfigAckDO> set) {
        if (GlobalOrange.reportUpdateAck && set.size() != 0) {
            OThreadFactory.execute(new Runnable() {
                public void run() {
                    if (GlobalOrange.reportUpdateAck) {
                        new BaseAuthRequest((String) null, true, OConstant.REQTYPE_ACK_CONFIG_UPDATE) {
                            /* access modifiers changed from: protected */
                            public Map<String, String> getReqParams() {
                                return null;
                            }

                            /* access modifiers changed from: protected */
                            public Object parseResContent(String str) {
                                return null;
                            }

                            /* access modifiers changed from: protected */
                            public String getReqPostBody() {
                                return JSON.toJSONString(set);
                            }
                        }.syncRequest();
                    }
                }
            }, GlobalOrange.randomDelayAckInterval);
        }
    }

    public static void reportIndexAck(final IndexAckDO indexAckDO) {
        AppMonitor.getInstance().commitStat(indexAckDO);
        if (GlobalOrange.reportUpdateAck) {
            if (OLog.isPrintLog(1)) {
                OLog.d(TAG, "report index ack", indexAckDO);
            }
            OThreadFactory.execute(new Runnable() {
                public void run() {
                    if (GlobalOrange.reportUpdateAck) {
                        new BaseAuthRequest((String) null, true, OConstant.REQTYPE_ACK_INDEX_UPDATE) {
                            /* access modifiers changed from: protected */
                            public Map<String, String> getReqParams() {
                                return null;
                            }

                            /* access modifiers changed from: protected */
                            public Object parseResContent(String str) {
                                return null;
                            }

                            /* access modifiers changed from: protected */
                            public String getReqPostBody() {
                                return JSON.toJSONString(indexAckDO);
                            }
                        }.syncRequest();
                    }
                }
            }, GlobalOrange.randomDelayAckInterval);
        }
    }
}
