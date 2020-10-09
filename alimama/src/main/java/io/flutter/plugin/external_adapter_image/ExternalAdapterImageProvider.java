package io.flutter.plugin.external_adapter_image;

import android.graphics.Bitmap;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Map;
import java.util.Vector;

@Keep
public interface ExternalAdapterImageProvider {

    @Keep
    public interface AnimatedBitmap {
        Bitmap getBufferBitmap();

        double getDuration();

        int getFrameCount();

        void start();

        void stop();
    }

    @Keep
    public interface Request {
        void cancel();
    }

    @Keep
    public interface Response {
        void finish(Image image);
    }

    void log(String str);

    @Keep
    Request request(@NonNull String str, int i, int i2, Map<String, String> map, Map<String, String> map2, @NonNull Response response);

    @Keep
    public static final class Image {
        private final AnimatedBitmap animatedBitmap;
        private final Vector<Bitmap> bitmaps;
        private final double duration;
        private final int frameCount;
        private final boolean isSingleBitmapAnimated;

        public Image(@NonNull Bitmap bitmap) {
            this.bitmaps = new Vector<>();
            this.bitmaps.add(bitmap);
            this.duration = 0.0d;
            this.frameCount = 1;
            this.isSingleBitmapAnimated = false;
            this.animatedBitmap = null;
        }

        public Image(@NonNull Vector<Bitmap> vector, double d) {
            this.bitmaps = vector;
            this.duration = d < 0.0d ? 1.0d : d;
            this.frameCount = vector.size();
            this.isSingleBitmapAnimated = false;
            this.animatedBitmap = null;
        }

        public Image(@NonNull AnimatedBitmap animatedBitmap2) {
            this.bitmaps = new Vector<>();
            this.bitmaps.add(animatedBitmap2.getBufferBitmap());
            this.duration = animatedBitmap2.getDuration();
            this.frameCount = animatedBitmap2.getFrameCount();
            this.isSingleBitmapAnimated = true;
            this.animatedBitmap = animatedBitmap2;
        }

        public double getDuration() {
            return this.duration;
        }

        public int getFrameCount() {
            return this.frameCount;
        }

        public int getBitmapCount() {
            return this.bitmaps.size();
        }

        public boolean isMultiframe() {
            return this.frameCount > 1;
        }

        public boolean isSingleBitmapAnimated() {
            return this.isSingleBitmapAnimated;
        }

        public Bitmap getBitmap() {
            if (this.bitmaps.size() > 0) {
                return this.bitmaps.firstElement();
            }
            return null;
        }

        public Bitmap getBitmap(int i) {
            if (i < this.bitmaps.size()) {
                return this.bitmaps.elementAt(i);
            }
            return null;
        }

        public Bitmap[] getBitmaps() {
            Object[] array = this.bitmaps.toArray();
            return (Bitmap[]) Arrays.copyOf(array, array.length, Bitmap[].class);
        }

        public void start() {
            if (this.animatedBitmap != null) {
                this.animatedBitmap.start();
            }
        }

        public void stop() {
            if (this.animatedBitmap != null) {
                this.animatedBitmap.stop();
            }
        }
    }
}
