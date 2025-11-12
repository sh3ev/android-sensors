package com.example.android_sensors.di

import android.content.Context
import com.example.android_sensors.data.repository.SensorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing application-level dependencies.
 * Installed in SingletonComponent to ensure dependencies live for the entire app lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    /**
     * Provides a singleton instance of SensorRepository.
     * @param context Application context for accessing system services
     * @return Singleton SensorRepository instance
     */
    @Provides
    @Singleton
    fun provideSensorRepository(
        @ApplicationContext context: Context
    ): SensorRepository {
        return SensorRepository(context)
    }
}
