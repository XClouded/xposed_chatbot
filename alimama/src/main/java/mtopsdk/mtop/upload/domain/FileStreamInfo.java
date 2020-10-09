package mtopsdk.mtop.upload.domain;

import com.taobao.weex.el.parse.Operators;
import java.io.InputStream;
import mtopsdk.common.util.StringUtils;
import mtopsdk.mtop.domain.IMTOPDataObject;

public class FileStreamInfo implements IMTOPDataObject {
    public long fileLength;
    private String fileName;
    private InputStream fileStream;

    public FileStreamInfo(InputStream inputStream, String str) {
        this.fileStream = inputStream;
        this.fileName = str;
    }

    public InputStream getFileStream() {
        return this.fileStream;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.fileName == null ? 0 : this.fileName.hashCode()) + 31) * 31;
        if (this.fileStream != null) {
            i = this.fileStream.hashCode();
        }
        return hashCode + i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FileStreamInfo fileStreamInfo = (FileStreamInfo) obj;
        if (this.fileName == null) {
            if (fileStreamInfo.fileName != null) {
                return false;
            }
        } else if (!this.fileName.equals(fileStreamInfo.fileName)) {
            return false;
        }
        if (this.fileStream == null) {
            if (fileStreamInfo.fileStream != null) {
                return false;
            }
        } else if (!this.fileStream.equals(fileStreamInfo.fileStream)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "FileStreamInfo [" + "fileStream=" + this.fileStream + ", fileName=" + this.fileName + Operators.ARRAY_END_STR;
    }

    public boolean isValid() {
        return !StringUtils.isBlank(this.fileName) && this.fileStream != null;
    }
}
