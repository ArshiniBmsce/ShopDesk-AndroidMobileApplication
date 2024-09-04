package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

import android.util.Log

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.database.*

class profile : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var editTextEmail: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        auth = Firebase.auth
        editTextEmail = view.findViewById(R.id.editTextText)
        val currentUser: FirebaseUser? = auth.currentUser
        val userEmail: String? = currentUser?.email
        editTextEmail.setText(userEmail)
        val lg = view.findViewById<Button>(R.id.button)
        lg.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }


}
