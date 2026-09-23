package com.momentum.app.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.momentum.app.R
import com.momentum.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    // Animation states
    var startAnimation by remember { mutableStateOf(false) }

    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logo_scale"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = EaseOutCubic),
        label = "content_alpha"
    )

    val subtitleAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 900, delayMillis = 300, easing = EaseOutCubic),
        label = "subtitle_alpha"
    )

    // Infinite breathing glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "breathing_glow")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_scale"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        // Keep splash visible briefly for an energetic, delightful intro
        delay(1800)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        // Ambient energetic background radial glow
        Box(
            modifier = Modifier
                .size(320.dp)
                .scale(glowScale)
                .alpha(glowAlpha)
                .blur(70.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SageGreen.copy(alpha = 0.45f),
                            WarmSand.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            // Main Logo with Squircle and elevation glow
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(140.dp)
                    .scale(logoScale)
                    .alpha(contentAlpha)
            ) {
                // Outer subtle ring
                Box(
                    modifier = Modifier
                        .size(136.dp)
                        .clip(RoundedCornerShape(34.dp))
                        .background(SurfaceContainerHigh)
                )

                // Logo Image
                Image(
                    painter = painterResource(id = R.drawable.momentum_logo),
                    contentDescription = "Momentum Logo",
                    modifier = Modifier
                        .size(128.dp)
                        .clip(RoundedCornerShape(30.dp))
                )
            }

            Spacer(Modifier.height(28.dp))

            // App Title with energetic modern styling
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.alpha(contentAlpha)
            ) {
                Text(
                    text = "MOMENTUM",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 5.sp,
                    color = OnSurface,
                    fontSize = 28.sp
                )
                Spacer(Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(SageGreen)
                )
            }

            Spacer(Modifier.height(10.dp))

            // Productive & Youthful Tagline
            Text(
                text = "Small steps, every day.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.2.sp,
                color = SageGreenLight,
                modifier = Modifier.alpha(subtitleAlpha)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Progress over perfection",
                style = MaterialTheme.typography.bodySmall,
                letterSpacing = 0.8.sp,
                color = OnSurfaceMuted,
                modifier = Modifier.alpha(subtitleAlpha)
            )
        }

        // Bottom subtle energy dots / indicator
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .alpha(subtitleAlpha),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val dotCount = 3
            for (i in 0 until dotCount) {
                val dotAlpha by infiniteTransition.animateFloat(
                    initialValue = 0.3f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(900, delayMillis = i * 250, easing = EaseInOut),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "dot_alpha_$i"
                )
                Box(
                    modifier = Modifier
                        .size(if (i == 1) 8.dp else 6.dp)
                        .alpha(dotAlpha)
                        .clip(CircleShape)
                        .background(if (i == 1) SageGreen else SteelBlueDark)
                )
            }
        }
    }
}
