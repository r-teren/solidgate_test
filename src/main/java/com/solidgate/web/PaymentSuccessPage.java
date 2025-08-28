package com.solidgate.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PaymentSuccessPage extends BasePage {


    @FindBy(xpath = "//h1[@data-testid='status-title']")
    WebElement paymentStatusTitle;
    @FindBy(xpath = "//span[@data-testid='status-order-description']")
    WebElement orderDescription;
    @FindBy(xpath = "//h1[@data-testid='status-title']/following-sibling::div//div[@data-testid='price_major']")
    WebElement orderPrice;
    @FindBy(xpath = "//span[@data-testid='status-order-title']")
    WebElement orderTitle;
    @FindBy(xpath = "//div[@data-testid='status-payment-method-card-number']")
    WebElement paymentCardNumber;


    public PaymentSuccessPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }


    public String getPaymentStatus() {
        return this.visible(this.paymentStatusTitle).getText();
    }


    public String getOrderDescription() {
        return this.visible(this.orderDescription).getText();
    }


    public String getOrderPrice() {
        return this.visible(this.orderPrice).getText();
    }


    public String getOrderTitle() {
        return this.visible(this.orderTitle).getText();
    }


    public String getPaymentCardNumber() {
        return this.visible(this.paymentCardNumber).getText();
    }


}
