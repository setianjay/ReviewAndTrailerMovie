package com.setianjay.movieapp.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.setianjay.movieapp.R
import com.setianjay.movieapp.activity.DetailActivity
import com.setianjay.movieapp.adapter.MovieAdapter
import com.setianjay.movieapp.constants.Constants
import com.setianjay.movieapp.model.MovieModel
import com.setianjay.movieapp.model.MovieResponse
import com.setianjay.movieapp.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NowPlayingFragment : Fragment() {
    private val TAG = "NowPlayingFragment"
    private lateinit var v: View
    private lateinit var nsvMovie: NestedScrollView
    private lateinit var rvMovie: RecyclerView
    private lateinit var pbMovie: ProgressBar
    private lateinit var pbMovieNextPage: ProgressBar
    private lateinit var movieAdapter: MovieAdapter

    private var scrolling = false
    private var currentPage = 1
    private var totalPages = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_now_playing, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpRecycleView()
        setUpListener()
    }

    override fun onStart() {
        super.onStart()
        getMovie()
        showLoaderNextPage(false)
    }

    private fun initView(){
        nsvMovie = v.findViewById(R.id.nsv_movie)
        rvMovie = v.findViewById(R.id.rv_movie)
        pbMovie = v.findViewById(R.id.pb_movie)
        pbMovieNextPage = v.findViewById(R.id.pb_movie_next_page)
    }

    private fun setUpRecycleView(){
        movieAdapter = MovieAdapter(arrayListOf(),object: MovieAdapter.OnAdapterListener{
            override fun onClick(movie: MovieModel) {
                val intent = Intent(requireContext(),DetailActivity::class.java)
                intent.putExtra("movie_id",movie.id)
                startActivity(intent)
            }
        })

        rvMovie.apply {
            layoutManager = GridLayoutManager(context,2)
            adapter = movieAdapter
            setHasFixedSize(true)
        }
    }

    private fun setUpListener(){
        nsvMovie.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight){
                    if (!scrolling){
                        if (currentPage <= totalPages){
                            getMovieNextPage()
                        }
                    }
                }
            }

        })
    }

    private fun getMovie(){
        showLoader(true)
        ApiService().endPoint.getMovieNowPlaying(Constants.API_KEY,currentPage)
            .enqueue(object: Callback<MovieResponse>{
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e(TAG,t.toString())
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful){
                        showLoader(false)
                        showDataMovie(response.body()!!)
                    }
                }

            })
    }

    private fun showDataMovie(data: MovieResponse){
        totalPages = data.total_pages
        movieAdapter.setData(data.results)
    }

    private fun getMovieNextPage(){
        showLoaderNextPage(true)
        currentPage += 1

        ApiService().endPoint.getMovieNowPlaying(Constants.API_KEY,currentPage)
            .enqueue(object: Callback<MovieResponse>{
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e(TAG,t.toString())
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful){
                        showLoaderNextPage(false)
                        showDataMovieNextPage(response.body()!!)
                    }
                }

            })
    }

    private fun showDataMovieNextPage(data: MovieResponse){
        movieAdapter.setDataNextPage(data.results)
    }

    private fun showLoader(status: Boolean){
        return when(status){
            true -> pbMovie.visibility = View.VISIBLE
            false -> pbMovie.visibility = View.GONE
        }
    }

    private fun showLoaderNextPage(status: Boolean){
        return when(status){
            true -> {
                scrolling = true
                pbMovieNextPage.visibility = View.VISIBLE
            }
            false -> {
                scrolling = false
                pbMovieNextPage.visibility = View.GONE
            }
        }
    }
}