package com.application.afol.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.R
import com.application.afol.ui.adapters.BricksRecyclerViewAdapter
import com.application.afol.utility.doNothing
import com.application.afol.utility.isInternetAvailable
import com.application.afol.utility.setVisibility
import com.application.afol.vm.bricksListViewModel.BrickListViewModel
import com.application.afol.vm.bricksListViewModel.BrickListViewModelFactory
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.activity_bricks_list.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

private const val PAGE_SIZE = 30

private const val COUNTS_COLUMNS = 3

class BricksListActivity : AppCompatActivity(), KodeinAware {

    companion object {
        private const val LEGO_SET_NUMBER = "legoSet"
        fun getIntent(context: Context, legoSetNumber: String): Intent =
            Intent(
                context,
                BricksListActivity::class.java
            )
                .putExtra(LEGO_SET_NUMBER, legoSetNumber)
    }

    override val kodein: Kodein by kodein()

    private val legoSetNumber: String by lazy { intent.getStringExtra(LEGO_SET_NUMBER).toString() }

    private val brickListViewModelFactory: BrickListViewModelFactory by instance()

    private var pageCounter = 1

    private lateinit var brickListViewModel: BrickListViewModel

    private lateinit var bricksRecyclerViewAdapter: BricksRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bricks_list)
        setViewModel()
        setUpToolbar()
        bricksRecyclerViewAdapter = BricksRecyclerViewAdapter()
        if (isInternetAvailable()) {
            group_parts.setVisibility(true)
            no_internet_information.setVisibility(false)
            bricksRecyclerViewAdapter.endMarker = { marker ->
                if (marker) {
                    pageCounter++
                    getBricks(pageCounter, legoSetNumber, PAGE_SIZE)
                }
            }
            getBricks(pageCounter, legoSetNumber, PAGE_SIZE)
        } else {
            group_parts.setVisibility(false)
            no_internet_information.setVisibility(true)
        }
        getSuccessRespond()
        getErrorRespond()
        getExceptionRespond()
    }

    override fun onBackPressed() {
        finish()

        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.view -> {
            brickListViewModel.toggleIsList()
            true
        }
        else -> {
            finish()
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_parts_list_view, menu)

        brickListViewModel.isList.observe(this, Observer { isList ->
            setIconViewState(menu!!, isList)
            setLayoutManager(bricks_recycler_view, isList)
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun setLayoutManager(recyclerView: RecyclerView, isList: Boolean) = with(recyclerView) {
        bricksRecyclerViewAdapter.isListMarker = isList
        if (isList) {
            layoutManager = LinearLayoutManager(applicationContext)
        } else {
            layoutManager = GridLayoutManager(applicationContext, COUNTS_COLUMNS)
        }.also {
            adapter =
                ScaleInAnimationAdapter(AlphaInAnimationAdapter(bricksRecyclerViewAdapter))
            bricksRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    private fun setIconViewState(menu: Menu, isList: Boolean) =
        menu.findItem(R.id.view).setIcon(if (isList) R.drawable.ic_grid else R.drawable.ic_list)

    private fun setViewModel() {
        brickListViewModel = ViewModelProviders.of(this, brickListViewModelFactory)
            .get(BrickListViewModel::class.java)
    }

    private fun setUpToolbar() {
        setSupportActionBar(bricks_toolbar_id)
        with(supportActionBar!!) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            title = getString(R.string.parts_list_title_prefix) + " " + legoSetNumber.dropLast(2)
        }
    }

    private fun getBricks(page: Int, setNumber: String, pageSize: Int) =
        brickListViewModel.getBricksFromSet(page, setNumber, pageSize)

    private fun getSuccessRespond() =
        brickListViewModel.getBricksSuccess.observe(this, Observer {
            if (pageCounter == 1) {
                bricksRecyclerViewAdapter.swapList(it.results)
                count_bricks_value.text = it.count.toString()
            } else {
                bricksRecyclerViewAdapter.addToList(it.results)
            }
        })

    private fun getErrorRespond() =
        brickListViewModel.getBricksError.observe(this, Observer {
            doNothing
        })

    private fun getExceptionRespond() =
        brickListViewModel.getBricksException.observe(this, Observer {
            doNothing
        })
}
