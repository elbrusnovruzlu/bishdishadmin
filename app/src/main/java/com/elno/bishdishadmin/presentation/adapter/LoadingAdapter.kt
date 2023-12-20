package com.elno.bishdishadmin.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elno.bishdishadmin.R
import java.util.*


class LoadingAdapter : RecyclerView.Adapter<LoadingAdapter.LoadingHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingHolder {
        return LoadingHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_loading, parent, false)
        )
    }

    override fun getItemCount() = 1

    override fun onBindViewHolder(holder: LoadingHolder, position: Int) {
        //no logic here
    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}