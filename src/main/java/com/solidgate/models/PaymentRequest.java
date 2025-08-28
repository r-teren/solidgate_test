package com.solidgate.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PaymentRequest {


    @JsonProperty("order")
    private Order order;
    @JsonProperty("page_customization")
    private PageCustomization pageCustomization;


}
