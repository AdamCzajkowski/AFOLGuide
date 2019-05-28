package com.example.mysets.ui.main.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.R
import com.example.mysets.models.GroupedThemes
import com.example.mysets.models.LegoSet
import com.example.mysets.ui.main.adapters.LegoRecyclerViewAdapter
import com.example.mysets.view.model.themeActivityViewModel.ThemeActivityViewModel
import com.example.mysets.view.model.themeActivityViewModel.ThemeActivityViewModelFactory
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.activity_lego_set_from_theme.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LegoSetFromThemeActivity : AppCompatActivity(), KodeinAware {

    companion object {
        private const val GROUPED_THEMES = "listOfGroupedThemes"
        fun getIntent(context: Context, groupedThemes: GroupedThemes): Intent =
            Intent(context, LegoSetFromThemeActivity::class.java)
                .putExtra(GROUPED_THEMES, groupedThemes)
    }

    override val kodein: Kodein by kodein()

    private val groupedThemes: GroupedThemes by lazy {
        intent.getParcelableExtra<GroupedThemes>(
            GROUPED_THEMES
        )
    }

    private val pageSize = 200

    private val page = 1

    private var counter = 1

    private val themeActivityViewModelFactory: ThemeActivityViewModelFactory by instance()

    private lateinit var themeActivityViewModel: ThemeActivityViewModel

    private lateinit var legoRecyclerViewAdapter: LegoRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lego_set_from_theme)
        setViewModel()
        initializeRecyclerView(lego_themes_recycler_view)
        setUpToolbar()
        getSuccessRespond()
        getErrorRespond()
        getExceptionRespond()
        startFetchLegoSet()
        legoRecyclerViewAdapter.selectedItem = { legoSet ->
            startDetailActivity(legoSet)
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

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        legoRecyclerViewAdapter = LegoRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        val alphaAdapter = AlphaInAnimationAdapter(legoRecyclerViewAdapter)
        recyclerView.adapter = ScaleInAnimationAdapter(alphaAdapter)
    }

    private fun setViewModel() {
        themeActivityViewModel = ViewModelProviders.of(this, themeActivityViewModelFactory)
            .get(ThemeActivityViewModel::class.java)
    }

    private fun setUpToolbar() {
        setSupportActionBar(theme_legoSet_toolbar_id)
        with(supportActionBar!!) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            title = groupedThemes.name
        }
    }

    private fun startFetchLegoSet() {
        themeActivityViewModel.getThemes(page, pageSize, groupedThemes.listOfID.first())

        Log.i("themeset", "counter $counter")
        legoRecyclerViewAdapter.endMarker = { marker ->
            if (marker && counter <= groupedThemes.listOfID.size) {
                themeActivityViewModel.getThemes(
                    page,
                    pageSize,
                    groupedThemes.listOfID[counter - 1]
                )

                Log.i("themeset", "counter increment $counter")
            }
        }
    }

    private fun getSuccessRespond() {
        themeActivityViewModel.getThemesSuccess.observe(this, Observer {
            if (counter == 1) {
                legoRecyclerViewAdapter.swapList(it.results)
                Log.i(
                    "themeset",
                    "id = ${groupedThemes.listOfID[counter - 1]}, swaplist counter $counter"
                )

            } else {
                legoRecyclerViewAdapter.addToList(it.results)
                Log.i(
                    "themeset",
                    " --------> id = ${groupedThemes.listOfID[counter - 1]}, add to list counter $counter"
                )
            }
            counter++
        })
    }

    private fun getErrorRespond() {
        themeActivityViewModel.getThemesError.observe(this, Observer {
            Log.i("searchThemeSet", it)
        })
    }

    private fun getExceptionRespond() {
        themeActivityViewModel.getThemesException.observe(this, Observer {
            Log.i("searchThemeSet", it.message)
        })
    }

    private fun startDetailActivity(legoSet: LegoSet) {
        startActivity(DetailActivity.getIntent(this, legoSet))
    }
}
