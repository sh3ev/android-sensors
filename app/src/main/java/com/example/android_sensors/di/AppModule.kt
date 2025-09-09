package com.example.android_sensors.di

import android.content.Context
import com.example.android_sensors.data.repository.SensorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideSensorRepository(
        @ApplicationContext context: Context
    ): SensorRepository {
        return SensorRepository(context)
    }
}
