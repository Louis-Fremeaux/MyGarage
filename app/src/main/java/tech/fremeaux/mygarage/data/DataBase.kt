package tech.fremeaux.mygarage.data

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val CarTableName = "car"
const val MakeTableName = "make"
const val ModelTableName = "model"
class DataBase(context: Context) : SQLiteOpenHelper(context, "app.db", null, 17){
    override fun onCreate(db: SQLiteDatabase?)
    {
        db?.execSQL("CREATE TABLE $CarTableName (id INTEGER PRIMARY KEY AUTOINCREMENT, make TEXT, model TEXT, hp INTEGER, nm INTEGER, color LONG, year TEXT, fuel TEXT, drive TEXT, transmission TEXT)")
        db?.execSQL("CREATE TABLE $MakeTableName (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)")
        db?.execSQL("CREATE TABLE $ModelTableName (id INTEGER PRIMARY KEY AUTOINCREMENT, makeId INTEGER, name TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, old: Int, new: Int)
    {
        db?.execSQL("DROP TABLE IF EXISTS $CarTableName")
        db?.execSQL("DROP TABLE IF EXISTS $MakeTableName")
        db?.execSQL("DROP TABLE IF EXISTS $ModelTableName")
        onCreate(db)
    }
}

@SuppressLint("DefaultLocale")
fun getDatabaseSize(context: Context): String {
    val dbFile = context.getDatabasePath("app.db")
    return if (dbFile.exists()) {
        val bytes = dbFile.length()
        if (bytes < 1024) "$bytes B"
        else if (bytes < 1024 * 1024) "${bytes / 1024} KB"
        else "${String.format("%.2f", bytes / (1024.0 * 1024.0))} MB"
    } else {
        "0 B"
    }
}

fun clearCache(context: Context) {
    val db = DataBase(context).writableDatabase
    db.execSQL("DELETE FROM $MakeTableName")
    db.execSQL("DELETE FROM $ModelTableName")
    db.close()
}
fun clearCar(context: Context) {
    val db = DataBase(context).writableDatabase
    db.execSQL("DELETE FROM $CarTableName")
    db.close()
}