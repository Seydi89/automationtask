package com.automation.task.tests.api.service.locationinformation;

import static io.restassured.RestAssured.given;

import com.automation.task.tests.api.base.PathConsts;
import com.automation.task.tests.api.common.CommonStrings;
import com.automation.task.tests.api.service.loginservice.LoginService;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LocationService {

    private static final String LAT_LONG_FIELD = "latlng";

    public static Response getLocationInformation(String latLng) {
        return given()
                .param(LAT_LONG_FIELD, latLng)
                .cookie(CommonStrings.COOKIE_FIELD, LoginService.cookie)
                .cookie(CommonStrings.SESSION_ID_FIELD, LoginService.sessionId)
                .when()
                .get(PathConsts.GEO_CODE_PATH);
    }
}
