package io.student.rococo.tests;

import com.codeborne.selenide.Configuration;
import io.student.rococo.config.Config;
import io.student.rococo.jupiter.User;
import io.student.rococo.model.UserJson;
import io.student.rococo.page.RegisterPage;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class RegisterTest {

  private static final Faker FAKER = new Faker();
  private static final String URL = Config.getInstance().authUrl() + "/register";
  private final String username = FAKER.credentials().username();
  private final String password = FAKER.credentials().password(3, 12);

  @BeforeEach
  void setUp() {
    Configuration.browserSize = "1920x1080";
  }

  @Test
  void shouldRegisterNewUser() {
    open(URL, RegisterPage.class)
        .register(username, password)
        .checkRegistrationFormDisappeared()
        .checkRegistrationSuccessHeader()
        .checkSignInButton();
  }

  @Test
  @User
  void shouldNotRegisterNewUserWithExistingUsername(UserJson user) {
    open(URL, RegisterPage.class)
        .register(user.username(), password)
        .checkRegistrationFormIsEmpty()
        .checkUsernameAlreadyExistsError(user.username())
        .checkPasswordErrorIsHidden();
  }

  @Test
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
    open(URL, RegisterPage.class)
        .register(username, password, FAKER.credentials().password(3, 12))
        .checkPasswordsDoNotMatchError()
        .checkUsernameErrorIsHidden();
  }

  @Test
  void shouldShowErrorIfUsernameHasLessThan3Characters() {
    open(URL, RegisterPage.class)
        .register(FAKER.credentials().password(1, 2), password)
        .checkUsernameLengthError()
        .checkPasswordErrorIsHidden();
  }

  @Test
  void shouldShowErrorIfUsernameHasMoreThan50Characters() {
    open(URL, RegisterPage.class)
        .register(FAKER.credentials().password(51, 52), password)
        .checkUsernameLengthError()
        .checkPasswordErrorIsHidden();
  }

  @Test
  void shouldShowErrorIfUsernameHasWhitespaces() {
    open(URL, RegisterPage.class)
        .register(FAKER.name().fullName(), password)
        .checkUsernameHasWhitespacesError()
        .checkPasswordErrorIsHidden();
  }

  @Test
  void shouldShowErrorIfPasswordHasLessThan3Characters() {
    open(URL, RegisterPage.class)
        .register(username, FAKER.credentials().password(1, 2))
        .checkPasswordLengthError()
        .checkUsernameErrorIsHidden();
  }

  @Test
  void shouldShowErrorIfPasswordHasMoreThan12Characters() {
    open(URL, RegisterPage.class)
        .register(username, FAKER.credentials().password(13, 14))
        .checkPasswordLengthError()
        .checkUsernameErrorIsHidden();
  }

  @Test
  void shouldShowErrorIfPasswordHasWhitespaces() {
    open(URL, RegisterPage.class)
        .register(username, FAKER.credentials().password(1, 5) + ' ' + FAKER.credentials().password(1, 5))
        .checkPasswordHasWhitespacesError()
        .checkUsernameErrorIsHidden();
  }

  @Test
  void shouldShowErrorsIfBothUsernameAndPasswordAreInvalid() {
    open(URL, RegisterPage.class)
        .register(FAKER.credentials().password(1, 2), FAKER.credentials().password(1, 2))
        .checkUsernameLengthError()
        .checkPasswordLengthError();
  }
}
