package com.ali.user.mobile.model;

import com.ali.user.mobile.rpc.login.model.GroupedCountryCode;
import java.util.List;

public class MtopCountryCodeContextResult {
    public List<GroupedCountryCode> countrycodes;
    public CountryCode defaultCountry;
    public String mobile;
    public String sessionId;
}
