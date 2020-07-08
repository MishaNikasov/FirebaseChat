package com.nikasov.firebasechat.data.user

import com.google.firebase.firestore.DocumentId

data class Profile  (
    var uid: String? = "",
    var name: String = "",
    var email: String = "",
    var age : Int? = -1,
    @DocumentId
    var id: String = ""
)