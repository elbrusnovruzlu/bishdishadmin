package com.elno.bishdish.presentation.adapter

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elno.bishdish.R
import com.elno.bishdish.common.Constants.FAVOURITE_LIST
import com.elno.bishdish.common.UtilityFunctions
import com.elno.bishdish.data.local.LocalDataStore
import com.elno.bishdish.domain.model.VendorModel

class VendorAdapter(
    private val onClick: (item: VendorModel?) -> Unit,
    private val onEmptyResult: (isEmpty: Boolean) -> Unit,
    val context: Context
) :  RecyclerView.Adapter<VendorAdapter.ViewHolder>(), Filterable {

    private var list = mutableListOf<VendorModel?>()
    private var filterList = mutableListOf<VendorModel?>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: MutableList<VendorModel?>) {
        list = newList
        filterList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_offer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filterList[position]
        holder.bind(item, onClick)
    }

    override fun getItemCount(): Int {
        return filterList.size
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filterList = if (charSearch.isEmpty()) { list }
                else {
                    val resultList = ArrayList<VendorModel?>()
                    list.forEach{
                        if (UtilityFunctions.getLocalizedTextFromMap(context, it?.title).lowercase().contains(charSearch.lowercase())) {
                            resultList.add(it)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as ArrayList<VendorModel?>
                onEmptyResult(filterList.isEmpty())
                notifyDataSetChanged()
            }
        }
    }


}