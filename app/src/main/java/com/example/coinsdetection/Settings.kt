package com.example.coinsdetection

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : AppCompatActivity() {
    private val sharedPrefFile = "settingsPref"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()

        val sharedDarkValue = sharedPreferences.getBoolean("isdark",false)
        val yoloconfidence = sharedPreferences.getInt("confidence",20)
        val confidenceText = sharedPreferences.getString("confidenceText","20")
        val autoSaveImages = sharedPreferences.getBoolean("autosave",true)
        val automaticsaveText = sharedPreferences.getString("autosavetext","Automatic saving is on")
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

    private fun darkModeListener(editor: SharedPreferences.Editor){
        darkModeSwitch.setOnCheckedChangeListener { _, _ ->
            when {
                darkModeSwitch.isChecked -> {
                    editor.putBoolean("isdark",true)
                    editor.putString("darkmodetext","Dark mode is on")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                }
                else -> {
                    editor.putBoolean("isdark",false)
                    editor.putString("darkmodetext","Dark mode is off")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                }
            }
            editor.apply()
            editor.commit()
        }
    }

    private fun settingsConfidenceListener(editor:SharedPreferences.Editor){
        settingsConfidence.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekSettingsValue.text = progress.toString()
                editor.putInt("confidence",progress)
                editor.putString("confidenceText",progress.toString())
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

    private fun autoSaveListener(editor: SharedPreferences.Editor){
        autoSaveSwitch.setOnCheckedChangeListener { _, _ ->
            if (autoSaveSwitch.isChecked) {
                editor.putBoolean("autosave",true)
                editor.putString("autosavetext","Automatic saving is on")
                automaticModeSummaryText.text = "Automatic saving is on"

            } else {
                editor.putBoolean("autosave",false)
                editor.putString("autosavetext","Automatic saving is off")
                automaticModeSummaryText.text = "Automatic saving is off"
            }
            editor.apply()
            editor.commit()
        }
    }

    private fun createNavigationMenu(){
        val x = CircularMenu(this)

        var camera = x.createButtons("Camera")
        var gallery = x.createButtons("Gallery")
        var home = x.createButtons("Home")

        var icon = ImageView(this); // Create an icon
        icon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.cursor));
        icon.setColorFilter(ContextCompat.getColor(this,R.color.white))

        val actionButton = FloatingActionButton.Builder(this)
            .setContentView(icon)
            .build();

        actionButton.layoutParams.height = 200
        actionButton.layoutParams.width = 200
        actionButton.background.setTint(ContextCompat.getColor(this,R.color.custom_blue))


        camera = selectedPage("Camera", camera)
        gallery = selectedPage("Gallery", gallery)
        home = selectedPage("Home", home)


        val actionMenu = FloatingActionMenu.Builder(this)
            .addSubActionView(camera)
            .addSubActionView(gallery)
            .addSubActionView(home)
            .attachTo(actionButton)
            .build()

    }

    private fun selectedPage(action:String, button: SubActionButton): SubActionButton {
        when(action){
            "Camera" -> {
                button.setOnClickListener {
                    val intent = Intent(applicationContext, DetectionResults::class.java).apply {
                        putExtra("selected", "camera").toString()
                        putExtra("name", 1)
                    }
                    startActivity(intent)
                }
            }

            "Gallery" -> {
                button.setOnClickListener {
                    val intent = Intent(applicationContext, DetectionResults::class.java).apply {
                        putExtra("selected", "gallery").toString()
                    }
                    startActivity(intent)
                }
            }
            "Home" -> {
                button.setOnClickListener {
                    finish()
                }
            }
        }
        return button
    }


}