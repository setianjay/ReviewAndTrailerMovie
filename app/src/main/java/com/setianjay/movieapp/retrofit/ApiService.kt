package com.setianjay.movieapp.retrofit

import com.setianjay.movieapp.constants.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class ApiService {
    val endPoint: ApiEndPoint
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
           val retrofit = Retrofit.Builder()
               .baseUrl(Constants.BASE_URL)
               .client(httpClient)
               .addConverterFactory(GsonConverterFactory.create())
               .build()

            return retrofit.create(ApiEndPoint::class.java)
        }
}