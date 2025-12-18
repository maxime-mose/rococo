package io.student.rococo.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class LoginPage {

  private final SelenideElement usernameInput = $("[name=username]");
  private final SelenideElement passwordInput = $("[name=password]");
  private final SelenideElement submitButton = $("[type=submit]");
  private final SelenideElement errorMessage = $(".form__error");

  public MainPage login(String username, String password) {
    fillAndSubmitForm(username, password);
    return page(MainPage.class);
  }

  public LoginPage loginWithInvalidCredentials(String username, String password) {
    fillAndSubmitForm(username, password);
    return this;
  }

  public void checkLoginFailed() {
    errorMessage.shouldHave(exactOwnText("Неверные учетные данные пользователя"));
    usernameInput.shouldBe(visible, empty);
    passwordInput.shouldBe(visible, empty);
    submitButton.shouldBe(visible);
  }

  private void fillAndSubmitForm(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
  }
}
