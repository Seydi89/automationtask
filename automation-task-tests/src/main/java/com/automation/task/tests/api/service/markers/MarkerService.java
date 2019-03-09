package com.automation.task.tests.api.service.markers;

import static com.automation.task.tests.api.service.markers.MarkerConsts.ID_FIELD;
import static io.restassured.RestAssured.given;

import com.automation.task.tests.api.base.PathConsts;
import com.automation.task.tests.api.common.CommonStrings;
import com.automation.task.tests.api.service.loginservice.LoginService;
import io.restassured.response.Response;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MarkerService {

    public static Response getMarks() {
        return given()
                .param("g", "3335756") // Task note: cannot figure out the what the value does, left it as it is
                .cookie(CommonStrings.COOKIE_FIELD, LoginService.cookie)
                .cookie(CommonStrings.SESSION_ID_FIELD, LoginService.sessionId)
                .when()
                .get(PathConsts.MARKER_PATH);
    }

    public static Optional<String> getFeatureOfMark(Response getMarkersResponse, String markId, MarkFeatures feature) {
        return getIndexOfMark(getMarkersResponse, markId).isPresent() ?
                Optional.of(getMarkersResponse
                        .jsonPath()
                        .getList(feature.getMarkerFeature())
                        .get(getIndexOfMark(getMarkersResponse, markId).get()).toString())
                : Optional.empty();
    }

    private static Optional<Integer> getIndexOfMark(Response response, String markId) {
        List<Integer> idList = response.jsonPath().getList(ID_FIELD);
        for (int counter = 0; counter < idList.size(); counter++) {
            if (idList.get(counter).toString().equals(markId)) {
                return Optional.of(counter);
            }
        }
        return Optional.empty();
    }
}
