package com.nikasov.firebasechat.ui.fragment.chat

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikasov.firebasechat.R
import com.nikasov.firebasechat.ui.adapter.MessageAdapter
import com.nikasov.firebasechat.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_chat.*

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {

    private val viewModel : ChatViewModel by viewModels()
    private val args : ChatFragmentArgs by navArgs()

    private lateinit var messageAdapter : MessageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun setUpList() {
        messageAdapter = MessageAdapter(viewModel.getCurrentId())
        messageRecycler.apply {
            adapter = messageAdapter
            (layoutManager as LinearLayoutManager).stackFromEnd = true
        }

        viewModel.messages.observe(viewLifecycleOwner, Observer { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        messageAdapter.submitList(resource.data.toList())
                    }
                }
            }
        })
    }

    private fun initUi() {

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    enableChat(false)
                }
                is Resource.Empty -> {
                    enableChat(true)
                }
                is Resource.Error -> {
                    enableChat(false)
                }
            }
        })

        if (args.dialogId != null) {
            viewModel.setExistingDialog(args.dialogId!!)
        } else {
            viewModel.checkIfDialogExist(args.memberId!!)
        }

        sendBtn.isEnabled = false
        setUpList()

        sendBtn.setOnClickListener {
            viewModel.addMessage(typeMessage.text.toString())
            messageRecycler.smoothScrollToPosition(messageAdapter.itemCount)
            typeMessage.text.clear()
        }
        typeMessage.doAfterTextChanged {
            sendBtn.isEnabled = typeMessage.text.isNotEmpty()
        }
    }

    private fun enableChat(isEnable: Boolean) {
        if (!isEnable) {
            loadingScreen.visibility = View.VISIBLE
            typeMessage.isEnabled = false
        } else {
            loadingScreen.visibility = View.GONE
            typeMessage.isEnabled = true
        }
    }
}