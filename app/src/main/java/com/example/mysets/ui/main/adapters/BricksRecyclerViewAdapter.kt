package com.example.mysets.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.databinding.SingleBrickRowBinding
import com.example.mysets.models.BrickResult


class BricksRecyclerViewAdapter : RecyclerView.Adapter<BricksRecyclerViewAdapter.ViewHolder>() {
    private var listOfBricks = mutableListOf<BrickResult>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val binding = SingleBrickRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfBricks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listOfBricks[position])
    }

    fun swapList(list: MutableList<BrickResult>) {
        listOfBricks.clear()
        listOfBricks.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: SingleBrickRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: BrickResult) {
            binding.brickColor = item.results.first().color
            binding.brickDetail = item.results.first().part
            binding.brickQuantity = item.results.first()
        }
    }
}