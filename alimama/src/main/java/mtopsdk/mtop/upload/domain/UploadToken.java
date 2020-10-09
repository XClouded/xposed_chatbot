package mtopsdk.mtop.upload.domain;

import com.taobao.weex.el.parse.Operators;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import mtopsdk.common.util.StringUtils;

public class UploadToken {
    public String bizCode;
    public String domain;
    public FileBaseInfo fileBaseInfo;
    public long retryCount;
    public long segmentSize;
    public String token;
    public Map<String, String> tokenParams;
    public AtomicLong uploadedLength = new AtomicLong();
    public boolean useHttps;

    public boolean isValid() {
        if (this.fileBaseInfo == null || this.fileBaseInfo.fileSize <= 0 || this.segmentSize <= 0 || StringUtils.isBlank(this.token) || StringUtils.isBlank(this.domain)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "UploadToken [ " + "token=" + this.token + ", domain=" + this.domain + ", tokenParams=" + this.tokenParams + ", retryCount=" + this.retryCount + ", patchSize=" + this.segmentSize + ", fileBaseInfo=" + this.fileBaseInfo + Operators.ARRAY_END_STR;
    }
}
