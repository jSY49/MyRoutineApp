package com.jsy.myroutineapp.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 매일 정해진 시간에 루틴 알림이 울리도록 알람을 예약하는 클래스.
 *
 * 호출 시점
 * - 앱 시작 (RoutineApplication.onCreate)
 * - 알람이 울린 직후, 다음 날 알람 재예약 (ReminderReceiver)
 * - 재부팅 직후, 사라진 알람 재예약 (BootReceiver)
 */
@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    /**
     * 다음 알림 시각으로 알람을 예약.
     * 매번 같은 PendingIntent(requestCode 동일)를 쓰기 때문에
     * 여러 번 호출해도 알람이 중복되지 않고 기존 알람이 새 시각으로 교체됨.
     *
     * setAndAllowWhileIdle: 절전(Doze) 상태에서도 울리는 "부정확한" 알람.
     * 정확한 알람(setExact...)과 달리 SCHEDULE_EXACT_ALARM 권한이 필요 없지만
     * 시스템 사정에 따라 몇 분 늦게 울릴 수 있음.
     */
    fun schedule() {
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, // 기기가 잠자기 상태여도 깨워서 실행, 시각은 시스템 시계 기준
            nextTriggerMillis(),
            pendingIntent()
        )
    }

    // 다음에 알림이 울릴 시각(epoch millis)을 계산
    private fun nextTriggerMillis(): Long {
        val now = LocalDateTime.now()
        var trigger = now.toLocalDate().atTime(REMINDER_TIME)
        // 오늘 알림 시각이 이미 지났으면 내일로 넘김
        if (!trigger.isAfter(now)) trigger = trigger.plusDays(1)
        return trigger.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    // 알람이 울릴 때 ReminderReceiver로 브로드캐스트를 보내는 PendingIntent
    private fun pendingIntent(): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            // UPDATE_CURRENT: 같은 PendingIntent가 있으면 내용만 갱신 / IMMUTABLE: Android 12+ 필수
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private companion object {
        // 매일 알림이 울리는 시각
        val REMINDER_TIME: LocalTime = LocalTime.of(9, 0)

        // PendingIntent 식별용 번호. 이 값이 같으면 같은 알람으로 취급됨
        const val REQUEST_CODE = 1000
    }
}
