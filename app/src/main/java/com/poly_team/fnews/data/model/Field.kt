package com.poly_team.fnews.data.model

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Field(
    var id: String? = null,
    var value: String? = null,
    var description: String? = null
) : Parcelable