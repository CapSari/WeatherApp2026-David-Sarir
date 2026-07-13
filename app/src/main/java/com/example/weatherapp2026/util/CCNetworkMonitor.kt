package com.example.weatherapp2026.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CCNetworkMonitor @Inject constructor(
    @ApplicationContext private val m_context: Context
) {
    fun isConnected(): Boolean {
        val m_manager = m_context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val m_network = m_manager.activeNetwork ?: return false
        val m_caps = m_manager.getNetworkCapabilities(m_network) ?: return false
        return m_caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                m_caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
