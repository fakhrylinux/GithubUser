package me.fakhry.githubuser.di

import android.content.Context
import me.fakhry.githubuser.data.local.room.FavoriteDatabase
import me.fakhry.githubuser.data.repository.FavoriteRepository

object Injection {

    fun provideRepository(context: Context): FavoriteRepository {
        val database = FavoriteDatabase.getInstance(context)
        val dao = database.favoriteDao()

        return FavoriteRepository.getInstance(dao)
    }
}