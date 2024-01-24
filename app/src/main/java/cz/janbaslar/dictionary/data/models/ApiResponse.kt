package cz.janbaslar.dictionary.data.models

class ApiResponse private constructor(
    private val success: Boolean,
    private val notFound: Boolean,
    val error: String?,
    val content: WordDefinition?
) {
    companion object {
        fun success(content: WordDefinition): ApiResponse {
            return ApiResponse(success = true, notFound = false, error = null, content = content)
        }

        fun wordNotFound(error: String): ApiResponse {
            return ApiResponse(success = false, notFound = true, error = error, content = null)
        }

        fun fail(error: String): ApiResponse {
            return ApiResponse(success = false, notFound = false, error = error, content = null)
        }
        fun empty(): ApiResponse {
            return ApiResponse(success = true, notFound = true, error = null, content = null)
        }
    }

    fun isSuccessful(): Boolean {
        return success
    }

    fun wordNotFound(): Boolean {
        return !success && notFound
    }

    fun isEmpty(): Boolean {
        return success && notFound
    }

    fun getErrorMessage() : String {
        return error ?: "Unexpected error appeared!"
    }
}