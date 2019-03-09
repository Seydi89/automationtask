package com.automation.task.tests.ui.login;

import static com.automation.task.tests.api.base.UserCredentials.PASSWORD;
import static com.automation.task.tests.api.base.UserCredentials.USERNAME;
import static com.automation.task.tests.ui.base.UiPropertyReader.ACCOUNT_OWNER_NAME;
import static com.automation.task.tests.ui.componentobjects.login.LoginComponent.ErrorMessageTypes.GENERAL_ERROR;
import static com.automation.task.tests.ui.componentobjects.login.LoginComponent.ErrorMessageTypes.MISSING_PW;
import static com.automation.task.tests.ui.componentobjects.login.LoginComponent.ErrorMessageTypes.MISSING_USERNAME;
import static com.automation.task.tests.ui.componentobjects.login.LoginConsts.ENTER_PW_ERR_MSG;
import static com.automation.task.tests.ui.componentobjects.login.LoginConsts.ENTER_USERNAME_ERR_MSG;
import static com.automation.task.tests.ui.componentobjects.login.LoginConsts.INCORRECT_PW_ERR_MSG;
import static com.automation.task.tests.ui.componentobjects.login.LoginConsts.NO_SUCH_ACCOUNT_ERR_MSG;
import static com.automation.task.tests.ui.componentobjects.login.LoginConsts.PW_MAX_INPUT;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.automation.task.tests.annotations.DefectIds;
import com.automation.task.tests.ui.base.BaseUiTest;
import com.automation.task.tests.ui.componentobjects.login.LoginComponent;
import com.automation.task.tests.ui.componentobjects.login.LoginComponent.ErrorMessageTypes;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LoginComponentTests extends BaseUiTest {

    private static LoginComponent login;

    private static final String VALID_EMAIL_DOMAIN = "@example.com";

    @BeforeEach
    void beforeMethod() {
        login = new LoginComponent(driver);
    }

    @ParameterizedTest
    @MethodSource("validLoginDetails")
    void shouldLoginSuccessfully() {
        login.login(USERNAME, PASSWORD);
        assertTrue(login.isLoggedIn(ACCOUNT_OWNER_NAME));
    }

    @DefectIds({2,3})
    @ParameterizedTest
    @MethodSource("invalidLoginDetails")
    void shouldGetErrorForInvalidLogin(String emailAddress, String pw, ErrorMessageTypes type,
            String expectedErrorMessage) {
        login.login(emailAddress, pw);
        assertEquals(expectedErrorMessage, login.getErrorMessage(type));
    }

    private static Stream<Arguments> validLoginDetails() {
        return Stream.of(
                Arguments.of(
                        USERNAME,
                        PASSWORD),
                Arguments.of(
                        USERNAME.toUpperCase(),
                        PASSWORD));
    }

    private static Stream<Arguments> invalidLoginDetails() {
        return Stream.of(
                Arguments.of( //no pw
                        USERNAME,
                        "",
                        MISSING_PW,
                        ENTER_PW_ERR_MSG),
                Arguments.of( //no username
                        "",
                        PASSWORD,
                        MISSING_USERNAME,
                        ENTER_USERNAME_ERR_MSG),
                Arguments.of( //non-matching username-pw combination
                        USERNAME,
                        randomPw(),
                        GENERAL_ERROR,
                        INCORRECT_PW_ERR_MSG),
                Arguments.of( //non-matching since pw is in uppercase
                        USERNAME,
                        PASSWORD.toUpperCase(),
                        GENERAL_ERROR,
                        INCORRECT_PW_ERR_MSG),
                Arguments.of( //username starting with an existing account
                        USERNAME + randomAlphanumeric(5),
                        PASSWORD,
                        GENERAL_ERROR,
                        NO_SUCH_ACCOUNT_ERR_MSG),
                Arguments.of( //non existing account
                        randomUsername(),
                        randomPw(),
                        GENERAL_ERROR,
                        NO_SUCH_ACCOUNT_ERR_MSG),
                Arguments.of( //characters
                        "!@123#?*.;İüpğ,i<>!'^^^%&/()=?_",
                        randomPw(),
                        GENERAL_ERROR,
                        NO_SUCH_ACCOUNT_ERR_MSG),
               /* Arguments.of( //email address without domain
                        randomAlphabetic(10),
                        randomPw(),
                        GENERAL_ERROR,
                        INVALID_EMAIL_ADDRESS_ERROR),*/ // TODO: Enable with DefectId#2
                Arguments.of( //pw max input
                        randomUsername(),
                        randomAlphabetic(PW_MAX_INPUT),
                        GENERAL_ERROR,
                        NO_SUCH_ACCOUNT_ERR_MSG));
                /*Arguments.of( //username max input
                        randomAlphabetic(USERNAME_MAX_INPUT),
                        randomPw(),
                        GENERAL_ERROR,
                        NO_SUCH_ACCOUNT_ERR_MSG));*/ // TODO: Enable with DefectId#3
    }

    private static String randomPw() {
        return randomAlphanumeric(10);
    }

    private static String randomUsername() {
        return randomAlphanumeric(10) + VALID_EMAIL_DOMAIN;
    }
}
