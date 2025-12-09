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
    // 생성자 + 기본값 + 의존성 주입
) : ViewModel() {

    private val _uiState = MutableStateFlow(CourtDetailUiState(isLoading = true))
    // 외부에는 읽기만 허용하고, 내부만 변경 가능하게 만드는 관례
    // 변경 가능한 Flow -> `MutableStateFlow`
    // 초기 상태 `로딩중` 기본값

    val uiState : StateFlow<CourtDetailUiState> = _uiState.asStateFlow()
    // MutableStateFlow를 StateFlow로 변환
    // 수정 불가 -> `StateFlow`

    fun loadCourt(courtId : Long) {
        viewModelScope.launch {
            // 비동기 처리 위한 코루틴 시작

            _uiState.value = CourtDetailUiState(isLoading = true)
            // isLoading = true가 됨

            try {
                val court = repository.getCourt(courtId = courtId)
                // id로 repository 호출

                _uiState.value = CourtDetailUiState(
                    isLoading = false,
                    courtName = court.name,
                    address = court.address,
                    thumbnail = court.thumbnail,
                    latitude = court.latitude,
                    longitude = court.longitude,
                    errorMessage = null
                )
                // 성공하면, uiState에 값 채우고, isLoading = false
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