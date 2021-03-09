package com.setianjay.movieapp.adapter

import android.nfc.Tag
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.setianjay.movieapp.R
import com.setianjay.movieapp.constants.Constants
import com.setianjay.movieapp.databinding.ItemMoviesBinding
import com.setianjay.movieapp.model.MovieModel
import com.setianjay.movieapp.model.MovieResponse
import com.setianjay.movieapp.util.Util
import com.squareup.picasso.Picasso

class MovieAdapter(val movies: ArrayList<MovieModel>, val listener: OnAdapterListener) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private val TAG = "MovieAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemMoviesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.binding.imgMovie.setOnClickListener {
            listener.onClick(movies[position])
        }
    }

    inner class ViewHolder(val binding: ItemMoviesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movies: MovieModel) {
            binding.tvLabelTitle.text = movies.title
            binding.tvLabelDate.text = movies.release_date
            Picasso.get()
                .load(Constants.POSTER_PATH + movies.poster_path)
                .into(binding.imgMovie)
//            Log.d(TAG,movies.backdrop_path!!)
        }

    }

    fun setData(newMovies: List<MovieModel>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    fun setDataNextPage(newMovies: List<MovieModel>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(movie: MovieModel)
    }

}