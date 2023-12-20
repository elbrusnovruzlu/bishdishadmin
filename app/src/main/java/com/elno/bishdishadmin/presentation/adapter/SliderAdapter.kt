package com.elno.bishdishadmin.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.elno.bishdishadmin.R
import com.elno.bishdishadmin.common.UtilityFunctions.getLocalizedTextFromMap
import com.elno.bishdishadmin.domain.model.SliderModel

class SliderAdapter(private val context: Context, private val onClick: (sliderModel: SliderModel?) -> Unit) : PagerAdapter() {

    private var list: ArrayList<SliderModel?> = arrayListOf()

    fun submitList(newList: ArrayList<SliderModel?>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =  (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.item_slider, null)
        val images = view.findViewById<ImageView>(R.id.image)

        val item = list[position]

        view.setOnClickListener {
            onClick(item)
        }
        item?.let {
            Glide.with(context)
                .load(getLocalizedTextFromMap(context, it.image))
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