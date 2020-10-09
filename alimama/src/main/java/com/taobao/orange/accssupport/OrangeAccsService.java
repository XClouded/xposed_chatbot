package com.taobao.orange.accssupport;

import com.alibaba.fastjson.JSON;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.common.Constants;
import com.taobao.orange.ConfigCenter;
import com.taobao.orange.OThreadFactory;
import com.taobao.orange.model.NameSpaceDO;
import com.taobao.orange.util.OLog;
import com.taobao.orange.util.OrangeUtils;
import java.util.HashSet;
import java.util.Set;

public class OrangeAccsService extends TaoBaseService {
    private static final String TAG = "OrangeAccs";
    /* access modifiers changed from: private */
    public static Set<NameSpaceDO> waitingNamesapces = new HashSet();

    public void onBind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
    }

    public void onResponse(String str, String str2, int i, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
    }

    public void onSendData(String str, String str2, int i, TaoBaseService.ExtraInfo extraInfo) {
    }

    public void onUnbind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
    }

    public void onData(String str, String str2, String str3, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
        if ("orange".equals(str)) {
            OLog.i(TAG, "onData", Constants.KEY_DATA_ID, str3, "userId", str2);
            handleUpdate(bArr);
        }
    }

    public static void handleUpdate(final byte[] bArr) {
        OThreadFactory.execute(new Runnable() {
            public void run() {
                if (bArr == null || bArr.length <= 0) {
                    OLog.e(OrangeAccsService.TAG, "handleUpdate data is empty", new Object[0]);
                    return;
                }
                NameSpaceDO nameSpaceDO = (NameSpaceDO) JSON.parseObject(new String(bArr), NameSpaceDO.class);
                if (nameSpaceDO == null) {
                    OLog.e(OrangeAccsService.TAG, "handleUpdate fail as namespace null", new Object[0]);
                    return;
                }
                OLog.d(OrangeAccsService.TAG, "handleUpdate", "namespace", OrangeUtils.formatNamespaceDO(nameSpaceDO));
                if (ConfigCenter.getInstance().mIsOrangeInit.get()) {
                    ConfigCenter.getInstance().loadConfigLazy(nameSpaceDO);
                    return;
                }
                OLog.w(OrangeAccsService.TAG, "handleUpdate fail as not init completed", new Object[0]);
                OrangeAccsService.waitingNamesapces.add(nameSpaceDO);
            }
        });
    }

    public static void complete() {
        if (!waitingNamesapces.isEmpty()) {
            OLog.v(TAG, "complete", "accs waiting", Integer.valueOf(waitingNamesapces.size()));
            for (NameSpaceDO loadConfigLazy : waitingNamesapces) {
                ConfigCenter.getInstance().loadConfigLazy(loadConfigLazy);
            }
            waitingNamesapces.clear();
            OLog.v(TAG, "complete end", new Object[0]);
        }
    }
}
