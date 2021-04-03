package com.example.coinsdetection

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_selected_history_image.*
import spencerstudios.com.bungeelib.Bungee
import java.io.File
import java.io.OutputStream
import java.util.*

class SelectedHistoryImage : AppCompatActivity() {
    private var db = DataBaseHandler(this)
    private lateinit var thumbnail:Bitmap
    private lateinit var imageName:String
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_history_image)

        val imageID = intent.getIntExtra("ID",-1)
        val objectCount = intent.getIntExtra("Count", -1)
        val totalCount = intent.getDoubleExtra("Total", 0.0)
        imageName = intent.getStringExtra("Name").toString()
        thumbnail = retrieveImage(imageID)
        selectedHistoryImage.setImageBitmap(thumbnail)

        saveDeleteBtn.tag = imageID
        totalCountTv.text = "Total: £ $totalCount"
        totalObjectsTv.text = "Objects: $objectCount"

        saveDeleteBtn.setOnClickListener {
            saveDeleteDialog()
        }
    }

    private fun saveDeleteDialog(){
        var position = 0
        val itemsList = arrayOf("Save to Device","Delete from Recents")

        val dialogBuilder = AlertDialog.Builder(this,R.style.AlertDialogResults)
        dialogBuilder.setTitle("Choose a task")
        dialogBuilder.setSingleChoiceItems(itemsList,position) { _, i->
            run { position = i }
        }
        dialogBuilder.setPositiveButton("Confirm") { _, _ ->
            if(position == 0){
                saveImage(imageName)
            }
            else{
                deleteImage(saveDeleteBtn.tag as Int)
            }
        }
        dialogBuilder.setNegativeButton("Close"){_,_ ->}
        dialogBuilder.create()
        dialogBuilder.show()
    }

    private fun retrieveImage(ID:Int):Bitmap{
        return db.getImage(ID)
    }
    private fun deleteImage(ID:Int){
        db.deleteData(ID)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        Bungee.zoom(this)

    }
    private fun saveImage(fileName:String){
        val stream: OutputStream
        try{
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
                val resolver = contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileName.jpg")
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpg")
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+ File.separator+"Object-Detection")
                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)
                stream = Objects.requireNonNull((imageUri))?.let { resolver.openOutputStream(it) }!!
                thumbnail.compress(Bitmap.CompressFormat.JPEG,100,stream)
                Objects.requireNonNull<OutputStream?>(stream)
                Toast.makeText(this,"Image Saved to Gallery", Toast.LENGTH_SHORT).show()
            }
        }
        catch (e:java.lang.Exception){
            print(e.stackTrace)
            Toast.makeText(this,"Error Saving Image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        Bungee.slideLeft(this)
    }
}