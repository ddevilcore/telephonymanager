package com.ictech.bustracker.core.di

import com.ictech.bustracker.core.common.DefaultLocationTracker
import com.ictech.bustracker.core.common.LocationTracker
import com.ictech.bustracker.data.repository.TelephonyRepositoryImpl
import com.ictech.bustracker.domain.repository.TelephonyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTelephonyRepository(
        telephonyRepositoryImpl: TelephonyRepositoryImpl
    ): TelephonyRepository
//    @Binds
//    @Singleton
//    abstract fun bindLocationRepository(
//        locationRepositoryImpl: DefaultLocationTracker
//    ): LocationTracker
}