package com.example.weatherapp2026

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class CCHiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        m_classLoader: ClassLoader,
        m_className: String,
        m_context: Context
    ): Application = super.newApplication(m_classLoader, HiltTestApplication::class.java.name, m_context)
}
