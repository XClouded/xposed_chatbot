package anet.channel.analysis;

import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.flow.FlowStat;
import anet.channel.flow.INetworkAnalysis;
import anet.channel.util.ALog;
import com.taobao.analysis.FlowCenter;

public class DefaultNetworkAnalysis implements INetworkAnalysis {
    private static final String TAG = "DefaultNetworkAnalysis";
    private boolean isNetAnalysisValid;

    public DefaultNetworkAnalysis() {
        try {
            Class.forName("com.taobao.analysis.FlowCenter");
            this.isNetAnalysisValid = true;
        } catch (Exception unused) {
            this.isNetAnalysisValid = false;
            ALog.e(TAG, "no NWNetworkAnalysisSDK sdk", (String) null, new Object[0]);
        }
    }

    public void commitFlow(FlowStat flowStat) {
        if (this.isNetAnalysisValid) {
            FlowCenter.getInstance().commitFlow(GlobalAppRuntimeInfo.getContext(), flowStat.refer, flowStat.protocoltype, flowStat.req_identifier, flowStat.upstream, flowStat.downstream);
        }
    }
}
