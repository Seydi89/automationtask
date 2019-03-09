package com.automation.task.tests.api.service.location;

import static com.automation.task.tests.api.service.locationinformation.LocationService.getLocationInformation;
import static com.automation.task.tests.api.service.loginservice.LoginService.login;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.automation.task.tests.annotations.DefectIds;
import com.automation.task.tests.api.base.BaseApiTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LocationServiceTests extends BaseApiTest {

    private static final String PLUS_CODE_FIELD = "plus_code";
    private static final String COMPOUND_CODE_FIELD = PLUS_CODE_FIELD + ".compound_code";
    private static final String GLOBAL_CODE_FIELD = PLUS_CODE_FIELD + ".global_code";

    private static final String RESULTS_FIELD = "results";
    private static final String FORMATTED_ADDRESS = RESULTS_FIELD + ".formatted_address";

    private static final String STATUS_FIELD = "status";
    private static final String NO_RESULTS_KEYWORD = "None";
    private static final String MESSAGE_FIELD = "message";

    private static final String INVALID_LAT_LNG_ERROR_MSG = "Enter valid location information!";

    @Test
    void shouldGetCorrectLocationDetailsWithValidInput() {
        String latLng = "46.935002,32.734801";
        String compoundCode = "WPPM+2W Sadove, Mykolaiv Oblast, Ukraine";
        String globalCode = "8GRJWPPM+2W";

        String formattedAddress = "Snihurivs'kyi district, Mykolaiv Oblast, Ukraine";

        login();
        Response locationInformation = getLocationInformation(latLng);

        locationInformation
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body(COMPOUND_CODE_FIELD, equalTo(compoundCode))
                .body(GLOBAL_CODE_FIELD, equalTo(globalCode));

        assertEquals(formattedAddress, fullFormattedAddress(locationInformation));
    }

    @Disabled("Enable with fixing of DefectId#5")
    @DefectIds(5)
    @ParameterizedTest
    @CsvSource({
            "'0,0', Atlantic Ocean",
            "'-46,46', Indian Ocean",
            "'-90,180', Halsey St, Brooklyn 11233, United States",
            "'90.000,-180.000', Arctic Ocean",
            "'89.999999,179.999999', Halsey St, Brooklyn 11233, United States",
            "'-0.9999999999,0.9999999999', South Atlantic Ocean"
    })
    void shouldGetCorrectLocationInformation(String latLng, String formattedAddress) {
        login();
        Response locationInformation = getLocationInformation(latLng);
        locationInformation
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body(STATUS_FIELD, not(equalTo(NO_RESULTS_KEYWORD)));
        assertEquals(formattedAddress, fullFormattedAddress(locationInformation));
    }

    @Disabled("Enable with fix of defectId#4")
    @DefectIds(4)
    @ParameterizedTest
    @CsvSource({
            "a, 32",
            "32, a",
            "klm, klm",
            ",",
            " - ",
            "0",
            "990.00,9900.000",
            "-90.001,0",
            "0.001,-180.001",
            "90.001,180.001",
            "*!, *!"
    })
    void shouldReturnBadRequestForInvalidInput(String latLng) {
        login();
        getLocationInformation(latLng)
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body(MESSAGE_FIELD, equalTo(INVALID_LAT_LNG_ERROR_MSG));
    }

    private static String fullFormattedAddress(Response response) {
        return response
                .jsonPath()
                .getList(FORMATTED_ADDRESS)
                .get(0).toString();
    }
}
