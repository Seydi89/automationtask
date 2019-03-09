package com.automation.task.tests.ui.base;

import static java.util.ResourceBundle.getBundle;

import java.util.ResourceBundle;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UiPropertyReader {

    private static final String UI_PROPERTY_FILE_NAME = "ui";
    private static final String BASE_URL_PROPERTY = "base.url";
    private static final String USERNAME_PROPERTY = "username";
    private static final String PW_PROPERTY = "pw";
    private static final String ACCOUNT_OWNER_NAME_PROPERTY = "account.owner.name";

    private static final String TIMEOUT = "timeout.";
    private static final String EXPLICIT_TIMEOUT_PROPERTY = TIMEOUT + "explicit";
    private static final String IMPLICIT_TIMEOUT_PROPERTY = TIMEOUT + "implicit";
    private static final String LOAD_TIMEOUT_PROPERTY = TIMEOUT + "load";

    private static final String GMAIL = "gmail";
    private static final String GMAIL_USERNAME_PROPERTY = GMAIL + ".username";
    private static final String GMAIL_PW_PROPERTY = GMAIL + ".pw";

    private static ResourceBundle properties = getBundle(UI_PROPERTY_FILE_NAME);

    public static final String CHROME_DRIVER_LOCATION_PROPERTY = "webdriver.chrome.driver";

    public static final String CHROME_DRIVER_LOCATION = properties.getString(CHROME_DRIVER_LOCATION_PROPERTY);
    public static final String BASE_URL = properties.getString(BASE_URL_PROPERTY);
    public static final String USERNAME = properties.getString(USERNAME_PROPERTY);
    public static final String PASSWORD = properties.getString(PW_PROPERTY);
    public static final String ACCOUNT_OWNER_NAME = properties.getString(ACCOUNT_OWNER_NAME_PROPERTY);

    public static final int EXPLICIT_TIMEOUT_IN_SECONDS = Integer.parseInt(properties.getString(EXPLICIT_TIMEOUT_PROPERTY));
    public static final int IMPLICIT_TIMEOUT_IN_SECONDS = Integer.parseInt(properties.getString(IMPLICIT_TIMEOUT_PROPERTY));
    public static final int LOAD_TIMEOUT_IN_SECONDS = Integer.parseInt(properties.getString(LOAD_TIMEOUT_PROPERTY));

    public static final String GMAIL_USERNAME = properties.getString(GMAIL_USERNAME_PROPERTY);
    public static final String GMAIL_PW = properties.getString(GMAIL_PW_PROPERTY);
}
