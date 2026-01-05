package com.voiddevelopers.elevate.presentation.screens.main

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.voiddevelopers.elevate.presentation.screens.main.components.ProgressCard1
import com.voiddevelopers.elevate.presentation.screens.main.components.ProgressCard2
import com.voiddevelopers.elevate.presentation.screens.main.components.ProgressCard3
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {

    val achievementList = listOf(
        listOf(false, true, true, true, true, true, true),
        listOf(true, true, true, true, true, true, true),
        listOf(true, true, true, true, false, true, true),
        listOf(false, false, true, true, false, true, false),
        listOf(true, false, true, false, true, true, false)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "PROGRESS",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("progress_builder") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { ProgressCard1(onClick = { navController.navigate("progress_viewer") }) }
            item { ProgressCard2() }
            item {
                ProgressCard3(
                    achievementList = achievementList,
                    row = 5,
                    col = 7,
                )
            }
        }
    }
}

suspend fun createViewAndConvertToBitmap(
    context: Context,
    createView: (Context) -> View,
): Bitmap = withContext(Dispatchers.Main) {

    val view = createView(context)

    view.measure(
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )

    val width = view.measuredWidth
    val height = view.measuredHeight

    view.layout(0, 0, width, height)

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)

    bitmap
}

fun saveBitmapToFile(
    context: Context,
    bitmap: Bitmap,
    fileName: String = "progress_${System.currentTimeMillis()}",
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    quality: Int = 100,
): String? {
    val extension = when (format) {
        Bitmap.CompressFormat.PNG -> "png"
        Bitmap.CompressFormat.JPEG -> "jpg"
        Bitmap.CompressFormat.WEBP -> "webp"
        else -> "png"
    }

    val mimeType = when (format) {
        Bitmap.CompressFormat.PNG -> "image/png"
        Bitmap.CompressFormat.JPEG -> "image/jpeg"
        Bitmap.CompressFormat.WEBP -> "image/webp"
        else -> "image/png"
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        saveBitmapToFileQ(context, bitmap, fileName, extension, mimeType, format, quality)
    } else {
        saveBitmapToFileLegacy(context, bitmap, fileName, extension, format, quality)
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
private fun saveBitmapToFileQ(
    context: Context,
    bitmap: Bitmap,
    fileName: String,
    extension: String,
    mimeType: String,
    format: Bitmap.CompressFormat,
    quality: Int,
): String? {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.$extension")
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        put(MediaStore.MediaColumns.IS_PENDING, 1)
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

    return uri?.let {
        try {
            resolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(format, quality, outputStream)
            }

            // Clear IS_PENDING flag to make file visible
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            resolver.update(it, contentValues, null, null)

            it.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            // If something went wrong, delete the entry
            resolver.delete(it, null, null)
            null
        }
    }
}

private fun saveBitmapToFileLegacy(
    context: Context,
    bitmap: Bitmap,
    fileName: String,
    extension: String,
    format: Bitmap.CompressFormat,
    quality: Int,
): String? {
    return try {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs()
        }

        val file = File(downloadsDir, "$fileName.$extension")
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(format, quality, outputStream)
            outputStream.flush()
        }

        // Notify MediaStore about the new file
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DATA, file.absolutePath)
            put(
                MediaStore.MediaColumns.MIME_TYPE, when (format) {
                    Bitmap.CompressFormat.PNG -> "image/png"
                    Bitmap.CompressFormat.JPEG -> "image/jpeg"
                    Bitmap.CompressFormat.WEBP -> "image/webp"
                    else -> "image/png"
                }
            )
        }
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}