package com.alimama.moon.features.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.R;
import java.util.ArrayList;
import java.util.List;

public class SearchRealTimeSuggestAdapter extends RecyclerView.Adapter<SearchRealTimeSuggestViewHolder> {
    private Context mContext;
    /* access modifiers changed from: private */
    public OnItemClickListener mOnItemClickListener;
    /* access modifiers changed from: private */
    public List<String> mRealTimeSugList = new ArrayList();

    public interface OnItemClickListener {
        void onItemClick(String str);
    }

    public void setData(List<String> list) {
        this.mRealTimeSugList.clear();
        this.mRealTimeSugList.addAll(list);
    }

    public void setOnItemClickLitener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public SearchRealTimeSuggestAdapter(Context context) {
        this.mContext = context;
    }

    public int getItemCount() {
        return this.mRealTimeSugList.size();
    }

    public SearchRealTimeSuggestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SearchRealTimeSuggestViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.search_input_realtime_suggest_layout, (ViewGroup) null));
    }

    public void onBindViewHolder(SearchRealTimeSuggestViewHolder searchRealTimeSuggestViewHolder, final int i) {
        if (i >= 0 && i < this.mRealTimeSugList.size()) {
            searchRealTimeSuggestViewHolder.mSuggestText.setText(this.mRealTimeSugList.get(i));
            if (this.mOnItemClickListener != null) {
                searchRealTimeSuggestViewHolder.mSuggestText.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        SearchRealTimeSuggestAdapter.this.mOnItemClickListener.onItemClick((String) SearchRealTimeSuggestAdapter.this.mRealTimeSugList.get(i));
                    }
                });
            }
        }
    }

    class SearchRealTimeSuggestViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public TextView mSuggestText;

        public SearchRealTimeSuggestViewHolder(View view) {
            super(view);
            this.mSuggestText = (TextView) view.findViewById(R.id.search_realtime_suggest_tv);
        }
    }
}
