package com.solidgate.services;


import com.solidgate.models.OrderStatusRequest;
import com.solidgate.models.OrderStatusResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PaymentStatusService {


        @POST("status")
        Call<OrderStatusResponse> getStatus(@Body OrderStatusRequest statusRequest);


}
