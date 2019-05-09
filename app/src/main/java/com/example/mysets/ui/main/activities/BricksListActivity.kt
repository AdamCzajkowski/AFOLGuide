package com.example.mysets.ui.main.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mysets.R
import com.example.mysets.view.model.bricksListViewModel.BrickListViewModel
import com.example.mysets.view.model.bricksListViewModel.BrickListViewModelFactory
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
                DetailActivity::class.java
            )
                .putExtra(LEGO_SET_NUMBER, legoSetNumber)
    }

    override val kodein: Kodein by kodein()

    private val legoSetNumber: String by lazy { intent.getStringExtra(LEGO_SET_NUMBER) }

    private lateinit var brickListViewModel: BrickListViewModel

    private val brickListViewModelFactory: BrickListViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bricks_list)
        setViewModel()
        setUpToolbar()
        getBricks(1, legoSetNumber)
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

    private fun setViewModel() {
        brickListViewModel = ViewModelProviders.of(this, brickListViewModelFactory)
            .get(BrickListViewModel::class.java)
    }

    private fun setUpToolbar() {
        setSupportActionBar(bricks_toolbar_id)
        with(supportActionBar!!) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun getBricks(page: Int, setNumber: String) {
        brickListViewModel.getBricksFromSet(page, setNumber)
    }

    private fun getSuccessRespond() {
        brickListViewModel.getBricksSuccess.observe(this, Observer {
            Log.i("searchbrick", "success")
        })
    }

    private fun getErrorRespond() {
        brickListViewModel.getBricksError.observe(this, Observer {
            Log.i("searchbrick", "error")
        })
    }

    private fun getExceptionRespond() {
        brickListViewModel.getBricksException.observe(this, Observer {
            Log.i("searchbrick", "exception")
        })
    }
}
