package com.example.coinsdetection

import android.app.AlertDialog
import android.content.Context
import android.graphics.*
import android.graphics.Paint.Style.FILL
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


data class Coordinates(val startX: Float, val startY: Float, val pointX: Float, val pointY: Float, val color :Int, val text:String)

class Drawing(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private var paintColor: Int = Color.BLACK
    lateinit var selected:String
    private var itemToDraw = "1p"
    private var drawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mCanvasBitmap: Bitmap? = null
    private var drawtext:Paint?=null
    val mPaths = ArrayList<Coordinates>() // ArrayList for Paths
    private val itemsValueslist = ArrayList<Double>()
    var boxes:Int =0
    var pointX = 0f
    var pointY = 0f
    var startX = 0f
    var startY = 0f

    private fun setupPaint() {
// Setup paint with color and stroke styles
        drawPaint = Paint()
        drawtext = Paint()
        drawPaint!!.color = paintColor
        drawPaint!!.isAntiAlias = true
        drawPaint!!.strokeWidth = 5F
        drawPaint!!.style = Paint.Style.STROKE
        drawPaint!!.strokeJoin = Paint.Join.ROUND
        drawPaint!!.strokeCap = Paint.Cap.ROUND
        drawtext!!.color = paintColor
        drawtext!!.strokeWidth = 5F
        drawtext!!.style = FILL
        drawtext!!.strokeJoin = Paint.Join.ROUND
        drawtext!!.strokeCap = Paint.Cap.ROUND
        drawtext!!.textSize = resources.getDimension(R.dimen.myFontSize)
        drawtext!!.textAlign
        drawtext!!.typeface = Typeface.create("Arial",Typeface.BOLD);

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        pointX = event.x
        pointY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = pointX
                startY = pointY
            }
            MotionEvent.ACTION_UP -> {
                stallBoxAdding()
            }

            MotionEvent.ACTION_MOVE -> {
            }
            else -> return false
        }
        // Force a view to draw again
        postInvalidate()
        return true
    }

    private fun stallBoxAdding(){
        if(selected == "All"){
            createDialog()
        }
        else{
            itemToDraw = selected
            saveCoordinates()
        }
    }

    private fun saveCoordinates(){
        val saveCoordinates = Coordinates(startX, startY, pointX, pointY,paintColor,
            "$itemToDraw 1.00"
        )
        mPaths.add(saveCoordinates)
        itemsValueslist.add(getSelectedItemValue())
        resetPosition()
    }

    private fun resetPosition(){
        boxes += 1
        startX = 0F
        startY = 0F
        pointX = 0F
        pointY = 0F
        invalidate()
    }

    fun undoCoordinate(){
        if(mPaths.size >0){
            mPaths.removeLast()
            itemsValueslist.removeLast()
            invalidate()
        }
    }

    fun calculateTotal(): Double {
        return itemsValueslist.sumByDouble { it }
    }

    private fun getSelectedItemValue(): Double {
        return when (itemToDraw) {
            "1p" -> 0.01
            "2p" -> 0.02
            "5p" -> 0.05
            "10p" -> 0.1
            "20p" -> 0.2
            "50p" -> 0.5
            "1P" -> 1.0
            "2P" -> 2.0
            else -> 0.0
        }
    }

    override fun onDraw(canvas: Canvas) {

        for(p in mPaths){
            drawPaint?.color = p.color
            drawtext?.color = p.color
            drawPaint?.let { canvas.drawRect(p.startX, p.startY, p.pointX, p.pointY, it) }
            drawtext?.let { canvas.drawText(p.text, p.startX, p.startY -20, it) }
        }
        if(startX >0.0 && startY>0.0) {
           drawPaint?.color = paintColor
           drawPaint?.let { canvas.drawRect(startX, startY, pointX, pointY, it) }
        }
    }

    fun setColor(newColor: String){
        paintColor = Color.parseColor(newColor)
        drawPaint!!.color = paintColor
        drawtext!!.color = paintColor
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        setupPaint()
    }

    private fun createDialog(){
        var position = 0
        val itemsList = arrayOf("1p","2p","5p","10p","20p","50p","1P","2P")

        val dialogBuilder = AlertDialog.Builder(context,R.style.AlertDialogResults)
        dialogBuilder.setTitle("Choose an item")
        dialogBuilder.setSingleChoiceItems(itemsList,position) { _, i->
            run {
                position = i
                itemToDraw = itemsList[position]
            }
        }
        dialogBuilder.setPositiveButton("Save") { _, _ ->
            saveCoordinates()
        }
        dialogBuilder.setNegativeButton("Cancel"){_,_ ->
            resetPosition()
        }
        dialogBuilder.create()
        dialogBuilder.show()

    }

}