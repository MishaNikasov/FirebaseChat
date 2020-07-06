package com.nikasov.firebasechat.ui.fragment.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import com.nikasov.firebasechat.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*

@AndroidEntryPoint
class ProfileFragment : Fragment (R.layout.fragment_profile) {

    private val profileViewModel : ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        setUpProfile()
    }

    private fun setUpUi() {
        logOutBtn.setOnClickListener {
            logOut()
        }
    }

    private fun setUpProfile() {
        profileViewModel.getProfileInfo()
        profileViewModel.profile.observe(viewLifecycleOwner, Observer {user ->
            updateUi(user)
        })
    }

    private fun updateUi(user : FirebaseUser) {
        name.text = user.displayName
    }

    private fun logOut() {
        profileViewModel.logOut()
        findNavController().apply {
            popBackStack()
            navigate(R.id.toAuthFragment)
        }
    }
}