package com.ali.user.mobile.register.model;

import com.ali.user.mobile.rpc.register.model.RegisterCountryModel;
import java.util.List;

public class OceanCountryCodeContextResult {
    public List<RegisterCountryModel> countryList;
    public RegisterCountryModel currentLocation;
    public String flagBaseUrl;
}
