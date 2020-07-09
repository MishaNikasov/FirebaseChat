package com.nikasov.firebasechat.util

import android.content.Context
import com.nikasov.firebasechat.common.Const
import javax.inject.Inject

class Prefs @Inject constructor (
    context: Context
) {

    private val sharedPref = context.getSharedPreferences(Const.SHARED_PREF, Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()

    fun loadProfileId() : String? {
        return sharedPref.getString(Const.PROFILE_ID, null)
    }

    fun saveProfileIdToPreferences(profileId: String) {
        editor.apply {
            putString(Const.PROFILE_ID, profileId)
        }.apply()
    }

    fun clearUser() {
        editor.remove(Const.PROFILE_ID)
    }
}