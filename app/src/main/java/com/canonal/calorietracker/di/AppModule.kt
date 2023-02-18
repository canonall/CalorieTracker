package com.canonal.calorietracker.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.canonal.core.data.preferences.DefaultPreferences
import com.canonal.core.domain.preferences.Preferences
import com.canonal.core.domain.use_case.FilterOutDigitsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        application: Application
    ): SharedPreferences {
        return application.getSharedPreferences("shared_pref", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferences(sharedPreferences: SharedPreferences): Preferences {
        return DefaultPreferences(sharedPref = sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideFilterOutUseCase(): FilterOutDigitsUseCase {
        return FilterOutDigitsUseCase()
    }
}
