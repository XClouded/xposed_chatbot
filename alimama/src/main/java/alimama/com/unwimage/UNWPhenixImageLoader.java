package alimama.com.unwimage;

import alimama.com.unwbase.callback.ImageDownloadFail;
import alimama.com.unwbase.callback.ImageDownloadSuccess;
import alimama.com.unwimage.interfaces.IImageLoader;
import android.widget.ImageView;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import java.util.HashMap;

public class UNWPhenixImageLoader implements IImageLoader {
    public void fetch(String str) {
        Phenix.instance().load(str).fetch();
    }

    public void fetch(String str, final ImageDownloadSuccess imageDownloadSuccess, final ImageDownloadFail imageDownloadFail) {
        Phenix.instance().load(str).succListener(new IPhenixListener<SuccPhenixEvent>() {
            public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
                if (imageDownloadSuccess == null || succPhenixEvent == null) {
                    return false;
                }
                HashMap hashMap = new HashMap();
                hashMap.put("url", succPhenixEvent.getUrl());
                hashMap.put("isFromDisk", Boolean.valueOf(succPhenixEvent.isFromDisk()));
                imageDownloadSuccess.callback(hashMap);
                return false;
            }
        }).failListener(new IPhenixListener<FailPhenixEvent>() {
            public boolean onHappen(FailPhenixEvent failPhenixEvent) {
                if (imageDownloadSuccess == null || failPhenixEvent == null) {
                    return false;
                }
                HashMap hashMap = new HashMap();
                hashMap.put("url", failPhenixEvent.getUrl());
                imageDownloadFail.callback(hashMap);
                return false;
            }
        }).fetch();
    }

    public void load(String str, ImageView imageView) {
        Phenix.instance().load(str).into(imageView);
    }
}
