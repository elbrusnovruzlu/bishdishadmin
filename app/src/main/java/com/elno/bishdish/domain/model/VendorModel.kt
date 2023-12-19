package com.elno.bishdish.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class VendorModel(
    val id: String? = null,
    val title: Map<String, String>? = null,
    val imageUrl: String? = null,
    val type: ArrayList<String>? = null,
    val description: Map<String, String>? = null,
    val images: ArrayList<String>? = null,
    val ingredients: ArrayList<String>? = null,
    val isPopular: Boolean? = null,
    val order: Int? = null,
): Parcelable