package tech.fremeaux.mygarage.data.repo

import android.content.ContentValues
import android.content.Context
import org.json.JSONObject
import tech.fremeaux.mygarage.data.DataBase
import tech.fremeaux.mygarage.data.MakeTableName
import tech.fremeaux.mygarage.data.ModelTableName
import tech.fremeaux.mygarage.data.api.ApiService
import tech.fremeaux.mygarage.data.model.Make
import tech.fremeaux.mygarage.data.model.Model

class ModelRepository(context: Context) {

    private val db = DataBase(context)

    fun parseModels(json: String): List<Model> {
        if (!json.isEmpty()){
            val list = mutableListOf<Model>()
            val jsonArray = JSONObject(json).getJSONArray("data")

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                val model = Model(id = obj.getInt("id"), makeId = obj.getInt("make_id"), name = obj.getString("name"))
                list.add(model)
                addModel(obj.getInt("make_id"),obj.getString("name"))
            }
            return list
        }else{
            return emptyList()
        }
    }

    fun addModel(makeId:Int, name: String) {
        val db = db.writableDatabase

        val values = ContentValues().apply {
            put("makeId", makeId)
            put("name", name)
        }

        db.insert(ModelTableName, null, values)
        db.close()
    }

    fun getModel(selectedMake: Make?): List<Model> {

        val db = db.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $ModelTableName where makeId = ?", arrayOf(selectedMake?.id.toString()))
        val model = mutableListOf<Model>()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val makeId = cursor.getInt(cursor.getColumnIndexOrThrow("makeId"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            model.add(Model(id, makeId, name))
        }

        cursor.close()
        db.close()

        return if (!model.isEmpty()){
            model
        }else{
            parseModels(ApiService().get("https://carapi.app/api/models/v2?make=${selectedMake?.name}"))
        }
    }
}