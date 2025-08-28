package com.solidgate.models;

import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@Builder(setterPrefix = "with")
@Getter
public class CreditCard {


    private static final Logger logger = LoggerFactory.getLogger(CreditCard.class);

    private String number;
    private int expiryMonth;
    private int expiryYear;
    private int cvcCode;
    private String holderFirstName;
    private String holderLastName;


    public String getExpirationDate() {
        return this.expiryMonth + "/" + this.expiryYear;
    }


    public String getCardHolder() {
        return this.holderFirstName + " " + this.holderLastName;
    }


    public String getLastFourDigits() {
        return this.number.substring(this.number.length() - 4);
    }


    public static CreditCard getDefaultSuccess() {
        CreditCard creditCard = CreditCard.builder()
                .withNumber("4067429974719265")
                .withExpiryMonth(12)
                .withExpiryYear(LocalDate.now().plusYears(10).getYear())
                .withCvcCode(123)
                .withHolderFirstName("Bojack")
                .withHolderLastName("Horseman")
                .build();

        logger.debug("Default success credit card returned: {}", creditCard);

        return creditCard;
    }


}
