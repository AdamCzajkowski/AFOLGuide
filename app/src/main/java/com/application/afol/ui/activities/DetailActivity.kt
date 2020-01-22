package com.application.afol.ui.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.application.afol.utility.doNothing
import com.application.afol.utility.dropLastTwoChars
import com.application.afol.utility.setVisibility
import com.application.afol.utility.showSnackbar
import com.application.afol.vm.detailViewModel.DetailViewModel
import com.application.afol.vm.detailViewModel.DetailViewModelFactory
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    private val legoSet: LegoSet by lazy { intent.getParcelableExtra<LegoSet>(LEGO_SET) }

    private val detailViewModelFactory: DetailViewModelFactory by instance()

    private var isSetInFav = false

    private lateinit var binding: ActivityDetailBinding

    private lateinit var detailViewModel: DetailViewModel

    private val listOfFavoriteLocal = mutableListOf<LegoSet>()

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
        GlobalScope.launch(Dispatchers.Main) {
            getFavorites()
        }
        initializeRecyclerView(moc_recycler_view_id)
        parts_list_button.setOnClickListener {
            partListButtonReaction()
        }
        imageButtonFavorite.setOnClickListener {
            if (isSetInFav) {
                removeFromFavorites(legoSet)
            } else {
                addToFavorites(legoSet)
            }
        }
    }

    override fun onBackPressed() =
        finish().also { super.onBackPressed() }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun removeFromFavorites(legoSet: LegoSet) {
        listOfFavoriteLocal.find { removedLegoSet ->
            removedLegoSet.set_num == legoSet.set_num
        }?.let {
            listOfFavoriteLocal.remove(it)
            detailViewModel.removeFromFavorites(it)
            showSnackBarFavoriteStatus(false, legoSet)
            isSetInFav = false
        }
    }

    private fun addToFavorites(legoSet: LegoSet) {
        if (listOfFavoriteLocal.find { addingLegoSet -> addingLegoSet.set_num == legoSet.set_num } == null) {
            detailViewModel.addToFavorites(legoSet)
            showSnackBarFavoriteStatus(true, legoSet)
            isSetInFav = true
        } else {
            doNothing
        }

    }

    private fun showSnackBarFavoriteStatus(status: Boolean, legoSet: LegoSet) {
        if (status) {
            coordinatorLayout.showSnackbar(legoSet.name + " " + getString(R.string.added_to_favorites_snack_bar_text))
        } else {
            coordinatorLayout.showSnackbar(legoSet.name + " " + getString(R.string.deleted_item_snack_bar_text))
        }
    }

    private fun setAddToFavoriteText(isInFavorite: Boolean) =
        if (isInFavorite) {
            favorite_text.text = getString(R.string.details_favorite_text_remove)
        } else {
            favorite_text.text = getString(R.string.details_favorite_text_add)
        }

    private suspend fun getFavorites() =
        detailViewModel.getListOfFavorites().observe(this, Observer { listOfFavorites ->
            listOfFavoriteLocal.clear()
            listOfFavoriteLocal.addAll(listOfFavorites)
            listOfFavorites.forEach {
                if (it.set_num == legoSet.set_num) isSetInFav = true
            }
            imageButtonFavorite.isSelected = isSetInFav
            setAddToFavoriteText(isSetInFav)
        })

    private fun getMocs() = legoSet.set_num?.let { detailViewModel.getMOCs(it) }

    private fun getErrorRespond() {
        detailViewModel.getMocsError.observe(
            this,
            Observer {
                doNothing
            }
        )
    }

    private fun getExceptionRespond() {
        detailViewModel.getMocsException.observe(
            this,
            Observer {
                doNothing
            }
        )
    }

    private fun getSuccessRespond() {
        detailViewModel.getMocsSuccess.observe(this, Observer {
            with(mocRecyclerViewAdapter) {
                updateList(it.results.toMutableList())
                group_moc.setVisibility(listOfMoc.isNotEmpty())
            }
        })
    }

    private fun setUpDetailsToolbar() {
        setSupportActionBar(lego_details_toolbar_id)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        } ?: error("supportActionBar is null")
        lego_details_collapsing_toolbar_id.title = legoSet.set_num?.dropLastTwoChars()
    }

    private fun setUpDetailsCollapsingToolbar() =
        with(lego_details_collapsing_toolbar_id) {
            setCollapsedTitleTextColor(android.graphics.Color.WHITE)
            setExpandedTitleColor(android.graphics.Color.WHITE)
        }

    private fun initializeViewModel() {
        detailViewModel =
            ViewModelProviders.of(this, detailViewModelFactory).get(DetailViewModel::class.java)
    }

    private fun bindView() {
        binding.legoSet = legoSet
        val adapter = BindingAdapter
        binding.adapter = adapter
        with(adapter) {
            bindURLParse = { url ->
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(url)
                startActivity(openURL)
            }
            bindMocToUrl = { url ->
                val openMocFromUrl = Intent(Intent.ACTION_VIEW)
                openMocFromUrl.data = Uri.parse(url)
                startActivity(openMocFromUrl)
            }
            bindInstrctionURLParse = { numberOfSet ->
                val openInstructionURL = Intent(Intent.ACTION_VIEW)
                openInstructionURL.data = Uri.parse(startInstructionSite(numberOfSet))
                startActivity(openInstructionURL)
            }
        }
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        mocRecyclerViewAdapter = MOCRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        val alphaAdapter = AlphaInAnimationAdapter(mocRecyclerViewAdapter)
        recyclerView.adapter = ScaleInAnimationAdapter(alphaAdapter)
    }

    private fun startInstructionSite(set_num: String) =
        "https://www.lego.com/pl-pl/service/buildinginstructions/search#?search&text=" +
                set_num.dropLastTwoChars()

    private fun partListButtonReaction() =
        legoSet.set_num?.let {
            startActivity(
                BricksListActivity.getIntent(
                    this,
                    it
                )
            )
        } ?: error("set number is unknown")
}
