package com.ali.user.mobile.data;

import android.os.AsyncTask;
import com.ali.user.mobile.callback.RpcRequestCallback;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.exception.RpcException;
import com.ali.user.mobile.register.model.OceanRegisterParam;
import com.ali.user.mobile.rpc.RpcResponse;

public class DataRepository {
    public static void register(final OceanRegisterParam oceanRegisterParam, final RpcRequestCallback rpcRequestCallback) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, RpcResponse>() {
            /* access modifiers changed from: protected */
            public RpcResponse doInBackground(Object... objArr) {
                try {
                    return RegisterComponent.getInstance().register(oceanRegisterParam);
                } catch (RpcException e) {
                    return DataRepository.getErrorRpcResponse(e);
                } catch (Throwable unused) {
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(RpcResponse rpcResponse) {
                DataRepository.handleResponse(rpcResponse, rpcRequestCallback);
            }
        }, new Object[0]);
    }

    public static void directRegister(final String str, final RpcRequestCallback rpcRequestCallback) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, RpcResponse>() {
            /* access modifiers changed from: protected */
            public RpcResponse doInBackground(Object... objArr) {
                try {
                    return RegisterComponent.getInstance().directRegister(str);
                } catch (RpcException e) {
                    return DataRepository.getErrorRpcResponse(e);
                } catch (Throwable unused) {
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(RpcResponse rpcResponse) {
                DataRepository.handleResponse(rpcResponse, rpcRequestCallback);
            }
        }, new Object[0]);
    }

    public static void sendSMS(final OceanRegisterParam oceanRegisterParam, final RpcRequestCallback rpcRequestCallback) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, RpcResponse>() {
            /* access modifiers changed from: protected */
            public RpcResponse doInBackground(Object... objArr) {
                try {
                    return RegisterComponent.getInstance().sendSMS(oceanRegisterParam);
                } catch (RpcException e) {
                    return DataRepository.getErrorRpcResponse(e);
                } catch (Throwable unused) {
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(RpcResponse rpcResponse) {
                DataRepository.handleResponse(rpcResponse, rpcRequestCallback);
            }
        }, new Object[0]);
    }

    public static void captchaCheck(final OceanRegisterParam oceanRegisterParam, final RpcRequestCallback rpcRequestCallback) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, RpcResponse>() {
            /* access modifiers changed from: protected */
            public RpcResponse doInBackground(Object... objArr) {
                try {
                    return RegisterComponent.getInstance().verify(oceanRegisterParam);
                } catch (RpcException e) {
                    return DataRepository.getErrorRpcResponse(e);
                } catch (Throwable unused) {
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(RpcResponse rpcResponse) {
                DataRepository.handleResponse(rpcResponse, rpcRequestCallback);
            }
        }, new Object[0]);
    }

    /* access modifiers changed from: private */
    public static RpcResponse getErrorRpcResponse(RpcException rpcException) {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.code = rpcException.getCode();
        rpcResponse.message = rpcException.getMsg();
        return rpcResponse;
    }

    /* access modifiers changed from: private */
    public static void handleResponse(RpcResponse rpcResponse, RpcRequestCallback rpcRequestCallback) {
        if (rpcResponse == null) {
            RpcResponse rpcResponse2 = new RpcResponse();
            rpcResponse2.code = 4;
            rpcResponse2.message = "rpc response is null";
            rpcRequestCallback.onSystemError(rpcResponse2);
        } else if (rpcResponse.code == 200 || rpcResponse.code == 3000) {
            rpcRequestCallback.onSuccess(rpcResponse);
        } else {
            rpcRequestCallback.onError(rpcResponse);
        }
    }
}
