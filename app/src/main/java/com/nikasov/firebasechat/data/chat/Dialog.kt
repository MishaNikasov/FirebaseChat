package com.nikasov.firebasechat.data.chat

import com.google.firebase.firestore.DocumentId

data class Dialog (
    var owner : String = "",
    var title : String = "",
    var messageList : List<Message> = emptyList(),
    @DocumentId
    var id: String = ""
)