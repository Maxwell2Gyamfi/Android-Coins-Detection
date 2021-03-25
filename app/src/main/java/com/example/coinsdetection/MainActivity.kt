package com.example.coinsdetection

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

         var historyImages: MutableList<History> = ArrayList()
         historyImages.add(History(R.drawable._0p,2,3))
         historyImages.add(History(R.drawable.detection__1_,3,4))
         historyImages.add(History(R.drawable._pa,4,5))
         historyImages.add(History(R.drawable._0p,2,3))
         historyImages.add(History(R.drawable.detection__1_,3,4))
         historyImages.add(History(R.drawable._pa,4,5))
         historyImages.add(History(R.drawable._0p,2,3))
         historyImages.add(History(R.drawable.detection__1_,3,4))
         historyImages.add(History(R.drawable._pa,4,5))
         historyImages.add(History(R.drawable._0p,2,3))
         historyImages.add(History(R.drawable.detection__1_,3,4))
         historyImages.add(History(R.drawable._pa,4,5))
         historyImages.add(History(R.drawable._0p,2,3))
         historyImages.add(History(R.drawable.detection__1_,3,4))
         historyImages.add(History(R.drawable._pa,4,5))
         historyImages.add(History(R.drawable._0p,2,3))
         historyImages.add(History(R.drawable.detection__1_,3,4))
         historyImages.add(History(R.drawable._pa,4,5))
         historyImages.add(History(R.drawable._0p,2,3))
         historyImages.add(History(R.drawable.detection__1_,3,4))
         historyImages.add(History(R.drawable._pa,4,5))
         historyImages.add(History(R.drawable._0p,2,3))
         historyImages.add(History(R.drawable.detection__1_,3,4))
         historyImages.add(History(R.drawable._pa,4,5))

         var mainAdapter: RecycleViewAdapter = RecycleViewAdapter(this, historyImages)
         history_images_rv.layoutManager = GridLayoutManager(this,4)
         history_images_rv.adapter = mainAdapter

    }

    fun selectedPage(view:View){
       when(view.id){
           R.id.homeBtn -> Toast.makeText(this, "home clicked", Toast.LENGTH_SHORT).show()
           R.id.cameraBtn-> Toast.makeText(this, "camera clicked", Toast.LENGTH_SHORT).show()
           R.id.galleryBtn -> Toast.makeText(this, "gallery clicked", Toast.LENGTH_SHORT).show()
           R.id.settingsBtn -> Toast.makeText(this, "settings clicked", Toast.LENGTH_SHORT).show()
       }
    }
}