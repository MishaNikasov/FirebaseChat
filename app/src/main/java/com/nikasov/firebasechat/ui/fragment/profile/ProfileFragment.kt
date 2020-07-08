package com.nikasov.firebasechat.ui.fragment.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseUser
import com.nikasov.firebasechat.R
import com.nikasov.firebasechat.data.user.Profile
import com.nikasov.firebasechat.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment : Fragment (R.layout.fragment_profile) {

    private val profileViewModel : ProfileViewModel by viewModels()
    private val args : ProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        setUpProfile()
        Timber.d(args.uid)
    }

    private fun setUpUi() {
        logOutBtn.setOnClickListener {
            logOut()
        }
        chatBtn.setOnClickListener {
//            findNavController().apply {
//                val action = ProfileFragmentDirections.actionProfileFragmentToChatFragment(args.uid)
//                navigate(action)
//            }
            profileViewModel.addDialog(args.uid)
        }
    }

    private fun setUpProfile() {
        profileViewModel.getProfile(args.uid)
        profileViewModel.profile.observe(viewLifecycleOwner, Observer {user ->
            when (user){
                is Resource.Success -> {
                    user.data?.let {
                        updateUi(user.data)
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        })
    }

    private fun updateUi(profile : Profile) {
        name.text = profile.name
    }

    private fun logOut() {
        profileViewModel.logOut()
        findNavController().apply {
            popBackStack()
            navigate(R.id.toAuthFragment)
        }
    }
}