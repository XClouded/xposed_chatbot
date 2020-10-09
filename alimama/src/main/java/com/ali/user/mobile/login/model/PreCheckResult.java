package com.ali.user.mobile.login.model;

import com.alibaba.fastjson.annotation.JSONField;

public class PreCheckResult {
    @JSONField(name = "preCheck_verify")
    public boolean preCheckVerify;
    public boolean verify;
}
