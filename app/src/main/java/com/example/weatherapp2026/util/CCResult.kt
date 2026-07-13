package com.example.weatherapp2026.util

sealed class CCResult<out T> {
    data object Loading : CCResult<Nothing>()
    data class Success<out T>(val m_data: T) : CCResult<T>()
    data class Error(val m_message: String, val m_cause: Throwable? = null) : CCResult<Nothing>()
}
