package com.nikasov.firebasechat.util

sealed class AuthResource<T> (
    val data : T? = null,
    val id: String? = null,
    val message : String? = null
) {
    class SignUp<T> (id: String?, data : T? = null) : AuthResource<T>(data, id)
    class LogIn<T> (id: String?, data : T? = null) : AuthResource<T>(data, id)
    class Error<T> (message: String?, data: T? = null) : AuthResource<T>(data, null,  message)
}