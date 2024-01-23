package cz.janbaslar.dictionary.service

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import cz.janbaslar.dictionary.data.models.ApiResponse
import cz.janbaslar.dictionary.data.models.WordDefinition
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class FreeDictionaryApiService {
    private val client = OkHttpClient()
    private val gson = Gson()
    interface DictionaryCallback {
        fun onSuccess(result: ApiResponse)
        fun onFailure(error: ApiResponse)
    }

    fun getWordDefinition(word: String, callback: DictionaryCallback) {
        val url = "https://api.dictionaryapi.dev/api/v2/entries/en/$word"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(ApiResponse.fail("Failed to connect to API!"))
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            val wordDefinitions: List<WordDefinition> = gson.fromJson(response.body()?.string(), object : TypeToken<List<WordDefinition>>() {}.type)
                            callback.onSuccess(ApiResponse.success(wordDefinitions.first()))
                        } catch (e: JsonSyntaxException) {
                            callback.onSuccess(ApiResponse.fail("Unexpected response from API!"))
                        }
                    } else {
                        callback.onFailure(ApiResponse.fail("API response has empty body!"))
                    }
                } else {
                    callback.onFailure(ApiResponse.wordNotFound("Word '$word' not found!"))
                }
            }
        })
    }
}

