package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ActAdapter(
    private val actItems: List<ActItem>
) : RecyclerView.Adapter<ActAdapter.ActViewHolder>() {

    inner class ActViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tid: TextView = itemView.findViewById(R.id.tid)
        val date: TextView = itemView.findViewById(R.id.date)
        val time: TextView = itemView.findViewById(R.id.item_gmt)
        val items: TextView = itemView.findViewById(R.id.items_list)
        val pay: ImageView = itemView.findViewById(R.id.imageView1)
        val collect: ImageView = itemView.findViewById(R.id.imageView2)
        val txtpay: TextView = itemView.findViewById(R.id.StatusPay)
        val txtcoll: TextView = itemView.findViewById(R.id.StatusCollect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_act, parent, false)
        return ActViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActViewHolder, position: Int) {
        val currentItem = actItems[position]
        holder.tid.text = "TID: ${currentItem.TID}"
        holder.date.text = "Date: ${currentItem.date}"
        holder.time.text = "Time (GMT): ${currentItem.time}"
        holder.txtpay.text = currentItem.payStatus
        holder.txtcoll.text = currentItem.collectStatus

        val itemsString = StringBuilder()
        for ((item, qty) in currentItem.items) {
            itemsString.append("$item: $qty\n")
        }
        holder.items.text = itemsString.toString().trim()

        val resourceId1 = holder.itemView.context.resources.getIdentifier(currentItem.payStatus, "drawable", holder.itemView.context.packageName)
        if (resourceId1 != 0) {
            val drawable = holder.itemView.context.resources.getDrawable(resourceId1, null)
            holder.pay.setImageDrawable(drawable)
        }

        val resourceId2 = holder.itemView.context.resources.getIdentifier(currentItem.collectStatus, "drawable", holder.itemView.context.packageName)
        if (resourceId2 != 0) {
            val drawable = holder.itemView.context.resources.getDrawable(resourceId2, null)
            holder.collect.setImageDrawable(drawable)
        }

    }

    override fun getItemCount() = actItems.size
}
