package com.setianjay.movieapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ApiService {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/movie/"
    }
    val endPoint: ApiEndPoint
        get() {
           val retrofit = Retrofit.Builder()
               .baseUrl(BASE_URL)
               .addConverterFactory(GsonConverterFactory.create())
               .build()

            return retrofit.create(ApiEndPoint::class.java)
        }
}