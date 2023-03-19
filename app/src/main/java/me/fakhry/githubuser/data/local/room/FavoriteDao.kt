package me.fakhry.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import me.fakhry.githubuser.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteEntity: FavoriteEntity)

    @Delete
    suspend fun delete(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM FavoriteEntity")
    suspend fun getAllFavorite(): List<FavoriteEntity>

    @Query("SELECT EXISTS(SELECT * FROM FavoriteEntity WHERE username = :username AND is_favorite = 1)")
    suspend fun isFavorite(username: String): Boolean

    @Query("SELECT * FROM FavoriteEntity WHERE username = :username")
    fun getFavoriteByUsername(username: String): LiveData<FavoriteEntity?>
}