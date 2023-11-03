package test;

import data.DataHelper;
import data.SQLHelper;
import org.junit.jupiter.api.AfterAll;
import page.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static data.SQLHelper.cleanAuthCodes;
import static com.codeborne.selenide.Selenide.open;
import static data.SQLHelper.cleanDataBase;


public class LoginBankTest {

    LoginPage loginPage;

    @AfterEach
    public void cleanDown() {
        cleanAuthCodes();
    }

    @BeforeEach
    public void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @AfterAll
    static void cleanDownAll() {
        cleanDataBase();
    }

    @Test
    public void shouldLoginPage () {
        var authInfo = DataHelper.getAuthInfoForTest();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVisibleVerificationPage();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    public void shouldBeErrorNotificationWithRandomCode() {
        var authInfo = DataHelper.getAuthInfoForTest();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVisibleVerificationPage();
        var verificationCode = DataHelper.generateRandomVerificationCode ();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");
    }
}
