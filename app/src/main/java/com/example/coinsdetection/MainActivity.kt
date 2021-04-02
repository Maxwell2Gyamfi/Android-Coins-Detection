package com.example.coinsdetection

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import spencerstudios.com.bungeelib.Bungee

class MainActivity : AppCompatActivity() {
    private lateinit var mainAdapter:RecycleViewAdapter
    private val sharedPrefFile = "settingsPref"
    private var db = DataBaseHandler(this)
    private lateinit var recentImages: MutableList<SavedImages>
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkDarkMode()
        recentImages = readImages()
        val recentsSize = recentImages.size
        recents.text = "Recents: $recentsSize"
         mainAdapter = RecycleViewAdapter(this, recentImages)
         history_images_rv.layoutManager = GridLayoutManager(this, 4)
         history_images_rv.adapter = mainAdapter
       
    }

    private fun readImages(): MutableList<SavedImages> {
        return db.readData()
    }

    fun selectedPage(view: View){
       when(view.id){
           R.id.homeBtn -> {
           }
           R.id.cameraBtn -> {
               val intent = Intent(applicationContext, DetectionResults::class.java).apply {
                   putExtra("selected", "camera").toString()
                   putExtra("name", 1)
               }
               startActivity(intent)
               Bungee.swipeRight(this)

           }
           R.id.galleryBtn -> {
               val intent = Intent(applicationContext, DetectionResults::class.java).apply {
                   putExtra("selected", "gallery").toString()
               }
               startActivity(intent)
               Bungee.swipeRight(this)


           }
           R.id.settingsBtn -> {
               val intent = Intent(applicationContext, Settings::class.java)
               startActivity(intent)
               Bungee.swipeRight(this)
           }
       }
    }

    fun deleteAll(view: View){

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
            dialogBuilder.setMessage("Are you sure you want to delete all images?")
            dialogBuilder.show()
        }
        else{
            Toast.makeText(this, "No images to delete", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkDarkMode(){
        val sharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedDarkValue = sharedPreferences.getBoolean("isdark", false)
        if(sharedDarkValue) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}