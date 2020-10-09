package com.taobao.downloader.adpater;

import java.util.List;

public interface DnsService {
    String getIpPort(String str);

    List<String> getIpPorts(String str);
}
