package org.example.pages;

import org.example.locatormanagement.BaseLocators;

public class LoginPage extends BasePage{

    public LoginPage() {
        super();
    }

    public void login(String user, String pass) {
        wrapper.sendKeys(BaseLocators.USERNAME, user);
        wrapper.sendKeys(BaseLocators.PASSWORD, pass);
        wrapper.click(BaseLocators.LOGIN_BUTTON);
    }

}
