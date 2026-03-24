package tech.fremeaux.mygarage.data.repo

import android.content.ContentValues
import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import tech.fremeaux.mygarage.data.DataBase
import tech.fremeaux.mygarage.data.MakeTableName
import tech.fremeaux.mygarage.data.api.ApiService
import tech.fremeaux.mygarage.data.model.Make

class MakeRepository(context: Context) {
    private val db = DataBase(context)

    fun parseMakesAndStore(json: String): List<Make> {
        if (!json.isEmpty()){
            val list = mutableListOf<Make>()
            val jsonArray = JSONObject(json).getJSONArray("data")

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                val make = Make(id = obj.getInt("id"), name = obj.getString("name"))
                list.add(make)
                //addMake(obj.getString("name"))
            }
            return list
        }else{
            return emptyList()
        }
    }
    fun getMakes(): List<Make> {

        val db = db.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $MakeTableName", null)
        val makes = mutableListOf<Make>()


        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))

            makes.add(Make(id, name))
        }

        cursor.close()
        db.close()

        return if (!makes.isEmpty()){
            makes
        }else{
            parseMakesAndStore(ApiService().get("https://carapi.app/api/makes/v2"))
        }
    }

    fun addMake(name: String) {
        val db = db.writableDatabase

        val values = ContentValues().apply {
            put("name", name)
        }

        db.insert(MakeTableName, null, values)
        db.close()
    }
}