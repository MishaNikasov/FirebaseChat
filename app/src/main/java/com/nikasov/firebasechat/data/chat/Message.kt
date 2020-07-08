package com.nikasov.firebasechat.data.chat

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message (
    var messageOwner: String = "",
    var text: String = "",
    @ServerTimestamp
    var date: Date = Date(),
    @DocumentId
    var id: String = ""
)