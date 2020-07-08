package com.nikasov.firebasechat.util

import android.content.Context
import com.nikasov.firebasechat.common.Const

class Prefs (
    private val context: Context
) {

    fun loadProfileId() : String? {
        val sharedPref = context.getSharedPreferences(Const.SHARED_PREF, Context.MODE_PRIVATE)
        return sharedPref.getString(Const.PROFILE_ID, null)
    }

    fun saveProfileIdToPreferences(profileId: String) {
        val sharedPref = context.getSharedPreferences(Const.SHARED_PREF, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.apply {
            putString(Const.PROFILE_ID, profileId)
        }.apply()
    }
}