package com.example.mbookie.di


import com.example.mbookie.data.repository.CustomerRepository
import com.example.mbookie.data.repository.CustomerRepositoryImpl
import com.example.mbookie.data.repository.MovieRepository
import com.example.mbookie.data.repository.MovieRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(
        database : FirebaseFirestore
    ) : MovieRepository{
        return MovieRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideCustomerRepository(
        database: FirebaseFirestore
    ) : CustomerRepository{
        return CustomerRepositoryImpl(database)
    }


}