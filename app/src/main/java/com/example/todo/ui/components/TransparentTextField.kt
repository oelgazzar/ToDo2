package com.example.todo.ui.components

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo.ui.theme.AppTheme

@Composable
fun TransparentTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    style: TextStyle? = null,
    focusRequester: FocusRequester? = null,
) {
    val textFieldModifier = if (focusRequester != null) modifier.focusRequester(focusRequester) else modifier

    BasicTextField(
        state = state,
        textStyle = style?.copy(color = MaterialTheme.colorScheme.onSurface)?:
        LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = textFieldModifier,
        decorator = {
            if (state.text.isEmpty()) {
                Text(
                    placeholderText,
                    style = style?: LocalTextStyle.current
                )
            }
            it()
        }
    )
}

@Preview
@Composable
private fun TransparentTextFieldPrev() {
    AppTheme {
        TransparentTextField(rememberTextFieldState())
    }
}