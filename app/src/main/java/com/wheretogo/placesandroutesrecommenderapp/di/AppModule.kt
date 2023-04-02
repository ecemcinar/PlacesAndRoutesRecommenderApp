package com.wheretogo.placesandroutesrecommenderapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.wheretogo.placesandroutesrecommenderapp.repository.authentication.FirebaseAuthRepository
import com.wheretogo.placesandroutesrecommenderapp.repository.authentication.FirebaseAuthRepositoryImpl
import com.wheretogo.placesandroutesrecommenderapp.repository.firestore.FirebaseFirestoreRepository
import com.wheretogo.placesandroutesrecommenderapp.repository.firestore.FirebaseFirestoreRepositoryImpl
import com.wheretogo.placesandroutesrecommenderapp.repository.storage.FirebaseStorageRepository
import com.wheretogo.placesandroutesrecommenderapp.repository.storage.FirebaseStorageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseAuthRepository(impl: FirebaseAuthRepositoryImpl): FirebaseAuthRepository = impl

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseFirestoreRepository(impl: FirebaseFirestoreRepositoryImpl): FirebaseFirestoreRepository = impl

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun provideFirebaseStorageRepository(impl: FirebaseStorageRepositoryImpl): FirebaseStorageRepository = impl
}