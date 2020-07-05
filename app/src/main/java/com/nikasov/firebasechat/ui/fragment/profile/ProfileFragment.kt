package com.nikasov.firebasechat.ui.fragment.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.nikasov.firebasechat.R
import timber.log.Timber

class ProfileFragment : Fragment (R.layout.fragment_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("profile")
    }
}