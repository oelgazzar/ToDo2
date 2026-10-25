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
public val school: ImageVector
  get() {
    if (_school != null) {
      return _school!!
    }
    _school =
      ImageVector.Builder(
          name = "school",
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
            moveTo(21f, 17f)
            verticalLineTo(10.1f)
            lineTo(12f, 15f)
            lineTo(1f, 9f)
            lineTo(12f, 3f)
            lineTo(23f, 9f)
            verticalLineToRelative(8f)
            horizontalLineTo(21f)
            close()
            moveToRelative(-9f, 4f)
            lineTo(5f, 17.2f)
            verticalLineToRelative(-5f)
            lineTo(12f, 16f)
            lineToRelative(7f, -3.8f)
            verticalLineToRelative(5f)
            lineTo(12f, 21f)
            close()
          }
        }
        .build()
    return _school!!
  }

private var _school: ImageVector? = null
