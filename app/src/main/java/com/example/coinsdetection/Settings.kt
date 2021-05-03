package com.example.coinsdetection

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : AppCompatActivity() {
    private val sharedPrefFile = "settingsPref"
    private val floatingMenu = CircularMenu(this)
    private val nav = Navigation(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        val sharedDarkValue = sharedPreferences.getBoolean("isdark", false)
        val yoloconfidence = sharedPreferences.getInt("confidence", 20)
        val confidenceText = sharedPreferences.getString("confidenceText", "20")
        val autoSaveImages = sharedPreferences.getBoolean("autosave", true)
        val automaticsaveText =
            sharedPreferences.getString("autosavetext", "Automatic saving is on")
        val darkmodeText = sharedPreferences.getString("darkmodetext", "Dark mode is off")

        darkModeSwitch.isChecked = sharedDarkValue
        autoSaveSwitch.isChecked = autoSaveImages
        settingsConfidence.progress = yoloconfidence
        seekSettingsValue.text = confidenceText
        automaticModeSummaryText.text = automaticsaveText
        nightModeSummaryText.text = darkmodeText

        darkModeListener(editor)
        settingsConfidenceListener(editor)
        autoSaveListener(editor)
        createNavigationMenu()

    }

    private fun darkModeListener(editor: SharedPreferences.Editor) {
        darkModeSwitch.setOnCheckedChangeListener { _, _ ->
            when {
                darkModeSwitch.isChecked -> {
                    editor.putBoolean("isdark", true)
                    editor.putString("darkmodetext", "Dark mode is on")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                }
                else -> {
                    editor.putBoolean("isdark", false)
                    editor.putString("darkmodetext", "Dark mode is off")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                }
            }
            editor.apply()
            editor.commit()
        }
    }

    private fun settingsConfidenceListener(editor: SharedPreferences.Editor) {
        settingsConfidence.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekSettingsValue.text = progress.toString()
                editor.putInt("confidence", progress)
                editor.putString("confidenceText", progress.toString())
                editor.apply()
                editor.commit()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }
        })
    }

    private fun autoSaveListener(editor: SharedPreferences.Editor) {
        autoSaveSwitch.setOnCheckedChangeListener { _, _ ->
            if (autoSaveSwitch.isChecked) {
                editor.putBoolean("autosave", true)
                editor.putString("autosavetext", "Automatic saving is on")
                automaticModeSummaryText.text = "Automatic saving is on"

            } else {
                editor.putBoolean("autosave", false)
                editor.putString("autosavetext", "Automatic saving is off")
                automaticModeSummaryText.text = "Automatic saving is off"
            }
            editor.apply()
            editor.commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        returnHome()
    }

    private fun returnHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createNavigationMenu() {

        val actionButton = nav.getNavButton()
        var camera = CircularMenu.createButtons(floatingMenu, "Camera")
        var gallery = CircularMenu.createButtons(floatingMenu, "Gallery")
        var home = CircularMenu.createButtons(floatingMenu, "Home")

        camera = Navigation.getNavSubButton(nav, "Camera", camera)
        gallery = Navigation.getNavSubButton(nav, "Gallery", gallery)
        home = Navigation.getNavSubButton(nav, "Home", home)

        FloatingActionMenu.Builder(this)
            .addSubActionView(home)
            .addSubActionView(gallery)
            .addSubActionView(camera)
            .attachTo(actionButton)
            .build()
    }

}