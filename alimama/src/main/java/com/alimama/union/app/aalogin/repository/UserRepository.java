package com.alimama.union.app.aalogin.repository;

import android.text.TextUtils;
import androidx.lifecycle.LiveData;
import com.alibaba.fastjson.TypeReference;
import com.alimama.moon.AppDatabase;
import com.alimama.moon.utils.StringUtil;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.aalogin.model.User;
import com.alimama.union.app.aalogin.model.UserConverter;
import com.alimama.union.app.aalogin.model.UserDao;
import com.alimama.union.app.network.IWebService;
import com.alimama.union.app.network.request.UserGradedetailGetRequest;
import com.alimama.union.app.network.response.BaseResponse;
import com.alimama.union.app.network.response.UserGradedetailGetResponseData;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import mtopsdk.mtop.domain.IMTOPDataObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class UserRepository {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) UserRepository.class);
    private final AppDatabase appDatabase;
    private final Executor executor;
    private final ILogin login;
    /* access modifiers changed from: private */
    public final UserDao userDao = this.appDatabase.userDao();
    private final IWebService webService;

    @Inject
    public UserRepository(AppDatabase appDatabase2, Executor executor2, @Named("mtop_service") IWebService iWebService, ILogin iLogin) {
        this.appDatabase = appDatabase2;
        this.executor = executor2;
        this.webService = iWebService;
        this.login = iLogin;
    }

    public LiveData<User> getUser(String str) {
        refreshUser(str);
        return this.userDao.getUserByIdAsync(str);
    }

    private void refreshUser(final String str) {
        this.executor.execute(new Runnable() {
            public void run() {
                User requestGradedetail = UserRepository.this.requestGradedetail(str);
                if (requestGradedetail != null && !TextUtils.isEmpty(requestGradedetail.getUserId())) {
                    UserRepository.this.userDao.insertUser(requestGradedetail);
                }
            }
        });
    }

    public User requestGradedetail(String str) {
        if (StringUtil.isBlank(str)) {
            return null;
        }
        UserGradedetailGetRequest userGradedetailGetRequest = new UserGradedetailGetRequest();
        userGradedetailGetRequest.setTaobaoNumId(Long.parseLong(str));
        try {
            return UserConverter.convertFromUserGradedetailGetResponseData(this.login, (UserGradedetailGetResponseData) ((BaseResponse) this.webService.get((IMTOPDataObject) userGradedetailGetRequest, new TypeReference<BaseResponse<UserGradedetailGetResponseData>>() {
            }.getType())).getData());
        } catch (Exception e) {
            logger.warn("mtop request exception: {}", (Object) e.getMessage());
            return null;
        }
    }
}
