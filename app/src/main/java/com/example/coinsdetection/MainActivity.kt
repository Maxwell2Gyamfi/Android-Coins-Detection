package com.example.coinsdetection

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton
import kotlinx.android.synthetic.main.activity_main.*
import spencerstudios.com.bungeelib.Bungee


class MainActivity : AppCompatActivity() {
    private lateinit var mainAdapter:RecycleViewAdapter
    private val sharedPrefFile = "settingsPref"
    private lateinit var imagesOrder:String
    private var recentsSize =0
    private var db = DataBaseHandler(this)
    private lateinit var recentImages: MutableList<SavedImages>
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkDarkMode()
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


    private fun createSecondMenu(){
        val x = CircularMenu(this)
        var sort = x.createButtons("Sort")
        var delete = x.createButtons("Delete")

        var icon = ImageView(this); // Create an icon
        icon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.menu));
        icon.setColorFilter(ContextCompat.getColor(this,R.color.white))

        val actionButton = FloatingActionButton.Builder(this)
            .setContentView(icon)
            .setPosition(FloatingActionButton.POSITION_TOP_RIGHT)
            .build();

        actionButton.layoutParams.height = 200
        actionButton.layoutParams.width = 200

        actionButton.background.setTint(ContextCompat.getColor(this,R.color.custom_blue))

        sort = selectedPage("Sort",sort)
        delete  = selectedPage("Delete", delete)

        val actionMenu = FloatingActionMenu.Builder(this)
            .addSubActionView(delete)
            .addSubActionView(sort)
            .attachTo(actionButton)
            .setStartAngle(-230)
            .setEndAngle(200)
            .build()
    }

    private fun createNavigationMenu(){
        val x = CircularMenu(this)

        var camera = x.createButtons("Camera")
        var gallery = x.createButtons("Gallery")
        var settings = x.createButtons("Settings")

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
        settings = selectedPage("Settings", settings)


        val actionMenu = FloatingActionMenu.Builder(this)
            .addSubActionView(camera)
            .addSubActionView(gallery)
            .addSubActionView(settings)
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
           "Settings" -> {
               button.setOnClickListener {
                   val intent = Intent(applicationContext, Settings::class.java)
                   startActivity(intent)
               }
           }

           "Sort" ->{
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

    private fun sortImages(){
        if(recentsSize >0) {
            val sharedPreferences =
                this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            when (sharedPreferences.getString("databaseorder", "DESC")) {
                "DESC" -> editor.putString("databaseorder", "ASC")
                else -> editor.putString("databaseorder", "DESC")
            }
            editor.apply()
            editor.commit()
            sortRecents()
        }
        else{
            Toast.makeText(this, "No list to reverse", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteAll(){
        if(recentImages.size >0) {
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
        }
        else{
            Toast.makeText(this, "No images to delete", Toast.LENGTH_SHORT).show()
        }
    }
    private fun sortRecents(){
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
    private fun checkDarkMode(){
        val sharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedDarkValue = sharedPreferences.getBoolean("isdark", false)
        if(sharedDarkValue) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}