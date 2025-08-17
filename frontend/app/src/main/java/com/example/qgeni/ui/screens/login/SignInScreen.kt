package com.example.qgeni.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qgeni.ui.screens.components.NextButton
import com.example.qgeni.ui.screens.uploads.ErrorScreen
import com.example.qgeni.ui.screens.uploads.LoadingScreen
import com.example.qgeni.ui.theme.QGenITheme
import com.example.qgeni.utils.ErrorMessages

@Composable
fun SignInScreen(
    onBackClick: () -> Unit,
    onSignInSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel = viewModel()
) {
    val signInUIState by signInViewModel.uiState.collectAsState()
    val context = LocalContext.current

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
            Text(
                text = "",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        SignInPage(
            email = signInUIState.email,
            password = signInUIState.password,
            onEmailChange = { signInViewModel.updateEmail(it)},
            onPasswordChange = { signInViewModel.updatePassword(it) },
            passwordVisible = signInUIState.passwordVisible,
            onPasswordVisibleClick = {signInViewModel.togglePasswordVisible()},
            onSignUpClick = onSignUpClick,
            onForgotPasswordClick = onForgotPasswordClick,
            isAccountError = signInUIState.isAccountError,
            isPasswordError = signInUIState.isPasswordError,
            onKeyBoardActions = { signInViewModel.reset() },
            isFailure = signInUIState.isFailure,
            modifier = Modifier.weight(1f)
        )
        Row {
            Spacer(modifier = Modifier.weight(2f))
            NextButton(
                onPrimary = false,
                onClick = {
                    signInViewModel.updateConstraint()
                    if (signInViewModel.checkEmpty()) {
                        signInViewModel.signIn()
                        signInViewModel.updateConstraint()
                    }
                }
            )
            Spacer(modifier = Modifier.weight(0.25f))
        }
        Spacer(modifier = Modifier.height(56.dp))
    }

    if (signInUIState.signInEvent == SignInEvent.LOADING) {
        LoadingScreen(message = "Các tiên nữ đang xác nhận thông tin")
    }

    if (signInUIState.signInEvent == SignInEvent.FAILURE) {
        ErrorScreen(
            onDismissRequest = { signInViewModel.hideError() },
            onLeaveButtonClick = { /*TODO*/ },
            message = "Vui lòng thử lại",
            showButton = false
        )
    }

    LaunchedEffect(signInUIState.signInEvent) {
        if (signInUIState.signInEvent == SignInEvent.SUCCESS) {
            onSignInSuccess()
        }
    }

}

@Composable
fun SignInPage(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibleClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    isAccountError: Boolean,
    isPasswordError: Boolean,
    onKeyBoardActions: () -> Unit,
    isFailure: Boolean,
    modifier: Modifier = Modifier
) {
    val focusRequester2 = remember {
        FocusRequester()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
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
            text = "Đăng nhập",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Chào mừng bạn quay trở lại",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                onEmailChange(it)
                onKeyBoardActions()
            },
            label = {
                Text(
                    text = "Email",
                )
            },
            maxLines = 1,
            leadingIcon = {
                Icon(
                    Icons.Outlined.Email,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            isError = isAccountError || isFailure,
            trailingIcon = {
                if (isAccountError)
                    Icon(Icons.Outlined.Error,"error", tint = MaterialTheme.colorScheme.error)
            },
            supportingText = {
                if (isFailure && !isAccountError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = ErrorMessages.Failure.message,
                        color = MaterialTheme.colorScheme.error
                    )
                } else if (isAccountError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = ErrorMessages.EmptyField.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            shape = RoundedCornerShape(size = 10.dp),
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
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusRequester2.requestFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
                onPasswordChange(it)
                onKeyBoardActions()
            },
            label = {
                Text(
                    text = "Mật khẩu",
                )
            },
            maxLines = 1,
            leadingIcon = {
                Icon(
                    Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            isError = isPasswordError || isFailure,
            supportingText = {
                if (isFailure && !isPasswordError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = ErrorMessages.Failure.message,
                        color = MaterialTheme.colorScheme.error
                    )
                } else if (isPasswordError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = ErrorMessages.EmptyField.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                val icon =
                    if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff

                if (isPasswordError) {
                    Icon(Icons.Outlined.Error,"error", tint = MaterialTheme.colorScheme.error)
                } else {
                    IconButton(
                        onClick = onPasswordVisibleClick
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "passwordVisible",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
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
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                   keyboardController?.hide()
                }
            ),
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester2)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quên mật khẩu?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(
                    onClick = onForgotPasswordClick
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chưa có tài khoản? ",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = "Đăng ký",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clickable(
                        onClick = onSignUpClick
                    )
                    .testTag("signup")
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignInLightScreenPreview() {
    QGenITheme(dynamicColor = false) {
        SignInScreen(
            onBackClick = {},
            onSignInSuccess = {},
            {},
            {}
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignInDarkScreenPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        SignInScreen(
            onBackClick = {},
            onSignInSuccess = {},
            {},
            {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun SignInPagePreview() {
    QGenITheme(dynamicColor = false) {
        val email by remember { mutableStateOf("") }
        val password by remember { mutableStateOf("") }
        SignInPage(
            email,
            password = password,
            onEmailChange = {},
            onPasswordChange = {},
            passwordVisible = false,
            {},
            {},
            {},
            isAccountError = true,
            isPasswordError = true,
            onKeyBoardActions = {},
            isFailure = false
        )
    }
}