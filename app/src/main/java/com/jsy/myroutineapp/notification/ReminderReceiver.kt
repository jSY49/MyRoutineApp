package com.jsy.myroutineapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jsy.myroutineapp.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * ReminderScheduler가 예약한 알람이 울릴 때 호출되는 리시버.
 * 알림을 띄운 뒤 다음 날 알람을 다시 예약해서 매일 반복되게 함.
 *
 * 알람은 한 번 울리면 사라지는 일회성이라, 반복하려면 울릴 때마다 재예약이 필요함.
 */
// Hilt로 아래 @Inject 필드를 주입받기 위해 필요
@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject lateinit var notificationHelper: NotificationHelper
    @Inject lateinit var reminderScheduler: ReminderScheduler

    override fun onReceive(context: Context, intent: Intent) {
        // 알림 발송 (알림 권한이 없으면 NotificationHelper가 조용히 건너뜀)
        notificationHelper.showRoutineNotification(
            notificationId = NOTIFICATION_ID,
            title = context.getString(R.string.Notification_routine_title),
            text = context.getString(R.string.Notification_routine_text)
        )
        // 다음 날 같은 시각으로 재예약 (nextTriggerMillis가 오늘이 지났으면 내일로 계산)
        reminderScheduler.schedule()
    }

    private companion object {
        // 같은 ID로 보내면 이전에 안 지운 알림을 덮어써서 알림이 쌓이지 않음
        const val NOTIFICATION_ID = 1
    }
}
