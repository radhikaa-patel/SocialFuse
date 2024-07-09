package com.radhika.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.radhika.instagramclone.Models.User
import com.radhika.instagramclone.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnlogin.setOnClickListener {
              if(binding.Email.editText?.text.toString().equals("") or
                  binding.Pass.editText?.text.toString().equals("")){
                  Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()
              }else{
                  var user= User(binding.Email.editText?.text.toString(),binding.Pass.editText?.text.toString())
                  Firebase.auth.signInWithEmailAndPassword(user.email!!,user.password!!)
                      .addOnCompleteListener {
                          if(it.isSuccessful){
                              startActivity(Intent(this,HomeActivity::class.java))
                              finish()
                          }else{
                              Toast.makeText(this,it.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                          }
                      }
              }
        }
        binding.btnnew.setOnClickListener {
            val intent=Intent(this,signUp::class.java)
            startActivity(intent)
        }
    }
}