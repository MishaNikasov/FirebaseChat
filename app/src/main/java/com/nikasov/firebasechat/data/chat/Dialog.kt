package com.nikasov.firebasechat.data.chat

import com.google.firebase.firestore.DocumentId

data class Dialog (
    var members : List<String> = emptyList(),
    var title : String = "",
    @DocumentId
    var id: String = ""
)