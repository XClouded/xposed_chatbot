package com.alimama.moon.network.api.domin;

import com.alimama.union.app.network.response.UserMemberinfoGetResponseData;
import mtopsdk.mtop.domain.BaseOutDo;

public class MtopAlimamaMoonProviderUserMemberinfoGetResponse extends BaseOutDo {
    private UserMemberinfoGetResponseData data;

    public UserMemberinfoGetResponseData getData() {
        return this.data;
    }

    public void setData(UserMemberinfoGetResponseData userMemberinfoGetResponseData) {
        this.data = userMemberinfoGetResponseData;
    }
}
