package com.setianjay.movieapp.adapter

import android.icu.text.CaseMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.setianjay.movieapp.R
import com.setianjay.movieapp.model.TrailerModel

class TrailerAdapter(val reviewList: ArrayList<TrailerModel>, val listener: OnAdapterListener) :
    RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie_trailers, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = reviewList.size


    override fun onBindViewHolder(holder: TrailerAdapter.ViewHolder, position: Int) {
        holder.funBind(reviewList[position])
        holder.tvLabelTrailer.setOnClickListener{
            listener.onPlay(reviewList[position].key!!,reviewList[position].name!!)
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvLabelTrailer = view.findViewById<TextView>(R.id.tv_title_trailer)
        fun funBind(trailer: TrailerModel) {
            tvLabelTrailer.text = trailer.name
        }
    }

    fun setData(data: List<TrailerModel>) {
        reviewList.clear()
        reviewList.addAll(data)
        notifyDataSetChanged()
        listener.onLoad(data[0].key!!,data[0].name!!)
    }

    interface OnAdapterListener {
        fun onPlay(key: String,title: String)
        fun onLoad(key: String,title: String)
    }
}