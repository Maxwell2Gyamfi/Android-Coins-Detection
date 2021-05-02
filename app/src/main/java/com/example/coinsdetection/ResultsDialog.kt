package com.example.coinsdetection

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat

class ResultsDialog(private val resultsDetection: ResultsDetection) : AppCompatDialogFragment() {
    private lateinit var numItems: TextView
    private lateinit var cost: TextView
    private lateinit var itemName: TextView
    private val sharedPrefFile = "settingsPref"
    private var sharedDarkValue:Boolean? = null

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


        getDarkMode(context!!)
        if(sharedDarkValue === true){
            numItems.background = ContextCompat.getDrawable(context!!,R.drawable.darkmode_border)
            cost.background = ContextCompat.getDrawable(context!!,R.drawable.darkmode_border)
            itemName.background = ContextCompat.getDrawable(context!!,R.drawable.darkmode_border)
            numItems.setTextColor(Color.WHITE)
            cost.setTextColor(Color.WHITE)
            itemName.setTextColor(Color.WHITE)
        }

        return dialogBuilder.create()
    }
    private fun getDarkMode(context: Context){
        val sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        sharedDarkValue = sharedPreferences.getBoolean("isdark", false)
    }
}