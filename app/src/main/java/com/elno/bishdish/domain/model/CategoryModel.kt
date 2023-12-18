package com.elno.bishdish.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryModel(
    val icon: String? = null,
    val name: Map<String, String>? = null,
    val type: String? = null
): Parcelable