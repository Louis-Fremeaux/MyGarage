package tech.fremeaux.mygarage.data

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