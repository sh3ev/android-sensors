package com.example.android_sensors.data

/**
 * Data class representing accelerometer sensor data
 */
data class AccelerometerData(
    val x: Float,
    val y: Float,
    val z: Float,
    val magnitude: Float
)

/**
 * Data class representing gyroscope sensor data
 */
data class GyroscopeData(
    val x: Float,
    val y: Float,
    val z: Float,
    val magnitude: Float
)

/**
 * Data class representing compass sensor data
 */
data class CompassData(
    val degrees: Int,
    val direction: String,
    val azimuth: Float
)
