package com.automation.task.tests.ui.componentobjects.login;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.automation.task.tests.ui.base.UiPropertyReader;
import com.automation.task.tests.ui.custom.CustomWait;
import com.automation.task.tests.ui.custom.ElementExistsCheck;
import com.automation.task.tests.ui.email.CheckEmailComponent;
import java.util.concurrent.TimeUnit;
import lombok.extern.java.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Log
public class LoginComponent {

    private final WebDriver driver;

    public enum ErrorMessageTypes {MISSING_PW, MISSING_USERNAME, GENERAL_ERROR}

    // Login page main elements
    @FindBy(id = "nav-link-accountList")
    private WebElement signInNavButton;

    @FindBy(name = "email")
    private WebElement emailInput;

    @FindBy(name = "password")
    private WebElement pwInput;

    @FindBy(id = "signInSubmit")
    private WebElement signInButton;

    @FindBy(name = "rememberMe")
    private WebElement keepSignedInCheckBox;

    @FindBy(id = "continue")
    private WebElement sendVerificationButton;

    // Login page error elements
    @FindBy(id = "auth-password-missing-alert")
    private WebElement missingPwAlert;

    @FindBy(id = "auth-email-missing-alert")
    private WebElement missingEmailAlert;

    @FindBy(id = "auth-error-message-box")
    private WebElement errorMessageBox;

    //Login verification elements
    @FindBy(name = "claimspicker")
    private WebElement claimsPicker;

    @FindBy(name = "code")
    private WebElement verificationCodeInput;

    @FindBy(css = "[type='submit']")
    private WebElement continueButton;

    // Main page nav bar
    @FindBy(id = "nav-tools")
    private WebElement navToolsBar;

    public LoginComponent(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void loginWithKeepMeSignedInChecked(String email, String pw) {
        signInNavButton.click();
        keepSignedInCheckBox.click();
        enterLoginCredentialsAndSignIn(email, pw);
        verifyAccountIfNeeded();
    }

    public void login(String email, String pw) {
        signInNavButton.click();
        enterLoginCredentialsAndSignIn(email, pw);
    }

    public void login() {
        login(UiPropertyReader.USERNAME, UiPropertyReader.PASSWORD);
        verifyAccountIfNeeded();
        isLoggedIn(UiPropertyReader.ACCOUNT_OWNER_NAME);
    }

    public boolean isLoggedIn(String accountOwnerName) {
        return navToolsBar.getText().contains("Hello, " + accountOwnerName) && !navToolsBar.getText()
                .contains("Hello, Sign in");
    }

    public String getErrorMessage(ErrorMessageTypes type) {
        switch (type) {
            case GENERAL_ERROR:
                return getErrorMessage();
            case MISSING_PW:
                return getMissingPwErrorMessage();
            case MISSING_USERNAME:
                return getMissingEmailErrorMessage();
            default:
                return "Non defined error message"; //
        }
    }

    public boolean isPasswordButtonVisible() {
        return ElementExistsCheck.isElementExistingOnPage(pwInput);
    }

    private String getMissingPwErrorMessage() {
        return missingPwAlert.getText();
    }

    private void enterLoginCredentialsAndSignIn(String email, String pw) {
        emailInput.sendKeys(email);
        pwInput.sendKeys(pw);
        signInButton.click();
    }

    private String getMissingEmailErrorMessage() {
        return missingEmailAlert.getText();
    }

    private String getErrorMessage() {
        return errorMessageBox.findElement(By.tagName("li")).getText();
    }

    private void verifyAccountIfNeeded() {
        if (ElementExistsCheck.isElementExistingOnPage(claimsPicker)) {
            CustomWait.waitUntilElementIsClickable(sendVerificationButton, driver);
            sendVerificationButton.click();
            CheckEmailComponent email = new CheckEmailComponent();
            await().atMost(40, TimeUnit.SECONDS)
                    .until(email.getEmailVerificationCode()::length, greaterThanOrEqualTo(1));

            log.info("Verification code: " + email.getVerificationCode());
            verificationCodeInput.sendKeys(email.getVerificationCode());
            continueButton.click();
        }
    }
}
