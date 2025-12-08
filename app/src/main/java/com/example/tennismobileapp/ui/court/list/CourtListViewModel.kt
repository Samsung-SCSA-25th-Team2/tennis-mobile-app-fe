package com.example.tennismobileapp.ui.court.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tennismobileapp.core.di.ServiceLocator
import com.example.tennismobileapp.domain.repository.TennisCourtRepository
import com.example.tennismobileapp.ui.court.component.CourtItemUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CourtListViewModel(
    private val repository: TennisCourtRepository = ServiceLocator.tennisCourtRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CourtListUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    init {
        loadCourts()
    }

    private fun loadCourts() {
        // 코루틴 실행
        viewModelScope.launch {
            _uiState.value = CourtListUiState(isLoading = true)

            try {
                val courts = repository.getAllCourts()

                val uiModels = courts.map { court ->
                    CourtItemUiModel(
                        courtId = court.courtId,
                        thumbnail = court.thumbnail,
                        name = court.name,
                        address = court.address
                    )
                }

                _uiState.value = CourtListUiState(
                    isLoading = false,
                    courtList = uiModels,
                    errorMessage = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = CourtListUiState(
                    isLoading = false,
                    errorMessage = "테니스장 목록 조회 실패. \n ${e.message}"
                )
            }
        }
    }
}
