package com.nikasov.firebasechat.data.chat

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Dialog (
    var members : List<String> = emptyList(),
    var title : String = "",
    var ownerId : String ="",
    @ServerTimestamp
    var date: Date = Date(),
    @DocumentId
    var id: String = ""
)