package com.example.mysets.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.databinding.SingleMocModelBinding
import com.example.mysets.models.MOCResult
import com.example.mysets.utility.autoNotify
import kotlin.properties.Delegates

class MOCRecyclerViewAdapter : RecyclerView.Adapter<MOCRecyclerViewAdapter.ViewHolder>() {
    var listOfMoc: MutableList<MOCResult.Result> by Delegates.observable(mutableListOf()) { _, oldList, newList ->
        autoNotify(oldList, newList) { old, new -> old.mocUrl == new.mocUrl }
    }

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

    class ViewHolder(private val binding: SingleMocModelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(moc: MOCResult.Result) {
            binding.moc = moc
            binding.executePendingBindings()
        }
    }
}