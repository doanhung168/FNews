package com.poly_team.fnews.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    var id: String? = null,
    var avatar: String? = null,
    var title: String? = null,
    var content: String? = null,
    var field: String? = null,
    var state: String? = null,
    var stateExtra: String? = null,
    var active: Boolean = true,
    var comment: List<String>? = null,
    var tags: List<String>? = null,
    var author: String? = null,
    var like: Int = 0,
    var time: Long = 0,
    var isSaved: Boolean = false
) : Parcelable