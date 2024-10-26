package com.example.foodorderapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        setContentView(binding.root)

        //initialization of firebase auth and database
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()


        //login with email and password
        binding.loginButton.setOnClickListener {
            //get data from text field
            email = binding.emaiAddress.text.toString().trim()
            password = binding.password.text.toString().trim()

            if(email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show()

            }else{
                checkAuthority()

            }
        }

        binding.dontHaveButton.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkAuthority() {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful){
                val user: FirebaseUser? = auth.currentUser
                updateUi(user)
            }else{
                Toast.makeText(this,"Login failed",Toast.LENGTH_SHORT).show()
                val intent = Intent(this,SignActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}