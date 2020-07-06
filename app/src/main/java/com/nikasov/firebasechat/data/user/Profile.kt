package com.nikasov.firebasechat.data.user

data class Profile  (
    var uid: String?,
    var name: String,
    var email: String,
    var age : Int? = null,
    var isAuthenticated: Boolean = false,
    var isNew: Boolean = false,
    var isCreated: Boolean = false
)