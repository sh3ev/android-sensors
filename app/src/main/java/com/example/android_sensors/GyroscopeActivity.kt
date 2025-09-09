package com.example.android_sensors

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.android_sensors.ui.viewmodel.GyroscopeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GyroscopeActivity : AppCompatActivity() {
    private val viewModel: GyroscopeViewModel by viewModels()
    private lateinit var xTextView: TextView
    private lateinit var yTextView: TextView
    private lateinit var zTextView: TextView
    private lateinit var magnitudeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gyroscope)
        
        // Set up toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Inicjalizacja widoków
        xTextView = findViewById(R.id.xTextView)
        yTextView = findViewById(R.id.yTextView)
        zTextView = findViewById(R.id.zTextView)
        magnitudeTextView = findViewById(R.id.magnitudeTextView)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.gyroscope_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        observeViewModel()
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.gyroscopeData.collect { gyroscopeData ->
                updateGyroscopeUI(gyroscopeData)
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
    
    private fun updateGyroscopeUI(gyroscopeData: com.example.android_sensors.data.GyroscopeData) {
        xTextView.text = "X: ${String.format("%.2f", gyroscopeData.x)} rad/s"
        yTextView.text = "Y: ${String.format("%.2f", gyroscopeData.y)} rad/s"
        zTextView.text = "Z: ${String.format("%.2f", gyroscopeData.z)} rad/s"
        magnitudeTextView.text = "Wielkość: ${String.format("%.2f", gyroscopeData.magnitude)} rad/s"
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
} 