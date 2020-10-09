package com.alimama.union.app.network.request;

import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import mtopsdk.mtop.domain.IMTOPDataObject;

public class UserGradedetailGetRequest implements IMTOPDataObject {
    private String API_NAME = "mtop.alimama.moon.provider.user.gradedetail.get";
    private boolean NEED_ECODE = false;
    private boolean NEED_SESSION = false;
    private String VERSION = DXMonitorConstant.DX_MONITOR_VERSION;
    private String nickName = null;
    private long taobaoNumId = 0;

    public String getAPI_NAME() {
        return this.API_NAME;
    }

    public void setAPI_NAME(String str) {
        this.API_NAME = str;
    }

    public String getVERSION() {
        return this.VERSION;
    }

    public void setVERSION(String str) {
        this.VERSION = str;
    }

    public boolean isNEED_ECODE() {
        return this.NEED_ECODE;
    }

    public void setNEED_ECODE(boolean z) {
        this.NEED_ECODE = z;
    }

    public boolean isNEED_SESSION() {
        return this.NEED_SESSION;
    }

    public void setNEED_SESSION(boolean z) {
        this.NEED_SESSION = z;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String str) {
        this.nickName = str;
    }

    public long getTaobaoNumId() {
        return this.taobaoNumId;
    }

    public void setTaobaoNumId(long j) {
        this.taobaoNumId = j;
    }
}
