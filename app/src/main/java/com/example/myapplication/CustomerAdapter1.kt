package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class CustomerAdapter1(
    val context: Context,
    val img: Array<Int>,
    val text: Array<String>,
    val price: Array<String>,
    val onTryClickListener: OnTryClickListener
) : RecyclerView.Adapter<CustomerAdapter1.CustomViewHolder>() {

    interface OnTryClickListener {
        fun onTryClick(position: Int)
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tryButton: Button = itemView.findViewById(R.id.b2)

        fun bindValue(image: Int, txt: String, price: String, position: Int) {
            itemView.findViewById<ImageView>(R.id.imageView).setImageResource(image)
            itemView.findViewById<TextView>(R.id.text1).text = txt
            itemView.findViewById<TextView>(R.id.text2).text = price

            tryButton.setOnClickListener {
                onTryClickListener.onTryClick(position)
                when (position) {
                    0, 1, 7 -> {
                        val intent = Intent(context, drawpen::class.java)
                        context.startActivity(intent)
                    }

                    9 -> {
                        val intent = Intent(context, drawpenc::class.java)
                        context.startActivity(intent)
                    }
                    // Add more cases if needed
                    else -> {
                        // Handle other positions if needed
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item1, parent, false)
        return CustomViewHolder(v)
    }

    override fun getItemCount(): Int {
        return img.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindValue(img[position], text[position], price[position], position)

        // Set the visibility of the try button based on your condition
        if (shouldShowTryButton(position)) {
            holder.tryButton.visibility = View.VISIBLE
        } else {
            holder.tryButton.visibility = View.GONE
        }
    }

    private fun shouldShowTryButton(position: Int): Boolean {
        // Add your logic here to determine if the "Try" button should be visible for this position
        // For example, specify the list of positions for which the button should be visible
        val visiblePositions = listOf(0, 1, 7, 9)
        return visiblePositions.contains(position)
    }
}
