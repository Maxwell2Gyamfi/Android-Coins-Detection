package com.example.coinsdetection

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment

class ResultsDialog(private val resultsDetection: ResultsDetection) : AppCompatDialogFragment() {
    private lateinit var numItems: TextView
    private lateinit var cost: TextView
    private lateinit var itemName: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialogBuilder = AlertDialog.Builder(context, R.style.AlertDialogResults)
        val inflater: LayoutInflater = activity!!.layoutInflater
        val dialogView = inflater.inflate(R.layout.results_dialog, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setTitle("Detection Results")
        dialogBuilder.setPositiveButton("Close") { _, _ -> }

        numItems = dialogView.findViewById(R.id.objectsDialogTv)
        cost = dialogView.findViewById(R.id.totalDialogTv)
        itemName = dialogView.findViewById(R.id.itemDialogTv)


        numItems.text = "Objects: ${resultsDetection.totalObjects}"
        cost.text = "Total: £ ${resultsDetection.totalCost}"
        itemName.text = "Item: ${resultsDetection.itemName}"

        return dialogBuilder.create()
    }
}