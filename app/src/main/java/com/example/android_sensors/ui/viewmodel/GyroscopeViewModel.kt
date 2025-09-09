package com.example.android_sensors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_sensors.data.GyroscopeData
import com.example.android_sensors.data.repository.SensorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GyroscopeViewModel @Inject constructor(
    private val sensorRepository: SensorRepository
) : ViewModel() {
    
    private val _gyroscopeData = MutableStateFlow(GyroscopeData(0f, 0f, 0f, 0f))
    val gyroscopeData: StateFlow<GyroscopeData> = _gyroscopeData.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        startGyroscopeDataCollection()
    }
    
    private fun startGyroscopeDataCollection() {
        viewModelScope.launch {
            try {
                _isLoading.value = false
                sensorRepository.getGyroscopeData().collect { data ->
                    _gyroscopeData.value = data
                }
            } catch (e: Exception) {
                _error.value = "Błąd podczas odczytu żyroskopu: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
