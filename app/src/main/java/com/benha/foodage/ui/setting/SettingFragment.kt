package com.benha.foodage.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.archico.core.utils.DarkMode
import com.benha.foodage.R
import java.util.Locale

class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        activity?.title = getString(R.string.title_setting)
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        setupDarkModePreference()
    }

    private fun setupDarkModePreference() {
        val darkModePref = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        darkModePref?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                val selectedMode = mapToDarkMode(newValue as String)
                applyDarkMode(selectedMode.value)
                true
            }
    }

    private fun mapToDarkMode(value: String): DarkMode {
        return when (value.uppercase(Locale.US)) {
            DarkMode.ON.name -> DarkMode.ON
            DarkMode.OFF.name -> DarkMode.OFF
            else -> DarkMode.FOLLOW_SYSTEM
        }
    }

    private fun applyDarkMode(mode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(mode)
        requireActivity().recreate()
        return true
    }
}
