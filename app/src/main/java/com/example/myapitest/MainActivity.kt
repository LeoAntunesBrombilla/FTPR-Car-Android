package com.example.myapitest

import HomeScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapitest.domain.model.AuthState
import com.example.myapitest.presentation.ui.auth.LoginScreen
import com.example.myapitest.presentation.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
}

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    // Google Sign In launcher
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            authViewModel.signInWithGoogle(account)
        } catch (e: ApiException) {
            Log.w("MainActivity", "Google sign in failed", e)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApiTestApp(
                authViewModel = authViewModel,
                onGoogleSignIn = { startGoogleSignIn() },
                activity = this
            )
        }
    }

    private fun startGoogleSignIn() {
        // Fixed: Handle missing default_web_client_id
        try {
            // Try to get the web client ID from resources
            val webClientId = "CHANGE_ME" // Replace with your actual web client ID

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)

        } catch (e: Exception) {
            Log.e("MainActivity", "Google Sign-In configuration error: ${e.message}")
            // You can show an error message to user or disable Google sign-in
            Log.e("MainActivity", "Make sure you have google-services.json configured properly")
        }
    }
}

@Composable
fun MyApiTestApp(
    authViewModel: AuthViewModel,
    onGoogleSignIn: () -> Unit,
    activity: MainActivity
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.observeAsState()

    // Check if user is already logged in
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = if (currentUser != null) Screen.Home.route else Screen.Login.route

    // Handle authentication state changes
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.navigate(Screen.Home.route) {
                    // Clear login screen from back stack
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }

            is AuthState.SignedOut -> {
                navController.navigate(Screen.Login.route) {
                    // Clear all screens from back stack
                    popUpTo(0) { inclusive = true }
                }
            }

            else -> { /* Handle other states */
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onGoogleSignIn = onGoogleSignIn,
                activity = activity // Remove the cast - just pass activity directly
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                authViewModel = authViewModel,
                onSignOut = {
                    authViewModel.signOut()
                }
            )
        }
    }
}