package com.taobao.phenix.loader.file;

import android.content.res.Resources;
import com.taobao.phenix.entity.ResponseData;
import com.taobao.phenix.request.SchemeInfo;
import java.io.IOException;
import java.util.Map;

public interface FileLoader {
    public static final int LOCAL_ASSET = 34;
    public static final int LOCAL_BASE64 = 40;
    public static final int LOCAL_EXTENDED = 48;
    public static final int LOCAL_FILE = 33;
    public static final int LOCAL_RES = 36;

    ResponseData load(SchemeInfo schemeInfo, String str, Map<String, String> map) throws IOException, Resources.NotFoundException, UnSupportedSchemeException;
}
