package com.application.afol.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer
import com.application.afol.R
import com.application.afol.ui.InformationDialog
import com.application.afol.ui.adapters.SectionsPagerAdapter
import com.application.afol.utility.showDialog
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpBottomNavigationView()
        name_set_text_name_id.setOnClickListener {
            InformationDialog(
                this,
                R.style.Theme_AppCompat_DayNight_Dialog_Alert
            ).showDialog()
        }
    }

    private fun setUpBottomNavigationView() {
        val navController = findNavController(R.id.nav_host_fragment_id)
        NavigationUI.setupWithNavController(navigation, navController)
    }
}