package com.setianjay.movieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.setianjay.movieapp.R
import com.setianjay.movieapp.model.MovieModel

class MovieAdapter(val movies: ArrayList<MovieModel>): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_movies,parent,false)
    )

    override fun getItemCount(): Int {
       return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imgMovies = view.findViewById<ImageView>(R.id.img_movie)
        val labelMovies = view.findViewById<TextView>(R.id.tv_label)
        fun bind(movies: MovieModel){
            labelMovies.text = movies.title
        }
    }

    public fun setData(newMovies: List<MovieModel>){
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

}