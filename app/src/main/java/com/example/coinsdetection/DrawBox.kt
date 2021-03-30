package com.example.coinsdetection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.chaquo.python.Python
import kotlinx.android.synthetic.main.activity_draw_box.*
import java.io.File


class DrawBox : AppCompatActivity() {
    private var mPaths = ArrayList<Coordinates>()
    private var py = Python.getInstance()
    private lateinit var addBoxImage:Bitmap
    private var mCurrentPaint:ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_box)
        val file = File(filesDir, "detection.jpg")
        addBoxImage = BitmapFactory.decodeFile(file.absolutePath)
        drawBoxImage.setImageBitmap(addBoxImage)
        mCurrentPaint = colorPalette[1] as ImageButton
        mCurrentPaint!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.palette_selected))

        undoBox.setOnClickListener {
            drawingPad.undoCoordinate()
        }
        updateButton.setOnClickListener {
            mPaths = drawingPad.mPaths
            addBoxInference(mPaths)
        }

    }

    fun paintClicked(view: View){
        if(view!==mCurrentPaint){
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingPad.setColor(colorTag)
            imageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.palette_selected))
            mCurrentPaint!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.palette_normal))
            mCurrentPaint = view
        }
    }

    private fun addBoxInference(mPaths: ArrayList<Coordinates>) {
        val file = File(filesDir, "detection.jpg")
        Toast.makeText(this, mPaths.size.toString(), Toast.LENGTH_SHORT).show()
    }


}







