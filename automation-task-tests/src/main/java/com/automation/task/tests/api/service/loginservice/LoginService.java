package com.automation.task.tests.api.service.loginservice;

import static com.automation.task.tests.api.base.UserCredentials.PASSWORD;
import static com.automation.task.tests.api.base.UserCredentials.USERNAME;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

import com.automation.task.tests.api.base.PathConsts;
import com.automation.task.tests.api.common.CommonStrings;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LoginService {

    public static String cookie;
    public static String sessionId;

    private static final String USERNAME_FIELD = "u_email";
    private static final String PASSWORD_FIELD = "u_passwd";

    public static void login() {
        Response loginResponse = login(USERNAME, PASSWORD);
        loginResponse
                .then()
                .assertThat()
                .statusCode(SC_OK);

        cookie = getCookie(loginResponse);
        sessionId = getSessionId(loginResponse);
    }

    public static Response login(String username, String password) {
        return given()
                .formParam(USERNAME_FIELD, username)
                .formParam(PASSWORD_FIELD, password)
                .when()
                .post(PathConsts.SIGN_IN_PATH);
    }

    private static String getCookie(Response loginResponse) {
        return loginResponse.then().extract().cookie(CommonStrings.COOKIE_FIELD);
    }

    private static String getSessionId(Response loginResponse) {
        return loginResponse.then().extract().cookie(CommonStrings.SESSION_ID_FIELD);
    }
}
