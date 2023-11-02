package Test;

import Data.DataHelper;
import Data.SQLHelper;
import Page.LoginPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static Data.SQLHelper.cleanAuthCodes;
import static Data.SQLHelper.cleanDataBase;
import static com.codeborne.selenide.Selenide.open;
;

public class LoginBankTest {
    LoginPage loginPage;

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @AfterEach
    void cleanDown() {
        cleanAuthCodes();
    }

//    @AfterAll
//    static void cleanDownAll() {
//        cleanDataBase();
//    }

    @Test
    void shouldLoginPage () {
        var authInfo = DataHelper.getAuthInfoForTest();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVisibleVerificationPage();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    void shouldBeErrorNotificationWithRandomCode() {
        var authInfo = DataHelper.getAuthInfoForTest();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVisibleVerificationPage();
        var verificationCode = DataHelper.generateRandomVerificationCode ();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");
    }
}
