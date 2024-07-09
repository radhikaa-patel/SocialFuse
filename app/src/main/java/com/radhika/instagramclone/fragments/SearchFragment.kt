package com.radhika.instagramclone.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.radhika.instagramclone.Models.User
import com.radhika.instagramclone.adapters.SearchAdapter
import com.radhika.instagramclone.databinding.FragmentSearchBinding
import com.radhika.instagramclone.utils.USER_NODE

class SearchFragment : Fragment() {

    lateinit var binding:FragmentSearchBinding
    lateinit var adapter:SearchAdapter
    var userList=ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSearchBinding.inflate(layoutInflater, container, false)
        binding.rv.layoutManager=LinearLayoutManager(requireContext())
        adapter= SearchAdapter(requireContext(),userList)
        binding.rv.adapter=adapter

        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {
            val tempList = ArrayList<User>()
            userList.clear()

            for(i in it.documents){
                if(i.id.toString() != Firebase.auth.currentUser!!.uid.toString()){
                    val user:User = i.toObject<User>()!!
                    tempList.add(user)
                }
            }

            userList.addAll(tempList)
            adapter.notifyDataSetChanged()


        }
        binding.searchButton.setOnClickListener {
            val text = binding.searchView.text.toString()
            Firebase.firestore.collection(USER_NODE).whereEqualTo("name", text).get()
                .addOnSuccessListener {
                    var tempList = ArrayList<User>()
                    userList.clear()

                    if(!it.isEmpty){
                        for(i in it.documents){
                            if(i.id.toString() != Firebase.auth.currentUser!!.uid.toString()){
                                val user:User = i.toObject<User>()!!
                                tempList.add(user)
                            }
                        }

                        userList.addAll(tempList)
                        adapter.notifyDataSetChanged()
                    }
                }
        }
        return binding.root
    }

    companion object {

    }
}