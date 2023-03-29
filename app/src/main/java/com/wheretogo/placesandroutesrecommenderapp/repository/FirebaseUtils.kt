package com.wheretogo.placesandroutesrecommenderapp.repository

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Task<T>.await(): T {
    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            it.exception?.let { exception ->
                cont.resumeWithException(exception)
            } ?: kotlin.run {
                cont.resume(it.result, null)
            }
        }
    }
}