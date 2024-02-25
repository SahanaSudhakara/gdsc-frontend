package com.gdsc.composesafespot.model

import android.app.Application
import com.gdsc.composesafespot.view.utils.CrimeStatusService
import com.gdsc.composesafespot.view.utils.CrimeStatusServiceImpl
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindCrimeStatusService(impl: CrimeStatusServiceImpl): CrimeStatusService

    companion object {
        @Provides
        @Singleton
        fun providePlacesClient(application: Application): PlacesClient {
            return Places.createClient(application)
        }
    }
}
