package com.automation.task.tests.api.service.marker;

import static com.automation.task.tests.api.common.CommonStrings.MESSAGE_FIELD;
import static com.automation.task.tests.api.service.loginservice.LoginService.login;
import static com.automation.task.tests.api.service.markers.DeleteMarkerService.removeMark;
import static com.automation.task.tests.api.service.markers.MarkFeatures.LAT;
import static com.automation.task.tests.api.service.markers.MarkFeatures.LNG;
import static com.automation.task.tests.api.service.markers.MarkerConsts.ID_FIELD;
import static com.automation.task.tests.api.service.markers.MarkerService.getFeatureOfMark;
import static com.automation.task.tests.api.service.markers.MarkerService.getMarks;
import static com.automation.task.tests.api.service.markers.RelocateMarkerService.relocateMark;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.automation.task.tests.annotations.DefectIds;
import com.automation.task.tests.api.base.BaseApiTest;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class MarkerServiceTests extends BaseApiTest {

    @Test
    void shouldGetListOfMarks() {
        login();

        getMarks()
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body(ID_FIELD, hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    void shouldRelocateMark() {
        login();

        String markId = getMarkId();
        String newLat = "45.0002";
        String newLng = "46.0002";

        relocateMark(markId, newLat, newLng)
                .then()
                .assertThat()
                .statusCode(SC_OK);

        Response markersAfterRelocation = getAllMarks();

        assertAll(
                () -> assertEquals(newLat, getFeatureOfMark(markersAfterRelocation, markId, LAT).get()),
                () -> assertEquals(newLng, getFeatureOfMark(markersAfterRelocation, markId, LNG).get())
        );
    }

    @Disabled("Enable with fixing of DefectId#6")
    @DefectIds(6)
    @ParameterizedTest
    @CsvSource({
            "a, a",
            "' ' , ' '",
            "-91, 0",
            "91, 0",
            "89, -181",
            "-89, 181"
    })
    void shouldNotRelocateMarkWithInvalidValues(String newLat, String newLng) {
        login();

        String markId = getMarkId();
        relocateMark(markId, newLat, newLng)
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body(MESSAGE_FIELD, equalTo("Enter valid lat and lng values!"));
    }

    @Disabled("Enable with fixing of DefectId#7")
    @DefectIds(7)
    @Test
    void shouldNotRelocateWhenNoChangeInValues() {
        login();

        Response getMarksResponse = getAllMarks();
        String markId = getMarkId();
        String lat = getFeatureOfMark(getMarksResponse, markId, LAT).get();
        String lng = getFeatureOfMark(getMarksResponse, markId, LNG).get();

        relocateMark(markId, lat, lng)
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body(MESSAGE_FIELD, equalTo("New location values should be different from the current values"));
    }

    @DefectIds(10)
    @Test
    void shouldBeAbleToDeleteMark() {
        login();

        String markId = getMarkId();
        removeMark(markId)
                .then()
                .assertThat()
                .statusCode(SC_OK);

        List<String> markIdsList = getAllMarks().jsonPath().getList(ID_FIELD);
        assertFalse(markIdsList.contains(markId));
    }

    private static String getMarkId() {
        return getAllMarks()
                .jsonPath()
                .getList(ID_FIELD)
                .get(0).toString(); // just picked the first one
    }

    private static Response getAllMarks() {
        Response getMarkersResponse = getMarks();
        getMarkersResponse
                .then()
                .assertThat()
                .statusCode(SC_OK);
        return getMarkersResponse;
    }
}
