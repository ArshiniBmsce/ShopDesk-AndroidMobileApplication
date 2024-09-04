package com.example.myapplication


import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

data class CartItem(val name: String, var no: Int, val cost: Int, var quantity: Int, val img: String)
class cart : Fragment() {

    private var cartItems = mutableListOf<CartItem>()
    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var buttonPay: Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart)
        buttonPay = view.findViewById(R.id.buttonPay)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid

            val databaseUrl =
                "https://bookmart-ee4ed-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val database = FirebaseDatabase.getInstance(databaseUrl)
            val cartReference = database.getReference("cart").child(userId)


            cartReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //cartItems.clear()
                    dataSnapshot.children.forEach { cartItemSnapshot ->
                        val itemName = cartItemSnapshot.key.toString()
                        val no = cartItemSnapshot.getValue(Int::class.java) ?: 0
                        databaseReference = database.getReference("item")
                        databaseReference.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(itemDataSnapshot: DataSnapshot) {
                                itemDataSnapshot.children.forEach { categorySnapshot ->
                                    if (categorySnapshot.hasChild(itemName)) {
                                        val itemDetailsSnapshot = categorySnapshot.child(itemName)
                                        val cost = itemDetailsSnapshot.child("cost")
                                            .getValue(Int::class.java) ?: 0
                                        val stock = itemDetailsSnapshot.child("stock")
                                            .getValue(Int::class.java) ?: 0
                                        val img = itemDetailsSnapshot.child("img")
                                            .getValue(String::class.java) ?: ""
                                        cartItems.add(CartItem(itemName, no, cost, stock, img))

                                        updatePayButtonVisibility()

                                        cartAdapter.notifyDataSetChanged()
                                        return@forEach
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.e(
                                    "CartFragment",
                                    "Failed to read value: ${databaseError.toException()}"
                                )
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("CartFragment", "Failed to read value: ${databaseError.toException()}")
                }
            })
        }
        updatePayButtonVisibility()


        cartAdapter = CartAdapter(cartItems,
            onDeleteClick = { position -> deleteCartItem(position) },
            onAddClick = { position -> increaseQuantity(position) },
            onSubtractClick = { position -> decreaseQuantity(position) }
        )
        recyclerViewCart.adapter = cartAdapter


        buttonPay.setOnClickListener {
            val intent = Intent(requireActivity(), payment1::class.java)
            startActivity(intent)
        }
    }


    private fun deleteCartItem(position: Int) {
        if (position in 0 until cartItems.size) {
        val currentItem = cartItems[position]
        val itemName = currentItem.name
        val databaseUrl =
            "https://bookmart-ee4ed-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        // Get the current user's ID
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid

            // Reference to the user's cart node for the specific item
            val cartReference = database.getReference("cart").child(userId).child(itemName)

            // Remove the item from the cart in Firebase database
            cartReference.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Item deleted from cart", Toast.LENGTH_SHORT)
                        .show()
                    cartItems.removeAt(position)
                    cartAdapter.notifyDataSetChanged()
                    refreshCartFromFirebase()
                    updatePayButtonVisibility()

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to delete item from cart",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
            //refreshCartFromFirebase()
    }
        //refreshCartFromFirebase()

    }

    private fun increaseQuantity(position: Int) {

        val currentItem = cartItems[position]
        if (currentItem.no < currentItem.quantity) {
            currentItem.no++
            updateCartItemQuantity(currentItem)
            refreshCartFromFirebase()
        } else {
            Toast.makeText(requireContext(), "Cannot add more. Stock limit reached.", Toast.LENGTH_SHORT).show()
        }

    }

    private fun decreaseQuantity(position: Int) {

        var currentItem = cartItems[position]
        if (currentItem.no > 1) {
            currentItem.no--
            updateCartItemQuantity(currentItem)
            refreshCartFromFirebase()
        } else {
            // If quantity becomes zero, call deleteCartItem function
            Toast.makeText(requireContext(),"Cannot decrease further",Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateCartItemQuantity(cartItem: CartItem) {
        val databaseUrl = "https://bookmart-ee4ed-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val database = FirebaseDatabase.getInstance(databaseUrl)
        val itemName = cartItem.name

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val cartReference = database.getReference("cart").child(userId).child(itemName)
            cartReference.setValue(cartItem.no).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(requireContext(), "Failed to update item quantity in cart", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun refreshCartFromFirebase() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid

            val databaseUrl = "https://bookmart-ee4ed-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val database = FirebaseDatabase.getInstance(databaseUrl)
            val cartReference = database.getReference("cart").child(userId)

            cartItems.clear() // Clear existing items before updating

            cartReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach { cartItemSnapshot ->
                        val itemName = cartItemSnapshot.key.toString()
                        val no = cartItemSnapshot.getValue(Int::class.java) ?: 0
                        databaseReference = database.getReference("item")
                        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(itemDataSnapshot: DataSnapshot) {
                                itemDataSnapshot.children.forEach { categorySnapshot ->
                                    if (categorySnapshot.hasChild(itemName)) {
                                        val itemDetailsSnapshot = categorySnapshot.child(itemName)
                                        val cost = itemDetailsSnapshot.child("cost").getValue(Int::class.java) ?: 0
                                        val stock = itemDetailsSnapshot.child("stock").getValue(Int::class.java) ?: 0
                                        val img = itemDetailsSnapshot.child("img").getValue(String::class.java) ?: ""

                                        // Check if the item already exists in the cartItems list
                                        val existingCartItem = cartItems.find { it.name == itemName }
                                        if (existingCartItem == null) {
                                            // Add the item to the cartItems list only if it doesn't exist already
                                            cartItems.add(CartItem(itemName, no, cost, stock, img))

                                        } else {
                                            // If the item already exists, update its quantity
                                            existingCartItem.no = no
                                        }

                                        updatePayButtonVisibility()

                                        cartAdapter.notifyDataSetChanged()
                                        return@forEach
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.e("CartFragment", "Failed to read value: ${databaseError.toException()}")
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("CartFragment", "Failed to read value: ${databaseError.toException()}")
                }
            })
        }
    }

    private fun updatePayButtonVisibility() {
        if (cartItems.isEmpty()) {
            buttonPay.visibility = View.GONE

        } else {
            buttonPay.visibility = View.VISIBLE

        }
    }





}
