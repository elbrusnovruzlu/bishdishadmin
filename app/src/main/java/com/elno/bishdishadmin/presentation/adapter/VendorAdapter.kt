package com.elno.bishdishadmin.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elno.bishdishadmin.R
import com.elno.bishdishadmin.common.Constants.FAVOURITE_LIST
import com.elno.bishdishadmin.common.UtilityFunctions
import com.elno.bishdishadmin.data.local.LocalDataStore
import com.elno.bishdishadmin.domain.model.VendorModel

class VendorAdapter(
    private val isHorizontal: Boolean = false,
    private val onClick: (item: VendorModel?) -> Unit
) :  RecyclerView.Adapter<VendorAdapter.ViewHolder>() {

    private var list = arrayListOf<VendorModel?>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: ArrayList<VendorModel?>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(newList: MutableList<VendorModel?>) {
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_offer, parent, false)
        if(isHorizontal) view.layoutParams.width = (UtilityFunctions.getScreenWidth() / 2.5f).toInt()
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
        private val name: TextView = view.findViewById(R.id.name)
        private val type: TextView = view.findViewById(R.id.type)
        private val favButton: ToggleButton = view.findViewById(R.id.favButton)
        private val cardView: CardView = view.findViewById(R.id.cardView)

        fun bind(item: VendorModel?, onClick: (categoryModel: VendorModel?) -> Unit) {
            item?.imageUrl.let {
                Glide.with(itemView.context)
                    .load(it)
                    .into(imageView)
            }
            name.text = UtilityFunctions.getLocalizedTextFromMap(itemView.context, item?.title)
            type.text = item?.type?.map { UtilityFunctions.getType(itemView.context, it) }.toString()
            favButton.isChecked = LocalDataStore(itemView.context).getList<String>(FAVOURITE_LIST).contains(item?.id) == true
            cardView.setOnClickListener {
                onClick(item)
            }
            favButton.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) {
                    LocalDataStore(itemView.context).addToList(item?.id, FAVOURITE_LIST)
                }
                else {
                    LocalDataStore(itemView.context).removeFromList(item?.id, FAVOURITE_LIST)
                }
            }
        }
    }

}