package com.example.coinsdetection

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton
import kotlinx.android.synthetic.main.activity_main.*
import spencerstudios.com.bungeelib.Bungee


class MainActivity : AppCompatActivity() {
    private lateinit var mainAdapter: RecycleViewAdapter
    private val sharedPrefFile = "settingsPref"
    private lateinit var imagesOrder: String
    private var recentsSize = 0
    private var db = DataBaseHandler(this)
    private lateinit var recentImages: MutableList<SavedImages>
    private val floatingMenu = CircularMenu(this)
    private val nav = Navigation(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkDarkMode()
        swiperefresh.setColorSchemeColors(ContextCompat.getColor(this,R.color.custom_blue))
        swiperefresh.setOnRefreshListener {
            sortImages()
            swiperefresh.isRefreshing = false
        }
        imagesOrder = getDataBaseImagesOrder()!!
        recentImages = readImages()
        recentsSize = recentImages.size
        recents.text = "Recents: $recentsSize"
        mainAdapter = RecycleViewAdapter(this, recentImages)
        history_images_rv.layoutManager = GridLayoutManager(this, 4)
        history_images_rv.adapter = mainAdapter
        createNavigationMenu()
        createSecondMenu()
    }

    private fun readImages(): MutableList<SavedImages> {
        return db.readData(imagesOrder)
    }

    private fun getDataBaseImagesOrder(): String? {
        val sharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        return sharedPreferences.getString("databaseorder", "DESC")
    }

    private fun createSecondMenu() {

        val actionButton = nav.getPageOptionsButton(false)
        var sort = CircularMenu.createButtons(floatingMenu, "Sort")
        var delete = CircularMenu.createButtons(floatingMenu, "Delete")
        sort = pageOptions("Sort", sort)
        delete = pageOptions("Delete", delete)

        FloatingActionMenu.Builder(this)
            .addSubActionView(delete)
            .addSubActionView(sort)
            .attachTo(actionButton)
            .setStartAngle(0)
            .build()
    }

    private fun createNavigationMenu() {

        val actionButton = nav.getNavButton()
        var camera = CircularMenu.createButtons(floatingMenu, "Camera")
        var gallery = CircularMenu.createButtons(floatingMenu, "Gallery")
        var settings = CircularMenu.createButtons(floatingMenu, "Settings")

        camera = nav.getNavSubButton("Camera", camera)
        gallery = nav.getNavSubButton("Gallery", gallery)
        settings = nav.getNavSubButton("Settings", settings)

        FloatingActionMenu.Builder(this)
            .addSubActionView(settings)
            .addSubActionView(gallery)
            .addSubActionView(camera)
            .attachTo(actionButton)
            .build()
    }

    private fun pageOptions(action: String, button: SubActionButton): SubActionButton {
        when (action) {
            "Sort" -> {
                button.setOnClickListener {
                    sortImages()
                }
            }
            "Delete" -> {
                button.setOnClickListener {
                    deleteAll()
                }
            }
        }
        return button
    }

    private fun sortImages() = if (recentsSize > 0) {
        val sharedPreferences =
            this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        when (sharedPreferences.getString("databaseorder", "DESC")) {
            "DESC" -> {editor.putString("databaseorder", "ASC")
                      makeText(this, "Sorted images by oldest",Toast.LENGTH_SHORT).show()}
            else -> {editor.putString("databaseorder", "DESC")
                makeText(this, "Sorted images by recents",Toast.LENGTH_SHORT).show()}
        }
        editor.apply()
        editor.commit()
        sortRecents()
    } else {
        makeText(this, "No list to reverse", Toast.LENGTH_SHORT).show()
    }

    private fun deleteAll() {
        if (recentImages.size > 0) {
            val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
            dialogBuilder.setTitle("Delete Everything")
            dialogBuilder.setPositiveButton("Confirm") { _, _ ->
                db.deleteAllData()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
                Bungee.zoom(this)
            }
            dialogBuilder.setNegativeButton("Close") { _, _ -> }
            dialogBuilder.setMessage("Are you sure you want to delete all recent detections ?")
            dialogBuilder.create()
            dialogBuilder.show()
        } else {
            makeText(this, "No images to delete", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sortRecents() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
        Bungee.zoom(this)

    }

    private fun checkDarkMode() {
        val sharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedDarkValue = sharedPreferences.getBoolean("isdark", false)
        if (sharedDarkValue) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}