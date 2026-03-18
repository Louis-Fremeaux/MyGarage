package tech.fremeaux.mygarage.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(context: Context) : SQLiteOpenHelper(context, "app.db", null, 1){
    override fun onCreate(db: SQLiteDatabase?)
    {
        db?.execSQL("CREATE TABLE cars (id INTEGER PRIMARY KEY AUTOINCREMENT,make TEXT,model TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, old: Int, new: Int)
    {
        db?.execSQL("DROP TABLE IF EXISTS cars")
        onCreate(db)
    }
}