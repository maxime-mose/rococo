package io.student.rococo.tests;

import com.codeborne.selenide.Configuration;
import io.student.rococo.config.Config;
import io.student.rococo.jupiter.User;
import io.student.rococo.model.UserJson;
import io.student.rococo.page.LoginPage;
import io.student.rococo.page.MainPage;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class LoginTest {

  private static final Faker FAKER = new Faker();
  private final String username = FAKER.credentials().username();
  private final String password = FAKER.credentials().password(3, 12);

  @BeforeEach
  void setUp() {
    Configuration.browserSize = "1920x1080";
    // TODO Переделать на открытие страницы входа по прямой ссылке http://localhost:9000/login вместо кнопки на MainPage
    open(Config.getInstance().frontUrl(), MainPage.class).clickLoginButton();
  }

  @Test
  @User
  void mainPageShouldBeDisplayedAfterSuccessfulLogin(UserJson user) {
    page(LoginPage.class)
        .login(user.username(), user.password())
        .checkLoginButtonIsHidden()
        .checkHeader()
        .checkProfileButtonIsPresent();
  }

  @Test
  @User
  void userShouldStayOnLoginPageAfterLoginWithInvalidPassword(UserJson user) {
    page(LoginPage.class)
        .loginWithInvalidCredentials(user.username(), password)
        .checkLoginFailed();
  }

  @Test
  @User
  void userShouldStayOnLoginPageAfterLoginWithInvalidUsername(UserJson user) {
    page(LoginPage.class)
        .loginWithInvalidCredentials(username, user.password())
        .checkLoginFailed();
  }

  @Test
  void userShouldStayOnLoginPageAfterLoginWithInvalidCredentials() {
    page(LoginPage.class)
        .loginWithInvalidCredentials(username, password)
        .checkLoginFailed();
  }

  @AfterEach
  void tearDown() {
    clearBrowserCookies();
    clearBrowserLocalStorage();
  }
}
