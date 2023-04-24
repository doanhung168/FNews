package com.poly_team.fnews.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "field")
@kotlinx.parcelize.Parcelize
data class Field(
    @PrimaryKey
    var id: String? = null,
    var value: String? = null,
    var description: String? = null
) : Parcelable