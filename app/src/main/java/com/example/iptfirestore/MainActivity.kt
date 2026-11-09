package com.example.iptfirestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.iptfirestore.ui.AuthUiState
import com.example.iptfirestore.ui.AuthViewModel
import com.example.iptfirestore.ui.ScreenState
import com.example.iptfirestore.ui.screens.HomeScreen
import com.example.iptfirestore.ui.screens.LoginScreen
import com.example.iptfirestore.ui.screens.RegisterScreen
import com.example.iptfirestore.ui.theme.IPTfirestoreTheme

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IPTfirestoreTheme {
                val state by authViewModel.uiState.collectAsState()

                MainAppScreen(
                    state = state,
                    viewModel = authViewModel
                )
            }
        }
    }
}

@Composable
fun MainAppScreen(
    state: AuthUiState,
    viewModel: AuthViewModel
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (state.currentScreen) {
            ScreenState.LOGIN -> {
                LoginScreen(
                    state = state,
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
                    onLoginClick = viewModel::onLoginClicked,
                    onNavigateToRegister = { viewModel.navigateTo(ScreenState.REGISTER) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            ScreenState.REGISTER -> {
                RegisterScreen(
                    state = state,
                    onNameChange = viewModel::onNameChange,
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                    onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
                    onRegisterClick = viewModel::onRegisterClicked,
                    onNavigateToLogin = { viewModel.navigateTo(ScreenState.LOGIN) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            ScreenState.HOME -> {
                HomeScreen(
                    state = state,
                    onLogoutClick = viewModel::onLogoutClicked,
                    onNoteTitleChange = viewModel::onNoteTitleChange,
                    onNoteContentChange = viewModel::onNoteContentChange,
                    onAddNoteClick = viewModel::onAddNoteClicked,
                    onDeleteNoteClick = viewModel::onDeleteNoteClicked,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
