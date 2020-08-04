package com.codechallenge.revolutcodechallenge.di

import com.codechallenge.revolutcodechallenge.repository.Repository
import com.codechallenge.revolutcodechallenge.repository.RepositoryImpl
import com.codechallenge.revolutcodechallenge.repository.RepositoryService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun getUrl() = "https://hiring.revolut.codes"

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, url: String) = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideRepositoryService(retrofit: Retrofit) = retrofit.create(RepositoryService::class.java)

    @Provides
    @Singleton
    fun provideRepository(repository: RepositoryImpl): Repository = repository
}