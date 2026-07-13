package com.example.weatherapp2026.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CCLocationHelper @Inject constructor(
    @ApplicationContext private val m_context: Context
) {
    private val m_fusedClient = LocationServices.getFusedLocationProviderClient(m_context)

    fun getCurrentLocation(): Flow<Location?> = callbackFlow {
        if (!hasLocationPermission()) {
            Timber.w("Location permission not granted — emitting null")
            trySend(null)
            close()
            return@callbackFlow
        }

        // Holds a reference so awaitClose can cancel it if the flow is cancelled (e.g. timeout)
        var m_pendingCallback: LocationCallback? = null

        m_fusedClient.lastLocation
            .addOnSuccessListener { m_location ->
                if (m_location != null) {
                    Timber.d("Last known location: ${m_location.latitude}, ${m_location.longitude}")
                    trySend(m_location)
                    close()
                } else {
                    Timber.d("No last location — requesting fresh fix")
                    val m_request = LocationRequest.Builder(
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY, 10_000L
                    )
                        .setWaitForAccurateLocation(false)
                        .setMinUpdateIntervalMillis(5_000L)
                        .setMaxUpdates(1)
                        .build()

                    val m_callback = object : LocationCallback() {
                        override fun onLocationResult(m_result: LocationResult) {
                            Timber.d("Fresh location: ${m_result.lastLocation?.latitude}")
                            m_fusedClient.removeLocationUpdates(this)
                            m_pendingCallback = null
                            trySend(m_result.lastLocation)
                            close()
                        }
                    }
                    m_pendingCallback = m_callback

                    try {
                        m_fusedClient.requestLocationUpdates(
                            m_request, m_callback, Looper.getMainLooper()
                        )
                    } catch (m_e: SecurityException) {
                        Timber.e(m_e, "Security exception requesting location")
                        trySend(null)
                        close()
                    }
                }
            }
            .addOnFailureListener { m_e ->
                Timber.e(m_e, "Failed to get last location")
                trySend(null)
                close()
            }

        // Called when the collector cancels (e.g. withTimeoutOrNull fires)
        awaitClose {
            m_pendingCallback?.let {
                Timber.d("Location flow cancelled — removing location updates")
                m_fusedClient.removeLocationUpdates(it)
            }
        }
    }

    fun hasLocationPermission(): Boolean {
        val m_fine = ContextCompat.checkSelfPermission(
            m_context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val m_coarse = ContextCompat.checkSelfPermission(
            m_context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return m_fine || m_coarse
    }
}
