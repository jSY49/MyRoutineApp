package com.jsy.myroutineapp.ui.today

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jsy.myroutineapp.R
import com.jsy.myroutineapp.data.model.Routine
import com.jsy.myroutineapp.data.model.RoutineCategory
import com.jsy.myroutineapp.ui.common.MultipleDevicePreview
import com.jsy.myroutineapp.ui.common.toRepeatText
import com.jsy.myroutineapp.ui.theme.MyRoutineAppTheme
import com.jsy.myroutineapp.ui.theme.OnPrimary
import com.jsy.myroutineapp.ui.theme.OnSurface
import com.jsy.myroutineapp.ui.theme.OnSurfaceVariant
import com.jsy.myroutineapp.ui.theme.Primary
import com.jsy.myroutineapp.ui.today.components.RoutineCard
import com.jsy.myroutineapp.ui.today.components.TodayAchievementCard
import com.jsy.myroutineapp.ui.today.components.TodayEmptyCard
import com.jsy.myroutineapp.ui.today.components.TodayHeader
import java.time.DayOfWeek
import java.time.LocalDate

// ViewModel/DB를 다루는 화면 (프리뷰 불가)
@Composable
fun TodayScreen(
    onAddRoutineClick: () -> Unit,
    onRoutineClick: (routineId: Long) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val viewModel: TodayViewModel = hiltViewModel()
    var showDeniedDialog by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(!isGranted){
            showDeniedDialog = true
        }
    }

    LaunchedEffect(Unit) {  //컴포저블이 화면에 처음 들어올 때 딱 한번만 실행 하는 블록 코드
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !viewModel.notificationPermissionAsked &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.notificationPermissionAsked = true
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val routines by viewModel.routines.collectAsStateWithLifecycle()
    val records by viewModel.records.collectAsStateWithLifecycle()

    val checkedIds = remember(records) { // 현재 완료 체크한 아이템 아이디 set
        records.map { it.routineId }
            .toSet()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refreshToday()
    }

    TodayContent(
        routines = routines,
        checkedIds = checkedIds,
        onCheckedChange = { routineId, checked ->
            viewModel.setChecked(
                routineId,
                checked
            )
        },
        onRoutineClick = onRoutineClick,
        onAddClick = onAddRoutineClick,
        modifier = modifier
    )

    if (showDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showDeniedDialog = false },
            title = { Text(stringResource(R.string.Today_permission_denied_title)) },
            text = { Text(stringResource(R.string.Today_permission_denied_desc)) },
            confirmButton = {
                TextButton(onClick = {
                    showDeniedDialog = false
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(
                            Settings.EXTRA_APP_PACKAGE,
                            context.packageName
                        )
                    }
                    context.startActivity(intent)
                }) { Text(stringResource(R.string.Today_permission_denied_confirm)) }
            },
            dismissButton = {
                TextButton(onClick = { showDeniedDialog = false }) { Text(stringResource(R.string.Today_permission_denied_dismiss)) }
            }
        )
    }
}

@Composable
private fun TodayContent(
    routines: List<Routine>,
    checkedIds: Set<Long>,
    onCheckedChange: (routineId: Long, checked: Boolean) -> Unit,
    onRoutineClick: (routineId: Long) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 96.dp  //Fab에 가려지지 않게
            )
        ) {
            item {
                TodayHeader()
            }
            item {
                if (routines.isEmpty()) {
                    TodayEmptyCard(onAddClick = onAddClick)
                } else {
                    TodayAchievementCard(
                        total = routines.size,
                        done = routines.count { it.id in checkedIds }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.Today_section_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = OnSurface
                    )
                    Text(
                        text = stringResource(
                            R.string.Today_section_count,
                            routines.size
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceVariant
                    )
                }
            }

            items(
                routines,
                key = { it.id }
            ) { routine ->
                RoutineCard(
                    category = routine.category,
                    title = routine.title,
                    repeatDays = routine.repeatDays.toRepeatText(),
                    description = routine.description,
                    isChecked = routine.id in checkedIds,
                    onCheckedChange = { checked ->
                        onCheckedChange(
                            routine.id,
                            checked
                        )
                    },
                    onClick = { onRoutineClick(routine.id) }
                )
            }
        }
        FloatingActionButton(
            onClick = onAddClick,
            containerColor = Primary,
            contentColor = OnPrimary,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                painterResource(R.drawable.ic_add),
                contentDescription = "루틴 추가"
            )
        }
    }
}

@MultipleDevicePreview
@Composable
private fun TodayContentPreview() {
    MyRoutineAppTheme {
        TodayContent(
            routines = listOf(
                Routine(
                    id = 1,
                    title = "물 2L 마시기",
                    category = RoutineCategory.HEALTH,
                    repeatDays = DayOfWeek.entries.toSet(),
                    description = "",
                    startDate = LocalDate.of(
                        2026,
                        10,
                        1
                    )
                ),
                Routine(
                    id = 2,
                    title = "운동 30분",
                    category = RoutineCategory.EXERCISE,
                    repeatDays = setOf(
                        DayOfWeek.MONDAY,
                        DayOfWeek.WEDNESDAY,
                        DayOfWeek.FRIDAY
                    ),
                    description = "dd",
                    startDate = LocalDate.of(
                        2026,
                        10,
                        1
                    )
                ),
            ),
            checkedIds = setOf(1L),
            onCheckedChange = { _, _ -> },
            onRoutineClick = {},
            onAddClick = {}
        )
    }
}

@MultipleDevicePreview
@Composable
private fun TodayContentEmptyPreview() {
    MyRoutineAppTheme {
        TodayContent(
            routines = emptyList(),
            checkedIds = emptySet(),
            onCheckedChange = { _, _ -> },
            onRoutineClick = {},
            onAddClick = {}
        )
    }
}
