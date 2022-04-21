package com.example.translation.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.translation.MainActivity
import com.example.translation.R

class SettingFragment : PreferenceFragmentCompat() {
    lateinit var mainActivity: MainActivity
    lateinit var prefs : SharedPreferences
    var borderthickPreference : Preference? = null
    var borderstylePreference : Preference? = null
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preference,rootKey)

        (activity as MainActivity).hideFloatingActionButton()

        mainActivity = context as MainActivity
        borderthickPreference = findPreference("border_thick")
        borderstylePreference = findPreference("border_style")

        prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity)

        if(prefs.getString("border_thick", "") != ""){
            borderthickPreference?.summary = prefs.getString("border_thick","1px")
        }

        if(prefs.getString("border_style","") != ""){
            borderstylePreference?.summary = prefs.getString("border_style","solid")
        }
    }

    private val prefListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, s ->
        when(s){
            "border_thick" -> {
                val summary = prefs.getString("border_thick","1px")
                borderthickPreference?.summary = summary
            }
            "border_style" -> {
                val summary = prefs.getString("border_style","solid")
                borderstylePreference?.summary = summary
            }
        }
    }

    override fun onResume() {
        super.onResume()
        prefs.registerOnSharedPreferenceChangeListener(prefListener)
    }

    override fun onPause() {
        super.onPause()
        prefs.unregisterOnSharedPreferenceChangeListener(prefListener)
    }

    /*
    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        if(preference == listPreference){
            listPreference.summary = newValue.toString()
        }
        else if(preference == listPreference2){
            listPreference2.summary = newValue.toString()
        }
        return true
    }
     */

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showFloatingActionButton()
    }
}