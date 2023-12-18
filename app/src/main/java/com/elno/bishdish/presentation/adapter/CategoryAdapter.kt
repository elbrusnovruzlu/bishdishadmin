package com.elno.bishdish.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elno.bishdish.R
import com.elno.bishdish.common.UtilityFunctions.dpToPx
import com.elno.bishdish.common.UtilityFunctions.getLocalizedTextFromMap
import com.elno.bishdish.common.UtilityFunctions.getScreenWidth
import com.elno.bishdish.domain.model.CategoryModel


class CategoryAdapter(private val onClick: (categoryModel: CategoryModel?) -> Unit): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val list = arrayListOf<CategoryModel?>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: ArrayList<CategoryModel?>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_category, parent, false)
        view.layoutParams.width = ((getScreenWidth() - dpToPx(parent.context, 20))/ 3.5).toInt()
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item, onClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        private val categoryName: TextView = view.findViewById(R.id.categoryName)
        private val cardView: CardView = view.findViewById(R.id.cardView)

        fun bind(item: CategoryModel?, onClick: (categoryModel: CategoryModel?) -> Unit) {
            item?.icon.let {
                Glide.with(itemView.context)
                    .load(it)
                    .into(imageView)
            }
            categoryName.text = getLocalizedTextFromMap(itemView.context, item?.name)
            cardView.setOnClickListener {
                onClick(item)
            }
        }
    }


}