package com.example.myapitest.presentation.ui.addcar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapitest.domain.model.Car
import com.example.myapitest.domain.model.Place
import com.example.myapitest.presentation.ui.components.LocationPickerMap
import com.example.myapitest.presentation.viewmodel.CarViewModel
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarScreen(
    onBackClick: () -> Unit,
    onCarAdded: () -> Unit,
    carViewModel: CarViewModel = viewModel()
) {
    var carId by remember { mutableStateOf("") }
    var carName by remember { mutableStateOf("") }
    var carYear by remember { mutableStateOf("") }
    var carLicence by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    val loading by carViewModel.loading.observeAsState(false)
    val error by carViewModel.error.observeAsState()
    val car by carViewModel.car.observeAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(car) {
        car?.let {
            onCarAdded()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Carro") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = carId,
                onValueChange = { carId = it },
                label = { Text("ID do Carro") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            )

            OutlinedTextField(
                value = carName,
                onValueChange = { carName = it },
                label = { Text("Nome do Carro") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            )

            OutlinedTextField(
                value = carYear,
                onValueChange = { carYear = it },
                label = { Text("Ano (ex: 2020/2021)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            )

            OutlinedTextField(
                value = carLicence,
                onValueChange = { carLicence = it },
                label = { Text("Placa (ex: ABC-1234)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            )

            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL da Imagem") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            )

            LocationPickerMap(
                selectedLocation = selectedLocation,
                onLocationSelected = { latLng ->
                    selectedLocation = latLng
                }
            )

            error?.let {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Erro: $it",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Button(
                onClick = {
                    if (carId.isNotBlank() && carName.isNotBlank() && carYear.isNotBlank() &&
                        carLicence.isNotBlank() && imageUrl.isNotBlank()
                        && selectedLocation != null
                    ) {

                        val newCar = Car(
                            id = carId,
                            name = carName,
                            year = carYear,
                            licence = carLicence,
                            imageUrl = imageUrl,
                            place = Place(
                                lat = selectedLocation!!.latitude,
                                long = selectedLocation!!.longitude
                            )
                        )
                        carViewModel.addCar(newCar)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading && carId.isNotBlank() && carName.isNotBlank() &&
                        carYear.isNotBlank() && carLicence.isNotBlank() &&
                        imageUrl.isNotBlank() && selectedLocation != null
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Adicionar Carro")
                }
            }
        }
    }
}