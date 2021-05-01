package com.example.coinsdetection

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import java.io.ByteArrayOutputStream

const val DATABASENAME = "MY DATABASE"
const val TABLENAME = "savedImages"
const val COL_NAME = "imageName"
const val COL_OBJECTS = "totalObjects"
const val COL_COST = "totalCost"
const val COL_IMAGE = "imageToSave"
const val COL_ID = "id"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(
    context, DATABASENAME, null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLENAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NAME VARCHAR(256), $COL_OBJECTS INTEGER, $COL_COST REAL, $COL_IMAGE BLOB)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db?.execSQL("DROP TABLE IF EXISTS $TABLENAME");
            db?.execSQL("DROP TABLE IF EXISTS $TABLENAME");
            onCreate(db);
        }
    }

    fun insertData(image: SavedImages) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, image.imageName)
        contentValues.put(COL_OBJECTS, image.totalItems)
        contentValues.put(COL_COST, image.totalCost)

        val bitmapImage: Bitmap = image.imageToSave
        val stream = ByteArrayOutputStream()
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        var byteImage = stream.toByteArray()

        contentValues.put(COL_IMAGE, byteImage)

        when (database.insert(TABLENAME, null, contentValues)) {
            (0).toLong() -> {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, "Saved image to recents", Toast.LENGTH_SHORT).show()
            }
        }
        database.close()

    }

    fun readData(order: String): MutableList<SavedImages> {
        val list: MutableList<SavedImages> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME ORDER BY $COL_ID $order"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) do {
            val id = result.getString(result.getColumnIndex(COL_ID)).toInt()
            val imagename = result.getString(result.getColumnIndex(COL_NAME))
            val totalObjects = result.getInt(result.getColumnIndex(COL_OBJECTS))
            val totalCost = result.getDouble(result.getColumnIndex(COL_COST))
            var image: ByteArray = result.getBlob(result.getColumnIndex(COL_IMAGE))
            val bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
            val retrievedImage = SavedImages(id, imagename, totalObjects, totalCost, bitmap)
            list.add(retrievedImage)
        } while (result.moveToNext())
        db.close()
        return list
    }

    fun getImage(imageID: Int): Bitmap {
        val db = this.readableDatabase
        val query = "Select $COL_IMAGE from $TABLENAME WHERE $COL_ID = $imageID"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        var image: ByteArray = result.getBlob(result.getColumnIndex(COL_IMAGE))
        db.close()
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }

    fun deleteData(imageID: Int) {
        val db = this.readableDatabase
        val query = "Delete from $TABLENAME WHERE $COL_ID = $imageID"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        db.close()
        Toast.makeText(context, "Successfully deleted image", Toast.LENGTH_SHORT).show()
    }

    fun deleteAllData() {
        val db = this.readableDatabase
        val query = "Delete from $TABLENAME"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        db.close()
    }
}