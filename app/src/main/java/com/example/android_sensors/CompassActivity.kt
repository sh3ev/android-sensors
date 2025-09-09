package com.example.android_sensors

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.android_sensors.ui.viewmodel.CompassViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompassActivity : AppCompatActivity() {
    private val viewModel: CompassViewModel by viewModels()
    private lateinit var compassImageView: ImageView
    private lateinit var rotationTextView: TextView
    private lateinit var directionTextView: TextView
    private var currentDegree = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_compass)
        
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Kompas"
        }
        
        compassImageView = findViewById(R.id.compassImageView)
        rotationTextView = findViewById(R.id.rotationTextView)
        directionTextView = findViewById(R.id.directionTextView)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.compass_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        observeViewModel()
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.compassData.collect { compassData ->
                updateCompassUI(compassData)
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
    
    private fun updateCompassUI(compassData: com.example.android_sensors.data.CompassData) {
        rotationTextView.text = "${compassData.degrees}°"
        directionTextView.text = compassData.direction
        
        val rotationAnimation = RotateAnimation(
            currentDegree, 
            (-compassData.degrees).toFloat(),
            Animation.RELATIVE_TO_SELF, 0.5f, 
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        
        rotationAnimation.duration = 210
        rotationAnimation.fillAfter = true
        
        compassImageView.startAnimation(rotationAnimation)
        currentDegree = (-compassData.degrees).toFloat()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
} 