package com.example.coinsdetection

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_detection_results.*
import java.io.File


const val REQUEST_IMAGE_CAPTURE = 1
private lateinit var photofile: File
private lateinit var detectedPhoto: File
private const val FILE_NAME = "photo.jpg"
private const val pickImage = 100
private var imageUri: Uri? = null


class DetectionResults : AppCompatActivity() {
    private lateinit var selectedOption: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detection_results)

        selectedOption = intent.getStringExtra("selected").toString()

        when (selectedOption) {
            "camera" -> takePicture()
            "gallery" -> openGallery()
        }
    }

    fun redo(view: View) {
        when(selectedOption){
            "camera" -> takePicture()
            "gallery" -> openGallery()
        }
    }

    fun goHome(view: View) {
        returnHome()
    }

    fun openResultsOverlay(view: View) {}

    fun performDetection(view: View) {
        val button = view as AppCompatButton
        val selected = button.text
        setDefault()
        applyFocus(view)
    }

    private fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photofile = getPhotoFile(FILE_NAME)
        val fileProvider = FileProvider.getUriForFile(
                this,
                "com.example.coinsdetection.fileprovider",
                photofile
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
        }
    }

    private fun openGallery(){
       val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
       startActivityForResult(gallery, pickImage)
    }

    private fun applyFocus(view: AppCompatButton) {
        view.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border)
        view.setTextColor(Color.parseColor("#ffffff"))
    }

    private fun setDefault() {
        var options = ArrayList<AppCompatButton>()
        options.add(all)
        options.add(onePenny)
        options.add(twoPence)
        options.add(fivePence)
        options.add(tenPence)
        options.add(twentyPence)
        options.add(fiftyPence)
        options.add(onePound)
        options.add(twoPound)

        for (option in options) {
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border)
            option.setTextColor(Color.parseColor("#1665B2"))
        }
    }

    private fun returnHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity((intent))
    }

    private fun getPhotoFile(fileName: String): File {
        var storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    private fun launchCropping(uri:Uri) =
            CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).start(this)

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                val takenImage = BitmapFactory.decodeFile(photofile.absolutePath)
                resultsImage.setImageBitmap(takenImage)
                setDefault()
                applyFocus(all)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (requestCode == pickImage && resultCode == RESULT_OK) {
            data?.data.also { imageUri = it }
            try {
                imageUri.let(
                        fun(_: Uri?) = if (Build.VERSION.SDK_INT < 28) {
                            imageUri?.let { launchCropping(it) }
                        } else {
                            imageUri?.let { launchCropping(it) }
                        },
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        else if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode === RESULT_OK) {
                val resultUri = result.uri
                val source = resultUri?.let { it1 -> ImageDecoder.createSource(this.contentResolver, it1) }
                val bitmap = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                resultsImage.setImageBitmap(bitmap)
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }

        else returnHome()
    }
}