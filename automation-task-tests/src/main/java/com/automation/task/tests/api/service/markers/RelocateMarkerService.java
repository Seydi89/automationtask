package com.automation.task.tests.api.service.markers;

import static io.restassured.RestAssured.given;

import com.automation.task.tests.api.base.PathConsts;
import com.automation.task.tests.api.common.CommonStrings;
import com.automation.task.tests.api.service.loginservice.LoginService;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RelocateMarkerService {

    public static Response relocateMark(String markerId, String newLat, String newLng) {
        return given()
                .param("g",
                        "3335756") // Task note: cannot figure out the what the value does, left it as it is (same as in MarkerService)
                .param(MarkerConsts.MARKER_ID_FIELD, markerId)
                .param(MarkerConsts.LAT_FIELD, newLat)
                .param(MarkerConsts.LNG_FIELD, newLng)
                .cookie(CommonStrings.COOKIE_FIELD, LoginService.cookie)
                .cookie(CommonStrings.SESSION_ID_FIELD, LoginService.sessionId)
                .when()
                .post(PathConsts.RELOCATE_PATH);
    }
}
