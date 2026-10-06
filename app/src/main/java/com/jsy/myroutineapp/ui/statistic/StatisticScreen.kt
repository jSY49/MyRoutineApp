package com.jsy.myroutineapp.ui.statistic

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StatisticScreen(modifier: Modifier = Modifier) {

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
private fun StatisticScreenPreview() {
    StatisticScreen()
}