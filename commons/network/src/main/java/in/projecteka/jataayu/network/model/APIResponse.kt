package `in`.projecteka.jataayu.network.model

data class APIResponse<T>(val response: T?, val error: Error?, val isLoading: Boolean = false) {

    fun hasErrors() : Boolean {
        return error != null
    }

    fun isSuccess() : Boolean {
        return error == null
    }


    companion object {
        private const val unhandledError = -1
        fun getError(error: Throwable): Error {
            return  Error(unhandledError, error.localizedMessage ?: "")
        }
    }
}