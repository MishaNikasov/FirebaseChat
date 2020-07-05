package com.nikasov.firebasechat.ui.data.user

data class User  (
    var uid: String,
    var name: String,
    var email: String,
    var isAuthenticated: Boolean = false,
    var isNew: Boolean = false,
    var isCreated: Boolean = false
)