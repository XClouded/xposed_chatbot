package com.ali.user.mobile.register.service.icbu.impl;

import android.os.AsyncTask;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.register.model.OceanRegisterParam;
import com.ali.user.mobile.rpc.RpcResponse;

public class RegisterServiceImpl {
    public void initContryList(final RpcRequestCallback rpcRequestCallback) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, RpcResponse>() {
            /* access modifiers changed from: protected */
            public RpcResponse doInBackground(Object... objArr) {
                try {
                    return RegisterComponent.getInstance().countryList(new OceanRegisterParam());
                } catch (RpcException e) {
                    RpcResponse rpcResponse = new RpcResponse();
                    rpcResponse.code = e.getCode();
                    rpcResponse.message = e.getMsg();
                    return rpcResponse;
                } catch (Throwable unused) {
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(RpcResponse rpcResponse) {
                RegisterServiceImpl.this.handleResponse(rpcResponse, rpcRequestCallback);
            }
        }, new Object[0]);
    }

    public void register(final OceanRegisterParam oceanRegisterParam, final RpcRequestCallback rpcRequestCallback) {
        if (oceanRegisterParam != null && rpcRequestCallback != null) {
            new CoordinatorWrapper().execute(new AsyncTask<Object, Void, RpcResponse>() {
                /* access modifiers changed from: protected */
                public RpcResponse doInBackground(Object... objArr) {
                    try {
                        return RegisterComponent.getInstance().register(oceanRegisterParam);
                    } catch (RpcException e) {
                        RpcResponse rpcResponse = new RpcResponse();
                        rpcResponse.code = e.getCode();
                        rpcResponse.message = e.getMsg();
                        return rpcResponse;
                    } catch (Throwable unused) {
                        return null;
                    }
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(RpcResponse rpcResponse) {
                    RegisterServiceImpl.this.handleResponse(rpcResponse, rpcRequestCallback);
                }
            }, new Object[0]);
        }
    }

    public void sendSMS(final OceanRegisterParam oceanRegisterParam, final RpcRequestCallback rpcRequestCallback) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, RpcResponse>() {
            /* access modifiers changed from: protected */
            public RpcResponse doInBackground(Object... objArr) {
                try {
                    return RegisterComponent.getInstance().sendSMS(oceanRegisterParam);
                } catch (RpcException e) {
                    RpcResponse rpcResponse = new RpcResponse();
                    rpcResponse.code = e.getCode();
                    rpcResponse.message = e.getMsg();
                    return rpcResponse;
                } catch (Throwable unused) {
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(RpcResponse rpcResponse) {
                RegisterServiceImpl.this.handleResponse(rpcResponse, rpcRequestCallback);
            }
        }, new Object[0]);
    }

    public void resendSMS(final OceanRegisterParam oceanRegisterParam, final RpcRequestCallback rpcRequestCallback) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, RpcResponse>() {
            /* access modifiers changed from: protected */
            public RpcResponse doInBackground(Object... objArr) {
                try {
                    return RegisterComponent.getInstance().resendSMS(oceanRegisterParam);
                } catch (RpcException e) {
                    RpcResponse rpcResponse = new RpcResponse();
                    rpcResponse.code = e.getCode();
                    rpcResponse.message = e.getMsg();
                    return rpcResponse;
                } catch (Throwable unused) {
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(RpcResponse rpcResponse) {
                RegisterServiceImpl.this.handleResponse(rpcResponse, rpcRequestCallback);
            }
        }, new Object[0]);
    }

    public void verifySMS(final OceanRegisterParam oceanRegisterParam, final RpcRequestCallback rpcRequestCallback) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, RpcResponse>() {
            /* access modifiers changed from: protected */
            public RpcResponse doInBackground(Object... objArr) {
                try {
                    return RegisterComponent.getInstance().verifySMS(oceanRegisterParam);
                } catch (RpcException e) {
                    RpcResponse rpcResponse = new RpcResponse();
                    rpcResponse.code = e.getCode();
                    rpcResponse.message = e.getMsg();
                    return rpcResponse;
                } catch (Throwable unused) {
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(RpcResponse rpcResponse) {
                RegisterServiceImpl.this.handleResponse(rpcResponse, rpcRequestCallback);
            }
        }, new Object[0]);
    }

    /* access modifiers changed from: private */
    public void handleResponse(RpcResponse rpcResponse, RpcRequestCallback rpcRequestCallback) {
        if (rpcResponse == null) {
            RpcResponse rpcResponse2 = new RpcResponse();
            rpcResponse2.code = 4;
            rpcResponse2.message = "rpc response is null";
            rpcRequestCallback.onSystemError(rpcResponse2);
        } else if (rpcResponse.code == 200) {
            rpcRequestCallback.onSuccess(rpcResponse);
        } else {
            rpcRequestCallback.onError(rpcResponse);
        }
    }
}
