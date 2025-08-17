package com.example.qgeni.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qgeni.ui.screens.components.NextButton
import com.example.qgeni.ui.theme.QGenITheme

@Composable
fun ResetPasswordScreen(
    onBackClick: () -> Unit,
    onPasswordChangeDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        Row(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .fillMaxWidth()
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "BackIcon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )
            }
            Spacer(modifier = Modifier.weight(0.7f))
        }
        ResetPasswordPage(
            password = uiState.password,
            rePassword = uiState.rePassword,
            onPasswordChanged = viewModel::updatePassword,
            onRePasswordChanged = viewModel::updateRePassword,
            passwordVisible = uiState.passwordVisible,
            onPasswordVisibleClick = viewModel::changePasswordVisibility,
            rePasswordVisible = uiState.rePasswordVisible,
            onRePasswordVisibleClick = viewModel::changeRePasswordVisibility,
            isPasswordSame = uiState.isPasswordSame,
        )
        Spacer(Modifier.weight(1f))
        Row {
            Spacer(modifier = Modifier.weight(2f))
            NextButton(
                onPrimary = false,
                onClick = {
                    if (uiState.isPasswordSame) {
                        viewModel.resetPassword()
                        onPasswordChangeDone()
                    }
                }
            )
            Spacer(modifier = Modifier.weight(0.25f))
        }
        Spacer(Modifier.height(56.dp))
    }
}

@Composable
fun ResetPasswordPage(
    password: String,
    rePassword: String,
    onPasswordChanged: (String) -> Unit,
    onRePasswordChanged: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleClick: () -> Unit,
    rePasswordVisible: Boolean,
    onRePasswordVisibleClick: () -> Unit,
    isPasswordSame: Boolean,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
                bottom = 16.dp,
                start = 32.dp,
                end = 32.dp
            )
    ) {
        Text(
            text = "Thay đổi mật khẩu",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
                onPasswordChanged(it)
            },
            label = {
                Text(
                    text = "Mật khẩu mới",
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                val icon =
                    if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff

                IconButton(
                    onClick = onPasswordVisibleClick
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "passwordVisible",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

            },
            shape = RoundedCornerShape(size = 10.dp),
            visualTransformation = if (!passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            colors = OutlinedTextFieldDefaults
                .colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    cursorColor = MaterialTheme.colorScheme.onBackground
                ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = rePassword,
            onValueChange = {
                onRePasswordChanged(it)
            },
            label = {
                Text(
                    text = "Nhập lại mật khẩu",
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            isError = !isPasswordSame,
            supportingText = {
                if (!isPasswordSame) {
                    Text(
                        text = "Mật khẩu không khớp",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                val icon =
                    if (rePasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff

                IconButton(
                    onClick = onRePasswordVisibleClick
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "passwordVisible",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            shape = RoundedCornerShape(size = 10.dp),
            visualTransformation = if (!rePasswordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            colors = OutlinedTextFieldDefaults
                .colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    cursorColor = MaterialTheme.colorScheme.onBackground
                ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(48.dp))
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun LightScreenPreview() {
    QGenITheme(dynamicColor = false) {
        ResetPasswordScreen(
            onBackClick = {},
            {},
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun DarkScreenPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        ResetPasswordScreen(
            onBackClick = {},
            {}
        )
    }
}

