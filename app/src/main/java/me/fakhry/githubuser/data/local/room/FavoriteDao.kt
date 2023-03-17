package me.fakhry.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import me.fakhry.githubuser.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteEntity: FavoriteEntity)

    @Delete
    fun delete(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM FavoriteEntity")
    fun getAllFavorite(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM FavoriteEntity WHERE username = :username")
    fun getFavoriteByUsername(username: String): LiveData<FavoriteEntity>
}