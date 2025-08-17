package com.example.qgeni.ui.screens.mascot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MascotMainScreen(viewModel: MascotViewModel = viewModel()) {
    val mascotUiState by viewModel.mascotUiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        DraggableMascot(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            uiState = mascotUiState,
            onDrag = {dx, dy -> viewModel.updateOffset(dx, dy)},
            onClickBubble = { viewModel.setDialogVisible(true) },
            onClickMascot = { viewModel.onMascotClicked() },
            onDismiss = { viewModel.setDialogVisible(false) },
            onHold = { viewModel.openChat(true) }
        )

        if (mascotUiState.showChat) {
            ChatbotPopup(
                onDismissRequest = { viewModel.openChat(false) },
                chatHistory = mascotUiState.chatHistories,
                userInput = mascotUiState.userInput,
                onUserInputChange = { viewModel.onUserInputChange(it) },
                onSend = viewModel::sendUserInput,
                onNewChat = viewModel::newChat
            )
        }
    }
}
