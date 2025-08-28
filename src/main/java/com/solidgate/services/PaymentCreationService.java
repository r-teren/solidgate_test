package com.solidgate.services;


import com.solidgate.models.PaymentRequest;
import com.solidgate.models.PaymentResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PaymentCreationService {


        @POST("init")
        Call<PaymentResponse> createPayment(@Body PaymentRequest paymentRequest);


}
