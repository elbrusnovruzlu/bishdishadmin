package com.elno.bishdish.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.elno.bishdish.R
import com.facebook.shimmer.ShimmerFrameLayout

class GalleryAdapter(
    private val onClick: (position: Int) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {


    private val imageList = arrayListOf<String?>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: ArrayList<String>?) {
        newList?.let {
            imageList.clear()
            imageList.addAll(it)
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = imageList[position]
        holder.bind(item, onClick)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        private val shimmerView: ShimmerFrameLayout = view.findViewById(R.id.shimmerView)

        fun bind(item: String?, onClick: (position: Int) -> Unit) {
            shimmerView.isVisible = true
            shimmerView.startShimmer()
            item?.let {
                Glide.with(itemView.context)
                    .load(it)
                    .listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            shimmerView.isVisible = false
                            shimmerView.stopShimmer()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            shimmerView.isVisible = false
                            shimmerView.stopShimmer()
                            return false
                        }
                    })
                    .into(imageView)
            }
            itemView.setOnClickListener {
                onClick(adapterPosition)
            }
        }
    }

}