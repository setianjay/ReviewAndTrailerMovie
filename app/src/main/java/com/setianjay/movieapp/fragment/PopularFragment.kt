package com.setianjay.movieapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.setianjay.movieapp.R
import com.setianjay.movieapp.activity.DetailActivity
import com.setianjay.movieapp.adapter.MovieAdapter
import com.setianjay.movieapp.constants.Constants
import com.setianjay.movieapp.databinding.FragmentPopularBinding
import com.setianjay.movieapp.model.MovieModel
import com.setianjay.movieapp.model.MovieResponse
import com.setianjay.movieapp.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PopularFragment : Fragment() {
    private val TAG = "PopularFragment"
    private lateinit var binding: FragmentPopularBinding // View Binding
    private lateinit var movieAdapter: MovieAdapter // Data Adapter

    private var scrolling = false
    private var currentPage = 1
    private var totalPages = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = FragmentPopularBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycleView()
        setUpListener()
    }

    override fun onStart() {
        super.onStart()
        getMovie()
        showLoaderNextPage(false)
    }


    private fun setUpRecycleView(){
        movieAdapter = MovieAdapter(arrayListOf(),object : MovieAdapter.OnAdapterListener{
            override fun onClick(movie: MovieModel) {
                val intent = Intent(requireContext(),DetailActivity::class.java)
                intent.putExtra("movie_id",movie.id)
                startActivity(intent)
            }

        })

        binding.rvMovie.apply {
            layoutManager = GridLayoutManager(context,2)
            adapter = movieAdapter
            setHasFixedSize(true)
        }
    }

    private fun setUpListener(){
        binding.nsvMovie.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
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
        showLoading(true)
        ApiService().endPoint.getMoviePopuler(Constants.API_KEY,currentPage)
            .enqueue(object: Callback<MovieResponse>{
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e(TAG,t.toString())
                }

                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful){
                        showLoading(false)
                        showMovie(response.body()!!)
                    }
                }
            })
    }

    private fun showMovie(data: MovieResponse){
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

    private fun showLoading(loading: Boolean){
        return when(loading){
            true -> binding.pbMovie.visibility = View.VISIBLE
            false -> binding.pbMovie.visibility = View.GONE
        }
    }

    private fun showLoaderNextPage(status: Boolean){
        return when(status){
            true -> {
                scrolling = true
                binding.pbMovieNextPage.visibility = View.VISIBLE
            }
            false -> {
                scrolling = false
                binding.pbMovieNextPage.visibility = View.GONE
            }
        }
    }

}