package com.nikasov.firebasechat.data.user

data class Profile  (
    var uid: String? = "",
    var name: String = "",
    var email: String = "",
    var age : Int? = -1
)