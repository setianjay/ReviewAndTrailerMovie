package com.setianjay.movieapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.setianjay.movieapp.R
import com.setianjay.movieapp.constants.Constants
import com.setianjay.movieapp.model.DetailResponse
import com.setianjay.movieapp.retrofit.ApiService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "DetailActivity"
    var movie_id = 0 // Get intent data

    // View
    lateinit var imgDetail: ImageView
    lateinit var fabPlay: FloatingActionButton
    lateinit var tvDetailTitle: TextView
    lateinit var tvDetailRate: TextView
    lateinit var tvDetailGenre: TextView
    lateinit var tvDetailOverview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(findViewById(R.id.toolbar))
        initView()
        initData()
        initListener()
    }

    override fun onStart() {
        super.onStart()
        getDetail()
    }

    private fun initView(){
        imgDetail = findViewById(R.id.img_detail)
        fabPlay = findViewById(R.id.fab_play)
        tvDetailTitle = findViewById(R.id.tv_detail_title)
        tvDetailRate = findViewById(R.id.tv_detail_rate)
        tvDetailGenre = findViewById(R.id.tv_detail_genre)
        tvDetailOverview = findViewById(R.id.tv_detail_overview)

//        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title

        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initData(){
        movie_id = intent.getIntExtra("movie_id",0)
    }

    private fun initListener(){
        fabPlay.setOnClickListener(this)
    }


    private fun getDetail(){
        ApiService().endPoint.getMovieDetails(movie_id,Constants.API_KEY)
            .enqueue(object: Callback<DetailResponse>{
                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    Log.e(TAG,t.toString())
                }

                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if (response.isSuccessful){
                        showDetail( response.body()!! )
                    }
                }

            })
    }

    private fun showDetail(response: DetailResponse){
        var listGenre: String = ""
        // Show Image
        val backdrop = Constants.BACKDROP_PATH + response.backdrop_path
        Picasso.get()
            .load(backdrop)
            .error(R.drawable.play_white)
            .into(imgDetail)

        tvDetailTitle.text = response.title
        tvDetailRate.text = response.vote_average.toString()
        tvDetailOverview.text = response.overview

        for(genre in response.genres!!){
            listGenre += "${genre.name} "
        }
        val genre = listGenre.split(" ")
            .joinToString(", ")
            .trimEnd(',',' ')
        tvDetailGenre.text = genre
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.fab_play -> {
                val intent = Intent(applicationContext,TrailerActivity::class.java)
                intent.putExtra("movie_id",movie_id)
                startActivity(intent)
            }
        }
    }
}