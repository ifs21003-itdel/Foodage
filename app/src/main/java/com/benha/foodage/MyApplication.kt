package com.benha.foodage

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.archico.core.utils.DarkMode
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
open class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        preferences.getString(
            getString(R.string.pref_key_dark),
            getString(R.string.pref_dark_follow_system)
        )?.apply {
            val mode = DarkMode.valueOf(this.uppercase(Locale.US))
            AppCompatDelegate.setDefaultNightMode(mode.value)
        }
    }
}