package com.taobao.phenix.animate;

import com.taobao.pexode.Pexode;
import com.taobao.pexode.mimetype.DefaultMimeTypes;

public class GifImage {
    public static boolean isSupported() {
        return Pexode.canSupport(DefaultMimeTypes.GIF);
    }
}
