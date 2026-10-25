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
public val flight: ImageVector
  get() {
    if (_flight != null) {
      return _flight!!
    }
    _flight =
      ImageVector.Builder(
          name = "flight",
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
            moveTo(7f, 22f)
            verticalLineTo(19.5f)
            lineToRelative(3f, -2.1f)
            verticalLineTo(13.8f)
            lineTo(2f, 17f)
            verticalLineTo(14f)
            lineTo(10f, 8.4f)
            verticalLineTo(4f)
            quadTo(10f, 3.17f, 10.59f, 2.59f)
            reflectiveQuadTo(12f, 2f)
            reflectiveQuadToRelative(1.41f, 0.59f)
            reflectiveQuadTo(14f, 4f)
            verticalLineTo(8.4f)
            lineTo(22f, 14f)
            verticalLineToRelative(3f)
            lineTo(14f, 13.8f)
            verticalLineToRelative(3.6f)
            lineToRelative(3f, 2.1f)
            verticalLineTo(22f)
            lineTo(12f, 20.5f)
            lineTo(7f, 22f)
            close()
          }
        }
        .build()
    return _flight!!
  }

private var _flight: ImageVector? = null
