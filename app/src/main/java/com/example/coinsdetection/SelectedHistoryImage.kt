package com.example.coinsdetection

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton
import kotlinx.android.synthetic.main.activity_selected_history_image.*
import spencerstudios.com.bungeelib.Bungee
import java.io.File
import java.io.OutputStream
import java.util.*


class SelectedHistoryImage : AppCompatActivity() {
    private var db = DataBaseHandler(this)
    private lateinit var thumbnail:Bitmap
    private lateinit var imageName:String
    private var totalCount = 0.0
    private var objectCount =0
    private var imageID = 0
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_history_image)

        imageID = intent.getIntExtra("ID", -1)
        objectCount = intent.getIntExtra("Count", -1)
        totalCount = intent.getDoubleExtra("Total", 0.0)
        imageName = intent.getStringExtra("Name").toString()
        thumbnail = retrieveImage(imageID)
        selectedHistoryImage.setImageBitmap(thumbnail)
        createFAB()
    }



    private fun showResults(){
        var resultsDetection = ResultsDetection("All", objectCount, totalCount)
        val resultsDialog = ResultsDialog(resultsDetection)
        resultsDialog.show(supportFragmentManager, "results")
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createFAB(){

        var icon = ImageView(this); // Create an icon
        icon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.menu));
        icon.setColorFilter(ContextCompat.getColor(this,R.color.white))
        val actionButton = FloatingActionButton.Builder(this)
            .setContentView(icon)
            .setPosition(FloatingActionButton.POSITION_TOP_RIGHT)
            .build();

        actionButton.layoutParams.height = 160
        actionButton.layoutParams.width = 160

        actionButton.background.setTint(ContextCompat.getColor(this,R.color.custom_blue))

        val x = CircularMenu(this)

        var save = x.createButtons("Save")
        var delete = x.createButtons("Delete")
        var total = x.createButtons("Total")

        save = selectedAction("Save",save)
        delete = selectedAction("Delete", delete)
        total = selectedAction("Total",total)

        val actionMenu = FloatingActionMenu.Builder(this)
            .addSubActionView(save)
            .addSubActionView(delete) // ...
            .addSubActionView(total)
            .setStartAngle(-250)
            .setEndAngle(200)
            .attachTo(actionButton)
            .build()
    }


    private fun selectedAction(action: String, button: SubActionButton): SubActionButton {

        when(action){
            "Save" -> button.setOnClickListener {
                saveImage(imageName)
            }
            "Total" -> button.setOnClickListener {
                showResults()
            }
            "Delete" -> button.setOnClickListener{
                deleteImage(imageID)
            }
        }
        return button
    }

    private fun retrieveImage(ID: Int):Bitmap{
        return db.getImage(ID)
    }
    private fun deleteImage(ID: Int){
        db.deleteData(ID)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        Bungee.zoom(this)

    }
    private fun saveImage(fileName: String){
        val stream: OutputStream
        try{
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
                val resolver = contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.jpg")
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + File.separator + "Object-Detection"
                )
                val imageUri = resolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )
                stream = Objects.requireNonNull((imageUri))?.let { resolver.openOutputStream(it) }!!
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                Objects.requireNonNull<OutputStream?>(stream)
                Toast.makeText(this, "Image Saved to Gallery", Toast.LENGTH_SHORT).show()
            }
        }
        catch (e: java.lang.Exception){
            print(e.stackTrace)
            Toast.makeText(this, "Error Saving Image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}


