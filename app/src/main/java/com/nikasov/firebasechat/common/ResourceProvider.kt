package com.nikasov.firebasechat.common

import android.content.Context
import javax.inject.Inject

class ResourceProvider @Inject constructor (var context : Context) {
    fun getStringArray(resId : Int) : Array<String>{
        return context.resources.getStringArray(resId)
    }
    fun getString(resId : Int) : String {
        return context.resources.getString(resId)
    }
}