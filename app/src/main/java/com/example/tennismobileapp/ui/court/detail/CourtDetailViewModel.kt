package com.example.tennismobileapp.ui.court.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tennismobileapp.core.di.ServiceLocator
import com.example.tennismobileapp.domain.repository.TennisCourtRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourtDetailViewModel(
    private val repository: TennisCourtRepository = ServiceLocator.tennisCourtRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CourtDetailUiState(isLoading = true))
    val uiState : StateFlow<CourtDetailUiState> = _uiState.asStateFlow()

    fun loadCourt(courtId : Long) {
        viewModelScope.launch {
            _uiState.value = CourtDetailUiState(isLoading = true)

            try {
                val court = repository.getCourt(courtId = courtId)

                _uiState.value = CourtDetailUiState(
                    isLoading = false,
                    courtName = court.name,
                    address = court.address,
                    thumbnail = court.thumbnail,
                    latitude = court.latitude,
                    longitude = court.longitude,
                    errorMessage = null
                )
            } catch (e : Exception) {
                e.printStackTrace()
                _uiState.value = CourtDetailUiState(
                    isLoading = false,
                    errorMessage = "테니스장 세부 정보를 불러오지 못했습니다. \n ${e.message}"
                )
            }
        }
    }
}