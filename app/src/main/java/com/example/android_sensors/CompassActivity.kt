package com.example.android_sensors

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.android_sensors.databinding.ActivityCompassBinding
import com.example.android_sensors.ui.viewmodel.CompassViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Activity that displays a real-time compass using magnetometer and accelerometer data.
 * Shows the device's orientation in degrees and cardinal directions.
 * The compass image rotates smoothly to indicate the current heading.
 * Data collection is lifecycle-aware and stops when the activity is not in the foreground.
 */
@AndroidEntryPoint
class CompassActivity : AppCompatActivity() {
    private val viewModel: CompassViewModel by viewModels()
    private lateinit var binding: ActivityCompassBinding
    private var currentDegree = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCompassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.compassLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        observeViewModel()
    }
    
    private fun observeViewModel() {
        // Collect compass data only when the lifecycle is at least STARTED
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.compassData.collect { compassData ->
                    updateCompassUI(compassData)
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
    
    private fun updateCompassUI(compassData: com.example.android_sensors.data.CompassData) {
        binding.rotationTextView.text = "${compassData.degrees}°"
        binding.directionTextView.text = compassData.direction
        
        val rotationAnimation = RotateAnimation(
            currentDegree, 
            (-compassData.degrees).toFloat(),
            Animation.RELATIVE_TO_SELF, 0.5f, 
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        
        rotationAnimation.duration = 210
        rotationAnimation.fillAfter = true
        
        binding.compassImageView.startAnimation(rotationAnimation)
        currentDegree = (-compassData.degrees).toFloat()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
} 