package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font

import java.util.Calendar
import java.text.SimpleDateFormat

import java.io.File
import java.util.TimeZone
import kotlin.properties.Delegates


data class TransactionData(val userId: String, val items: Map<String, Int>, val timeStamp: String)
class payment1 : AppCompatActivity() {
    private var total by Delegates.notNull<Int>()
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment1)
        val paybtn = findViewById<Button>(R.id.paybtn)
        paybtn.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            currentUser?.let { user ->
                val userId = user.uid
                val databaseUrl = "https://bookmart-ee4ed-default-rtdb.asia-southeast1.firebasedatabase.app/"
                val database = FirebaseDatabase.getInstance(databaseUrl)
                val cartReference = database.getReference("cart").child(userId)

                cartReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        //cartItems.clear()
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

                                            if (stock>0) {
                                                val newstock = stock-no;
                                                val itemReference = categorySnapshot.ref.child(itemName).child("stock")
                                                itemReference.setValue(newstock)
                                                    .addOnSuccessListener {
                                                        Log.d("Look", "Stock of $itemName decremented successfully.")
                                                        total += cost
                                                    }
                                                    .addOnFailureListener { exception ->
                                                        Log.e("Look", "Failed to decrement stock of $itemName: $exception")
                                                    }
                                            }

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

                    }
                })

                cartReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.value?.let { cartData ->
                            val calendar = Calendar.getInstance()
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
                            val currentTime = dateFormat.format(calendar.time)
                            val transactionData = TransactionData(userId, cartData as Map<String, Int>,currentTime)
                            val transactionsId = database.getReference("transactions")
                            val newTransactionRef = transactionsId.push()
                            newTransactionRef.setValue(transactionData)
                                .addOnSuccessListener {
                                    val transactionId = newTransactionRef.key
                                    Log.d("Transactions", "Transaction added successfully!")
                                    cartReference.setValue(null)
                                        .addOnSuccessListener {
                                            Log.d("Cart", "Cart cleared!")
                                            // Start the new activity here
                                            val intent = Intent(this@payment1, cart::class.java)
                                            startActivity(intent)
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.d("Cart", "Cart Not cleared :(")
                                        }
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("Transactions", "Failed to add transaction: $exception")
                                }



                            cartReference.removeValue()
                                .addOnSuccessListener {
                                    Log.d("Cart", "Cart cleared!")

                                    // Start the new activity here
                                    val intent = Intent(this@payment1, cart::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { exception ->
                                    Log.d("Cart", "Cart Not cleared :(")
                                }


                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("Cart", "Failed to read cart data: ${databaseError.toException()}")
                    }
                })

            }
            Log.d("Paybtn","Stocks reduced!")
            val intent = Intent(this@payment1, cart::class.java)
            startActivity(intent)
        }

    }


}
