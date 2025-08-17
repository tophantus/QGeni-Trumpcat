package com.example.qgeni

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.qgeni.data.preferences.ThemeMode
import com.example.qgeni.data.preferences.ThemePreferenceManager
import com.example.qgeni.reminder.NotificationHelper
import com.example.qgeni.ui.screens.dictionary.DictionaryScreen
import com.example.qgeni.ui.screens.dictionary.DictionaryViewModel
import com.example.qgeni.ui.screens.leaderboard.LeaderboardViewModel
import com.example.qgeni.ui.screens.mascot.MascotMainScreen
import com.example.qgeni.ui.screens.mascot.MascotViewModel
import com.example.qgeni.ui.screens.navigation.QGNavHost
import com.example.qgeni.ui.screens.profile.WordStatsRangeChartScreen
import com.example.qgeni.ui.theme.QGenITheme
import com.example.qgeni.utils.ContextConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var themePreferenceManager: ThemePreferenceManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppContextHolder.appContext = applicationContext
        themePreferenceManager = ThemePreferenceManager(this)
        NotificationHelper.createNotificationChannel(this)

        val mascotViewModel: MascotViewModel = ViewModelProvider(this)[MascotViewModel::class.java]
        val dictionaryViewModel: DictionaryViewModel = ViewModelProvider(this)[DictionaryViewModel::class.java]
        val leaderboardViewModel: LeaderboardViewModel = ViewModelProvider(this)[LeaderboardViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            val themeMode = themePreferenceManager.themeFlow.collectAsState(initial = ThemeMode.DARK.name)
            val currentTheme = remember(themeMode.value) {
                ThemeMode.valueOf(themeMode.value)
            }

            ContextConstants.init(this)

            val isDarkTheme = when (currentTheme) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            QGenITheme(dynamicColor = false, darkTheme = isDarkTheme) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val navController = rememberNavController()
                    val currentBackStack by navController.currentBackStackEntryAsState()
                    val currentRoute = currentBackStack?.destination?.route

                    val mascotUiState by mascotViewModel.mascotUiState.collectAsState()


                    val hideMascotRoutes = listOf(
                        "sign_in",
                        "sign_up",
                        "forgot_password",
                        "verification",
                        "reset_password",
                        "welcome"
                    )
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)) {
                        QGNavHost(
                            modifier = Modifier,
                            navController = navController,
                            currentTheme = currentTheme,
                            onThemeChange = { newTheme ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    themePreferenceManager.saveTheme(newTheme.name)
                                }
                            },
                            mascotViewModel = mascotViewModel,
                            dictionaryViewModel = dictionaryViewModel,
                            leaderboardViewModel = leaderboardViewModel
                        )
                        if (currentRoute !in hideMascotRoutes && mascotUiState.showMascot) {
                            MascotMainScreen()
                        }

                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = getSharedPreferences("mascot_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putLong("last_open", System.currentTimeMillis()).apply()
    }
}

object AppContextHolder{
    lateinit var appContext: Context
}








