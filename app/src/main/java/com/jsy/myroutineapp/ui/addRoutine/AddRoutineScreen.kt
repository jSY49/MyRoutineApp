package com.jsy.myroutineapp.ui.addRoutine

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.ui.addRoutine.components.CategorySection
import com.jsy.myroutineapp.ui.addRoutine.components.RepeatDaysSection
import com.jsy.myroutineapp.ui.common.CommonButton
import com.jsy.myroutineapp.ui.common.CommonInputBox
import com.jsy.myroutineapp.ui.common.toRepeatText
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OutlineVariant
import com.jsy.myroutineapp.ui.theme.TextTertiary
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

    val viewModel: AddRoutineViewModel = hiltViewModel()

    // 수정 모드: 불러온 루틴 값으로 입력창을 한 번만 채움 (회전 시 입력 중인 값을 덮어쓰지 않도록)
    val editing by viewModel.editing.collectAsStateWithLifecycle()
    var loaded by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(editing) {
        val routine = editing
        if (routine != null && !loaded) {
            routineName = routine.title
            routineCategory = routine.category
            routineRepeatDays = routine.repeatDays
            routineDesc = routine.description
            loaded = true
        }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
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
                        stringResource(
                            if (viewModel.isEdit) R.string.Add_TopBar_title_edit
                            else R.string.Add_TopBar_title
                        ),
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
                                Toast.makeText(
                                    context,
                                    errorMessage,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
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
                .padding(16.dp)
        ) {

            Text(
                text = stringResource(R.string.Add_section_preview_title),
                color = TextTertiary,
                style = Typography.labelSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            RoutineCard(
                category = routineCategory,
                title = routineName.ifBlank { stringResource(R.string.Add_input_title_placeholder) },
                repeatDays = routineRepeatDays.toRepeatText(),
                description = routineDesc.ifBlank { stringResource(R.string.Add_input_goal_placeholder) },
                isChecked = false,
                onCheckedChange = {},
                onClick = {},
                enabled = false
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

            // 요일을 바꾸면 이후 일정부터 적용되고 이전 기록은 유지됨을 안내
            if (viewModel.isEdit && editing != null && routineRepeatDays != editing?.repeatDays) {
                Text(
                    text = stringResource(R.string.Add_edit_repeat_notice),
                    color = TextTertiary,
                    style = Typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

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