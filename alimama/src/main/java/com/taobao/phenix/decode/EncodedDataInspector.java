package com.taobao.phenix.decode;

import com.taobao.phenix.entity.EncodedData;

public interface EncodedDataInspector {
    EncodedData inspectEncodedData(String str, EncodedData encodedData);
}
