package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class dash2 : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bottom_nav)
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
        replacefragment(home())
    }
    private fun replacefragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit()
    }
}