package com.example.coinsdetection

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton
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
    private lateinit var selected:String
    private var currentCost =0.0
    private var currentObjects = 0
    private var action = "none"
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

        drawingPad.selected = selected
        createColours()
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


    private fun createActions(){
        val x = CircularMenu(this)

        var undo = x.createButtons("Undo")
        var complete = x.createButtons("Complete")

        var icon = ImageView(this); // Create an icon
        icon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.menu));
        icon.setColorFilter(ContextCompat.getColor(this,R.color.custom_blue))

        val actionButton = FloatingActionButton.Builder(this)
            .setContentView(icon)
            .setPosition(FloatingActionButton.POSITION_TOP_RIGHT)
            .build();

        actionButton.layoutParams.height = 160
        actionButton.layoutParams.width = 160

        actionButton.background.setTint(Color.TRANSPARENT)

        undo.setOnClickListener {
            drawingPad.undoCoordinate()
        }
        complete.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val mBitmap = getBitmapFromFrameLayout(drawingPad_fl)
                var name: String = "Custom" + UUID.randomUUID().toString()
                val boxes = drawingPad.mPaths.size
                val cost = drawingPad.calculateTotal()
                currentCost += cost
                currentObjects += boxes

                val formatter: NumberFormat = DecimalFormat("#0.00")
                currentCost = formatter.format(currentCost).toDouble()
                when {
                    boxes > 0 -> {
                        db.insertData(
                            SavedImages(0, name, currentObjects, currentCost, mBitmap!!)
                        )
                        action = "success"
                        onBackPressed()
                    }
                    else -> onBackPressed()
                }
            }
        }

        val actionMenu = FloatingActionMenu.Builder(this)
            .addSubActionView(undo)
            .addSubActionView(complete)
            .setStartAngle(-230)
            .setEndAngle(200)
            .attachTo(actionButton)
            .build()
    }

    private fun createColours(){
        val x = CircularMenu(this)
        var teal = x.createColoursMenu("Teal")
        var yellow = x.createColoursMenu("Yellow")
        var green = x.createColoursMenu("Green")
        var red = x.createColoursMenu("Red")
        var blue = x.createColoursMenu("Blue")
        var charlie = x.createColoursMenu("Black")

        teal = colourClicked(teal)
        yellow = colourClicked(yellow)
        green = colourClicked(green)
        red = colourClicked(red)
        blue = colourClicked(blue)
        charlie = colourClicked(charlie)

        var icon = ImageView(this); // Create an icon
        icon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pipette));
        icon.setColorFilter(ContextCompat.getColor(this,R.color.custom_blue))

        val actionButton = FloatingActionButton.Builder(this)
            .setContentView(icon)
            .build();

        actionButton.layoutParams.height = 160
        actionButton.layoutParams.width = 160

        actionButton.background.setTint(Color.TRANSPARENT)

        val actionMenu = FloatingActionMenu.Builder(this)
            .addSubActionView(teal)
            .addSubActionView(yellow)
            .addSubActionView(green)
            .addSubActionView(red)
            .addSubActionView(charlie)
            .addSubActionView(blue)
            .attachTo(actionButton)
            .build()

    }

    private fun colourClicked(button: SubActionButton): SubActionButton {
      button.setOnClickListener {
          val colorTag = button.tag.toString()
          drawingPad.setColor(colorTag)
      }
        return button
    }

    override fun onBackPressed() = when(action){
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

}







