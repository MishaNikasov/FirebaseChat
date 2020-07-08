package com.nikasov.firebasechat.ui.fragment.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nikasov.firebasechat.R
import com.nikasov.firebasechat.data.chat.Dialog
import com.nikasov.firebasechat.data.user.Profile
import com.nikasov.firebasechat.ui.adapter.DialogsAdapter
import com.nikasov.firebasechat.util.Prefs
import com.nikasov.firebasechat.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dialogs.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DialogFragment : Fragment (R.layout.fragment_dialogs) {

    private val dialogViewModel : DialogViewModel by viewModels()
    private val args : DialogFragmentArgs by navArgs()
    private lateinit var profileId : String

    @Inject
    lateinit var prefs : Prefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileId =
            (if (args.profileId != null) {
                args.profileId
            } else {
                prefs.loadProfileId()
            }) as String

        setUpUi()
    }

    private fun setUpUi() {

        setUpProfile()
        setUpDialogsList()

        logOutBtn.setOnClickListener {
            logOut()
        }
        chatBtn.setOnClickListener {

            dialogViewModel.addDialog()
        }
    }

    private fun setUpProfile() {
        dialogViewModel.getProfile(profileId)
        dialogViewModel.profile.observe(viewLifecycleOwner, Observer { user ->
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

    private fun setUpDialogsList() {
        dialogViewModel.getDialogs()
        val dialogClickListener = object : DialogsAdapter.Interaction {
            override fun onItemSelected(position: Int, item: Dialog) {
                findNavController().apply {
                    val action =
                        DialogFragmentDirections.actionProfileFragmentToChatFragment(profileId, item.id)
                    navigate(action)
                }
            }

        }
        val dialogAdapter = DialogsAdapter(dialogClickListener)
        dialogRecycler.apply {
            adapter = dialogAdapter
        }
        dialogViewModel.dialogs.observe(viewLifecycleOwner, Observer { user ->
            when (user){
                is Resource.Success -> {
                    user.data?.let { dialogs ->
                        dialogAdapter.submitList(dialogs)
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
        dialogViewModel.logOut()
        findNavController().apply {
            popBackStack()
            navigate(R.id.toAuthFragment)
        }
    }
}