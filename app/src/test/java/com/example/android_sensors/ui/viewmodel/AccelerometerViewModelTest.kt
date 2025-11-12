package com.example.android_sensors.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.android_sensors.data.AccelerometerData
import com.example.android_sensors.data.repository.SensorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AccelerometerViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var sensorRepository: SensorRepository

    private lateinit var viewModel: AccelerometerViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        // Given
        whenever(sensorRepository.isAccelerometerAvailable()).thenReturn(true)
        val testData = AccelerometerData(0f, 0f, 0f, 0f)
        whenever(sensorRepository.getAccelerometerData()).thenReturn(
            flow { emit(testData) }
        )

        // When
        viewModel = AccelerometerViewModel(sensorRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.accelerometerData.test {
            val item = awaitItem()
            assertEquals(0f, item.x)
            assertEquals(0f, item.y)
            assertEquals(0f, item.z)
            assertEquals(0f, item.magnitude)
        }
    }

    @Test
    fun `viewModel emits accelerometer data when sensor is available`() = runTest {
        // Given
        whenever(sensorRepository.isAccelerometerAvailable()).thenReturn(true)
        val testData = AccelerometerData(1.0f, 2.0f, 3.0f, 3.74f)
        whenever(sensorRepository.getAccelerometerData()).thenReturn(
            flow { emit(testData) }
        )

        // When
        viewModel = AccelerometerViewModel(sensorRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.accelerometerData.test {
            val item = awaitItem()
            assertEquals(1.0f, item.x)
            assertEquals(2.0f, item.y)
            assertEquals(3.0f, item.z)
            assertEquals(3.74f, item.magnitude)
        }
    }

    @Test
    fun `viewModel emits error when sensor is not available`() = runTest {
        // Given
        whenever(sensorRepository.isAccelerometerAvailable()).thenReturn(false)

        // When
        viewModel = AccelerometerViewModel(sensorRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.error.test {
            val error = awaitItem()
            assertNotNull(error)
            assertTrue(error.contains("not available"))
        }
    }

    @Test
    fun `viewModel handles sensor reading errors gracefully`() = runTest {
        // Given
        whenever(sensorRepository.isAccelerometerAvailable()).thenReturn(true)
        whenever(sensorRepository.getAccelerometerData()).thenReturn(
            flow { throw RuntimeException("Sensor error") }
        )

        // When
        viewModel = AccelerometerViewModel(sensorRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.error.test {
            val error = awaitItem()
            assertNotNull(error)
            assertTrue(error.contains("Error reading accelerometer"))
        }
    }

    @Test
    fun `clearError sets error to null`() = runTest {
        // Given
        whenever(sensorRepository.isAccelerometerAvailable()).thenReturn(false)
        viewModel = AccelerometerViewModel(sensorRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.clearError()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.error.test {
            val error = awaitItem()
            assertNull(error)
        }
    }

    @Test
    fun `isLoading is false after initialization`() = runTest {
        // Given
        whenever(sensorRepository.isAccelerometerAvailable()).thenReturn(true)
        val testData = AccelerometerData(0f, 0f, 0f, 0f)
        whenever(sensorRepository.getAccelerometerData()).thenReturn(
            flow { emit(testData) }
        )

        // When
        viewModel = AccelerometerViewModel(sensorRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.isLoading.test {
            val isLoading = awaitItem()
            assertFalse(isLoading)
        }
    }
}
