package com.example.coinsdetection

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_selected_history_image.*

class SelectedHistoryImage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_history_image)

        val thumbnail = intent.getIntExtra("Image",-1)
        val objectCount = intent.getIntExtra("Count", -1)
        val totalCount = intent.getDoubleExtra("Total", 0.0)

        selectedHistoryImage.setImageResource(thumbnail)
        totalCountTv.text = "Total: $totalCount"
        totalObjectsTv.text = "Objects: $objectCount"
    }

    fun returnHome(view:View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}