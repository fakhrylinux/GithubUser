package me.fakhry.githubuser.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
class FavoriteEntity(

    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    var username: String = "",

    @field:ColumnInfo(name = "avatar_url")
    var avatarUrl: String,

    @field:ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean
) : Parcelable