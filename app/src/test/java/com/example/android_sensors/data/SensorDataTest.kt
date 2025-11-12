package com.example.android_sensors.data

import org.junit.Test
import kotlin.test.assertEquals

class SensorDataTest {

    @Test
    fun `AccelerometerData is created correctly`() {
        // Given
        val x = 1.0f
        val y = 2.0f
        val z = 3.0f
        val magnitude = 3.74f

        // When
        val data = AccelerometerData(x, y, z, magnitude)

        // Then
        assertEquals(x, data.x)
        assertEquals(y, data.y)
        assertEquals(z, data.z)
        assertEquals(magnitude, data.magnitude)
    }

    @Test
    fun `GyroscopeData is created correctly`() {
        // Given
        val x = 0.5f
        val y = 1.5f
        val z = 2.5f
        val magnitude = 3.0f

        // When
        val data = GyroscopeData(x, y, z, magnitude)

        // Then
        assertEquals(x, data.x)
        assertEquals(y, data.y)
        assertEquals(z, data.z)
        assertEquals(magnitude, data.magnitude)
    }

    @Test
    fun `CompassData is created correctly`() {
        // Given
        val degrees = 45
        val direction = "NE"
        val azimuth = 45.0f

        // When
        val data = CompassData(degrees, direction, azimuth)

        // Then
        assertEquals(degrees, data.degrees)
        assertEquals(direction, data.direction)
        assertEquals(azimuth, data.azimuth)
    }

    @Test
    fun `AccelerometerData with zero values`() {
        // Given
        val data = AccelerometerData(0f, 0f, 0f, 0f)

        // Then
        assertEquals(0f, data.x)
        assertEquals(0f, data.y)
        assertEquals(0f, data.z)
        assertEquals(0f, data.magnitude)
    }

    @Test
    fun `CompassData with all cardinal directions`() {
        // Test North
        val north = CompassData(0, "N", 0f)
        assertEquals("N", north.direction)
        assertEquals(0, north.degrees)

        // Test East
        val east = CompassData(90, "E", 90f)
        assertEquals("E", east.direction)
        assertEquals(90, east.degrees)

        // Test South
        val south = CompassData(180, "S", 180f)
        assertEquals("S", south.direction)
        assertEquals(180, south.degrees)

        // Test West
        val west = CompassData(270, "W", 270f)
        assertEquals("W", west.direction)
        assertEquals(270, west.degrees)
    }

    @Test
    fun `data classes equality works correctly`() {
        // Given
        val data1 = AccelerometerData(1f, 2f, 3f, 3.74f)
        val data2 = AccelerometerData(1f, 2f, 3f, 3.74f)
        val data3 = AccelerometerData(2f, 3f, 4f, 5.38f)

        // Then
        assertEquals(data1, data2)
        assert(data1 != data3)
    }
}
