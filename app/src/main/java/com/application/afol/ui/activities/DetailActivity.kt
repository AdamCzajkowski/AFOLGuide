package com.application.afol.ui.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.R
import com.application.afol.databinding.ActivityDetailBinding
import com.application.afol.models.LegoSet
import com.application.afol.ui.adapters.BindingAdapter
import com.application.afol.ui.adapters.MOCRecyclerViewAdapter
import com.application.afol.utility.setVisibility
import com.application.afol.vm.detailViewModel.DetailViewModel
import com.application.afol.vm.detailViewModel.DetailViewModelFactory
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.activity_detail.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class DetailActivity : AppCompatActivity(), KodeinAware {

    companion object {
        const val LEGO_SET = "legoSetNumber"
        fun getIntent(context: Context, legoSet: LegoSet): Intent =
            Intent(
                context,
                DetailActivity::class.java
            )
                .putExtra(LEGO_SET, legoSet)
    }

    override val kodein: Kodein by kodein()

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModelFactory: DetailViewModelFactory by instance()

    private lateinit var detailViewModel: DetailViewModel

    private val legoSet: LegoSet by lazy { intent.getParcelableExtra<LegoSet>(LEGO_SET) }

    private lateinit var mocRecyclerViewAdapter: MOCRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        initializeViewModel()
        getErrorRespond()
        getExceptionRespond()
        getSuccessRespond()
        getMocs()
        setUpDetailsToolbar()
        setUpDetailsCollapsingToolbar()
        bindView()
        initializeRecyclerView(moc_recycler_view_id)
        parts_list_button.setOnClickListener {
            partListButtonReaction()
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun getMocs() {
        detailViewModel.getMOCs(legoSet.set_num!!)
    }

    private fun getErrorRespond() {
        detailViewModel.getMocsError.observe(
            this,
            Observer {
                Log.i("searchError", it)
            }
        )
    }

    private fun getExceptionRespond() {
        detailViewModel.getMocsException.observe(
            this,
            Observer {
                Log.i("searchException", it.message.toString())
            }
        )
    }

    private fun getSuccessRespond() {
        detailViewModel.getMocsSuccess.observe(this, Observer {
            mocRecyclerViewAdapter.listOfMoc = it.results.toMutableList()
            list_mocs_title.setVisibility(mocRecyclerViewAdapter.listOfMoc.isNotEmpty())
            set_number_mocs_list_value.setVisibility(mocRecyclerViewAdapter.listOfMoc.isNotEmpty())
        })
    }

    private fun setUpDetailsToolbar() {
        setSupportActionBar(lego_details_toolbar_id)
        with(supportActionBar!!) {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun setUpDetailsCollapsingToolbar() {
        with(lego_details_collapsing_toolbar_id) {
            setCollapsedTitleTextColor(android.graphics.Color.WHITE)
            setExpandedTitleColor(android.graphics.Color.WHITE)
        }
    }

    private fun initializeViewModel() {
        detailViewModel =
            ViewModelProviders.of(this, detailViewModelFactory).get(DetailViewModel::class.java)
    }

    private fun bindView() {
        binding.legoSet = legoSet
        val adapter = BindingAdapter
        binding.adapter = adapter
        adapter.bindURLParse = { url ->
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(url)
            startActivity(openURL)
        }
        adapter.bindImageToUrl = { url ->
            val openURLfromImage = Intent(Intent.ACTION_VIEW)
            openURLfromImage.data = Uri.parse(url)
            startActivity(openURLfromImage)
        }
        adapter.bindInstrctionURLParse = { set_num ->
            val openInstructionURL = Intent(Intent.ACTION_VIEW)
            openInstructionURL.data = Uri.parse(startInstructionSite(set_num))
            startActivity(openInstructionURL)
        }
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        mocRecyclerViewAdapter = MOCRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        val alphaAdapter = AlphaInAnimationAdapter(mocRecyclerViewAdapter)
        recyclerView.adapter = ScaleInAnimationAdapter(alphaAdapter)
    }

    private fun startInstructionSite(set_num: String) =
        "https://www.lego.com/pl-pl/service/buildinginstructions/search#?search&text=" + set_num.substring(
            0,
            set_num.length - 2
        )


    private fun partListButtonReaction() {
        startActivity(
            BricksListActivity.getIntent(
                this,
                legoSet.set_num!!
            )
        )
    }
}