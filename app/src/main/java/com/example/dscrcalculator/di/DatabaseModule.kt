package com.example.dscrcalculator.di

import android.content.Context
import androidx.room.Room
import com.example.dscrcalculator.data.local.AppDatabase
import com.example.dscrcalculator.data.local.CalculationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "dscr_database"
        ).build()

    @Provides
    fun provideCalculationDao(database: AppDatabase): CalculationDao = database.calculationDao()
}
