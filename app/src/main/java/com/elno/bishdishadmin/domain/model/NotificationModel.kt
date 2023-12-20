package com.elno.bishdishadmin.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationModel(
    val id: String? = null,
    val action: String? = null,
    val actionId: String? = null,
    val title: Map<String, String>? = null,
    val subtitle: Map<String, String>? = null,
    val time: Long? = null
): Parcelable
