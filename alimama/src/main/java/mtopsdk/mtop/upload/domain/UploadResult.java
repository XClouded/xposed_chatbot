package mtopsdk.mtop.upload.domain;

import com.taobao.weex.el.parse.Operators;

public class UploadResult {
    public boolean isFinish = false;
    public String location;
    public String serverRT;

    public UploadResult(boolean z, String str) {
        this.isFinish = z;
        this.location = str;
    }

    public String toString() {
        return "UploadResult [ " + "isFinish=" + this.isFinish + "location=" + "location" + "serverRT=" + "serverRT" + Operators.ARRAY_END_STR;
    }
}
