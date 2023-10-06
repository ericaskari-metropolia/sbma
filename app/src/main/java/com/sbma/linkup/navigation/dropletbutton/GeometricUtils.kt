package com.sbma.linkup.navigation.dropletbutton

fun lerp(start: Float, stop: Float, fraction: Float) =
    (start * (1 - fraction) + stop * fraction)