package mtopsdk.mtop.upload.util;

import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.StringUtils;
import mtopsdk.mtop.upload.domain.UploadConstants;

public class FileUtil {
    private static final String TAG = "mtopsdk.FileUtil";

    public static String parseErrCode(Map<String, List<String>> map) {
        String singleHeaderFieldByKey = HeaderHandlerUtil.getSingleHeaderFieldByKey(map, UploadConstants.X_ERROR_CODE);
        return StringUtils.isBlank(singleHeaderFieldByKey) ? UploadConstants.ERRCODE_FILE_UPLOAD_FAIL : singleHeaderFieldByKey;
    }

    public static String parseErrMsg(Map<String, List<String>> map) {
        String urlDecode = MtopUtils.urlDecode(HeaderHandlerUtil.getSingleHeaderFieldByKey(map, UploadConstants.X_ERROR_MSG), "utf-8");
        return StringUtils.isBlank(urlDecode) ? UploadConstants.ERRMSG_FILE_UPLOAD_FAIL : urlDecode;
    }

    public static String parseUrlLocation(Map<String, List<String>> map) {
        String singleHeaderFieldByKey = HeaderHandlerUtil.getSingleHeaderFieldByKey(map, UploadConstants.X_DATA);
        return StringUtils.isNotBlank(singleHeaderFieldByKey) ? MtopUtils.urlDecode(singleHeaderFieldByKey, "utf-8") : singleHeaderFieldByKey;
    }
}
