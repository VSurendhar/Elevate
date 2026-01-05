package com.voiddevelopers.elevate.presentation.screens.progressBuilder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.voiddevelopers.elevate.data.model.CircularWidget
import com.voiddevelopers.elevate.data.model.GridWidget
import com.voiddevelopers.elevate.data.model.LinearWidget
import com.voiddevelopers.elevate.data.model.ProgressUpdate
import com.voiddevelopers.elevate.data.model.ProgressWidget
import com.voiddevelopers.elevate.presentation.screens.main.components.ProgressCard3View
import com.voiddevelopers.elevate.presentation.ui.theme.textColors
import com.voiddevelopers.elevate.util.CustomRoundedCorderBox
import com.voiddevelopers.elevate.util.toPx
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressBuilderScreen(navController: NavHostController) {
    var selectedProgressWidget by remember {
        mutableStateOf<ProgressWidget>(
            GridWidget(
                percentage = 75.0, name = "My Grid", rows = 5, columns = 7
            )
        )
    }
    var selectedUpdatePreference by remember { mutableStateOf(ProgressUpdate.Manual) }
    var selectedAutoProgressTime by remember { mutableStateOf("Start") }
    var progressTitle by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    LaunchedEffect(selectedUpdatePreference) {
        if (selectedUpdatePreference == ProgressUpdate.Automatic) {
            delay(200)
            listState.animateScrollToItem(
                index = 4
            )
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    ScreenTitle()
                }, actions = {
                    SaveButton()
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProgressTitleInputCard(
                    value = progressTitle, onValueChange = { progressTitle = it })
            }
            item { ProgressWidgetPreview(selectedProgressWidget) }
            item {
                ProgressViewSelector(
                    selectedProgressWidget = selectedProgressWidget, onSelect = { newWidget ->
                        selectedProgressWidget = newWidget
                    })
            }
            item {
                ProgressUpdatePreferenceSelector(
                    selectedUpdatePreference,
                    onProgressUpdateChange = { selectedUpdatePreference = it })
            }
            item {
                AnimatedVisibility(
                    visible = selectedUpdatePreference == ProgressUpdate.Automatic,
                    enter = expandVertically(
                        expandFrom = Alignment.Top
                    ) + fadeIn(),
                    exit = shrinkVertically(
                        shrinkTowards = Alignment.Top
                    ) + fadeOut()
                ) {
                    AutomaticProgressSelector(
                        selectedTime = selectedAutoProgressTime,
                        onSelect = { selectedAutoProgressTime = it })
                }
            }
        }
    }
}

@Composable
fun ScreenTitle() {
    Text(
        text = "Progress Builder",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun SaveButton() {
    IconButton(
        modifier = Modifier.padding(horizontal = 20.dp), onClick = { /* Save action */ }) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "✓",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProgressTitleInputCard(
    title: String = "Enter the title of the progress",
    maxChars: Int = 30,
    value: String,
    onValueChange: (String) -> Unit,
) {
    CustomRoundedCorderBox {
        Column(
            modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Section title
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.textColors().subTitle1Color
            )

            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                    .padding(horizontal = 10.dp)
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = {
                        if (it.length <= maxChars) {
                            onValueChange(it)
                        }
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.weight(1f)
                            ) {
                                if (value.isEmpty()) {
                                    Text(
                                        text = "Progress title",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.5f
                                        )
                                    )
                                }
                                innerTextField()
                            }

                            // Character counter
                            Text(
                                text = "${value.length} / $maxChars",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.textColors().descriptionColor
                            )
                        }
                    })

                // Bottom divider
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                )
            }
        }
    }
}

@Composable
fun ProgressWidgetPreview(selectedWidget: ProgressWidget) {
    CustomRoundedCorderBox {
        Column(
            modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Progress Widget",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.textColors().subTitle1Color
            )

            when (selectedWidget) {
                is GridWidget -> GridProgressView(selectedWidget)
                is LinearWidget -> LinearProgressView(selectedWidget)
                is CircularWidget -> CircularProgressView(selectedWidget)
            }
        }
    }
}

