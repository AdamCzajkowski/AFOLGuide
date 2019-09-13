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
import com.application.afol.models.LegoSet
import com.application.afol.ui.activities.DetailActivity
import com.application.afol.ui.adapters.LegoRecyclerViewAdapter
import com.application.afol.utility.gone
import com.application.afol.utility.show
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
    private lateinit var legoRecyclerViewAdapter: LegoRecyclerViewAdapter

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
        legoRecyclerViewAdapter.selectedItem = {
            singleItemClickedReaction(it)
        }
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

    private fun singleItemClickedReaction(legoSet: LegoSet) {
        startDetailActivity(legoSet)
        //startWithAnimationActivity(1, legoSet)
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        legoRecyclerViewAdapter = LegoRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        val alphaAdapter = AlphaInAnimationAdapter(legoRecyclerViewAdapter)
        recyclerView.adapter = ScaleInAnimationAdapter(alphaAdapter)
    }

    private suspend fun getAllMySets() {
        mySetsViewModel.getListOfMySets().observe(this, Observer { listOfMySets ->
            //legoRecyclerViewAdapter.swapList(listOfMySets)
            legoRecyclerViewAdapter.listOfLegoSet = listOfMySets
            if (listOfMySets.isEmpty()) default_my_sets_view.show()
            else default_my_sets_view.gone()
        })
    }

    private fun startDetailActivity(legoSet: LegoSet) {
        startActivity(DetailActivity.getIntent(context!!, legoSet))
    }
}