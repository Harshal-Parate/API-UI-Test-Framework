package org.example.locatormanagement;

import lombok.Getter;
import java.text.MessageFormat;

@Getter
public enum BaseLocators {

    DIV_WITH_TEXT("LoginPage.DIV_WITH_TEXT"),
    DIV_WITH_ID("LoginPage.DIV_WITH_ID"),
    USERNAME("LoginPage.USERNAME"),
    PASSWORD("LoginPage.PASSWORD"),
    LOGIN_BUTTON("LoginPage.LOGIN_BUTTON");









    private final String key;

    BaseLocators(String key) {
        this.key = key;
    }

    public String get(Object... args) {
        String raw = LocatorRepository.getLocator(key);
        try {
            return MessageFormat.format(raw, args);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error formatting locator: " + key + " with args: " + java.util.Arrays.toString(args)
            );
        }
    }
}