package com.elno.bishdish.presentation.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.elno.bishdish.R
import com.facebook.shimmer.ShimmerFrameLayout

class GallerySliderAdapter(private val context: Context) : PagerAdapter() {

    private var list: ArrayList<String?> = arrayListOf()

    fun submitList(newList: ArrayList<String>?) {
        newList?.let {
            list.clear()
            list.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =  (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.item_gallery, null)
        val images = view.findViewById<ImageView>(R.id.imageView)
        val shimmerView = view.findViewById<ShimmerFrameLayout>(R.id.shimmerView)

        val item = list[position]

        shimmerView.isVisible = true
        shimmerView.startShimmer()
        item?.let {
            Glide.with(context)
                .load(it)
                .listener(object : RequestListener<Drawable> {
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
                .into(images)
        }

        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

}