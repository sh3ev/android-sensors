package com.example.android_sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CompassActivity : AppCompatActivity(), SensorEventListener {
    private var sensor: Sensor? = null
    private var sensorManager: SensorManager? = null
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
        
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        
        compassImageView = findViewById(R.id.compassImageView)
        rotationTextView = findViewById(R.id.rotationTextView)
        directionTextView = findViewById(R.id.directionTextView)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.compass_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    
    override fun onSensorChanged(event: SensorEvent?) {
        val degree = Math.round(event!!.values[0])
        
        rotationTextView.text = "$degree°"
        
        updateDirectionText(degree)
        
        val rotationAnimation = RotateAnimation(
            currentDegree, 
            (-degree).toFloat(),
            Animation.RELATIVE_TO_SELF, 0.5f, 
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        
        rotationAnimation.duration = 210
        rotationAnimation.fillAfter = true
        
        compassImageView.startAnimation(rotationAnimation)
        
        currentDegree = (-degree).toFloat()
    }
    
    private fun updateDirectionText(degree: Int) {
        val direction = when {
            degree >= 337.5 || degree < 22.5 -> "N"
            degree >= 22.5 && degree < 67.5 -> "NE"
            degree >= 67.5 && degree < 112.5 -> "E"
            degree >= 112.5 && degree < 157.5 -> "SE"
            degree >= 157.5 && degree < 202.5 -> "S"
            degree >= 202.5 && degree < 247.5 -> "SW"
            degree >= 247.5 && degree < 292.5 -> "W"
            else -> "NW"
        }
        directionTextView.text = direction
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
    
    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
    }
    
    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
} 