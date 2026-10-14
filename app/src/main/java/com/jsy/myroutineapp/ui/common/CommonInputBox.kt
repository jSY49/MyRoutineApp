package com.jsy.myroutineapp.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.OutlineVariant
import com.jsy.myroutineapp.ui.theme.Primary
import com.jsy.myroutineapp.ui.theme.Typography

@Composable
fun CommonInputBox(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    optionalText: String? = null,
    maxLength: Int? = null,
    singleLine: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
) {
    Column(modifier = modifier.fillMaxWidth()) {

        // Title
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = title, style = Typography.labelLarge, color = OnSurface)
            if (optionalText != null) {
                Spacer(Modifier.width(6.dp))
                Text(text = optionalText, style = Typography.bodySmall, color = OnSurfaceVariant)
            }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = { new ->
                if (maxLength == null || new.length <= maxLength) onValueChange(new)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = placeholder, color = OnSurfaceVariant) },
            singleLine = singleLine,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(imeAction = imeAction),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = OutlineVariant,
            ),
            supportingText = if (maxLength != null) {
                {
                    Text(
                        text = "${value.length}/$maxLength",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            } else null,
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun CommonInputBoxPreview() {
    var text by remember { mutableStateOf("") }
    CommonInputBox(
        title = "루틴 이름",
        value = text,
        onValueChange = { text = it },
        placeholder = "물 마시기",
        maxLength = 20
    )
}
