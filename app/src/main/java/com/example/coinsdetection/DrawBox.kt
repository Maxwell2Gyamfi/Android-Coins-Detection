package com.example.coinsdetection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_draw_box.*


class DrawBox : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_box)

        undoBox.setOnClickListener {
            drawingPad.undoCoordinate()
        }
    }
}

