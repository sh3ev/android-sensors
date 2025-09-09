package com.example.android_sensors.data.repository

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.android_sensors.data.AccelerometerData
import com.example.android_sensors.data.CompassData
import com.example.android_sensors.data.GyroscopeData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.sqrt
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SensorRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    private val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    
    /**
     * Zwraca Flow z danymi akcelerometru
     */
    fun getAccelerometerData(): Flow<AccelerometerData> = callbackFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    val magnitude = sqrt(x * x + y * y + z * z)
                    
                    trySend(AccelerometerData(x, y, z, magnitude))
                }
            }
            
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        
        accelerometer?.let { sensor ->
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        }
        
        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }.distinctUntilChanged()
    
    /**
     * Zwraca Flow z danymi żyroskopu
     */
    fun getGyroscopeData(): Flow<GyroscopeData> = callbackFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    val magnitude = sqrt(x * x + y * y + z * z)
                    
                    trySend(GyroscopeData(x, y, z, magnitude))
                }
            }
            
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        
        gyroscope?.let { sensor ->
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        }
        
        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }.distinctUntilChanged()
    
    /**
     * Zwraca Flow z danymi kompasu
     */
    fun getCompassData(): Flow<CompassData> = callbackFlow {
        val lastAccelerometer = FloatArray(3)
        val lastMagnetometer = FloatArray(3)
        var lastAccelerometerSet = false
        var lastMagnetometerSet = false
        val rotationMatrix = FloatArray(9)
        val orientation = FloatArray(3)
        
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let { sensorEvent ->
                    when (sensorEvent.sensor.type) {
                        Sensor.TYPE_ACCELEROMETER -> {
                            System.arraycopy(sensorEvent.values, 0, lastAccelerometer, 0, sensorEvent.values.size)
                            lastAccelerometerSet = true
                        }
                        Sensor.TYPE_MAGNETIC_FIELD -> {
                            System.arraycopy(sensorEvent.values, 0, lastMagnetometer, 0, sensorEvent.values.size)
                            lastMagnetometerSet = true
                        }
                    }
                    
                    if (lastAccelerometerSet && lastMagnetometerSet) {
                        SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetometer)
                        SensorManager.getOrientation(rotationMatrix, orientation)
                        
                        val azimuthInRadians = orientation[0]
                        val azimuthInDegrees = Math.toDegrees(azimuthInRadians.toDouble()).toFloat()
                        val degree = ((azimuthInDegrees + 360) % 360).toInt()
                        
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
                        
                        trySend(CompassData(degree, direction, azimuthInDegrees))
                    }
                }
            }
            
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        
        accelerometer?.let { sensor ->
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
        magnetometer?.let { sensor ->
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
        
        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }.distinctUntilChanged()
    
    /**
     * Sprawdza czy sensory są dostępne
     */
    fun areSensorsAvailable(): Boolean {
        return accelerometer != null && gyroscope != null && magnetometer != null
    }
}
