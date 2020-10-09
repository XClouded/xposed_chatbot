package com.alimama.union.app.messageCenter.model;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.alibaba.fastjson.TypeReference;
import com.alimama.moon.AppDatabase;
import com.alimama.moon.R;
import com.alimama.moon.network.MtopException;
import com.alimama.moon.utils.StringUtil;
import com.alimama.moon.web.WebPageIntentGenerator;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.network.IWebService;
import com.alimama.union.app.network.request.MessageLatestGetRequest;
import com.alimama.union.app.network.request.MessageReadUpdateRequest;
import com.alimama.union.app.network.response.BaseResponse;
import com.alimama.union.app.network.response.MessageLatestGetResponseData;
import com.alimama.union.app.network.response.MessageReadUpdateResponseData;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import mtopsdk.mtop.domain.IMTOPDataObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AlertMessageRepository {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) AlertMessageRepository.class);
    private final Context appContext;
    /* access modifiers changed from: private */
    public final AppDatabase appDatabase;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Executor executor;
    /* access modifiers changed from: private */
    public final ILogin login;
    private final IWebService webService;

    @Inject
    public AlertMessageRepository(@Named("appContext") Context context, AppDatabase appDatabase2, Executor executor2, @Named("mtop_service") IWebService iWebService, ILogin iLogin) {
        this.appContext = context;
        this.appDatabase = appDatabase2;
        this.executor = executor2;
        this.webService = iWebService;
        this.login = iLogin;
        initDataAsync();
    }

    private void initDataAsync() {
        this.executor.execute(new Runnable() {
            public void run() {
                AlertMessageRepository.this.insertThresholdCheckMessage();
            }
        });
    }

    @MainThread
    public LiveData<AlertMessage> getAlertMessageAsync() {
        MutableLiveData mutableLiveData = new MutableLiveData();
        refreshRemoteMessage();
        if (!StringUtil.isBlank(this.login.getUserId())) {
            return this.appDatabase.alertMessageDao().getLastedUnreadMessageAsync(Long.valueOf(this.login.getUserId()));
        }
        logger.warn("userId is blank");
        return mutableLiveData;
    }

    @MainThread
    private void refreshRemoteMessage() {
        this.executor.execute(new Runnable() {
            public void run() {
                List<AlertMessage> access$200;
                if (!TextUtils.isEmpty(AlertMessageRepository.this.login.getUserId()) && (access$200 = AlertMessageRepository.this.requestRemoteAlertMessage()) != null && !access$200.isEmpty()) {
                    for (AlertMessage taobaoNumId : access$200) {
                        taobaoNumId.setTaobaoNumId(Long.valueOf(AlertMessageRepository.this.login.getUserId()));
                    }
                    AlertMessageRepository.this.appDatabase.alertMessageDao().insert(access$200);
                }
            }
        });
    }

    @MainThread
    public void sendNewUserGradeAlertMessageAsync(final JSMessage jSMessage) {
        this.executor.execute(new Runnable() {
            public void run() {
                AlertMessageRepository.this.insertNewUserGradeMessage(jSMessage);
            }
        });
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public void insertThresholdCheckMessage() {
        Date date;
        if (!TextUtils.isEmpty(this.login.getUserId()) && this.appDatabase.alertMessageDao().getLastedMessage(Long.valueOf(this.login.getUserId()), AlertMessageTypeEnum.CHECK_NOTICE.getMsgType()) == null) {
            Date date2 = new Date();
            try {
                date = this.dateFormat.parse("2017-08-01 00:00:00");
                try {
                    if (date2.after(date)) {
                        return;
                    }
                } catch (ParseException unused) {
                }
            } catch (ParseException unused2) {
                date = null;
            }
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.setTaobaoNumId(Long.valueOf(this.login.getUserId()));
            alertMessage.setMsgType(AlertMessageTypeEnum.CHECK_NOTICE.getMsgType());
            alertMessage.setId(-1L);
            alertMessage.setTitle("重要预告");
            alertMessage.setContent(this.appContext.getResources().getString(R.string.threshold_check_content));
            alertMessage.setAction("查看考核方法");
            alertMessage.setActionUrl(WebPageIntentGenerator.getThresholdRegulationUri().toString());
            alertMessage.setCreateTime(new Date());
            alertMessage.setExpireDay(Integer.valueOf((int) ((date.getTime() - date2.getTime()) / 86400000)));
            this.appDatabase.alertMessageDao().insert(alertMessage);
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public void insertNewUserGradeMessage(JSMessage jSMessage) {
        if (!TextUtils.isEmpty(this.login.getUserId()) && this.appDatabase.alertMessageDao().getLastedMessage(Long.valueOf(this.login.getUserId()), AlertMessageTypeEnum.NEW_USER_GRADE.getMsgType()) == null) {
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.setTaobaoNumId(Long.valueOf(this.login.getUserId()));
            alertMessage.setMsgType(AlertMessageTypeEnum.NEW_USER_GRADE.getMsgType());
            alertMessage.setId(-2L);
            alertMessage.setTitle(jSMessage.getTitle());
            alertMessage.setContent(jSMessage.getMessage());
            alertMessage.setAction(jSMessage.getButton());
            alertMessage.setActionUrl(jSMessage.getUrl());
            alertMessage.setCreateTime(new Date());
            alertMessage.setExpireDay(10);
            this.appDatabase.alertMessageDao().insert(alertMessage);
        }
    }

    @MainThread
    public void readMessage(final Integer num, final Long l) {
        this.executor.execute(new Runnable() {
            public void run() {
                AlertMessage messageById;
                if (!TextUtils.isEmpty(AlertMessageRepository.this.login.getUserId()) && (messageById = AlertMessageRepository.this.appDatabase.alertMessageDao().getMessageById(Long.valueOf(AlertMessageRepository.this.login.getUserId()), num, l)) != null) {
                    messageById.setRead(0);
                    messageById.setReadTime(new Date());
                    AlertMessageRepository.this.appDatabase.alertMessageDao().update(messageById);
                    if (l.longValue() > 0) {
                        Boolean unused = AlertMessageRepository.this.syncMessageReadState(l);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public List<AlertMessage> requestRemoteAlertMessage() {
        try {
            return ((MessageLatestGetResponseData) ((BaseResponse) this.webService.get((IMTOPDataObject) new MessageLatestGetRequest(), new TypeReference<BaseResponse<MessageLatestGetResponseData>>() {
            }.getType())).getData()).getResult();
        } catch (MtopException e) {
            logger.warn("mtop request exception: {}", (Object) e.getMessage());
            return null;
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public Boolean syncMessageReadState(Long l) {
        MessageReadUpdateRequest messageReadUpdateRequest = new MessageReadUpdateRequest();
        messageReadUpdateRequest.setMsgId(l.longValue());
        try {
            return ((MessageReadUpdateResponseData) ((BaseResponse) this.webService.get((IMTOPDataObject) messageReadUpdateRequest, new TypeReference<BaseResponse<MessageReadUpdateResponseData>>() {
            }.getType())).getData()).getResult();
        } catch (MtopException e) {
            logger.warn("mtop request exception: {}", (Object) e.getMessage());
            return false;
        }
    }
}
