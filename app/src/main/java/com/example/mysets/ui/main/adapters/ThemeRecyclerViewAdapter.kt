package com.example.mysets.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.databinding.SingleThemeBinding
import com.example.mysets.models.GroupedThemes

class ThemesRecyclerViewAdapter : RecyclerView.Adapter<ThemesRecyclerViewAdapter.ViewHolder>() {
    private val listOfThemes = mutableListOf<GroupedThemes>()

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

    fun swapList(list: MutableList<GroupedThemes>) {
        listOfThemes.clear()
        listOfThemes.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: SingleThemeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: GroupedThemes) {
            binding.theme = item
            binding.executePendingBindings()
        }
    }
}