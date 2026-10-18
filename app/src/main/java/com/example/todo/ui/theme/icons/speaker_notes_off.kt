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
public val speaker_notes_off: ImageVector
  get() {
    if (_speaker_notes_off != null) {
      return _speaker_notes_off!!
    }
    _speaker_notes_off =
      ImageVector.Builder(
          name = "speaker_notes_off",
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
            moveTo(6.29f, 13.71f)
            quadTo(6f, 13.43f, 6f, 13f)
            reflectiveQuadTo(6.29f, 12.29f)
            reflectiveQuadTo(7f, 12f)
            reflectiveQuadToRelative(0.71f, 0.29f)
            reflectiveQuadTo(8f, 13f)
            reflectiveQuadTo(7.71f, 13.71f)
            reflectiveQuadTo(7f, 14f)
            quadTo(6.58f, 14f, 6.29f, 13.71f)
            close()
            moveTo(20.7f, 17.85f)
            lineTo(18.85f, 16f)
            horizontalLineTo(20f)
            verticalLineTo(4f)
            horizontalLineTo(6.85f)
            lineToRelative(-2f, -2f)
            horizontalLineTo(20f)
            quadToRelative(0.83f, 0f, 1.41f, 0.59f)
            reflectiveQuadTo(22f, 4f)
            verticalLineTo(16f)
            quadToRelative(0f, 0.65f, -0.36f, 1.14f)
            reflectiveQuadTo(20.7f, 17.85f)
            close()
            moveTo(13.85f, 11f)
            lineToRelative(-2f, -2f)
            horizontalLineTo(18f)
            verticalLineToRelative(2f)
            horizontalLineTo(13.85f)
            close()
            moveTo(20.5f, 23.3f)
            lineTo(15.15f, 18f)
            horizontalLineTo(6f)
            lineTo(2f, 22f)
            verticalLineTo(4.8f)
            lineTo(0.7f, 3.5f)
            lineTo(2.1f, 2.1f)
            lineTo(21.9f, 21.9f)
            lineToRelative(-1.4f, 1.4f)
            close()
            moveTo(8.6f, 11.4f)
            close()
            moveTo(12.85f, 10f)
            close()
            moveTo(6.29f, 10.71f)
            quadTo(6f, 10.43f, 6f, 10f)
            quadTo(6f, 9.57f, 6.29f, 9.29f)
            reflectiveQuadTo(7f, 9f)
            reflectiveQuadTo(7.71f, 9.29f)
            reflectiveQuadTo(8f, 10f)
            reflectiveQuadTo(7.71f, 10.71f)
            reflectiveQuadTo(7f, 11f)
            quadTo(6.58f, 11f, 6.29f, 10.71f)
            close()
            moveTo(10.85f, 8f)
            lineTo(10f, 7.15f)
            verticalLineTo(6f)
            horizontalLineToRelative(8f)
            verticalLineTo(8f)
            horizontalLineTo(10.85f)
            close()
            moveTo(4f, 6.8f)
            verticalLineTo(17.13f)
            lineTo(5.15f, 16f)
            horizontalLineTo(13.2f)
            lineTo(4f, 6.8f)
            close()
          }
        }
        .build()
    return _speaker_notes_off!!
  }

private var _speaker_notes_off: ImageVector? = null
