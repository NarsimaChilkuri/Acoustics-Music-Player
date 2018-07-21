package com.internshala.acoustics.databases
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.internshala.acoustics.Songs
import com.internshala.acoustics.databases.AcouDatabase.Staticated.COLUMN_ID
import com.internshala.acoustics.databases.AcouDatabase.Staticated.COLUMN_SONG_ARTIST
import com.internshala.acoustics.databases.AcouDatabase.Staticated.COLUMN_SONG_PATH
import com.internshala.acoustics.databases.AcouDatabase.Staticated.COLUMN_SONG_TITLE
import com.internshala.acoustics.databases.AcouDatabase.Staticated.TABLE_NAME


class AcouDatabase : SQLiteOpenHelper {


    var _songList = ArrayList<Songs>()




    object Staticated{
        val TABLE_NAME = "FavoriteTable"
        val COLUMN_ID = "SongID"
        val COLUMN_SONG_TITLE = "SongTitle"
        val COLUMN_SONG_ARTIST = "SongArtist"
        val COLUMN_SONG_PATH = "SongPath"
        var DB_VERSION = 1
        val DB_NAME = "FavoriteDatabase"
    }


    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        sqLiteDatabase?.execSQL("CREATE TABLE " + TABLE_NAME + "( " + COLUMN_ID +
                " INTEGER," + COLUMN_SONG_ARTIST + " STRING," + COLUMN_SONG_TITLE + " STRING,"
                + COLUMN_SONG_PATH + " STRING);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(context, name, factory, version) {}
    constructor(context: Context?) : super(context, Staticated.DB_NAME, null, Staticated.DB_VERSION)
    fun storeAsFavorite(id: Int?, artist: String?, songTitle: String?, path: String?) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_ID, id)
        contentValues.put(COLUMN_SONG_ARTIST, artist)
        contentValues.put(COLUMN_SONG_TITLE, songTitle)
        contentValues.put(COLUMN_SONG_PATH, path)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }


    fun queryDBList(): ArrayList<Songs>? {



        try {
            val db = this.readableDatabase

            val query_params = "SELECT * FROM " + TABLE_NAME
            var cSor = db.rawQuery(query_params, null)

            if (cSor.moveToFirst()) {


                do {

                    var _id = cSor.getInt(cSor.getColumnIndexOrThrow(COLUMN_ID))
                    var _artist = cSor.getString(cSor.getColumnIndexOrThrow(COLUMN_SONG_ARTIST))
                    var _title = cSor.getString(cSor.getColumnIndexOrThrow(COLUMN_SONG_TITLE))
                    var _songPath = cSor.getString(cSor.getColumnIndexOrThrow(COLUMN_SONG_PATH))
                    _songList.add(Songs(_id.toLong(), _title, _artist, _songPath, 0))
                }

                while (cSor.moveToNext())
            }


            else {
                return null
            }
        }


        catch (e: Exception) {
            e.printStackTrace()
        }


        return _songList
    }


    fun checkifIdExists(_id: Int): Boolean {


        var storeId = -1090
        val db = this.readableDatabase


        val query_params = "SELECT * FROM " + TABLE_NAME + " WHERE SongID = '$_id'"
        val cSor = db.rawQuery(query_params, null)
        if (cSor.moveToFirst()) {
            do {

                storeId = cSor.getInt(cSor.getColumnIndexOrThrow(COLUMN_ID))
            } while (cSor.moveToNext())
        } else {
            return false
        }

        return storeId != -1090
    }


    fun deleteFavourite(_id: Int) {
        val db = this.writableDatabase


        db.delete(TABLE_NAME, COLUMN_ID + " = " + _id, null)


        db.close()
    }
    fun checkSize(): Int {
        var counter = 0
        val db = this.readableDatabase
        var query_params = "SELECT * FROM " + TABLE_NAME

        val cSor = db.rawQuery(query_params, null)

        if (cSor.moveToFirst()) {
            do {
                counter = counter + 1
            } while (cSor.moveToNext())
        } else {
            return 0
        }

        return counter
    }
}