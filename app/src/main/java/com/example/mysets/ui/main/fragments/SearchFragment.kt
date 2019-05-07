package com.example.mysets.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.R
import com.example.mysets.models.LegoSet
import com.example.mysets.ui.main.LegoRecyclerViewAdapter
import com.example.mysets.view.model.searchViewModel.SearchLegoViewModel
import com.example.mysets.view.model.searchViewModel.SearchLegoViewModelFactory
import kotlinx.android.synthetic.main.fragment_search.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.support.kodein
import org.kodein.di.generic.instance

/**
 * A placeholder fragment containing a simple view.
 */
class SearchFragment : Fragment(), KodeinAware {

    companion object {
        @JvmStatic
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override val kodein: Kodein by kodein()

    private val searchLegoViewModelFactory: SearchLegoViewModelFactory by instance()

    private lateinit var searchLegoViewModel: SearchLegoViewModel
    private lateinit var legoRecyclerViewAdapter: LegoRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        initializeLegoViewModel()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeRecyclerView(rv_search_id)
        getSuccessRespond(view)
        getErrorRespond(view)
        getExceptionRespond(view)
        mockLego()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initializeLegoViewModel() {
        searchLegoViewModel =
            ViewModelProviders.of(this, searchLegoViewModelFactory).get(SearchLegoViewModel::class.java)
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        legoRecyclerViewAdapter = LegoRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = legoRecyclerViewAdapter
    }

    private fun getSuccessRespond(view: View) {
        searchLegoViewModel.getLegoSuccess().observe(this, Observer {
            Log.i("test", "success")
        })
    }

    private fun getErrorRespond(view: View) {
        searchLegoViewModel.getLegoError().observe(this, Observer {
            Log.i("test", "error")
        })
    }

    private fun getExceptionRespond(view: View) {
        searchLegoViewModel.getLegoException().observe(this, Observer {
            Log.i("test", "exception")
        })
    }

    private fun mockLego() {
        val lego1 = LegoSet(
            1,
            "44444",
            "title",
            2000,
            1111,
            "https://rebrickable.com/media/sets/8421-1.jpg",
            "https://rebrickable.com/sets/42070-1/6x6-all-terrain-tow-truck/"
        )
        val list = mutableListOf(lego1)
        legoRecyclerViewAdapter.swapList(list)
    }
}
