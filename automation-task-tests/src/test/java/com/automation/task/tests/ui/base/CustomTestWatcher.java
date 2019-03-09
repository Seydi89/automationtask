package com.automation.task.tests.ui.base;

import java.io.File;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

@Log
public class CustomTestWatcher implements TestWatcher {

    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable throwable) {
        log.warning("Test aborted!");
    }

    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional<String> optional) {
        log.warning("Test disabled!");
    }

    @SneakyThrows
    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable throwable) {
        log.warning("Test failed, adding a screenshot!");
        FileUtils.copyFile(BaseUiTest.getSrc(), new File(
                "./screenshot/output/"
                        + extensionContext.getTestClass().get().getName()
                        + "_"
                        + extensionContext.getTestMethod().get().getName()
                        + "_"
                        + System.currentTimeMillis()
                        + ".png"));
    }

    @Override
    public void testSuccessful(ExtensionContext extensionContext) {
        log.info("Test is successful!");
    }
}
