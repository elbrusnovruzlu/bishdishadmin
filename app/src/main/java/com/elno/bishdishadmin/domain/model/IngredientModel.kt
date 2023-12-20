package com.elno.bishdishadmin.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IngredientModel(
    val name: Map<String, String>? = null,
    val type: String? = null
): Parcelable