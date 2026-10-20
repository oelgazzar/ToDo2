package com.example.test

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val target_check: ImageVector
  get() {
    if (_target_check != null) {
      return _target_check!!
    }
    _target_check =
      ImageVector.Builder(
          name = "target_check",
          defaultWidth = 24.dp,
          defaultHeight = 24.dp,
          viewportWidth = 24f,
          viewportHeight = 24f,
        )
        .apply {
          path(
            fill = SolidColor(Color.Black),
            fillAlpha = 1f,
            stroke = null,
            strokeAlpha = 1f,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
            pathFillType = PathFillType.Companion.NonZero,
          ) {
            moveTo(12f, 22f)
            quadTo(9.93f, 22f, 8.1f, 21.21f)
            quadTo(6.28f, 20.43f, 4.93f, 19.08f)
            quadTo(3.58f, 17.73f, 2.79f, 15.9f)
            reflectiveQuadTo(2f, 12f)
            quadTo(2f, 9.92f, 2.79f, 8.1f)
            quadTo(3.58f, 6.27f, 4.93f, 4.93f)
            quadTo(6.28f, 3.57f, 8.1f, 2.79f)
            quadTo(9.93f, 2f, 12f, 2f)
            quadToRelative(1.45f, 0f, 2.79f, 0.39f)
            reflectiveQuadTo(17.28f, 3.5f)
            lineTo(15.8f, 4.95f)
            quadTo(14.95f, 4.5f, 14f, 4.25f)
            reflectiveQuadTo(12f, 4f)
            quadTo(8.65f, 4f, 6.33f, 6.32f)
            reflectiveQuadTo(4f, 12f)
            reflectiveQuadToRelative(2.33f, 5.68f)
            reflectiveQuadTo(12f, 20f)
            reflectiveQuadToRelative(5.68f, -2.32f)
            reflectiveQuadTo(20f, 12f)
            quadToRelative(0f, -0.63f, -0.1f, -1.23f)
            reflectiveQuadTo(19.63f, 9.6f)
            lineTo(21.2f, 8.05f)
            quadToRelative(0.4f, 0.93f, 0.6f, 1.91f)
            reflectiveQuadTo(22f, 12f)
            quadToRelative(0f, 2.07f, -0.79f, 3.9f)
            reflectiveQuadToRelative(-2.14f, 3.17f)
            quadToRelative(-1.35f, 1.35f, -3.17f, 2.14f)
            reflectiveQuadTo(12f, 22f)
            close()
            moveToRelative(0f, -4f)
            quadTo(9.5f, 18f, 7.75f, 16.25f)
            reflectiveQuadTo(6f, 12f)
            reflectiveQuadTo(7.75f, 7.75f)
            reflectiveQuadTo(12f, 6f)
            quadToRelative(0.63f, 0f, 1.2f, 0.11f)
            quadToRelative(0.57f, 0.11f, 1.1f, 0.34f)
            lineToRelative(-1.6f, 1.6f)
            quadTo(12.53f, 8f, 12.36f, 8f)
            reflectiveQuadTo(12f, 8f)
            quadTo(10.35f, 8f, 9.18f, 9.17f)
            reflectiveQuadTo(8f, 12f)
            reflectiveQuadToRelative(1.18f, 2.82f)
            reflectiveQuadTo(12f, 16f)
            quadToRelative(0.8f, 0f, 1.54f, -0.31f)
            reflectiveQuadTo(14.85f, 14.8f)
            quadToRelative(0.27f, -0.27f, 0.49f, -0.59f)
            reflectiveQuadTo(15.7f, 13.55f)
            lineToRelative(2.25f, -2.28f)
            quadToRelative(0.18f, 1.38f, -0.27f, 2.69f)
            quadToRelative(-0.45f, 1.31f, -1.43f, 2.29f)
            quadToRelative(-0.8f, 0.8f, -1.9f, 1.27f)
            reflectiveQuadTo(12f, 18f)
            close()
            moveToRelative(0f, -3.6f)
            lineToRelative(-3f, -3f)
            lineTo(10.4f, 10f)
            lineTo(12f, 11.6f)
            lineTo(20.6f, 3f)
            lineTo(22f, 4.4f)
            lineToRelative(-10f, 10f)
            close()
          }
        }
        .build()
    return _target_check!!
  }

private var _target_check: ImageVector? = null
