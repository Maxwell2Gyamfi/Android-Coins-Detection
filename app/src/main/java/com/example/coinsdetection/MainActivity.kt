package com.example.coinsdetection

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainAdapter:RecycleViewAdapter
    private val sharedPrefFile = "darkmodePref"
    private var db = DataBaseHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)

        val sharedDarkValue = sharedPreferences.getBoolean("isdark",false)

        if(sharedDarkValue){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        var recentImages = readImages()
         mainAdapter = RecycleViewAdapter(this, recentImages)
         history_images_rv.layoutManager = GridLayoutManager(this, 4)
         history_images_rv.adapter = mainAdapter

    }

    private fun readImages(): MutableList<SavedImages> {

        var imagesDB: MutableList<SavedImages> = db.readData()
        db.close()
        return  imagesDB
    }

    fun selectedPage(view: View){
       when(view.id){
           R.id.homeBtn -> {}
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
               val intent = Intent(applicationContext, Settings::class.java)
               startActivity(intent)
           }
       }
    }

    fun deleteAll(view:View){
        val dialogBuilder = AlertDialog.Builder(this, R.style.AlertDialogCustom)

        dialogBuilder.setTitle("Delete Everything")
        dialogBuilder.setPositiveButton("Confirm") { _, _ ->
            db.deleteAllData()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        dialogBuilder.setNegativeButton("Close"){_,_ ->}
        dialogBuilder.setMessage("Are you sure you want to delete all images?")
        dialogBuilder.show()
    }
}