package com.nikasov.firebasechat.ui.fragment.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.nikasov.firebasechat.R
import com.nikasov.firebasechat.data.chat.Dialog
import com.nikasov.firebasechat.data.user.Profile
import com.nikasov.firebasechat.ui.adapter.DialogsAdapter
import com.nikasov.firebasechat.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dialogs.*

@AndroidEntryPoint
class DialogFragment : Fragment (R.layout.fragment_dialogs) {

    private val dialogViewModel : DialogViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        setUpDialogsList()
    }

    private fun setUpDialogsList() {
        dialogViewModel.getDialogs()
        val dialogClickListener = object : DialogsAdapter.Interaction {
            override fun onItemSelected(position: Int, item: Dialog) {
                findNavController().apply {
                    val action =
                        DialogFragmentDirections.actionProfileFragmentToChatFragment(item.id)
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

}