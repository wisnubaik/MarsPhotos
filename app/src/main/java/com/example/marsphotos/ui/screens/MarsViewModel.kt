package com.example.marsphotos.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.marsphotos.MarsPhotosApplication
import com.example.marsphotos.data.MarsPhotosRepository
import com.example.marsphotos.model.MarsPhoto
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface MarsUiState {
    data class Success(val photos: List<MarsPhoto>) : MarsUiState
    object Error : MarsUiState
    object Loading : MarsUiState
}

class MarsViewModel(private val marsPhotosRepository: MarsPhotosRepository) : ViewModel() {

    /** The mutable State that stores the status of the most recent request */
    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Loading)
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        // Memanggil getMarsPhotos() ketika ViewModel diinisialisasi
        getMarsPhotos()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List].
     */
    fun getMarsPhotos() {
        // Set loading state before making network request
        marsUiState = MarsUiState.Loading

        viewModelScope.launch {
            try {
                // Mendapatkan data MarsPhoto dari repository
                val photos = marsPhotosRepository.getMarsPhotos()

                // Jika berhasil, update UI state dengan daftar foto yang diterima
                MarsUiState.Success(marsPhotosRepository.getMarsPhotos())

                // Contoh untuk mendapatkan URL gambar pertama dan melakukan sesuatu dengannya
                val firstPhoto = photos.firstOrNull()
                firstPhoto?.let {
                    val firstPhotoUrl = it.imgSrc
                    // Lakukan sesuatu dengan URL ini, misalnya log atau tampilkan di UI
                }

            } catch (e: IOException) {
                // Handle network or I/O error
                marsUiState = MarsUiState.Error
            } catch (e: HttpException) {
                // Handle HTTP error
                marsUiState = MarsUiState.Error
            }
        }
    }

    /**
     * Factory for [MarsViewModel] that takes [MarsPhotosRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MarsPhotosApplication)
                val marsPhotosRepository = application.container.marsPhotosRepository
                MarsViewModel(marsPhotosRepository = marsPhotosRepository)
            }
        }
    }
}
