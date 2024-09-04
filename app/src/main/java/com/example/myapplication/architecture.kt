package com.example.myapplication


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

class architecture : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val img = arrayOf(R.drawable.scale, R.drawable.adjust_scale, R.drawable.pencilset, R.drawable.geometry,
        R.drawable.setsquare, R.drawable.storagecase, R.drawable.chartpaper)
    private val text = arrayOf("Scale", "Adjustable scale", "Pencil set", "Geometry box",
        "Set square", "Storage case", "Chartpaper")
    private val price = arrayOf("Rs.20", "Rs.40", "Rs.200", "Rs.120", "Rs.50", "Rs250", "Rs.10")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_architecture)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        recyclerView = findViewById(R.id.recycler_view)

        auth = FirebaseAuth.getInstance()
        /*
        val databaseUrl = "https://bookmart-ee4ed-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        databaseReference = database.reference.child("items")
        */

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CustomAdapter(img, text, price, ::addToCart)
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
            val databaseUrl = "https://bookmart-ee4ed-default-rtdb.asia-southeast1.firebasedatabase.app/"
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
                    Toast.makeText(baseContext,"$selectedItem added to cart: $quantity present",Toast.LENGTH_SHORT).show()
                    // Log statement to confirm item added to cart
                    Log.d("Cart", "$selectedItem added to cart for user: $userId")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(baseContext,"Add to cart failed !!",Toast.LENGTH_SHORT).show()
                    Log.e("Cart", "Failed to read value: ${databaseError.toException()}")
                }
            })
        }
    }

}


/*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.example.myapplication.CustomAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class architecture: AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private val img= arrayOf(R.drawable.scale, R.drawable.adjust_scale, R.drawable.pencilset, R.drawable.geometry,
        R.drawable.setsquare, R.drawable.storagecase,R.drawable.chartpaper)
    private val text= arrayOf("Scale","Adjustable scale","Pencil set","Geometry box",
        "Set square","Storage case","Chartpaper")
    private val price= arrayOf("Rs.20","Rs.40","Rs.200","Rs.120","Rs.50","Rs250","Rs.10")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notebooks)
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
