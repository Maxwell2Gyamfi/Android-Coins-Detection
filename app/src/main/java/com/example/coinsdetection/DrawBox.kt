package com.example.coinsdetection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import spencerstudios.com.bungeelib.Bungee
import java.io.ByteArrayOutputStream
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class DrawBox : AppCompatActivity() {
    var mPaths: MutableList<PyObject> = ArrayList()
    private var py = Python.getInstance()
    private var pyobj = py.getModule("detection")
    private lateinit var addBoxImage: Bitmap
    private lateinit var selected:String
    private var currentCost =0.0
    private var currentObjects = 0

    private var mCurrentPaint: ImageButton? = null
    private var db = DataBaseHandler(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selected = intent.getStringExtra("Selected")!!
        currentCost = intent.getDoubleExtra("Cost", 0.0)
        currentObjects = intent.getIntExtra("Objects", 0)

        setContentView(R.layout.activity_draw_box)
        val file = File(filesDir, "detection.jpg")

        addBoxImage = BitmapFactory.decodeFile(file.absolutePath)
        drawBoxImage.setImageBitmap(addBoxImage)
        mCurrentPaint = colorPalette[4] as ImageButton
        mCurrentPaint!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.palette_selected))

        drawingPad.selected = selected

        undoBox.setOnClickListener {
            drawingPad.undoCoordinate()
        }

        updateButton.setOnClickListener(
                fun(_: View) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val mBitmap = getBitmapFromFrameLayout(drawingPad_fl)
                        var name: String = "Custom" + UUID.randomUUID().toString()
                        val boxes = drawingPad.mPaths.size
                        val cost = getMoneyValue(
                                boxes,
                                selected,
                        )
                        currentCost += cost!!
                        currentObjects += boxes

                        val df = DecimalFormat("#.##")
                        RoundingMode.CEILING.also { df.roundingMode = it }
                        df.format(currentCost)
                        when {
                            boxes > 0 -> {
                                db.insertData(SavedImages(0, name, currentObjects, currentCost, mBitmap!!))
                                returnHome()
                            }
                            else -> discardChanges()
                        }
                    }
                },
        )

    }

    fun paintClicked(view: View) {
        if (view !== mCurrentPaint) {
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingPad.setColor(colorTag)
            imageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.palette_selected))
            mCurrentPaint!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.palette_normal))
            mCurrentPaint = view
        }
    }

    private suspend fun getBitmapFromFrameLayout(view:View): Bitmap? {
        return withContext(Dispatchers.Default) {
            val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(returnedBitmap)
            val bgDrawable = BitmapDrawable(resources, addBoxImage)
            if (bgDrawable != null) {
                bgDrawable.draw(canvas)
            }

            view.draw(canvas)
            return@withContext returnedBitmap
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addBoxInference(mPaths: MutableList<PyObject>) {

        val height = drawingPad.height.toString()
        val width = drawingPad.width.toString()

        val stream = ByteArrayOutputStream()
        addBoxImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        var byteImage = stream.toByteArray()
        val imageString = Base64.getEncoder().encodeToString(byteImage)

        pyobj.callAttr("drawBoxes", mPaths, imageString, "All", width, height)
    }

    private fun getMoneyValue(count: Int, moneyStr: String): Double? {
        return when (moneyStr) {
            "1p" -> count * 0.01
            "2p" -> count * 0.02
            "5p" -> count * 0.05
            "10p" -> count * 0.1
            "20p" -> count * 0.2
            "50p" -> count * 0.5
            "1P" -> count * 1.0
            "2P" -> count * 2.0
            else -> null
        }
    }
    private fun returnHome() {
        onBackPressed()
        Bungee.zoom(this)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Bungee.spin(this)

    }

    private fun discardChanges(){
        Toast.makeText(this,"Discarded changes", Toast.LENGTH_SHORT).show()
        onBackPressed()
        Bungee.spin(this)
    }
}







