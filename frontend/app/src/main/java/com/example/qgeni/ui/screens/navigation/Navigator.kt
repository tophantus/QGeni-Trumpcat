package com.example.qgeni.ui.screens.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.qgeni.data.preferences.JwtPreferenceManager
import com.example.qgeni.ui.screens.game.WordleGame
import com.example.qgeni.data.preferences.ThemeMode
import com.example.qgeni.data.preferences.UserPreferenceManager
import com.example.qgeni.ui.screens.HomeScreen
import com.example.qgeni.ui.screens.dictionary.DictionaryScreen
import com.example.qgeni.ui.screens.dictionary.DictionarySelectionScreen
import com.example.qgeni.ui.screens.dictionary.DictionaryViewModel
import com.example.qgeni.ui.screens.dictionary.SavedWordsScreen
import com.example.qgeni.ui.screens.leaderboard.LeaderboardListScreen
import com.example.qgeni.ui.screens.leaderboard.LeaderboardListeningListViewModel
import com.example.qgeni.ui.screens.leaderboard.LeaderboardReadingListViewModel
import com.example.qgeni.ui.screens.leaderboard.LeaderboardScreen
import com.example.qgeni.ui.screens.leaderboard.LeaderboardViewModel
import com.example.qgeni.ui.screens.login.ForgotPasswordScreen
import com.example.qgeni.ui.screens.login.ResetPasswordScreen
import com.example.qgeni.ui.screens.login.SignInScreen
import com.example.qgeni.ui.screens.login.SignUpScreen
import com.example.qgeni.ui.screens.login.VerificationScreen
import com.example.qgeni.ui.screens.mascot.MascotViewModel
import com.example.qgeni.ui.screens.practices.ListeningPracticeListScreen
import com.example.qgeni.ui.screens.practices.ListeningPracticeScreen
import com.example.qgeni.ui.screens.practices.ReadingPracticeListScreen
import com.example.qgeni.ui.screens.practices.ReadingPracticeScreen
import com.example.qgeni.ui.screens.practices.SelectionScreen
import com.example.qgeni.ui.screens.profile.ChangeInformationScreen
import com.example.qgeni.ui.screens.profile.ProfileScreen
import com.example.qgeni.ui.screens.profile.WordStatsRangeChartScreen
import com.example.qgeni.ui.screens.reminder.ReminderSettingsScreen
import com.example.qgeni.ui.screens.welcome.WelcomeScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    data object ForgotPassword : Screen("forgot_password")
    data object SignIn : Screen("sign_in")
    data object SignUp : Screen("sign_up")
    data object Verification : Screen("verification")
    data object ResetPassword: Screen("reset_password")
    data object ListeningPractice : Screen("listening_practice/{id}")
    data object ListeningPracticeList: Screen("listening_practice_list")
    data object ReadingPractice : Screen("reading_practice/{id}")
    data object ReadingPracticeList: Screen("reading_practice_list")
    data object Selection : Screen("selection")
    data object Welcome : Screen("welcome")
    data object Home : Screen("home")
    data object Profile: Screen("profile")
    data object ChangeInfo: Screen("change_information")
    data object Dictionary: Screen("dictionary")
    data object ReminderSetting: Screen("reminder")
    data object Stats: Screen("stats")
    data object DictionarySelection: Screen("dictionary_selection")
    data object SavedWord: Screen("saved_word")
    data object Game: Screen("game")
    data object Leaderboard: Screen("leaderboard")
    data object ReadingLeaderboardList: Screen("reading_leaderboard")
    data object ListeningLeaderboardList: Screen("listening_leaderboard")
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QGNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    mascotViewModel: MascotViewModel,
    dictionaryViewModel: DictionaryViewModel,
    leaderboardViewModel: LeaderboardViewModel
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route,
        modifier = modifier
    ) {

        composable(
            Screen.Welcome.route,
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(
                        durationMillis = 500,
                        delayMillis = 100
                    )
                )
            }
        ) {
            WelcomeScreen(
                onNextButtonClick = {
                    coroutineScope.launch {
                        val jwt = JwtPreferenceManager.getJwtFlow(context)
                        if (jwt.isNullOrBlank()) {
                            navController.navigate(Screen.SignIn.route)
                        } else {
                            navController.navigate(Screen.Home.route)
                        }
                    }
                }
            )
        }


        composable(
            Screen.SignIn.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(
                        durationMillis = 500,
                        delayMillis = 100
                    )
                )
            }
        ) {
            SignInScreen(
                onBackClick = { navController.navigate(Screen.Welcome.route) },
                onSignInSuccess = { navController.navigate(Screen.Home.route) },
                onForgotPasswordClick = { navController.navigate(Screen.ForgotPassword.route) },
                onSignUpClick = { navController.navigate(Screen.SignUp.route) }
            )
        }


        composable(Screen.SignUp.route) {
            SignUpScreen(
                onBackClick = { navController.navigateUp() },
                onSignUpSuccess = { navController.navigate(Screen.SignIn.route) },
                onSignInClick = { navController.navigate(Screen.SignIn.route) }
            )
        }


        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = { navController.navigateUp() },
                onEmailVerified = {navController.navigate(Screen.Verification.route)}
            )
        }


        composable(Screen.Verification.route) {
            VerificationScreen(
                onBackClick = { navController.navigateUp() },
                onOTPVerified = { navController.navigate(Screen.ResetPassword.route) }
            )
        }

        composable(Screen.ResetPassword.route) {
            ResetPasswordScreen(
                onBackClick = { navController.navigateUp() },
                onPasswordChangeDone = { navController.navigate(Screen.SignIn.route) }
            )
        }

        composable(
            route = Screen.ListeningPractice.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType})
        ) { backStackEntry ->
            ListeningPracticeScreen(
                id = backStackEntry.arguments?.getInt("id") ?: 0,
                onBackClick = { navController.navigate(Screen.ListeningPracticeList.route) },
                onNavigatingToPracticeRepo = { navController.navigate(Screen.ListeningPracticeList.route) }
            )
        }

        composable(Screen.Game.route) {
            WordleGame(
                onBackClick = { navController.navigateUp() },
                onLeaderBoardClick = {
                    leaderboardViewModel.updateAchievement(it)
                    navController.navigate(Screen.Leaderboard.route)
                }
            )
        }

        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                viewModel = leaderboardViewModel
            )
        }

        composable(Screen.ReadingLeaderboardList.route) {
            LeaderboardListScreen(
                onBackClick = { navController.navigateUp() },
                onItemClick = {
                    leaderboardViewModel.updateAchievement(it)
                    navController.navigate(Screen.Leaderboard.route)
                },
                viewModel = LeaderboardReadingListViewModel()
            )
        }

        composable(Screen.ListeningLeaderboardList.route) {
            LeaderboardListScreen(
                onBackClick = { navController.navigateUp() },
                onItemClick = {
                    leaderboardViewModel.updateAchievement(it)
                    navController.navigate(Screen.Leaderboard.route)
                },
                viewModel = LeaderboardListeningListViewModel()
            )
        }
        
        composable(
            Screen.ReadingPractice.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            ReadingPracticeScreen(
                id = backStackEntry.arguments?.getInt("id") ?: 0,
                onNextButtonClick = {navController.navigate(Screen.ReadingPracticeList.route)},
                onBackClick = { navController.navigate(Screen.ReadingPracticeList.route) }
            )
        }

        composable(Screen.Selection.route) {
            SelectionScreen(
                onBackClick = { navController.navigate(Screen.Home.route) },
                onListeningListClick = { navController.navigate(Screen.ListeningPracticeList.route)},
                onReadingListClick = { navController.navigate(Screen.ReadingPracticeList.route) },
                onLeaderboardReadingClick = { navController.navigate(Screen.ReadingLeaderboardList.route)},
                onLeaderboardListeningClick = { navController.navigate(Screen.ListeningLeaderboardList.route)}
            )
        }



        composable(Screen.Home.route) {
            HomeScreen(
                onDictionaryClick = {navController.navigate(Screen.DictionarySelection.route)},
                onBackpackClick = { navController.navigate(Screen.Selection.route) },
                onAvatarClick = { navController.navigate(Screen.Profile.route) },
                onGameClick = { navController.navigate(Screen.Game.route)}
            )
        }

        composable(Screen.DictionarySelection.route) {
            DictionarySelectionScreen(
                onBackClick = { navController.navigateUp() },
                onDictionaryClick = { navController.navigate(Screen.Dictionary.route) },
                onSavedWordsClick = { navController.navigate(Screen.SavedWord.route) })
        }

        composable(Screen.SavedWord.route) {
            SavedWordsScreen(
                onBackClick = { navController.navigateUp() },
                onWordItemClick = {
                    navController.navigate(Screen.Dictionary.route)
                    dictionaryViewModel.selectWord(it)
                }
            )
        }
        composable(Screen.Dictionary.route) {
            DictionaryScreen(
                onBackClick = { navController.navigateUp() },
                viewModel = dictionaryViewModel
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onBackClick = { navController.navigateUp() },
                onChangeInfoClick = { navController.navigate(Screen.ChangeInfo.route) },
                onLogOutClick = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }, //thêm vào
                currentTheme = currentTheme,
                onThemeChange = onThemeChange,
                onNotificationClick = { navController.navigate(Screen.ReminderSetting.route) },
                onStatsClick = { navController.navigate(Screen.Stats.route) },
                mascotViewModel = mascotViewModel
            )
        }

        composable(Screen.ListeningPracticeList.route) {
            ListeningPracticeListScreen(
                onBackClick = { navController.navigate(Screen.Selection.route) },
                onItemClick = {
                    navController.navigate(
                        Screen.ListeningPractice.route.replace("{id}", it.toString())
                    )
                }
            )
        }

        composable(Screen.ReadingPracticeList.route) {
            ReadingPracticeListScreen(
                onBackClick = { navController.navigate(Screen.Selection.route) },
                onItemClick = {
                    navController.navigate(
                        Screen.ReadingPractice.route.replace("{id}", it.toString())
                    )
                }
            )
        }


        composable(Screen.ChangeInfo.route) {
            ChangeInformationScreen(
                onBackClick = { navController.navigateUp() },
                onNextButtonClick = { navController.navigateUp()}
            )
        }
        
        composable(Screen.ReminderSetting.route) {
            ReminderSettingsScreen(onBackClick = { navController.navigateUp() })
        }

        composable(Screen.Stats.route) {
            WordStatsRangeChartScreen(onBackClick = { navController.navigateUp() })
        }
    }
}
