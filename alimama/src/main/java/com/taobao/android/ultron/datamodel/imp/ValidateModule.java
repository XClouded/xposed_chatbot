package com.taobao.android.ultron.datamodel.imp;

import com.alibaba.android.umbrella.trace.UmbrellaTracker;
import com.taobao.android.ultron.common.ValidateResult;
import com.taobao.android.ultron.common.model.IDMComponent;
import java.util.Map;

public class ValidateModule {
    private DMContext mDMContext;

    public ValidateModule(DMContext dMContext) {
        this.mDMContext = dMContext;
    }

    public ValidateResult execute() {
        ValidateResult validate;
        Map<String, DMComponent> renderComponentMap = this.mDMContext.getRenderComponentMap();
        if (renderComponentMap != null && renderComponentMap.size() > 0) {
            for (IDMComponent next : renderComponentMap.values()) {
                if (next.getStatus() != 0 && (validate = next.validate()) != null && !validate.getValidateState()) {
                    UmbrellaTracker.commitFailureStability("formValidationFailed", "ValidateModule", "1.0", this.mDMContext.getBizName(), (String) null, (Map<String, String>) null, "errorcode", validate.getValidateFailedMsg());
                    return validate;
                }
            }
        }
        return new ValidateResult();
    }
}
