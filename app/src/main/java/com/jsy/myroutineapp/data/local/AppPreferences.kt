package com.jsy.myroutineapp.data.local

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// 앱 공용 SharedPreferences 래퍼. 키는 PrefKeys에 정의해서 사용
@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    fun getBoolean(key: String, default: Boolean = false): Boolean =
        prefs.getBoolean(key, default)

    fun putBoolean(key: String, value: Boolean) = prefs.edit { putBoolean(key, value) }

    fun getInt(key: String, default: Int = 0): Int = prefs.getInt(key, default)

    fun putInt(key: String, value: Int) = prefs.edit { putInt(key, value) }

    fun getLong(key: String, default: Long = 0L): Long = prefs.getLong(key, default)

    fun putLong(key: String, value: Long) = prefs.edit { putLong(key, value) }

    fun getString(key: String, default: String? = null): String? = prefs.getString(key, default)

    fun putString(key: String, value: String?) = prefs.edit { putString(key, value) }

    fun remove(key: String) = prefs.edit { remove(key) }

    private companion object {
        const val PREFS_NAME = "app_prefs"
    }
}
