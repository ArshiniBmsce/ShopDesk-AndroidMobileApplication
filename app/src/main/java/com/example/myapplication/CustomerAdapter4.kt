
package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R


class CustomerAdapter4(val img:Array<Int>,
                       val text:Array<String>, val price:Array<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item4, parent, false)
        return CustomViewHolder(v)
    }


    override fun getItemCount(): Int {
        return img.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.run { bindvalue(img[position], text[position],price[position]) }
    }


    private fun RecyclerView.ViewHolder.bindvalue(image: Int, txt: String,price: String) {
        itemView.findViewById<ImageView>(R.id.imageView).setImageResource(image)
        itemView.findViewById<TextView>(R.id.text1).text = txt
        itemView.findViewById<TextView>(R.id.text2).text = price

    }
}