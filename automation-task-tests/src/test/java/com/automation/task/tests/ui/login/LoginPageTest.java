package com.automation.task.tests.ui.login;

import static com.automation.task.tests.api.base.UserCredentials.BASE_URL;
import static com.automation.task.tests.api.base.UserCredentials.PASSWORD;
import static com.automation.task.tests.api.base.UserCredentials.USERNAME;
import static com.automation.task.tests.ui.base.UiPropertyReader.ACCOUNT_OWNER_NAME;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.automation.task.tests.annotations.DefectIds;
import com.automation.task.tests.ui.base.BaseUiTest;
import com.automation.task.tests.ui.componentobjects.login.LoginComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class LoginPageTest extends BaseUiTest {

    private static LoginComponent login;

    @BeforeEach
    void beforeMethod() {
        login = new LoginComponent(driver);
    }

    @Test
    void shouldKeepUserSignedIn() {
        login.loginWithKeepMeSignedInChecked(USERNAME, PASSWORD);
        driver.get(BASE_URL);
        assertTrue(login.isLoggedIn(ACCOUNT_OWNER_NAME));

        driver.manage().deleteAllCookies();
        driver.get(BASE_URL);
        assertFalse(login.isLoggedIn(ACCOUNT_OWNER_NAME));
    }

    @Disabled("Enable with fixing of DefectId:#1")
    @DefectIds(1)
    @Test
    void shouldKeepUserSignedInAfterNavigationBack() {
        login.loginWithKeepMeSignedInChecked(USERNAME, PASSWORD);
        driver.navigate().back();
        assertFalse(login.isPasswordButtonVisible());
    }
}
