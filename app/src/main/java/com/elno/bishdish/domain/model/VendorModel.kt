package com.elno.bishdish.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VendorModel(
    val id: String? = null,
    val title: Map<String, String>? = null,
    val imageUrl: String? = null,
    val type: ArrayList<String>? = null,
    val location: Map<String, Double>? = null,
    val description: Map<String, String>? = null,
    val mobile: String? = null,
    val instagram: String? = null,
    val whatsapp: String? = null,
    val images: ArrayList<String>? = null,
    val ingredients: ArrayList<String>? = null,
    val isPopular: Boolean? = null,
    val order: Int? = null,
): Parcelable