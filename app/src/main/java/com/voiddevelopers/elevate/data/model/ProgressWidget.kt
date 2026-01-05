package com.voiddevelopers.elevate.data.model

sealed interface ProgressWidget {
    val percentage: Double
    val name: String
}

data class GridWidget(
    override val percentage: Double,
    override val name: String,
    val rows: Int,
    val columns: Int
) : ProgressWidget

data class LinearWidget(
    override val percentage: Double,
    override val name: String
) : ProgressWidget

data class CircularWidget(
    override val percentage: Double,
    override val name: String
) : ProgressWidget