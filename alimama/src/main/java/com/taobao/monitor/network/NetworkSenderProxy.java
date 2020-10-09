package com.taobao.monitor.network;

public class NetworkSenderProxy implements INetworkSender {
    private INetworkSender sender;

    private NetworkSenderProxy() {
        this.sender = new INetworkSender() {
            public void send(String str, String str2) {
            }
        };
    }

    public static NetworkSenderProxy instance() {
        return Holder.INSTANCE;
    }

    public NetworkSenderProxy setSender(INetworkSender iNetworkSender) {
        this.sender = iNetworkSender;
        return this;
    }

    public void send(String str, String str2) {
        if (this.sender != null) {
            this.sender.send(str, str2);
        }
    }

    private static class Holder {
        static final NetworkSenderProxy INSTANCE = new NetworkSenderProxy();

        private Holder() {
        }
    }
}
