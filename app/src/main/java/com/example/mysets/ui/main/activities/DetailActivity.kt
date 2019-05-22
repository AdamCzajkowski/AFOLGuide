package com.example.mysets.ui.main.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.R
import com.example.mysets.databinding.ActivityDetailBinding
import com.example.mysets.models.LegoSet
import com.example.mysets.ui.main.adapters.BindingAdapter
import com.example.mysets.ui.main.adapters.MOCRecyclerViewAdapter
import com.example.mysets.view.model.detailViewModel.DetailViewModel
import com.example.mysets.view.model.detailViewModel.DetailViewModelFactory
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

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModelFactory: DetailViewModelFactory by instance()

    private lateinit var detailViewModel: DetailViewModel

    private val legoSet: LegoSet by lazy { intent.getParcelableExtra<LegoSet>(LEGO_SET) }

    lateinit var mocRecyclerViewAdapter: MOCRecyclerViewAdapter

    var isInMySetsMarker: ((Boolean) -> Unit)? = null

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

        checkIsInAnyDatabase()
        initializeMySetsFAB()
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
            mocRecyclerViewAdapter.swapList(it.results.toMutableList())
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

    private fun checkIsInAnyDatabase() {
        GlobalScope.launch(Dispatchers.Main) {
            isInMySets()
        }
    }

    private fun initializeViewModel() {
        detailViewModel =
            ViewModelProviders.of(this, detailViewModelFactory).get(DetailViewModel::class.java)
    }

    private suspend fun isInMySets() {
        detailViewModel.getListOfMySets().observe(this, Observer { listOfMySets ->
            listOfMySets?.forEach {
                if (it.set_num == legoSet.set_num) {
                    isInMySetsMarker?.invoke(true)
                    return@Observer
                }
            }
            isInMySetsMarker?.invoke(false)
            return@Observer
        })
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
    }

    private fun toastMessage(message: Int) {
        Toast.makeText(
            this,
            "${legoSet.name} ${getString(message)}",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setImageButtonIcon(imageButton: ImageView, drawable: Int) {
        imageButton.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                drawable
            )
        )
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        mocRecyclerViewAdapter = MOCRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        val alphaAdapter = AlphaInAnimationAdapter(mocRecyclerViewAdapter)
        recyclerView.adapter = ScaleInAnimationAdapter(alphaAdapter)
    }

    private fun initializeMySetsFAB() {
        isInMySetsMarker = { marker ->
            Log.i("test", "mysets: $marker")
            if (marker) {
                setImageButtonIcon(my_sets_button, R.drawable.wishlist_added_icon)
                my_sets_text.text = getString(R.string.remove_from_my_sets_text)
            } else {
                setImageButtonIcon(my_sets_button, R.drawable.wishlist_unadded_icon)
                my_sets_text.text = getString(R.string.add_to_my_sets_text)
            }
            my_sets_button.setOnClickListener {
                if (marker) {
                    detailViewModel.removeFromMySets(legoSet)
                    setImageButtonIcon(my_sets_button, R.drawable.wishlist_unadded_icon)
                    toastMessage(R.string.removed_from_my_sets)
                    my_sets_text.text = getString(R.string.add_to_my_sets_text)
                } else {
                    detailViewModel.addToMySets(legoSet)
                    setImageButtonIcon(my_sets_button, R.drawable.wishlist_added_icon)
                    toastMessage(R.string.added_to_my_sets)
                    my_sets_text.text = getString(R.string.remove_from_my_sets_text)
                }
            }
        }
    }

    private fun partListButtonReaction() {
        startActivity(BricksListActivity.getIntent(this, legoSet.set_num!!))
    }
}