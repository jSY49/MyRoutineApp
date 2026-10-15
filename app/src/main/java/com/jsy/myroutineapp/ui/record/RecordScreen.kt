package com.jsy.myroutineapp.ui.record

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsy.myroutineapp.ui.record.components.RecordHeader

@Composable
fun RecordScreen(modifier: Modifier = Modifier) {

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            RecordHeader()
        }
    }
}

@Preview(
    name = "Phone",
    device = "spec:width=360dp,height=800dp",
    showBackground = true,
    showSystemUi = true
)
@Preview(
    name = "Phone_flip",
    device = "spec:width=360dp,height=880dp",
    showBackground = true,
    showSystemUi = true
)
@Preview(
    name = "Tablet",
    device = "spec:width=1280dp,height=800dp",
    showBackground = true,
    showSystemUi = true
)
@Preview(
    name = "Foldable",
    device = "spec:width=673dp,height=841dp",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun RecordScreenPreview() {
    RecordScreen()
}