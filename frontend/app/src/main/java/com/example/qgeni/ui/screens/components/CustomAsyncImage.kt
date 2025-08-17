package com.example.qgeni.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.compose.runtime.*
import java.io.BufferedInputStream
import java.util.concurrent.TimeUnit

suspend fun fetchImageBitmap(url: String): ImageBitmap? = withContext(Dispatchers.IO) {
    try {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder().url(url).build()

        val response = client.newCall(request).execute()

        return@withContext response.use {
            if (!response.isSuccessful) return@use null

            val inputStream = response.body?.byteStream() ?: return@use null

            // Use buffered input stream
            BufferedInputStream(inputStream).use { bufferedStream ->
                val bitmap = BitmapFactory.decodeStream(bufferedStream)
                bitmap?.asImageBitmap()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun CustomAsyncImage(
    modifier: Modifier = Modifier,
    url: String,
    placeholder: Painter,
    error: Painter,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Fit
) {
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var loadState by remember { mutableStateOf<ImageLoadState>(ImageLoadState.Loading) }

    LaunchedEffect(url) {
        loadState = ImageLoadState.Loading
        val result = fetchImageBitmap(url)
        if (result != null) {
            bitmap = result
            loadState = ImageLoadState.Success
        } else {
            loadState = ImageLoadState.Error
        }
    }


        when (loadState) {
            ImageLoadState.Loading -> Image(
                painter = placeholder,
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = modifier
            )
            ImageLoadState.Error -> Image(
                painter = error,
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = modifier
            )
            ImageLoadState.Success -> Image(
                bitmap = bitmap!!,
                contentDescription = contentDescription,
                contentScale = contentScale,
                modifier = modifier
            )
        }

}

sealed class ImageLoadState {
    data object Loading : ImageLoadState()
    data object Error : ImageLoadState()
    data object Success : ImageLoadState()
}