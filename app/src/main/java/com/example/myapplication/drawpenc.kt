package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class drawpenc : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawpenc)
        val penDrawingView = findViewById<PencilDrawingView>(R.id.pencilDrawingView)
        val b1=findViewById<Button>(R.id.b7)
        val b2=findViewById<Button>(R.id.b8)
        b2.setOnClickListener {
            val intent= Intent(this,stationary::class.java)
            startActivity(intent)
        }
        b1.setOnClickListener {
                    penDrawingView.clearDrawing()
                }
    }
}