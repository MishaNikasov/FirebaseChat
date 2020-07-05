package com.nikasov.firebasechat.ui.data.user

data class User  (
    var uid: String? = null,
    var name: String? = null,
    var email: String? = null,
    var isAuthenticated: Boolean = false,
    var isNew: Boolean = false,
    var isCreated: Boolean = false
)