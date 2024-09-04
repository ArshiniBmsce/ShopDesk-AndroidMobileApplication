package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

data class ActItem(val TID: String, var date: String, val time: String, var payStatus: String, val collectStatus: String,val items: Map<String, Int>)
class settings : Fragment() {

    private var actItems = mutableListOf<ActItem>()
    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var actAdapter: ActAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_act, container, false)
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val databaseUrl = "https://bookmart-ee4ed-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val database = FirebaseDatabase.getInstance(databaseUrl)
            val transactionsRef = database.getReference("transactions")
            val query = transactionsRef.orderByChild("userId").equalTo(userId)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    actItems.clear()
                    dataSnapshot.children.forEach { transactionSnapshot ->
                        val transactionId = transactionSnapshot.key.toString()
                        val transactionData = transactionSnapshot.value as? Map<String, Any>
                        val timeStamp = transactionData?.get("timeStamp") as? String

                        val dateTimeSplit = timeStamp?.split(" ")
                        val date = dateTimeSplit?.get(0) ?: ""
                        val time = dateTimeSplit?.get(1) ?: ""

                        val itemsData = transactionData?.get("items") as? Map<String, Long>
                        val items = itemsData?.mapValues { it.value.toInt() } ?: emptyMap()

                        val actItem = ActItem(
                            TID = transactionId,
                            date = date,
                            time = time,
                            payStatus = "paid",
                            collectStatus = "uncollected",
                            items = items
                        )
                        actItems.add(actItem)
                    }
                    actItems.sortByDescending { it.date + " " + it.time }
                    //recyclerViewCart.adapter = TransactionAdapter(actItems)
                    actAdapter = ActAdapter(actItems)
                    recyclerViewCart.adapter = actAdapter
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Transactions", "Failed to read transaction IDs: ${databaseError.toException()}")
                }
            })
        }


    }


}

