package com.solidgate.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderStatusResponse {


    private Order order;
    private Transaction transaction;


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Order {


        @JsonProperty("processing_amount")
        private int amount;
        @JsonProperty("processing_currency")
        private String currency;
        @JsonProperty("status")
        private String status;


    }


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Transaction {


        private int amount;
        private String currency;
        private String status;


    }


}
