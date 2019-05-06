package com.example.mysets.ui.main

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.mysets.R
import com.example.mysets.ui.main.fragments.PlaceholderFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_title_my,
    R.string.tab_title_wishlist,
    R.string.tab_title_search
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> PlaceholderFragment.newInstance()
            1 -> PlaceholderFragment.newInstance()
            2 -> PlaceholderFragment.newInstance()
            else -> throw IllegalStateException("Position $position exceeds the fragment count")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        return fragment
    }

    override fun getCount(): Int {
        return 3
    }
}