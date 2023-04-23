package com.poly_team.fnews.data.network


import retrofit2.Response
import retrofit2.http.*

const val BASE_URL = "http://192.168.0.102:3000/"
const val NEWS = "news"
const val VIDEO = "video"
interface Network {

    // Auth
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Response<NetworkResponse>

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun loginWith(
        @Field("account_type") accountType: String,
        @Field("account_id") accountId: String,
        @Field("display_name") displayName: String,
        @Field("avatar") avatar: String,
        @Field("email") email: String?
    ): Response<NetworkResponse>

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("account_type") accountType: String = "local"
    ): NetworkResponse

    @FormUrlEncoded
    @POST("auth/forgot-password")
    suspend fun findPassword(
        @Field("email") email: String
    ) : NetworkResponse

    @POST("auth/auto-login")
    suspend fun autoLogin(
        @Header("Authorization") token : String
    ) : NetworkResponse

    // Media
    @GET("news")
    @JvmSuppressWildcards
    suspend fun getMedia(
        @QueryMap options : Map<String, Any>
    ) : NetworkResponse



}