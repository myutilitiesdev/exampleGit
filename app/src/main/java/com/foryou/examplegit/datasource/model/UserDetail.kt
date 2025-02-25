package com.foryou.examplegit.datasource.model

import com.google.gson.annotations.SerializedName

data class UserDetail(
    val id: Int,
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    val location: String? = null,
    val followers: Int = 0,
    val following: Int = 0,
)

fun UserDetail.toUser(): User {
    return User(
        id = this.id,
        login = this.login,
        avatarUrl = this.avatarUrl,
        htmlUrl = this.htmlUrl
    )
}