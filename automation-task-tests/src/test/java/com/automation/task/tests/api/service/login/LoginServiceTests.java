package com.automation.task.tests.api.service.login;

import static com.automation.task.tests.api.base.UserCredentials.PASSWORD;
import static com.automation.task.tests.api.base.UserCredentials.USERNAME;
import static com.automation.task.tests.api.common.CommonStrings.MESSAGE_FIELD;
import static com.automation.task.tests.api.common.CommonStrings.SUCCESS_FIELD;
import static com.automation.task.tests.api.service.loginservice.LoginService.login;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;

import com.automation.task.tests.annotations.DefectIds;
import com.automation.task.tests.api.base.BaseApiTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LoginServiceTests extends BaseApiTest {

    @Test
    void shouldLoginSuccessfullyWithValidCredentials() {
        login(USERNAME, PASSWORD)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body(SUCCESS_FIELD, equalTo(true));
    }

    @Disabled("Enable with fixing of DefectId#8")
    @DefectIds(8)
    @Test
    void shouldNotLoginWithNonMatchingCredentials() {
        login("test@example.com", "somepassword123.")
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body(SUCCESS_FIELD, equalTo(false))
                .body(MESSAGE_FIELD, equalTo("Email and password do not match."));
    }

    @Disabled("Enable with fixing of DefectId#9")
    @DefectIds(9)
    @ParameterizedTest
    @CsvSource({
            "' ', ' '",
            "a, ' '",
            "' ', b*!."
    })
    void shouldNotLoginWithInvalidCredentials(String username, String password) {
        login(username, password)
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body(SUCCESS_FIELD, equalTo(false))
                .body(MESSAGE_FIELD, equalTo("Enter valid credentials."));
    }
}
