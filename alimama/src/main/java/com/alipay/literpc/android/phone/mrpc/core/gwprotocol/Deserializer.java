package com.alipay.literpc.android.phone.mrpc.core.gwprotocol;

import com.alipay.literpc.android.phone.mrpc.core.RpcException;

public interface Deserializer {
    Object parser() throws RpcException;
}
