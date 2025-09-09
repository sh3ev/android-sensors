package com.example.android_sensors

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        // Set up toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Set up card click listeners
        findViewById<MaterialCardView>(R.id.btnCompass).setOnClickListener {
            startActivity(Intent(this, CompassActivity::class.java))
        }
        
        findViewById<MaterialCardView>(R.id.btnAccelerometer).setOnClickListener {
            startActivity(Intent(this, AccelerometerActivity::class.java))
        }
        
        findViewById<MaterialCardView>(R.id.btnGyroscope).setOnClickListener {
            startActivity(Intent(this, GyroscopeActivity::class.java))
        }
    }
}