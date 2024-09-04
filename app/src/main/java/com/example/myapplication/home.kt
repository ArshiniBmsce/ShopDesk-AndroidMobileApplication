package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2

class home : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var viewPagerAdapter: Viewpageadapter
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val delay: Long = 3000 // Adjust the delay time as needed

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize images before using them in ViewPager2
        val images = listOf(R.drawable.ad1, R.drawable.ad2, R.drawable.ad3,R.drawable.ad4)

        // Setup ViewPager2
        viewPager2 = view.findViewById(R.id.viewPager2)
        viewPagerAdapter = Viewpageadapter(images)
        viewPager2.adapter = viewPagerAdapter

        // Setup automatic scrolling
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val currentItem = viewPager2.currentItem
                val nextItem = if (currentItem < viewPagerAdapter.itemCount - 1) currentItem + 1 else 0
                viewPager2.currentItem = nextItem
                startAutoScroll()
            }
        }

        // Setup Toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = ""

        // Find the SearchView
        val searchView: SearchView = toolbar.findViewById(R.id.search)
        // Set a hint for the SearchView
        searchView.queryHint = "Type to search"

        // Set a query text listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission
                if (!query.isNullOrBlank()) {
                    // Example: If the user searches for "chocolate," navigate to ChocolateActivity
                    if (query.equals("stationary", ignoreCase = true)) {
                        val intent = Intent(activity, stationary::class.java)
                        startActivity(intent)
                    }
                    else if (query.equals("notebooks", ignoreCase = true)) {
                        val intent = Intent(activity, notebooks::class.java)
                        startActivity(intent)}

                    else if (query.equals("chocolates", ignoreCase = true)) {
                        val intent = Intent(activity, chocolate::class.java)
                        startActivity(intent)}

                    else if (query.equals("architecture", ignoreCase = true)) {
                        val intent = Intent(activity, architecture::class.java)
                        startActivity(intent)}

                    else if (query.equals("textbook", ignoreCase = true)) {
                        val intent = Intent(activity, textbook::class.java)
                        startActivity(intent)
                    }else {
                        // Handle other search queries or perform a general search action
                        Toast.makeText(requireContext(),"No such item!",Toast.LENGTH_SHORT).show()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search query text change
                // Example: filterData(newText)
                return true
            }
        })

        // Find the ImageButton
        val b1: ImageButton = view.findViewById(R.id.button)
        // Set an OnClickListener for the button
        b1.setOnClickListener {
            val intent = Intent(activity, notebooks::class.java)
            startActivity(intent)
        }

        val b2: ImageButton = view.findViewById(R.id.button2)
        b2.setOnClickListener {
            val intent = Intent(activity, stationary::class.java)
            startActivity(intent)
        }
        val b3: ImageButton = view.findViewById(R.id.button3)
        b3.setOnClickListener {
            val intent = Intent(activity, architecture::class.java)
            startActivity(intent)
        }
        val b4: ImageButton = view.findViewById(R.id.button4)
        b4.setOnClickListener {
            val intent = Intent(activity, chocolate::class.java)
            startActivity(intent)
        }
        val b5: ImageButton = view.findViewById(R.id.button5)
        b5.setOnClickListener {
            val intent = Intent(activity, textbook::class.java)
            startActivity(intent)
        }
        val b6: ImageButton = view.findViewById(R.id.ib1)
        b6.setOnClickListener {
            val intent = Intent(activity, stationary::class.java)
            startActivity(intent)
        }
        val b7: ImageButton = view.findViewById(R.id.ib2)
        b7.setOnClickListener {
            val intent = Intent(activity, notebooks::class.java)
            startActivity(intent)
        }
        val b8: ImageButton = view.findViewById(R.id.ib3)
        b8.setOnClickListener {
            val intent = Intent(activity, chocolate::class.java)
            startActivity(intent)
        }
        val b9: ImageButton = view.findViewById(R.id.ib4)
        b9.setOnClickListener {
            val intent = Intent(activity, notebooks::class.java)
            startActivity(intent)
        }
        val b10: ImageButton = view.findViewById(R.id.ib5)
        b10.setOnClickListener {
            val intent = Intent(activity, stationary::class.java)
            startActivity(intent)
        }
        val b11: ImageButton = view.findViewById(R.id.ib6)
        b11.setOnClickListener {
            val intent = Intent(activity, stationary::class.java)
            startActivity(intent)
        }


        // Start automatic scrolling
        startAutoScroll()

        return view
    }

    private fun startAutoScroll() {
        handler.postDelayed(runnable, delay)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop the handler when the fragment is destroyed to prevent memory leaks
        handler.removeCallbacks(runnable)
    }
}