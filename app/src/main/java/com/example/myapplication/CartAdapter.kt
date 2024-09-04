package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController


class CartAdapter(
    private val cartItems: List<CartItem>,
    private val onDeleteClick: (Int) -> Unit,
    private val onAddClick: (Int) -> Unit,
    private val onSubtractClick: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textItemName: TextView = itemView.findViewById(R.id.textItemName)
        val textItemNo: TextView = itemView.findViewById(R.id.textItemNo)
        val textItemCost: TextView = itemView.findViewById(R.id.textItemCost)
        val textItemQuantity: TextView = itemView.findViewById(R.id.textItemQuantity)
        val item_img = itemView.findViewById<ImageView>(R.id.imageView)
        val btnAdd: Button = itemView.findViewById(R.id.btnAdd)
        val btnSubtract: Button = itemView.findViewById(R.id.btnSubtract)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        init {
            btnAdd.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onAddClick(position)
                }
            }
            btnSubtract.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onSubtractClick(position)
                }

            }
            btnDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClick(position)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartItems[position]
        holder.textItemName.text = currentItem.name
        holder.textItemNo.text = "Number: ${currentItem.no}"
        holder.textItemCost.text = "Cost: ${currentItem.cost}"
        holder.textItemQuantity.text = "Quantity: ${currentItem.quantity}"

        val resourceId = holder.itemView.context.resources.getIdentifier(currentItem.img, "drawable", holder.itemView.context.packageName)
        if (resourceId != 0) {
            val drawable = holder.itemView.context.resources.getDrawable(resourceId, null)
            holder.item_img.setImageDrawable(drawable)
        }
    }

    override fun getItemCount() = cartItems.size
}
