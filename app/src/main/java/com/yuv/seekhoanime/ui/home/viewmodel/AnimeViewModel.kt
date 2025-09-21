package com.yuv.seekhoanime.ui.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuv.seekhoanime.data.remote.models.AnimeApiModel
import com.yuv.seekhoanime.data.remote.models.AnimeDetailResponse
import com.yuv.seekhoanime.data.remote.repository.AnimeRepository
import kotlinx.coroutines.launch

class AnimeViewModel(private val repository: AnimeRepository) : ViewModel() {
    val animeList = repository.allAnime
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error
    private val _animeDetail = MutableLiveData<AnimeDetailResponse?>()
    val animeDetail: LiveData<AnimeDetailResponse?> get() = _animeDetail

    fun refreshAnime() {
        viewModelScope.launch {
            try {
                repository.fetchAndSaveTopAnime()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadAnimeDetails(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val detail = repository.getAnimeDetails(id)
                _animeDetail.value = detail
            } catch (e: Exception) {
                _error.value = "Unable to fetch details."
            } finally {
                _isLoading.value = false
            }
        }
    }
}