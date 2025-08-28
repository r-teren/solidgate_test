package com.solidgate.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder(setterPrefix = "with")
@Getter
@Setter
public class PaymentResponse {


    private String url;
    private String guid;
    private String id;
    private Object error;

}
