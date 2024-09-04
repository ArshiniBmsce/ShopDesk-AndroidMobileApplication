package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()

        val b1 = findViewById<Button>(R.id.button3)
        val b2 = findViewById<Button>(R.id.button2)
        val n = findViewById<EditText>(R.id.editTextTextPersonName)
        val e = findViewById<EditText>(R.id.editTextTextPersonName2)
        val p = findViewById<EditText>(R.id.editTextTextPassword)

        // Check if the user is already signed in
        if (auth.currentUser != null) {
            // User is already signed in, you can navigate to the main activity or perform any action
            val intent = Intent(this, dash2::class.java)
            startActivity(intent)
            finish() // finish the current activity
        }

        b1.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        b2.setOnClickListener {
            val name = n.text.toString()
            val email = e.text.toString()
            val password = p.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("email", "signInWithEmail:success")
                        Toast.makeText(baseContext, "Account created", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}
