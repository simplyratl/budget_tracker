package com.example.budget_tracker.api.models.repository

import com.example.budget_tracker.api.models.models.AddTransationRequest
import com.example.budget_tracker.api.models.models.LoginRequest
import com.example.budget_tracker.api.models.models.TransactionResponse
import com.example.budget_tracker.api.models.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TransactionAPI {

    @GET("transactions/")
    suspend fun getAllTransactions():Response<List<TransactionResponse>>

    @POST("users/login/")
    suspend fun loginUser(@Body request: LoginRequest): Response<UserResponse>

    @POST("transactions/")
    suspend fun createTransaction(@Body request: AddTransationRequest): Response<TransactionResponse>
}