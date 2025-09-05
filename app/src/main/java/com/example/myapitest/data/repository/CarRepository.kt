package com.example.myapitest.data.repository

import com.example.myapitest.data.api.ApiClient
import com.example.myapitest.domain.model.Car
import com.example.myapitest.domain.model.CarResponse
import com.example.myapitest.domain.model.DeleteResponse
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

    suspend fun getCarById(id: String): Result<CarResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getCarById(id)
                if (response.isSuccessful) {
                    Result.success(response.body() ?: throw Exception("Nao encontramos esse carro"))
                } else {
                    Result.failure(Exception("API Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deleteCar(id: String): Result<DeleteResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteCar(id)
                if (response.isSuccessful) {
                    Result.success(response.body() ?: DeleteResponse("Carro deletado com sucesso!"))
                } else {
                    Result.failure(Exception("API Error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}