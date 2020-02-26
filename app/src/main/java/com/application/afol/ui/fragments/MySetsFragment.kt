package com.application.afol.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.afol.R
import com.application.afol.models.Label
import com.application.afol.models.LegoSet
import com.application.afol.ui.activities.DetailActivity
import com.application.afol.ui.adapters.MySetsRecyclerViewAdapter
import com.application.afol.utility.setVisibility
import com.application.afol.utility.showSnackbar
import com.application.afol.vm.mySetsViewModel.MySetsViewModel
import com.application.afol.vm.mySetsViewModel.MySetsViewModelFactory
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
        addLabelListener()
        removeFromMySets()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getAllMySetsFromDatabase() {
        GlobalScope.launch(Dispatchers.Main) {
            getAllMySets()
        }
    }

    override fun onResume() {
        mySetsRecyclerViewAdapter.notifyDataSetChanged()
        super.onResume()
    }

    private fun initializeLegoViewModel() {
        mySetsViewModel =
            ViewModelProviders.of(this, mySetsViewModelFactory).get(MySetsViewModel::class.java)
    }

    private fun singleItemClickedReaction(legoSet: LegoSet) = startDetailActivity(legoSet)

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        mySetsRecyclerViewAdapter = MySetsRecyclerViewAdapter()
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = ScaleInAnimationAdapter(AlphaInAnimationAdapter(mySetsRecyclerViewAdapter))
        }
    }

    private suspend fun getAllMySets() {
        mySetsViewModel.getListOfMySets().observe(this, Observer { listOfMySets ->
            mySetsRecyclerViewAdapter.listOfLegoSet = listOfMySets
            default_my_sets_view.setVisibility(listOfMySets.isEmpty())
            val counterValue = setupCounter(listOfMySets)
            with(my_sets_counter_bricks_sets) {
                text =
                    getString(R.string.my_sets_counter, counterValue.first, counterValue.second)
                setVisibility(listOfMySets.isEmpty().not())
            }
        })
    }

    private fun addLabelListener() {
        mySetsRecyclerViewAdapter.labelSelected = { legoSet ->
            mySetsViewModel.addLabel(legoSet, Label.COMPLETE)
        }
    }

    private fun isLabelcompatibility(legoSet: LegoSet, label: Label): Boolean {
        when (label) {
            Label.COMPLETE -> legoSet.listOfLabels?.find { searchedLabel ->
                searchedLabel == Label.INCOMPLETE }?.let {
                return false
            } ?: return true
            Label.INCOMPLETE -> legoSet.listOfLabels?.find { searchedLabel ->
                searchedLabel == Label.COMPLETE }?.let {
                return false
            } ?: return true
            Label.ASSEMBLED -> legoSet.listOfLabels?.find { searchedLabel ->
                searchedLabel == Label.UNASSAMBLED }?.let {
                return false
            } ?: return true
            Label.UNASSAMBLED -> legoSet.listOfLabels?.find { searchedLabel ->
                searchedLabel == Label.ASSEMBLED }?.let {
                return false
            } ?: return true
            Label.OWNED -> legoSet.listOfLabels?.find { searchedLabel ->
                searchedLabel == Label.DESIRE }?.let {
                return false
            } ?: return true
            Label.DESIRE -> legoSet.listOfLabels?.find { searchedLabel ->
                searchedLabel == Label.OWNED }?.let {
                return false
            } ?: return true
        }
    }

    private fun setupCounter(listOfSets: MutableList<LegoSet>): Pair<String, String> {
        var summaryOfBricks = 0
        listOfSets.forEach { set ->
            summaryOfBricks += set.num_parts!!
        }
        return Pair(listOfSets.size.toString(), summaryOfBricks.toString())
    }

    private fun removeFromMySets() {
        with(mySetsRecyclerViewAdapter) {
            deleteItem = { legoSet ->
                mySetsViewModel.removeFromMySets(legoSet)
                removeItem(legoSet)
                constraint_favorites_fragment.showSnackbar(legoSet.name + " " + getString(R.string.deleted_item_snack_bar_text))
            }
        }
    }

    private fun startDetailActivity(legoSet: LegoSet) =
        startActivity(DetailActivity.getIntent(requireContext(), legoSet))
}