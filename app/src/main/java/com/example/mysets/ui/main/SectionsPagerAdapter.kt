package com.example.mysets.ui.main

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.mysets.R
import com.example.mysets.ui.main.fragments.MySetsFragment
import com.example.mysets.ui.main.fragments.SearchFragment
import com.example.mysets.ui.main.fragments.WishlistFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_title_my,
    R.string.tab_title_wishlist,
    R.string.tab_title_search
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> MySetsFragment()
            1 -> WishlistFragment()
            2 -> SearchFragment()
            else -> throw IllegalStateException("Position $position exceeds the fragment count")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}