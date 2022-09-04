package com.connect.framework.di

import com.apollographql.apollo3.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkingModule {

  @Provides
  @Singleton
  fun provideApolloClient(): ApolloClient = ApolloClient.Builder()
    .serverUrl("https://app.tibber.com/v4/gql").build()
}