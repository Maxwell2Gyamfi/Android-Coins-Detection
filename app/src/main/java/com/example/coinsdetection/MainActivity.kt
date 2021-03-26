package com.example.coinsdetection

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         var historyImages = readImages()
         var mainAdapter: RecycleViewAdapter = RecycleViewAdapter(this, historyImages)
         history_images_rv.layoutManager = GridLayoutManager(this,4)
         history_images_rv.adapter = mainAdapter
    }

    private fun readImages(): MutableList<History> {

        var historyImages: MutableList<History> = ArrayList()
        historyImages.add(History(R.drawable._0p, 2, 1.24))
        historyImages.add(History(R.drawable.detection__1_, 3, 1.24))
        historyImages.add(History(R.drawable._pa, 4, 1.44))
        historyImages.add(History(R.drawable._0p, 2, 1.24))
        historyImages.add(History(R.drawable.detection__1_, 3, 1.64))
        historyImages.add(History(R.drawable._0p, 2, 1.24))
        historyImages.add(History(R.drawable.detection__1_, 3, 1.24))
        historyImages.add(History(R.drawable._pa, 4, 1.44))
        historyImages.add(History(R.drawable._0p, 2, 1.24))
        historyImages.add(History(R.drawable.detection__1_, 3, 1.64))
        historyImages.add(History(R.drawable.detection__1_, 3, 1.24))
        historyImages.add(History(R.drawable._pa, 4, 1.44))
        historyImages.add(History(R.drawable._0p, 2, 1.24))
        historyImages.add(History(R.drawable.detection__1_, 3, 1.64))
        return historyImages
    }

    fun selectedPage(view:View){
       when(view.id){
           R.id.homeBtn -> Toast.makeText(this, "home clicked", Toast.LENGTH_SHORT).show()
           R.id.cameraBtn-> {
               val intent = Intent(this, DetectionResults::class.java)
               startActivity(intent)
           }
           R.id.galleryBtn -> Toast.makeText(this, "gallery clicked", Toast.LENGTH_SHORT).show()
           R.id.settingsBtn -> Toast.makeText(this, "settings clicked", Toast.LENGTH_SHORT).show()
       }
    }
}