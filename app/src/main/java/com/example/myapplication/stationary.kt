package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class stationary : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val img = arrayOf(
        R.drawable.black_pen, R.drawable.blue_pen, R.drawable.crayola, R.drawable.gluestick,
        R.drawable.scissors, R.drawable.tape, R.drawable.highlighter, R.drawable.pilotpen,
        R.drawable.posit, R.drawable.pencil, R.drawable.sketchpen
    )
    private val text = arrayOf(
        "Black pen", "Blue pen", "Crayola", "Gluestick", "Scissors", "Tape",
        "Highligter", "Pilot pen", "Postit", "Pencil", "Sketchpen"
    )
    private val price = arrayOf(
        "Rs.10", "Rs.10", "Rs.100", "Rs.20", "Rs.30", "Rs20", "Rs.100", "Rs.80",
        "Rs.50", "Rs.10", "Rs.70"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stationary)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        recyclerView = findViewById(R.id.recycler_view)

        auth = FirebaseAuth.getInstance()

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CustomAdapter(img, text, price, ::onItemClick)
        recyclerView.adapter = adapter

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> replaceFragment(home())
                R.id.bottom_settings -> replaceFragment(settings())
                R.id.bottom_profile -> replaceFragment(profile())
                R.id.bottom_cart -> replaceFragment(cart())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

    private fun addToCart(position: Int) {
        val currentUser = auth.currentUser
        currentUser?.let {
            val userId = it.uid
            val selectedItem = text[position]
            val databaseUrl =
                "https://bookmart-ee4ed-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val database = FirebaseDatabase.getInstance(databaseUrl)
            val cartItemRef = database.reference.child("cart").child(userId).child(selectedItem)

            // Check if the item already exists in the cart
            cartItemRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val quantity = if (dataSnapshot.exists()) {
                        // If the item exists, retrieve its current quantity and increment it
                        val currentQuantity = (dataSnapshot.value as? Long ?: 0).toInt()
                        currentQuantity + 1
                    } else {
                        // If the item doesn't exist, set its quantity to 1
                        1
                    }
                    // Set the quantity of the item in the cart
                    cartItemRef.setValue(quantity)
                    Toast.makeText(
                        baseContext,
                        "$selectedItem added to cart: $quantity present",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Log statement to confirm item added to cart
                    Log.d("Cart", "$selectedItem added to cart for user: $userId")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(baseContext, "Add to cart failed !!", Toast.LENGTH_SHORT).show()
                    Log.e("Cart", "Failed to read value: ${databaseError.toException()}")
                }
            })
        }
    }

    private fun onTryButtonClick(position: Int) {
        // Handle the "Try" button click here
        when (position) {
            // Implement your logic based on the item position
            0, 1, 7 -> {
                val intent = Intent(this@stationary, drawpen::class.java)
                startActivity(intent)
            }
            9 -> {
                val intent = Intent(this@stationary, drawpenc::class.java)
                startActivity(intent)
            }
            // Add more cases if needed
            else -> {
                // Handle other positions if needed
            }
        }
    }

    private fun onItemClick(position: Int) {
        addToCart(position)
        onTryButtonClick(position)
    }
}



/*
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class stationary : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private val img= arrayOf(R.drawable.black_pen, R.drawable.blue_pen, R.drawable.crayola, R.drawable.gluestick,
        R.drawable.scissors, R.drawable.tape,R.drawable.highlighter,R.drawable.pilotpen,R.drawable.posit,R.drawable.pencil,R.drawable.sketchpen)
    private val text= arrayOf("Black pen","Blue pen","Crayola","Gluestick","Scissors","Tape",
        "Highligter","Pilot pen","Postit","Pencil","Sketchpen")
    private val price= arrayOf("Rs.10","Rs.10","Rs.100","Rs.20","Rs.30","Rs20","Rs.100","Rs.80",
        "Rs.50","Rs.10","Rs.70")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stationary)
        val b1=findViewById<Button>(R.id.b1)
        b1.setOnClickListener {
            val intent= Intent(this,drawpen::class.java)
            startActivity(intent)
        }
        val b2=findViewById<Button>(R.id.b2)
        b2.setOnClickListener {
            val intent= Intent(this,drawpenc::class.java)
            startActivity(intent)
        }
            bottomNavigationView=findViewById(R.id.bottom_navigation)
            bottomNavigationView.setOnItemSelectedListener { menuitem->
                when(menuitem.itemId){
                    R.id.bottom_home->{
                        replacefragment(home())
                        true
                    }
                    R.id.bottom_settings->{
                        replacefragment(settings())
                        true
                    }
                    R.id.bottom_profile->{
                        replacefragment(profile())
                        true
                    }
                    R.id.bottom_cart->{
                        replacefragment(cart())
                        true
                    }
                    else -> false
                }
            }
        val recyclerView=findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.adapter= CustomAdapter(img,text,price)
        }
        private fun replacefragment(fragment: Fragment){
            supportFragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit()
        }
    }
*/