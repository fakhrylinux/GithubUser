package me.fakhry.githubuser.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.fakhry.githubuser.data.local.entity.FavoriteEntity
import me.fakhry.githubuser.data.repository.FavoriteRepository

class FavoriteViewModel(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _listFavorite = MutableLiveData<List<FavoriteEntity>>()
    val listFavorite: LiveData<List<FavoriteEntity>> = _listFavorite

    init {
        getFavoriteUsers()
    }

    fun getFavoriteUsers() {
        viewModelScope.launch {
            _listFavorite.value = favoriteRepository.getAllFavorite()
        }
    }
}