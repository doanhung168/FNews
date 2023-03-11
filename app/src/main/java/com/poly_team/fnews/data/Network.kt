package com.poly_team.fnews.data

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface Network {

    @FormUrlEncoded
    @POST("users/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Response<NetworkResponse>

    @FormUrlEncoded
    @POST("users/register")
    suspend fun loginWith(
        @Field("account_type") accountType: String,
        @Field("account_id") accountId: String,
        @Field("display_name") displayName: String,
        @Field("avatar") avatar: String,
        @Field("email") email: String?
    ): Response<NetworkResponse>

    @FormUrlEncoded
    @POST("users/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("account_type") accountType: String = "local"
    ): NetworkResponse

    @FormUrlEncoded
    @POST("users/forgotPassword")
    suspend fun findPassword(
        @Field("email") email: String
    ) : NetworkResponse

    @POST("users/autoLogin")
    suspend fun autoLogin(
        @Header("Authorization") token : String
    ) : NetworkResponse


}