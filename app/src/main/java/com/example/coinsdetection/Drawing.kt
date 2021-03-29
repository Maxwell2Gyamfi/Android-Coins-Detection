package com.example.coinsdetection

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

data class Coordinates(val startX:Float,val startY:Float, val pointX:Float,val pointY:Float)

class Drawing(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private val paintColor: Int = Color.BLACK
    private var drawPaint: Paint? = null
    var pointX = 0f
    var pointY = 0f
    var startX = 0f
    var startY = 0f
    private val mPaths = ArrayList<Coordinates>() // ArrayList for Paths
    private val undoPaths =  ArrayList<Coordinates>()
    private fun setupPaint() {
// Setup paint with color and stroke styles
        Paint().also { this.drawPaint = it }
        this.drawPaint!!.color = paintColor
        drawPaint!!.isAntiAlias = true
        drawPaint!!.strokeWidth = 5F
        drawPaint!!.style = Paint.Style.STROKE
        drawPaint!!.strokeJoin = Paint.Join.ROUND
        drawPaint!!.strokeCap = Paint.Cap.ROUND
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
                val saveCoordinates =  Coordinates(startX,startY,pointX,pointY)
                mPaths.add(saveCoordinates)
                startX = 0F
                startY = 0F
                pointX = 0F
                pointY = 0F
            }

            MotionEvent.ACTION_MOVE -> {
            }
            else -> return false
        }
        // Force a view to draw again
        postInvalidate()
        return true
    }

    fun undoCoordinate(){

        if(mPaths.size >0){
            mPaths.removeLast()
            invalidate()
        }
    }


    fun setupDrawing(){

    }

    override fun onDraw(canvas: Canvas) {

        for(p in mPaths){
            drawPaint?.let { canvas.drawRect(p.startX, p.startY, p.pointX, p.pointY, it) }
        }
        if(startX >0.0 && startY>0.0) {
           drawPaint?.let { canvas.drawRect(startX, startY, pointX, pointY, it) }
        }
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        setupPaint()
    }
}