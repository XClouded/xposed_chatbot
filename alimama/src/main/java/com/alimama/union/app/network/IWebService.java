package com.alimama.union.app.network;

import com.alimama.moon.network.MtopException;
import java.lang.reflect.Type;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.intf.Mtop;

public interface IWebService {
    <T extends BaseOutDo> T get(IMTOPDataObject iMTOPDataObject, Class<T> cls) throws MtopException;

    <T extends BaseOutDo> T get(IMTOPDataObject iMTOPDataObject, Type type) throws MtopException;

    Mtop getMtop();

    <T extends BaseOutDo> T post(IMTOPDataObject iMTOPDataObject, Class<T> cls) throws MtopException;
}
