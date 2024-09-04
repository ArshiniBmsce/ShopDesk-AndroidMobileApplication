package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class drawpen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawpen)

        val penDrawingView = findViewById<PenDrawingView>(R.id.penDrawingView)
        val b1=findViewById<Button>(R.id.b7)
        val b2=findViewById<Button>(R.id.b8)
        b2.setOnClickListener {
            val intent= Intent(this,stationary::class.java)
            startActivity(intent)
        }
        b1.setOnClickListener {
            penDrawingView.clearDrawing()
        }
        val blueButton: Button = findViewById(R.id.blueButton)
        blueButton.setOnClickListener {
            penDrawingView.setColor(Color.BLUE)
        }

        // Button for selecting black color
        val blackButton: Button = findViewById(R.id.blackButton)
        blackButton.setOnClickListener {
            penDrawingView.setColor(Color.BLACK)
        }

        // Button for selecting green color
        val greenButton: Button = findViewById(R.id.redButton)
        greenButton.setOnClickListener {
            penDrawingView.setColor(Color.RED)
        }
        // You can add additional functionality or setup here as needed
    }
}
