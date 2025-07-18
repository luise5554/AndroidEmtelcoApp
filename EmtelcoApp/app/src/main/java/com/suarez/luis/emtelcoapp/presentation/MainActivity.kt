package com.suarez.luis.emtelcoapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.suarez.luis.emtelcoapp.infrastructure.network.NetworkMonitor
import com.suarez.luis.emtelcoapp.infrastructure.network.NetworkUtils
import com.suarez.luis.emtelcoapp.presentation.theme.EmtelcoAppTheme
import com.suarez.luis.emtelcoapp.presentation.view.PokemonListScreen
import com.suarez.luis.emtelcoapp.presentation.view.ShoppingCartScreen
import com.suarez.luis.emtelcoapp.presentation.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()

        val networkUtils = NetworkUtils()
        networkUtils.createNetworkChannel(this)
        val networkMonitor = NetworkMonitor(this)

        lifecycleScope.launchWhenStarted {
            networkMonitor.isConnected.collect { connected ->
                networkUtils.showNetworkNotification(this@MainActivity, connected)
            }
        }

        enableEdgeToEdge()
        setContent {
            EmtelcoAppTheme {
                MainScreen()
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                }

                shouldShowRequestPermissionRationale(
                    Manifest.permission.POST_NOTIFICATIONS
                ) -> {
                    requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }

                else -> {
                    requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    fun authenticateBeforeCart(onSuccess: () -> Unit, onFail: () -> Unit) {
        val executor = ContextCompat.getMainExecutor(this)

        val biometricManager = BiometricManager.from(this)
        val canAuthenticate = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )

        if (canAuthenticate == BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE) {
            onFail()
            return
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación requerida")
            .setSubtitle("Confirma tu identidad para ver el carrito")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onFail()
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }

}

@Composable
fun MainScreen(viewModel: PokemonViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf("list") }
    val context = LocalContext.current

    BackHandler(enabled = currentScreen == "cart") {
        currentScreen = "list"
    }

    when (currentScreen) {
        "list" -> PokemonListScreen(
            viewModel = viewModel,
            onGoToCart = {
                val activity = context as? MainActivity
                activity?.authenticateBeforeCart(
                    onSuccess = {
                        currentScreen = "cart"
                    },
                    onFail = {
                        Toast.makeText(context, "Autenticación cancelada", Toast.LENGTH_SHORT).show()
                    }
                )
                 }
        )
        "cart" -> ShoppingCartScreen(
            viewModel = viewModel,
            onBack = { currentScreen = "list" }
        )
    }
}