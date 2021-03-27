package com.example.coinsdetection

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment

open class ConfidenceDialog: AppCompatDialogFragment(){
    private lateinit var listener: ConfidenceDialogListener
    private lateinit var confidence:EditText
    private lateinit var seekBar:SeekBar
    private lateinit var seekVal:TextView
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialogBuilder = AlertDialog.Builder(context)
        val inflater: LayoutInflater = activity!!.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_with_confidence, null)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setTitle("Edit Confidence")
        dialogBuilder.setPositiveButton("Save") { _, _ ->
            Toast.makeText(context, seekVal.text.toString(), Toast.LENGTH_SHORT).show()
            listener.applyTexts(seekVal.text.toString())
        }
        dialogBuilder.setNegativeButton("Cancel") { _, _ ->

        }
        seekBar = dialogView.findViewById(R.id.seekConfidence)
        seekVal = dialogView.findViewById(R.id.seekValue)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var progressChangedValue = 0
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                progressChangedValue = progress
                seekVal.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Toast.makeText(activity, "Seek bar progress is :$progressChangedValue",
                    Toast.LENGTH_SHORT).show();
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