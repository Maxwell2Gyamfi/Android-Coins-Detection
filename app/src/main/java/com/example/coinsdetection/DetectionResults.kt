package com.example.coinsdetection

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.chaquo.python.Python
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_detection_results.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import spencerstudios.com.bungeelib.Bungee
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


const val REQUEST_IMAGE_CAPTURE = 1
private lateinit var photofile: File
private lateinit var detectedPhoto: File
private const val FILE_NAME = "photo.jpg"
private const val pickImage = 100
private var imageUri: Uri? = null


data class SavedImages(
    val id: Int,
    val imageName: String,
    val totalItems: Int,
    val totalCost: Double,
    val imageToSave: Bitmap
)

data class ResultsDetection(val itemName: String, val totalObjects: Int, val totalCost: Double)

class DetectionResults : AppCompatActivity(), ConfidenceDialog.ConfidenceDialogListener {
    private lateinit var selectedOption: String
    private lateinit var inferenceBitmap: Bitmap
    private lateinit var selectedBitmap: Bitmap
    private var py = Python.getInstance()
    private var db = DataBaseHandler(this)
    private var pyobj = py.getModule("detection")
    private lateinit var selectedMny: String
    private var totalItems: Int = 0
    private var totalCost: Double = 0.0
    private val sharedPrefFile = "settingsPref"
    private var sharedDarkValue:Boolean? = null
    private lateinit var save: SubActionButton
    private val floatingMenu = CircularMenu(this)
    private val nav = Navigation(this)


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detection_results)
        selectedOption = intent.getStringExtra("selected").toString()
        getYoloConfidence()
        setDefault()
        when (selectedOption) {
            "camera" -> {
                takePicture()
            }
            "gallery" -> {
                openGallery()
            }
        }
        createNavigationMenu()
        createSecondMenu()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun openConfidenceOverlay() {
        val conf = getConfidence()
        var confidenceDialog = ConfidenceDialog(conf)
        confidenceDialog.show(supportFragmentManager, "confidence")
    }

    override fun applyTexts(confidence: String) {
        if (this::inferenceBitmap.isInitialized) {
            setConfidence(confidence)
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageDB(button: SubActionButton?) {
        val sharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val autosave = sharedPreferences.getBoolean("autosave", true)
        var name: String = "Detection" + UUID.randomUUID().toString()
        val imageToSave = SavedImages(0, name, totalItems, totalCost, inferenceBitmap)


        if (autosave) {
            button!!.isEnabled = false
            db.insertData(imageToSave)
        } else {
            button!!.isEnabled = true
            button!!.setOnClickListener {
                var name: String = "Detection" + UUID.randomUUID().toString()
                val imageToSave = SavedImages(0, name, totalItems, totalCost, inferenceBitmap)
                db.insertData(imageToSave)
            }
        }
    }

    private fun showResults() {
        var resultsDetection = ResultsDetection(selectedMny, totalItems, totalCost)
        val resultsDialog = ResultsDialog(resultsDetection)
        resultsDialog.show(supportFragmentManager, "results")

    }

    fun performDetection(view: View) {
        val button = view as AppCompatButton
        val selected = button.text.toString()
        selectedMny = selected
        setDefault()
        applyFocus(view)
        when (resultsImage.drawable) {
            null -> Toast.makeText(this, "No image to perform inference", Toast.LENGTH_SHORT).show()
            else -> initInference(selectedBitmap, selected)
        }
    }

    private fun pyResults(pyString: String) {
        var str = pyString.split("-").toTypedArray()
        totalCost = str[0].toDouble()
        totalItems = str[1].toInt()
    }

    private fun setConfidence(confidence: String) {
        pyobj.callAttr("changeConfidence", confidence)
        initInference(selectedBitmap, selectedMny)
    }

    private fun getConfidence(): String {
        val conf = pyobj.callAttr("getConfidence")
        return conf.toString()
    }

    private fun getYoloConfidence() {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val yoloValue = sharedPreferences.getInt("confidence", 20)
        pyobj.callAttr("changeConfidence", yoloValue)
    }

    private fun initInference(image: Bitmap, selectedItem: String) {
        CoroutineScope(Dispatchers.Main).launch {
            progressBarDetection.visibility = View.VISIBLE
            runInference(image, selectedItem)
            progressBarDetection.visibility = View.INVISIBLE
            val file = File(filesDir, "detection.jpg")
            val detectedImage = BitmapFactory.decodeFile(file.absolutePath)
            inferenceBitmap = detectedImage
            resultsImage.setImageBitmap(detectedImage)
            saveImageDB(save)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun runInference(image: Bitmap, selectedItem: String) {
        return withContext(Dispatchers.Default) {
            val stream = ByteArrayOutputStream()
            val nh = (image.height * (1200.0 / image.width)).toInt()
            var scaled = Bitmap.createScaledBitmap(image, 1200, nh, true)
            scaled.compress(Bitmap.CompressFormat.PNG, 100, stream)
            var byteImage = stream.toByteArray()
            val imageString = Base64.getEncoder().encodeToString(byteImage)
            py = Python.getInstance()
            pyobj = py.getModule("detection")
            val obj = pyobj.callAttr("main", imageString, selectedItem)
            pyResults(obj.toString())
        }
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

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)
    }

    private fun applyFocus(view: AppCompatButton) {

        if(sharedDarkValue === true){
            view.background = ContextCompat.getDrawable(this, R.drawable.selected_border_darkmode)
            view.setTextColor(Color.BLACK)
        }
        else {
            view.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border)
            view.setTextColor(Color.parseColor("#ffffff"))
        }
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

        getDarkMode()
        when {
            sharedDarkValue === true -> {
                for (option in options) {
                    option.background = ContextCompat.getDrawable(this, R.drawable.darkmode_border)
                    option.setTextColor(Color.WHITE)
                }
            }
            else -> for (option in options) {
                option.background =
                    ContextCompat.getDrawable(this, R.drawable.default_option_border)
                option.setTextColor(Color.parseColor("#1665B2"))
            }
        }
    }

    private fun getDarkMode(){
        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        sharedDarkValue = sharedPreferences.getBoolean("isdark", false)
    }


    private fun drawBoundingBox() {
        val intent = Intent(this, DrawBox::class.java)
        intent.putExtra("Selected", "$selectedMny")
        intent.putExtra("Cost", totalCost)
        intent.putExtra("Objects", totalItems)
        startActivity(intent)
        Bungee.zoom(this)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun createSecondMenu() {

        val actionButton = nav.getPageOptionsButton(false)
        var add = CircularMenu.createButtons(floatingMenu, "Add")
        var result = CircularMenu.createButtons(floatingMenu, "Total")
        var confidence = CircularMenu.createButtons(floatingMenu, "Confidence")
        save = CircularMenu.createButtons(floatingMenu, "SaveToRecents")

        add = pageOptions("Add", add)
        result = pageOptions("Total", result)
        confidence = pageOptions("Confidence", confidence)
        save = pageOptions("Save", save)

        FloatingActionMenu.Builder(this)
            .addSubActionView(save)
            .addSubActionView(add)
            .addSubActionView(confidence)
            .addSubActionView(result)
            .attachTo(actionButton)
            .setStartAngle(-0)
            .setEndAngle(-90)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createNavigationMenu() {

        val actionButton = nav.getNavButton()
        var camera = CircularMenu.createButtons(floatingMenu, "Camera")
        var gallery = CircularMenu.createButtons(floatingMenu, "Gallery")
        var settings = CircularMenu.createButtons(floatingMenu, "Settings")
        var home = CircularMenu.createButtons(floatingMenu, "Home")

        camera = nav.getNavSubButton("Camera", camera)
        gallery = nav.getNavSubButton("Gallery", gallery)
        settings = nav.getNavSubButton("Settings", settings)
        home = nav.getNavSubButton("Home", home)


        FloatingActionMenu.Builder(this)
            .addSubActionView(settings)
            .addSubActionView(home)
            .addSubActionView(gallery)
            .addSubActionView(camera)
            .attachTo(actionButton)
            .build()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun pageOptions(action: String, button: SubActionButton): SubActionButton {

        when (action) {

            "Save" -> button.setOnClickListener {
                saveImageDB(button)
            }

            "Total" -> button.setOnClickListener {
                showResults()
            }

            "Add" -> button.setOnClickListener {
                drawBoundingBox()
            }

            "Confidence" -> button.setOnClickListener {
                openConfidenceOverlay()
            }
        }

        return button
    }

    override fun onBackPressed() {
        super.onBackPressed()
        returnHome()
    }

    private fun returnHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    private fun getPhotoFile(fileName: String): File {
        var storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }


    private fun launchCropping(uri: Uri) =
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).start(this)

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Bungee.zoom(this)
                val takenImage = BitmapFactory.decodeFile(photofile.absolutePath)
                selectedBitmap = takenImage
                resultsImage.setImageBitmap(takenImage)
                setDefault()
                applyFocus(all)
                selectedMny = "All"
                initInference(selectedBitmap, "All")
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
        } else if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            val result = CropImage.getActivityResult(data)
            when {
                resultCode === RESULT_OK -> {
                    Bungee.zoom(this)
                    val resultUri = result.uri
                    val source = resultUri?.let { it1 ->
                        ImageDecoder.createSource(
                            this.contentResolver,
                            it1
                        )
                    }
                    val bitmap = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
                    resultsImage.setImageBitmap(bitmap)
                    selectedBitmap = bitmap!!
                    selectedMny = "All"
                    initInference(bitmap, "All")
                }
                else -> {
                    finish()

                }
            }
        } else {
            finish()

        }
    }


}