package com.example.myapitest.data.repository

import com.example.myapitest.data.api.ApiClient
import com.example.myapitest.domain.model.Car
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CarRepository {
    private val apiService = ApiClient.carApiService

    suspend fun getCars(): Result<List<Car>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCars()
                if (response.isSuccessful) {
                    Result.success(response.body() ?: emptyList())
                } else {
                    Result.failure(Exception("API Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}