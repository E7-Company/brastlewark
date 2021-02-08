package com.axa.brastlewark.di

import android.content.Context
import com.axa.brastlewark.data.BrastlewarkRepository
import com.axa.brastlewark.data.local.AppDatabase
import com.axa.brastlewark.data.local.GnomeDao
import com.axa.brastlewark.data.remote.BrastlewarkAPI
import com.axa.brastlewark.data.remote.NetworkDataSource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/rrafols/mobile_test/master/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideGnomeService(retrofit: Retrofit): BrastlewarkAPI = retrofit.create(BrastlewarkAPI::class.java)

    @Singleton
    @Provides
    fun provideGnomeRemoteDataSource(gnomeService: BrastlewarkAPI) = NetworkDataSource(gnomeService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideGnomeDao(db: AppDatabase) = db.gnomeDao()

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: NetworkDataSource,
                          localDataSource: GnomeDao) =
        BrastlewarkRepository(remoteDataSource, localDataSource)
}