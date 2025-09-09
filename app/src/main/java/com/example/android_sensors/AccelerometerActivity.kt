package com.example.android_sensors

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.android_sensors.ui.viewmodel.AccelerometerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccelerometerActivity : AppCompatActivity() {
    private val viewModel: AccelerometerViewModel by viewModels()
    private lateinit var xTextView: TextView
    private lateinit var yTextView: TextView
    private lateinit var zTextView: TextView
    private lateinit var magnitudeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_accelerometer)
        
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Akcelerometr"
        }
        
        // Inicjalizacja widoków
        xTextView = findViewById(R.id.xTextView)
        yTextView = findViewById(R.id.yTextView)
        zTextView = findViewById(R.id.zTextView)
        magnitudeTextView = findViewById(R.id.magnitudeTextView)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.accelerometer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        observeViewModel()
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.accelerometerData.collect { accelerometerData ->
                updateAccelerometerUI(accelerometerData)
            }
        }
        
        lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    // Można dodać Toast lub Snackbar do wyświetlania błędów
                }
            }
        }
    }
    
    private fun updateAccelerometerUI(accelerometerData: com.example.android_sensors.data.AccelerometerData) {
        xTextView.text = "X: ${String.format("%.2f", accelerometerData.x)} m/s²"
        yTextView.text = "Y: ${String.format("%.2f", accelerometerData.y)} m/s²"
        zTextView.text = "Z: ${String.format("%.2f", accelerometerData.z)} m/s²"
        magnitudeTextView.text = "Wielkość: ${String.format("%.2f", accelerometerData.magnitude)} m/s²"
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
} 