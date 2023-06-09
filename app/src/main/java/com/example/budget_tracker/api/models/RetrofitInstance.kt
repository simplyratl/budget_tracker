package com.example.budget_tracker.api.models

import com.example.budget_tracker.api.models.repository.TransactionAPI
import com.example.budget_tracker.constants.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {
    companion object{
        private val retrofit by lazy {
            var logging = HttpLoggingInterceptor()
            logging. setLevel(HttpLoggingInterceptor.Level.BODY )
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        val api by lazy {
             retrofit.create(TransactionAPI::class.java)
        }
    }
}