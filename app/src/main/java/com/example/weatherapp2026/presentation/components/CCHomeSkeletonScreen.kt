package com.example.weatherapp2026.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.weatherapp2026.domain.model.CCWeatherType
import com.example.weatherapp2026.presentation.theme.CCTheme
import com.example.weatherapp2026.presentation.theme.CCWeatherTheme

@Composable
fun CCHomeSkeletonScreen() {
    CCWeatherBackground(m_weatherType = CCWeatherType.SUNNY) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CCShimmerBox(size = CCTheme.spacing.iconHeader, shape = CircleShape)
                Spacer(modifier = Modifier.height(CCTheme.spacing.lg))
                CCShimmerBox(width = 110.dp, height = 64.dp)
                Spacer(modifier = Modifier.height(CCTheme.spacing.xs))
                CCShimmerBox(width = 160.dp, height = 20.dp)
                Spacer(modifier = Modifier.height(CCTheme.spacing.sm))
                CCShimmerBox(width = 140.dp, height = 20.dp)
                Spacer(modifier = Modifier.height(CCTheme.spacing.sm))
                CCShimmerBox(width = 200.dp, height = 16.dp)
                Spacer(modifier = Modifier.height(CCTheme.spacing.sm))
                CCShimmerBox(width = 180.dp, height = 16.dp)
            }

            CCShimmerBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = CCTheme.spacing.screenEdge),
                height = 48.dp
            )
            Spacer(modifier = Modifier.height(CCTheme.spacing.xl))
        }
    }
}

@Composable
private fun CCShimmerBox(
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp? = null,
    size: Dp? = null,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(CCTheme.spacing.sm)
) {
    val m_transition = rememberInfiniteTransition(label = "shimmer")
    val m_alpha by m_transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )
    val m_sized = when {
        size != null -> modifier.size(size)
        width != null && height != null -> modifier.width(width).height(height)
        height != null -> modifier.height(height)
        else -> modifier
    }
    Box(modifier = m_sized.clip(shape).background(Color.White.copy(alpha = m_alpha)))
}

@Preview(name = "Home Skeleton", showSystemUi = true)
@Composable
private fun CCHomeSkeletonPreview() {
    CCWeatherTheme { CCHomeSkeletonScreen() }
}
