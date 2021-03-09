package com.setianjay.movieapp.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.setianjay.movieapp.R
import com.setianjay.movieapp.adapter.MovieAdapter
import com.setianjay.movieapp.constants.Constants
import com.setianjay.movieapp.model.MovieModel
import com.setianjay.movieapp.model.MovieResponse
import com.setianjay.movieapp.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class  MainActivity : AppCompatActivity() {
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var nsvMovie: NestedScrollView
    private lateinit var rvMovies: RecyclerView
    private lateinit var pbMovies: ProgressBar
    private lateinit var pbMoviesNextPage: ProgressBar
    private val TAG: String = "MainActivity"
    private var movieCategory = 0
    private val api = ApiService().endPoint
    private var scrolling = false
    private var currentPage = 1 // Default page 1
    private var totalPages = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        transStatusBar()
        initView()
        setUpRecycleView()
        setUpListener()
    }

    override fun onStart() {
        super.onStart()
        getMovie()
        showLoadingNextPage(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun initView() {
        // Initialisasi View
        nsvMovie = findViewById(R.id.nsv_movie)
        rvMovies = findViewById(R.id.rv_movie)
        pbMovies = findViewById(R.id.pb_movie)
        pbMoviesNextPage = findViewById(R.id.pb_movie_next_page)

    }

    private fun setUpRecycleView() {
        movieAdapter = MovieAdapter(arrayListOf(),object: MovieAdapter.OnAdapterListener{
            override fun onClick(movie: MovieModel) {
                val intent = Intent(applicationContext,DetailActivity::class.java)
                intent.putExtra("movie_id",movie.id)
                startActivity(intent)
//                Toast.makeText(applicationContext, "${movie.title}", Toast.LENGTH_SHORT).show()
            }

        })

        rvMovies.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = movieAdapter
            setHasFixedSize(true)
        }

    }

    private fun setUpListener(){
        nsvMovie.setOnScrollChangeListener(object: NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                // Apakah scroll sudah mentok kebawah
                if(scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight){
                    if (!scrolling){ // Jika scrolling True
                        if(currentPage <= totalPages){ // Jika currenPage lebih kecil dari totalPage
                        getMovieNextPage()
                        }
                    }
                }
            }

        })
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
        nsvMovie.scrollTo(0,0)
        showLoading(true)
        currentPage = 1
        var apiCall: Call<MovieResponse>? = null
        when (movieCategory) {
            0 -> {
                apiCall = api.getMovieNowPlaying(Constants.API_KEY, currentPage)
            }
            1 -> {
                apiCall = api.getMoviePopuler(Constants.API_KEY, currentPage)
            }
        }

        apiCall!!.enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    showLoading(false)
                    Log.e(TAG, t.toString())
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
        totalPages = response.total_pages
        movieAdapter.setData(response.results)
//        for (movie in response.results){
//            println(movie)
//        }
    }


    private fun getMovieNextPage() {
        currentPage += 1
        showLoadingNextPage(true)
        var apiCall: Call<MovieResponse>? = null

        when (movieCategory) {
            0 -> {
                apiCall = api.getMovieNowPlaying(Constants.API_KEY, currentPage)
            }
            1 -> {
                apiCall = api.getMoviePopuler(Constants.API_KEY, currentPage)
            }
        }

        apiCall!!.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                showLoadingNextPage(false)
                Log.e(TAG, t.toString())
            }

            override fun onResponse(
                call: Call<MovieResponse>,
                response: Response<MovieResponse>
            ) {
                showLoadingNextPage(false)
                if (response.isSuccessful) {
                    showMovieNextPage(response.body()!!)
                }
            }

        })
    }

    private fun showMovieNextPage(response: MovieResponse) {
        totalPages = response.total_pages
        movieAdapter.setDataNextPage(response.results)
        Toast.makeText(applicationContext, "Page ${currentPage}", Toast.LENGTH_SHORT).show()
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

    private fun showLoadingNextPage(loading: Boolean) {
        return when (loading) {
            true -> {
                scrolling = true
                pbMoviesNextPage.visibility = View.VISIBLE
            }
            false -> {
                scrolling = false
                pbMoviesNextPage.visibility = View.GONE
            }
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