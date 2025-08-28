package com.solidgate.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderStatusRequest {


    @JsonProperty("order_id")
    private String orderId;


}
