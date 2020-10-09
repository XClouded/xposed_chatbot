package com.alibaba.android.anynetwork.plugin.allinone;

import com.alibaba.android.anynetwork.core.ANRequest;
import com.alibaba.android.anynetwork.core.ANResponse;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;

public interface IAllInOneConverter {
    MtopRequest convertANRequest2MtopRequest(ANRequest aNRequest);

    ANResponse convertMtopResponse2ANResponse(MtopResponse mtopResponse);
}
