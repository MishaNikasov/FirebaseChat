package com.nikasov.firebasechat.util

sealed class Resource<T> (
    val data : T? = null,
    val message : String? = null,
    val loading : Boolean = false
) {
    class Success<T> (data : T) : Resource<T>(data)
    class Empty<T> (data : T? = null) : Resource<T>(data)
    class Error<T> (message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}