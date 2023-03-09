package com.poly_team.fnews.data

data class NetworkResponse(
    val success: Boolean,
    val message: String?,
    val data: Any?
)