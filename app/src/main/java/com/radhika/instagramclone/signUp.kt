package com.radhika.instagramclone

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.radhika.instagramclone.Models.User
import com.radhika.instagramclone.databinding.ActivitySignUpBinding
import com.radhika.instagramclone.utils.USER_NODE
import com.radhika.instagramclone.utils.USER_PROFILE_FOLDER
import com.radhika.instagramclone.utils.uploadImage
import com.squareup.picasso.Picasso


class signUp : AppCompatActivity() {

    val binding by lazy{
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    lateinit var user: User
    private val launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        uri?.let{
              uploadImage(uri, USER_PROFILE_FOLDER){
                  if(it!=null){
                      user.image=it
                      binding.profileimage.setImageURI(uri)
                  }
              }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val text = "<font color=#FF000000>Already Have An Account?</font> <font color=#1E88E5>Login</font>"
        binding.login.setText(Html.fromHtml(text))
        user=User()

        if(intent.hasExtra("MODE")){
            if(intent.getIntExtra("MODE",-1)==1){

                binding.btnregister.text="Update Profile"
                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                    user=it.toObject<User>()!!
                    if(!user.image.isNullOrEmpty()){
                        Picasso.get().load(user.image).into(binding.profileimage)
                    }
                    binding.name.editText?.setText(user.name)
                    binding.email.editText?.setText(user.email)
                    binding.password.editText?.setText(user.password)

                }

            }
        }

        binding.btnregister.setOnClickListener {
            if(intent.hasExtra("MODE")){
                if(intent.getIntExtra("MODE",-1)==1){
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(Intent(this@signUp, HomeActivity::class.java))
                            finish()
                        }
                }
            }else {
                if (binding.name.editText?.text.toString().equals("") or
                    binding.email.editText?.text.toString().equals("") or
                    binding.password.editText?.text.toString().equals("")
                ) {
                    Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.email.editText?.text.toString(),
                        binding.password.editText?.text.toString()
                    ).addOnCompleteListener { result ->

                        if (result.isSuccessful) {
                            Toast.makeText(this, "Successfully Registered!", Toast.LENGTH_SHORT)
                                .show()
                            user.name = binding.name.editText?.text.toString()
                            user.email = binding.email.editText?.text.toString()
                            user.password = binding.password.editText?.text.toString()
                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    startActivity(Intent(this@signUp, HomeActivity::class.java))
                                    finish()
                                }

                        } else {
                            Toast.makeText(
                                this,
                                result.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

        }
        binding.plusicon.setOnClickListener{
            launcher.launch("image/*")

        }
        binding.profileimage.setOnClickListener{
            launcher.launch("image/*")

        }
        binding.login.setOnClickListener {
            startActivity(Intent(this@signUp,LoginActivity::class.java))
            finish()
        }
    }
}