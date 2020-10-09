package com.alimama.union.app.network.response;

import mtopsdk.mtop.domain.IMTOPDataObject;

public class UserMemberinfoGetResponseData implements IMTOPDataObject {
    private long memberId = 0;
    private long status = 0;
    private long taobaoNumId = 0;

    public long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(long j) {
        this.memberId = j;
    }

    public long getStatus() {
        return this.status;
    }

    public void setStatus(long j) {
        this.status = j;
    }

    public long getTaobaoNumId() {
        return this.taobaoNumId;
    }

    public void setTaobaoNumId(long j) {
        this.taobaoNumId = j;
    }
}
