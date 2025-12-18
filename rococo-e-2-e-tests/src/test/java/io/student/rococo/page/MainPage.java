package io.student.rococo.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

  private final SelenideElement header = $("h1");
  private final SelenideElement loginButton = $("button.variant-filled-primary");
  private final SelenideElement profileButton = $("button figure");

  public void clickLoginButton() {
    loginButton.click();
  }

  public MainPage checkLoginButtonIsHidden() {
    loginButton.shouldBe(hidden);
    return this;
  }

  public MainPage checkHeader() {
    header.shouldHave(exactText("Rococo"));
    return this;
  }

  public void checkProfileButtonIsPresent() {
    profileButton.shouldBe(visible);
  }
}
