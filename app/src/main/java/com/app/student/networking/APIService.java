package com.app.student.networking;

import com.app.student.networking.notification.MyResponse;
import com.app.student.networking.notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type: application/json",
            "Authorization:key=AAAA87rKvws:APA91bHoyeDjfwxt_CnDN7oAxmfF9xHkL6WY8-zphvp2GEBM16zIfvFpJX4VkLW6i5wYjsnmboFMx2H5YwxcSclQ-TCUdweqg7H025qz-6yuYnK1uFLngqAy0ieJs_IA3MoPl47T061Z"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);


}
