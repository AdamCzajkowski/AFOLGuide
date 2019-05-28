package com.example.mysets.ui.main.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.mysets.R
import com.example.mysets.ui.main.fragments.MySetsFragment
import com.example.mysets.ui.main.fragments.SearchBrickFragment
import com.example.mysets.ui.main.fragments.SearchSetFragment
import com.example.mysets.ui.main.fragments.ThemesFragment


private val TAB_TITLES = arrayOf(
    R.string.tab_title_my,
    R.string.tab_title_search,
    R.string.tab_title_search_part,
    R.string.Themes_tab_name
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> MySetsFragment()
            1 -> SearchSetFragment()
            2 -> SearchBrickFragment()
            3 -> ThemesFragment()
            else -> throw IllegalStateException("Position $position exceeds the fragment count")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 4
    }
}