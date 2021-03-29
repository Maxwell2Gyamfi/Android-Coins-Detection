package com.example.coinsdetection

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment

open class ConfidenceDialog(private val pyConfidence:String): AppCompatDialogFragment(){
    private lateinit var listener: ConfidenceDialogListener
    private lateinit var seekBar:SeekBar
    private lateinit var seekVal:TextView
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialogBuilder = AlertDialog.Builder(context, R.style.AlertDialogCustom)
        val inflater: LayoutInflater = activity!!.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_with_confidence, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle("Edit Confidence")
        dialogBuilder.setPositiveButton("Save") { _, _ ->
            listener.applyTexts(seekVal.text.toString())
        }
        dialogBuilder.setNegativeButton("Cancel") { _, _ ->

        }
        seekBar = dialogView.findViewById(R.id.seekConfidence)
        seekVal = dialogView.findViewById(R.id.seekValue)

        seekBar.progress = pyConfidence.toInt()
        seekVal.text = pyConfidence

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekVal.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }
        })

        return dialogBuilder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ConfidenceDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString().toString() +
                        "must implement ConfidenceDialogListener"
            )
        }
    }

    interface ConfidenceDialogListener {
        fun applyTexts(confidence: kotlin.String)
    }

}