package com.airbnb.lottie.animation.keyframe;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import com.airbnb.lottie.value.Keyframe;
import java.util.List;

public class PathKeyframeAnimation extends KeyframeAnimation<PointF> {
    private PathMeasure pathMeasure = new PathMeasure();
    private PathKeyframe pathMeasureKeyframe;
    private final PointF point = new PointF();
    private final float[] pos = new float[2];

    public PathKeyframeAnimation(List<? extends Keyframe<PointF>> list) {
        super(list);
    }

    public PointF getValue(Keyframe<PointF> keyframe, float f) {
        PathKeyframe pathKeyframe = (PathKeyframe) keyframe;
        Path path = pathKeyframe.getPath();
        if (path == null) {
            return (PointF) keyframe.startValue;
        }
        if (this.valueCallback != null) {
            PointF pointF = (PointF) this.valueCallback.getValueInternal(pathKeyframe.startFrame, pathKeyframe.endFrame.floatValue(), pathKeyframe.startValue, pathKeyframe.endValue, getLinearCurrentKeyframeProgress(), f, getProgress());
            if (pointF != null) {
                return pointF;
            }
        }
        if (this.pathMeasureKeyframe != pathKeyframe) {
            this.pathMeasure.setPath(path, false);
            this.pathMeasureKeyframe = pathKeyframe;
        }
        this.pathMeasure.getPosTan(f * this.pathMeasure.getLength(), this.pos, (float[]) null);
        this.point.set(this.pos[0], this.pos[1]);
        return this.point;
    }
}
