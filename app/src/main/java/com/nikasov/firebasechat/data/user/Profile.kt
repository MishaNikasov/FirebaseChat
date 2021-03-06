package com.nikasov.firebasechat.data.user

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*
import kotlin.collections.HashMap

data class Profile  (
    var uid: String? = "",
    var name: String = "",
    var email: String = "",
    var age : Int? = -1,
    var chatHashMap: HashMap<String, String> = hashMapOf(),
    @ServerTimestamp
    var date: Date = Date(),
    @DocumentId
    var id: String = ""
)