package me.fakhry.githubuser.networking

import me.fakhry.githubuser.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getUsersByQuery(
        @Query("q") query: String
    ): Call<UserResponse>
}