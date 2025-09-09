package com.example.android_sensors.data

/**
 * Data class reprezentujący dane z akcelerometru
 */
data class AccelerometerData(
    val x: Float,
    val y: Float,
    val z: Float,
    val magnitude: Float
)

/**
 * Data class reprezentujący dane z żyroskopu
 */
data class GyroscopeData(
    val x: Float,
    val y: Float,
    val z: Float,
    val magnitude: Float
)

/**
 * Data class reprezentujący dane z kompasu
 */
data class CompassData(
    val degrees: Int,
    val direction: String,
    val azimuth: Float
)
