package com.example.coinsdetection

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var db = DataBaseHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         var recentImages = readImages()
         var mainAdapter = RecycleViewAdapter(this, recentImages)
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
           R.id.homeBtn -> Toast.makeText(this, "home clicked", Toast.LENGTH_SHORT).show()
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
           R.id.settingsBtn -> Toast.makeText(this, "settings clicked", Toast.LENGTH_SHORT).show()
       }
    }
}