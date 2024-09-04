package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var auth: FirebaseAuth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        val e=findViewById<EditText>(R.id.editTextTextEmailAddress)
        val p=findViewById<EditText>(R.id.editTextTextPassword)
        val notsigned=findViewById<Button>(R.id.button3)
        val logged=findViewById<Button>(R.id.button2)
        notsigned.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
        logged.setOnClickListener {
            val email = e.text.toString()
            val password = p.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("sign", "signInWithEmail:success")
                        Toast.makeText(this,"login success",Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        val intent2=Intent(this,dash2::class.java)
                        startActivity(intent2)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("not signed", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }


    }

    }
