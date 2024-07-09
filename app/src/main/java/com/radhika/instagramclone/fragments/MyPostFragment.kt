package com.radhika.instagramclone.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.radhika.instagramclone.Models.Post
import com.radhika.instagramclone.R
import com.radhika.instagramclone.adapters.MyPostRvAdapter
import com.radhika.instagramclone.databinding.FragmentMyPostBinding


class MyPostFragment : Fragment() {
   private lateinit var binding:FragmentMyPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentMyPostBinding.inflate(layoutInflater, container, false)
        var postList=ArrayList<Post>()
        var adapter=MyPostRvAdapter(requireContext(),postList)
        binding.rv.layoutManager=StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter=adapter
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            var tempList= arrayListOf<Post>()
            for (i in it.documents){
                var post:Post=i.toObject<Post>()!!
                tempList.add(post)

            }
            postList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {

    }
}