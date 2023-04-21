package com.poly_team.fnews.data.model

data class User(
    private val id: String,
    private val username: String,
    private val avatar: String,
    private val displayName: Int,
    private val active: Boolean,
    private val role: Int,
    private val accountType: String,
)
