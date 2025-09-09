package com.example.android_sensors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_sensors.data.AccelerometerData
import com.example.android_sensors.data.repository.SensorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccelerometerViewModel @Inject constructor(
    private val sensorRepository: SensorRepository
) : ViewModel() {
    
    private val _accelerometerData = MutableStateFlow(AccelerometerData(0f, 0f, 0f, 0f))
    val accelerometerData: StateFlow<AccelerometerData> = _accelerometerData.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        startAccelerometerDataCollection()
    }
    
    private fun startAccelerometerDataCollection() {
        viewModelScope.launch {
            try {
                _isLoading.value = false
                sensorRepository.getAccelerometerData().collect { data ->
                    _accelerometerData.value = data
                }
            } catch (e: Exception) {
                _error.value = "Błąd podczas odczytu akcelerometru: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
