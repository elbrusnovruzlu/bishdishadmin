package com.elno.bishdish.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.elno.bishdish.R
import com.elno.bishdish.common.UtilityFunctions.convertDate
import com.elno.bishdish.common.UtilityFunctions.getLocalizedTextFromMap
import com.elno.bishdish.domain.model.NotificationModel
import com.elno.bishdish.presentation.notification.NotificationAction


class NotificationAdapter(
    private val onClick: (notificationModel: NotificationModel?) -> Unit
): RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private val list = arrayListOf<NotificationModel?>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: MutableList<NotificationModel?>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.default_cell, parent, false)
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
        private val icon: ImageView = view.findViewById(R.id.icon)
        private val title: TextView = view.findViewById(R.id.title)
        private val subTitle: TextView = view.findViewById(R.id.subTitle)

        fun bind(item: NotificationModel?, onClick: (notificationModel: NotificationModel?) -> Unit) {
            val iconResId = when (item?.action) {
                NotificationAction.OPEN_VENDOR.value -> R.drawable.notification_partner
                NotificationAction.NEW_CATEGORY.value -> R.drawable.notification_category
                else -> R.drawable.notification_info
            }
            icon.setImageResource(iconResId)
            icon.setColorFilter(ContextCompat.getColor(itemView.context, R.color.brandColor), android.graphics.PorterDuff.Mode.SRC_IN)
            subTitle.isVisible = true
            title.text = getLocalizedTextFromMap(itemView.context, item?.title)
            item?.time?.let { subTitle.text =  convertDate(itemView.context, it) }
            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }
}