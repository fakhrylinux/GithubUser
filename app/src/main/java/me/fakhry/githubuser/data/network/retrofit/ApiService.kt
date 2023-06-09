package me.fakhry.githubuser.data.network.retrofit

import me.fakhry.githubuser.data.network.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getUsersByQuery(
        @Query("q") query: String
    ): Call<SearchUserResponse>

    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String
    ): Call<GetUserResponse>

    @GET("users/{username}/followers")
    fun getListFollowersOfUser(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getListFollowingOfUser(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}