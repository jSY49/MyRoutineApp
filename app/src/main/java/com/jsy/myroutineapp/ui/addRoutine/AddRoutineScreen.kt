package com.jsy.myroutineapp.ui.addRoutine

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import com.jsy.myroutineapp.ui.theme.OutlineVariant
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.RoutineApplication
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.ui.addRoutine.components.CategorySection
import com.jsy.myroutineapp.ui.addRoutine.components.RepeatDaysSection
import com.jsy.myroutineapp.ui.common.CommonButton
import com.jsy.myroutineapp.ui.common.CommonInputBox
import com.jsy.myroutineapp.ui.common.toRepeatText
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.Typography
import com.jsy.myroutineapp.ui.today.components.RoutineCard
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoutineScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {

    var routineName by rememberSaveable { mutableStateOf("") }
    var routineCategory by rememberSaveable { mutableStateOf(RoutineCategory.EXERCISE) }
    var routineRepeatDays by rememberSaveable { mutableStateOf(DayOfWeek.entries.toSet()) }
    var routineDesc by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current
    val appContext = context.applicationContext as RoutineApplication
    val viewModel: AddRoutineViewModel = viewModel(
        factory = viewModelFactory {
            initializer { AddRoutineViewModel(appContext.database.routineDao()) }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = stringResource(R.string.close),
                        )
                    }
                },
                title = {
                    Text(
                        stringResource(R.string.Add_TopBar_title),
                        style = Typography.headlineSmall,
                        color = OnSurface
                    )
                }
            )
        },
        bottomBar = {
            Surface(color = Color.White) {
                Column {
                    HorizontalDivider(color = OutlineVariant)
                    CommonButton(
                        text = stringResource(R.string.Add_save),
                        onClick = {

                            val errorMessage = when {
                                routineName.isBlank() -> R.string.Add_error_title_empty
                                routineRepeatDays.isEmpty() -> R.string.Add_error_days_empty
                                else -> null
                            }
                            if (errorMessage != null) {
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                return@CommonButton
                            }

                            viewModel.save(
                                title = routineName,
                                category = routineCategory,
                                repeatDays = routineRepeatDays,
                                description = routineDesc,
                                onSaved = {
                                    navController.popBackStack()
                                }
                            )
                        },
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(16.dp)
                    )
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(16.dp)
        ) {

            RoutineCard(
                category = routineCategory,
                title = routineName.ifBlank { stringResource(R.string.Add_input_title_placeholder) },
                repeatDays = routineRepeatDays.toRepeatText(),
                description = routineDesc.ifBlank { stringResource(R.string.Add_input_goal_placeholder) },
                isChecked = false,
                onCheckedChange = {},
                onClick = {}
            )

            Spacer(Modifier.size(16.dp))
            CommonInputBox(
                title = stringResource(R.string.Add_input_title),
                value = routineName,
                onValueChange = { routineName = it },
                placeholder = stringResource(R.string.Add_input_title_placeholder),
                maxLength = 20
            )

            Spacer(Modifier.size(16.dp))
            CategorySection(
                selected = routineCategory,
                onSelect = { routineCategory = it }
            )

            Spacer(Modifier.size(24.dp))

            RepeatDaysSection(
                selected = routineRepeatDays,
                onSelect = { day ->
                    routineRepeatDays =
                        if (day in routineRepeatDays) routineRepeatDays - day
                        else routineRepeatDays + day
                },
                onSelectPreset = { routineRepeatDays = it })

            Spacer(Modifier.size(24.dp))

            CommonInputBox(
                title = stringResource(R.string.Add_input_goal_title),
                optionalText = stringResource(R.string.Add_input_goal_desc),
                value = routineDesc,
                onValueChange = { routineDesc = it },
                placeholder = stringResource(R.string.Add_input_goal_placeholder),
                imeAction = ImeAction.Done
            )

        }

    }

}

@Preview(
    name = "Phone",
    widthDp = 360,
    heightDp = 800,
    showBackground = true
)
@Composable
private fun AddRoutineScreenPreview() {
    AddRoutineScreen()
}