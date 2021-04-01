package com.example.coinsdetection

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : AppCompatActivity() {
    private val sharedPrefFile = "darkmodePref"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)

        val sharedDarkValue = sharedPreferences.getBoolean("isdark",false)
        darkModeSwitch.isChecked = sharedDarkValue

        darkModeSwitch.setOnCheckedChangeListener { _, _ ->
            val editor:SharedPreferences.Editor =  sharedPreferences.edit()
            if (darkModeSwitch.isChecked) {
                editor.putBoolean("isdark",true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                editor.putBoolean("isdark",false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            editor.apply()
            editor.commit()
        }
    }

    fun selectedPage(view: View){
        when(view.id){
            R.id.homeBtn ->{
                finish()
            }
            R.id.cameraBtn -> {
                val intent = Intent(applicationContext, DetectionResults::class.java).apply {
                    putExtra("selected", "camera").toString()
                    putExtra("name", 1)
                }
                startActivity(intent)
            }
            R.id.galleryBtn -> {
                val intent = Intent(applicationContext, DetectionResults::class.java).apply {
                    putExtra("selected", "gallery").toString()
                }
                startActivity(intent)
            }
            R.id.settingsBtn -> {
            }
        }
    }
}