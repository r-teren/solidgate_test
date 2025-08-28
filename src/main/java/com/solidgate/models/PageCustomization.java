package com.solidgate.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Builder(setterPrefix = "with")
@Getter
@Setter
public class PageCustomization {


    private static final Logger logger = LoggerFactory.getLogger(PageCustomization.class);


    @JsonProperty("public_name")
    private String publicName;
    @JsonProperty("order_title")
    private String orderTitle;
    @JsonProperty("order_description")
    private String orderDescription;
    @JsonProperty("payment_methods")
    private List<String> paymentMethods;
    @JsonProperty("button_font_color")
    private String buttonFontColor;
    @JsonProperty("button_color")
    private String buttonColor;
    @JsonProperty("font_name")
    private String fontName;
    @JsonProperty("is_cardholder_visible")
    private Boolean cardholderVisible;
    @JsonProperty("terms_url")
    private String termsUrl;
    @JsonProperty("back_url")
    private String backUrl;


    public static PageCustomization getDefault() {
        PageCustomization defaultPageCustomization = PageCustomization.builder()
                .withPublicName("Public Name")
                .withOrderTitle("Order Title")
                .withOrderDescription("Premium package")
                .withPaymentMethods(List.of("paypal"))
                .withButtonFontColor("#FFFFFF")
                .withButtonColor("#00816A")
                .withFontName("Open Sans")
                .withCardholderVisible(true)
                .withTermsUrl("https://solidgate.com/terms")
                .withBackUrl("https://solidgate.com")
                .build();

        logger.debug("Default test page customization returned: {}", defaultPageCustomization);

        return defaultPageCustomization;

    }


}
