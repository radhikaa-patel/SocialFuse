package com.radhika.instagramclone.Post

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.radhika.instagramclone.HomeActivity
import com.radhika.instagramclone.Models.Post
import com.radhika.instagramclone.Models.Reel
import com.radhika.instagramclone.Models.User
import com.radhika.instagramclone.R
import com.radhika.instagramclone.databinding.ActivityReelsBinding
import com.radhika.instagramclone.utils.POST
import com.radhika.instagramclone.utils.POST_FOLDER
import com.radhika.instagramclone.utils.REEL
import com.radhika.instagramclone.utils.REEL_FOLDER
import com.radhika.instagramclone.utils.USER_NODE
import com.radhika.instagramclone.utils.uploadImage
import com.radhika.instagramclone.utils.uploadVideo

class ReelsActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityReelsBinding.inflate(layoutInflater)
    }
    private lateinit var videoUrl:String
    private lateinit var progressDialog: ProgressDialog
    private val launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->
        uri?.let{
            uploadVideo(uri, REEL_FOLDER,progressDialog){
                    url->
                if(url!=null){
                    videoUrl=url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressDialog=ProgressDialog(this)

        binding.selectReel.setOnClickListener {
            launcher.launch("video/*")
        }
        binding.btncancel.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        binding.btnpost.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                var user:User=it.toObject<User>()!!
                val reel: Reel = Reel(videoUrl,binding.caption.editText?.text.toString(),user.image!!)
                Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+REEL).document().set(reel).addOnSuccessListener {
                        startActivity(Intent(this,HomeActivity::class.java))
                        finish()
                    }
                }
            }

        }
    }
}