package com.momentum.app.ui.theme

import androidx.compose.ui.graphics.Color

// Primary: Sage Green — growth, calm, nature
val SageGreen = Color(0xFF7C9E87)
val SageGreenLight = Color(0xFFA8C4AF)
val SageGreenDark = Color(0xFF507A5D)
val SageGreenContainer = Color(0xFF1E3328)

// Secondary: Steel Blue-Gray — focus, clarity
val SteelBlue = Color(0xFF9BA8B0)
val SteelBlueLight = Color(0xFFC4CDD5)
val SteelBlueDark = Color(0xFF6E7D85)

// Tertiary: Warm Sand — balance, rest
val WarmSand = Color(0xFFA89E87)
val WarmSandLight = Color(0xFFC8BEA7)

// Neutral backgrounds — calm dark mode
val Background = Color(0xFF1A1C1E)
val SurfaceContainer = Color(0xFF252729)
val SurfaceContainerHigh = Color(0xFF2F3133)
val SurfaceContainerHighest = Color(0xFF393B3D)

// Text
val OnBackground = Color(0xFFE2E2E5)
val OnSurface = Color(0xFFE2E2E5)
val OnSurfaceMuted = Color(0xFF9BA8B0)
val OnSurfaceSubtle = Color(0xFF8E9BA6)

// States — never alarming red for missed/incomplete
val StateCompleted = SageGreen               // checked / done
val StateMissed = Color(0xFF6B7680)           // missed — neutral gray, not red
val StatePending = Color(0xFF3A3D40)          // not yet due — very subtle

// Mood colors — gentle, muted progression from cool gray-rose to sage green (WCAG AA compliant)
val MoodDifficult = Color(0xFF9E8A87)         // muted rose-gray (Tough day)
val MoodDifficultContainer = Color(0xFF2E2423)
val MoodNeutral = Color(0xFF9BA8B0)           // steel blue-gray (Okay)
val MoodNeutralContainer = Color(0xFF23282C)
val MoodGood = Color(0xFF8FA897)              // sage tint (Good)
val MoodGoodContainer = Color(0xFF212C26)
val MoodGreat = Color(0xFF7C9E87)             // full sage green (Great)
val MoodGreatContainer = Color(0xFF1E3328)

// Entertainment category colors — harmonious, calm family of accents
val CategoryGaming = Color(0xFF9599B3)        // muted slate violet
val CategoryGamingContainer = Color(0xFF262733)
val CategoryYouTube = Color(0xFFBA948D)       // soft terracotta / warm rose
val CategoryYouTubeContainer = Color(0xFF332725)
val CategoryNetflix = Color(0xFFA88B9C)       // warm mauve / dusk
val CategoryNetflixContainer = Color(0xFF2E242B)
val CategorySocial = Color(0xFF85A3AD)        // muted ocean / teal gray
val CategorySocialContainer = Color(0xFF202A2E)
val CategoryOther = Color(0xFFA89E87)         // warm sand / clay
val CategoryOtherContainer = Color(0xFF2E2A23)
val CategoryRest = Color(0xFF7C9E87)          // sage green
val CategoryRestContainer = Color(0xFF1E3328)

// Priority dots — amber for high, gray for medium/low
val PriorityHigh = Color(0xFFD4A853)
val PriorityMedium = Color(0xFF6B7680)
val PriorityLow = Color(0xFF4A5055)

// Error — kept soft, used sparingly
val SoftError = Color(0xFFC08080)
val SoftErrorContainer = Color(0xFF3D2020)
