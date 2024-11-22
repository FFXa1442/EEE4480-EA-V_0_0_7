package com.sample.application.ea.ui.main.description

@Suppress("SameParameterValue")
fun DescriptionFragment.dpToPx(
    dp: Int,
) = Math.round(dp * resources.displayMetrics.density)

@Suppress("SameParameterValue")
fun DescriptionFragment.map(
    x: Int,
    inMin: Int,
    inMax: Int,
    outMin: Int,
    outMax: Int,
) = (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin