package mtopsdk.mtop.upload.domain;

import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.io.InputStream;

public class FileBaseInfo {
    public File file;
    public String fileId;
    public InputStream fileInputStream;
    public String fileName;
    public long fileSize;
    public String fileType;

    public FileBaseInfo(File file2) {
        this.file = file2;
    }

    public FileBaseInfo(InputStream inputStream) {
        this.fileInputStream = inputStream;
    }

    public String toString() {
        return "FileBaseInfo [" + "file=" + this.file + ", fileInputStream=" + this.fileInputStream + ", fileName=" + this.fileName + ", fileType=" + this.fileType + ", fileId=" + this.fileId + ", fileSize=" + this.fileSize + Operators.ARRAY_END_STR;
    }
}
