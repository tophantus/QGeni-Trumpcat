package com.example.qgeni.ui.screens.reminder

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material.icons.outlined.Loop
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qgeni.reminder.NotificationHelper
import com.example.qgeni.reminder.ReminderScheduler
import com.example.qgeni.ui.screens.components.CustomIconSwitch2
import com.example.qgeni.ui.screens.profile.getNextTheme
import com.example.qgeni.ui.theme.QGenITheme

@Composable
fun ReminderSettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReminderViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    val showTimePicker = remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                ReminderScheduler.scheduleDailyReminder(
                    context, state.hour, state.minute, state.intervalDays
                )
            } else {
                Toast.makeText(context, "Bạn chưa cấp quyền thông báo!", Toast.LENGTH_SHORT).show()
            }
        }
    )


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
                text = "Reminder Setting",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.weight(1f))
        }



        Column(
            Modifier
                .weight(1f)
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(16.dp))
        {
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Reminder",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.weight(1f)
                            )
                            CustomIconSwitch2(
                                checked = state.enabled,
                                onCheckedChange = {
                                    viewModel.setEnabled(it)
                                    if (it) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                                            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                                            != PackageManager.PERMISSION_GRANTED
                                        ) {
                                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                        } else {
                                            ReminderScheduler.scheduleDailyReminder(
                                                context, state.hour, state.minute, state.intervalDays
                                            )
                                        }
                                    } else {
                                        ReminderScheduler.cancelReminder(context)
                                    }
                                },
                                useIcon = true,
                                enabledIconResId = Icons.Outlined.Notifications,
                                disabledIconResId = Icons.Outlined.NotificationsOff,
                                testTag = "test highlight"
                            )
                        }
                    }
                }
            }

            Button(onClick = { NotificationHelper.showReminderNotification(context) }) {
                Text("Test Notification")
            }

            if (state.enabled) {
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
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.EditCalendar,
                                    contentDescription = "Calendar",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "Thời gian nhắc: %02d:%02d".format(state.hour, state.minute),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .clickable { showTimePicker.value = true }
                                        .padding(vertical = 8.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Loop,
                                    contentDescription = "Loop",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    "Tần suất: mỗi ${state.intervalDays} ngày",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Slider(
                                value = state.intervalDays.toFloat(),
                                onValueChange = {
                                    viewModel.setInterval(it.toInt())
                                    ReminderScheduler.scheduleDailyReminder(context, state.hour, state.minute, it.toInt())
                                },
                                colors = SliderColors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    activeTrackColor = MaterialTheme.colorScheme.primary,
                                    activeTickColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledThumbColor = MaterialTheme.colorScheme.primary,
                                    disabledActiveTrackColor = MaterialTheme.colorScheme.primary,
                                    disabledActiveTickColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledInactiveTickColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledInactiveTrackColor = MaterialTheme.colorScheme.tertiary,
                                    inactiveTrackColor = MaterialTheme.colorScheme.tertiary,
                                    inactiveTickColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                valueRange = 1f..7f,
                                steps = 5
                            )
                        }
                    }
                }
            }
        }


    }

    if (showTimePicker.value) {
        LaunchedEffect(Unit) {
            TimePickerDialog(
                context,
                { _, hour: Int, minute: Int ->
                    viewModel.setTime(hour, minute)
                    ReminderScheduler.scheduleDailyReminder(context, hour, minute, state.intervalDays)
                },
                state.hour,
                state.minute,
                true
            ).show()

            showTimePicker.value = false // Ẩn sau khi hiển thị
        }
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun ReminderSettingLightScreenPreview() {
    QGenITheme(dynamicColor = false, darkTheme = false) {
        ReminderSettingsScreen(
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun ReminderSettingDarkScreenPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        ReminderSettingsScreen(
            onBackClick = {}
        )
    }
}



