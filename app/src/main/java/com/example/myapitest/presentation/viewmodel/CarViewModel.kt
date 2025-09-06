package com.example.myapitest.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapitest.data.repository.CarRepository
import com.example.myapitest.domain.model.Car
import kotlinx.coroutines.launch

class CarViewModel : ViewModel() {
    private val repository = CarRepository()

    private val _cars = MutableLiveData<List<Car>>()
    val cars: LiveData<List<Car>> = _cars

    private val _car = MutableLiveData<Car>()
    val car: LiveData<Car> = _car

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadCars() {
        viewModelScope.launch {
            _loading.value = true
            repository.getCars()
                .onSuccess { carList ->
                    _cars.value = carList
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _loading.value = false
        }
    }

    fun addCar(car: Car) {
        viewModelScope.launch {
            _loading.value = true
            repository.addCar(car)
                .onSuccess { car ->
                    _car.value = car
                    _error.value = null
                    loadCars()
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _loading.value = false
        }
    }

    fun deleteCar(id: String) {
        viewModelScope.launch {
            _loading.value = true
            repository.deleteCar(id)
                .onSuccess { car ->
                    _error.value = null
                    loadCars()
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _loading.value = false
        }
    }

    fun updateCar(id: String, car: Car) {
        viewModelScope.launch {
            _loading.value = true
            repository.updateCar(id, car)
                .onSuccess { updatedCar ->
                    _car.value = updatedCar
                    _error.value = null
                    loadCars()
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _loading.value = false
        }
    }
}