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
import com.example.mysets.ui.main.adapters.BricksRecyclerViewAdapter
import com.example.mysets.view.model.bricksListViewModel.BrickListViewModel
import com.example.mysets.view.model.bricksListViewModel.BrickListViewModelFactory
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.activity_bricks_list.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

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

    private val legoSetNumber: String by lazy { intent.getStringExtra(LEGO_SET_NUMBER) }

    private lateinit var brickListViewModel: BrickListViewModel

    private lateinit var bricksRecyclerViewAdapter: BricksRecyclerViewAdapter

    private val pageSize = 30

    private var pageCounter = 1

    private val brickListViewModelFactory: BrickListViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bricks_list)
        setViewModel()
        setUpToolbar()
        initializeRecyclerView(bricks_recycler_view)
        bricksRecyclerViewAdapter.endMarker = { marker ->
            if (marker) {
                pageCounter++
                getBricks(pageCounter, legoSetNumber, pageSize)
            }
        }
        getBricks(pageCounter, legoSetNumber, pageSize)
        getSuccessRespond()
        getErrorRespond()
        getExceptionRespond()

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
        bricksRecyclerViewAdapter = BricksRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        val alphaAdapter = AlphaInAnimationAdapter(bricksRecyclerViewAdapter)
        recyclerView.adapter = ScaleInAnimationAdapter(alphaAdapter)
    }

    private fun setViewModel() {
        brickListViewModel = ViewModelProviders.of(this, brickListViewModelFactory)
            .get(BrickListViewModel::class.java)
    }

    private fun setUpToolbar() {
        setSupportActionBar(bricks_toolbar_id)
        with(supportActionBar!!) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(true)
            title = legoSetNumber
        }
    }

    private fun getBricks(page: Int, setNumber: String, pageSize: Int) {
        brickListViewModel.getBricksFromSet(page, setNumber, pageSize)
    }

    private fun getSuccessRespond() {
        brickListViewModel.getBricksSuccess.observe(this, Observer {
            if (pageCounter == 1) {
                bricksRecyclerViewAdapter.swapList(it.results)
                count_bricks_value.text = it.count.toString()
                Log.i("searchbrick", "success $pageCounter")
            } else {
                bricksRecyclerViewAdapter.addToList(it.results)
                Log.i("searchbrick", "success $pageCounter")
            }
        })
    }

    private fun getErrorRespond() {
        brickListViewModel.getBricksError.observe(this, Observer {
            Log.i("searchbrick", "error")
        })
    }

    private fun getExceptionRespond() {
        brickListViewModel.getBricksException.observe(this, Observer {
            Log.i("searchbrick", "$it")
        })
    }
}
