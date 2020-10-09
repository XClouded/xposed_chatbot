package com.taobao.weex.devtools.toolbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.umbrella.link.export.UMLLCons;
import com.taobao.weex.devtools.adapter.JsLogAdapter;
import com.taobao.weex.inspector.R;

public class JsLogFragment extends Fragment {
    private Spinner logLevel;
    private RecyclerView logList;
    private View rootView;
    private SearchView searchView;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.rootView = layoutInflater.inflate(R.layout.fragment_js_log, viewGroup, false);
        instantiationViews();
        this.logList.setLayoutManager(new LinearLayoutManager(getContext()));
        this.logList.setAdapter(JsLogAdapter.getInstance());
        this.logLevel.setAdapter(new ArrayAdapter<String>(getContext(), 17367043, new String[]{"VERBOSE", UMLLCons.FEATURE_TYPE_DEBUG, "INFO", "WARN", "ERROR", "ASSERT"}) {
            @NonNull
            public View getView(int i, @Nullable View view, @NonNull ViewGroup viewGroup) {
                View view2 = super.getView(i, view, viewGroup);
                if (view2 instanceof TextView) {
                    ((TextView) view2).setTextSize(12.0f);
                }
                return view2;
            }
        });
        this.logLevel.setSelection(JsLogAdapter.getInstance().getLogLevel() - 2);
        this.logLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                JsLogAdapter.getInstance().setLogLevel(i + 2);
            }
        });
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                JsLogAdapter.getInstance().getFilter().filter(str);
                return true;
            }
        });
        return this.rootView;
    }

    private void instantiationViews() {
        this.logLevel = (Spinner) this.rootView.findViewById(R.id.log_level);
        this.logList = (RecyclerView) this.rootView.findViewById(R.id.log_list);
        this.searchView = (SearchView) this.rootView.findViewById(R.id.search_view);
    }
}
