package cz.janbaslar.dictionary.service

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.janbaslar.dictionary.data.models.SimpleWordDefinition

class SharedPreferencesService(context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("savedWords", Context.MODE_PRIVATE)
    }

    private val gson = Gson()

    fun saveDefinitions(objectsList: List<SimpleWordDefinition>) {
        val json = gson.toJson(objectsList)
        sharedPreferences.edit().putString("objectsList", json).apply()
    }

    fun getSavedDefinitions(): List<SimpleWordDefinition> {
        val json = sharedPreferences.getString("objectsList", null)
        return if (json != null) {
            val type = object : TypeToken<List<SimpleWordDefinition>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}