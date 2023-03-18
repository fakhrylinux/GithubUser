package me.fakhry.githubuser.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import me.fakhry.githubuser.data.local.entity.FavoriteEntity
import me.fakhry.githubuser.data.local.room.FavoriteDao

class FavoriteRepository private constructor(
    private val favoriteDao: FavoriteDao
) {
    private val result = MediatorLiveData<Result<List<FavoriteEntity>>>()

    suspend fun getAllFavorite(): List<FavoriteEntity> {
        return favoriteDao.getAllFavorite()
    }

    suspend fun setFavorite(favorite: FavoriteEntity, favoriteState: Boolean) {
        favorite.isFavorite = favoriteState
        favoriteDao.insert(favorite)
    }

    fun getFavoriteByUsername(username: String): LiveData<FavoriteEntity?> {
        return favoriteDao.getFavoriteByUsername(username)
    }

    suspend fun isFavorite(username: String): Boolean {
        return favoriteDao.isFavorite(username)
    }

    suspend fun deleteFavorite(favorite: FavoriteEntity) {
        favoriteDao.delete(favorite)
    }

    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null
        fun getInstance(
            favoriteDao: FavoriteDao,
        ): FavoriteRepository = instance ?: synchronized(this) {
            instance ?: FavoriteRepository(favoriteDao)
        }.also { instance = it }
    }
}