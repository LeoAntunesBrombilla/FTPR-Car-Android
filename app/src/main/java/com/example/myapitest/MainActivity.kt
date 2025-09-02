package com.example.myapitest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapitest.ui.theme.MyApiTestTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApiTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }

        requestLocationPermission()
    }

    override fun onResume() {
        super.onResume()
        fetchItems()
    }

    private fun requestLocationPermission() {
        // TODO: Implement location permission request
    }

    private fun fetchItems() {
        // TODO: Implement API call to fetch items
    }
}

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Car Management App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Features to implement:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text("1. Firebase Login (Phone/Google)")
        Text("2. Logout option")
        Text("3. REST API integration for cars")
        Text("4. Google Maps integration (optional)")

        LaunchedEffect(Unit) {
            // This replaces setupView() - any initialization logic goes here
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyApiTestTheme {
        MainScreen()
    }
}