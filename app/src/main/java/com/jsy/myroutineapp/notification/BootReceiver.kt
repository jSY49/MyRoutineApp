package com.jsy.myroutineapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 기기 재부팅이 끝나면 호출되는 리시버.
 * AlarmManager에 등록한 알람은 재부팅하면 모두 사라지므로,
 * 앱을 다시 열지 않아도 알림이 오도록 여기서 알람을 다시 예약함.
 *
 * AndroidManifest의 RECEIVE_BOOT_COMPLETED 권한과 BOOT_COMPLETED intent-filter 등록이 필요함.
 */
// Hilt로 아래 @Inject 필드를 주입받기 위해 필요
@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject lateinit var reminderScheduler: ReminderScheduler

    override fun onReceive(context: Context, intent: Intent) {
        // 부팅 완료 브로드캐스트일 때만 처리 (다른 액션이 들어오는 경우 방어)
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            reminderScheduler.schedule()
        }
    }
}
