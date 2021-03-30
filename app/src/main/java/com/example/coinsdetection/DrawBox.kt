package com.example.coinsdetection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import kotlinx.android.synthetic.main.activity_draw_box.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class DrawBox : AppCompatActivity() {
    var mPaths: MutableList<PyObject> = ArrayList()
    private var py = Python.getInstance()
    private var pyobj = py.getModule("detection")
    private lateinit var addBoxImage:Bitmap
    private var mCurrentPaint:ImageButton? = null

    @RequiresApi(Build.VERSION_CODES.O)
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
            mPaths = drawingPad.params
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addBoxInference(mPaths: MutableList<PyObject>) {

        val stream = ByteArrayOutputStream()
        addBoxImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        var byteImage = stream.toByteArray()
        val imageString = Base64.getEncoder().encodeToString(byteImage)

        Toast.makeText(this, mPaths.size.toString(), Toast.LENGTH_SHORT).show()
//        val params: MutableList<PyObject> = ArrayList()

//        for(item in mPaths){
//            params.add(PyObject.fromJava(item))
//        }
//        params.toTypedArray()

        pyobj.callAttr("drawBoxes", mPaths, imageString, "All")
    }


}







