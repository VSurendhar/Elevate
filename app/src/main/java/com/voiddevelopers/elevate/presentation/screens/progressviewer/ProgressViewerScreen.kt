package com.voiddevelopers.elevate.presentation.screens.progressviewer


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.voiddevelopers.elevate.data.model.GridWidget
import com.voiddevelopers.elevate.presentation.screens.main.components.ProgressCard3View

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressViewerScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("View Progress") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Menu, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            repeat(3) {
                item {
                    Row {
                        Spacer(modifier = Modifier.width(30.dp))
                        GridProgressView(
                            widget = GridWidget(
                                percentage = 50.0,
                                name = "Preview",
                                rows = 5,
                                columns = 7
                            )
                        )
                        if (it == 2) {
                            Spacer(modifier = Modifier.width(30.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
fun GridProgressView(widget: GridWidget) {
    val completedColor = MaterialTheme.colorScheme.primary.toArgb()
    val incompleteColor = MaterialTheme.colorScheme.inversePrimary.toArgb()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        val achievementList = List(widget.rows) { row ->
            List(widget.columns) { col ->
                // For demo purposes, fill randomly (replace with your real data)
                ((row + col) % 2 == 0)
            }
        }

        AndroidView(
            modifier = Modifier.wrapContentSize(), factory = { ctx ->
                ProgressCard3View(
                    context = ctx,
                    row = widget.rows,
                    col = widget.columns,
                    achievementList = achievementList,
                    boxDecoration = ProgressCard3View.boxDefaults.copy(
                        completedColor = completedColor, incompleteColor = incompleteColor
                    )
                )
            })

    }
}

