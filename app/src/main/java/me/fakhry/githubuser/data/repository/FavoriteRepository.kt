package me.fakhry.githubuser.data.repository

import me.fakhry.githubuser.data.local.entity.FavoriteEntity
import me.fakhry.githubuser.data.local.room.FavoriteDao

class FavoriteRepository private constructor(private val favoriteDao: FavoriteDao) {
    suspend fun getAllFavorite(): List<FavoriteEntity> {
        return favoriteDao.getAllFavorite()
    }

    suspend fun setFavorite(favorite: FavoriteEntity, favoriteState: Boolean) {
        favorite.isFavorite = favoriteState
        favoriteDao.insert(favorite)
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