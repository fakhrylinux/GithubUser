package me.fakhry.githubuser.data.network.response

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(

    @field:SerializedName("items")
    val items: List<ItemsItem>
)

data class ItemsItem(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String
)
