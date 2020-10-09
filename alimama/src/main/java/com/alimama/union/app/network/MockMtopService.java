package com.alimama.union.app.network;

import com.alimama.moon.network.MtopException;
import com.alimama.union.app.messageCenter.model.AlertMessage;
import com.alimama.union.app.network.request.MessageLatestGetRequest;
import com.alimama.union.app.network.request.UserGradedetailGetRequest;
import com.alimama.union.app.network.response.BaseResponse;
import com.alimama.union.app.network.response.UserGradedetailGetResponseData;
import com.alimama.union.app.personalCenter.model.GradeThreshold;
import java.lang.reflect.Type;
import java.util.Date;
import javax.inject.Singleton;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.intf.Mtop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class MockMtopService implements IWebService {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) MockMtopService.class);

    public Mtop getMtop() {
        return null;
    }

    public <T extends BaseOutDo> T get(IMTOPDataObject iMTOPDataObject, Class<T> cls) throws MtopException {
        logger.info("get");
        return null;
    }

    public <T extends BaseOutDo> T get(IMTOPDataObject iMTOPDataObject, Type type) throws MtopException {
        logger.info("get");
        if (iMTOPDataObject instanceof UserGradedetailGetRequest) {
            return mockUserGradedetailGetRequest();
        }
        if (iMTOPDataObject instanceof MessageLatestGetRequest) {
            return mockMessageLatestGetRequest();
        }
        return null;
    }

    public <T extends BaseOutDo> T post(IMTOPDataObject iMTOPDataObject, Class<T> cls) throws MtopException {
        logger.info("post");
        return null;
    }

    private BaseResponse<UserGradedetailGetResponseData> mockUserGradedetailGetRequest() {
        BaseResponse<UserGradedetailGetResponseData> baseResponse = new BaseResponse<>();
        UserGradedetailGetResponseData userGradedetailGetResponseData = new UserGradedetailGetResponseData();
        userGradedetailGetResponseData.setAvatarUrl("https://wwc.alicdn.com/avatar/getAvatar.do?userId=456&width=160&height=160&type=sns");
        userGradedetailGetResponseData.setCurrentTotalOrder(15L);
        userGradedetailGetResponseData.setCurrentTotalUV(89L);
        userGradedetailGetResponseData.setGradeString("primary");
        userGradedetailGetResponseData.setLastTaskEndTime(new Date(1497657600000L));
        userGradedetailGetResponseData.setLastTaskStartTime(new Date(1495152000000L));
        userGradedetailGetResponseData.setNickName("");
        userGradedetailGetResponseData.setOrderFinishRate(100);
        userGradedetailGetResponseData.setTaskEndShowTime("6月25日");
        userGradedetailGetResponseData.setTaskEndTime(new Date(1498348800000L));
        userGradedetailGetResponseData.setTaskStartShowTime("5月27日");
        userGradedetailGetResponseData.setTaskStartTime(new Date(1495843200000L));
        GradeThreshold gradeThreshold = new GradeThreshold();
        gradeThreshold.setCheckOrderNum(10);
        gradeThreshold.setMinValidOrderNum(10);
        gradeThreshold.setTotalUv(60);
        gradeThreshold.setValidOrderNum(15);
        userGradedetailGetResponseData.setThreshold(gradeThreshold);
        baseResponse.setData(userGradedetailGetResponseData);
        return baseResponse;
    }

    private BaseResponse<AlertMessage> mockMessageLatestGetRequest() {
        BaseResponse<AlertMessage> baseResponse = new BaseResponse<>();
        AlertMessage alertMessage = new AlertMessage();
        alertMessage.setMsgType(3);
        alertMessage.setTitle("恭喜进入数据审核期");
        alertMessage.setContent("您的目标已达成，已进入15天的数据审核期。在审核期内产生的退款订单不计入有效订单，请耐心等待数据审核结果哦！");
        alertMessage.setCreateTime(new Date());
        alertMessage.setExpireDay(10);
        baseResponse.setData(alertMessage);
        return baseResponse;
    }
}
