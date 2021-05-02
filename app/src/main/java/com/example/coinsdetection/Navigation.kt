package com.example.coinsdetection

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton
import spencerstudios.com.bungeelib.Bungee

class Navigation(context: Context) {
    private var context = context
    private val sharedPrefFile = "settingsPref"
    private var sharedDarkValue:Boolean? = null
    fun getNavSubButton(action: String, button: SubActionButton): SubActionButton {
        when (action) {
            "Camera" -> {
                button.setOnClickListener {
                    val intent = Intent(context, DetectionResults::class.java).apply {
                        putExtra("selected", "camera").toString()
                        putExtra("name", 1)
                    }
                    context.startActivity(intent)
                    Bungee.zoom(context)
                    (context as Activity).finish()
                }
            }

            "Gallery" -> {
                button.setOnClickListener {
                    val intent = Intent(context, DetectionResults::class.java).apply {
                        putExtra("selected", "gallery").toString()
                    }
                    context.startActivity(intent)
                    Bungee.zoom(context)
                    (context as Activity).finish()
                }
            }
            "Settings" -> {
                button.setOnClickListener {
                    val intent = Intent(context, Settings::class.java)
                    context.startActivity(intent)
                    Bungee.zoom(context)
                    (context as Activity).finish()
                }
            }

            "Home" -> {
                button.setOnClickListener {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                    Bungee.zoom(context)
                    (context as Activity).finish()
                }
            }
        }
        return button
    }

    private fun getDarkMode(){
        val sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        sharedDarkValue = sharedPreferences.getBoolean("isdark", false)
    }


    fun getColoursNavButton(): FloatingActionButton?{
        var icon = ImageView(context); // Create an icon
        icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.pipette));
        getDarkMode()

        when {
            !sharedDarkValue!! -> {
                icon.setColorFilter(ContextCompat.getColor(context, R.color.custom_blue))
            }
            else -> {
                icon.setColorFilter(ContextCompat.getColor(context, R.color.white))
            }
        }

        val actionButton = FloatingActionButton.Builder(context as Activity?)
            .setContentView(icon)
            .build();

        actionButton.layoutParams.height = 160
        actionButton.layoutParams.width = 160
        actionButton.background.setTint(Color.TRANSPARENT)

        return actionButton
    }

    fun getNavButton(): FloatingActionButton? {
        var icon = ImageView(context); // Create an icon
        icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cursor));
        getDarkMode()

        when {
            !sharedDarkValue!! -> {
                icon.setColorFilter(ContextCompat.getColor(context, R.color.custom_blue))
            }
            else -> {
                icon.setColorFilter(ContextCompat.getColor(context, R.color.white))
            }
        }

        val actionButton = FloatingActionButton.Builder(context as Activity?)
            .setContentView(icon)
            .build();

        actionButton.layoutParams.height = 160
        actionButton.layoutParams.width = 160
        actionButton.background.setTint(Color.TRANSPARENT)

        return actionButton
    }

    fun getPageOptionsButton(draw: Boolean): FloatingActionButton? {
        var icon = ImageView(context); // Create an icon
        if (draw) icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.exit))
        else icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.menu))

        getDarkMode()

        when {
            !sharedDarkValue!! -> {
                icon.setColorFilter(ContextCompat.getColor(context, R.color.custom_blue))
            }
            else -> {
                icon.setColorFilter(ContextCompat.getColor(context, R.color.white))
            }
        }

        val actionButton = FloatingActionButton.Builder(context as Activity?)
            .setContentView(icon)
            .setPosition(FloatingActionButton.POSITION_BOTTOM_LEFT)
            .build()

        actionButton.layoutParams.height = 160
        actionButton.layoutParams.width = 160
        actionButton.background.setTint(Color.TRANSPARENT)

        return actionButton
    }
}