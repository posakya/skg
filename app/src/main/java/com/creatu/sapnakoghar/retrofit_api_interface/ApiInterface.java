package com.creatu.sapnakoghar.retrofit_api_interface;



import com.creatu.sapnakoghar.model_class.ParticularModelClass;
import com.creatu.sapnakoghar.model_class.SiteModelClass;
import com.creatu.sapnakoghar.model_class.bill_model_class.BillModelClass;
import com.creatu.sapnakoghar.model_class.notification_model_class.NotificationModelClass;
import com.creatu.sapnakoghar.model_class.order_detail_model_class.OrderDetailModelClass;
import com.creatu.sapnakoghar.model_class.order_model_class.OrderModelClass;
import com.creatu.sapnakoghar.model_class.vendor_model_class.VendorModelClass;
import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lokesh on 6/1/18.
 */

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<JsonObject> login(@Body JsonObject login1);

    @GET("get-all-particulars")
    Call<ParticularModelClass> getParticularItem();

    @GET("get-all-orders/{userId}")
    Call<OrderModelClass> getOrders(@Path("userId") String userId, @Query("page") String page, @Query("limit") String limit);

    @GET("get-all-orderbills/{userId}")
    Call<BillModelClass> getBills(@Path("userId") String userId, @Query("page") String page, @Query("limit") String limit);

    @GET("get-all-sites")
    Call<SiteModelClass> getSites();

    @GET("get-all-venders")
    Call<VendorModelClass> getVendor();

    @GET("order/{orderId}")
    Call<OrderDetailModelClass> getOrderDetails(@Path("orderId") String orderId);

    /// get notifications ///
    @GET("get-all-notifications/{userId}")
    Call<NotificationModelClass> getNotification(@Path("userId") String userId);


    @Headers("Content-Type: application/json")
    @POST("add-new-order/{userId}")
    Call<JsonObject> addOrder(@Body JsonObject add_new_order, @Path("userId") String userId);

    @Headers("Content-Type: application/json")
    @POST("assign-venders/{userId}")
    Call<JsonObject> addVendor(@Body JsonObject assign_venderos, @Path("userId") String userId);

    //add-new-bill
    @POST("add-new-bill/{userId}")
    Call<ResponseBody> addBill (@Body RequestBody body,@Path("userId") String userId);

    @Headers("Content-Type: application/json")
    @POST("verify-order/{userId}")
    Call<JsonObject> verifyOrder(@Body JsonObject add_new_order, @Path("userId") String userId);

}
