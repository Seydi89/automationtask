package com.automation.task.tests.ui.email;

import static com.automation.task.tests.ui.custom.CustomClick.jsClick;

import com.automation.task.tests.ui.base.UiPropertyReader;
import com.automation.task.tests.ui.custom.CustomWait;
import com.google.common.util.concurrent.Uninterruptibles;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckEmailComponent {

    private WebDriver driver;

    @Getter
    private String verificationCode;

    private static final String EMAIL_URL = "https://mail.google.com/";

    private static final String EMAIL_INPUT_CSS = "[type='email']";
    private static final String EMAIL_NEXT_BUTTON_ID = "identifierNext";
    private static final String PW_INPUT_NAME = "password";
    private static final String PW_NEXT_BUTTON_ID = "passwordNext";
    private static final String COMPOSE_BUTTON_XPATH = "//*[@role='button' and (.)='Compose']";
    private static final String INBOX_LIST_CSS = "span[class='bog']";
    private static final String LAST_EMAIL_XPATH = "(//*[@role='listitem'])[last()]";

    public CheckEmailComponent() {
        PageFactory.initElements(driver, this);
    }

    private void login() {
        driver = new ChromeDriver();
        driver.get(EMAIL_URL);
        driver.findElement(By.cssSelector(EMAIL_INPUT_CSS)).sendKeys(UiPropertyReader.GMAIL_USERNAME);
        driver.findElement(By.id(EMAIL_NEXT_BUTTON_ID)).click();

        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
        CustomWait.waitUntilElementIsClickable(driver.findElement(By.name(PW_INPUT_NAME)), driver);
        driver.findElement(By.name(PW_INPUT_NAME)).sendKeys(UiPropertyReader.GMAIL_PW);
        driver.findElement(By.id(PW_NEXT_BUTTON_ID)).click();
    }

    public String getEmailVerificationCode() {
        login();
        new WebDriverWait(driver, UiPropertyReader.EXPLICIT_TIMEOUT_IN_SECONDS)
                .until(ExpectedConditions.elementToBeClickable(By.xpath(COMPOSE_BUTTON_XPATH)));

        jsClick(driver.findElement(By.cssSelector(INBOX_LIST_CSS)),
                driver);

        verificationCode = driver.findElement(By.xpath(LAST_EMAIL_XPATH)).getText()
                .split("Hello,\n"
                        + "Please use the following code to complete verification:")[1]
                .split(" ")[0].
                split("This")[0]
                .trim();

        driver.quit();
        return verificationCode;
    }
}
