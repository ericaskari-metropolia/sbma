package com.exyte.animatednavbar.shape

import com.sbma.linkup.navigation.ShapeCornerRadius
import com.sbma.linkup.navigation.shapeCornerRadius

data class IndentShapeData(
    val xIndent: Float = 0f,
    val height: Float = 0f,
    val width: Float = 0f,
    val cornerRadius: ShapeCornerRadius = shapeCornerRadius(0f),
    val ballOffset: Float = 0f,
)