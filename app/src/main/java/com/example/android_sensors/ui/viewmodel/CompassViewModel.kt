package com.example.android_sensors.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_sensors.data.CompassData
import com.example.android_sensors.data.repository.SensorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompassViewModel @Inject constructor(
    private val sensorRepository: SensorRepository
) : ViewModel() {
    
    private val _compassData = MutableStateFlow(CompassData(0, "N", 0f))
    val compassData: StateFlow<CompassData> = _compassData.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        startCompassDataCollection()
    }
    
    private fun startCompassDataCollection() {
        viewModelScope.launch {
            try {
                _isLoading.value = false
                sensorRepository.getCompassData().collect { data ->
                    _compassData.value = data
                }
            } catch (e: Exception) {
                _error.value = "Błąd podczas odczytu kompasu: ${e.message}"
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
