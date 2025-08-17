package com.example.qgeni.ui.screens.login

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qgeni.R
import com.example.qgeni.ui.screens.components.NextButton
import com.example.qgeni.ui.screens.uploads.LoadingScreen
import com.example.qgeni.ui.theme.QGenITheme
import com.example.qgeni.utils.ErrorMessages


@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onSignUpSuccess: () -> Unit,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel = viewModel(),
) {

    val uiState by signUpViewModel.uiState.collectAsState()

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
        SignUpPage(
            username = uiState.username,
            phoneNumber = uiState.phoneNumber,
            email = uiState.email,
            password = uiState.password,
            passwordVisible = uiState.passwordVisible,
            termsAccepted = uiState.termsAccepted,
            onUsernameChange = { signUpViewModel.updateUsername(it) },
            onPhoneNumberChange = { signUpViewModel.updatePhoneNumber(it) },
            onEmailChange = { signUpViewModel.updateEmail(it) },
            onPasswordChange = { signUpViewModel.updatePassword(it) },
            onPasswordVisibleClick = { signUpViewModel.togglePasswordVisible() },
            onTermsAcceptedChange = { signUpViewModel.toggleTermsAccepted() },
            onSignInClick = onSignInClick,
            isAccountError = uiState.isAccountError,
            isEmailError = uiState.isEmailError,
            isPasswordError = uiState.isPasswordError,
            isPhoneNumberError = uiState.isPhoneNumberError,
            onKeyBoardActions = { signUpViewModel.reset() },
            modifier = Modifier.weight(1f)
        )
        Row {
            Spacer(modifier = Modifier.weight(2f))
            NextButton(
                onPrimary = false,
                onClick = {
                    signUpViewModel.updateConstraint()
                    if (signUpViewModel.checkEmpty()) {
                        signUpViewModel.signUp()
                    }
//                    signUpViewModel.showSuccessDialog(true)
                },
            )
            Spacer(modifier = Modifier.weight(0.25f))
        }
        Spacer(modifier = Modifier.height(56.dp))
    }

    if (uiState.isLoading) {
        LoadingScreen(message = "Các tiên nữ đang làm thủ thục")
    }

    if (uiState.showSuccessDialog) {
        SignUpSuccess(
            text = "TẠO TÀI KHOẢN THÀNH CÔNG",
            onDismissRequest = {
                onSignUpSuccess()
                signUpViewModel.showSuccessDialog(false)
            },
            onNextButtonClick =
            {
                onSignUpSuccess() //chuyển về đăng nhập
                signUpViewModel.showSuccessDialog(false)
            }
        )
    }
}


@Composable
fun SignUpPage(
    username: String,
    phoneNumber: String,
    email: String,
    password: String,
    passwordVisible: Boolean,
    termsAccepted: Boolean,
    onUsernameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibleClick: () -> Unit,
    onTermsAcceptedChange: (Boolean) -> Unit,
    onSignInClick: () -> Unit,
    isAccountError: Boolean,
    isPasswordError: Boolean,
    isEmailError: Boolean,
    isPhoneNumberError: Boolean,
    onKeyBoardActions: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester2 = remember {
        FocusRequester()
    }
    val focusRequester3 = remember {
        FocusRequester()
    }
    val focusRequester4 = remember {
        FocusRequester()
    }
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
            text = "Đăng ký",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Tạo tài khoản mới",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = username,
            onValueChange = {
                onUsernameChange(it)
                onKeyBoardActions()
            },
            label = {
                Text(
                    text = "Tên tài khoản",
                )
            },
            maxLines = 1,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            isError = isAccountError,
            trailingIcon = {
                if (isAccountError)
                    Icon(Icons.Outlined.Error,"error", tint = MaterialTheme.colorScheme.error)
            },
            supportingText = {
                if (isAccountError) {
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
            value = phoneNumber,
            onValueChange = {
                onPhoneNumberChange(it)
                onKeyBoardActions()
            },
            label = {
                Text(
                    text = "Số điện thoại",
                )
            },
            maxLines = 1,
            leadingIcon = {
                Icon(
                    Icons.Outlined.Phone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            isError = isPhoneNumberError,
            trailingIcon = {
                if (isPhoneNumberError)
                    Icon(Icons.Outlined.Error,"error", tint = MaterialTheme.colorScheme.error)
            },
            supportingText = {
                if (isPhoneNumberError) {
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
                    focusRequester3.requestFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester2)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {
                onEmailChange(it)
                onKeyBoardActions()
            },
            label = {
                Text(
                    text = "Địa chỉ email",
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
            isError = isEmailError,
            trailingIcon = {
                if (isEmailError)
                    Icon(Icons.Outlined.Error,"error", tint = MaterialTheme.colorScheme.error)
            },
            supportingText = {
                if (isEmailError) {
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
                    focusRequester4.requestFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester3)
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
            leadingIcon = {
                Icon(
                    Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            isError = isPasswordError,
            supportingText = {
                if (isPasswordError) {
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
                onNext = {
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester4)
                .testTag("password_field")
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = onTermsAcceptedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary,

                ),
                modifier = Modifier.testTag("terms_checkbox")
            )
            Text(
                text = "Đồng ý với",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = " điều khoản của chúng tôi",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(
                    onClick = {

                    }
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Đã có tài khoản?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = " Đăng nhập",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(
                    onClick = onSignInClick
                )
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Composable
fun SignUpSuccess(
    text: String,
    onDismissRequest: () -> Unit,
    onNextButtonClick: () -> Unit,
    @DrawableRes
    imageResourceId: Int = R.drawable.avatar_3,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(imageResourceId),
                    contentDescription = "fairy",
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = text,
                        modifier = Modifier.testTag("Success"),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = onNextButtonClick,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            Text(
                                text = "XÁC NHẬN",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignUpSuccessLightScreenPreview() {
    QGenITheme(dynamicColor = false) {
        SignUpSuccess(
            text = "TẠO TÀI KHOẢN THÀNH CÔNG",
            onDismissRequest = {},
            onNextButtonClick = {},
            imageResourceId = R.drawable.avatar_3,
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignUpSuccessDarkScreenPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        SignUpSuccess(
            text = "TẠO TÀI KHOẢN THÀNH CÔNG",
            onDismissRequest = {},
            onNextButtonClick = {},
            imageResourceId = R.drawable.avatar_3,
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignUpLightScreenPreview() {
    QGenITheme(dynamicColor = false) {
        SignUpScreen(
            onBackClick = {},
            onSignUpSuccess = {},
            {}
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignUpDarkScreenPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        SignUpScreen(
            onBackClick = {},
            onSignUpSuccess = {},
            {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun SignUpPagePreview() {
    QGenITheme(dynamicColor = false) {
        var username by remember { mutableStateOf("") }
        var phoneNumber by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var termsAccepted by remember { mutableStateOf(true) }
        SignUpPage(
            username = username,
            phoneNumber = phoneNumber,
            email = email,
            password = password,
            passwordVisible = false,
            termsAccepted = termsAccepted,
            onUsernameChange = { username = it },
            onPhoneNumberChange = { phoneNumber = it },
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onPasswordVisibleClick = {},
            onTermsAcceptedChange = { termsAccepted = it },
            onSignInClick = { },
            false,
            false,
            false,
            false,
            {}
        )
    }
}