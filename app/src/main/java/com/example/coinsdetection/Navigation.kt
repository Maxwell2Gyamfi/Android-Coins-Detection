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
                }
            }

            "Gallery" -> {
                button.setOnClickListener {
                    val intent = Intent(context, DetectionResults::class.java).apply {
                        putExtra("selected", "gallery").toString()
                    }
                    context.startActivity(intent)
                    Bungee.zoom(context)
                }
            }
            "Settings" -> {
                button.setOnClickListener {
                    val intent = Intent(context, Settings::class.java)
                    context.startActivity(intent)
                    (context as Activity).finish()
                    Bungee.zoom(context)
                }
            }

            "Home" -> {
                button.setOnClickListener {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    context.startActivity(intent)
                    Bungee.zoom(context)
                }
            }
        }
        return button
    }

    fun getNavButton(): FloatingActionButton? {
        var icon = ImageView(context); // Create an icon
        icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cursor));
        icon.setColorFilter(ContextCompat.getColor(context, R.color.custom_blue))

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
        icon.setColorFilter(ContextCompat.getColor(context, R.color.custom_blue))

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