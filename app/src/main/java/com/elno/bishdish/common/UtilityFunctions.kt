package com.elno.bishdish.common

import android.content.Context
import android.content.res.Resources
import com.elno.bishdish.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


object UtilityFunctions {

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return (dp * displayMetrics.density).roundToInt()
    }

    fun dpToPxFloat(context: Context, dp: Int): Float {
        val displayMetrics = context.resources.displayMetrics
        return (dp * displayMetrics.density)
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getLocalizedTextFromMap(context: Context?, map: Map<String, String>?): String {
        val langString: String = LocaleManager(context).getLanguage()
        return map?.get(langString).orEmpty()
    }

    fun getType(context: Context?, type: String?): String {
        return when(type) {
            "all" -> context?.getString(R.string.all).orEmpty()
            else -> {
                getLocalizedTextFromMap(context, Static.filterModel.categories?.find { it?.type == type}?.name)
            }
        }
    }

    fun convertDate(context: Context?, time: Long): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy - HH:mm", LocaleManager(context).getLocale(context?.resources))
        val netDate = Date(time)
        return sdf.format(netDate)
    }
}