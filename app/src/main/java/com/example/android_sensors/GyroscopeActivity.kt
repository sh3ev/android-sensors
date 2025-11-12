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
import com.example.android_sensors.databinding.ActivityGyroscopeBinding
import com.example.android_sensors.ui.viewmodel.GyroscopeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GyroscopeActivity : AppCompatActivity() {
    private val viewModel: GyroscopeViewModel by viewModels()
    private lateinit var binding: ActivityGyroscopeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGyroscopeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.gyroscopeLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        observeViewModel()
    }
    
    private fun observeViewModel() {
        // Collect gyroscope data only when the lifecycle is at least STARTED
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.gyroscopeData.collect { gyroscopeData ->
                    updateGyroscopeUI(gyroscopeData)
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
    
    private fun updateGyroscopeUI(gyroscopeData: com.example.android_sensors.data.GyroscopeData) {
        binding.xTextView.text = "X: ${String.format("%.2f", gyroscopeData.x)} rad/s"
        binding.yTextView.text = "Y: ${String.format("%.2f", gyroscopeData.y)} rad/s"
        binding.zTextView.text = "Z: ${String.format("%.2f", gyroscopeData.z)} rad/s"
        binding.magnitudeTextView.text = "${String.format("%.2f", gyroscopeData.magnitude)} rad/s"
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
} 