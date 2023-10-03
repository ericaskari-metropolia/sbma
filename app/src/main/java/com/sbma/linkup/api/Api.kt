package com.sbma.linkup.api

import com.sbma.linkup.api.apimodels.ApiCard
import com.sbma.linkup.api.apimodels.ApiConnection
import com.sbma.linkup.api.apimodels.ApiShare
import com.sbma.linkup.api.apimodels.ApiTag
import com.sbma.linkup.api.apimodels.ApiUser
import com.sbma.linkup.api.apimodels.AssignTagRequest
import com.sbma.linkup.api.apimodels.NewCardRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // Profile GET Request
    @GET("/profile")
    @Headers("Content-Type: application/json")
    fun getProfile(@Header("Authorization") authorization: String): Call<ApiUser>

    // New Card POST Request
    @POST("/card")
    @Headers("Content-Type: application/json")
    fun createNewCard(@Header("Authorization") authorization: String, @Body request: NewCardRequest): Call<ApiCard>

    // Share POST Request
    @POST("/share")
    @Headers("Content-Type: application/json")
    fun share(@Header("Authorization") authorization: String, @Body shareData: List<String>): Call<ApiShare>

    // Scan POST Request
    @POST("/share/{shareId}/scan")
    @Headers("Content-Type: application/json")
    fun scanShare(@Header("Authorization") authorization: String, @Path("shareId") shareId: String): Call<ApiConnection>

    // Tag Assign POST Request
    @POST("/tag")
    @Headers("Content-Type: application/json")
    fun assignTag(@Header("Authorization") authorization: String, @Body request: AssignTagRequest): Call<ApiTag>

    // Tag Scan POST Request
    @POST("/tag/{tagId}/scan")
    @Headers("Content-Type: application/json")
    fun scanTag(@Header("Authorization") authorization: String, @Path("tagId") tagId: String): Call<ApiConnection>

    // Tag Delete DELETE Request
    @DELETE("/tag/{tagId}")
    @Headers("Content-Type: application/json")
    fun deleteTag(@Header("Authorization") authorization: String, @Path("tagId") tagId: String): Call<Unit>
}