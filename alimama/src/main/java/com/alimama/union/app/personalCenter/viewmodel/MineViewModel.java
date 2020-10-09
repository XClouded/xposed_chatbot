package com.alimama.union.app.personalCenter.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.alimama.moon.App;
import com.alimama.union.app.aalogin.model.User;
import com.alimama.union.app.aalogin.repository.UserRepository;
import com.alimama.union.app.configcenter.ConfigKeyList;
import com.alimama.union.app.personalCenter.model.MineItemData;
import com.alimamaunion.base.configcenter.EtaoConfigCenter;
import com.alimamaunion.base.safejson.SafeJSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import javax.inject.Inject;

public class MineViewModel extends AndroidViewModel {
    Gson gson = new Gson();
    List<MineItemData> mineItemDataList;
    @Inject
    UserRepository userRepository;

    public MineViewModel(Application application) {
        super(application);
        App app = (App) application;
        App.getAppComponent().inject(this);
    }

    public LiveData<User> getUserLiveData(String str) {
        return this.userRepository.getUser(str);
    }

    public User getUser(String str) {
        return this.userRepository.requestGradedetail(str);
    }

    public List<MineItemData> getMineItemList() {
        if (this.mineItemDataList == null) {
            try {
                this.mineItemDataList = (List) this.gson.fromJson(new SafeJSONObject(EtaoConfigCenter.getInstance().getConfigResult(ConfigKeyList.UNION_MINE)).optJSONObject("data").optString("mineJsonStr"), new TypeToken<List<MineItemData>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.mineItemDataList;
    }
}
