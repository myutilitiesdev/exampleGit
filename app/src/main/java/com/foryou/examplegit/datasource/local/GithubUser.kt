package com.foryou.examplegit.datasource.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.foryou.examplegit.datasource.model.User

@Entity(tableName = "github_users")
data class GithubUser(
    @PrimaryKey val id: Int,
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String
)

fun User.toEntity(): GithubUser {
    return GithubUser(
        id = this.id,
        login = this.login,
        avatarUrl = this.avatarUrl,
        htmlUrl = this.htmlUrl
    )
}

fun GithubUser.toDomain(): User {
    return User(
        id = this.id,
        login = this.login,
        avatarUrl = this.avatarUrl,
        htmlUrl = this.htmlUrl
    )
}