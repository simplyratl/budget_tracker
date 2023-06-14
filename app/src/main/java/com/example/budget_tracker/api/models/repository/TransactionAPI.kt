package com.example.budget_tracker.api.models.repository

import com.example.budget_tracker.api.models.models.AddBudgetUserRequest
import com.example.budget_tracker.api.models.models.AddTransationRequest
import com.example.budget_tracker.api.models.models.LoginRequest
import com.example.budget_tracker.api.models.models.RegisterRequest
import com.example.budget_tracker.api.models.models.ScanQRCodeRequest
import com.example.budget_tracker.api.models.models.ScannedQRCodeResponse
import com.example.budget_tracker.api.models.models.TransactionResponse
import com.example.budget_tracker.api.models.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TransactionAPI {

    @GET("transactions/user/{id}")
    suspend fun getTransactionById(@Path("id") id: String): Response<List<TransactionResponse>>

    @GET("transactions/transactions/statistics/weekly/{id}")
    suspend fun getStatisticsWeek(@Path("id") id: String): Response<List<Double>>

    @GET("transactions/transactions/statistics/month/{id}")
    suspend fun getStatisticsMonth(@Path("id") id: String): Response<List<Double>>

    @POST("users/login/")
    suspend fun loginUser(@Body request: LoginRequest): Response<UserResponse>

    @POST("users/register/")
    suspend fun registerUser(@Body request: RegisterRequest): Response<UserResponse>

    @POST("transactions/")
    suspend fun createTransaction(@Body request: AddTransationRequest): Response<TransactionResponse>

    @POST("users/add-budget")
    suspend fun addBudgetToUser(@Body request: AddBudgetUserRequest): Response<UserResponse>

    @POST("scanning/scan")
    suspend fun scanQRCode(@Body request: ScanQRCodeRequest): Response<ScannedQRCodeResponse>
}