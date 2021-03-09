package com.setianjay.movieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.setianjay.movieapp.databinding.ItemMovieTrailersBinding
import com.setianjay.movieapp.model.TrailerModel

class TrailerAdapter(val trailerList: ArrayList<TrailerModel>, val listener: OnAdapterListener) :
    RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemMovieTrailersBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )

    override fun getItemCount(): Int = trailerList.size


    override fun onBindViewHolder(holder: TrailerAdapter.ViewHolder, position: Int) {
        holder.funBind(trailerList[position])
        holder.binding.tvTitleTrailer.setOnClickListener{
            listener.onPlay(trailerList[position].key!!,trailerList[position].name!!)
        }
    }


    inner class ViewHolder(val binding: ItemMovieTrailersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun funBind(trailer: TrailerModel) {
            binding.tvTitleTrailer.text = trailer.name
        }
    }

    fun setData(data: List<TrailerModel>) {
        trailerList.clear()
        trailerList.addAll(data)
        notifyDataSetChanged()
        listener.onLoad(data[0].key!!,data[0].name!!)
    }

    interface OnAdapterListener {
        fun onPlay(key: String,title: String)
        fun onLoad(key: String,title: String)
    }
}