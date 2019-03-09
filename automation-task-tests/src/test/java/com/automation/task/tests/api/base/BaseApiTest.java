package com.automation.task.tests.api.base;

import static com.automation.task.tests.api.base.UserCredentials.BASE_URL;
import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class BaseApiTest {

    @BeforeAll
    public static void baseSetup() {
        baseURI = BASE_URL;
        enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterAll
    public static void baseTearDown() {
        basePath = "";
    }
}
