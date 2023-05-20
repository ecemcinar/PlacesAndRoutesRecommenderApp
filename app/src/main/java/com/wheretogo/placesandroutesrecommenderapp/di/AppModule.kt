package com.wheretogo.placesandroutesrecommenderapp.di

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.wheretogo.placesandroutesrecommenderapp.BuildConfig
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.repository.authentication.FirebaseAuthRepository
import com.wheretogo.placesandroutesrecommenderapp.repository.authentication.FirebaseAuthRepositoryImpl
import com.wheretogo.placesandroutesrecommenderapp.repository.firestore.FirebaseFirestoreRepository
import com.wheretogo.placesandroutesrecommenderapp.repository.firestore.FirebaseFirestoreRepositoryImpl
import com.wheretogo.placesandroutesrecommenderapp.repository.googlemaps.GoogleMapsService
import com.wheretogo.placesandroutesrecommenderapp.repository.googlemaps.MapsConstant.BASE_URL
import com.wheretogo.placesandroutesrecommenderapp.repository.placeservice.PlaceRepository
import com.wheretogo.placesandroutesrecommenderapp.repository.placeservice.PlaceService
import com.wheretogo.placesandroutesrecommenderapp.repository.placeservice.PlaceServiceImpl
import com.wheretogo.placesandroutesrecommenderapp.repository.storage.FirebaseStorageRepository
import com.wheretogo.placesandroutesrecommenderapp.repository.storage.FirebaseStorageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    @Provides
    @Singleton
    fun providePlacesClient(@ApplicationContext context: Context): PlacesClient {
        if (!Places.isInitialized()) {
            Places.initialize(context, context.getString(R.string.google_api_key))
        }
        return Places.createClient(context)
    }

    @Provides
    fun providePlaceService(placesClient: PlacesClient): PlaceService {
        return PlaceServiceImpl(placesClient)
    }

    @Provides
    fun providePlaceRepository(placeService: PlaceService): PlaceRepository {
        return PlaceRepository(placeService)
    }

    @Singleton
    @Provides
    fun provideBaseUrl() = BASE_URL

    @Singleton
    @Provides
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideOkHttpInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            if (!BuildConfig.DEBUG) {
                this.level = HttpLoggingInterceptor.Level.NONE
            } else {
                this.level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .callTimeout(60L, TimeUnit.SECONDS)
            .connectTimeout(61L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .writeTimeout(60L, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideGoogleMapsService(retrofit: Retrofit): GoogleMapsService {
        return retrofit.create(GoogleMapsService::class.java)
    }
}