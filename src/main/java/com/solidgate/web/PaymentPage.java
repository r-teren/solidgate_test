package com.solidgate.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class PaymentPage extends BasePage {


    @FindBy(name = "cardNumber")
    WebElement cardNumber;
    @FindBy(name = "cardExpiryDate")
    WebElement cardExpire;
    @FindBy(name = "cardCvv")
    WebElement cvc;
    @FindBy(name = "cardHolder")
    WebElement nameOnCard;
    @FindBy(xpath = "//div[@data-testid='terms-checkbox']")
    WebElement termsAndConditionsCheckbox;
    @FindBy(xpath = "//span[contains(@class, 'CardForm_rootText')]")
    WebElement paymentAmount;
    @FindBy(xpath = "//button[contains(@class, 'CardForm_root') and @data-testid='submit']")
    WebElement payButton;


    public PaymentPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }


    public PaymentPage fillCardNumber(String cardNumber) {
        this.visible(this.cardNumber).sendKeys(cardNumber);

        return this;
    }


    public PaymentPage fillCardExpire(String cardExpire) {
        this.visible(this.cardExpire).sendKeys(cardExpire);

        return this;
    }


    public PaymentPage fillCvc(int cvc) {
        this.visible(this.cvc).sendKeys(String.valueOf(cvc));

        return this;
    }


    public PaymentPage fillNameOnCard(String nameOnCard) {
        this.visible(this.nameOnCard).sendKeys(nameOnCard);

        return this;
    }


    public PaymentPage checkTermsAndConditions(boolean check) {
        String ariaChecked = this.visible(this.termsAndConditionsCheckbox).getAttribute("aria-checked");
        boolean checked = Objects.equals(ariaChecked, "true");

        if (checked != check) {
            this.visible(this.termsAndConditionsCheckbox).click();
        }

        return this;
    }


    public String getPaymentAmount() {
        String[] s = this.visible(this.paymentAmount).getText().split(" ");
        return s[s.length - 1];
    }


    public <T> T pay(Class<T> targetPage) {
        this.visible(this.payButton).click();

        try {
            return targetPage.getConstructor(WebDriver.class).newInstance(this.driver);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
