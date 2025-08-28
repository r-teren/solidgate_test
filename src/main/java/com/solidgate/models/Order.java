package com.solidgate.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Builder(setterPrefix = "with")
@Getter
@Setter
public class Order {


    private static final Logger logger = LoggerFactory.getLogger(Order.class);


    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("amount")
    private int amount;  // coins
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("order_description")
    private String orderDescription;
    @JsonProperty("order_items")
    private String orderItems;
    @JsonProperty("order_date")
    private String orderDate;
    @JsonProperty("order_number")
    private Integer orderNumber;
    @JsonProperty("type")
    private String type;
    @JsonProperty("settle_interval")
    private Integer settleInterval;
    @JsonProperty("force3ds")
    private Boolean force3Ds;
    @JsonProperty("google_pay_allowed_auth_methods")
    private List<String> googlePayAllowedAuthMethods;
    @JsonProperty("customer_date_of_birth")
    private String customerBirthday;
    @JsonProperty("customer_email")
    private String customerEmail;
    @JsonProperty("customer_first_name")
    private String customerFirstName;
    @JsonProperty("customer_last_name")
    private String customerLastName;
    @JsonProperty("customer_phone")
    private String customerPhone;
    @JsonProperty("traffic_source")
    private String trafficSource;
    @JsonProperty("transaction_source")
    private String transactionSource;
    @JsonProperty("purchase_country")
    private String purchaseCountry;
    @JsonProperty("geo_country")
    private String geoCountry;
    @JsonProperty("geo_city")
    private String geoCity;
    @JsonProperty("language")
    private String language;
    @JsonProperty("website")
    private String website;
    @JsonProperty("order_metadata")
    private OrderMetadata orderMetadata;
    @JsonProperty("success_url")
    private String successUrl;
    @JsonProperty("fail_url")
    private String failUrl;


    @Builder(setterPrefix = "with")
    private static class OrderMetadata {


        @JsonProperty("coupon_code")
        private String couponCode;
        @JsonProperty("partner_id")
        private String partnerId;


        public static OrderMetadata getDefault() {
            OrderMetadata defaultMetaData = OrderMetadata.builder()
                    .withCouponCode("NY2024")
                    .withPartnerId("123989")
                    .build();

            logger.debug("Default test order meta data returned: {}", defaultMetaData);

            return defaultMetaData;
        }


    }


    public static Order getDefault(String orderId, int amount, String currency) {
        Order defaultOrder = Order.builder()
                .withOrderId(orderId)
                .withAmount(amount)
                .withCurrency(currency)
                .withOrderDescription("Premium package")
                .withOrderItems("item 1 x 10, item 2 x 30")
                .withOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .withOrderNumber(Math.abs(new Random().nextInt(100, 1000)))
                .withType("auth")
                .withSettleInterval(0)
                .withForce3Ds(false)
                .withGooglePayAllowedAuthMethods(List.of("PAN_ONLY"))
                .withCustomerBirthday(LocalDate.now().minusYears(new Random().nextLong(18, 100))
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .withCustomerEmail("example@example.com")
                .withCustomerFirstName("Bojack")
                .withCustomerLastName("Horseman")
                .withCustomerPhone("12345")
                .withTrafficSource("facebook")
                .withTransactionSource("main_menu")
                .withPurchaseCountry("USA")
                .withGeoCountry("USA")
                .withGeoCity("Labrador Peninsula")
                .withLanguage("en")
                .withWebsite("https://solidgate.com")
                .withOrderMetadata(OrderMetadata.getDefault())
                .build();


        logger.debug("Default test order returned: {}", defaultOrder);


        return defaultOrder;
    }


    @AllArgsConstructor
    @Getter
    public enum Currency {

        EUR("€");

        private final String webRepresentation;
    }


    public String getAmountInCurrency() {
        // is it always 100?
        return Currency.valueOf(this.currency).webRepresentation + (double) this.amount / 100;
    }


}
