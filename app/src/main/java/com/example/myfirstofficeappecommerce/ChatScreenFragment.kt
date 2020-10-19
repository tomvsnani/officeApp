package com.example.myfirstofficeappecommerce

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstofficeappecommerce.Adapters.ChatAdapter
import com.example.myfirstofficeappecommerce.Models.ChatModel
import com.example.myfirstofficeappecommerce.databinding.FragmentChatScreenBinding


class ChatScreenFragment : Fragment() {
    var binding: FragmentChatScreenBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_chat_screen, container, false)
        binding = FragmentChatScreenBinding.bind(v)
        var adapter = ChatAdapter(this)
        binding!!.chatrecyclerview.adapter = adapter

        binding!!.chatrecyclerview.layoutManager =
            LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding!!.chattoolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        adapter.submitList(
            mutableListOf(
                ChatModel("a", "b", "1", Constants.MESSAGE_STATUS_SENT, 1, "hai"),
                ChatModel("a", "b", "2", Constants.MESSAGE_STATUS_NOT_SENT, 2, "haii"),
                ChatModel("b", "a", "3", Constants.MESSAGE_STATUS_DELIVERED, 3, "hai"),
                ChatModel("b", "a", "4", Constants.MESSAGE_STATUS_SENT, 4, "haii"),
                ChatModel("a", "b", "5", Constants.MESSAGE_STATUS_SENT, 5, "hai"),
                ChatModel("a", "b", "1", Constants.MESSAGE_STATUS_SENT, 1, "hai"),
                ChatModel("a", "b", "2", Constants.MESSAGE_STATUS_NOT_SENT, 2, "haii"),
                ChatModel("b", "a", "3", Constants.MESSAGE_STATUS_DELIVERED, 3, "hai"),
                ChatModel("b", "a", "4", Constants.MESSAGE_STATUS_SENT, 4, "haii"),
                ChatModel("a", "b", "5", Constants.MESSAGE_STATUS_SENT, 5, "hai"),
                ChatModel("a", "b", "1", Constants.MESSAGE_STATUS_SENT, 1, "hai"),
                ChatModel("a", "b", "2", Constants.MESSAGE_STATUS_NOT_SENT, 2, "haii"),
                ChatModel("b", "a", "3", Constants.MESSAGE_STATUS_DELIVERED, 3, "hai"),
                ChatModel("b", "a", "4", Constants.MESSAGE_STATUS_SENT, 4, "haii"),
                ChatModel("a", "b", "5", Constants.MESSAGE_STATUS_SENT, 5, "hai")
            )
        )

        binding!!.sendmessgaebutton.setOnClickListener {
            if (binding!!.entermessageEdittext.text.toString().isNotEmpty()) {
                var list = (adapter.currentList).toMutableList()
                list.add(
                    ChatModel(
                        "a",
                        "b",
                        "5",
                        Constants.MESSAGE_STATUS_SENT,
                        adapter.currentList[adapter.currentList.size - 1].id,
                        binding!!.entermessageEdittext.text.toString()
                    )
                )
                adapter.submitList(list)
            }
        }


        return v
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            activity?.onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        for (i in 0..1) {
            menu.getItem(i).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onStop() {
        var view = binding!!.entermessageEdittext
        var inputManager: InputMethodManager =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        super.onStop()
    }
}
