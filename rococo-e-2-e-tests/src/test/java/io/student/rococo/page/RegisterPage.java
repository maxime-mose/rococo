package io.student.rococo.page;

import com.codeborne.selenide.SelenideElement;
import io.student.rococo.config.Config;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {

  private final SelenideElement usernameInput = $("#username");
  private final SelenideElement passwordInput = $("#password");
  private final SelenideElement confirmPasswordInput = $("#passwordSubmit");
  private final SelenideElement submitButton = $("button[type=submit]");
  private final SelenideElement usernameErrorMessage = $("#username ~ .form__error");
  private final SelenideElement passwordErrorMessage = $("#password ~ .form__error");
  private final SelenideElement registrationSuccessHeader = $(".form__subheader");
  private final SelenideElement signInButton = $("a.form__submit");

  public RegisterPage register(String username, String password) {
    return register(username, password, password);
  }

  public RegisterPage register(String username, String password, String confirmPassword) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    confirmPasswordInput.setValue(confirmPassword);
    submitButton.click();
    return this;
  }

  public RegisterPage checkRegistrationFormDisappeared() {
    usernameInput.should(disappear);
    passwordInput.should(disappear);
    confirmPasswordInput.should(disappear);
    submitButton.should(disappear);
    return this;
  }

  public RegisterPage checkRegistrationSuccessHeader() {
    registrationSuccessHeader.shouldHave(exactText("Добро пожаловать в Rococo"));
    return this;
  }

  public void checkSignInButton() {
    signInButton.shouldHave(exactOwnText("Войти в систему"), href(Config.getInstance().frontUrl()));
  }

  public RegisterPage checkRegistrationFormIsEmpty() {
    usernameInput.shouldBe(visible, empty);
    passwordInput.shouldBe(visible, empty);
    confirmPasswordInput.shouldBe(visible, empty);
    submitButton.shouldBe(visible);
    return this;
  }

  public RegisterPage checkUsernameAlreadyExistsError(String username) {
    checkUsernameError(String.format("Username `%s` already exists", username));
    return this;
  }

  public RegisterPage checkUsernameLengthError() {
    checkUsernameError("Allowed username length should be from 3 to 50 characters");
    return this;
  }

  public RegisterPage checkUsernameHasWhitespacesError() {
    checkUsernameError("Username must not contain whitespaces");
    return this;
  }

  public RegisterPage checkPasswordsDoNotMatchError() {
    checkPasswordError("Passwords should be equal");
    return this;
  }

  public RegisterPage checkPasswordLengthError() {
    checkPasswordError("Allowed password length should be from 3 to 12 characters");
    return this;
  }

  public RegisterPage checkPasswordHasWhitespacesError() {
    checkPasswordError("Password must not contain whitespaces");
    return this;
  }

  public void checkUsernameErrorIsHidden() {
    usernameErrorMessage.shouldBe(hidden);
  }

  public void checkPasswordErrorIsHidden() {
    passwordErrorMessage.shouldBe(hidden);
  }

  private void checkUsernameError(String error) {
    usernameErrorMessage.shouldHave(exactOwnText(error));
  }

  private void checkPasswordError(String error) {
    passwordErrorMessage.shouldHave(exactOwnText(error));
  }
}
