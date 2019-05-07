package com.example.mysets.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mysets.R
import com.example.mysets.databinding.ActivityDetailBinding
import com.example.mysets.models.LegoSet
import kotlinx.android.synthetic.main.activity_detail.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class DetailActivity : AppCompatActivity(), KodeinAware {

    companion object {
        private const val LEGO_SET = "legoSet"
        fun getIntent(context: Context, legoSet: LegoSet): Intent =
            Intent(context,
                DetailActivity::class.java)
                .putExtra(LEGO_SET, legoSet)
    }

    override val kodein: Kodein by kodein()

    private lateinit var binding: ActivityDetailBinding

    private val legoSet: LegoSet by lazy { intent.getParcelableExtra<LegoSet>(LEGO_SET) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setUpDetailsToolbar()
        bindView()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    private fun setUpDetailsToolbar() {
        setSupportActionBar(toolbar)
        with(supportActionBar!!) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun bindView() {
        binding.legoSet = legoSet
    }

    private fun MySetSwitchReaction() {
        //my_sets_switch_id.
    }
}
