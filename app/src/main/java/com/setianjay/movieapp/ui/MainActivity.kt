package com.setianjay.movieapp.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.method.MovementMethod
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.setianjay.movieapp.R
import com.setianjay.movieapp.adapter.MovieAdapter
import com.setianjay.movieapp.constants.Constants
import com.setianjay.movieapp.model.MovieResponse
import com.setianjay.movieapp.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"
    private var movieCategory = 0
    private val api = ApiService().endPoint
    lateinit var movieAdapter: MovieAdapter
    lateinit var rvMovies: RecyclerView
    lateinit var pbMovies: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        setUpView()
        setUpRecycleView()
        transStatusBar()
    }

    override fun onStart() {
        super.onStart()
        getMovie()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun setUpView() {
        // Initialisasi View
        rvMovies = findViewById(R.id.rv_movie)
        pbMovies = findViewById(R.id.pb_movie)

    }

    private fun setUpRecycleView() {
        movieAdapter = MovieAdapter(arrayListOf())
        rvMovies.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = movieAdapter
        }

    }

    // Function statusbar transparant
    private fun transStatusBar(){
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    private fun getMovie() {
        showLoading(true)
        var apiCall: Call<MovieResponse>? = null

        when (movieCategory) {
            0 -> {
                apiCall = api.getMovieNowPlaying(Constants.API_KEY, 1)
            }
            1 -> {
                apiCall = api.getMoviePopuler(Constants.API_KEY, 1)
            }
        }

        apiCall!!.enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    showLoading(false)
                    Log.d(TAG, t.toString())
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        showMovie(response.body()!!)
                    }
                }

            })
    }

    private fun showMovie(response: MovieResponse) {
        movieAdapter.setData(response.results)
//        for (movie in response.results){
//            println(movie)
//        }
    }

    private fun showLoading(loading: Boolean) {
        return when (loading) {
            true -> pbMovies.visibility = View.VISIBLE
            false -> pbMovies.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_popular -> {
                Toast.makeText(applicationContext, "Action Popular", Toast.LENGTH_SHORT).show()
                movieCategory = 1
                getMovie()
                true
            }
            R.id.action_now_playing -> {
                Toast.makeText(applicationContext, "Action Now Playing", Toast.LENGTH_SHORT).show()
                movieCategory = 0
                getMovie()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}