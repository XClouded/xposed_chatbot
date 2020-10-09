package com.alimama.union.app.messageCenter.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.alimama.moon.App;
import com.alimama.union.app.messageCenter.model.AlertMessage;
import com.alimama.union.app.messageCenter.model.AlertMessageRepository;
import javax.inject.Inject;

public class AlertMessageViewModel extends AndroidViewModel {
    @Inject
    AlertMessageRepository alertMessageRepository;

    public AlertMessageViewModel(Application application) {
        super(application);
        App app = (App) application;
        App.getAppComponent().inject(this);
    }

    public LiveData<AlertMessage> getAlertMessageAsync() {
        return this.alertMessageRepository.getAlertMessageAsync();
    }

    public void readMessage(Integer num, Long l) {
        this.alertMessageRepository.readMessage(num, l);
    }
}
