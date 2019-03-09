package com.automation.task.tests.api.base;

import static java.util.ResourceBundle.getBundle;

import java.util.ResourceBundle;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserCredentials {

    private static final String API_PROPERTY_FILE_NAME = "api";
    private static final String BASE_URL_PROPERTY = "base.url";
    private static final String USERNAME_PROPERTY = "username";
    private static final String PW_PROPERTY = "pw";

    private static ResourceBundle properties = getBundle(API_PROPERTY_FILE_NAME);

    public static final String BASE_URL = properties.getString(BASE_URL_PROPERTY);
    public static final String USERNAME = properties.getString(USERNAME_PROPERTY);
    public static final String PASSWORD = properties.getString(PW_PROPERTY);
}
