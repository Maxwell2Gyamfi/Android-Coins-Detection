package com.example.coinsdetection

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_selected_history_image.*

class SelectedHistoryImage : AppCompatActivity() {
    private var db = DataBaseHandler(this)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_history_image)

        val imageID = intent.getIntExtra("ID",-1)
        val objectCount = intent.getIntExtra("Count", -1)
        val totalCount = intent.getDoubleExtra("Total", 0.0)
        val thumbnail = retrieveImage(imageID)
        selectedHistoryImage.setImageBitmap(thumbnail)

        deleteBtn.tag = imageID
        totalCountTv.text = "Total: £ $totalCount"
        totalObjectsTv.text = "Objects: $objectCount"

        deleteBtn.setOnClickListener{
            deleteImage(deleteBtn.tag as Int)
        }
    }

    fun returnHome(view:View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun retrieveImage(ID:Int):Bitmap{
        return db.getImage(ID)
    }
    private fun deleteImage(ID:Int){
        db.deleteData(ID)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}