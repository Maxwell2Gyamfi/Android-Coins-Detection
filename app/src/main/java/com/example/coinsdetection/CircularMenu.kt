package com.example.coinsdetection

import android.app.Activity
import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton

class CircularMenu(context: Context) {
    var context = context

    fun createButtons(action: String): SubActionButton {
        val itemBuilder = SubActionButton.Builder(context as Activity?)
        val blueParams: FrameLayout.LayoutParams =
            FrameLayout.LayoutParams(160, 160)
        itemBuilder.setLayoutParams(blueParams)

        val itemIcon = ImageView(context);
        var button1: SubActionButton = itemBuilder.setContentView(itemIcon).build();
        button1.background.setTint(ContextCompat.getColor(context,R.color.custom_blue))
        itemIcon.setColorFilter(ContextCompat.getColor(context,R.color.white))


        when (action) {
            "Delete" -> itemIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.delete
                )
            )

            "Total" -> itemIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.pound
                )
            )

            "Save" -> itemIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.download__6_
                )
            )

            "Home" -> itemIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.home))

            "Settings" -> itemIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.settings
                )
            )

            "Camera" -> itemIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.camera
                )
            )

            "Gallery" -> itemIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.gallery
                )
            )

            "Sort" -> itemIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.sort_down__1_
                )
            )

            "Confidence" -> itemIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.computer_slider_tool
                )
            )

            "Add" -> itemIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.pencil
                )
            )

            "Complete" -> {itemIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.check__1_
                )
            )
                button1.background.setTint(ContextCompat.getColor(context,R.color.custom_blue))
            }

            "Undo" -> {itemIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.undo__1_
                )
            )
                button1.background.setTint(ContextCompat.getColor(context,R.color.charliePink))
            }
        }
        itemIcon.layoutParams.height = 75
        itemIcon.layoutParams.width = 75
        return button1
    }

    fun createColoursMenu(action: String): SubActionButton {
        val itemBuilder = SubActionButton.Builder(context as Activity?)
        val blueParams: FrameLayout.LayoutParams =
            FrameLayout.LayoutParams(100, 100)
        itemBuilder.setLayoutParams(blueParams)

        val itemIcon = ImageView(context);
        val button1 = itemBuilder.setContentView(itemIcon).build();



        when (action) {
            "Teal" -> {
                button1.background.setTint(ContextCompat.getColor(context, R.color.yolo1))
                button1.tag =
                    "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.yolo1))
            }

            "Yellow" -> {
                button1.background.setTint(ContextCompat.getColor(context, R.color.yolo2))
                button1.tag =
                    "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.yolo2))
            }

            "Green" -> {
                button1.background.setTint(ContextCompat.getColor(context, R.color.yolo3))
                button1.tag =
                    "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.yolo3))
            }

            "Red" -> {
                button1.background.setTint(ContextCompat.getColor(context, R.color.yolo4))
                button1.tag =
                    "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.yolo4))
            }

            "Blue" -> {
                button1.background.setTint(ContextCompat.getColor(context, R.color.custom_blue))
                button1.tag =
                    "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.custom_blue))
            }

            "Black" -> {
                button1.background.setTint(ContextCompat.getColor(context, R.color.black))
                button1.tag =
                    "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.black))
            }
        }

        return button1
    }

}