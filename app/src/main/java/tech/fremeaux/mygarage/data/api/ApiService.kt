package tech.fremeaux.mygarage.data.api

import java.net.HttpURLConnection
import java.net.URL

class ApiService {
    fun get(url:String): String {
        val url = URL(url)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000

        return try {
            val stream = connection.inputStream
            stream.bufferedReader().use { it.readText() }

        } catch (e: Exception){
            e.printStackTrace()
            return ""
        } finally {
            connection.disconnect()
        }
    }
}