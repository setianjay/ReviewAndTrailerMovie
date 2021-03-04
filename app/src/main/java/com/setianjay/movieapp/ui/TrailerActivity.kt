package com.setianjay.movieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.setianjay.movieapp.R
import com.setianjay.movieapp.constants.Constants
import com.setianjay.movieapp.model.TrailerModel
import com.setianjay.movieapp.model.TrailerResponse
import com.setianjay.movieapp.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrailerActivity : AppCompatActivity() {
    private val TAG = "TrailerActivity"
    var movie_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer)
        initData()
    }

    override fun onStart() {
        super.onStart()
        getTrailer()
    }

    private fun initData(){
        movie_id = intent.getIntExtra("movie_id",0)
    }

    private fun getTrailer(){
        ApiService().endPoint.getMovieTrailer(movie_id,Constants.API_KEY)
            .enqueue(object: Callback<TrailerResponse>{
                override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {
                    Log.e(TAG,t.toString())
                }

                override fun onResponse(
                    call: Call<TrailerResponse>,
                    response: Response<TrailerResponse>
                ) {
                    if (response.isSuccessful){
                        showTrailer(response.body()!!)
                    }
                }
            })
    }

    private fun showTrailer(response: TrailerResponse){
        for (trailer in response.results){
            Log.d(TAG,"${trailer.name}")
        }
    }
}