@Composable
fun ProgressUpdatePreferenceSelector(
    selectedUpdate: ProgressUpdate,
    onProgressUpdateChange: (ProgressUpdate) -> Unit,
) {
    CustomRoundedCorderBox {
        Column(
            modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Choose the Progress Update Preference",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.textColors().subTitle1Color
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProgressUpdate.entries.forEach { update ->
                    FilterChip(
                        selected = selectedUpdate == update,
                        onClick = { onProgressUpdateChange(update) },
                        label = { Text(update.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.textColors().subTitle1Color
                        )
                    )
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

@Composable
@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
fun LinearProgressView(widget: LinearWidget) {
    val trackColor = MaterialTheme.colorScheme.inversePrimary.toArgb()
    val indicatorColor = MaterialTheme.colorScheme.primary.toArgb()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .padding(horizontal = 40.dp),
            factory = { ctx ->
                com.google.android.material.progressindicator.LinearProgressIndicator(ctx).apply {
                    isIndeterminate = false
                    max = 100
                    progress = widget.percentage.toInt()
                    setIndicatorColor(indicatorColor)
                    this.trackColor = trackColor
                    trackThickness = 8.dp.toPx(ctx)
                }
            })
    }
}

@Composable
@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
fun CircularProgressView(widget: CircularWidget) {
    val indicatorColor = MaterialTheme.colorScheme.primary.toArgb()
    val trackColor = MaterialTheme.colorScheme.inversePrimary.toArgb()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp), factory = { ctx ->
                com.google.android.material.progressindicator.CircularProgressIndicator(ctx).apply {
                    isIndeterminate = false
                    max = 100
                    progress = widget.percentage.toInt()
                    setIndicatorColor(indicatorColor)
                    this.trackColor = trackColor
                    indicatorSize = 100.dp.toPx(ctx)
                    trackThickness = 8.dp.toPx(ctx)
                }
            })

        Text(
            text = "${widget.percentage.toInt()}%",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.textColors().titleColor
        )
    }
}

@Composable
fun ProgressViewSelector(
    selectedProgressWidget: ProgressWidget,
    onSelect: (ProgressWidget) -> Unit = {},
) {
    CustomRoundedCorderBox {
        Column(
            modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Choose the Progress View",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.textColors().subTitle1Color
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("Circular", "Linear", "Grid").forEach { label ->
                    val isSelected = when (selectedProgressWidget) {
                        is CircularWidget -> label == "Circular"
                        is LinearWidget -> label == "Linear"
                        is GridWidget -> label == "Grid"
                    }

                    FilterChip(
                        selected = isSelected, onClick = {
                            // Default percentage and name for demo; can be replaced with dynamic values
                            val widget = when (label) {
                                "Circular" -> CircularWidget(
                                    percentage = 70.0, name = "Circular Widget"
                                )

                                "Linear" -> LinearWidget(percentage = 50.0, name = "Linear Widget")
                                "Grid" -> GridWidget(
                                    percentage = 75.0, name = "Grid Widget", rows = 5, columns = 7
                                )

                                else -> CircularWidget(percentage = 0.0, name = "Unknown")
                            }
                            onSelect(widget)
                        }, label = { Text(label) }, colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.textColors().subTitle1Color
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AutomaticProgressSelector(
    selectedTime: String,
    onSelect: (String) -> Unit = {},
) {
    CustomRoundedCorderBox {
        Column(
            modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Automatic Progress At",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.textColors().subTitle1Color
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    "Start of the Day" to "12:00 AM", "End of the Day" to "11:59 PM"
                ).forEach { (label, time) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                            .clickable { onSelect(label) }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = label,
                            color = MaterialTheme.colorScheme.textColors().subTitle1Color,
                            fontSize = 14.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(text = time, color = Color.Gray, fontSize = 14.sp)
                            RadioButton(
                                selected = selectedTime == label,
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                    unselectedColor = MaterialTheme.colorScheme.inversePrimary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}