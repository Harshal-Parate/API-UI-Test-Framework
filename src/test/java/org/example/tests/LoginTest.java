package org.example.tests;


import org.example.dataproviderutils.DataProviderConfig;
import org.example.dataproviderutils.JSONDataProvider;
import org.example.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;

@Epic("Authentication")
@Feature("Login Feature")
public class LoginTest extends BaseTest {
    LoginPage loginPage;

    public LoginTest() {
        loginPage = new LoginPage();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "jsonData", dataProviderClass = JSONDataProvider.class)
    @DataProviderConfig(key = "loginCredentials", fields = "username,password")
    public void validLoginTest(String username, String password) {
        loginPage.login(username, password);
    }

}
