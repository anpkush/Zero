package com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.servicesInterface

import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.AddAddressApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.Booking
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.BookingListApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.BookingRequest
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.BookingResponse
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartItemResponse
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartItemUpdate
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CartViewApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.CustomerReview
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.DeviceIdRequest
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.DeviceIdResponse
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.EditAddresses
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.EditProfile
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.FAQsData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.GetCartAPI
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.ItemUpdateRequest
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.ItemUpdateResponse
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.LoginRequest
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.LoginResponse
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.OtpRequest
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.OtpVerificationResponse
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.ResendOtp
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SavedAddressApi
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.ServiceMenuData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SubCatData
import com.zerobrokage.allservices.homeservices.vehiclesservices.electricians.allhomeservices.zerobrokage.modelClass.SubMenuListData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
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
    fun savedAddresses(@Path("id") id: Int): Call<SavedAddressApi>

    @DELETE("address/{userId}/{addressId}")
    fun deleteAddress(
        @Path("userId") userId: Int,
        @Path("addressId") addressId: Int
    ): Call<Map<String, Any>>

    @POST("device-id")
    fun deviceId(@Body deviceIdRequest: DeviceIdRequest): Call<DeviceIdResponse>


    @PUT("profile/{id}")
    fun updateProfile(@Path("id") id: Int, @Body editProfile: EditProfile): Call<EditProfile>

    @PUT("update-address/{id}")
    fun editAddress(@Path("id") id: Int, @Body editAddresses: EditAddresses): Call<EditAddresses>

    @POST("cart/add/{id}")
    fun addToCart(@Path("id") userId: Int, @Body cartApi: CartApi): Call<CartApi>

    @GET("api/cart/{id}")
    fun getCartItem():Call<GetCartAPI>

    @GET("cart/{id}")
    fun cartViewApi(@Path("id") id: Int): Call<CartViewApi>

    @DELETE("cart/delete/{userId}/{itemId}")
    fun deleteCartItem(
        @Path("userId") userId: Int,
        @Path("itemId") itemId: Int
    ): Call<Map<String, Any>>

    @DELETE("Item/delete/{userId}/{subMenuId}")
    fun deleteItem(
        @Path("userId") userId: Int,
        @Path("subMenuId") subMenuId: Int
    ): Call<Map<String, Any>>


    @POST("booking-list/{userId}")
    fun createBooking(
        @Path("userId") userId: Int,
        @Body bookingRequest: BookingRequest
    ): Call<BookingRequest>

    @POST("item/update/{enquiriesId}/{subMenuId}")
    fun updateCartItem(
        @Path("enquiriesId") enquiriesId: Int,
        @Path("subMenuId") subMenuId: Int,
        @Body request: ItemUpdateRequest
    ): Call<ItemUpdateResponse>


    @PATCH("cart/{userId}/{itemId}/update-quantity")
    fun updateCart( @Path("enquiriesId") enquiriesId: Int,
                    @Path("subMenuId") subMenuId: Int,
                    @Body cartItemUpdate: CartItemUpdate
    ): Call<CartItemResponse>


    @GET("enquiries/{enquiries_id}/bookings")
    fun getBookingList(@Path("enquiries_id") userId: Int): Call<BookingListApi>



}