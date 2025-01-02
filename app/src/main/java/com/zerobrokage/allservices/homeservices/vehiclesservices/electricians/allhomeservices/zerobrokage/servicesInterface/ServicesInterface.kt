package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.servicesInterface

import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.AddAddressApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.Cart_Api
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CustomerReview
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.DeleteApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.DeviceID
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.EditAddresses
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.EditProfile
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.FAQsData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.LoginRequest
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.LoginResponse
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.OtpRequest
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.OtpVerificationResponse
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.ResendOtp
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SavedAddressesApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.ServiceMenuData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SubCatData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SubMenuListData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServicesInterface {

    @GET("menus/{id}")
    fun getServicesMenuData(@Path("id") id: Int): Call<ServiceMenuData>

    @GET("reviews")
    fun getCustomerReview(): Call<CustomerReview>

    @GET("subcategories")
    fun getSubCat(): Call<SubCatData>

    @GET("faqs")
    fun getFaqs(): Call<FAQsData>

    @GET("submenu/{id}")
    fun getSubMenuListData(@Path("id") id: Int): Call<SubMenuListData>

    @POST("send-otp")
    fun postSendOtp(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("verify-otp")
    fun postOtp(@Body otpRequest: OtpRequest): Call<OtpVerificationResponse>

    @POST("resend-otp")
    fun postResendOtp(@Body resendOtp: ResendOtp): Call<ResendOtp>

    @POST("address/{id}")
    fun addAddress(@Path("id") id: Int, @Body addAddressApi: AddAddressApi): Call<AddAddressApi>

    @GET("saved-addresses/{id}")
    fun savedAddresses(@Path("id") id: Int): Call<SavedAddressesApi>

    @DELETE("address/{id}")
    fun deleteAddress(@Path("id") id: Int): Call<DeleteApi>

    @POST("device-id")
    fun deviceId(@Body deviceID: DeviceID): Call<DeviceID>


    @PUT("profile/{id}")
    fun updateProfile(@Path("id") id: Int, @Body editProfile: EditProfile): Call<EditProfile>

    @PUT("update-address/{id}")
    fun editAddress(@Path("id") id: Int, @Body editAddresses: EditAddresses): Call<EditAddresses>

    @POST("/cart/add")
    fun addToCart(@Body cartApi: Cart_Api): Call<Cart_Api>


}