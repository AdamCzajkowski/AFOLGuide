package com.example.mysets.ui.main.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.databinding.SingleBrickRowBinding
import com.example.mysets.models.BrickResult


class BricksRecyclerViewAdapter : RecyclerView.Adapter<BricksRecyclerViewAdapter.ViewHolder>() {
    private var listOfBricks = mutableListOf<BrickResult.Result>()

    lateinit var context: Context
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
        holder.bindView(listOfBricks[position])
        holder.createAdapter(context)
    }

    fun swapList(list: MutableList<BrickResult.Result>) {
        listOfBricks.clear()
        listOfBricks.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: SingleBrickRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: BrickResult.Result) {
            binding.brickColor = item.color
            binding.brickDetail = item.part
            binding.brickQuantity = item
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