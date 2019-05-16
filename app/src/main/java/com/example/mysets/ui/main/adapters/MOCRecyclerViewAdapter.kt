package com.example.mysets.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.databinding.SingleMocModelBinding
import com.example.mysets.models.MOCResult

class MOCRecyclerViewAdapter : RecyclerView.Adapter<MOCRecyclerViewAdapter.ViewHolder>() {
    private val listOfMoc = mutableListOf<MOCResult.Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val binding = SingleMocModelBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfMoc.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listOfMoc[position])
    }

    fun swapList(list: MutableList<MOCResult.Result>) {
        listOfMoc.clear()
        listOfMoc.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: SingleMocModelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(moc: MOCResult.Result) {
            binding.moc = moc
            binding.executePendingBindings()
        }
    }
}