package com.example.android_sensors.data.repository

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SensorRepositoryTest {

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var sensorManager: SensorManager

    @Mock
    private lateinit var accelerometer: Sensor

    @Mock
    private lateinit var gyroscope: Sensor

    @Mock
    private lateinit var magnetometer: Sensor

    private lateinit var repository: SensorRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        whenever(context.getSystemService(Context.SENSOR_SERVICE)).thenReturn(sensorManager)
    }

    @Test
    fun `isAccelerometerAvailable returns true when sensor exists`() {
        // Given
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)).thenReturn(accelerometer)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)).thenReturn(null)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)).thenReturn(null)
        repository = SensorRepository(context)

        // When
        val result = repository.isAccelerometerAvailable()

        // Then
        assertTrue(result)
    }

    @Test
    fun `isAccelerometerAvailable returns false when sensor does not exist`() {
        // Given
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)).thenReturn(null)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)).thenReturn(null)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)).thenReturn(null)
        repository = SensorRepository(context)

        // When
        val result = repository.isAccelerometerAvailable()

        // Then
        assertFalse(result)
    }

    @Test
    fun `isGyroscopeAvailable returns true when sensor exists`() {
        // Given
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)).thenReturn(null)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)).thenReturn(gyroscope)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)).thenReturn(null)
        repository = SensorRepository(context)

        // When
        val result = repository.isGyroscopeAvailable()

        // Then
        assertTrue(result)
    }

    @Test
    fun `isGyroscopeAvailable returns false when sensor does not exist`() {
        // Given
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)).thenReturn(null)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)).thenReturn(null)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)).thenReturn(null)
        repository = SensorRepository(context)

        // When
        val result = repository.isGyroscopeAvailable()

        // Then
        assertFalse(result)
    }

    @Test
    fun `isMagnetometerAvailable returns true when sensor exists`() {
        // Given
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)).thenReturn(null)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)).thenReturn(null)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)).thenReturn(magnetometer)
        repository = SensorRepository(context)

        // When
        val result = repository.isMagnetometerAvailable()

        // Then
        assertTrue(result)
    }

    @Test
    fun `isMagnetometerAvailable returns false when sensor does not exist`() {
        // Given
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)).thenReturn(null)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)).thenReturn(null)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)).thenReturn(null)
        repository = SensorRepository(context)

        // When
        val result = repository.isMagnetometerAvailable()

        // Then
        assertFalse(result)
    }

    @Test
    fun `areSensorsAvailable returns true when all sensors exist`() {
        // Given
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)).thenReturn(accelerometer)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)).thenReturn(gyroscope)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)).thenReturn(magnetometer)
        repository = SensorRepository(context)

        // When
        val result = repository.areSensorsAvailable()

        // Then
        assertTrue(result)
    }

    @Test
    fun `areSensorsAvailable returns false when any sensor is missing`() {
        // Given
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)).thenReturn(accelerometer)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)).thenReturn(null)
        whenever(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)).thenReturn(magnetometer)
        repository = SensorRepository(context)

        // When
        val result = repository.areSensorsAvailable()

        // Then
        assertFalse(result)
    }
}
