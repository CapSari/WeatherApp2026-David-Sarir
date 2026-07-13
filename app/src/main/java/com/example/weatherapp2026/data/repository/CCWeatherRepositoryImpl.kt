package com.example.weatherapp2026.data.repository

import com.example.weatherapp2026.data.local.dao.CCWeatherDao
import com.example.weatherapp2026.data.mapper.CCWeatherMapper
import com.example.weatherapp2026.data.remote.CCWeatherApiService
import com.example.weatherapp2026.domain.model.CCCurrentWeather
import com.example.weatherapp2026.domain.model.CCForecastDay
import com.example.weatherapp2026.domain.repository.CCWeatherRepository
import com.example.weatherapp2026.util.CCConstants
import com.example.weatherapp2026.util.CCNetworkMonitor
import com.example.weatherapp2026.util.CCResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CCWeatherRepositoryImpl @Inject constructor(
    private val m_apiService: CCWeatherApiService,
    private val m_weatherDao: CCWeatherDao,
    private val m_mapper: CCWeatherMapper,
    private val m_networkMonitor: CCNetworkMonitor
) : CCWeatherRepository {

    override fun getCurrentWeather(m_lat: Double, m_lon: Double): Flow<CCResult<CCCurrentWeather>> = flow {
        emit(CCResult.Loading)

        val m_cached = m_weatherDao.getCurrentWeather()
        if (m_cached != null) {
            Timber.d("Emitting cached current weather for ${m_cached.m_cityName}")
            emit(CCResult.Success(m_mapper.mapEntityToCurrentWeather(m_cached)))
        }

        if (m_networkMonitor.isConnected()) {
            try {
                val m_response = m_apiService.getCurrentWeather(
                    m_lat = m_lat,
                    m_lon = m_lon,
                    m_apiKey = CCConstants.API_KEY,
                    m_units = CCConstants.UNITS_METRIC
                )
                val m_entity = m_mapper.mapResponseToEntity(m_response)
                m_weatherDao.upsertCurrentWeather(m_entity)
                Timber.d("Emitting fresh current weather for ${m_entity.m_cityName}")
                emit(CCResult.Success(m_mapper.mapEntityToCurrentWeather(m_entity)))
            } catch (m_e: IOException) {
                Timber.e(m_e, "Network error fetching current weather")
                if (m_cached == null) emit(CCResult.Error("Network error. Please check your connection.", m_e))
            } catch (m_e: HttpException) {
                Timber.e(m_e, "API error fetching current weather: ${m_e.code()}")
                if (m_cached == null) emit(CCResult.Error("API error (${m_e.code()}). Please try again.", m_e))
            }
        } else if (m_cached == null) {
            emit(CCResult.Error("No internet connection and no cached data available."))
        }
    }

    override fun getFiveDayForecast(m_lat: Double, m_lon: Double): Flow<CCResult<List<CCForecastDay>>> = flow {
        emit(CCResult.Loading)

        val m_cachedEntities = m_weatherDao.getForecast()
        if (m_cachedEntities.isNotEmpty()) {
            Timber.d("Emitting ${m_cachedEntities.size} cached forecast days")
            emit(CCResult.Success(m_cachedEntities.map { m_mapper.mapEntityToForecastDay(it) }))
        }

        if (m_networkMonitor.isConnected()) {
            try {
                val m_response = m_apiService.getFiveDayForecast(
                    m_lat = m_lat,
                    m_lon = m_lon,
                    m_apiKey = CCConstants.API_KEY,
                    m_units = CCConstants.UNITS_METRIC
                )
                val m_entities = m_mapper.mapForecastResponseToEntities(m_response)
                m_weatherDao.clearForecast()
                m_weatherDao.upsertForecast(m_entities)
                Timber.d("Emitting ${m_entities.size} fresh forecast days")
                emit(CCResult.Success(m_entities.map { m_mapper.mapEntityToForecastDay(it) }))
            } catch (m_e: IOException) {
                Timber.e(m_e, "Network error fetching forecast")
                if (m_cachedEntities.isEmpty()) emit(CCResult.Error("Network error. Please check your connection.", m_e))
            } catch (m_e: HttpException) {
                Timber.e(m_e, "API error fetching forecast: ${m_e.code()}")
                if (m_cachedEntities.isEmpty()) emit(CCResult.Error("API error (${m_e.code()}). Please try again.", m_e))
            }
        } else if (m_cachedEntities.isEmpty()) {
            emit(CCResult.Error("No internet connection and no cached data available."))
        }
    }
}
