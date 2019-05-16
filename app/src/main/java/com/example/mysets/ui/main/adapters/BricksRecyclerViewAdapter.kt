package com.example.mysets.ui.main.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.databinding.SingleBrickRowBinding
import com.example.mysets.models.BrickResult
import kotlinx.android.synthetic.main.single_brick_row.view.*


class BricksRecyclerViewAdapter : RecyclerView.Adapter<BricksRecyclerViewAdapter.ViewHolder>() {
    private var listOfBricks = mutableListOf<BrickResult.Result>()

    var endMarker: ((Boolean) -> Unit)? = null

    lateinit var context: Context

    private var multiplier = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val binding = SingleBrickRowBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfBricks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listOfBricks[position], position + 1)
        holder.createAdapter(context)
        if (position == (multiplier * 30) - 10) {
            multiplier++
            endMarker?.invoke(true)
            Log.i("searchbrick", "endMarker true multiplier $multiplier")
        } else {
            endMarker?.invoke(false)
            Log.i("searchbrick", "endMarker false multiplier $multiplier")
        }
    }

    fun swapList(list: MutableList<BrickResult.Result>) {
        listOfBricks.clear()
        listOfBricks.addAll(list)
        notifyDataSetChanged()
    }

    fun addToList(list: MutableList<BrickResult.Result>) {
        listOfBricks.addAll(list)
        notifyItemRangeChanged(itemCount - list.size, itemCount)
    }

    class ViewHolder(val binding: SingleBrickRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: BrickResult.Result, position: Int) {
            binding.brickColor = item.color
            binding.brickDetail = item.part
            binding.brickQuantity = item
            itemView.position_value.text = position.toString()
        }

        fun createAdapter(context: Context) {
            val adapter = BindingAdapter
            binding.adapter = adapter
            adapter.bindURLParse = { url ->
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(url)
                startActivity(context, openURL, Bundle())
            }
        }
    }
}