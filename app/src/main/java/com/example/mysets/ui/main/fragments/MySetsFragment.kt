package com.example.mysets.ui.main.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysets.R
import com.example.mysets.models.LegoSet
import com.example.mysets.ui.main.LegoRecyclerViewAdapter
import com.example.mysets.view.model.mySetsViewModel.MySetsViewModel
import com.example.mysets.view.model.mySetsViewModel.MySetsViewModelFactory
import kotlinx.android.synthetic.main.fragment_my_sets.*
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
        getAllMySets()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initializeLegoViewModel() {
        mySetsViewModel =
            ViewModelProviders.of(this, mySetsViewModelFactory).get(MySetsViewModel::class.java)
    }

    private fun initializeRecyclerView(recyclerView: RecyclerView) {
        legoRecyclerViewAdapter = LegoRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = legoRecyclerViewAdapter
    }

    private fun getAllMySets() {
        mySetsViewModel.getAllMySets().observe(this, Observer {
            legoRecyclerViewAdapter.swapList(it)
        })
    }
}
