package com.example.android_sensors

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.android_sensors.databinding.ActivityAccelerometerBinding
import com.example.android_sensors.ui.viewmodel.AccelerometerViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccelerometerActivity : AppCompatActivity() {
    private val viewModel: AccelerometerViewModel by viewModels()
    private lateinit var binding: ActivityAccelerometerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccelerometerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.accelerometerLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        observeViewModel()
    }
    
    private fun observeViewModel() {
        // Collect accelerometer data only when the lifecycle is at least STARTED
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.accelerometerData.collect { accelerometerData ->
                    updateAccelerometerUI(accelerometerData)
                }
            }
        }
        
        // Collect errors throughout the activity lifecycle
        lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Snackbar.make(
                        binding.root,
                        it,
                        Snackbar.LENGTH_LONG
                    ).show()
                    viewModel.clearError()
                }
            }
        }
    }
    
    private fun updateAccelerometerUI(accelerometerData: com.example.android_sensors.data.AccelerometerData) {
        binding.xTextView.text = "X: ${String.format("%.2f", accelerometerData.x)} m/s²"
        binding.yTextView.text = "Y: ${String.format("%.2f", accelerometerData.y)} m/s²"
        binding.zTextView.text = "Z: ${String.format("%.2f", accelerometerData.z)} m/s²"
        binding.magnitudeTextView.text = "${String.format("%.2f", accelerometerData.magnitude)} m/s²"
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
} 