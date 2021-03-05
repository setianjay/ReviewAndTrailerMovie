package com.setianjay.movieapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.setianjay.movieapp.R
import com.setianjay.movieapp.adapter.TrailerAdapter
import com.setianjay.movieapp.constants.Constants
import com.setianjay.movieapp.model.TrailerModel
import com.setianjay.movieapp.model.TrailerResponse
import com.setianjay.movieapp.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TrailerActivity : AppCompatActivity() {
    private val TAG = "TrailerActivity"
    lateinit var rvReview: RecyclerView
    lateinit var youtubePlayer: YouTubePlayer
    lateinit var trailerAdapter: TrailerAdapter
    lateinit var pbTrailer: ProgressBar
    var movie_id = 0
    private var videoKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer)
        initView()
        initData()
        setUpRecycleview()
    }

    override fun onStart() {
        super.onStart()
        getTrailer()
    }

    private fun initView(){
        rvReview = findViewById(R.id.list_movie_trailer)
        pbTrailer = findViewById(R.id.pb_movie_trailer)

        val youtubePlayerTrailer =
            findViewById<YouTubePlayerView>(R.id.youtube_player_trailer)
        lifecycle.addObserver(youtubePlayerTrailer)

        youtubePlayerTrailer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youtubePlayer = player
                videoKey?.let {
                    youtubePlayer.cueVideo(it, 0f)
                }
            }
        })

    }

    private fun initData(){
        movie_id = intent.getIntExtra("movie_id",0)
    }

    private fun setUpRecycleview(){
        trailerAdapter = TrailerAdapter(arrayListOf(),object: TrailerAdapter.OnAdapterListener{
            override fun onPlay(key: String,title: String) {
                youtubePlayer.loadVideo(key, 0f)
                supportActionBar!!.title = title
            }

            override fun onLoad(key: String,title: String) {
                videoKey = key
                supportActionBar!!.title = title
            }

        })

        rvReview.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = trailerAdapter
            setHasFixedSize(true)
        }
    }

    private fun getTrailer(){
        showLoading(true)
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
                        showLoading(false)
                        showTrailer(response.body()!!)
//                        val body = response.body()
//                        Log.d(TAG,"${body}")
                    }
                }
            })
    }

    private fun showTrailer(response: TrailerResponse){
        trailerAdapter.setData(response.results)
//        for (trailer in response.results){
//            Log.d(TAG,"${trailer.name}")
//        }
    }

    private fun showLoading(loading: Boolean){
        return when(loading){
            true -> pbTrailer.visibility = View.VISIBLE
            false -> pbTrailer.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}