package com.example.coinsdetection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python
import kotlinx.android.synthetic.main.activity_draw_box.*
import java.io.File


class DrawBox : AppCompatActivity() {
    private var mPaths = ArrayList<Coordinates>()
    private var py = Python.getInstance()
    private lateinit var addBoxImage:Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_box)
        val file = File(filesDir, "detection.jpg")
        addBoxImage = BitmapFactory.decodeFile(file.absolutePath)
        drawBoxImage.setImageBitmap(addBoxImage)

        undoBox.setOnClickListener {
            drawingPad.undoCoordinate()
        }
        updateButton.setOnClickListener {
            mPaths = drawingPad.mPaths
            addBoxInference(mPaths)
        }

        drawBoxImage.setOnClickListener {
            var numBoxes = drawingPad.boxes
            addedLenght.text = "Added: $numBoxes"
            //TODO
        }
    }

    private fun addBoxInference(mPaths: ArrayList<Coordinates>) {
        val file = File(filesDir, "detection.jpg")
        Toast.makeText(this, mPaths.size.toString(), Toast.LENGTH_SHORT).show()
    }


}







