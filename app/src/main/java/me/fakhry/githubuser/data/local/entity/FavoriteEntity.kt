package me.fakhry.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class FavoriteEntity(

    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    var username: String = "",

    @field:ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null,

    @field:ColumnInfo(name = "is_favorite")
    var isFavorited: Boolean
)