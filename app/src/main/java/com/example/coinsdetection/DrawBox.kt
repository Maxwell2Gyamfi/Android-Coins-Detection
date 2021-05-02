package com.example.coinsdetection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton
import kotlinx.android.synthetic.main.activity_draw_box.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import spencerstudios.com.bungeelib.Bungee
import java.io.File
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class DrawBox : AppCompatActivity() {
    var mPaths: MutableList<PyObject> = ArrayList()
    private var py = Python.getInstance()
    private var pyobj = py.getModule("detection")
    private lateinit var addBoxImage: Bitmap
    private lateinit var selected: String
    private var currentCost = 0.0
    private var currentObjects = 0
    private var action = "none"
    private var db = DataBaseHandler(this)
    private val floatingMenu = CircularMenu(this)
    private val nav = Navigation(this)

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

        drawingPad.selected = selected
        Companion.createColours(this)
        createActions()
    }

    private suspend fun getBitmapFromFrameLayout(view: View): Bitmap? {
        return withContext(Dispatchers.Default) {
            val returnedBitmap = Bitmap.createBitmap(
                view.width,
                view.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(returnedBitmap)
            val bgDrawable = BitmapDrawable(resources, addBoxImage)
            if (bgDrawable != null) {
                bgDrawable.draw(canvas)
            }
            view.draw(canvas)
            return@withContext returnedBitmap
        }
    }


    private fun createActions() {

        var undo = CircularMenu.createButtons(floatingMenu, "Undo")
        var complete = CircularMenu.createButtons(floatingMenu, "Complete")

        undo = Companion.setPageOptions(this, "undo", undo)
        complete = Companion.setPageOptions(this, "complete", complete)


        val actionButton = nav.getPageOptionsButton(true)
        FloatingActionMenu.Builder(this)
            .addSubActionView(undo)
            .addSubActionView(complete)
            .setStartAngle(0)
            .attachTo(actionButton)
            .build()
    }


    private fun colourClicked(button: SubActionButton): SubActionButton {
        button.setOnClickListener {
            val colorTag = button.tag.toString()
            drawingPad.setColor(colorTag)
            Toast.makeText(this, "Colour selected", Toast.LENGTH_SHORT).show()
        }
        return button
    }

    override fun onBackPressed() = when (action) {
        "success" -> {
            super.onBackPressed()
            Bungee.zoom(this)
        }
        else -> {
            Toast.makeText(this, "No changes applied", Toast.LENGTH_SHORT).show()
            super.onBackPressed()
            Bungee.spin(this)
        }

    }

    companion object {
        private fun setPageOptions(drawBox: DrawBox, curAction: String, button: SubActionButton): SubActionButton {
            when (curAction) {
                "undo" -> button.setOnClickListener {
                    if (drawBox.drawingPad.mPaths.isEmpty()) drawBox.onBackPressed()
                    else drawBox.drawingPad.undoCoordinate()
                }
                "complete" -> button.setOnClickListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        val mBitmap = drawBox.getBitmapFromFrameLayout(drawBox.drawingPad_fl)
                        var name: String = "Custom" + UUID.randomUUID().toString()
                        val boxes = drawBox.drawingPad.mPaths.size
                        val cost = drawBox.drawingPad.calculateTotal()
                        drawBox.currentCost += cost
                        drawBox.currentObjects += boxes

                        val formatter: NumberFormat = DecimalFormat("#0.00")
                        drawBox.currentCost = formatter.format(drawBox.currentCost).toDouble()
                        when {
                            boxes > 0 -> {
                                drawBox.db.insertData(
                                    SavedImages(0, name,
                                        drawBox.currentObjects,
                                        drawBox.currentCost, mBitmap!!)
                                )
                                drawBox.action = "success"
                                drawBox.onBackPressed()
                            }
                            else -> drawBox.onBackPressed()
                        }
                    }
                }
            }

            return button
        }

        private fun createColours(drawBox: DrawBox) {
    
            var teal = CircularMenu.createColoursMenu(drawBox.floatingMenu, "Teal")
            var yellow = CircularMenu.createColoursMenu(drawBox.floatingMenu, "Yellow")
            var green = CircularMenu.createColoursMenu(drawBox.floatingMenu, "Green")
            var red = CircularMenu.createColoursMenu(drawBox.floatingMenu, "Red")
//            var blue = CircularMenu.createColoursMenu(drawBox.floatingMenu, "Blue")
//            var charlie = CircularMenu.createColoursMenu(drawBox.floatingMenu, "Black")
    
            teal = drawBox.colourClicked(teal)
            yellow = drawBox.colourClicked(yellow)
            green = drawBox.colourClicked(green)
            red = drawBox.colourClicked(red)

            var icon = CircularMenu.createButtons(drawBox.floatingMenu, "SelectColours")
    
            val actionButton = drawBox.nav.getColoursNavButton()
    
            FloatingActionMenu.Builder(drawBox)
                .addSubActionView(teal)
                .addSubActionView(yellow)
                .addSubActionView(green)
                .addSubActionView(red)
                .attachTo(actionButton)
                .build()
    
        }
    }

}







