package com.application.afol.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.R
import com.application.afol.models.LegoSet
import com.application.afol.ui.activities.DetailActivity
import com.application.afol.ui.adapters.MySetsRecyclerViewAdapter
import com.application.afol.utility.DragManageDeleteAdapter
import com.application.afol.utility.gone
import com.application.afol.utility.show
import com.application.afol.vm.mySetsViewModel.MySetsViewModel
import com.application.afol.vm.mySetsViewModel.MySetsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.fragment_my_sets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.kodein
import org.kodein.di.generic.instance

class MySetsFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()

    private val mySetsViewModelFactory: MySetsViewModelFactory by instance()

    private lateinit var mySetsViewModel: MySetsViewModel

    private lateinit var mySetsRecyclerViewAdapter: MySetsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_sets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeLegoViewModel()
        initializeRecyclerView(rv_my_sets_id)
        getAllMySetsFromDatabase()
        mySetsRecyclerViewAdapter.selectedItem = {
            singleItemClickedReaction(it)
        }
        setupItemTouchHelper()
        removeFromMySetsOnSwipe()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getAllMySetsFromDatabase() {
        GlobalScope.launch(Dispatchers.Main) {
            getAllMySets()
        }
    }

    private fun initializeLegoViewModel() {
        mySetsViewModel =
            ViewModelProviders.of(this, mySetsViewModelFactory).get(MySetsViewModel::class.java)
    }

    private fun singleItemClickedReaction(legoSet: LegoSet) = startDetailActivity(legoSet)

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        mySetsRecyclerViewAdapter = MySetsRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        val alphaAdapter = AlphaInAnimationAdapter(mySetsRecyclerViewAdapter)
        recyclerView.adapter = ScaleInAnimationAdapter(alphaAdapter)
    }

    private fun setupItemTouchHelper() {
        val callback = DragManageDeleteAdapter(
            mySetsRecyclerViewAdapter, context!!,
            0, ItemTouchHelper.LEFT
        )
        val helper = ItemTouchHelper(callback)
        helper.attachToRecyclerView(rv_my_sets_id)
    }

    private suspend fun getAllMySets() {
        mySetsViewModel.getListOfMySets().observe(this, Observer { listOfMySets ->
            mySetsRecyclerViewAdapter.listOfLegoSet = listOfMySets
            if (listOfMySets.isEmpty()) default_my_sets_view.show()
            else default_my_sets_view.gone()
        })
    }

    private fun removeFromMySetsOnSwipe() {
        mySetsRecyclerViewAdapter.swipedItem = { legoSet ->
            mySetsViewModel.removeFromMySets(legoSet)
            showUndoSnackbar(legoSet)
        }
    }

    private fun showUndoSnackbar(legoSet: LegoSet) {
        val snackbar = Snackbar.make(
            view!!, legoSet.name + " " + getString(R.string.deleted_item_snack_bar_text),
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(" " + getString(R.string.undo_snack_bar_text)) { undoDelete() }
        snackbar.show()
    }
    

    private fun undoDelete() {
        mySetsRecyclerViewAdapter.let {
            it.listOfLegoSet.add(
                it.mRecentlyDeletedItemPosition,
                it.mRecentlyDeletedItem
            )
            mySetsViewModel.addToMySets(it.mRecentlyDeletedItem)
            it.notifyDataSetChanged()
        }
    }

    private fun startDetailActivity(legoSet: LegoSet) {
        startActivity(DetailActivity.getIntent(context!!, legoSet))
    }
}