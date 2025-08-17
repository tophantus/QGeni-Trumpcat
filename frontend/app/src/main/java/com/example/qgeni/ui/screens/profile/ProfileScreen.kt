package com.example.qgeni.ui.screens.profile

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qgeni.R
import com.example.qgeni.data.preferences.ThemeMode
import com.example.qgeni.ui.screens.components.CurvedBackground
import com.example.qgeni.ui.screens.mascot.MascotViewModel
import com.example.qgeni.ui.theme.QGenITheme

/*
    Màn hình hiển thị thông tin cá nhân.
    thay đổi cài đặt ứng dụng
 */

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onChangeInfoClick: () -> Unit,
    onLogOutClick: () -> Unit,
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    onNotificationClick: () -> Unit,
    onStatsClick: () -> Unit,
    mascotViewModel: MascotViewModel = viewModel(),
    viewModel: ProfileViewModel = viewModel()
) {
    val userInfo by viewModel.userInfo.collectAsState()
    val mascotUiState by mascotViewModel.mascotUiState.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.onPrimary
            )
    ) {
        CurvedBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Transparent
                )
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
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(0.7f))
                Text(
                    text = "",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            HeaderSection(
                username = userInfo?.username ?: "",
                email = userInfo?.email ?: "",
                phoneNumber = userInfo?.phoneNumber ?: ""
            )
            Spacer(modifier = Modifier.height(16.dp))
            PersonalInfoSection(
                onChangeInfoClick = onChangeInfoClick,
                onNotificationClick = onNotificationClick,
                onStatsClick = onStatsClick,
                showMascot = mascotUiState.showMascot,
                onShowMascotClick = {
                    mascotViewModel.toggleMascotVisibility(!mascotUiState.showMascot)
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingsSection(
                currentTheme = currentTheme,
                onThemeChange = onThemeChange
            )
            Spacer(modifier = Modifier.weight(1f))
            Row {
                Spacer(modifier = Modifier.weight(2f))
                IconButton(
                    onClick = {
                        viewModel.logOut(context = context)
                        onLogOutClick()
                    },
                    colors =
                    IconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .size(60.dp)
                        .clip(shape = RoundedCornerShape(size = 100.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = "next_button",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(0.25f))
            }
            Spacer(modifier = Modifier.height(56.dp))
        }
    }

}

@Composable
fun HeaderSection(
    username: String,
    email: String,
    phoneNumber: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Transparent
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(
                modifier = Modifier.height(50.dp)
            )
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .background(
                        color = Color.Transparent,
                        shape = CircleShape
                    )
                    .padding(4.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.avatar_2),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = CircleShape
                        )
                )
//                IconButton(
//                    onClick = {},
//                    modifier = Modifier
//                        .size(36.dp)
//                        .background(
//                            color = MaterialTheme.colorScheme.primary,
//                            shape = CircleShape
//                        )
//                        .border(
//                            width = 2.dp,
//                            color = MaterialTheme.colorScheme.tertiary,
//                            shape = CircleShape
//                        )
//                        .padding(8.dp)
//                        .align(Alignment.BottomEnd)
//                ) {
//                    Icon(
//                        painter = painterResource(id = android.R.drawable.ic_menu_edit),
//                        contentDescription = "Edit Icon",
//                        tint = MaterialTheme.colorScheme.onPrimary,
//                    )
//                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = username,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "$email | $phoneNumber",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun PersonalInfoSection(
    onChangeInfoClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onStatsClick: () -> Unit,
    showMascot: Boolean,
    onShowMascotClick: () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Transparent
                )
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onChangeInfoClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit Personal Info",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Thay đổi thông tin cá nhân",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onNotificationClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notification Settings",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Thông báo",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Thay đổi",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onStatsClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BarChart,
                        contentDescription = "Barchart",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Thống kê",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Xem",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onShowMascotClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Pets,
                        contentDescription = "Mascot",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Trợ lí",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = if (showMascot) "Hiển thị" else "Ẩn",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Transparent
                )
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SettingRow("Liên hệ với chúng tôi", Icons.AutoMirrored.Outlined.HelpOutline)
                Spacer(modifier = Modifier.height(16.dp))
                SettingRow("Chính sách quyền riêng tư", Icons.Outlined.Lock)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val nextTheme = getNextTheme(currentTheme)
                            onThemeChange(nextTheme)
                        }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ColorLens,
                        contentDescription = "Theme",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Chủ đề",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = when (currentTheme) {
                            ThemeMode.LIGHT -> "Sáng"
                            ThemeMode.DARK -> "Tối"
                            ThemeMode.SYSTEM -> "Theo thiết bị"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

//Hàm lấy theme kế tiếp khi ấn vào
fun getNextTheme(current: ThemeMode): ThemeMode {
    return when (current) {
        ThemeMode.LIGHT -> ThemeMode.DARK
        ThemeMode.DARK -> ThemeMode.SYSTEM
        ThemeMode.SYSTEM -> ThemeMode.LIGHT
    }
}

@Composable
fun LogOutConfirm(
    onNextButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
    @DrawableRes
    imageResourceId: Int = R.drawable.avatar_2,
) {
    Dialog(onDismissRequest = {}) {
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
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Bạn chắc chắn muốn đăng xuất",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Button(
                            onClick = onDismissRequest,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.tertiary,
                                    shape = RoundedCornerShape(10.dp)
                                )
                        ) {
                            Text(
                                text = "HỦY",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
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

@Composable
fun SettingRow(title: String, iconRes: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { /* Handle click */ }
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun SettingRow(title: String, imageVector: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { /* Handle click */ }
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
fun LogOutConfirmLightPreview() {
    QGenITheme(dynamicColor = false) {
        LogOutConfirm(
            onNextButtonClick = {},
            onDismissRequest = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    QGenITheme(dynamicColor = false) {
        ProfileScreen(
            onBackClick = {},
            {},
            {},
            ThemeMode.LIGHT,
            {},
            {},
            {},

        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewHeaderSection() {
    QGenITheme(dynamicColor = false) {
        HeaderSection(
            username = "",
            email = "fantus@domain.com",
            phoneNumber = "+01 234 567 89"
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewPersonalInfoSection() {
    QGenITheme(dynamicColor = false) {
        PersonalInfoSection({}, {}, {}, false) {
            {  }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsSection() {
    QGenITheme(dynamicColor = false) {
        SettingsSection(
            ThemeMode.DARK,
            {}
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewSettingRow() {
    QGenITheme(dynamicColor = false) {
        SettingRow(title = "Chủ đề", iconRes = android.R.drawable.ic_menu_manage)
    }

}

