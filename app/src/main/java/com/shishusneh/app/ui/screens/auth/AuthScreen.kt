package com.shishusneh.app.ui.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shishusneh.app.ui.components.AppTextField
import com.shishusneh.app.ui.theme.OrangeAccent
import com.shishusneh.app.ui.theme.TealPrimary
import com.shishusneh.app.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var loginMode by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(TealPrimary, OrangeAccent)))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Shishu-Sneh",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = "Baby's first-year guide for growth, feeding, milestones, and vaccines.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        Card(
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (loginMode) "Welcome back" else "Create your family account",
                    style = MaterialTheme.typography.titleLarge
                )
                AnimatedContent(targetState = loginMode, label = "auth_mode") { isLogin ->
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (!isLogin) {
                            AppTextField(value = name, onValueChange = { name = it }, label = "Full name")
                        }
                        AppTextField(value = email, onValueChange = { email = it }, label = "Email")
                        AppTextField(value = password, onValueChange = { password = it }, label = "Password")
                    }
                }
                state.error?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
                Button(
                    onClick = {
                        if (loginMode) viewModel.login(email, password)
                        else viewModel.signup(name, email, password)
                    },
                    enabled = !state.loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (loginMode) "Login" else "Sign up")
                }
                TextButton(
                    onClick = {
                        loginMode = !loginMode
                        viewModel.clearError()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (loginMode) "New mother? Create account" else "Already have an account? Login")
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}
