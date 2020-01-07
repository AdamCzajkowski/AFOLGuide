package com.application.afol.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.databinding.SingleMocModelBinding
import com.application.afol.models.MOCResult

class MOCRecyclerViewAdapter : RecyclerView.Adapter<MOCRecyclerViewAdapter.ViewHolder>() {
    var listOfMoc = mutableListOf<MOCResult.Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val binding = SingleMocModelBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = listOfMoc.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listOfMoc[position])
    }

    fun updateList(listOfMocs: MutableList<MOCResult.Result>) {
        listOfMoc.clear()
        listOfMoc.addAll(listOfMocs)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: SingleMocModelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(resultMoc: MOCResult.Result) {
            with(binding) {
                moc = resultMoc
                executePendingBindings()
            }
        }
    }
}