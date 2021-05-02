package com.example.coinsdetection

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton

class CircularMenu(context: Context) {
    var context = context
    private val sharedPrefFile = "settingsPref"
    private var sharedDarkValue:Boolean? = null

    private fun getDarkMode(){
        val sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        sharedDarkValue = sharedPreferences.getBoolean("isdark", false)
    }


    companion object {
        fun createColoursMenu(circularMenu: CircularMenu, action: String): SubActionButton {
            val itemBuilder = SubActionButton.Builder(circularMenu.context as Activity?)
            val blueParams: FrameLayout.LayoutParams =
                FrameLayout.LayoutParams(120, 120)
            itemBuilder.setLayoutParams(blueParams)

            val itemIcon = ImageView(circularMenu.context);
            val button1 = itemBuilder.setContentView(itemIcon).build();

            when (action) {
                "Teal" -> {
                    button1.background.setTint(ContextCompat.getColor(circularMenu.context, R.color.yolo1))
                    button1.tag =
                        "#" + Integer.toHexString(ContextCompat.getColor(circularMenu.context, R.color.yolo1))
                }

                "Yellow" -> {
                    button1.background.setTint(ContextCompat.getColor(circularMenu.context, R.color.yolo2))
                    button1.tag =
                        "#" + Integer.toHexString(ContextCompat.getColor(circularMenu.context, R.color.yolo2))
                }

                "Green" -> {
                    button1.background.setTint(ContextCompat.getColor(circularMenu.context, R.color.yolo3))
                    button1.tag =
                        "#" + Integer.toHexString(ContextCompat.getColor(circularMenu.context, R.color.yolo3))
                }

                "Red" -> {
                    button1.background.setTint(ContextCompat.getColor(circularMenu.context, R.color.yolo4))
                    button1.tag =
                        "#" + Integer.toHexString(ContextCompat.getColor(circularMenu.context, R.color.yolo4))
                }

                "Blue" -> {
                    button1.background.setTint(ContextCompat.getColor(circularMenu.context, R.color.custom_blue))
                    button1.tag =
                        "#" + Integer.toHexString(ContextCompat.getColor(circularMenu.context, R.color.custom_blue))
                }

                "Black" -> {
                    button1.background.setTint(ContextCompat.getColor(circularMenu.context, R.color.black))
                    button1.tag =
                        "#" + Integer.toHexString(ContextCompat.getColor(circularMenu.context, R.color.black))
                }
            }

            return button1
        }

        fun createButtons(circularMenu: CircularMenu, action: String): SubActionButton {
            val itemBuilder = SubActionButton.Builder(circularMenu.context as Activity?)
            val blueParams: FrameLayout.LayoutParams =
                FrameLayout.LayoutParams(160, 160)
            itemBuilder.setLayoutParams(blueParams)
            circularMenu.getDarkMode()
            val itemIcon = ImageView(circularMenu.context);
            var button1: SubActionButton = itemBuilder.setContentView(itemIcon).build();
    
            when {
                !circularMenu.sharedDarkValue!! -> {
                    button1.background.setTint(Color.TRANSPARENT)
//                    button1.background.setTint(ContextCompat.getColor(circularMenu.context, R.color.custom_blue))
                    itemIcon.setColorFilter(ContextCompat.getColor(circularMenu.context, R.color.custom_blue))
                }
                else -> {
                    button1.background.setTint(Color.TRANSPARENT)
                    itemIcon.setColorFilter(ContextCompat.getColor(circularMenu.context, R.color.white))
                }
            }
    
            when (action) {
                "Delete" -> itemIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        circularMenu.context,
                        R.drawable.delete__1_
                    )
                )
    
                "Total" -> itemIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        circularMenu.context,
                        R.drawable.pound
                    )
                )
    
                "SaveToRecents" -> itemIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        circularMenu.context,
                        R.drawable.download__7_
                    )
                )
    
                "SaveToDevice" -> itemIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        circularMenu.context,
                        R.drawable.download__4_
                    )
                )
    
                "Home" -> itemIcon.setImageDrawable(ContextCompat.getDrawable(circularMenu.context, R.drawable.home))
    
                "Settings" -> itemIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        circularMenu.context,
                        R.drawable.settings
                    )
                )
    
                "Camera" -> itemIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        circularMenu.context,
                        R.drawable.camera
                    )
                )
    
                "Gallery" -> itemIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        circularMenu.context,
                        R.drawable.gallery
                    )
                )
    
                "Sort" -> itemIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        circularMenu.context,
                        R.drawable.sort_down__1_
                    )
                )
    
                "Confidence" -> itemIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        circularMenu.context,
                        R.drawable.computer_slider_tool
                    )
                )
    
                "Add" -> itemIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        circularMenu.context,
                        R.drawable.pencil
                    )
                )
    
                "Complete" -> {
                    itemIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            circularMenu.context,
                            R.drawable.check__1_
                        )
                    )
                }
    
                "Undo" -> {
                    itemIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            circularMenu.context,
                            R.drawable.undo__1_
                        )
                    )
                }
                
                "SelectColours" ->{
                    itemIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            circularMenu.context,
                            R.drawable.pipette
                        )
                    )
                }
            }
            itemIcon.layoutParams.height = 75
            itemIcon.layoutParams.width = 75
            return button1
        }
    }

}