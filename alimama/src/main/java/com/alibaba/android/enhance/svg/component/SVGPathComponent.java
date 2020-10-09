package com.alibaba.android.enhance.svg.component;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent;
import com.alibaba.android.enhance.svg.utils.PropHelper;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;

public class SVGPathComponent extends RenderableSVGVirtualComponent {
    private String mDString;
    private Path mPath;
    private PropHelper.PathParser mPathParser;

    public SVGPathComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
    }

    @WXComponentProp(name = "d")
    public void setD(String str) {
        this.mDString = str;
        this.mPathParser = new PropHelper.PathParser(str, this.mScale);
        this.mPath = this.mPathParser.getPath();
        markUpdated();
    }

    public void setPath(Path path) {
        this.mPath = path;
        markUpdated();
    }

    @Nullable
    public String getDString() {
        return this.mDString;
    }

    public Path getPath(Canvas canvas, Paint paint) {
        return this.mPath;
    }

    public Path getPath() {
        return this.mPath;
    }

    public Path getPath(Canvas canvas, Paint paint, RectF rectF) {
        if (this.mPathParser == null || rectF == null) {
            return this.mPath;
        }
        Path path = this.mPathParser.getPath(rectF);
        Matrix matrix = new Matrix();
        matrix.setTranslate(rectF.left, rectF.top);
        path.transform(matrix);
        return path;
    }
}
