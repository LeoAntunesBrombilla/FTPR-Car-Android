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
}