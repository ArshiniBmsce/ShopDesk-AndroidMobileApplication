package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(
    private val img: Array<Int>,
    private val text: Array<String>,
    private val price: Array<String>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {


    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(v)
    }

    override fun getItemCount(): Int = img.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindValue(img[position], text[position], price[position])
    }

    private fun CustomViewHolder.bindValue(image: Int, txt: String, price: String) {
        itemView.findViewById<ImageView>(R.id.imageView).setImageResource(image)
        itemView.findViewById<TextView>(R.id.text1).text = txt
        itemView.findViewById<TextView>(R.id.text2).text = price
    }
}
