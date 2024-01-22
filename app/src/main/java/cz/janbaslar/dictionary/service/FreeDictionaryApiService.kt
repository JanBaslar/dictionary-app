package cz.janbaslar.dictionary.service

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class FreeDictionaryApiService {
    private val client = OkHttpClient()
    interface DictionaryCallback {
        fun onSuccess(result: String)
        fun onFailure(error: String)
    }

    fun getWordDefinition(word: String, callback: DictionaryCallback) {
        val url = "https://api.dictionaryapi.dev/api/v2/entries/en/$word"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()?.string() ?: "No result")
                } else {
                    callback.onFailure("Error: ${response.code()}")
                }
            }
        })
    }
}

