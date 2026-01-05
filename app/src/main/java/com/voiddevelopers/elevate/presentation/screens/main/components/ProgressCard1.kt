package com.voiddevelopers.elevate.presentation.screens.main.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.voiddevelopers.elevate.presentation.ui.theme.textColors
import com.voiddevelopers.elevate.util.CustomRoundedCorderBox
import com.voiddevelopers.elevate.util.toPx

@Composable
fun ProgressCard1(onClick: () -> Unit) {
    CustomRoundedCorderBox {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .clickable { onClick() }
        ) {

            Text(
                text = "Progress 1: Learn Spanish",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.textColors().subTitle1Color
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                val trackColor = MaterialTheme.colorScheme.inversePrimary.toArgb()
                val indicatorColor = MaterialTheme.colorScheme.primary.toArgb()
                AndroidView(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp), factory = { ctx ->
                        LinearProgressIndicator(ctx)
                            .apply {
                                isIndeterminate = false
                                max = 100
                                progress = 50

                                setIndicatorColor(indicatorColor)
                                this.trackColor = trackColor

                                trackThickness = 8.dp.toPx(ctx)
                            }
                    })

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "50%",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.textColors().titleColor
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Current: 50 / 100 Lessons",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.textColors().descriptionColor
            )
        }
    }
}

@Composable
fun ProgressCard2() {
    CustomRoundedCorderBox {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Progress 2: Project Finish",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.textColors().subTitle1Color
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)
                ) {

                    val indicatorColor = MaterialTheme.colorScheme.primary.toArgb()
                    val trackColor = MaterialTheme.colorScheme.inversePrimary.toArgb()

                    AndroidView(
                        modifier = Modifier.fillMaxSize(), factory = { ctx ->
                            CircularProgressIndicator(ctx).apply {
                                isIndeterminate = false
                                max = 100
                                progress = 70

                                setIndicatorColor(indicatorColor)
                                this.trackColor = trackColor

                                indicatorSize = 100.dp.toPx(ctx)
                                trackThickness = 8.dp.toPx(ctx)
                            }
                        })

                    Text(
                        text = "70%",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.textColors().titleColor
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                Column {

                    Text(
                        text = "Project Finish",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.textColors().subTitle2Color
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Current: 7 / 10 Milestones",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.textColors().descriptionColor
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressCard3(row: Int, col: Int, achievementList: List<List<Boolean>>) {
    val totalHabits = row * col
    val completedHabits = achievementList.flatten().count { it }
    val percentage = if (totalHabits > 0) (completedHabits * 100) / totalHabits else 0

    CustomRoundedCorderBox {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "Progress 3: Daily Habits",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.textColors().subTitle1Color
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val completedColor = MaterialTheme.colorScheme.primary.toArgb()
                val inCompletedColor = MaterialTheme.colorScheme.inversePrimary.toArgb()
                AndroidView(
                    modifier = Modifier.wrapContentSize(),
                    factory = { ctx ->
                        ProgressCard3View(
                            context = ctx,
                            row = row,
                            col = col,
                            achievementList = achievementList,
                            boxDecoration = ProgressCard3View.boxDefaults.copy(
                                completedColor = completedColor,
                                incompleteColor = inCompletedColor
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.width(24.dp))

                Column {

                    Text(
                        text = "$percentage%",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.textColors().titleColor
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Current: $completedHabits/$totalHabits Habits Complete",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.textColors().descriptionColor
                    )
                }
            }
        }
    }
}

class ProgressCard3View @JvmOverloads constructor(
    context: Context,
    private val row: Int,
    private val col: Int,
    private val achievementList: List<List<Boolean>>,
    private val boxDecoration: BoxDecoration = boxDefaults,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    companion object {
        val boxDefaults = BoxDecoration(
            boxSize = 48f,
            boxSpacing = 24f,
            cornerRadius = 18f,
            completedColor = 0xFF00BFA5.toInt(),
            incompleteColor = 0xFFE0E0E0.toInt(),
        )
    }

    data class BoxDecoration(
        val boxSize: Float,
        val boxSpacing: Float,
        val cornerRadius: Float,
        val completedColor: Int,
        val incompleteColor: Int,
    )

    private val boxRect = RectF()
    private val cardRect = RectF()

    private val completedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = boxDecoration.completedColor
        style = Paint.Style.FILL
    }

    private val incompletePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = boxDecoration.incompleteColor
        style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth =
            (col * boxDecoration.boxSize + (col - 1) * boxDecoration.boxSpacing).toInt()

        val desiredHeight =
            (row * boxDecoration.boxSize + (row - 1) * boxDecoration.boxSpacing).toInt()

        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Card background
        cardRect.set(0f, 0f, width.toFloat(), height.toFloat())

        for (rowIndex in 0 until row) {
            for (colIndex in 0 until col) {

                val isCompleted = achievementList.getOrNull(rowIndex)?.getOrNull(colIndex) ?: false

                val left = colIndex * (boxDecoration.boxSize + boxDecoration.boxSpacing)
                val top = rowIndex * (boxDecoration.boxSize + boxDecoration.boxSpacing)

                boxRect.set(
                    left, top, left + boxDecoration.boxSize, top + boxDecoration.boxSize
                )

                canvas.drawRoundRect(
                    boxRect,
                    boxDecoration.cornerRadius,
                    boxDecoration.cornerRadius,
                    if (isCompleted) completedPaint else incompletePaint
                )
            }
        }
    }
}