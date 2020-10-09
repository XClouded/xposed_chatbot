package com.taobao.android.preview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dinamic.tempate.DinamicTemplate;

public class PreviewViewHolder extends RecyclerView.ViewHolder {
    public DinamicTemplate template;

    public PreviewViewHolder(View view, DinamicTemplate dinamicTemplate) {
        super(view);
        this.template = dinamicTemplate;
    }

    public static PreviewViewHolder createViewHolder(View view, DinamicTemplate dinamicTemplate) {
        Context context = view.getContext();
        view.getContext();
        if (!context.getSharedPreferences("template_data_debug", 0).getBoolean("template_data_debug", false)) {
            return new PreviewViewHolder(view, dinamicTemplate);
        }
        CustomFrameLayout customFrameLayout = new CustomFrameLayout(view.getContext());
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            customFrameLayout.addView(view, new FrameLayout.LayoutParams(layoutParams.width, layoutParams.height));
        } else {
            customFrameLayout.addView(view);
        }
        return new PreviewViewHolder(customFrameLayout, dinamicTemplate);
    }
}
