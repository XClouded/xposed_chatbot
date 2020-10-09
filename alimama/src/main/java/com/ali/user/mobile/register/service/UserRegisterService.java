package com.ali.user.mobile.register.service;

import com.ali.user.mobile.model.MtopRegisterInitcontextResponseData;
import com.ali.user.mobile.register.model.BaseRegistRequest;
import com.ali.user.mobile.rpc.register.model.MtopRegisterCheckMobileResponseData;
import com.ali.user.mobile.rpc.register.model.MtopRegisterH5ResponseData;

public interface UserRegisterService {
    MtopRegisterInitcontextResponseData countryCodeRes(BaseRegistRequest baseRegistRequest);

    MtopRegisterH5ResponseData getRegisterH5Url(BaseRegistRequest baseRegistRequest);

    MtopRegisterCheckMobileResponseData verifyMobileAndCaptcha(BaseRegistRequest baseRegistRequest);
}
