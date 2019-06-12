package com.example.mysets.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.databinding.SingleThemeBinding
import com.example.mysets.models.GroupedThemes
import com.example.mysets.utility.autoNotify
import kotlin.properties.Delegates

class ThemesRecyclerViewAdapter : RecyclerView.Adapter<ThemesRecyclerViewAdapter.ViewHolder>() {
    var listOfThemes: MutableList<GroupedThemes> by Delegates.observable(mutableListOf()) { _, oldList, newList ->
        autoNotify(oldList, newList) { old, new -> old.listOfID.first() == new.listOfID.first() }
    }

    var selectedItem: ((GroupedThemes) -> Unit)? = null

    override fun getItemCount(): Int {
        return listOfThemes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(listOfThemes[position])

        holder.itemView.setOnClickListener {
            selectedItem?.invoke(listOfThemes[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val binding = SingleThemeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(val binding: SingleThemeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: GroupedThemes) {
            binding.theme = item
            binding.executePendingBindings()
        }
    }
}